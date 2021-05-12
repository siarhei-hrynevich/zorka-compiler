package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.ast.concrete.SymbolExpression;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.parser.Symbol;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Function;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FunctionCallExpression extends SymbolExpression {
    private List<ValueExpression> args;
    private Function function;

    public FunctionCallExpression(Token token, Symbol symbol, List<ValueExpression> args) {
        super(token, symbol);
        this.args = args;
    }

    private boolean isValidArgs(Function function, List<Type> argTypes) {
        List<Variable> functionParams = function.getParams();
        if (functionParams.size() != argTypes.size())
            return false;
        for (int i = 0; i < functionParams.size(); i++) {
            if (!functionParams.get(i).getType().equals(argTypes.get(i)))
                return false;
        }
        return true;
    }
    @Override
    public void validate(TranslatorContext context) {
        if (function != null)
            return;
        List<Function> functions = context.getTable().findFunctions(getName());
        args.forEach(e -> e.validate(context));
        List<Type> argTypes = getArgs()
                .stream()
                .map(ValueExpression::getValidType)
                .collect(Collectors.toList());
        Optional<Function> function = functions
                .stream()
                .filter(e -> isValidArgs(e, argTypes)).findFirst();
        setFunction(function.orElseThrow(() -> new ContextException(this.token, ContextError.InvalidArgs, getName())));
    }

    @Override
    public void translate(Translator translator) {
        for (ValueExpression e : args) {
            e.translate(translator);
        }
        translator.pushFunctionCall(function);
    }

    public List<ValueExpression> getArgs() {
        return args;
    }

    public void setArgs(List<ValueExpression> args) {
        this.args = args;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public Type getValidType() {
        return function.getReturnValue();
    }

    @Override
    public boolean canAssign() {
        return false;
    }
}