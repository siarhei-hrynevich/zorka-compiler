package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

public class ArrayInstance extends ValueExpression {
    private final ValueExpression size;
    private final String typeName;
    private final int typeArrayDimension;

    private Type type;

    public ArrayInstance(Token token, ValueExpression size, String type, int typeArrayDimension) {
        super(token);
        this.size = size;
        this.typeName = type;
        this.typeArrayDimension = typeArrayDimension;
    }

    @Override
    public void validate(TranslatorContext context) {
        size.validate(context);

        type = new Type(context.getTable()
                .findType(typeName)
                .orElseThrow(() -> new ContextException(this.token, ContextError.UndeclaredType)));
        type.setArrayDimension(typeArrayDimension + 1);
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
