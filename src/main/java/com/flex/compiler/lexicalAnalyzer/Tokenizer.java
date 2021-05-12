package com.flex.compiler.lexicalAnalyzer;

import com.flex.compiler.parser.ParserUtil;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Tokenizer {
    private static final List<String> patterns = new ArrayList<>() {{
        add("//.+$");
        add("\".*\"");
        add("\\.");
        add("(\\+||-){0,1}\\d+\\.{0,1}\\d+");
        add("\\w+");
        //add("[A-Za-z]\\w*");
        add("\\{");
        add("\\}");
        add("\\(");
        add("\\)");
        add("\\[");
        add("\\]");
        add(",");
        add("\\+=");
        add("\\+");
        add("-=");
        add("-");
        add("\\*=");
        add("\\*");
        add(";");
        add("<");
        add(">");
        add("/=");
        add("/");
        add("\\!=");
        add("==");
        add("=");
    }};


    private static String createRegex(List<String> patterns)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < patterns.size() - 1; i++)
        {
            builder.append("(");
            builder.append(patterns.get(i));
            builder.append(")|");
        }
        builder.append("(");
        builder.append(patterns.get(patterns.size() - 1));
        builder.append(")");
        return builder.toString();
    }

    private static List<Token> split(List<String> lines) {
        List<Token> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile(createRegex(patterns));
        for (int i = 0; i < lines.size(); i++) {
            Matcher matcher = pattern.matcher(lines.get(i));
            while (matcher.find()) {
                Token token = new Token();
                token.start = matcher.start();
                token.end = matcher.end();
                token.value = (lines.get(i).substring(token.start, token.end));
                token.line = i;
                if(isComment(token.value))
                    continue;
                token.type = determineType(token.value);
                if (token.type == TokenType.Identifier && Character.isDigit(token.value.charAt(0)))
                    throw new ParsingException(token, Error.NeedIdentifier);
                tokens.add(token);
            }
        }
        Token token = new Token();
        token.value = "";
        token.type = TokenType.EndOfFile;
        tokens.add(token);
        return tokens;
    }

    private static boolean isComment(String text) {
        return text.startsWith("//");
    }

    private static TokenType determineType(String value) {
        if (ParserUtil.isKeyword(value))
            return TokenType.Keyword;
        if (LexerUtils.isConst(value))
            return TokenType.Literal;
        return switch (value) {
            case "(" -> TokenType.OpenBracket;
            case ")" -> TokenType.CloseBracket;
            case "{" -> TokenType.BeginBlock;
            case "}" -> TokenType.EndBlock;
            case "=", "+=", "-=", "*=", "/=" -> TokenType.Assignment;
            case "[" -> TokenType.OpenSquareBracket;
            case "]" -> TokenType.CloseSquareBracket;
            case "+", "-", "*", "/", "<", ">", "!=", "==" -> TokenType.BinaryOperation;
            case "," -> TokenType.Comma;
            case ";" -> TokenType.OperationSplitter;
            case "." -> TokenType.Dot;
            default -> TokenType.Identifier;
        };
    }

    public static List<Token> tokenize(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> lines = reader.lines().collect(Collectors.toList());
        return split(lines);
    }
}
