package br.ufscar.dc.compiladores.la.semantico;

import java.util.List;
import java.util.Map;

public class TiposLA {
    static class TipoLA {
        String nome;

        TipoLA(String nome) {
            this.nome = nome;
        }
    }

    static final TipoLA INVALIDO = new TipoLA("invalido");

    static final TipoLA LITERAL = new TipoLA("literal");

    static final TipoLA INTEIRO = new TipoLA("inteiro");

    static final TipoLA REAL = new TipoLA("real");

    static final TipoLA LOGICO = new TipoLA("logico");

    static final TipoLA ENDERECO = new TipoLA("endereco");

    static class Procedimento extends TipoLA {
        Map<String, TipoLA> parametros;

        public Procedimento(Map<String, TipoLA> parametros) {
            super("procedimento");
            this.parametros = parametros;
        }
    }

    static class Funcao extends TipoLA {
        Map<String, TipoLA> tipoParametros;
        TipoLA tipoRetorno;

        public Funcao(Map<String, TipoLA> tipoParametros, TipoLA tipoRetorno) {
            super("funcao");
            this.tipoParametros = tipoParametros;
            this.tipoRetorno = tipoRetorno;
        }
    }
}
