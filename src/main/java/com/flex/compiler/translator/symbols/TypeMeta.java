package com.flex.compiler.translator.symbols;

import java.util.List;

public class TypeMeta {
    private int size;
    private List<Variable> fields;
    private TypeModifier[] modifiers;
    private final boolean isSimple;

    public TypeMeta(String name) {
        isSimple = false;
     //   this.name = name;
    }

    public TypeMeta(int size) {
        this.size = size;
        this.isSimple = true;
    }
}
