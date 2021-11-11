package br.ufscar.dc.compiladores.la.semantico;

import java.util.LinkedList;

public class Escopos {
    static class Escopo {
        TabelaDeSimbolos tabela;
        TiposLA.TipoLA tipoDeRetorno;

        Escopo(TabelaDeSimbolos tabela, TiposLA.TipoLA tipoDeRetorno) {
            this.tabela = tabela;
            this.tipoDeRetorno = tipoDeRetorno;
        }
    }


    private final LinkedList<Escopo> pilhaDeEscopos;

    public Escopos() {
        pilhaDeEscopos = new LinkedList<>();
        init();
    }

    public void init() {
        pilhaDeEscopos.clear();
        criarNovoEscopo(null);
    }

    public void criarNovoEscopo(TiposLA.TipoLA tipo) {
        pilhaDeEscopos.push(new Escopo(new TabelaDeSimbolos(), tipo));
    }

    public TabelaDeSimbolos obterEscopoAtual() {
        Escopo escopo = pilhaDeEscopos.peek();
        if (escopo == null) return null;
        return pilhaDeEscopos.peek().tabela;
    }

    public TabelaDeSimbolos obterEscopoGlobal() {
        return pilhaDeEscopos.getLast().tabela;
    }

//    public List<TabelaDeSimbolos> percorrerEscoposAninhados() {
//        return pilhaDeTabelas;
//    }


    public void abandonarEscopo() {
        pilhaDeEscopos.pop();
    }

    public TiposLA.TipoLA obterTipoDeRetorno() {
        Escopo escopo = pilhaDeEscopos.peek();
        if (escopo == null) return null;
        return pilhaDeEscopos.peek().tipoDeRetorno;
    }

    public EntradaTabelaDeSimbolos verificar(String identificador) {
        EntradaTabelaDeSimbolos etds = obterEscopoAtual().verificar(identificador);
        if (etds == null) {
            etds = obterEscopoGlobal().verificar(identificador);
        }
        return etds;
    }
}
