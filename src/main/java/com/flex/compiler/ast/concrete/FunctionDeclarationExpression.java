package com.flex.compiler.ast.concrete;

import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Function;
import com.flex.compiler.translator.symbols.FunctionModifier;

import java.util.List;

public class FunctionDeclarationExpression extends DeclarationExpression {

    private String type;
    private String name;
    private List<VariableDeclarationExpression> args;
    private BlockExpression implementation;
    private Function function;


    public FunctionDeclarationExpression(String type, String name, List<VariableDeclarationExpression> args) {
        this.type = type;
        this.name = name;
        this.args = args;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<VariableDeclarationExpression> getArgs() {
        return args;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArgs(List<VariableDeclarationExpression> args) {
        this.args = args;
    }

    public BlockExpression getImplementation() {
        return implementation;
    }

    public void setImplementation(BlockExpression implementation) {
        this.implementation = implementation;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public void validate(TranslatorContext context) {
        context.createScope();
        context.getCurrentScope().setReturnValue(function.getReturnValue());
        function.getParams().forEach(arg -> context.getCurrentScope().addVariable(arg));
        if (!function.hasModifier(FunctionModifier.Extern)) {
            implementation.setReturnType(function.getReturnValue());
            implementation.validate(context);
        }
        context.popScope();
    }

    @Override
    public void translate(Translator translator) {
        translator.functionDeclaration(function);
        if (implementation != null)
            implementation.translate(translator);
    }
}
