package br.ufscar.dc.compiladores.la.semantico;

import java.util.LinkedList;

public class Escopos {
    private final LinkedList<TabelaDeSimbolos> pilhaDeEscopos;

    public Escopos() {
        pilhaDeEscopos = new LinkedList<>();
        init();
    }

    public void init() {
        pilhaDeEscopos.clear();
        criarNovoEscopo();
    }

    public void criarNovoEscopo() {
        pilhaDeEscopos.push(new TabelaDeSimbolos());
    }

    public TabelaDeSimbolos obterEscopoAtual() {
        return pilhaDeEscopos.peek();
    }

    public TabelaDeSimbolos obterEscopoGlobal() {
        return pilhaDeEscopos.getLast();
    }
//
//    public List<TabelaDeSimbolos> percorrerEscoposAninhados() {
//        return pilhaDeTabelas;
//    }

    public void abandonarEscopo() {
        pilhaDeEscopos.pop();
    }

    public TabelaDeSimbolos.EntradaTabelaDeSimbolos verificar(String identificador) {
        TabelaDeSimbolos.EntradaTabelaDeSimbolos etds = obterEscopoAtual().verificar(identificador);
        if (etds == null) {
            etds = obterEscopoGlobal().verificar(identificador);
        }
        return etds;
    }
}
