package com.flex.compiler.translator;

import com.flex.compiler.contextAnalyzer.SimpleTypes;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.translator.symbols.Function;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;

import java.util.List;
import java.util.Optional;

public class TranslatorContext {

    private SymbolsTable table;

    private final Scope global = new Scope(null);

    private Scope current;

    public Scope getGlobal() {
        return global;
    }

    public TranslatorContext() {
        global.setReturnValue(SimpleTypes.getVoidType());
        current = global;
    }

    public void createScope() {
        Type returnValue = current.getReturnValue();
        current = current.addChild();
        current.setReturnValue(returnValue);

    }

    public void popScope() {
        current = current.getParent();
    }

    public Scope getCurrentScope() {
        return current;
    }

    public SymbolsTable getTable() {
        return table;
    }

    public void setTable(SymbolsTable table) {
        this.table = table;
    }

    public List<Function> findFunctions(String name) {
        return table.findFunctions(name);
    }

//    public Variable findVar(String name) {
//        return Optional.ofNullable(current.findVariable(name))
//                .orElse(table.findVariable(name)
//                        .orElseThrow(() -> new ContextException(ContextError.SymbolNotDeclared)));
//    }

}

