package com.flex.compiler.parser;

import com.flex.compiler.ast.concrete.keywords.StructExpression;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.DeclarationExpression;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.parser.parsers.DeclarationParser;
import com.flex.compiler.parser.parsers.keywords.ImportExpressionParser;
import com.flex.compiler.parser.parsers.keywords.StructExpressionParser;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static final DeclarationParser declarationParser = new DeclarationParser();
    private static final ImportExpressionParser importParser = new ImportExpressionParser();
    private static final StructExpressionParser structParser = new StructExpressionParser();

    public static List<Expression> parse(TokenSequence tokens) throws Exception {
        List<Expression> expressions = new ArrayList<>();
        while (!tokens.isLast()) {
            if(tokens.getCurrent().type == TokenType.Keyword && ParserUtil.isImport(tokens.getCurrent().value)) {
                expressions.add(importParser.tryParse(tokens));
                continue;
            }
            List<String> modifiers = ParserUtil.parseModifiers(tokens);
            if (ParserUtil.isStruct(tokens.getCurrent().value)) {
                StructExpression struct = structParser.tryParse(tokens);
                struct.setModifiers(modifiers);
                expressions.add(struct);
                continue;
            }
            if (ParserUtil.isType(tokens.getCurrent())) {
                DeclarationExpression expression = declarationParser.tryParse(tokens);
                expression.setModifiers(modifiers);
                expressions.add(expression);
                continue;
            }
            throw new ParsingException(tokens.getCurrent(), Error.UnrecognizedOperation);
        }
        return expressions;
    }
}

