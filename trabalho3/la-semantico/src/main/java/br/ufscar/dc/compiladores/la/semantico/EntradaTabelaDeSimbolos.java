package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.TiposLA.TipoLA;

public class EntradaTabelaDeSimbolos {
    public String nome;
    public TipoLA tipo;

    public EntradaTabelaDeSimbolos(String nome, TipoLA tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }
}
