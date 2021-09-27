package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.TiposLA.TipoLA;

import java.util.HashMap;
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
            visitVariavel(ctx.variavel());
        } else if (start.equals("constante")) {
            String identificador = ctx.IDENT().getText();
            TiposLA.TipoLA tipo = LASemanticoUtils.verificarTipo(ctx.tipo_basico());
            String valor = ctx.valor_constante().getText();

            // TODO: verificar erro de atribuição

            escopos.obterEscopoAtual().adicionar(identificador, tipo, valor);

        } else {
            String identificador = ctx.IDENT().getText();
            TabelaDeSimbolos.EntradaTabelaDeSimbolos etds = escopos.verificar(identificador);
            if (etds != null) {
                // TODO: Erro - identificador em uso
            }

            if (ctx.tipo().start.getText().equals("registro")) {

            }
            // TODO:
//            visitTipo(ctx.tipo());
        }
        return super.visitDeclaracao_local(ctx);
    }

    @Override
    public Void visitVariavel(LAParser.VariavelContext ctx) {
        TipoLA tipo = LASemanticoUtils.verificarTipo(escopos, ctx.tipo());

        if (tipo == TiposLA.INVALIDO) {
            // TODO: Adicionar erro de tipo inválido
            // Precisa sair?????
        }

        for (var identificador : ctx.identificador()) {
            TabelaDeSimbolos.EntradaTabelaDeSimbolos etds = escopos.verificar(identificador.getText());
            if ( etds != null){
                // TODO: Adicionar erro de identificador já existe
            } else if (tipo != TiposLA.INVALIDO) {
                String id = identificador.IDENT().stream()
                        .map(Object::toString).collect(Collectors.joining("."));

                    escopos.obterEscopoAtual().adicionar(id, tipo, null);
            }
        }

        return super.visitVariavel(ctx);
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

        Map<String, TipoLA> tiposParametros = new HashMap<>();
        // checar os parametros
        for (var parametro: ctx.parametros().parametro()) {
            for (var pid : parametro.identificador()) {
                String strPid = pid.getText();
                if (tiposParametros.containsKey(strPid)) {
                    // TODO: erro - nome de parametro já declarado
                } else {
                    TipoLA tipoParametro = LASemanticoUtils.verificarTipo(escopos, parametro.tipo_estendido());
                    tiposParametros.put(strPid, tipoParametro);
                    escopos.obterEscopoAtual().adicionar(strPid, tipoParametro, null);
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
        escopos.obterEscopoAtual().adicionar(identificador, tipo, null);

        return super.visitDeclaracao_global(ctx);
    }

    @Override
    public Void visitCmdLeia(LAParser.CmdLeiaContext ctx) {
        return super.visitCmdLeia(ctx);
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

//        return super.visitCmdCaso(ctx);
        return null;
    }

    @Override
    public Void visitCmdPara(LAParser.CmdParaContext ctx) {
        TabelaDeSimbolos.EntradaTabelaDeSimbolos etds = escopos.verificar(ctx.IDENT().getText());
        if (etds == null) {
            // TODO: erro - variavel nao declarada
        } else if (etds.tipo != TiposLA.INTEIRO) {
            // TODO: erro - tipo de variavel errado
        }

        ctx.cmd().forEach(this::visitCmd);

//        return super.visitCmdPara(ctx);
        return null;
    }

    @Override
    public Void visitCmdEnquanto(LAParser.CmdEnquantoContext ctx) {
        TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());
        if (tipoExp != TiposLA.LOGICO) {
            // TODO: Algum erro
        }

        ctx.cmd().forEach(this::visitCmd);
//        return super.visitCmdEnquanto(ctx);
        return null;
    }

    @Override
    public Void visitCmdFaca(LAParser.CmdFacaContext ctx) {
        TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());
        if (tipoExp != TiposLA.LOGICO) {
            // TODO: Algum erro
        }

        ctx.cmd().forEach(this::visitCmd);
//        return super.visitCmdFaca(ctx);
        return null;
    }

    @Override
    public Void visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx) {
        TipoLA tipoId = LASemanticoUtils.verificarTipo(escopos, ctx.identificador());

        if (tipoId == TiposLA.INVALIDO) {
            // TODO: variavel não declarada
        } else if (ctx.start.getText().equals("^")) {
            if (tipoId instanceof TiposLA.Endereco) {
                tipoId = ((TiposLA.Endereco) tipoId).tipoConteudo;
            } else {
                tipoId = TiposLA.INVALIDO;
            }
        }

        TipoLA tipoExp = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());

        if (tipoId != tipoExp) {
            // TODO: erro - atribuicao incompativel
        }

//        return super.visitCmdAtribuicao(ctx);
        return null;
    }

    @Override
    public Void visitCmdRetorne(LAParser.CmdRetorneContext ctx) {
        TipoLA tipoRetorno = LASemanticoUtils.verificarTipo(escopos, ctx.expressao());
        if (escopos.obterTipoDeRetorno() == null) {
            // TODO: erro - uso inapropriado do comando retorne
        } else if (tipoRetorno != escopos.obterTipoDeRetorno()) {
            // TODO: Mostrar algum erro
        }

        return super.visitCmdRetorne(ctx);
    }

    @Override
    public Void visitIdentificador(LAParser.IdentificadorContext ctx) {
        TabelaDeSimbolos.EntradaTabelaDeSimbolos etds = escopos.verificar(ctx.getText());
        if (etds == null) {
            // TODO: erro - variavel não declarada
            // Talvez seja preciso diferenciar endereços
        }

        return null;
    }

}
