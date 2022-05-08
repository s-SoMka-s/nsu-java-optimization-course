package lang;

import lang.ast.*;

public class Runner {

    public void run(ExpressionNode root) {
        for (var node:root.children) {
            if (node instanceof PrintNode){
                runPrint((PrintNode) node);
            }
        }
    }

    private void runPrint(PrintNode node) {
        var child = node.value;
        if (child instanceof LiteralNode) {
            var value = ((LiteralNode) child).getValue();
            System.out.println(value.text);
            return;
        }

        if (child instanceof VariableIdentifierNode) {
            System.out.println("Тут будет переменная");
            return;
        }

        if (child instanceof BinaryOperatorNode) {
            var res = runBinaryOperator((BinaryOperatorNode) child);
        }

    }

    private int runBinaryOperator(BinaryOperatorNode node) {
        if (node.operator.type == TokenType.MINUS) {
            return this.run(node.leftNode) - this.run(node.rightNode)
        }
    }
}
