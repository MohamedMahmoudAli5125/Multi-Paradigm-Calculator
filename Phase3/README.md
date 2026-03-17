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