package com.flex.compiler.translator;

import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;

import java.util.ArrayList;
import java.util.List;

public class Scope {
    private final List<Scope> children = new ArrayList<>();

    private final Scope parent;

    private Type returnValue;

    private final List<Variable> variables = new ArrayList<>();

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Scope addChild() {
        Scope scope = new Scope(this);
        children.add(scope);
        return scope;
    }

    public Scope getParent() {
        return parent;
    }


    public Variable findVariable(String name) {
        Variable variable = findInScope(name);
        if (variable != null)
            return variable;
        return parent != null ? parent.findVariable(name) : null;
    }

    public void addVariable(Variable var) {
        if (findInScope(var.getName()) != null)
            throw new ContextException(ContextError.SymbolExistInScope);
        variables.add(var);
    }

    private Variable findInScope(String name) {
        for (Variable var :
                variables) {
            if (var.getName().equals(name))
                return var;
        }
        return null;
    }

    public Type getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Type returnValue) {
        this.returnValue = returnValue;
        if (returnValue == null) {
            this.returnValue = SimpleTypes.getVoidType();
        }
    }
}
