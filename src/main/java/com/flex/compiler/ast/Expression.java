package com.flex.compiler.ast;

import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;

public interface Expression {

    void validate(TranslatorContext context);

    void translate(Translator translator);
}

