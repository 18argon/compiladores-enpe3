package br.ufscar.dc.compiladores.planner;

import java.util.Calendar;

public class Tarefa implements Comparable<Tarefa> {
    private String id;
    private Calendar inicio;
    private Calendar fim;
    private String descricao;

    public Tarefa(String id, Calendar inicio, Calendar fim, String descricao) {
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
        this.descricao = descricao;
    }

    public Tarefa(String id, Calendar inicio, Calendar fim) {
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
    }

    public String getId() {
        return id;
    }

    public Calendar getInicio() {
        return inicio;
    }

    public Calendar getFim() {
        return fim;
    }

    public String getDescricao() {
        return descricao;
    }

    public int compareTo(Tarefa o) {
        int inicioCmp = this.inicio.compareTo(o.inicio);
        if (inicioCmp == 0) {
            return this.id.compareTo(o.id);
        }
        return inicioCmp;
    }
}
