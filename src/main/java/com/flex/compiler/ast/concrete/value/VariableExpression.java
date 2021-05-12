package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.ast.concrete.SymbolExpression;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.parser.Symbol;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;

import java.util.List;

public class VariableExpression extends SymbolExpression {

    Variable variable;

    public VariableExpression(Token token, Symbol symbol) {
        super(token, symbol);
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }


    @Override
    public void validate(TranslatorContext context) {
        Variable var = context.getCurrentScope().findVariable(getName());
        if (var == null)
            var = context.getTable()
                    .findVariable(getName())
                    .orElseThrow(() -> new ContextException(this.token, ContextError.SymbolNotExistInPackage));
        this.variable = var;
    }

    @Override
    public void translate(Translator translator) {
        translator.pushVariable(variable);
    }

    @Override
    public Type getValidType() {
        return variable.getType();
    }

    @Override
    public boolean canAssign() {
        return !variable.isConst();
    }
}
