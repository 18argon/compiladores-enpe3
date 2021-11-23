# Planner
Projeto desenvolvido para a disciplina de Construção de Compiladores no período ENPE-2021/1.

A linguagem de geração de Planner foi desenvolvida pelos alunos Pabolo e Vanessa, no âmbito do DC/UFSCar.

## Grupo:
- Pabolo Vinícius da Rosa Pires \[760648\]
- Vanessa de Cássia Alves  \[795314\]

## Dependências
Os testes foram realizados em uma máquina linux com as seguintes dependências:

- Apache Maven 3.6.3
- Java 11
- gcc 9.3.0

## Compilação
Para compilar o programa basta utilizar o comando:

```
mvn install
```

Este comando irá gerar a pasta `target` que contém o programa compilado e os testes podem ser executados com o comando:

```
java -jar $PWD/target/planner-1.0-SNAPSHOT-jar-with-dependencies.jar" "$PWD/casos-de-teste" $PWD/tmp/saida.txt
```

## Execução
Para executar os casos de teste use o comando:

```
java -jar <caminho-analisador>  <caso-de-teste> <saida>
```

Exemplo:

```
java -jar $PWD/planner.jar gcc "" "$PWD/casos-de-teste/corretos/semanal1.txt" "$PWD/tmp/saida.txt"
```

O programa executará todos os casos de teste do analisador semântico e apresentará o resultado no final.
O programa executará todos os casos de teste e gerará os códigos em HTML.
As saídas para cada um dos testes está no diretório de saída informado no comando executado.
