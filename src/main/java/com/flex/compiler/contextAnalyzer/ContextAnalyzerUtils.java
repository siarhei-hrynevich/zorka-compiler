package com.flex.compiler.contextAnalyzer;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.FunctionDeclarationExpression;
import com.flex.compiler.ast.concrete.VariableDeclarationExpression;
import com.flex.compiler.ast.concrete.keywords.StructExpression;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.parser.ParserUtil;
import com.flex.compiler.translator.SymbolsTable;
import com.flex.compiler.translator.symbols.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContextAnalyzerUtils {
    public static <T> List<T> filterExpressions(List<Expression> ast, Class<T> type) {
        return ast.stream()
                .filter(e -> e.getClass().isAssignableFrom(type))
                .map(e -> (T) e)
                .collect(Collectors.toList());
    }

    public static Variable analyzeVariable(VariableDeclarationExpression expression, SymbolsTable table) {
        Variable variable = new Variable();
        variable.setName(expression.getName());
        Type type = findType(expression.getType(), table);
        variable.setType(type);
        expression.setVariable(variable);
        List<String> modifiers = expression.getModifiers();
        variable.getType().setArrayDimension(expression.getDimension());
        if (modifiers == null)
            return variable;
        for (String modifierName : modifiers) {
            VariableModifier variableModifier = ParserUtil.parseVariableModifier(modifierName);
            if (variableModifier == null) {
                AccessModifier modifier = ParserUtil.parseAccessModifier(modifierName);
                variable.setAccessModifier(modifier);
            } else variable.addModifier(variableModifier);
        }
        return variable;
    }

    private static List<Variable> analyzeFunctionArgs(FunctionDeclarationExpression expression, SymbolsTable table) throws ContextException {
        List<Variable> args = new ArrayList<>();
        for (VariableDeclarationExpression e : expression.getArgs()) {
            Type type = findType(e.getType(), table);
            Variable variable = new Variable(type, e.getName());
            args.add(variable);
        }
        return args;
    }

    public static Type findType(String name, SymbolsTable table) {
        Type type = table.findType(name)
                .orElse(SimpleTypes.findType(name));
        if (type == null)
            return new Type(name);
        return new Type(type);
    }

    public static Function analyzeFunction(FunctionDeclarationExpression expression, SymbolsTable table) throws ContextException {
        Function function = new Function();
        expression.setFunction(function);
        function.setName(expression.getName());
        Type type = findType(expression.getTypeName(), table);
        type.setArrayDimension(expression.getReturnValueArrayDimension());
        function.setReturnValue(type);

        List<String> modifiers = expression.getModifiers();
        for (String modifierName : modifiers) {
            FunctionModifier variableModifier = ParserUtil.parseFunctionModifier(modifierName);
            if (variableModifier == null) {
                AccessModifier modifier = ParserUtil.parseAccessModifier(modifierName);
                function.setAccessModifier(modifier);
            } else function.addModifier(variableModifier);
        }

        function.setArgs(analyzeFunctionArgs(expression, table));
        return function;
    }

    public static Type analyzeStruct(StructExpression expression, SymbolsTable table) {
        Type type = new Type(expression.getName());
        type.setDeclared(true);
        List<Variable> fields = expression.getFields()
                .stream()
                .map(varExpr -> ContextAnalyzerUtils.analyzeVariable(varExpr, table))
                .collect(Collectors.toList());
        type.setFields(fields);

        for (String modifierName : expression.getModifiers()) {
            TypeModifier variableModifier = ParserUtil.parseTypeModifier(modifierName);
            if (variableModifier == null) {
                AccessModifier modifier = ParserUtil.parseAccessModifier(modifierName);
                type.setAccessModifier(modifier);
            } else type.addModifier(variableModifier);
        }

        expression.setType(type);
        return type;
    }

    public static boolean assertTypes(Type first, Type second) {
        return first.equals(second);
    }
}
