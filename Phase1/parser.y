%{
#include <stdio.h>
#include <stdlib.h>
void yyerror(char *s);
int yylex(void);
%}

%union{
    int num;
}

%token<num> NUMBER
%token PLUS MINUS TIMES DIVIDE LPAREN RPAREN EOL

%type<num> exp term factor

%%

input:
    | input line
    ;

line:
    exp EOL { printf("%d\n", $1); }
    | EOL
    | error EOL { yyerrok; }
    ;

exp:
    term                { $$ = $1; }
    | exp PLUS term     { $$ = $1 + $3; }
    | exp MINUS term    { $$ = $1 - $3; }
    ;

term:
    factor              { $$ = $1; }
    | term TIMES factor { $$ = $1 * $3; }
    | term DIVIDE factor { 
        if ($3 == 0) {
           fprintf(stderr, "Error: Division by zero\n");
           YYERROR; 
        } else {
            $$ = $1 / $3;
        }
    }

    ;

factor:
    NUMBER              { $$ = $1; }
    | LPAREN exp RPAREN { $$ = $2; }
    | MINUS factor        { $$ = -$2; }
    ;

%%

int main() {
    return yyparse();
}

void yyerror(char *s) {
    fprintf(stderr, "Error: %s\n", s);
}