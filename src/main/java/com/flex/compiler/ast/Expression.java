package com.flex.compiler.ast;

import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;

public abstract class Expression {
    protected Token token;

    public Expression(Token token) {
        this.token = token;
    }

    public abstract void validate(TranslatorContext context);

    public abstract void translate(Translator translator);
}

