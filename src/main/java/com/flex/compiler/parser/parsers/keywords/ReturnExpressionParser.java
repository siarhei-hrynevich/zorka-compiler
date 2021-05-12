package com.flex.compiler.parser.parsers.keywords;

import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.keywords.ReturnExpression;
import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.parser.parsers.ExpressionParser;
import com.flex.compiler.parser.parsers.ValueExpressionParser;

public class ReturnExpressionParser implements ExpressionParser {
    @Override
    public Expression tryParse(TokenSequence tokens) throws Exception {
        Token token = tokens.getCurrent();
        tokens.next();
        ValueExpression expression = new ValueExpressionParser().tryParse(tokens);
        if (tokens.getCurrent().type == TokenType.OperationSplitter)
            tokens.next();
        return new ReturnExpression(token, expression);
    }
}
