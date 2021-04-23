package com.flex.compiler.translator;


import com.flex.compiler.lexicalAnalyzer.Operator;
import com.flex.compiler.translator.symbols.Function;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;

public interface Translator {

    void pushConstValue(String value);

    void pushVariable(Variable var);

    void pushFunctionCall(Function function);

    void pushIndex();

    void pushArrayInstantiation(Type arrayType);

    void pushSizeOfArray();

    void pushField(Type type, String fieldName);

    void operation(Operator operator);

    void variableDeclaration(Variable var, boolean needInit);

    void functionDeclaration(Function function);

    void include(String location);

    void nextOperation();

    void createScope();

    void popScope();

    void returnOperation();

    void whileStatement();

    void ifStatement();

    void elseStatement();

    void struct(Type type);

    void translate();
}
