package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.TiposLA.TipoLA;

import java.util.ArrayList;
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
//            TabelaDeSimbolos.TipoLA tipo = LASemanticoUtils.verificarTipo(ctx.tipo());
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

        escopos.criarNovoEscopo();
        String start = ctx.start.getText();
        String identificador = ctx.IDENT().getText();

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
            TipoLA tipoRetorno = LASemanticoUtils.verificarTipo(escopos, ctx.tipo_estendido());
            tipo = new TiposLA.Funcao(tiposParametros, tipoRetorno);
        }

        ctx.declaracao_local().forEach(this::visitDeclaracao_local);

        ctx.cmd().forEach(this::visitCmd);

        escopos.abandonarEscopo();

        // TODO: adicionar somente se sem erros
        escopos.obterEscopoAtual().adicionar(identificador, tipo, null);

        return super.visitDeclaracao_global(ctx);
    }



}
