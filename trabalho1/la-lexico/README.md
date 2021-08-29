# Analisador Léxico para a LA(Linguagem Algorítmica)
Projeto desenvolvido para a disciplina de Construção de Compiladores no período ENPE-2021/1.

A linguagem LA foi desenvolvida pelo professor Jander Moreira, no âmbito do DC/UFSCar.

## Grupo:
- Pabolo Vinícius da Rosa Pires \[760648\]
- Vanessa de Cássia Alves  \[795314\]

## Dependências
Os testes foram realizados em uma máquina linux com as seguintes dependências:

- Apache Maven 3.6.3
- Java 11
- gcc 9.3.0
- Corretor Automático [[GitHub]](https://github.com/dlucredio/compiladores-corretor-automatico)


## Compilação
Para compilar o programa basta utilizar o comando:

```
mvn install
```

Este comando irá gerar a pasta `target` que contém o programa compilado e os testes podem ser executados com o comando:

```
java -jar "$PWD/corretor.jar" "java -jar $PWD/target/la-lexico-1.0-SNAPSHOT-jar-with-dependencies.jar" gcc "$PWD/tmp/" "$PWD/casos-de-teste/" "760648, 795314" lexico
```

## Execução
Para executar os casos de teste use o comando:

```
java -jar <caminho-corretor> "java -jar <caminho-analisador>" gcc <diretório-saida> <diretório-casos-de-teste> "[RA dos alunos]" lexico
```

Exemplo:

```
java -jar "$PWD/corretor.jar" "java -jar $PWD/analisador-lexico.jar" gcc "$PWD/tmp/" "$PWD/casos-de-teste/" "760648, 795314" lexico
```

O programa executará todos os casos de teste do analisador sintático e apresentará o resultado no final.
As saídas para cada um dos testes está no diretório de saída informado no comando executado.
