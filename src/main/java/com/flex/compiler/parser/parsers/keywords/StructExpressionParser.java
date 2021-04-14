package com.flex.compiler.parser.parsers.keywords;

import com.flex.compiler.ast.concrete.DeclarationExpression;
import com.flex.compiler.ast.concrete.VariableDeclarationExpression;
import com.flex.compiler.ast.concrete.keywords.StructExpression;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.ast.Expression;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.parser.parsers.DeclarationParser;
import com.flex.compiler.parser.parsers.ExpressionParser;

import java.util.ArrayList;
import java.util.List;

public class StructExpressionParser implements ExpressionParser {
    private static DeclarationParser parser = new DeclarationParser();

    @Override
    public StructExpression tryParse(TokenSequence tokens) throws Exception {
        List<VariableDeclarationExpression> fields = new ArrayList<>();
        if (tokens.next().type != TokenType.Identifier)
            throw new ParsingException(tokens.getCurrent(), Error.NeedIdentifier);
        String name = tokens.getCurrent().value;
        if (tokens.next().type != TokenType.BeginBlock)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        tokens.next();
        while (tokens.getCurrent().type != TokenType.EndBlock) {
            DeclarationExpression var = parser.tryParse(tokens);
            if (var instanceof VariableDeclarationExpression)
                fields.add((VariableDeclarationExpression) var);
            else
                throw new ParsingException(tokens.getCurrent(), Error.ExpectedVariable);
        }
        tokens.next();
        return new StructExpression(fields, name);
    }
}
