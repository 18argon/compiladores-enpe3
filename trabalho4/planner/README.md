# Planner
A linguagem Planner possibilita a geração de uma página HTML a partir de um código declarativo simplificado.
A sua gramática pode ser encontrada [aqui](https://raw.githubusercontent.com/18argon/compiladores-enpe3/main/trabalho4/planner/gramatica.pdf)

Projeto desenvolvido para a disciplina de Construção de Compiladores no período ENPE-2021/1.

A linguagem de geração de Planner foi desenvolvida pelos alunos Pabolo e Vanessa, no âmbito do DC/UFSCar, com supervisão do professor Daniel Lucrédio. 

O compilador é capaz de gerar páginas HTML de planner anual, mensal ou semanal, tarefas anexadas ao planner podem conter datas e horas de 
início e fim e suas descrições. Ele verifica erros léxicos, sintáticos e semânticos tendo como base a gramática da linguagem Planner e faz 
verificações referentes às datas e horas.

## Grupo:
- Pabolo Vinícius da Rosa Pires \[760648\]
- Vanessa de Cássia Alves  \[795314\]

## Exemplo de uso
O código a seguir descreve um planner anual
```
planner anual {
    ano: 2022
    Inscricao{
        inicio: 15/01 15:00
    }

    Inscricao_Curso{
        inicio: 15/11 15:00
        fim: 16/11 16:00
    }

    Viagem{
        inicio: 04/09 08:00
    }
}
```
A saida pode ser visualizada através deste [link](https://htmlpreview.github.io/?https://github.com/18argon/compiladores-enpe3/blob/main/trabalho4/planner/casos-de-teste/corretos/saida/anual1.html)


## Dependências
Os testes foram realizados numa máquina linux com as seguintes dependências:

- Apache Maven 3.6.3
- Java 11

## Compilação
Para compilar o programa basta utilizar o comando:

``` bash
mvn install
```

Este comando irá gerar a pasta `target` que contém o programa compilado. O analisador pode ser executados com o comando:

``` bash
java -jar "$PWD/target/planner-1.0-SNAPSHOT-jar-with-dependencies.jar" <entrada> <saida>
```

Exemplo usando um caso de teste
``` bash
java -jar "$PWD/target/planner-1.0-SNAPSHOT-jar-with-dependencies.jar" "$PWD/casos-de-teste/corretos/semanal1.txt" "$PWD/tmp/saida.txt"
```

## Execução
Para executar os casos de teste use o comando:

``` bash
java -jar <caminho-analisador> <entrada> <saida>
```

Exemplo:

``` bash
java -jar $PWD/planner.jar "$PWD/casos-de-teste/corretos/semanal1.txt" "$PWD/saida.txt"
```

