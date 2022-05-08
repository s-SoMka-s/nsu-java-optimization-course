package lang;

import lang.ast.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    private final ArrayList<Token> tokens;
    private int pos = 0;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public ExpressionNode parse() {
        var root = new ExpressionNode();
        while(this.pos < tokens.size()) {
            var node = this.parseStatement();
            root.add(node);
        }

        return root;
    }

    private ExpressionNode parseStatement() {
        var curToken = this.tokens.get(this.pos);

        switch (curToken.type) {
            case VAR, CONST -> {
                return parseVariableDeclaration();
            }
            case IF -> {
                return parseIfStatement();
            }
            case PRINT -> {
                return parsePrintStatement();
            }
            case LINE_BREAK -> {
                this.pos++;
                return parseStatement();
            }
            default -> throw new IllegalStateException("Unexpected value: " + curToken.type);
        }
    }

    private PrintNode parsePrintStatement() {
        this.require(TokenType.PRINT);
        this.require(TokenType.LEFT_PARENTHESIS);
        var expression = this.parseExpression();
        this.require(TokenType.RIGHT_PARENTHESIS);
        this.require(TokenType.SEMICOLON);

        return new PrintNode(expression);
    }

    private BlockNode parseBlockStatement() {
        var root = new BlockNode();

        while (this.match(TokenType.RIGHT_BRACE) == null) {
            var node = this.parseStatement();
            if (node != null) {
                root.add(node);
            }
        }

        return root;
    }

    private IfStatementNode parseIfStatement(){
        this.require(TokenType.IF);

        var test = this.parseExpression();

        ExpressionNode consequent;
        if (this.match(TokenType.LEFT_BRACE) != null) {
            consequent = this.parseBlockStatement();
        }
        else {
            consequent = this.parseStatement();
        }

        if (this.match(TokenType.ELSE) == null) {
            return new IfStatementNode(test, consequent, null);
        }

        ExpressionNode alternative;
        if (this.match(TokenType.LEFT_BRACE) != null) {
            alternative = this.parseBlockStatement();
        }
        else {
            alternative = this.parseStatement();
        }

        return new IfStatementNode(test, consequent, alternative);
    }

    private VariableDeclarationNode parseVariableDeclaration() {
        var mutabilityType = this.tokens.get(this.pos).type == TokenType.CONST ? VariableMutabilityType.CONST : VariableMutabilityType.MUTABLE;
        this.pos++;

        var name = this.require(TokenType.WORD).text;

        this.require(TokenType.COLON);

        var tokenVariableType = this.require(TokenType.VARIABLE_TYPE);
        var variableType = VariableType.fromToken(tokenVariableType);

        this.require(TokenType.ASSIGN);

        var expression = this.parseExpression();
        this.require(TokenType.SEMICOLON);

        return new VariableDeclarationNode(name, mutabilityType, variableType, expression);
    }

    private ExpressionNode parseExpression() {
        var leftNode = this.parseParentheses();
        var operator = this.match(TokenType.PLUS, TokenType.MINUS, TokenType.EQUALITY);
        while (operator != null) {
            var rightNode = this.parseParentheses();
            leftNode = new BinaryOperatorNode(operator, leftNode, rightNode);
            operator = this.match(TokenType.PLUS, TokenType.MINUS, TokenType.EQUALITY);
        }

        return leftNode;
    }

    private ExpressionNode parseParentheses() {
        if (this.match(TokenType.LEFT_PARENTHESIS) != null) {
            var node = this.parseExpression();
            this.require(TokenType.RIGHT_PARENTHESIS);
            return node;
        } else {
            return this.parseVariableOrLiteral();
        }
    }

    private ExpressionNode parseVariableOrLiteral() {
        var number = this.match(TokenType.NUMBER);
        if (number != null) {
            return new LiteralNode(number);
        }
        var variable = this.match(TokenType.WORD);
        if (variable != null) {
            return new VariableIdentifierNode(variable);
        }
        throw new Error("Ожидается число на ${this.pos} позиции");
    }

    private Token match(TokenType... expected) {
        if (this.pos < this.tokens.size()) {
            var currentToken = this.tokens.get(this.pos);
            if (Arrays.stream(expected).anyMatch(type -> type == currentToken.type)) {
                this.pos += 1;
                return currentToken;
            }
        }
        return null;
    }

    private Token require(TokenType... expected) {
        var token = this.match(expected);
        if (token == null) {
            throw new Error("на позиции " + this.pos + " ожидается " + expected[0].name());
        }

        return token;
    }
}
