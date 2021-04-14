package com.flex.compiler.ast.concrete.keywords;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.contextAnalyzer.ContextAnalyzerUtils;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;

public class ReturnExpression implements Expression {
    public ReturnExpression(ValueExpression value) {
        this.value = value;
    }

    public ValueExpression getValue() {
        return value;
    }

    public void setValue(ValueExpression value) {
        this.value = value;
    }

    private ValueExpression value;

    @Override
    public void validate(TranslatorContext context) {
        if (context.getCurrentScope().getReturnValue().isVoid() && value != null)
            throw new ContextException(ContextError.UnexpectedExpression);
        if (!context.getCurrentScope().getReturnValue().isVoid()) {
            value.validate(context);
            ContextAnalyzerUtils.assertTypes(value.getValidType(), context.getCurrentScope().getReturnValue());
        }
    }

    @Override
    public void translate(Translator translator) {
        value.translate(translator);
        translator.returnOperation();
    }
}
