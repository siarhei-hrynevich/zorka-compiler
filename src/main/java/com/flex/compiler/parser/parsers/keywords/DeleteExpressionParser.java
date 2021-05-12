package com.flex.compiler.parser.parsers.keywords;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.keywords.DeleteExpression;
import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.parser.parsers.ExpressionParser;
import com.flex.compiler.parser.parsers.ValueExpressionParser;

public class DeleteExpressionParser implements ExpressionParser {
    private static final ValueExpressionParser VALUE_PARSER = new ValueExpressionParser();
    @Override
    public Expression tryParse(TokenSequence tokens) throws Exception {
        Token token = tokens.getCurrent();
        if (tokens.next().type != TokenType.OpenBracket)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        tokens.next();
        ValueExpression value = VALUE_PARSER.tryParse(tokens);
        if (tokens.getCurrent().type != TokenType.CloseBracket)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        tokens.next();
        return new DeleteExpression(token, value);
    }
}
