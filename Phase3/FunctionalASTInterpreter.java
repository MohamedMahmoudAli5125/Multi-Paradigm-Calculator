import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

abstract class ASTNode {
    public abstract int evaluate();

    public abstract void print(String prefix, boolean isLeft);
}

class NumberNode extends ASTNode {
    private int value;

    public NumberNode(int value) {
        this.value = value;
    }

    @Override
    public int evaluate() {
        return value;
    }

    @Override
    public void print(String prefix, boolean isLeft) {
        System.out.println(prefix + (isLeft ? "|-- " : "\\-- ") + value);
    }
}

class OperatorNode extends ASTNode {
    private char operator;
    private ASTNode left;
    private ASTNode right;

    public OperatorNode(char operator) {
        this.operator = operator;
    }

    public char getOperator() {
        return operator;
    }

    public void setLeft(ASTNode left) {
        this.left = left;
    }

    public void setRight(ASTNode right) {
        this.right = right;
    }

    public ASTNode getLeft() {
        return left;
    }

    public ASTNode getRight() {
        return right;
    }

    @Override
    public int evaluate() {
        int l = left.evaluate();
        int r = right.evaluate();
        switch (operator) {
            case '+':
                return l + r;
            case '-':
                return l - r;
            case '*':
                return l * r;
            case '/':
                if (r == 0)
                    throw new ArithmeticException("Division by zero");
                return l / r;
            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }

    @Override
    public void print(String prefix, boolean isLeft) {
        System.out.println(prefix + (isLeft ? "|-- " : "\\-- ") + operator);
        String childPrefix = prefix + (isLeft ? "|   " : "    ");
        left.print(childPrefix, true);
        right.print(childPrefix, false);
    }
}

class ExpressionConverter {
    static String[] infixToPrefix(String infix) {

        String[] tokens = infix.trim().split("\\s+");
        String[] reversed = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            reversed[i] = tokens[tokens.length - 1 - i];
        }

        Stack<String> st = new Stack<>();
        List<String> postfix = new ArrayList<>();
        for (String t : reversed) {

            if (isOperator(t)) {

                while (!st.isEmpty() &&
                        precedence(st.peek().charAt(0)) > precedence(t.charAt(0))) {

                    postfix.add(st.pop());
                }

                st.push(t);
            } else {
                postfix.add(t);
            }
        }

        while (!st.isEmpty())
            postfix.add(st.pop());

        String[] prefix = new String[postfix.size()];

        for (int i = 0; i < postfix.size(); i++)
            prefix[i] = postfix.get(postfix.size() - 1 - i);

        return prefix;
    }

    static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-")
                || token.equals("*") || token.equals("/");
    }

    static int precedence(char c) {
        if (c == '*' || c == '/')
            return 2;
        else if (c == '+' || c == '-')
            return 1;
        else
            return -1;
    }

}

class ASTParser {
static record ParseResult(ASTNode node, int nextIdx) {}
static ParseResult build(String[] tokens, int idx) {
    String token = tokens[idx];

    if (isOperator(token)) {
        // Parse left subtree
        ParseResult left = build(tokens, idx + 1);

        // Parse right subtree using nextIdx from left
        ParseResult right = build(tokens, left.nextIdx);

        // Create operator node
        OperatorNode opNode = new OperatorNode(token.charAt(0));
        opNode.setLeft(left.node());
        opNode.setRight(right.node());

        // Return node + next index
        return new ParseResult(opNode, right.nextIdx);
    } else {
        // Base case: number
        return new ParseResult(new NumberNode(Integer.parseInt(token)), idx + 1);
    }
}
    static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-")
                || token.equals("*") || token.equals("/");
    }
}

class TreePrinter {
    static void printRoot(ASTNode root) {
        if (root instanceof OperatorNode) {
            OperatorNode node = (OperatorNode) root;
            System.out.println(node.getOperator());
            node.getLeft().print("", true);
            node.getRight().print("", false);
        } else {
            NumberNode node = (NumberNode) root;
            System.out.println(node.evaluate());
        }
    }
}

class toPrefixConverter {
    static String toPrefix(ASTNode node) {
        if (node instanceof OperatorNode) {
            OperatorNode op = (OperatorNode) node;
            return "(" + op.getOperator()
                    + " " + toPrefix(op.getLeft())
                    + " " + toPrefix(op.getRight())
                    + ")";
        } else {
            NumberNode num = (NumberNode) node;
            return String.valueOf(num.evaluate());
        }
    }

}

class PrefixEvaluator {
    static String[] tokenize(String prefixStr) {
        prefixStr = prefixStr.replace("(", "( ").replace(")", " )");
        return prefixStr.trim().split("\\s+");
    }

    static int applyOp(char op, int a, int b) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0)
                    throw new ArithmeticException("Division by zero");
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unknown: " + op);
        };
    }

    static record EvalResult(int value, int nextIdx) {}

static EvalResult evalPrefix(String[] tokens, int idx) {
    String token = tokens[idx];

    if (token.equals("(")) {
        String op = tokens[idx + 1];
        EvalResult left = evalPrefix(tokens, idx + 2);
        EvalResult right = evalPrefix(tokens, left.nextIdx);
        int val = applyOp(op.charAt(0), left.value, right.value);
        return new EvalResult(val, right.nextIdx + 1); // skip closing ')'
    } else {
        return new EvalResult(Integer.parseInt(token), idx + 1);
    }
}
}

public class FunctionalASTInterpreter {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        while (true) {

            System.out.print("Enter infix expression (space-separated): ");
            String line = sc.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println("No input provided.");
                return;
            }

            try {
                String[] prefix = ExpressionConverter.infixToPrefix(line);
                ASTParser.ParseResult result = ASTParser.build(prefix, 0);
ASTNode root = result.node();
                System.out.println("\nPrefix expression: " + toPrefixConverter.toPrefix(root)); // for debugging
                PrefixEvaluator.EvalResult resultval = PrefixEvaluator.evalPrefix(PrefixEvaluator.tokenize(toPrefixConverter.toPrefix(root)), 0);
System.out.println("Result: " + resultval.value());                                                                                           // style
            } catch (NumberFormatException e) {
                System.out.println("Error: invalid token in expression.");
            } catch (ArithmeticException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}