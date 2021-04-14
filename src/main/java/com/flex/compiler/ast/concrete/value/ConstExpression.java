package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

public class ConstExpression extends ValueExpression {
    private String value;

    private boolean isChar;
    private boolean isFloat;
    private boolean isInt;
    private boolean isString;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChar() {
        return isChar;
    }

    public void setChar(boolean aChar) {
        isChar = aChar;
    }

    public boolean isFloat() {
        return isFloat;
    }

    public void setFloat(boolean aFloat) {
        isFloat = aFloat;
    }

    public boolean isInt() {
        return isInt;
    }

    public void setInt(boolean anInt) {
        isInt = anInt;
    }

    public ConstExpression(String value) {
        this.value = value;
    }

    @Override
    public void validate(TranslatorContext context) {

    }

    @Override
    public void translate(Translator translator) {
        translator.pushConstValue(value);
    }

    public boolean isString() {
        return isString;
    }

    public void setString(boolean string) {
        isString = string;
    }

    @Override
    public Type getValidType() {
        if (isString)
            return SimpleTypes.getStringType();
        if (isChar)
            return SimpleTypes.getCharType();
        if (isFloat)
            return SimpleTypes.getFloat32Type();
        if (isInt)
            return SimpleTypes.getInt32Type();
        throw new ContextException(ContextError.UnexpectedExpression);
    }

    @Override
    public boolean canAssign() {
        return false;
    }
}
