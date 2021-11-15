package br.ufscar.dc.compiladores.planner;

//import br.ufscar.dc.compiladores.planner.TiposPlanner.TipoPlanner;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TabelaDeSimbolos {
    private final Map<String, EntradaTabelaDeSimbolos> tabela;

    public TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }

    public void adicionar(String nome) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome));
    }

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public EntradaTabelaDeSimbolos verificar(String nome) {
        return tabela.get(nome);
    }
}
