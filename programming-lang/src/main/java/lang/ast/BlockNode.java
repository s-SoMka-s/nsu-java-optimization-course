package lang.ast;

import java.util.ArrayList;

public class BlockNode extends ExpressionNode {
    public ArrayList<ExpressionNode> children = new ArrayList<>();

    public void add(ExpressionNode node){
        this.children.add(node);
    }
}
