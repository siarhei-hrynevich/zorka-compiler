package com.flex.compiler.translator.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Variable extends Symbol {

    private Type type;
    private List<VariableModifier> modifiers = new ArrayList<>();

    public Variable() {}

    public Variable(Type type, String name) {
        this.name = name;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<VariableModifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<VariableModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public void addModifier(VariableModifier modifier) {
        modifiers.add(modifier);
    }

    public boolean hasModifier(VariableModifier modifier) {
        return modifiers.contains(modifier);
    }

    public boolean isConst() {
        return hasModifier(VariableModifier.Const);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Variable variable = (Variable) o;
        return Objects.equals(type, variable.type);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), type);
        result = 31 * result;
        return result;
    }
}
