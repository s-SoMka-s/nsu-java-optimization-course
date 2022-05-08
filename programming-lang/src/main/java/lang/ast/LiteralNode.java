package lang.ast;

import lang.Token;

public class LiteralNode extends ExpressionNode {
    private final Token value;

    public LiteralNode(Token value) {

        this.value = value;
    }

    public Token getValue(){
        return value;
    }
}
