package com.flex.compiler.parser.parsers;

import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.ast.Expression;
import com.flex.compiler.parser.ParserUtil;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;

public class KeywordParser implements ExpressionParser {

    @Override
    public Expression tryParse(TokenSequence tokens) throws Exception {
        Token token = tokens.getCurrent();
        ExpressionParser parser = ParserUtil.getKeywordParser(token.value);
        if (parser == null) {
            throw new ParsingException(token, Error.UnrecognizedOperation);
        }
        return parser.tryParse(tokens);
    }
}
