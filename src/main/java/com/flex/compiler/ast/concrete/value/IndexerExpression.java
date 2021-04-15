package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.contextAnalyzer.ContextAnalyzerUtils;
import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

public class IndexerExpression extends ValueExpression {

    private final ValueExpression pointer;
    private final ValueExpression index;

    private Type type;

    public IndexerExpression(ValueExpression pointer, ValueExpression index) {
        this.pointer = pointer;
        this.index = index;
    }

    @Override
    public void validate(TranslatorContext context) {
        pointer.validate(context);
        index.validate(context);
        if (!ContextAnalyzerUtils.assertTypes(SimpleTypes.getInt32Type(), index.getValidType()))
            throw new ContextException(ContextError.InvalidValueType);
        if (pointer.getValidType().getArrayDimension() == 0)
            throw new ContextException(ContextError.InvalidValueType);

        type = new Type(pointer.getValidType());
        type.decrementDimension();
    }

    @Override
    public void translate(Translator translator) {
        pointer.translate(translator);
        index.translate(translator);
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
