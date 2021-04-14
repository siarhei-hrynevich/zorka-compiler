package com.flex.compiler.contextAnalyzer.exception;

import java.util.HashMap;
import java.util.Map;

public class ContextException extends RuntimeException {
    private static Map<ContextError, String> messages = new HashMap<>() {{

    }};

    public ContextException(ContextError error, Object ...args) {
        //super(String.format(messages.get(error), args));
    }
}
