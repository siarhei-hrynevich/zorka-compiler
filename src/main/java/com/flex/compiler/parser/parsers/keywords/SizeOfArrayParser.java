package com.flex.compiler.parser.parsers.keywords;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.value.SizeOfArray;
import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.parser.parsers.ValueExpressionParser;

public class SizeOfArrayParser implements KeywordValueParser {
    private static final ValueExpressionParser VALUE_PARSER = new ValueExpressionParser();
    @Override
    public ValueExpression tryParse(TokenSequence tokens) throws Exception {
        if (tokens.next().type != TokenType.OpenBracket)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        tokens.next();
        ValueExpression array = VALUE_PARSER.tryParse(tokens);
        if (tokens.getCurrent().type != TokenType.CloseBracket)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        tokens.next();
        return new SizeOfArray(array);
    }

    @Override
    public boolean canParse(TokenSequence tokens) {
        return tokens.getCurrent().value.equals("countOf");
    }

}
