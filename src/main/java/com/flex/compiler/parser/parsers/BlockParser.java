package com.flex.compiler.parser.parsers;

import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.ast.concrete.BlockExpression;
import com.flex.compiler.ast.Expression;

import java.util.ArrayList;
import java.util.List;

public class BlockParser implements ExpressionParser {

    private final OperationParser parser = new OperationParser();

    @Override
    public BlockExpression tryParse(TokenSequence tokens) throws Exception {
        List<Expression> expressions = new ArrayList<>();
        if (tokens.getCurrent().type != TokenType.BeginBlock) {
            expressions.add(parser.tryParse(tokens));
        } else {
            tokens.next();
            expressions = parseBlock(tokens);
            if (!tokens.isLast())
                tokens.next();
        }

        return new BlockExpression(expressions);
    }

    private List<Expression> parseBlock(TokenSequence tokens) throws Exception {
        List<Expression> expressions = new ArrayList<>();
        while (tokens.getCurrent().type != TokenType.EndBlock) {
            expressions.add(parser.tryParse(tokens));
            if (tokens.getCurrent().type == TokenType.OperationSplitter)
                tokens.next();
        }
        return expressions;
    }
}
