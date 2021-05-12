package com.flex.compiler.translator;

import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.translator.symbols.Function;
import com.flex.compiler.translator.symbols.Symbol;
import com.flex.compiler.translator.symbols.Type;
import com.flex.compiler.translator.symbols.Variable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SymbolsTable {
    private final List<String> location;

    private final List<Symbol> symbols = new ArrayList<>();

    private final List<Symbol> importedSymbols = new ArrayList<>();

    public SymbolsTable(List<String> location) {
        this.location = location;
    }

    public void add(Symbol symbol) throws ContextException {
        if(symbols.contains(symbol) || importedSymbols.contains(symbol))
            throw new ContextException(ContextError.SymbolExistInPackage, symbol);
        symbols.add(symbol);
    }

    public void addImport(List<Symbol> imports, String path) throws ContextException {
        for (Symbol symbol : imports) {
            if (symbols.contains(symbol))
                throw new ContextException(ContextError.SymbolExistInPackage, symbol);
        }
        imports.forEach((symbol -> symbol.setLocation(path)));
        importedSymbols.addAll(imports);
    }

    public List<Function> findFunctions(String name) {
        return filterByName(name)
                .filter(s -> s instanceof Function)
                .map(s -> (Function)s)
                .collect(Collectors.toList());
    }

    public Optional<Variable> findVariable(String name) {
        return filterByName(name)
                .filter(s -> s instanceof Variable)
                .map(s -> (Variable)s)
                .findFirst();
    }

    public Optional<Type> findType(String name) {
        return filterByName(name)
                .filter(s -> s instanceof Type)
                .map(s -> (Type)s)
                .findFirst();
    }

    public List<Symbol> findSymbolsByName(String name) {
        return filterByName(name).collect(Collectors.toList());
    }

    public List<Symbol> findSymbolsByNameInPackage(String name) {
        return symbols.stream()
                .filter(s -> s.getName().equals(name))
                .collect(Collectors.toList());
    }

    public List<String> getLocation() {
        return location;
    }

    private Stream<Symbol> filterByName(String name) {
        return Stream.concat(symbols.stream(), importedSymbols.stream())
                .filter(s -> s.getName().equals(name));
    }
}
