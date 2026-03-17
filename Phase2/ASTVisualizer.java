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
    static ASTNode build(String[] tokens, int[] idx) {
        String token = tokens[idx[0]++];

        if (isOperator(token)) {
            OperatorNode node = new OperatorNode(token.charAt(0));
            node.setLeft(build(tokens, idx));
            node.setRight(build(tokens, idx));
            return node;
        } else {
            return new NumberNode(Integer.parseInt(token));
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

public class ASTVisualizer {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("=== AST Visualizer ===");
        System.out.println("Enter infix expressions to visualize their Abstract Syntax Tree");
        System.out.println("Type 'exit' or 'quit' to end the program\n");

        int expressionCount = 0;

        while (true) {
            System.out.print("[" + (expressionCount + 1) + "] Enter expression: ");
            String line = sc.nextLine().trim();

            // Check for exit commands
            if (line.isEmpty()) {
                System.out.println("No input provided. Type 'exit' to quit.\n");
                continue;
            }

            if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                System.out.println("\nExiting AST Visualizer.");
                break;
            }

            try {
                String[] prefix = ExpressionConverter.infixToPrefix(line);
                ASTNode root = ASTParser.build(prefix, new int[] { 0 });

                System.out.println("\n ABSTRACT SYNTAX TREE:");
                TreePrinter.printRoot(root);

                System.out.println("\n EVALUATION RESULT:");
                double result = root.evaluate();
                System.out.printf("   %s = %d \n", line, (int) result);

                expressionCount++;

            } catch (NumberFormatException e) {
                System.out.println("\n Error: Invalid token in expression.");
                System.out.println("   Please use numbers and operators (+, -, *, /)\n");
            } catch (ArithmeticException e) {
                System.out.println("\n Error: " + e.getMessage() + "\n");
            } catch (Exception e) {
                System.out.println("\n Unexpected error: " + e.getMessage() + "\n");
            }
        }

        sc.close();
    }
}