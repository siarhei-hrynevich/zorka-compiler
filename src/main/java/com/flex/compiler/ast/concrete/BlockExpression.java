package com.flex.compiler.ast.concrete;

import com.flex.compiler.ast.Expression;
import com.flex.compiler.ast.concrete.keywords.ReturnExpression;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.lexicalAnalyzer.Token;
import com.flex.compiler.translator.Translator;
import com.flex.compiler.translator.TranslatorContext;
import com.flex.compiler.translator.symbols.Type;

import java.util.List;

public class BlockExpression extends Expression {

    private final List<Expression> expressions;
    private Type returnType;

    public List<Expression> getExpressions() {
        return expressions;
    }

    public BlockExpression(Token token, List<Expression> expressions) {
        super(token);
        this.expressions = expressions;
    }

    @Override
    public void validate(TranslatorContext context) {
        context.createScope();
        expressions.forEach((e) -> e.validate(context));
        context.popScope();
//        ReturnExpression ret = (ReturnExpression) e;
//        Type type = getValidValueType(ret.getValue());
//        if (iterator.hasNext())
//            throw new ContextException(ContextError.UnattainableCode);
//        if (returnType == null) {
//            return;
//        }
//        if (!assertTypes(type, returnType)) {
//            throw new ContextException(ContextError.InvalidReturnType);
//        }
    }

    @Override
    public void translate(Translator translator) {
        translator.createScope();
        for (Expression e : expressions) {
            e.translate(translator);
            translator.nextOperation();
        }
        translator.popScope();
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
}
