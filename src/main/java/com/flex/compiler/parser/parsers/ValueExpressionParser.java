package com.flex.compiler.parser.parsers;

import com.flex.compiler.ast.concrete.value.*;
import com.flex.compiler.lexicalAnalyzer.*;
import com.flex.compiler.parser.ParserUtil;
import com.flex.compiler.parser.Symbol;
import com.flex.compiler.parser.exception.Error;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.parser.parsers.keywords.ArrayInstantiationParser;
import com.flex.compiler.parser.parsers.keywords.KeywordValueParser;
import com.flex.compiler.parser.parsers.keywords.SizeOfArrayParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ValueExpressionParser implements ExpressionParser {

    private static final List<KeywordValueParser> KEYWORD_PARSERS = new ArrayList<>() {{
        add(new ArrayInstantiationParser());
        add(new SizeOfArrayParser());
    }};

    private List<ValueExpression> parseArgs(TokenSequence tokens) throws Exception {
        List<ValueExpression> args = new ArrayList<>();
        tokens.next();
        ValueExpressionParser parser = new ValueExpressionParser();
        while (tokens.getCurrent().type != TokenType.CloseBracket) {
            args.add(parser.tryParse(tokens));
            if (tokens.getCurrent().type == TokenType.Comma)
                tokens.next();
        }
        tokens.next();
        return args;
    }

    private FunctionCallExpression parseFunctionCall(TokenSequence tokens, Symbol symbol) throws Exception {
        Token token = tokens.getCurrent();
        List<ValueExpression> args = parseArgs(tokens);
        return new FunctionCallExpression(token, symbol, args);
    }

    private VariableExpression parseVariable(TokenSequence tokens, Symbol symbol) {
        return new VariableExpression(tokens.getCurrent(), symbol);
    }

    private ConstExpression createConst(Token token) {
        ConstExpression expression = new ConstExpression(token);
        if (expression.getValue().startsWith("\"")) {
            expression.setString(true);
            return expression;
        }
        boolean isFloat = token.value.matches("[-+]?[0-9]*\\.[0-9]+");
        expression.setInt(!isFloat);
        expression.setFloat(isFloat);
        return expression;
    }

    private ValueExpression parseSymbol(TokenSequence tokens) throws Exception {
        Token token = tokens.getCurrent();
        if (token.type == TokenType.Literal) {
            ConstExpression expression = createConst(tokens.getCurrent());
            tokens.next();
            return expression;
        }
        if (token.type == TokenType.Identifier) {
            Symbol symbol = ParserUtil.tryParseSymbol(tokens);

            token = tokens.next();

            if (token.type == TokenType.OpenBracket)
                return parseFunctionCall(tokens, symbol);
            return parseVariable(tokens, symbol);
        }
        return null;
    }

    private IndexerExpression parseIndexer(TokenSequence tokens, ValueExpression pointer) throws Exception {
        Token token = tokens.getCurrent();
        tokens.next();
        ValueExpression index = tryParse(tokens);
        if (tokens.getCurrent().type != TokenType.CloseSquareBracket)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        return new IndexerExpression(token, pointer, index);
    }

    private StructFieldExpression parseField(TokenSequence tokens, ValueExpression struct) {
        Token token = tokens.getCurrent();
        tokens.next();
        if (tokens.getCurrent().type != TokenType.Identifier)
            throw new ParsingException(tokens.getCurrent(), Error.NeedIdentifier);
        return new StructFieldExpression(token, tokens.getCurrent().value, struct);
    }

    ValueExpression parseKeywordOrSymbol(TokenSequence tokens) throws Exception {
        KeywordValueParser parser = KEYWORD_PARSERS.stream()
                .filter(p -> p.canParse(tokens))
                .findFirst()
                .orElse(null);
        if (parser == null)
            return parseSymbol(tokens);
        return parser.tryParse(tokens);
    }

    // const, var, function call, indexer, field
    private ValueExpression parseValue(TokenSequence tokens) throws Exception {
        ValueExpression value = parseKeywordOrSymbol(tokens);

        while (tokens.getCurrent().type == TokenType.OpenSquareBracket
                || tokens.getCurrent().type == TokenType.Dot) {
            value = tokens.getCurrent().type == TokenType.OpenSquareBracket ?
                    parseIndexer(tokens, value) : parseField(tokens, value);
            tokens.next();
        }
        return value;
    }

    private final Stack<Token> operators = new Stack<>();
    private final Stack<ValueExpression> values = new Stack<>();
    private int bracketsCount = 0;

    @Override
    public ValueExpression tryParse(TokenSequence tokens) throws Exception {

        boolean expectedOperation = false;

        while (true) {
            Token token = tokens.getCurrent();

            if (token.type == TokenType.OpenBracket) {
                operators.add(token);
                tokens.next();
                bracketsCount++;
                expectedOperation = false;
                continue;
            }

            if (token.type == TokenType.CloseBracket) {
                if (bracketsCount == 0)
                    break;
                bracketsCount--;
                popBeforeBracket();
                expectedOperation = false;
                tokens.next();
                continue;
            }

            if (token.type == TokenType.Assignment || token.type == TokenType.BinaryOperation) {
                if (!expectedOperation) {
                    throw new ParsingException(token, Error.ExpectedValue);
                }
                expectedOperation = false;
                popHighOperations(token);
                tokens.next();
                continue;
            }
            ValueExpression value = parseValue(tokens);
            if (value == null)
                break;
            expectedOperation = true;
            values.push(value);
        }
        if (bracketsCount != 0)
            throw new ParsingException(tokens.getCurrent(), Error.UnexpectedToken);
        if (values.empty())
            throw new ParsingException(tokens.getCurrent(), Error.ExpectedValue);
        return getFinalValue();
    }

    private ValueExpression getFinalValue() {
        while (!operators.empty()) {
            popOperator();
        }
        return values.pop();
    }

    private void popOperator() {
        Token last = operators.pop();
        ValueExpression right = values.pop();
        ValueExpression left = values.pop();
        BinaryOperation result = new BinaryOperation(last, last.value, left, right);
        values.push(result);
    }

    private void popBeforeBracket() {
        while (!operators.empty()
                && operators.peek().type != TokenType.OpenBracket) {
            popOperator();
        }
        operators.pop();
    }

    private void popHighOperations(Token token) {
        Operator operator = LexerUtils.getOperator(token.value);
        while (!operators.empty()
                && operators.peek().type != TokenType.OpenBracket
                && hasLessPriorityThenLast(operator)) {
            popOperator();
        }
        operators.push(token);
    }

    private boolean hasLessPriorityThenLast(Operator operator) {
        return !operators.empty()
                && LexerUtils.getOperatorPriority(operators.peek().value) > LexerUtils.getOperatorPriority(operator);
    }
}
