package com.flex.compiler.lexicalAnalyzer;

import java.util.HashMap;
import java.util.Map;

public class LexerUtils {

    private static Map<String, Operator> operators = new HashMap<>() {{
        put("=", Operator.Assignment);
        put("+=", Operator.PlusAssignment);
        put("-=", Operator.MinusAssignment);
        put("*=", Operator.MulAssignment);
        put("/=", Operator.DivAssignment);
        put("+", Operator.Plus);
        put("*", Operator.Mul);
        put("/", Operator.Div);
        put("-", Operator.Minus);
        put("<", Operator.Less);
        put(">", Operator.More);
        put("==", Operator.Equal);
        put("!=", Operator.NotEqual);
    }};

    private static Map<Operator, Integer> operatorsPriority = new HashMap<>() {{
        put(Operator.Assignment, 1);
        put(Operator.PlusAssignment, 1);
        put(Operator.MinusAssignment, 1);
        put(Operator.MulAssignment, 5);
        put(Operator.DivAssignment, 5);
        put(Operator.Plus, 1);
        put(Operator.Mul, 5);
        put(Operator.Div, 5);
        put(Operator.Minus, 1);
        put(Operator.Less, 1);
        put(Operator.More, 1);
        put(Operator.Equal, 1);
    }};

    public static boolean isOperator(String value) {
        return operators.containsKey(value);
    }

    public static boolean isConst(String value) {
        if (value.startsWith("\"") && value.endsWith("\""))
            return true;
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            try {
                Float.parseFloat(value);
                return true;
            } catch (Exception e1) {
            }
            return false;
        }
    }

    public static String getOperatorName(Operator operator) {
        return operators.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(operator))
                .findFirst()
                .get()
                .getKey();
    }

    public static Operator getOperator(String operator) {
        return operators.get(operator);
    }

    public static int getOperatorPriority(Operator operator) {
        return operatorsPriority.get(operator);
    }

    public static int getOperatorPriority(String operator) {
        return getOperatorPriority(getOperator(operator));
    }
}

