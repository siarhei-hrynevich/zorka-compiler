package com.flex.compiler.parser.exception;

public enum Error {
    UnexpectedToken,
    UnrecognizedOperation,
    NeedDeclaration,
    NeedIdentifier,
    NeedTypeName,
    UnexpectedEnd,
    ExpectedVariable,
    ExpectedValue,
    ExpectedAssignment
}
