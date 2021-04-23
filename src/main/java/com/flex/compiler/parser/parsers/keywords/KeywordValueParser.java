package com.flex.compiler.parser.parsers.keywords;

import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.parser.parsers.ExpressionParser;

public interface KeywordValueParser extends ExpressionParser {
    boolean canParse(TokenSequence tokens);

    @Override
    ValueExpression tryParse(TokenSequence tokens) throws Exception;
}
