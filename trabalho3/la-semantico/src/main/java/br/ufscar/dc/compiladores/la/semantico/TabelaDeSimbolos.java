package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.TiposLA.TipoLA;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TabelaDeSimbolos {

    private final Map<String, EntradaTabelaDeSimbolos> tabela;

    public TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }

    public void adicionar(String nome, TipoLA tipo) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome, tipo));
    }

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public EntradaTabelaDeSimbolos verificar(String nome) {
        return tabela.get(nome);
    }
}