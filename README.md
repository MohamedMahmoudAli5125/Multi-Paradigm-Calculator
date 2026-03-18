# Multi-Paradigm Calculator DSL — Lab 1 (CSE-225)

This project implements a **Domain Specific Language (DSL)** for a mathematical calculator, built progressively across three distinct programming paradigms. Each phase introduces a new layer of abstraction and a different way of modeling computation — from low-level lexical analysis, to object-oriented tree structures, to pure functional evaluation.

The three phases are:
1. **Phase 1 — Declarative & Imperative**: A Flex/Bison-based calculator with a hand-crafted grammar.
2. **Phase 2 — Object-Oriented**: A Java AST interpreter modeled using the Composite Design Pattern.
3. **Phase 3 — Functional**: A prefix-notation evaluator applying functional programming constraints on top of the Phase 2 AST.

---
## 🚀 How to Run

### Phase 1: Flex & Bison (C)

**Prerequisites:** `flex`, `bison`, and `gcc`.

Generate the parser and lexer:
```bash
bison -d parser.y
flex lexer.l
```

Compile the generated C files:
```bash
gcc parser.tab.c lex.yy.c -o calculator -lfl
```

Run the interpreter:
```bash
./calculator
```

### Phase 2 & 3: Java AST & Functional Interpreter

**Prerequisites:** Java Development Kit (JDK).

Compile all Java source files:
```bash
javac *.java
```

Run the OOP Visualizer (Phase 2):
```bash
java ASTVisualizer
```

Run the Functional Interpreter (Phase 3):
```bash
java FunctionalASTInterpreter
```
---
## 🔍 Key Considerations in Phase 1

During Phase 1, the main goal was to design and implement a working lexical analyzer and parser for a simple integer calculator using Flex and Bison. The following key considerations were taken into account:

### 1. Token Design (Lexical Analysis)

The lexer was designed to recognize all necessary tokens for arithmetic expressions:

* Integer numbers using the pattern `[0-9]+`
* Arithmetic operators: `+`, `-`, `*`, `/`
* Parentheses: `(` and `)`
* End-of-line to separate expressions

Whitespace characters (spaces and tabs) were ignored to allow flexible input formatting.

Additionally, invalid characters are detected and reported to improve error handling.

---

### 2. Grammar Structure and Precedence

The grammar was carefully structured into three levels:

* `exp` for addition and subtraction
* `term` for multiplication and division
* `factor` for numbers and parentheses

This hierarchical structure ensures correct operator precedence:

* Multiplication and division have higher precedence than addition and subtraction
* Parentheses override precedence

---

### 3. Associativity Handling

Left recursion was used in rules such as:

```text
exp → exp + term
```

This guarantees **left associativity**, meaning expressions like:

```text
10 - 3 - 2
```

are evaluated as:

```text
(10 - 3) - 2
```

---

### 4. Error Handling

Two types of errors were considered:

* **Syntax errors**: handled using Bison's `error` rule and `yyerrok` for recovery
* **Lexical errors**: invalid characters are detected in the lexer and reported

---

### 5. Division by Zero

A runtime check was added in the parser to detect division by zero.
If detected, an error message is printed and parsing continues safely.

---

### 6. Unary Minus Support

Unary minus was implemented in the grammar:

```text
factor → - factor
```

This allows handling negative numbers such as:

```text
-5 + 3
```

---

### 7. Simplicity and Scope

The calculator was intentionally limited to:

* Integer values only
* Basic arithmetic operations

This keeps the implementation simple while focusing on core parsing concepts.

---
## 📌 References

- **Flex (Fast Lexical Analyzer Generator)**, *Flex Manual*, Version 2.6.4.  
  [https://westes.github.io/flex/manual/](https://westes.github.io/flex/manual/)

- **GNU Bison**, *GNU Bison Manual*.  
  [https://www.gnu.org/software/bison/manual/](https://www.gnu.org/software/bison/manual/)

- **Harvard University**, *CS153: Compilers – Lecture 4: Recursive Parsing*.  
  [https://groups.seas.harvard.edu/courses/cs153/2018fa/lectures/Lec04-Parsing.pdf](https://groups.seas.harvard.edu/courses/cs153/2018fa/lectures/Lec04-Parsing.pdf)
  
---
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
```
## 📌 References

- **GeeksforGeeks**, *Convert Infix to Prefix Notation*.  
  https://www.geeksforgeeks.org/dsa/convert-infix-prefix-notation/

- **GeeksforGeeks**, *Building Expression Tree from Prefix Expression*.  
  https://www.geeksforgeeks.org/dsa/building-expression-tree-from-prefix-expression/

- **GeeksforGeeks**, *Expression Tree (Introduction & Evaluation)*.  
  https://www.geeksforgeeks.org/dsa/expression-tree/

---

# Phase 3: Functional Transformation and Evaluation

Phase 3 evolves the project by treating the AST from Phase 2 as an **Intermediate Representation (IR)** and applying functional programming paradigms to it.

## 1. Evolution from Phase 2
This phase continues the work of Phase 2 but changes how the data is processed:
- **Reuse of the Parser:** We use the exact same `ASTParser` and `ASTNode` structure built in Phase 2.
- **Logic Decoupling:** We move the evaluation logic out of the node objects and into a standalone functional evaluator.
- **Data Flow:** `Infix (Input)` -> `AST (Phase 2 Object)` -> `Prefix (Transformation)` -> `Integer (Result)`.

## 2. Functional Transformation
The `toPrefixConverter` class handles the transformation of the AST into a Lisp-style string:
- **Method:** Recursive Pre-order Traversal.
- **Format:** Operands and operators are wrapped in parentheses, e.g., `(+ 5 (* 3 2))`.
- **Constraint:** This transformation is **pure**; it does not modify the original AST objects.

## 3. Functional Evaluation
The `PrefixEvaluator` implements the evaluation using functional constraints:
- **No Global Mutable Variables:** All state is passed via method parameters (like the token index).
- **Pure Recursion:** `evalPrefix` avoids loops, relying entirely on the call stack to process nested parentheses.
- **Pattern Matching:** The evaluator looks at tokens to decide whether to return a number or recurse into an operation, which is the functional alternative to OOP polymorphism.



## 4. Requirement Compliance
- **Transformation must not modify AST:** Verified by the fact that `toPrefix` only reads the nodes to build a new String.
- **Evaluation must be recursive:** `evalPrefix` is 100% recursive.
- **Lisp-style notation:** The output matches the required `(operator left right)` syntax.
