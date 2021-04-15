package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;


public class StructFieldExpression extends ValueExpression {

    private String fieldName;
    private ValueExpression struct;

    private Type fieldType;
    private Type structType;
    private boolean isConst;

    public StructFieldExpression(String fieldName, ValueExpression struct) {
        this.fieldName = fieldName;
        this.struct = struct;
    }

    @Override
    public void validate(TranslatorContext context) {
        struct.validate(context);

        structType = struct.getValidType();
        Variable variable = structType.findField(fieldName);
        fieldType = variable.getType();
        isConst = variable.isConst();
    }

    @Override
    public void translate(Translator translator) {
        struct.translate(translator);
        translator.pushField(structType, fieldName);
    }

    public String getFieldName() {
        return fieldName;
    }

    public ValueExpression getStruct() {
        return struct;
    }

    @Override
    public Type getValidType() {
        return fieldType;
    }

    @Override
    public boolean canAssign() {
        return !isConst;
    }
}
