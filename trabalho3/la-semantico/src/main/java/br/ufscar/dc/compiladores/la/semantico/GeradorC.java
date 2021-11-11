package br.ufscar.dc.compiladores.la.semantico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (ctx.declaracoes() != null) {
            visitDeclaracoes(ctx.declaracoes());
            saida.append("\n");
        }
        saida.append("int main() {\n");
        visitCorpo(ctx.corpo());
        saida.append("return 0;\n");
        saida.append("}\n");
        return null;
    }

    //    : 'procedimento' IDENT '(' parametros? ')' declaracao_local* cmd* 'fim_procedimento'
    //    | 'funcao' IDENT '(' parametros? ')' ':' tipo_estendido declaracao_local* cmd* 'fim_funcao'
    @Override
    public Void visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
        String start = ctx.start.getText();
        TiposLA.TipoLA tipoRetorno = TiposLA.INVALIDO;
        if (start.equals("procedimento")) {
            saida.append("void ");

            escopos.criarNovoEscopo(null);
        } else {
            visitTipo_estendido(ctx.tipo_estendido());
            saida.append(" ");

            tipoRetorno = LASemanticoUtils.verificarTipo(escopos, ctx.tipo_estendido());
            escopos.criarNovoEscopo(LASemanticoUtils.
                    verificarTipo(escopos, ctx.tipo_estendido()));
        }

        saida.append(ctx.IDENT()).append(" (");
        if (ctx.parametros() != null) {
            visitParametros(ctx.parametros());
        }
        ctx.declaracao_local().forEach(this::visitDeclaracao_local);
        saida.append(") {\n");

        ctx.cmd().forEach(this::visitCmd);
        saida.append("}\n");

        escopos.abandonarEscopo();

        List<TiposLA.TipoLA> tiposParametros = new ArrayList<>();
        // checar os parametros
        for (var parametro : ctx.parametros().parametro()) {
            for (var pid : parametro.identificador()) {
                String strPid = pid.getText();
                if (escopos.verificar(strPid) != null) {
                    // TODO: erro - nome de parametro já declarado
                    LASemanticoUtils.adicionarErroSemantico(
                            pid.start,
                            String.format(Mensagens.ERRO_IDENTIFICADOR_JA_DECLARADO, strPid));
                } else {
                    TiposLA.TipoLA tipoParametro = LASemanticoUtils.verificarTipo(escopos, parametro.tipo_estendido());
                    tiposParametros.add(tipoParametro);
                    escopos.obterEscopoAtual().adicionar(strPid, tipoParametro);
                }
            }
        }

        TiposLA.TipoLA tipo;
        if (start.equals("procedimento")) {
            tipo = new TiposLA.Procedimento(tiposParametros);
        } else { // funcao
            tipo = new TiposLA.Funcao(tiposParametros, tipoRetorno);
        }
        escopos.obterEscopoAtual().adicionar(ctx.IDENT().getText(), tipo);

        return null;
    }

    @Override
    public Void visitParametro(LAParser.ParametroContext ctx) {
        TiposLA.TipoLA tipo = LASemanticoUtils.verificarTipo(escopos, ctx.tipo_estendido());
        for (var id : ctx.identificador()) {
            String strId = id.getText();

            visitTipo_estendido(ctx.tipo_estendido());
            saida.append(" ");
            if (tipo == TiposLA.LITERAL) {
                saida.append("*");
            }
            saida.append(strId);

            escopos.obterEscopoAtual().adicionar(strId, tipo);
        }
        return null;
    }

    @Override
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        String start = ctx.start.getText();
        if (start.equals("declare")) {
            visitVariavel(ctx.variavel());
        } else if (start.equals("constante")) {
            visitTipo_basico(ctx.tipo_basico());
            saida.append(" ")
                    .append(ctx.IDENT());


            if (ctx.tipo_basico().getText().equals("literal")) {
                saida.append("[]");
            }
            saida.append(" = ")
                    .append(ctx.valor_constante().getText())
                    .append(";\n");

            String strId = ctx.IDENT().getText();
            TiposLA.TipoLA tipoId = LASemanticoUtils.verificarTipo(ctx.tipo_basico());
            escopos.obterEscopoAtual().adicionar(strId, tipoId);
        } else {
            String strId = ctx.IDENT().getText();

            saida.append("typedef ");
            visitTipo(ctx.tipo());
            saida.append(" ")
                    .append(strId)
                    .append(";\n");

            if (ctx.tipo().start.getText().equals("registro")) {
                //extração dos campos
                Map<String, TiposLA.TipoLA> campos = new HashMap<>();
                for (var variavel : ctx.tipo().registro().variavel()) {
                    TiposLA.TipoLA tipoVar = LASemanticoUtils.verificarTipo(escopos, variavel.tipo());
                    for (var varId : variavel.identificador()) {
                        // TODO: Talvez seja preciso mudar a gramática para não aceitar pontos
                        String idVar = varId.id1.getText();
                        if (varId.dimensao() != null && !varId.dimensao().exp_aritmetica().isEmpty()) {
                            campos.put(idVar, new TiposLA.Arranjo(tipoVar));
                        } else {
                            campos.put(idVar, tipoVar);
                        }
                    }
                }
                escopos.obterEscopoAtual().adicionar(strId, new TiposLA.Registro(campos));
            }
        }


        return null;
    }

    @Override
    public Void visitCmd(LAParser.CmdContext ctx) {

        return super.visitCmd(ctx);
    }

    @Override
    public Void visitTipo_basico(LAParser.Tipo_basicoContext ctx) {
        String strTipo = ctx.getText();
        switch (strTipo) {
            case "literal":
                saida.append("char");
                break;
            case "logico":
            case "inteiro":
                saida.append("int");
                break;
            default: // real
                saida.append("float");
                break;
        }
        return null;
    }

    @Override
    public Void visitVariavel(LAParser.VariavelContext ctx) {
        visitTipo(ctx.tipo());
        saida.append(" ");

        String start = ctx.tipo().start.getText();
        String ids = ctx.identificador()
                .stream().map(id -> {
                    if (start.equals("^")) {
                        return "*" + id.getText();
                    } else if (start.equals("literal")) {
                        return id.getText() + "[80]";
                    } else {
                        return id.getText();
                    }
                })
                .collect(Collectors.joining(", "));
        saida.append(ids);

        saida.append(";\n");

        TiposLA.TipoLA tipo = LASemanticoUtils.verificarTipo(escopos, ctx.tipo());
        for (var identificador : ctx.identificador()) {
            String strId = identificador.IDENT(0).getText();
//            var indices = identificador.dimensao().exp_aritmetica();

//            for (int i = indices.size() - 1; i >= 0; i--) {
//                var indice = indices.get(i);
//                TiposLA.TipoLA tipoIndice = LASemanticoUtils.verificarTipo(escopos, indice);
//                if (tipoIndice != TiposLA.INTEIRO) {
//                    tipo = TiposLA.INVALIDO;
//                } else {
//                    tipo = new TiposLA.Arranjo(tipo);
//                }
//            }

            escopos.obterEscopoAtual().adicionar(strId, tipo);
        }

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
    public Void visitTipo_basico_ident(LAParser.Tipo_basico_identContext ctx) {
        if (ctx.tipo_basico() != null) {
            visitTipo_basico(ctx.tipo_basico());
        } else {
            saida.append(ctx.IDENT().getText());
        }
        return null;
    }

    @Override
    public Void visitCmdLeia(LAParser.CmdLeiaContext ctx) {
        for (int i = 0; i < ctx.identificador().size(); i++) {
            TiposLA.TipoLA tipo = LASemanticoUtils
                    .verificarTipo(escopos, ctx.identificador(i));

            if (tipo == TiposLA.LITERAL) {
                saida.append("gets(x);\n");
            } else {


                saida.append("scanf(");


                if (tipo == TiposLA.INTEIRO) {
                    saida.append("\"%d\"");
                } else if (tipo == TiposLA.REAL) {
                    saida.append("\"%f\"");
                }

                saida.append(", ");

                if (ctx.OP_PONTEIRO(i) == null) {
                    saida.append("&");
                }
                saida.append(ctx.identificador(i).getText());
                saida.append(");\n");
            }

        }
        return null;
    }

    @Override
    public Void visitCmdEscreva(LAParser.CmdEscrevaContext ctx) {
        for (int i = 0; i < ctx.expressao().size(); i++) {
            saida.append("printf(");

            TiposLA.TipoLA tipo = LASemanticoUtils
                    .verificarTipo(escopos, ctx.expressao(i));

            if (tipo instanceof TiposLA.Funcao) {
                tipo = ((TiposLA.Funcao) tipo).tipoRetorno;
            }
            if (tipo == TiposLA.INTEIRO) {
                saida.append("\"%d\"");
            } else if (tipo == TiposLA.REAL) {
                saida.append("\"%f\"");
            } else if (tipo == TiposLA.LITERAL) {
                saida.append("\"%s\"");
            }

            saida.append(", ");

            saida.append(ctx.expressao(i).getText());
            saida.append(");\n");
        }
        return null;
    }

    @Override
    public Void visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx) {
        TiposLA.TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());
        if (tipoExp == TiposLA.LITERAL) {
            saida.append("strcpy(")
                    .append(ctx.identificador().getText())
                    .append(", ");
            visitExpressao(ctx.expressao());
            saida.append(");\n");
        } else {
            if (ctx.start.getText().equals("^")) {
                saida.append("*");
            }
            saida.append(ctx.identificador().getText())
                    .append(" = ");
            visitExpressao(ctx.expressao());
            saida.append(";\n");
        }
        return null;
    }

    @Override
    public Void visitExpressao(LAParser.ExpressaoContext ctx) {
        visitTermo_logico(ctx.termo1);
        for (var termo :
                ctx.outrosTermos) {
            saida.append(" || ");
            visitTermo_logico(termo);
        }
        return null;
    }

    @Override
    public Void visitTermo_logico(LAParser.Termo_logicoContext ctx) {
        visitFator_logico(ctx.fator1);
        for (var fator :
                ctx.outrosFatores) {
            saida.append(" && ");
            visitFator_logico(fator);
        }
        return null;
    }

    @Override
    public Void visitFator_logico(LAParser.Fator_logicoContext ctx) {
        if (ctx.start.getText().equals("nao")) {
            saida.append("!");
            visitParcela_logica(ctx.parcela_logica());

        } else {
            visitParcela_logica(ctx.parcela_logica());
        }
        return null;
    }

    @Override
    public Void visitParcela_logica(LAParser.Parcela_logicaContext ctx) {
        if (ctx.exp_relacional() != null) {
            visitExp_relacional(ctx.exp_relacional());
        } else if (ctx.start.getText().equals("verdadeiro")) {
            saida.append("1");
        } else {
            saida.append("0");
        }
        return null;
    }

    @Override
    public Void visitExp_relacional(LAParser.Exp_relacionalContext ctx) {
        visitExp_aritmetica(ctx.exp1);
        if (ctx.op_relacional() != null) {
            String op = ctx.op_relacional().getText();
            if (op.equals("<>")) {
                saida.append(" != ");
            } else if (op.equals("=")) {
                saida.append(" == ");
            } else {
                saida.append(" ").append(op).append(" ");
            }
            visitExp_aritmetica(ctx.exp2);
        }

        return null;
    }

    @Override
    public Void visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) {
        visitTermo(ctx.termo1);
        for (int i = 0; i < ctx.outrosTermos.size(); i++) {
            saida.append(" ")
                    .append(ctx.op.get(i).getText())
                    .append(" ");
            visitTermo(ctx.outrosTermos.get(i));
        }
        return null;
    }

    @Override
    public Void visitTermo(LAParser.TermoContext ctx) {
        visitFator(ctx.fator1);
        for (int i = 0; i < ctx.outrosFatores.size(); i++) {
            saida.append(" ")
                    .append(ctx.OP_ARITIMETICO2(i).getText())
                    .append(" ");
            visitFator(ctx.outrosFatores.get(i));
        }
        return null;
    }

    @Override
    public Void visitFator(LAParser.FatorContext ctx) {
        visitParcela(ctx.parcela1);
        for (int i = 0; i < ctx.outrasParcelas.size(); i++) {
            saida.append(" % ");
            visitParcela(ctx.outrasParcelas.get(i));
        }
        return null;
    }

    @Override
    public Void visitParcela(LAParser.ParcelaContext ctx) {
        if (ctx.parcela_unario() != null) {
            if (ctx.op != null) {
                saida.append("-");
            }
            visitParcela_unario(ctx.parcela_unario());
        } else {
            visitParcela_nao_unario(ctx.parcela_nao_unario());
        }
        return null;
    }

    @Override
    public Void visitParcela_unario(LAParser.Parcela_unarioContext ctx) {
        if (ctx.start.getText().equals("^")) {
            saida.append("*")
                    .append(ctx.identificador().getText());
        } else if (ctx.expParam != null) {
            saida.append("(");
            visitExpressao(ctx.expParam);
            saida.append(")");
        } else if (ctx.IDENT() != null) {
            saida.append(ctx.IDENT().getText())
                    .append("(");
            for (int i = 0; i < ctx.args.size(); i++) {
                visitExpressao(ctx.args.get(i));
                if (i == ctx.args.size() - 1) {
                    saida.append(", ");
                }
            }
            saida.append(")");
        } else {
            saida.append(ctx.getText());
        }
        return null;
    }

    @Override
    public Void visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
        saida.append(ctx.getText());
        return null;
    }

    @Override
    public Void visitCmdSe(LAParser.CmdSeContext ctx) {
        saida.append("if (");
        visitExpressao(ctx.expressao());
        saida.append(") {\n");
        ctx.cmdsEntao.forEach(this::visitCmd);
        if (!ctx.cmdsSenao.isEmpty()) {
            saida.append("} else {\n");
            ctx.cmdsSenao.forEach(this::visitCmd);
        }
        saida.append("}\n");
        return null;
    }

    @Override
    public Void visitCmdCaso(LAParser.CmdCasoContext ctx) {
        saida.append("switch (");
        visitExp_aritmetica(ctx.exp_aritmetica());
        saida.append(") {\n");
        visitSelecao(ctx.selecao());

        if (!ctx.cmd().isEmpty()) {
            // caso padrão
            saida.append("default:\n");
            ctx.cmd().forEach(this::visitCmd);
        }
        saida.append("}\n");

        return null;
    }

    @Override
    public Void visitItem_selecao(LAParser.Item_selecaoContext ctx) {
        for (var intervalo : ctx.constantes().numero_intervalo()) {
            int inicio = Integer.parseInt(intervalo.inicio.getText());
            inicio = intervalo.sinalInicio != null ? -1 * inicio : inicio;
            int fim = inicio;
            if (intervalo.fim != null) {
                fim = Integer.parseInt(intervalo.fim.getText());
                fim = intervalo.sinalFim != null ? -1 * fim : fim;
            }

            for (int i = inicio; i <= fim; i++) {
                saida.append("case ").append(i).append(":\n");
            }
        }

        ctx.cmd().forEach(this::visitCmd);
        saida.append("break;\n");
        return null;
    }

    @Override
    public Void visitCmdPara(LAParser.CmdParaContext ctx) {
        saida.append("for (")
                .append(ctx.IDENT().getText())
                .append(" = ");
        visitExp_aritmetica(ctx.expInicio);
        saida.append("; ")
                .append(ctx.IDENT().getText())
                .append(" <= ");
        visitExp_aritmetica(ctx.expFim);
        saida.append("; ")
                .append(ctx.IDENT().getText())
                .append("++) {\n");
        ctx.cmd().forEach(this::visitCmd);
        saida.append("}\n");

        return null;
    }

    @Override
    public Void visitCmdEnquanto(LAParser.CmdEnquantoContext ctx) {
        saida.append("while (");
        visitExpressao(ctx.expressao());
        saida.append(") {\n");
        ctx.cmd().forEach(this::visitCmd);
        saida.append("}\n");
        return null;
    }

    @Override
    public Void visitCmdFaca(LAParser.CmdFacaContext ctx) {
        saida.append("do {\n");
        ctx.cmd().forEach(this::visitCmd);
        saida.append("} while (");
        visitExpressao(ctx.expressao());
        saida.append(");\n");
        return null;
    }

    @Override
    public Void visitCmdChamada(LAParser.CmdChamadaContext ctx) {
        saida.append(ctx.IDENT().getText())
                .append("(");
        for (int i = 0; i < ctx.expressao().size(); i++) {
            visitExpressao(ctx.expressao(i));
            if (i != ctx.expressao().size()-1) {
                saida.append(", ");
            }
        }
        saida.append(");\n");
        return null;
    }

    @Override
    public Void visitCmdRetorne(LAParser.CmdRetorneContext ctx) {
        saida.append("return ");
        visitExpressao(ctx.expressao());
        saida.append(";\n");
        return null;
    }
}
