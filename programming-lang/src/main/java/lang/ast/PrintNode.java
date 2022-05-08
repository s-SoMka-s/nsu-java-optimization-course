package lang.ast;

public class PrintNode extends ExpressionNode {
    public ExpressionNode value;

    public PrintNode(ExpressionNode value) {
        this.value = value;
    }
}
