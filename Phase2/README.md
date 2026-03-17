# AST Interpreter - Phase 2 (Object-Oriented)

This project implements a mathematical expression interpreter using an **Abstract Syntax Tree (AST)**. The primary goal is to shift from procedural calculation to an object-oriented model where expressions are treated as hierarchical data structures.

## 1. Project Overview
The interpreter follows a three-step pipeline:
1.  **Conversion:** Translates an infix expression (e.g., `5 + 3 * 2`) into prefix notation.
2.  **Parsing:** Builds a tree of objects representing the operations.
3.  **Evaluation:** Recursively traverses the tree to produce an integer result.

## 2. Arithmetic Expression Modeling
In this OOP phase, the expression is modeled using the **Composite Design Pattern**.

* **ASTNode (Component):** An abstract base class defining the contract for all nodes.
* **NumberNode (Leaf):** Represents a terminal value (an integer).
* **OperatorNode (Composite):** Represents an operation (e.g., `+`, `-`, `*`, `/`). It holds references to two `ASTNode` children, allowing for nested complexity.



## 3. Engineering Principles

### OOP Paradigm (The Four Pillars)
* **Abstraction:** The system interacts with the `ASTNode` interface, hiding the specific logic of whether a node is a number or an operator.
* **Inheritance:** `NumberNode` and `OperatorNode` inherit from `ASTNode`, ensuring they share the same base structure.
* **Polymorphism:** The `evaluate()` method is polymorphic. Calling it on the root node triggers a chain of calls that are resolved at runtime based on the specific node type.
* **Encapsulation:** Internal states (like the node's value or operator type) are kept private and managed through constructors and methods.

### SOLID Implementation
* **Single Responsibility Principle (SRP):** The logic is divided into specialized classes: `ExpressionConverter` (string manipulation), `ASTParser` (structure building), and `TreePrinter` (visualization).
* **Open/Closed Principle (OCP):** New operations (like Unary Minus or Power) can be added by creating new subclasses of `ASTNode` without modifying existing node classes.
* **Dependency Inversion Principle (DIP):** The parser and printer depend on the abstract `ASTNode` rather than the concrete implementations.

## 4. Technical Constraints
* **Integer Math:** Per requirements, `evaluate()` returns an `int`. This means the interpreter performs integer division (e.g., `5 / 4 = 1`).
* **Space Separation:** The input tokens must be space-separated to ensure accurate tokenization.

## 5. Visualization
The interpreter generates a graphical ASCII tree for debugging purposes. It uses a recursive depth-first search (DFS) to display the hierarchy.

**Example Input:** `5 + 3 * 2`
**Output AST:**
```text
+
|-- 5
\-- *
    |-- 3
    \-- 2
