package com.flex.compiler.translator;


import com.flex.compiler.lexicalAnalyzer.LexerUtils;
import com.flex.compiler.lexicalAnalyzer.Operator;
import com.flex.compiler.translator.symbols.Function;
import com.flex.compiler.translator.symbols.FunctionModifier;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;
import com.flex.compiler.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TranslatorC implements Translator {

    private final List<String> location;
    private final String out;

    private final List<String> values = new LinkedList<>();

    private final StringBuilder includes = new StringBuilder();
    private final StringBuilder declarations = new StringBuilder();
    private final StringBuilder body = new StringBuilder();
    private boolean needSeparator = true;

    //    private StringBuilder header = new StringBuilder();
    private int scopesCount = 0;

    public TranslatorC(List<String> location, String out) {
        this.location = location;
        if (!out.endsWith(File.separator))
            this.out = out + File.separator;
        else
            this.out = out;
        File dir = new File(out);
        if (!dir.exists())
            dir.mkdir();

        include("_lang_utils.h");
    }

    @Override
    public void pushConstValue(String value) {
        values.add(value);
    }

    @Override
    public void pushVariable(Variable var) {
        values.add(var.getName());
    }

    @Override
    public void pushFunctionCall(Function function) {
        StringBuilder builder = new StringBuilder();
        builder.append(function.getExtendedName());
        builder.append("(");
        List<String> args = new ArrayList<>(values.subList(values.size() - function.getParams().size(), values.size()));
        for (int i = 0; i < args.size(); i++) {
            builder.append(args.get(i));
            if (i < args.size() - 1)
                builder.append(", ");
            values.remove(args.get(i));
        }
        builder.append(")");
        values.add(builder.toString());
    }

    @Override
    public void pushIndex() {
        String pointerValue = popValue();
        String indexValue = popValue();
        values.add(String.format("%s[%s]", pointerValue, indexValue));
    }

    @Override
    public void pushArrayInstantiation(Type arrayType) {
        String arraySize = popValue();
        values.add(String.format("create_array(%s, sizeof(%s))",
                arraySize,
                (arrayType.isSimple() ? "" : "struct ")
                        + TranslatorCUtils.getTypeName(arrayType)
                        + "*".repeat(Math.max(0, arrayType.getArrayDimension() - 1))));
    }

    @Override
    public void pushSizeOfArray() {
        String array = popValue();
        values.add(String.format("*((int*)%s - 1)", array));
    }

    @Override
    public void pushField(Type type, String fieldName) {
        String struct = popValue();
        values.add(String.format("%s.%s", struct, fieldName));
    }

    @Override
    public void operation(Operator operator) {
        String right = popValue();
        String left = popValue();
        values.add(String.format("(%s %s %s)", left, LexerUtils.getOperatorName(operator), right));
    }

    @Override
    public void variableDeclaration(Variable var, boolean needInit) {
        addPaddings(body);

        StringBuilder builder = new StringBuilder();
        translateType(var.getType(), builder);
        builder.append(var.getName());

        if (needInit) {
            builder.append(' ');
            builder.append(LexerUtils.getOperatorName(Operator.Assignment));
            builder.append(' ');
            builder.append(popValue());
        }
        builder.append(";\n");
        needSeparator = false;
        if (scopesCount == 0)
            declarations.append(builder);
        else
            body.append(builder);
    }

    @Override
    public void functionDeclaration(Function function) {
        StringBuilder builder = new StringBuilder();
        translateType(function.getReturnValue(), builder);
        builder.append(function.getExtendedName());
        builder.append("(");
        List<Variable> args = function.getParams();
        for (int i = 0; i < args.size(); i++) {
            translateType(args.get(i).getType(), builder);
            builder.append(args.get(i).getName());
            if (i < args.size() - 1)
                builder.append(", ");
        }
        builder.append(")");

        declarations.append(builder.toString());
        declarations.append(";\n");

        if (!function.hasModifier(FunctionModifier.Extern))
            body.append(builder.toString());
    }

    @Override
    public void include(String location) {
        includes.append(String.format("#include<%s>\n", FileUtils.changeExtension(location, ".h")));
        needSeparator = false;
    }

    @Override
    public void nextOperation() {

        if (values.size() != 0) {
            addPaddings(body);
            body.append(popValue());
        }

        if (needSeparator && !body.isEmpty())
            body.append(";\n");
        needSeparator = true;
    }

    @Override
    public void createScope() {
        body.append(" {\n");
        scopesCount++;
    }

    @Override
    public void popScope() {
        scopesCount--;
        addPaddings(body);
        body.append("}\n");
        needSeparator = false;
    }

    @Override
    public void returnOperation() {
        addPaddings(body);
        body.append("return ");
        body.append(popValue());
        body.append(";\n");
        needSeparator = false;
    }

    @Override
    public void whileStatement() {
        addPaddings(body);
        body.append(String.format("while(%s)", popValue()));
    }

    @Override
    public void ifStatement() {
        addPaddings(body);
        body.append(String.format("if(%s)", popValue()));
    }

    @Override
    public void elseStatement() {
        body.append("else");
    }

    @Override
    public void struct(Type type) {
        addPaddings(declarations);
        declarations.append("struct ");
        declarations.append(type.getName());
        declarations.append(" {\n");
        scopesCount++;
        List<Variable> fields = type.getFields();
        for (int i = 0; i < fields.size(); i++) {
            addPaddings(declarations);
            translateType(fields.get(i).getType(), declarations);
            declarations.append(fields.get(i).getName());
            declarations.append(";\n");
        }
        scopesCount--;
        declarations.append("};\n");
    }

    @Override
    public void translate() {
        include(FileUtils.createPath(location));
        FileUtils.createFolders(location, out);
        String path = FileUtils.createPath(location);
        String sourcePath = out + FileUtils.changeExtension(path, ".c");
        String headerPath = out + FileUtils.changeExtension(path, ".h");

        if (!body.isEmpty()) {
            File sourceFile = new File(sourcePath);
            if (!sourceFile.exists()) {
                try {
                    sourceFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try (FileWriter writer = new FileWriter(sourceFile)) {
                writer.write(includes.toString());
                writer.write(body.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        File headerFile = new File(headerPath);
        if (!headerFile.exists()) {
            try {
                headerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileWriter writer = new FileWriter(headerFile)) {
            String define = headerPath.replace(File.separatorChar, '_').replace('.', '_');

            writer.write("#ifndef ");
            writer.write(define);
            writer.write("\n#define ");
            writer.write(define);
            writer.write('\n');
            writer.write(includes.toString());
            writer.write(declarations.toString());
            writer.write("#endif");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete() {
        String value = popValue();
        addPaddings(body);
        body.append(String.format("delete_array(%s)", value));
    }

    private String popValue() {
        String value = values.get(values.size() - 1);
        values.remove(values.size() - 1);
        return value;
    }

    private void addPaddings(StringBuilder builder) {
        builder.append("\t".repeat(Math.max(0, scopesCount)));
    }

    private void translateType(Type type, StringBuilder builder) {
        if (!type.isSimple())
            builder.append("struct ");
        builder.append(TranslatorCUtils.getTypeName(type));
        builder.append("*".repeat(type.getArrayDimension()));
        builder.append(' ');
    }
}
