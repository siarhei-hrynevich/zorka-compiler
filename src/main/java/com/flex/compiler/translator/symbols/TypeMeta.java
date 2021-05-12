package com.flex.compiler.translator.symbols;

import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeMeta {

    private static final List<TypeMeta> TYPES = new ArrayList<>();

    private int size;
    private String name;
    private List<Variable> fields;
    private List<TypeModifier> modifiers;
    private final boolean isSimple;
    private List<String> location;

    public TypeMeta getType(String name, List<String> location) {
        return TYPES.stream()
                .filter(t -> t.name.equals(name) && t.location.equals(location))
                .findFirst().orElseThrow(() -> new ContextException(ContextError.UndeclaredType));
    }

    private TypeMeta(String name) {
        isSimple = false;
        this.name = name;
    }

    private TypeMeta(int size) {
        this.size = size;
        this.isSimple = true;
    }

    public int getSize() {
        return size;
    }
}
