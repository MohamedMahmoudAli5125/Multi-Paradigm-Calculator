%{
#include "parser.tab.h"
#include <stdlib.h>
%}

%%

[0-9]+ {
    yylval.num = atoi(yytext);
     return NUMBER;
     }

"+"      { return PLUS; }
"-"     { return MINUS; }
"*"      { return TIMES; }
"/"      { return DIVIDE; }
"("      { return LPAREN; }
")"      { return RPAREN; }

\n     { return EOL; }  

[ \t]  {}
. {
    printf("Invalid character: %s\n", yytext);
}
%%

int yywrap() {
    return 1;
}

