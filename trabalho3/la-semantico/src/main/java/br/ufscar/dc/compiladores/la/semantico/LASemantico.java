package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.TiposLA.TipoLA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LASemantico extends LABaseVisitor<Void> {
    private final Escopos escopos;

    LASemantico() {
        escopos = new Escopos();
    }

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        escopos.init();
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        String start = ctx.start.getText();
        if (start.equals("declare")) {
            //verificação da declaração de variáveis

            visitVariavel(ctx.variavel());
        } else if (start.equals("constante")) {
            //verificação da declação de constantes

            String strId = ctx.IDENT().getText();
            TiposLA.TipoLA tipoId = LASemanticoUtils.verificarTipo(ctx.tipo_basico());
            TipoLA tipoValor = LASemanticoUtils.verificarTipo(ctx.valor_constante());

            if (tipoId != tipoValor) {
                LASemanticoUtils.adicionarErroSemantico(
                        ctx.IDENT().getSymbol(),
                        String.format(Mensagens.ERRO_ATRIBUICAO_NAO_COMPATIVEL, strId)
                );
                // TODO: verificar erro de atribuição

            }

            escopos.obterEscopoAtual().adicionar(strId, tipoId);

        } else {
            // verificação da declaração de tipos

            String strId = ctx.IDENT().getText();
            EntradaTabelaDeSimbolos etds = escopos.verificar(strId);
            if (etds != null) {
                // TODO: Erro - identificador já declarado
                LASemanticoUtils.adicionarErroSemantico(
                        ctx.IDENT().getSymbol(),
                        String.format(Mensagens.ERRO_IDENTIFICADOR_JA_DECLARADO, strId));
            }

            if (ctx.tipo().start.getText().equals("registro")) {
                // verificação da declaração de registro
                Map<String, TipoLA> campos = new HashMap<>();
                for (var variavel : ctx.tipo().registro().variavel()) {
                    TipoLA tipoVar = LASemanticoUtils.verificarTipo(escopos, variavel.tipo());
                    for (var varId : variavel.identificador()) {
                        // TODO: Talvez seja preciso mudar a gramática para não aceitar pontos
                        String idVar = varId.id1.getText();
                        if (campos.containsKey(idVar)) {
                            // TODO: identificador já declarado
                            LASemanticoUtils.adicionarErroSemantico(
                                    ctx.IDENT().getSymbol(),
                                    String.format(
                                            Mensagens.ERRO_IDENTIFICADOR_JA_DECLARADO,
                                            idVar)
                            );
                        }
                        if (varId.dimensao() != null && !varId.dimensao().exp_aritmetica().isEmpty()) {
                            campos.put(idVar, new TiposLA.Arranjo(tipoVar));
                        } else {
                            campos.put(idVar, tipoVar);
                        }
                    }
                }
                escopos.obterEscopoAtual()
                        .adicionar(ctx.IDENT().getText(), new TiposLA.Registro(campos));
            }
        }
        return null;
    }

    @Override
    public Void visitVariavel(LAParser.VariavelContext ctx) {
        TipoLA tipo = LASemanticoUtils.verificarTipo(escopos, ctx.tipo());

        if (tipo == TiposLA.INVALIDO) {

            // TODO: Adicionar erro de tipo inválido
            LASemanticoUtils.adicionarErroSemantico(
                    ctx.tipo().start,
                    String.format(Mensagens.ERRO_TIPO_NAO_DECLARADO, ctx.tipo().getText()));
        }

        for (var identificador : ctx.identificador()) {
            EntradaTabelaDeSimbolos etds = escopos.verificar(identificador.getText());
            if (etds != null) {
                // TODO: Adicionar erro de identificador já existe
                LASemanticoUtils.adicionarErroSemantico(identificador.IDENT(0).getSymbol(),
                        String.format(
                                Mensagens.ERRO_IDENTIFICADOR_JA_DECLARADO,
                                identificador.getText()));

            }

            String id = identificador.IDENT(0).getText();
            var indices = identificador.dimensao().exp_aritmetica();

            for (int i = indices.size() - 1; i >= 0; i--) {
                var indice = indices.get(i);
                TipoLA tipoIndice = LASemanticoUtils.verificarTipo(escopos, indice);
                if (tipoIndice != TiposLA.INTEIRO) {
                    tipo = TiposLA.INVALIDO;
                } else {
                    tipo = new TiposLA.Arranjo(tipo);
                }
            }

            if (etds == null) {
                escopos.obterEscopoAtual().adicionar(id, tipo);
            }
        }

        return null;
    }

    @Override
    public Void visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
        TipoLA tipo;

        String start = ctx.start.getText();
        String identificador = ctx.IDENT().getText();
        TipoLA tipoRetorno = TiposLA.INVALIDO;
        if (start.equals("procedimento")) {
            escopos.criarNovoEscopo(null);
        } else { // funcao
            tipoRetorno = LASemanticoUtils.verificarTipo(escopos, ctx.tipo_estendido());
            escopos.criarNovoEscopo(tipoRetorno);
        }

        List<TipoLA> tiposParametros = new ArrayList<>();
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
                    TipoLA tipoParametro = LASemanticoUtils.verificarTipo(escopos, parametro.tipo_estendido());
                    tiposParametros.add(tipoParametro);
                    escopos.obterEscopoAtual().adicionar(strPid, tipoParametro);
                }
            }
        }

        if (start.equals("procedimento")) {
            tipo = new TiposLA.Procedimento(tiposParametros);
        } else { // funcao
            tipo = new TiposLA.Funcao(tiposParametros, tipoRetorno);
        }

        ctx.declaracao_local().forEach(this::visitDeclaracao_local);

        ctx.cmd().forEach(this::visitCmd);

        escopos.abandonarEscopo();

        // TODO: adicionar somente se sem erros
        escopos.obterEscopoAtual().adicionar(identificador, tipo);

        return null;
    }

    @Override
    public Void visitCmdSe(LAParser.CmdSeContext ctx) {
        TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());
        if (tipoExp != TiposLA.LOGICO) {
            // TODO: algum erro
        }
        ctx.cmd().forEach(this::visitCmd);
