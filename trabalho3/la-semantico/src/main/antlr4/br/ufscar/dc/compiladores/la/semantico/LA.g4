grammar LA;
//Gramatica da linguagem LA
programa
    : declaracoes 'algoritmo' corpo 'fim_algoritmo'
    ;

declaracoes
    : decl_local_global*
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
    : id1=IDENT ('.' outrosIds+=IDENT)* dimensao
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
    : OP_PONTEIRO? tipo_basico_ident
    ;

valor_constante
    : CADEIA
    | NUM_INT
    | NUM_REAL
    | 'verdadeiro'
    | 'falso'
    ;

registro
    : 'registro' variavel* 'fim_registro'
    ;

declaracao_global
    : 'procedimento' IDENT '(' parametros? ')' declaracao_local* cmd* 'fim_procedimento'
    | 'funcao' IDENT '(' parametros? ')' ':' tipo_estendido declaracao_local* cmd* 'fim_funcao'
    ;

parametro
    : 'var'? identificador (',' identificador)* ':' tipo_estendido
    ;

parametros
    : parametro (',' parametro)*
    ;

corpo
    : declaracao_local* cmd*
    ;
//Comandos existentes na linguagem LA
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
    : 'leia' '(' OP_PONTEIRO? identificador (',' OP_PONTEIRO? identificador)* ')'
    ;

cmdEscreva
    : 'escreva' '(' expressao (',' expressao)* ')'
    ;

cmdSe
    : 'se' expressao 'entao' cmd* ('senao' cmd*)? 'fim_se'
    ;

cmdCaso
    : 'caso' exp_aritmetica 'seja' selecao ('senao' cmd*)? 'fim_caso'
    ;

cmdPara
    : 'para' IDENT '<-' expInicio=exp_aritmetica 'ate' expFim=exp_aritmetica 'faca' cmd* 'fim_para'
    ;

cmdEnquanto
    : 'enquanto' expressao 'faca' cmd* 'fim_enquanto'
    ;

cmdFaca
    : 'faca' cmd* 'ate' expressao
    ;

cmdAtribuicao
    : OP_PONTEIRO? identificador '<-' expressao
    ;

cmdChamada
    : IDENT '(' expressao (',' expressao)* ')'
    ;

cmdRetorne
    : 'retorne' expressao
    ;

selecao
    : item_selecao*
    ;

item_selecao
    : constantes ':' cmd*
    ;

constantes
    : numero_intervalo (',' numero_intervalo)*
    ;

numero_intervalo
    : OP_UNARIO? NUM_INT ('..' OP_UNARIO? NUM_INT)?
    ;

//Expressao aritmetica
exp_aritmetica
    : termo1=termo (OP_ARITIMETICO1 outrosTermos+=termo)*
    ;

termo
    : fator1=fator (OP_ARITIMETICO2 outrosFatores+=fator)*
    ;

fator
    : parcela1=parcela (OP_ARITIMETICO3 outrasParcelas+=parcela)*
    ;


parcela
    : OP_UNARIO? parcela_unario
    | parcela_nao_unario
    ;

parcela_unario
    : OP_PONTEIRO? identificador
    | IDENT '(' args+=expressao (',' args+=expressao)* ')'
    | NUM_INT
    | NUM_REAL
    | '(' expParam=expressao ')'
    ;

parcela_nao_unario
    : OP_ENDERECO identificador
    | CADEIA
    ;

//Expressao relacional
exp_relacional
    : exp1=exp_aritmetica (op_relacional exp2=exp_aritmetica)?
    ;


expressao
    : termo1=termo_logico (OP_LOGICO1 outrosTermos+=termo_logico)*
    ;

termo_logico
    : fator1=fator_logico (OP_LOGICO2 outrosFatores+=fator_logico)*
    ;

fator_logico
    : 'nao'? parcela_logica
    ;

parcela_logica
    : exp_relacional
    | ('verdadeiro' | 'falso')
    ;

//operadores aritimeticos
OP_ARITIMETICO1
    : '+'
    | '-'
    ;

OP_ARITIMETICO2
    : '*'
    | '/'
    ;

OP_UNARIO
    : '-'
    ;

OP_ARITIMETICO3
    : '%'
    ;

//operadores logicos
OP_LOGICO1
    : 'ou'
    ;

OP_LOGICO2
    : 'e'
    ;

//operadores relacionais
op_relacional
    : '<>'
    | '='
    | '<'
    | '<='
    | '>='
    | '>'
    ;
//Operador para ponteiro
OP_PONTEIRO
    : '^'
    ;
//operador para endereco
OP_ENDERECO
    : '&'
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
    : [ \n\r\t] -> skip
    ;

//Comentários dentro do programa
COMENTARIO
    : '{' ( ~('\n') )*? '}' -> skip
    ;

//Qualquer caractere que não faça parte do conjunto léxico
DESCONHECIDO
    : .+?
    ;
