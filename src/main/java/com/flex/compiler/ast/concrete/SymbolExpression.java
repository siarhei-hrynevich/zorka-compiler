package com.flex.compiler.ast.concrete;

import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.parser.Symbol;

import java.util.List;

public abstract class SymbolExpression extends ValueExpression {

    private Symbol symbol;

    public SymbolExpression(Symbol symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return symbol.name;
    }

    public List<String> getLocation() {
        return symbol.location;
    }
}
