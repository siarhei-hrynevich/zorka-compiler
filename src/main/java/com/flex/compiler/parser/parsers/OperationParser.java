package com.flex.compiler.parser.parsers;

import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.DeclarationExpression;
import com.flex.compiler.parser.ParserUtil;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;

import java.util.List;

public class OperationParser implements ExpressionParser {

    private final KeywordParser keywordParser = new KeywordParser();
    private final DeclarationParser declarationParser = new DeclarationParser();
    private final ValueExpressionParser valueExpressionParser = new ValueExpressionParser();

    @Override
    public Expression tryParse(TokenSequence tokens) throws Exception {
        if (tokens.getCurrent().type == TokenType.Keyword) {
            List<String> modifiers = ParserUtil.parseModifiers(tokens);
            if (ParserUtil.isType(tokens.getCurrent())) {
                DeclarationExpression expression = declarationParser.tryParse(tokens);
                expression.setModifiers(modifiers);
                return expression;
            }
            if(tokens.getCurrent().type == TokenType.Keyword)
                return keywordParser.tryParse(tokens);

        }
        if (tokens.getCurrent().type == TokenType.Identifier) {
            Token second = tokens.peekNext();
            if (second.type == TokenType.OpenSquareBracket) {
                if (tokens.peekNext(2).type == TokenType.CloseSquareBracket)
                    return declarationParser.tryParse(tokens);
            }
            if (second.type == TokenType.Identifier) {
                return declarationParser.tryParse(tokens);
            }
        }

        return valueExpressionParser.tryParse(tokens);

//        throw new ParsingException(tokens.getCurrent(), Error.UnrecognizedOperation);
    }
}
