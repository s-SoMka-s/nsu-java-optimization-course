package lang.ast;


// const number: int = 12;
// var name: string = "Alex";
public class VariableDeclarationNode extends ExpressionNode {
    private final String name;
    private final VariableType type;
    private final VariableMutabilityType mutabilityType;
    private final ExpressionNode expression;

    public VariableDeclarationNode(String name, VariableMutabilityType mutabilityType, VariableType type, ExpressionNode expression) {
        this.name = name;
        this.type = type;
        this.mutabilityType = mutabilityType;
        this.expression = expression;
    }
}

