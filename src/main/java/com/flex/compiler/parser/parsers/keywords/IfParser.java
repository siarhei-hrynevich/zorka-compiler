package com.flex.compiler.parser.parsers.keywords;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.BlockExpression;
import com.flex.compiler.ast.concrete.keywords.IfExpression;
import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.parser.parsers.BlockParser;
import com.flex.compiler.parser.parsers.ExpressionParser;
import com.flex.compiler.parser.parsers.ValueExpressionParser;

public class IfParser implements ExpressionParser {

    private static final ValueExpressionParser valueParser = new ValueExpressionParser();
    private static final BlockParser blockParser = new BlockParser();

    @Override
    public Expression tryParse(TokenSequence tokens) throws Exception {
        if(tokens.next().type != TokenType.OpenBracket)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        tokens.next();
        ValueExpression predicate = valueParser.tryParse(tokens);

        if(tokens.getCurrent().type != TokenType.CloseBracket)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        tokens.next();
        BlockExpression thenBody = blockParser.tryParse(tokens);
        BlockExpression elseBody = null;
        if (tokens.getCurrent().type == TokenType.Keyword && tokens.getCurrent().value.equals("else"))
            elseBody = blockParser.tryParse(tokens);
        return new IfExpression(predicate, thenBody, elseBody);
    }
}
