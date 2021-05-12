package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.symbols.Type;

public abstract class ValueExpression extends Expression {
    public ValueExpression(Token token) {
        super(token);
    }

    public abstract Type getValidType();

    public abstract boolean canAssign();
}
