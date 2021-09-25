grammar LA;

programa
    : declaracoes 'algoritmo' corpo 'fim_algoritmo'
    ;

declaracoes
    : (decl_local_global)*
    ;

decl_local_global
    : declaracao_local
    | declaracao_global
    ;

declaracao_local
    : 'declare' variavel
    | 'constante' IDENT ':' tipo_basico '=' valor_constante
    | 'tipo' IDENT ':' tipo;

variavel
    : identificador (',' identificador)* ':' tipo
    ;

identificador
    : IDENT ('.' IDENT)* dimensao
    ;

dimensao
    : ('[' exp_aritmetica ']')*
    ;

tipo
    : registro
    | tipo_estendido
    ;

tipo_basico
    : 'literal'
    | 'inteiro'
    | 'real'
    | 'logico'
    ;

tipo_basico_ident
    : tipo_basico
    | IDENT
    ;

tipo_estendido
    : ('^')? tipo_basico_ident
    ;

valor_constante
    : CADEIA
    | NUM_INT
    | NUM_REAL
    | 'verdadeiro'
    | 'falso'
    ;

registro
    : 'registro' (variavel)* 'fim_registro'
    ;

declaracao_global
    : 'procedimento' IDENT '(' (parametros)? ')' (declaracao_local)* (cmd)* 'fim_procedimento'
    | 'funcao' IDENT '(' (parametros)? ')' ':' tipo_estendido (declaracao_local)* (cmd)* 'fim_funcao'
    ;

parametro
    : ('var')? identificador (',' identificador)* ':' tipo_estendido
    ;

parametros
    : parametro (',' parametro)*
    ;

corpo
    : (declaracao_local)* (cmd)*
    ;

cmd
    : cmdLeia
    | cmdEscreva
    | cmdSe
    | cmdCaso
    | cmdPara
    | cmdEnquanto
    | cmdFaca
    | cmdAtribuicao
    | cmdChamada
    | cmdRetorne
    ;

cmdLeia
    : 'leia' '(' ('^')? identificador (',' ('^')? identificador)* ')'
    ;

cmdEscreva
    : 'escreva' '(' expressao (',' expressao)* ')'
    ;

cmdSe
    : 'se' expressao 'entao' (cmd)* ('senao' (cmd)*)? 'fim_se'
    ;

cmdCaso
    : 'caso' exp_aritmetica 'seja' selecao ('senao' (cmd)*)? 'fim_caso'
    ;

cmdPara
    : 'para' IDENT '<-' exp_aritmetica 'ate' exp_aritmetica 'faca' (cmd)* 'fim_para'
    ;

cmdEnquanto
    : 'enquanto' expressao 'faca' (cmd)* 'fim_enquanto'
    ;

cmdFaca
    : 'faca' (cmd)* 'ate' expressao
    ;

cmdAtribuicao
    : ('^')? identificador '<-' expressao;

cmdChamada
    : IDENT '(' expressao (',' expressao)* ')'
    ;

cmdRetorne
    : 'retorne' expressao
    ;

selecao
    : (item_selecao)*
    ;

item_selecao
    : constantes ':' (cmd)*
    ;

constantes
    : numero_intervalo (',' numero_intervalo)*
    ;

numero_intervalo
    : (op_unario)? NUM_INT ('..' (op_unario)? NUM_INT)?
    ;

op_unario
    : '-'
    ;

exp_aritmetica
    : termo (OP_ARITIMETICO1 termo)*
    ;

termo
    : fator (OP_ARITIMETICO2 fator)*
    ;

fator
    : parcela (OP_ARITIMETICO3 parcela)*
    ;


parcela
    : (op_unario)? parcela_unario
    | parcela_nao_unario
    ;

parcela_unario
    : ('^')? identificador
    | IDENT '(' expressao (',' expressao)* ')'
    | NUM_INT
    | NUM_REAL
    | '(' expressao ')'
    ;

parcela_nao_unario
    : '&' identificador
    | CADEIA;

exp_relacional
    : exp_aritmetica (OP_RELACIONAL exp_aritmetica)?
    ;


expressao
    : termo_logico (OP_LOGICO1 termo_logico)*
    ;

termo_logico
    : fator_logico (OP_LOGICO2 fator_logico)*
    ;

fator_logico
    : ('nao')? parcela_logica
    ;

parcela_logica
    : ('verdadeiro' | 'falso')
    | exp_relacional
    ;

OP_ARITIMETICO1
    : '+'
    | '-'
    ;

OP_ARITIMETICO2
    : '*'
    | '/'
    ;

OP_ARITIMETICO3
    : '%'
    ;


OP_LOGICO1

    : 'ou'
    ;

OP_LOGICO2
    : 'e'
    ;

OP_RELACIONAL
    : '='
    | '<>'
    | '>='
    | '<='
    | '>'
    | '<'
    ;


//Identificadores, nomes das variáveis
IDENT
    : ('a'..'z' | 'A'..'Z' | '_') ('a'..'z'| 'A'..'Z' | '0'..'9' | '_')*
    ;

//Números inteiros
NUM_INT
    : ('0'..'9')+
    ;

//Números reais
NUM_REAL
    : ('0'..'9')+ ('.'('0'..'9')+)?
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
COMENTARIO
    : '{' ( ~('\n') )*? '}' {skip();}
    ;

//Qualquer caractere que não faça parte do conjunto léxico
DESCONHECIDO
    : .+?
    ;