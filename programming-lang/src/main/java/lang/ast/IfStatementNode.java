package lang.ast;

public class IfStatementNode extends ExpressionNode {
    public ExpressionNode test;
    public ExpressionNode consequent;
    public ExpressionNode alternative;

    public IfStatementNode(ExpressionNode test, ExpressionNode consequent, ExpressionNode alternative) {
        this.test = test;
        this.consequent = consequent;
        this.alternative = alternative;
    }
}
