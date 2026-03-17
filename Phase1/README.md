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

* **Syntax errors**: handled using Bison’s `error` rule and `yyerrok` for recovery
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
