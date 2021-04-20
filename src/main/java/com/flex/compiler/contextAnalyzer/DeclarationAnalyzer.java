package com.flex.compiler.contextAnalyzer;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.FunctionDeclarationExpression;
import com.flex.compiler.ast.concrete.keywords.ImportExpression;
import com.flex.compiler.ast.concrete.VariableDeclarationExpression;
import com.flex.compiler.ast.concrete.keywords.StructExpression;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.translator.SymbolsTable;
import com.flex.compiler.translator.symbols.*;
import com.flex.compiler.utils.SymbolsStorage;

import java.util.List;
import java.util.stream.Collectors;

public class DeclarationAnalyzer {

    private static void analyzeImport(ImportExpression e, SymbolsTable table) throws ContextException, Exception {
        List<Symbol> symbols = SymbolsStorage.getSymbols(e.getPath(),
                e.getSymbol());
        table.addImport(symbols, e.getSymbol());
    }

    private static void addVariable(VariableDeclarationExpression expression, SymbolsTable table) throws ContextException {
        table.add(ContextAnalyzerUtils.analyzeVariable(expression, table));
    }

    private static void addType(StructExpression expression, SymbolsTable table) throws ContextException {
        table.add(ContextAnalyzerUtils.analyzeStruct(expression, table));
    }

    private static void addFunction(FunctionDeclarationExpression expression, SymbolsTable table) {
        table.add(ContextAnalyzerUtils.analyzeFunction(expression, table));
    }

    public static void analyzeTypes(List<Expression> ast, SymbolsTable table) {
        List<StructExpression> types =
                ContextAnalyzerUtils.filterExpressions(ast, StructExpression.class);
        types.forEach(e -> addType(e, table));
    }

    public static void analyzeFunctions(List<Expression> ast, SymbolsTable table) {
        List<FunctionDeclarationExpression> functions =
                ContextAnalyzerUtils.filterExpressions(ast, FunctionDeclarationExpression.class);
        functions.forEach(e -> addFunction(e, table));
    }

    public static void analyzeVars(List<Expression> ast, SymbolsTable table) {
        List<VariableDeclarationExpression> variables =
                ContextAnalyzerUtils.filterExpressions(ast, VariableDeclarationExpression.class);
        variables.forEach(e -> addVariable(e, table));
    }

    public static void validateImports(List<Expression> ast, SymbolsTable table) throws Exception {
        List<ImportExpression> imports = ContextAnalyzerUtils.filterExpressions(ast, ImportExpression.class);
        for (ImportExpression e : imports) {
            analyzeImport(e, table);
        }

        validateUndeclaredTypes(ast, table);
    }

    private static void validateUndeclaredTypes(List<Expression> ast, SymbolsTable table) {
        for (Expression e : ast) {
            if (e instanceof FunctionDeclarationExpression) {
                validateTypesInFunction(((FunctionDeclarationExpression) e).getFunction(), table);
            } else if (e instanceof VariableDeclarationExpression) {
                validateTypeOfVariable(((VariableDeclarationExpression) e).getVariable(), table);
            } else if (e instanceof StructExpression) {
                validateStruct((StructExpression) e, table);
            }
        }
    }

    private static void validateStruct(StructExpression e, SymbolsTable table) {
         e.setType(getValidType(e.getType(), table));
         e.getType().getFields().forEach(var -> validateTypeOfVariable(var, table));
    }

    private static void validateTypesInFunction(Function func, SymbolsTable table) {
        func.setReturnValue(getValidType(func.getReturnValue(), table));
        for (Variable param : func.getParams()) {
            validateTypeOfVariable(param, table);
        }
    }

    private static Type getValidType(Type type, SymbolsTable table) {
        if (!type.isDeclared()) {
            int arrayDimension = type.getArrayDimension();
            type = ContextAnalyzerUtils.findType(type.getName(), table);
            if (!type.isDeclared())
                throw new ContextException(ContextError.UndeclaredType);
            type.setArrayDimension(arrayDimension);
        }
        return type;
    }

    private static void validateTypeOfVariable(Variable variable, SymbolsTable table) {
        variable.setType(getValidType(variable.getType(), table));
    }
}
