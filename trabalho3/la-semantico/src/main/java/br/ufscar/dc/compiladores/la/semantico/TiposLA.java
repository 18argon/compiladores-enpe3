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

    static class Ponteiro extends TipoLA {
        TipoLA tipoConteudo;

        public Ponteiro(TipoLA tipo) {
            super("ponteiro");
            this.tipoConteudo = tipo;
        }
    }

    static class Procedimento extends TipoLA {
        List<TipoLA> parametros;

        public Procedimento(List<TipoLA> parametros) {
            super("procedimento");
            this.parametros = parametros;
        }
    }

    static class Funcao extends TipoLA {
        List<TipoLA> tipoParametros;
        TipoLA tipoRetorno;

        public Funcao(List<TipoLA> tipoParametros, TipoLA tipoRetorno) {
            super("funcao");
            this.tipoParametros = tipoParametros;
            this.tipoRetorno = tipoRetorno;
        }
    }

    static class Registro extends TipoLA {
        Map<String, TipoLA> campos;

        public Registro(Map<String, TipoLA> campos) {
            super("registro");
            this.campos = campos;
        }
    }

    static class Arranjo extends TipoLA {
        TipoLA tipo;

        public Arranjo(TipoLA tipo) {
            super("arranjo");
            this.tipo = tipo;
        }
    }
}
