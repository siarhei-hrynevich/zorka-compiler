package com.flex.compiler.parser.parsers;

import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.lexicalAnalyzer.TokenType;
import com.flex.compiler.lexicalAnalyzer.TokenSequence;
import com.flex.compiler.ast.*;
import com.flex.compiler.ast.concrete.value.BinaryOperation;
import com.flex.compiler.ast.concrete.value.ValueExpression;
import com.flex.compiler.ast.concrete.value.VariableExpression;
import com.flex.compiler.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class AssignmentParser implements ExpressionParser {


    @Override
    public Expression tryParse(TokenSequence tokens) throws Exception {

        List<String> location = new ArrayList<>();

//        while (tokens.getCurrent().type == TokenType.Identifier) {
//            location.add(tokens.getCurrent().value);
//            if (tokens.next().type != TokenType.Dot)
//
//        }
        //ValueExpression left = new VariableExpression(ParserUtil.tryParseSymbol(tokens));
        Token token = tokens.next();
        if (token.type == TokenType.Assignment) {
            tokens.next();
            ValueExpression right = new ValueExpressionParser().tryParse(tokens);
            return null;//new BinaryOperation(token.value, left, right);
        } else {
            throw new Exception("Expected: operation");
        }

    }
}
