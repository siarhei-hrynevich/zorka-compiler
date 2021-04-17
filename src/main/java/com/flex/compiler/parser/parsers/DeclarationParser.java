package com.flex.compiler.parser.parsers;

import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.ast.concrete.*;
import com.flex.compiler.parser.ParserUtil;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;

import java.util.ArrayList;
import java.util.List;


public class DeclarationParser implements ExpressionParser {

    private int parsePointersCount(TokenSequence tokens) {
        int count = 0;
        while (tokens.getCurrent().type == TokenType.OpenSquareBracket) {
            if (tokens.next().type != TokenType.CloseSquareBracket)
                throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
            tokens.next();
            count++;
        }
        return count;
    }

    private VariableDeclarationExpression parseVariableDeclaration(TokenSequence tokens) throws Exception {
        Token token = tokens.getCurrent();
        String type = token.value;
        if (ParserUtil.isType(token)) {
            token = tokens.next();
            int pointersCount = parsePointersCount(tokens);

            if (token.type != TokenType.Identifier) {
                throw new Exception("Expected: Identifier");
            }
            VariableDeclarationExpression var = new VariableDeclarationExpression(type, token.value, null);
            var.setDimension(pointersCount);
            if (tokens.next().type == TokenType.Assignment) {
                tokens.next();
                ValueExpression initialization = new ValueExpressionParser().tryParse(tokens);
                var.setInitializer(initialization);
            }

            return var;
        }
        throw new Exception("Expected: Type name");
    }

    private FunctionDeclarationExpression parseFunctionDeclaration(String type, String name, TokenSequence tokens) throws Exception {
        FunctionDeclarationExpression function;

        List<VariableDeclarationExpression> args = new ArrayList<>();
        tokens.next();
        while (tokens.getCurrent().type != TokenType.CloseBracket) {
            args.add(parseVariableDeclaration(tokens));
            if (tokens.getCurrent().type == TokenType.Comma)
                tokens.next();
        }
        tokens.next();
        function = new FunctionDeclarationExpression(type, name, args);
        return function;
    }

    private DeclarationExpression parseFunction(String type, String name, TokenSequence tokens) throws Exception {
        FunctionDeclarationExpression declaration = parseFunctionDeclaration(type, name, tokens);
        if (tokens.getCurrent().type == TokenType.OperationSplitter) {
            tokens.next();
        } else {
            BlockParser parser = new BlockParser();
            BlockExpression body = parser.tryParse(tokens);
            declaration.setImplementation(body);
        }
        return declaration;
    }

    private VariableDeclarationExpression createVariable(String type, String name) throws Exception {
        return new VariableDeclarationExpression(type, name, null);
    }

    private int parseDimensionCount(TokenSequence tokens) {
        int count = 0;
        while (tokens.next().type == TokenType.OpenSquareBracket) {
            if (tokens.next().type != TokenType.CloseSquareBracket)
                throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
            count++;
        }
        return count;
    }

    @Override
    public DeclarationExpression tryParse(TokenSequence tokens) throws Exception {
        Token token = tokens.getCurrent();
        if (!ParserUtil.isType(token))
            throw new ParsingException(token, Error.NeedTypeName);

        String type = token.value;
        String name;

        int dimensionCount = parseDimensionCount(tokens);
        token = tokens.getCurrent();
        if (token.type != TokenType.Identifier)
            throw new Exception("Expected: Identifier");

        name = token.value;

        token = tokens.next();

        if (token.type == TokenType.OpenBracket) {
            return parseFunction(type, name, tokens);
        } else {
            VariableDeclarationExpression var = createVariable(type, name);
            if (token.type == TokenType.Assignment) {
                tokens.next();
                var.setInitializer(new ValueExpressionParser().tryParse(tokens));
            }
            if (tokens.getCurrent().type == TokenType.OperationSplitter)
                tokens.next();
            var.setDimension(dimensionCount);
            return var;
        }
    }
}
