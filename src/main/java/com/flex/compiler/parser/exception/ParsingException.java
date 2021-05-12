package com.flex.compiler.parser.exception;

import com.flex.compiler.lexicalAnalyzer.Token;

import java.util.HashMap;
import java.util.Map;

public class ParsingException extends RuntimeException {

    private static Map<Error, String> messages = new HashMap<>() {{
        put(Error.UnexpectedToken, "Unexpected token");
        put(Error.UnrecognizedOperation, "Unrecognized operation");
        put(Error.NeedIdentifier, "Need identifier");
        put(Error.NeedTypeName, "Expected type name");
        put(Error.UnexpectedEnd, "Unexpected end of file");
        put(Error.ExpectedVariable, "Expected variable");
        put(Error.ExpectedValue, "Expected value");
        put(Error.ExpectedAssignment, "Expected assignment");
    }};


    private final Error errorCode;
    private final Token token;
    private final String[] args;

    public ParsingException(Token token, Error errorCode, String... args) {
        this.errorCode = errorCode;
        this.token = token;
        this.args = args;
    }

    public Error getErrorCode() {
        return errorCode;
    }

    public Token getToken() {
        return token;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public String getMessage() {
        String message = messages.get(errorCode);
        for (int i = 0; i < args.length; i++) {
            message = String.format(message, args[i]);
        }
        return message;
    }
}
