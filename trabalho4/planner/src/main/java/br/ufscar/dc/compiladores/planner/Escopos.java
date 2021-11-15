package br.ufscar.dc.compiladores.planner;

import java.util.LinkedList;

public class Escopos {    
    static class Escopo {
        TabelaDeSimbolos tabela;
        //TiposPlanner.TipoPlanner tipoDeRetorno;

        Escopo(TabelaDeSimbolos tabela) {
            this.tabela = tabela;
          //  this.tipoDeRetorno = tipoDeRetorno;
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

    public void criarNovoEscopo(TiposPlanner.TipoPlanner tipo) {
        pilhaDeEscopos.push(new Escopo(new TabelaDeSimbolos()));
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

    public EntradaTabelaDeSimbolos verificar(String tarefa) {
        EntradaTabelaDeSimbolos etds = obterEscopoAtual().verificar(tarefa);
        if (etds == null) {
            etds = obterEscopoGlobal().verificar(tarefa);
        }
        return etds;
    }
}
