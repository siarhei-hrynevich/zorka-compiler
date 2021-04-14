package com.flex.compiler.parser.exception;

import com.flex.compiler.lexicalAnalyzer.Token;

public class ParsingException extends RuntimeException {

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
}
