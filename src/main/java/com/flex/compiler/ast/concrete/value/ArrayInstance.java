package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

public class ArrayInstance extends ValueExpression {
    private final ValueExpression size;
    private final String typeName;

    private Type type;

    public ArrayInstance(ValueExpression size, String type) {
        this.size = size;
        this.typeName = type;
    }

    @Override
    public void validate(TranslatorContext context) {
        size.validate(context);

        type = new Type(context.getTable()
                .findType(typeName)
                .orElseThrow(() -> new ContextException(ContextError.UndeclaredType)));
        type.incrementDimension();
    }

    @Override
    public void translate(Translator translator) {
        size.translate(translator);
        translator.pushArrayInstantiation(type);
    }

    @Override
    public Type getValidType() {
        return new Type(type);
    }

    @Override
    public boolean canAssign() {
        return false;
    }
}
