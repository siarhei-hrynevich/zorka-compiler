package com.flex.compiler.utils;

import com.flex.compiler.Compiler;
import com.flex.compiler.contextAnalyzer.exception.ContextError;
import com.flex.compiler.contextAnalyzer.exception.ContextException;
import com.flex.compiler.parser.exception.ParsingException;
import com.flex.compiler.translator.SymbolsTable;
import com.flex.compiler.translator.symbols.AccessModifier;
import com.flex.compiler.translator.symbols.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SymbolsStorage {

    private static final List<SymbolsTable> tables = new ArrayList<>();

    public static void addTable(SymbolsTable table) {
        tables.add(table);
    }

    private static SymbolsTable loadTable(List<String> location) throws Exception {
        Compiler.loadFile(location);
        return tables.stream()
                .filter(t -> t.getLocation().equals(location))
                .findFirst().get();
    }

    public static SymbolsTable getTable(List<String> location) throws Exception {
        Optional<SymbolsTable> table = tables.stream()
                .filter(t -> t.getLocation().equals(location))
                .findFirst();
        if(table.isEmpty())
            return loadTable(location);
        return table.get();
    }

    public static List<Symbol> getSymbols(List<String> location, String name) throws Exception {
        SymbolsTable table = getTable(location);
        List<Symbol> symbols = table.findSymbolsByNameInPackage(name);
        symbols = symbols.stream()
                .filter(s -> s.getAccessModifier() == AccessModifier.Public)
                .collect(Collectors.toList());
        if (symbols.size() == 0)
            throw new ContextException(ContextError.SymbolNotExistInPackage);
        return symbols;
    }

    public static List<Symbol> getSymbol(List<String> location, String name) throws ContextException, ParsingException, Exception {
        SymbolsTable table = getTable(location);
        return table.findSymbolsByName(name);
    }
}
