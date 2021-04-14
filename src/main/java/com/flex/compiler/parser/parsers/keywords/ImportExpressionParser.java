package com.flex.compiler.parser.parsers.keywords;

import com.flex.compiler.ast.concrete.keywords.ImportExpression;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.ast.Expression;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.parser.parsers.ExpressionParser;

import java.util.ArrayList;
import java.util.List;

public class ImportExpressionParser implements ExpressionParser {

    @Override
    public Expression tryParse(TokenSequence tokens) throws Exception {
        List<String> path = new ArrayList<>();
        String symbol;

        while (tokens.next().type == TokenType.Identifier) {
            path.add(tokens.getCurrent().value);
            if (tokens.next().type == TokenType.OperationSplitter)
                break;
            if (tokens.getCurrent().type != TokenType.Dot) {
                throw new ParsingException(tokens.getCurrent(), Error.UnrecognizedOperation);
            }
        }

        if (tokens.getCurrent().type == TokenType.OperationSplitter)
            tokens.next();

        if (path.size() == 0)
            throw new ParsingException(tokens.getCurrent(), Error.NeedIdentifier);

        symbol = path.get(path.size() - 1);
        path.remove(path.size() - 1);

        return new ImportExpression(path, symbol);
    }
}
