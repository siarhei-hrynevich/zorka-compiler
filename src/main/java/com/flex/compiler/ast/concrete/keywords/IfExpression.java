package com.flex.compiler.ast.concrete.keywords;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.BlockExpression;
import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.contextAnalyzer.ContextAnalyzerUtils;
import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;

public class IfExpression implements Expression {

    private ValueExpression predicate;
    private BlockExpression thenBody;
    private BlockExpression elseBody;

    public IfExpression(ValueExpression predicate, BlockExpression thenBody, BlockExpression elseBody) {
        this.predicate = predicate;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public void validate(TranslatorContext context) {
        predicate.validate(context);
        if (!ContextAnalyzerUtils.assertTypes(predicate.getValidType(), SimpleTypes.getBoolType()))
            throw new ContextException(ContextError.InvalidValueType);
        thenBody.validate(context);
        if (elseBody != null)
            elseBody.validate(context);
    }

    @Override
    public void translate(Translator translator) {
        predicate.translate(translator);
        translator.ifStatement();
        thenBody.translate(translator);
        if (elseBody != null) {
            translator.elseStatement();
            elseBody.translate(translator);
        }
    }

    public ValueExpression getPredicate() {
        return predicate;
    }

    public BlockExpression getThenBody() {
        return thenBody;
    }

    public BlockExpression getElseBody() {
        return elseBody;
    }
}
