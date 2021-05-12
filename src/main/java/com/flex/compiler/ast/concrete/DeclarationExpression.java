package com.flex.compiler.ast.concrete;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.lexicalAnalyzer.Token;

import java.util.List;

public abstract class DeclarationExpression extends Expression {
    private List<String> modifiers;

    public DeclarationExpression(Token token) {
        super(token);
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }
}