package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

public class SizeOfArray extends ValueExpression {
    private ValueExpression array;

    public SizeOfArray(Token token, ValueExpression array) {
        super(token);
        this.array = array;
    }

    @Override
    public Type getValidType() {
        return SimpleTypes.getInt32Type();
    }

    @Override
    public boolean canAssign() {
        return false;
    }

    @Override
    public void validate(TranslatorContext context) {
        array.validate(context);
        if (array.getValidType().getArrayDimension() == 0)
            throw new ContextException(this.token, ContextError.InvalidValueType);
    }

    @Override
    public void translate(Translator translator) {
        array.translate(translator);
        translator.pushSizeOfArray();
    }
}
