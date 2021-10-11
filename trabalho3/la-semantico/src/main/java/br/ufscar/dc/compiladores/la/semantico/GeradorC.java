package br.ufscar.dc.compiladores.la.semantico;

import java.util.stream.Collectors;

//Classe responsável por gerar em linguagem C os códigos da linguagem LA
public class GeradorC extends LABaseVisitor<Void> {
    StringBuilder saida;
    Escopos escopos;

    GeradorC() {
        saida = new StringBuilder();
        this.escopos = new Escopos();
    }

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        saida.append("#include <stdio.h>\n");
        saida.append("#include <stdlib.h>\n");
        saida.append("\n");
        visitDeclaracoes(ctx.declaracoes());
        saida.append("\n");
        saida.append("int main() {\n");
        visitCorpo(ctx.corpo());
        saida.append("}\n");
        return null;
    }

    @Override
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        String start = ctx.start.getText();
        if (start.equals("declare")) {
            visitVariavel(ctx.variavel());
        } else if (start.equals("constante")) {
            saida.append(ctx.tipo_basico().getText())
                    .append(" ")
                    .append(ctx.IDENT())
                    .append(" = ")
                    .append(ctx.valor_constante().getText())
                    .append(";\n");
        } else {
            saida.append("typedef ");
            visitTipo(ctx.tipo());
            saida.append(";\n");
        }
        return null;
    }

    @Override
    public Void visitVariavel(LAParser.VariavelContext ctx) {
        visitTipo(ctx.tipo());
        saida.append(" ");
        ctx.identificador().forEach(id -> saida.append(id.getText()));
        saida.append(";\n");
        return null;
    }

    @Override
    public Void visitRegistro(LAParser.RegistroContext ctx) {
        saida.append("struct {\n");
        ctx.variavel().forEach(this::visitVariavel);
        saida.append("}");
        return null;
    }

    @Override
    public Void visitTipo_estendido(LAParser.Tipo_estendidoContext ctx) {
        saida.append(ctx.tipo_basico_ident().getText());
        if (ctx.start.getText().equals("^")) {
            // TODO: criar multiplos ponteiros
            saida.append("*");
        }
        return null;
    }

    @Override
    public Void visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
        String start = ctx.start.getText();
        if (start.equals("procedimento")) {
            saida.append("void ");
        } else {
            visitTipo_estendido(ctx.tipo_estendido());
        }

        saida.append(ctx.IDENT())
                .append("(");
        visitParametros(ctx.parametros());
        saida.append(") {\n");
        ctx.cmd().forEach(this::visitCmd);
        saida.append("}\n");
        return null;
    }

    @Override
    public Void visitParametro(LAParser.ParametroContext ctx) {
        boolean ehPonteiro = ctx.tipo_estendido().start.getText().equals("^");
        String tipo = ctx.tipo_estendido().tipo_basico_ident().getText();
        saida.append(tipo);
        String params = ctx.identificador()
                .stream().map(id -> (ehPonteiro ? "*" : "") + id.getText())
                .collect(Collectors.joining(", "));
        saida.append(params);
        return null;
    }

    @Override
    public Void visitCmdLeia(LAParser.CmdLeiaContext ctx) {
        saida.append("printf(");



        saida.append(");\n");
        return null;
    }
}
