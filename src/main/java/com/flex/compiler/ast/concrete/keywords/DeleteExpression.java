package com.flex.compiler.ast.concrete.keywords;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

public class DeleteExpression extends Expression {

    private ValueExpression value;

    public DeleteExpression(Token token, ValueExpression value) {
        super(token);
        this.value = value;
    }

    @Override
    public void validate(TranslatorContext context) {
        value.validate(context);
        Type valueType = value.getValidType();
        if (valueType.getArrayDimension() == 0) {
            valueType.incrementDimension();
            throw new ContextException(this.token, ContextError.InvalidValueType, valueType);
        }
    }

    @Override
    public void translate(Translator translator) {
        value.translate(translator);
        translator.delete();
    }
}