//        return super.visitCmdSe(ctx);
        return null;
    }

    @Override
    public Void visitCmdCaso(LAParser.CmdCasoContext ctx) {
        TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.exp_aritmetica());
        if (tipoExp != TiposLA.INTEIRO) {
            // TODO: Algum erro
        }

//        ctx.selecao().item_selecao().forEach(this::visitItem_selecao);

        ctx.cmd().forEach(this::visitCmd);

        return null;
    }

    @Override
    public Void visitCmdPara(LAParser.CmdParaContext ctx) {
        EntradaTabelaDeSimbolos etds = escopos.verificar(ctx.IDENT().getText());
        if (etds == null) {
            // TODO: erro - variavel nao declarada
            LASemanticoUtils.adicionarErroSemantico(
                    ctx.IDENT().getSymbol(),
                    String.format(Mensagens.ERRO_IDENTIFICADOR_NAO_DECLARADO, ctx.IDENT()));
        } else if (etds.tipo != TiposLA.INTEIRO) {
            // TODO: erro - tipo de variavel errado
        }

        ctx.cmd().forEach(this::visitCmd);

        return null;
    }

    @Override
    public Void visitCmdEnquanto(LAParser.CmdEnquantoContext ctx) {
        visitExpressao(ctx.expressao());
        TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());
        if (tipoExp != TiposLA.LOGICO) {
            // TODO: Algum erro
        }

        ctx.cmd().forEach(this::visitCmd);
        return null;
    }

    @Override
    public Void visitCmdFaca(LAParser.CmdFacaContext ctx) {
        visitExpressao(ctx.expressao());
        TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());
        if (tipoExp != TiposLA.LOGICO) {
            // TODO: Algum erro
        }

        ctx.cmd().forEach(this::visitCmd);
        return null;
    }

    @Override
    public Void visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx) {
        TipoLA tipoId = LASemanticoUtils.verificarTipo(escopos, ctx.identificador());

        if (tipoId == TiposLA.INVALIDO) {
            // TODO: variavel não declarada
            LASemanticoUtils.adicionarErroSemantico(
                    ctx.identificador().start,
                    String.format(
                            Mensagens.ERRO_IDENTIFICADOR_NAO_DECLARADO,
                            ctx.identificador().getText()));
        } else if (ctx.OP_PONTEIRO() != null) {
            if (tipoId instanceof TiposLA.Ponteiro) {
                tipoId = ((TiposLA.Ponteiro) tipoId).tipoConteudo;
            } else {
                tipoId = TiposLA.INVALIDO;
            }
        }

        TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());

        if (tipoId == TiposLA.REAL && tipoExp == TiposLA.INTEIRO) {
            // ok
        } else if (tipoId instanceof TiposLA.Ponteiro && tipoExp == TiposLA.ENDERECO) {

        } else if (tipoId != tipoExp && tipoId != TiposLA.INVALIDO) {
            // Não atribuir se a variavel não existe
            // TODO: erro - atribuicao incompativel
            String strId = ctx.identificador().getText();
            strId = ctx.start.getText().equals("^") ? "^" + strId : strId;
            LASemanticoUtils.adicionarErroSemantico(
                    ctx.identificador().start,
                    String.format(
                            Mensagens.ERRO_ATRIBUICAO_NAO_COMPATIVEL,
                            strId));
        }

        return null;
    }

    @Override
    public Void visitCmdRetorne(LAParser.CmdRetorneContext ctx) {
        TipoLA tipoRetorno = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());
        if (escopos.obterTipoDeRetorno() == null) {
            // TODO: erro - uso inapropriado do comando retorne
            LASemanticoUtils.adicionarErroSemantico(
                    ctx.start,
                    Mensagens.ERRO_RETORNE_NAO_PERMITIDO);
        } else if (tipoRetorno != escopos.obterTipoDeRetorno()) {
            // TODO: Mostrar algum erro
        }

        return super.visitCmdRetorne(ctx);
    }

    @Override
    public Void visitIdentificador(LAParser.IdentificadorContext ctx) {
        EntradaTabelaDeSimbolos etds = escopos.verificar(ctx.IDENT(0).getText());
        TipoLA tipoId = TiposLA.INVALIDO;
        if (etds != null) {
            tipoId = etds.tipo;
            if (tipoId instanceof TiposLA.Registro) {
                for (int i = 1; i < ctx.IDENT().size(); i++) {
                    var strId = ctx.IDENT(i).getText();
                    if (!(tipoId instanceof TiposLA.Registro) ||
                            !((TiposLA.Registro) tipoId).campos.containsKey(strId)) {
                        tipoId = TiposLA.INVALIDO;
                        break;
                    }
                    tipoId = ((TiposLA.Registro) tipoId).campos.get(strId);
                }
                if (tipoId == TiposLA.INVALIDO) {
                    LASemanticoUtils.adicionarErroSemantico(
                            ctx.start,
                            String.format(Mensagens.ERRO_IDENTIFICADOR_NAO_DECLARADO, ctx.getText()));
                }
            }
        } else {
            // TODO: erro - variavel não declarada
            // Talvez seja preciso diferenciar endereços
            LASemanticoUtils.adicionarErroSemantico(
                    ctx.start,
                    String.format(Mensagens.ERRO_IDENTIFICADOR_NAO_DECLARADO, ctx.getText()));
        }

        return null;
    }

    @Override
    public Void visitCmdChamada(LAParser.CmdChamadaContext ctx) {
        ctx.expressao().forEach(this::visitExpressao);
        EntradaTabelaDeSimbolos etds = escopos.verificar(ctx.IDENT().getText());
        if (etds == null) {
            LASemanticoUtils.adicionarErroSemantico(
                    ctx.start,
                    String.format(
                            Mensagens.ERRO_IDENTIFICADOR_NAO_DECLARADO,
                            ctx.start.getText()));
        } else {
            List<TipoLA> params = new ArrayList<>();
            if (etds.tipo instanceof TiposLA.Funcao) {
                params = ((TiposLA.Funcao) etds.tipo).tipoParametros;
            } else if (etds.tipo instanceof TiposLA.Procedimento) {
                params = ((TiposLA.Procedimento) etds.tipo).parametros;
            }

            if (params.size() != ctx.expressao().size()) {
                LASemanticoUtils.adicionarErroSemantico(
                        ctx.start,
                        String.format(
                                Mensagens.ERRO_PARAMETROS_INCOMPATIVEIS,
                                ctx.start.getText()));
            } else {
                for (int i = 0; i < ctx.expressao().size(); i++) {
                    TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.expressao(i));
                    if (tipoExp != params.get(i)) {
                        LASemanticoUtils.adicionarErroSemantico(
                                ctx.start,
                                String.format(
                                        Mensagens.ERRO_PARAMETROS_INCOMPATIVEIS,
                                        ctx.start.getText()));
                        break;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Void visitParcela_unario(LAParser.Parcela_unarioContext ctx) {
        if (ctx.IDENT() != null) {
            LASemanticoUtils.verificarTipo(escopos, ctx);
        } else if (ctx.expParam != null) {
            visitExpressao(ctx.expParam);
        } else if (ctx.identificador() != null) {
            visitIdentificador(ctx.identificador());
        }
        return null;
    }
}
