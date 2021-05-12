package com.flex.compiler.ast.concrete.keywords;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.utils.FileUtils;

import java.util.List;

public class ImportExpression extends Expression {
    private List<String> path;
    private String symbol;

    public ImportExpression(Token token, List<String> path, String symbol) {
        super(token);
        this.path = path;
        this.symbol = symbol;
    }

    @Override
    public void validate(TranslatorContext context) {

    }

    @Override
    public void translate(Translator translator) {
        translator.include(FileUtils.createPath(path));
    }

    public List<String> getPath() {
        return path;
    }

    public String getSymbol() {
        return symbol;
    }
}
