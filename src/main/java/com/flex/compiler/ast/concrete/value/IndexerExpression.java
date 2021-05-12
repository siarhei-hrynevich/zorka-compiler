package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.contextAnalyzer.ContextAnalyzerUtils;
import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

public class IndexerExpression extends ValueExpression {

    private final ValueExpression pointer;
    private final ValueExpression index;

    private Type type;

    public IndexerExpression(Token token, ValueExpression pointer, ValueExpression index) {
        super(token);
        this.pointer = pointer;
        this.index = index;
    }

    @Override
    public void validate(TranslatorContext context) {
        pointer.validate(context);
        index.validate(context);
        if (!ContextAnalyzerUtils.assertTypes(SimpleTypes.getInt32Type(), index.getValidType()))
            throw new ContextException(this.token, ContextError.InvalidValueType);
        if (pointer.getValidType().getArrayDimension() == 0)
            throw new ContextException(this.token, ContextError.InvalidValueType);

        type = new Type(pointer.getValidType());
        try {
            type.decrementDimension();
        } catch (Exception e) {
            throw new ContextException(this.token, ContextError.InvalidValueType);
        }
    }

    @Override
    public void translate(Translator translator) {
        index.translate(translator);
        pointer.translate(translator);
        translator.pushIndex();
    }

    @Override
    public Type getValidType() {
        return type;
    }

    @Override
    public boolean canAssign() {
        return true;
    }
}
