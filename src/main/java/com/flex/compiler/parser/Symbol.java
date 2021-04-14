package com.flex.compiler.parser;

import java.util.List;

public class Symbol {
    public Symbol(List<String> location, String name) {
        this.location = location;
        this.name = name;
    }

    public List<String> location;
    public String name;
}
