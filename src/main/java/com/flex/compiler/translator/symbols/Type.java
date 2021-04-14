package com.flex.compiler.translator.symbols;

import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Type extends Symbol {
    private int size;
    private List<Variable> fields;
    private List<TypeModifier> modifiers = new ArrayList<>();
    private final boolean isSimple;
    private String resultName;

    private int arrayDimension = 0;
    private boolean isDeclared;

    public Type(String name) {
        isSimple = false;
        this.name = name;
        isDeclared = isVoid();
        if (isDeclared)
            resultName = name;
    }

    public Type(int size) {
        this.size = size;
        this.isSimple = true;
        isDeclared = true;
    }

    public Type(Type type) {
        this.resultName = type.resultName;
        this.name = type.name;
        this.isDeclared = type.isDeclared;
        this.fields = type.fields;
        this.modifiers = type.modifiers;
        this.location = type.location;
        this.isSimple = type.isSimple;
        this.arrayDimension = type.arrayDimension;
        this.size = type.size;
    }

    public boolean isVoid() {
        return name.equals("void");
    }

    public boolean isSimple() {
        return isSimple;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Variable> getFields() {
        return fields;
    }

    public void setFields(List<Variable> fields) {
        this.fields = fields;
    }

    public List<TypeModifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<TypeModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public void addModifier(TypeModifier modifier) {
        if (modifier != null) {
            modifiers.add(modifier);
        }
    }

    public boolean isDeclared() {
        return isDeclared;
    }

    public void setDeclared(boolean declared) {
        isDeclared = declared;
    }

    public int getArrayDimension() {
        return arrayDimension;
    }

    public void setArrayDimension(int arrayDimension) {
        this.arrayDimension = arrayDimension;
    }

    public void decrementDimension() {
        if (arrayDimension == 0)
            throw new ContextException(ContextError.InvalidValueType);
        arrayDimension--;
    }

    public Variable findField(String name) {
        return fields.stream()
                .filter(f -> f.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new ContextException(ContextError.FieldIsNotExist));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Type type = (Type) o;
        return size == type.size &&
                (fields == type.fields || fields.equals(type.fields)) &&
                modifiers.equals(type.modifiers) && arrayDimension == type.arrayDimension;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), size);
        result = 31 * result + Objects.hash(fields);
        result = 31 * result + modifiers.hashCode();
        result = 31 * result + arrayDimension;
        return result;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }
}