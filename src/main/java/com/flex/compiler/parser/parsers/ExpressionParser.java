package com.flex.compiler.parser.parsers;

import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.ast.Expression;

public interface ExpressionParser {
    Expression tryParse(TokenSequence tokens) throws Exception;
}
