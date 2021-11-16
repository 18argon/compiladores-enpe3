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

//Corpo planner semanal
corpo_semanal
    : tarefa_semanal+
    ;

//Corpo planner mensal
corpo_mensal
    : campo_mes tarefa_mensal+
    ;

//Corpo planner anual
corpo_anual
    : campo_ano tarefa_anual+
    ;

//Formato tarefas
tarefa_semanal
    : TAREFA '{' data_semanal campo_descricao?'}'
    ;

tarefa_mensal
    : TAREFA '{' data_mensal campo_descricao?'}'
    ;

tarefa_anual
    : TAREFA '{' data_anual campo_descricao?'}'
    ;

//Data das tarefas
data_semanal
    : 'inicio' ':' dia_inicio=dia_da_semana horario_inicio=horario? ('fim' ':' dia_fim=dia_da_semana horario_fim=horario?)?
    ;

data_mensal
    : 'inicio' ':' dia_inicio=dia_do_mes horario_inicio=horario? ('fim' ':' dia_fim=dia_do_mes horario_fim=horario? )?
    ;

data_anual
    : 'inicio' ':' dia_inicio=dia_mes horario_inicio=horario? ('fim' ':' dia_fim=dia_mes horario_fim=horario? )?
    ;

//Ano de referencia
campo_ano
    : 'ano' ':' ano=NUMERO
    ;
//Mes e ano de referencia
campo_mes
    : 'mes' ':' mes=NUMERO'/'ano=NUMERO
    ;

//Descricao da tarefa
campo_descricao
    : 'descricao' ':' DESCRICAO
    ;

//Opcoes para dias da semana
dia_da_semana
    : SEGUNDA
    | TERCA
    | QUARTA
    | QUINTA
    | SEXTA
    | SABADO
    | DOMINGO
    ;

//Formatos para dia do mes tarefa anual
dia_mes : dia=NUMERO'/'mes=NUMERO;

//Dia do mes tarefa mensal
dia_do_mes : dia=NUMERO;

//Formato horario
horario: hora=NUMERO':'minuto=NUMERO;

//Formatos aceitos para dias da semana
SEGUNDA
    : 'segunda-feira'
    | 'segunda'
    | '1'
    ;

TERCA
    : 'terca-feira'
    | 'terca'
    | '2'
    ;

QUARTA
    : 'quarta-feira'
    | 'quarta'
    | '3'
    ;

QUINTA
    : 'quinta-feira'
    | 'quinta'
    | '4'
    ;

SEXTA
    : 'sexta-feira'
    | 'sexta'
    | '5'
    ;

SABADO
    : 'sabado'
    | '6'
    ;

DOMINGO
    : 'domingo'
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