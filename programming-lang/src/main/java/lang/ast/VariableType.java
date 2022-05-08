package lang.ast;

import lang.Token;

public enum VariableType {
    INT,
    STRING;

    public static VariableType fromToken(Token token) {
        return INT;
    }
}
