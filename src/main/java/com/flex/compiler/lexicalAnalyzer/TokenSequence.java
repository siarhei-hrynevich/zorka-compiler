package com.flex.compiler.lexicalAnalyzer;

import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;

import java.util.List;

public class TokenSequence {
    public List<Token> tokens;
    public int counter;

    public Token getCurrent() {
        return tokens.get(counter);
    }

    public boolean isLast() {
        return tokens.size() - 1 == counter;
    }

    public Token next() {
        if(++counter == tokens.size())
            throw new ParsingException(tokens.get(counter - 1), Error.UnexpectedEnd);
        return tokens.get(counter);
    }

    public Token peekNext() {
        return counter + 1 == tokens.size() ? null : tokens.get(counter + 1);
    }
}
