package com.flex.compiler.ast.concrete.keywords;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.VariableDeclarationExpression;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

import java.util.List;

public class StructExpression extends Expression {

    private List<VariableDeclarationExpression> fields;
    private String name;
    private Type type;
    private List<String> modifiers;

    public StructExpression(Token token, List<VariableDeclarationExpression> fields, String name) {
        super(token);
        this.fields = fields;
        this.name = name;
    }

    @Override
    public void validate(TranslatorContext context) {

    }

    @Override
    public void translate(Translator translator) {
        translator.struct(type);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VariableDeclarationExpression> getFields() {
        return fields;
    }

    public void setFields(List<VariableDeclarationExpression> fields) {
        this.fields = fields;
    }
}
