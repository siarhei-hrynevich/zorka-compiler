package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;


public class StructFieldExpression extends ValueExpression {

    private String fieldName;
    private ValueExpression struct;

    private Variable field;

    public StructFieldExpression(String fieldName, ValueExpression struct) {
        this.fieldName = fieldName;
        this.struct = struct;
    }

    @Override
    public void validate(TranslatorContext context) {

    }

    @Override
    public void translate(Translator translator) {

    }

    public String getFieldName() {
        return fieldName;
    }

    public ValueExpression getStruct() {
        return struct;
    }

    public Variable getField() {
        return field;
    }

    public void setField(Variable field) {
        this.field = field;
    }

    @Override
    public Type getValidType() {
        return null;
    }

    @Override
    public boolean canAssign() {
        return !field.isConst();
    }
}
