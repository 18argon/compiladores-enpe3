lexer grammar LALexer;

//Palavras chaves revervadas para a programação
PALAVRA_CHAVE
    : 'algoritmo'
    | 'fim_algoritmo'
    | 'declare'
    | 'constante'
    | 'tipo'
    | 'literal'
    | 'inteiro'
    | 'real'
    | 'logico'
    | 'verdadeiro'
    | 'falso'
    | 'registro'
    | 'fim_registro'
    | 'procedimento'
    | 'fim_procedimento'
    | 'funcao'
    | 'fim_funcao'
    | 'var'
    | 'leia'
    | 'escreva'
    | 'se'
    | 'entao'
    | 'senao'
    | 'fim_se'
    | 'caso'
    | 'seja'
    | 'fim_caso'
    | 'para'
    | 'ate'
    | 'faca'
    | 'fim_para'
    | 'enquanto'
    | 'fim_enquanto'
    | 'retorne'
    | 'nao'
    | 'ou'
    | 'e'
    ;

//Identificadores, nomes das variáveis
IDENT
    : ('a'..'z' | 'A'..'Z' | '_')('a'..'z'| 'A'..'Z' | '0'..'9' | '_')*
    ;

//Números inteiros
NUM_INT
    : ('0'..'9')+
    ;

//Números reais
NUM_REAL
    : ('0'..'9')+('.'('0'..'9')+)?
    ;

//Cadeia de letras e caracteres entre aspas duplas
CADEIA
    : '"' ( ESC_SEQ | ~('"' | '\\' | '\n') )* '"'
    ;

//Sequência de escape para aspas duplas
fragment
ESC_SEQ
    : '\\"'
    ;

//Espaços em branco: pular linha, tabulação
WS
    : (' ' | '\n' | '\r' | '\t') {skip();}
    ;

//Comentários dentro do programa
COMENTARIO: '{' ( ~('\n') )*? '}' {skip();};

//Operadores relacionais
OP_REL
    : '='
    | '<>'
    | '<'
    | '>'
    | '<='
    | '>='
    ;

//Operadores aritméticos
OP_ARIT
    : '+'
    | '-'
    | '*'
    | '/'
    | '%'
    ;

//Operador de atribuição
OP_ATRIBUICAO
    : '<-'
    ;

//Operador de intervalo, ex:2..9, do 2 até o 9
OP_INTERVALO
    : '..'
    ;

//Abre parênteses
ABREPAR
    : '('
    ;

//Fecha parênteses
FECHAPAR
    : ')'
    ;

//Abre colchetes
ABRECOL
    : '['
    ;

//Fecha colchetes
FECHACOL
    : ']'
    ;

//Delimitadores
DELIM
    : ':'
    | ','
    | '.'
    ;

AND
    : '&'
    ;

CIRCUNFLEXO
    : '^'
    ;

//Qualquer caractere que não faça parte do conjunto léxico
DESCONHECIDO
    : .+?
    ;
