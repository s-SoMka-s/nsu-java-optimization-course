package lang;

public enum TokenType {
    VARIABLE_TYPE("(int|string)"),

    VAR("var"),
    CONST("const"),

    EQUALITY("=="),
    PLUS("\\+"),
    MINUS("\\-"),
    ASSIGN("\\="),

    IF("if"),
    ELSE("else"),

    PRINT("print"),

    LEFT_PARENTHESIS("\\("),
    RIGHT_PARENTHESIS("\\)"),

    LEFT_BRACE("\\{"),
    RIGHT_BRACE("\\}"),

    SEMICOLON("\\;"),
    COLON("\\:"),
    QUOTE("\""),
    LINE_BREAK("[\\n\\r]"),
    SPACE("[ \\t]"),
    COMMA("\\,"),

    NUMBER("[0-9]+"),
    WORD("[a-zA-Z]+");

    public final String regex;

    TokenType(String regex) {
        this.regex = regex;
    }
}
