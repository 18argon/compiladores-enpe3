grammar Planner;
//gramatica da linguagem Planner, para criacao de planners

//Inicio do programa
programa
    : 'planner' formato
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
    : 'mensal' '{' corpo_mensal '}'
    ;

//Planner anual
anual
    : 'anual' '{' corpo_semanal '}'
    ;

//Corpo planner semanal
corpo_semanal
    : tarefa_semanal+
    ;

//Corpo planner mensal
corpo_mensal
    : 'inicio' ':' dia_semana ';' tarefa+
    ;

//Inicio tarefa semanal e anual
tarefa_semanal
    : TAREFA '{' data_semanal hora? descricao?'}'
    ;

//Inicio tarefa
tarefa
    : TAREFA '{'data hora? descricao?'}'
    ;

//Data da tarefa semanal
data_semanal
    : 'data' ('inicio')? ':' dia_semana (NUMERO'/'NUMERO)? ('data' 'fim' ':' dia_semana (NUMERO'/'NUMERO)?)?
    ;

//Data da tarefa mensal
data
    : 'data' ('inicio')? ':' dia_semana? NUMERO'/'NUMERO ('data' 'fim' ':' dia_semana? NUMERO'/'NUMERO)?
    ;

//Horario da tarefa
hora
    : 'hora' ('inicio')? ':' NUMERO ':' NUMERO ('hora' 'fim' ':' NUMERO ':' NUMERO)?
    ;

//Descricao da tarefa
descricao
    : 'descricao' ':' DESCRICAO
    ;

//Opcoes para dias da semana
dia_semana
    : 'domingo'
    | 'segunda-feira'
    | 'terca-feira'
    | 'quarta-feira'
    | 'quinta-feira'
    | 'sexta-feira'
    | 'sabado'
    ;

//Formato para descricao de tarefa
DESCRICAO
    : '"' ( ~('"' | '\n') )* '"'
    ;

//Nome tarefa
TAREFA
    : ('a'..'z' | 'A'..'Z') ('a'..'z'| 'A'..'Z' | '0'..'9')*
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