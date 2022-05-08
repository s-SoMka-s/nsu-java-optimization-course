package lang.ast;

import lang.Token;

// var name: string = "Alex";
// print(name);
public class VariableIdentifierNode extends ExpressionNode {
    public Token variable;

    public VariableIdentifierNode(Token variable){
        this.variable = variable;
    }
}
