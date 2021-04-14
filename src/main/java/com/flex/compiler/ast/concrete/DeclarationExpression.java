package com.flex.compiler.ast.concrete;

import com.flex.compiler.ast.Expression;

import java.util.List;

public abstract class DeclarationExpression implements Expression {
    private List<String> modifiers;

    public List<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }
}