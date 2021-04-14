package com.flex.compiler.ast.concrete.value;

import com.flex.compiler.contextAnalyzer.ContextAnalyzerUtils;
import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.LexerUtils;
import com.flex.compiler.lexicalAnalyzer.Operator;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

public class BinaryOperation extends ValueExpression {

    private ValueExpression left;
    private ValueExpression right;
    private Operator operator;

    public BinaryOperation(String operator, ValueExpression left, ValueExpression right) {
        this.operator = LexerUtils.getOperator(operator);
        this.left = left;
        this.right = right;
    }

    public BinaryOperation(Operator operator, ValueExpression left, ValueExpression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public Operator getOperator() {
        return operator;
    }

    public ValueExpression getLeft() {
        return left;
    }

    public ValueExpression getRight() {
        return right;
    }

    @Override
    public void validate(TranslatorContext context) {
        left.validate(context);
        right.validate(context);
            if(!ContextAnalyzerUtils.assertTypes(left.getValidType(), right.getValidType()))
                throw new ContextException(ContextError.InvalidRightValue);

        if (operator == Operator.Assignment && !left.canAssign())
            throw new ContextException(ContextError.InvalidLeftValue);
    }

    @Override
    public void translate(Translator translator) {
        left.translate(translator);
        right.translate(translator);
        translator.operation(operator);
    }

    @Override
    public Type getValidType() {
        return SimpleTypes.getOperationType(left.getValidType(), operator);
    }

    @Override
    public boolean canAssign() {
        return false;
    }
}
