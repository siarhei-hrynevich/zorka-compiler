package com.flex.compiler.ast.concrete;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;

public class OperationExpression implements Expression {
    private final Expression expression;

    protected OperationExpression(Expression expression) {
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
