package com.flex.compiler.translator.symbols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Function extends Symbol {

    private Type returnValue;
    private List<Variable> args;
    private List<FunctionModifier> modifiers = new ArrayList<>();


    public Type getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Type returnValue) {
        this.returnValue = returnValue;
    }

    public List<Variable> getParams() {
        return args;
    }

    public void setArgs(List<Variable> args) {
        this.args = args;
    }

    public List<FunctionModifier> getModifiers() {
        return modifiers;
    }

    public void addModifier(FunctionModifier modifier) {
        modifiers.add(modifier);
    }

    public void setModifiers(List<FunctionModifier> modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Function function = (Function) o;
        return Objects.equals(returnValue, function.returnValue) &&
                Objects.equals(args, function.args);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), returnValue, args);
        result = 31 * result;
        return result;
    }

    public String getExtendedName() {
        if (modifiers.contains(FunctionModifier.Native))
            return name;
        int hash = Objects.hash(args);
        return name + (hash < 0 ? '_' + String.valueOf(-hash) : String.valueOf(hash)) ;
    }

    public boolean hasModifier(FunctionModifier modifier) {
        return modifiers.contains(modifier);
    }
}
