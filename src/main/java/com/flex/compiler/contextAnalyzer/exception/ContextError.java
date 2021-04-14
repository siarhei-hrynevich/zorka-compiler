package com.flex.compiler.contextAnalyzer.exception;

public enum ContextError {
    SymbolExistInPackage,
    SymbolExistInScope,
    SymbolNotDeclared,
    SymbolNotExistInPackage,
    InvalidLeftValue,
    InvalidRightValue,
    ExpectedBody,
    UnexpectedExpression,
    InvalidReturnType,
    UnattainableCode,
    InvalidArgs,
    InvalidValueType,
    ExpectedReturn,
    UndeclaredType
}
