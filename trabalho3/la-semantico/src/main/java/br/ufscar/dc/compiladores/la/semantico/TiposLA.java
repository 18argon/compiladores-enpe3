package br.ufscar.dc.compiladores.la.semantico;

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

    static class Endereco extends TipoLA {
        TipoLA tipoConteudo;

        public Endereco(TipoLA tipo) {
            super("endereco");
            this.tipoConteudo = tipo;
        }
    }

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

    static class Registro extends TipoLA {
        Map<String, TipoLA> campos;

        public Registro(Map<String, TipoLA> campos) {
            super("registro");
            this.campos = campos;
        }
    }

    static class Arranjo extends TipoLA {
        int tamanho;
        TipoLA tipo;

        public Arranjo(int tamanho, TipoLA tipo) {
            super("arranjo");
            this.tamanho = tamanho;
            this.tipo = tipo;
        }
    }
}
