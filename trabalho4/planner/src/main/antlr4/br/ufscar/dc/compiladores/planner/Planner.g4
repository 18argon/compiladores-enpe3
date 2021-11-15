grammar Planner;
//gramatica da linguagem Planner, para criacao de planners

//Inicio do programa
programa
    : 'planner' formato EOF
    ;

//Tipos de planner
formato
    : semanal 
    | mensal 
    | anual
    ;

//Planner semanal
semanal
    : 'semanal' '{' corpo_semanal '}'
    ;

//Planner mensal
mensal
    : 'mensal' '{'  corpo_mensal '}'
    ;

//Planner anual
anual
    : 'anual' '{' corpo_anual '}'
    ;

ano
    : 'ano' ':' NUMERO
    ;

mes
    : 'mes' ':' NUMERO'/'NUMERO
    ;

//Corpo planner semanal
corpo_semanal
    : tarefa_semanal+
    ;

//Corpo planner mensal
corpo_mensal
    : mes tarefa_mensal+
    ;

//Corpo planner mensal
corpo_anual
    : ano tarefa_anual+
    ;


//Inicio tarefa semanal e anual
tarefa_semanal
    : TAREFA '{' data_semanal descricao?'}'
    ;

//Inicio tarefa
tarefa_mensal
    : TAREFA '{' data_mensal descricao?'}'
    ;

//Inicio tarefa semanal e anual
tarefa_anual
    : TAREFA '{' data_anual descricao?'}'
    ;

//Data da tarefa semanal
data_semanal
    : 'inicio' ':' dia_da_semana horario? ('fim' ':' dia_da_semana horario?)?
    ;

data_mensal
    : 'inicio' ':' dia horario? ('fim' ':' dia horario? )?
    ;

data_anual
    : 'inicio' ':' dia_mes horario? ('fim' ':' dia_mes horario? )?
    ;

//Descricao da tarefa
descricao
    : 'descricao' ':' DESCRICAO
    ;

//Opcoes para dias da semana
dia_da_semana
    : DOMINGO
    | SEGUNDA
    | TERCA
    | QUARTA
    | QUINTA
    | SEXTA
    | SABADO
    ;

dia_mes : NUMERO'/'NUMERO;

dia : NUMERO;

horario: NUMERO':'NUMERO;


DOMINGO
    : 'domingo'
    | '1'
    ;

SEGUNDA
    : 'segunda-feira'
    | 'segunda'
    | '2'
    ;

TERCA
    : 'terca-feira'
    | 'terca'
    | '3'
    ;

QUARTA
    : 'quarta-feira'
    | 'quarta'
    | '4'
    ;

QUINTA
    : 'quinta-feira'
    | 'quinta'
    | '5'
    ;

SEXTA
    : 'sexta-feira'
    | 'sexta'
    | '6'
    ;

SABADO
    : 'sabado'
    | '7'
    ;

//Formato para descricao de tarefa
DESCRICAO
    : '"' ( ~('"' | '\n') )* '"'
    ;

//Nome tarefa
TAREFA
    : ('a'..'z' | 'A'..'Z') ('a'..'z'| 'A'..'Z' | '0'..'9' | '-' | '_')*
    ;

//Formato para dia
NUMERO
    : [0-9]+
    ;

//Espaços em branco: pular linha, tabulação
WS
    : [ \n\r\t] -> skip
    ;

//Comentários dentro do programa
COMENTARIO
    : '/*' ( ~('\n') )*? '*/' -> skip
    ;

//Qualquer caractere que não faça parte do conjunto léxico
DESCONHECIDO
    : .+?
    ;