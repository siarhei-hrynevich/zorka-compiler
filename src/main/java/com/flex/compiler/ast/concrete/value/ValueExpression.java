package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.translator.symbols.Type;

public abstract class ValueExpression implements Expression {
    public abstract Type getValidType();

    public abstract boolean canAssign();
}
