package com.flex.compiler;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.translator.SymbolsTable;
import com.flex.compiler.translator.TranslatorContext;

import java.util.List;

public class CompilerContext {

    private List<Expression> ast;
    private final TranslatorContext translatorContext = new TranslatorContext();

    public List<Expression> getAst() {
        return ast;
    }

    public void setAst(List<Expression> ast) {
        this.ast = ast;
    }


    public TranslatorContext getTranslatorContext() {
        return translatorContext;
    }

}
