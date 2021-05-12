package com.flex.compiler.parser;

import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.parser.parsers.ExpressionParser;
import com.flex.compiler.parser.parsers.keywords.*;
import com.flex.compiler.translator.symbols.AccessModifier;
import com.flex.compiler.translator.symbols.FunctionModifier;
import com.flex.compiler.translator.symbols.TypeModifier;
import com.flex.compiler.translator.symbols.VariableModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserUtil {

    private static final Map<String, ExpressionParser> keywordsParsers = new HashMap<>() {{
        put("return", new ReturnExpressionParser());
        put("if", new IfParser());
        put("while", new WhileParser());
        put("import", new ImportExpressionParser());
        put("struct", new StructExpressionParser());
        put("delete", new DeleteExpressionParser());
    }};

    private static final Map<String, AccessModifier> accessModifiers = new HashMap<>() {{
        put("export", AccessModifier.Public);
        put("internal", AccessModifier.Internal);
    }};

    private static final Map<String, VariableModifier> variableModifiers = new HashMap<>() {{
        put("const", VariableModifier.Const);
    }};

    private static final Map<String, FunctionModifier> functionModifiers = new HashMap<>() {{
        put("native", FunctionModifier.Native);
        put("extern", FunctionModifier.Extern);
    }};

    public static boolean isKeyword(String token) {
        return keywordsParsers.containsKey(token)
                || SimpleTypes.isSimpleType(token)
                || isModifier(token);
    }

    public static boolean isModifier(String token) {
        return accessModifiers.containsKey(token)
                || variableModifiers.containsKey(token)
                || functionModifiers.containsKey(token);
    }

    public static boolean isImport(String token) {
        return token.equals("import");
    }

    public static ExpressionParser getKeywordParser(String keyword) {
        return keywordsParsers.get(keyword);
    }

    public static boolean isType(Token token) {
        return token.type == TokenType.Identifier ||
                (token.type == TokenType.Keyword && SimpleTypes.isSimpleType(token.value));
    }

    public static boolean isStruct(String token) {
        return token.equals("struct");
    }

    public static List<String> parseModifiers(TokenSequence tokens) throws Exception {
        List<String> modifiers = new ArrayList<>();
        while (tokens.getCurrent().type == TokenType.Keyword && ParserUtil.isModifier(tokens.getCurrent().value)) {
            modifiers.add(tokens.getCurrent().value);
            tokens.next();
        }
        return modifiers;
    }

    public static AccessModifier parseAccessModifier(String value) {
        return accessModifiers.get(value);
    }

    public static VariableModifier parseVariableModifier(String value) {
        return variableModifiers.get(value);
    }

    public static FunctionModifier parseFunctionModifier(String value) {
        return functionModifiers.get(value);
    }

    public static TypeModifier parseTypeModifier(String value) {
        return null;
    }

    public static Symbol tryParseSymbol(TokenSequence tokens) {
            List<String> path = new ArrayList<>();
            path.add(tokens.getCurrent().value);
            String name = path.get(path.size() - 1);
            path.remove(path.size() - 1);
            return new Symbol(path, name);
    }
}
