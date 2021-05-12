package com.flex.compiler.parser.parsers.keywords;

import com.flex.compiler.ast.concrete.value.ArrayInstance;
import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.parser.ParserUtil;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.parser.parsers.ExpressionParser;
import com.flex.compiler.parser.parsers.ValueExpressionParser;

public class ArrayInstantiationParser implements KeywordValueParser {
    private static final ValueExpressionParser VALUE_PARSER = new ValueExpressionParser();

    @Override
    public boolean canParse(TokenSequence tokens) {
        return tokens.getCurrent().value.equals("array");
    }

    @Override
    public ArrayInstance tryParse(TokenSequence tokens) throws Exception {
        Token token = tokens.getCurrent();
        if (!tokens.next().value.equals("<"))
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);

        if (!ParserUtil.isType(tokens.next()))
            throw new ParsingException(tokens.getCurrent(), Error.NeedTypeName);
        String type = tokens.getCurrent().value;
        int arrayDimensionCount = 0;
        while (tokens.next().type == TokenType.OpenSquareBracket) {
            if (tokens.next().type != TokenType.CloseSquareBracket)
                throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
            arrayDimensionCount++;
        }

        if (!tokens.getCurrent().value.equals(">"))
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);

        if (tokens.next().type != TokenType.OpenBracket)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        tokens.next();
        ValueExpression size = VALUE_PARSER.tryParse(tokens);

        if (tokens.getCurrent().type != TokenType.CloseBracket)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        tokens.next();
        return new ArrayInstance(token, size, type, arrayDimensionCount);
    }
}
