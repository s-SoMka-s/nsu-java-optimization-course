package lang.ast;

import lang.Token;

public class BinaryOperatorNode extends ExpressionNode{
    public Token operator;
    public ExpressionNode leftNode;
    public ExpressionNode rightNode;

    public BinaryOperatorNode(Token operator, ExpressionNode leftNode, ExpressionNode rightNode){
        this.operator = operator;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }
}
