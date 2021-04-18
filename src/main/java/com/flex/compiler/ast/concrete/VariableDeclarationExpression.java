package com.flex.compiler.ast.concrete;

import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.contextAnalyzer.ContextAnalyzerUtils;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;

public class VariableDeclarationExpression extends DeclarationExpression {

    private Variable variable;

    private final String type;
    private final String name;

    private ValueExpression initializer;
    private int dimension;


    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public VariableDeclarationExpression(String type, String name, ValueExpression initializer) {
        this.type = type;
        this.name = name;
        this.initializer = initializer;
    }

    public void setInitializer(ValueExpression initializer) {
        this.initializer = initializer;
    }

    @Override
    public void validate(TranslatorContext context) {
        variable = ContextAnalyzerUtils.analyzeVariable(this, context.getTable());
        if (!variable.getType().isDeclared())
            throw new ContextException(ContextError.UndeclaredType);
        if (initializer != null) {
            initializer.validate(context);
            Type initType = initializer.getValidType();
            if (!ContextAnalyzerUtils.assertTypes(variable.getType(), initType))
                throw new ContextException(ContextError.InvalidRightValue);
        }
        context.getCurrentScope().addVariable(variable);
    }

    @Override
    public void translate(Translator translator) {
        if (initializer != null)
            initializer.translate(translator);
        translator.variableDeclaration(variable, initializer != null);
    }

    public ValueExpression getInitializer() {
        return initializer;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
}
