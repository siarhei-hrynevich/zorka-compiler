package com.flex.compiler.contextAnalyzer.exception;

import com.flex.compiler.lexicalAnalyzer.Token;

import java.util.HashMap;
import java.util.Map;

public class ContextException extends RuntimeException {
    private static Map<ContextError, String> messages = new HashMap<>() {{
        put(ContextError.FileNotFound, "File %s not exist");
        put(ContextError.SymbolExistInScope, "Symbol (%s) already exist in this package");
        put(ContextError.SymbolNotExistInPackage, "Symbol (%s) not exist in this package");
        put(ContextError.InvalidLeftValue, "Invalid left value (expected %s)");
        put(ContextError.InvalidRightValue, "Invalid right value (right value can be mutable variable)");
        put(ContextError.UnexpectedExpression, "Unexpected expression");
        put(ContextError.InvalidArgs, "Invalid args in function %s");
        put(ContextError.InvalidValueType, "Invalid value type (expected: %s)");
        put(ContextError.UndeclaredType, "Undeclared type (%s)");
        put(ContextError.FieldIsNotExist, "Field (%s) not exist");
    }};

    private Token token;
    private Object[] args;
    private ContextError error;

    public ContextException(ContextError error, Object ...args) {
        this.error = error;
        this.args = args;
    }

    public ContextException(Token token, ContextError error, Object ...args) {
        this.token = token;
        this.error = error;
        this.args = args;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String getMessage() {
        String message = messages.get(error);
        for (int i = 0; i < args.length; i++) {
            message = String.format(message, args[i]);
        }
        return message;
    }

    public Token getToken() {
        return token;
    }
}
