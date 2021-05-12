package com.flex.compiler;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.contextAnalyzer.DeclarationAnalyzer;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.lexicalAnalyzer.Tokenizer;
import com.flex.compiler.parser.Parser;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.translator.SymbolsTable;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorC;
import com.flex.compiler.utils.FileUtils;
import com.flex.compiler.utils.SymbolsStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Compiler {

    private static final List<String> sourceLocations = new ArrayList<>();

    private static final Map<String, CompilerContext> loadedFiles = new HashMap<>();

    private static File openFile(List<String> location) {
        String filePath = FileUtils.createPath(location);
        File file = new File(filePath);
        if (file.exists())
            return file;
        String sourcePath = sourceLocations.stream()
                .filter(path -> new File(path + filePath).exists())
                .findFirst().orElseThrow(() -> new ContextException(ContextError.FileNotFound, filePath));

        return new File(sourcePath + filePath);
    }

    private static CompilerContext createContext(List<String> location) throws Exception {
        File file = openFile(location);
        CompilerContext context = null;
        TokenSequence tokens = new TokenSequence();

        try {
            tokens.tokens = Tokenizer.tokenize(file);
            List<Expression> expressions = Parser.parse(tokens);
            context = new CompilerContext();
            context.setAst(expressions);
            context.getTranslatorContext().setTable(new SymbolsTable(location));
            analyzeDeclarationsInPackage(context);
            SymbolsStorage.addTable(context.getTranslatorContext().getTable());
            DeclarationAnalyzer.validateImports(context.getAst(), context.getTranslatorContext().getTable());

        } catch (ContextException e) {
            String fileName = FileUtils.createPath(context.getTranslatorContext().getTable().getLocation());
            showError(fileName, e.getToken(), e.getMessage());
        } catch (ParsingException e) {
            String fileName = FileUtils.createPath(context.getTranslatorContext().getTable().getLocation());
            showError(fileName, e.getToken(), e.getMessage());
        }
        return context;
    }

    public static SymbolsTable loadFile(List<String> location) throws Exception {
        CompilerContext context = loadedFiles.get(location);
        if (context == null) {
            context = createContext(location);
            if (context == null)
                return null;
            loadedFiles.put(FileUtils.createPath(location), context);
        }
        return context.getTranslatorContext().getTable();
    }

    private static void analyzeDeclarationsInPackage(CompilerContext context) {
        DeclarationAnalyzer.analyzeTypes(context.getAst(), context.getTranslatorContext().getTable());
        DeclarationAnalyzer.analyzeFunctions(context.getAst(), context.getTranslatorContext().getTable());
        DeclarationAnalyzer.analyzeVars(context.getAst(), context.getTranslatorContext().getTable());
    }

    private static void showError(String file, Token token, String message) {
        System.out.println("Error:");
        if (file != null)
            System.out.printf("In file: %s\n", file);
        if (token != null)
            System.out.printf("In position: line: %s at: %s \n", token.line, token.start);
        System.out.println(message);
    }

    public static void compile(String source, String out) throws Exception {
        try {
            compileAndThrows(source, out);
        } catch (ContextException e) {
//            showError(e.getToken(), e.getMessage());
        } catch (ParsingException e) {
  //          showError(e.getToken(), e.getMessage());
        } catch (Exception e) { }
    }

    private static void compileAndThrows(String source, String out) throws Exception {
        source = FileUtils.removeExtension(source);
        List<String> location = Arrays.asList(source.split("\\\\"));
        if (loadFile(location) == null)
            return;
        for (Map.Entry<String, CompilerContext> entry : loadedFiles.entrySet()) {
            CompilerContext context = entry.getValue();
            try {
                context.getAst().forEach(expression -> expression.validate(context.getTranslatorContext()));
            } catch (ContextException e) {
                String file = FileUtils.createPath(context.getTranslatorContext().getTable().getLocation());
                showError(file, e.getToken(), e.getMessage());
            }
            List<Expression> ast = context.getAst();
            Translator translator = new TranslatorC(context.getTranslatorContext().getTable().getLocation(), out);
            ast.forEach(e -> { e.translate(translator); translator.nextOperation(); });
            translator.translate();
        }
        FileUtils.copyFile("_lang_utils.c", out + "\\_lang_utils.c");
        FileUtils.copyFile("_lang_utils.h", out + "\\_lang_utils.h");
    }

    public static void addDefaultSourceLocation(String location) {
        if (!location.endsWith("\\"))
            location += '\\';
        sourceLocations.add(location);
    }
}
