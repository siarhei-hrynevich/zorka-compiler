package com.flex.compiler;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.contextAnalyzer.DeclarationAnalyzer;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.lexicalAnalyzer.Tokenizer;
import com.flex.compiler.parser.Parser;
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

    private static File openFile(List<String> location) throws FileNotFoundException {
        String filePath = FileUtils.createPath(location);
        File file = new File(filePath);
        if (file.exists())
            return file;
        String sourcePath = sourceLocations.stream()
                .filter(path -> new File(path + filePath).exists())
                .findFirst().orElseThrow(FileNotFoundException::new);

        return new File(sourcePath + filePath);
    }

    private static CompilerContext createContext(List<String> location) throws Exception {
        File file = openFile(location);
        CompilerContext context;
        TokenSequence tokens = new TokenSequence();
        tokens.tokens = Tokenizer.tokenize(file);

        List<Expression> expressions = Parser.parse(tokens);

        context = new CompilerContext();
        context.setAst(expressions);
        context.getTranslatorContext().setTable(new SymbolsTable(location));

        analyzeDeclarationsInPackage(context);
        SymbolsStorage.addTable(context.getTranslatorContext().getTable());
        DeclarationAnalyzer.validateImports(context.getAst(), context.getTranslatorContext().getTable());
        return context;
    }

    public static SymbolsTable loadFile(List<String> location) throws Exception {
        CompilerContext context = loadedFiles.get(location);
        if (context == null) {
            context = createContext(location);
            loadedFiles.put(FileUtils.createPath(location), context);
        }
        return context.getTranslatorContext().getTable();
    }

    private static void analyzeDeclarationsInPackage(CompilerContext context) throws Exception {
        DeclarationAnalyzer.analyzeTypes(context.getAst(), context.getTranslatorContext().getTable());
        DeclarationAnalyzer.analyzeFunctions(context.getAst(), context.getTranslatorContext().getTable());
        DeclarationAnalyzer.analyzeVars(context.getAst(), context.getTranslatorContext().getTable());
    }

    public static void compile(String source, String out) throws Exception {
        try {
            compileAndThrows(source, out);
        } catch (Exception e) {
            throw e;
        }
//        } catch (ParsingException e) {
//            System.out.println(e.getMessage());
//        } catch (ContextException e) {
//            System.out.println(e.getMessage());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

    private static void compileAndThrows(String source, String out) throws Exception {
        List<String> location = Arrays.asList(source.split("\\\\"));
        location.set(location.size() - 1, FileUtils.changeExtension(location.get(location.size() - 1), ""));
        loadFile(location);
        for (Map.Entry<String, CompilerContext> entry : loadedFiles.entrySet()) {
            CompilerContext context = entry.getValue();
            context.getAst().forEach(expression -> expression.validate(context.getTranslatorContext()));
            //ScopeAnalyzer analyzer = new ScopeAnalyzer(context.getTranslatorContext());
            //analyzer.validate(context.getAst());

            List<Expression> ast = context.getAst();
            Translator translator = new TranslatorC(context.getTranslatorContext().getTable().getLocation(), out);
            ast.forEach(e -> { e.translate(translator); translator.nextOperation(); });
            translator.translate();
        }
    }

    public static void addDefaultSourceLocation(String location) {
        if (!location.endsWith("\\"))
            location += '\\';
        sourceLocations.add(location);
    }
}
