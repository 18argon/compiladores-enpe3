package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.TiposLA.TipoLA;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {

    static class EntradaTabelaDeSimbolos {
        String nome;
        TipoLA tipo;
        String valor;

        private EntradaTabelaDeSimbolos(String nome, TipoLA tipo, String valor) {
            this.nome = nome;
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    private final Map<String, EntradaTabelaDeSimbolos> tabela;

    public TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }

    public void adicionar(String nome, TipoLA tipo, String valor) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome, tipo, valor));
    }

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public EntradaTabelaDeSimbolos verificar(String nome) {
        return tabela.get(nome);
    }
}