package com.flex.compiler.translator;

import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.translator.symbols.Type;

import java.util.HashMap;
import java.util.Map;

public class TranslatorCUtils {
    private static Map<String, String> simpleTypeNames = new HashMap<>() {{
        put(SimpleTypes.getInt32Type().getName(), "int");
        put(SimpleTypes.getBoolType().getName(), "_Bool");
        put(SimpleTypes.getCharType().getName(), "char");
        put(SimpleTypes.getFloat32Type().getName(), "float");
        put(SimpleTypes.getStringType().getName(), "char*");
    }};

    public static String getTypeName(Type type) {
        String name = simpleTypeNames.get(type.getName());
        if (name == null)
            return type.getName();
        return name;
    }
}
