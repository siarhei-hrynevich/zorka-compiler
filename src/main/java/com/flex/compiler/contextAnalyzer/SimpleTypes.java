package com.flex.compiler.contextAnalyzer;

import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.Operator;
import com.flex.compiler.translator.symbols.Type;

import java.util.HashMap;
import java.util.Map;

public class SimpleTypes {

    private static final Map<String, Type> simpleTypes = new HashMap<>() {{
        Type integer32 = new Type(4);
        integer32.setName("int32");
        integer32.setResultName("int");
        put(integer32.getName(), integer32);

        Type character = new Type(2);
        character.setName("char");
        character.setResultName("char");
        put(character.getName(), character);

        Type float32 = new Type(4);
        float32.setName("float32");
        float32.setResultName("float");
        put(float32.getName(), float32);

        Type string = new Type(8);
        string.setName("string");
        string.setResultName("char*");
        put(string.getName(), string);

        Type bool = new Type(1);
        bool.setName("bool");
        bool.setResultName("bool");
        put(bool.getName(), bool);
    }};

    public static Type findType(String name) {
        Type type = simpleTypes.get(name);
        if (type == null)
            return null;
        return new Type(type);
    }

    public static Type getStringType() {
        return new Type(simpleTypes.get("string"));
    }

    public static Type getInt32Type() {
        return new Type(simpleTypes.get("int32"));
    }

    public static Type getFloat32Type() {
        return new Type(simpleTypes.get("float32"));
    }

    public static Type getCharType() {
        return new Type(simpleTypes.get("char"));
    }

    public static Type getBoolType() {
        return new Type(simpleTypes.get("bool"));
    }

    public static Type getVoidType() {
        return new Type("void");
    }

    public static boolean isSimpleType(String name) {
        return simpleTypes.containsKey(name);
    }

    public static Type getOperationType(Type operandType, Operator operator) {
        if (!isSimpleType(operandType.getName()))
            throw new ContextException(ContextError.InvalidValueType);
        return switch (operator) {
            case Div, Mul, Plus, Minus, Assignment -> operandType;
            case Less, Equal, More -> getBoolType();
            default -> throw new ContextException(ContextError.InvalidValueType);
        };
    }
}
