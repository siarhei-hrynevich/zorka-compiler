package com.flex.compiler.ast.concrete;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;

public class OperationExpression extends Expression {
    private final Expression expression;

    protected OperationExpression(Token token, Expression expression) {
        super(token);
        this.expression = expression;
    }

    @Override
    public void validate(TranslatorContext context) {
        expression.validate(context);
    }

    @Override
    public void translate(Translator translator) {
        expression.translate(translator);
    }
}
