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

public class WhileExpression implements Expression {

    private BlockExpression body;
    private ValueExpression predicate;

    public WhileExpression(BlockExpression body, ValueExpression predicate) {
        this.body = body;
        this.predicate = predicate;
    }

    @Override
    public void validate(TranslatorContext context) {
        predicate.validate(context);
        if (!ContextAnalyzerUtils.assertTypes(predicate.getValidType(), SimpleTypes.getBoolType()))
            throw new ContextException(ContextError.InvalidValueType);
        body.validate(context);
    }

    @Override
    public void translate(Translator translator) {
        predicate.translate(translator);
        translator.whileStatement();
        body.translate(translator);
    }

    public BlockExpression getBody() {
        return body;
    }

    public ValueExpression getPredicate() {
        return predicate;
    }
}
