package br.ufscar.dc.compiladores.planner.semantico;


import br.ufscar.dc.compiladores.planner.*;

/*
    Visitante usado para verificação de erros semânticos
 */
public class PlannerSemantico extends PlannerBaseVisitor<Void> {
    TabelaDeSimbolos<EntradaTabelaDeSimbolos> tds;

    public PlannerSemantico() {
        tds = new TabelaDeSimbolos<>();
    }

    @Override
    public Void visitCorpo_semanal(PlannerParser.Corpo_semanalContext ctx) {
        for (var tarefa : ctx.tarefa_semanal()) {
            EntradaTabelaDeSimbolos etds = tds.verificar(tarefa.TAREFA().getText());
            if (etds != null) {
                PlannerSemanticoUtils.adicionarErroSemantico(
                        tarefa.TAREFA().getSymbol(),
                        String.format(
                                Mensagens.ERRO_TAREFA_JA_CRIADA,
                                tarefa.TAREFA().getText()));
            } else {
                String nomeTarefa = tarefa.TAREFA().getText();
                tds.adicionar(nomeTarefa, new EntradaTabelaDeSimbolos(nomeTarefa));
            }
            PlannerSemanticoUtils.verificarIntervalo(tarefa.data_semanal());
            visitData_semanal(tarefa.data_semanal());
        }

        return null;
    }

    @Override
    public Void visitCorpo_mensal(PlannerParser.Corpo_mensalContext ctx) {
        int mes = Integer.parseInt(ctx.campo_mes().mes.getText());
        int ano = Integer.parseInt(ctx.campo_mes().ano.getText());

        if (mes < 1 || 12 < mes) { //data inválida
            PlannerSemanticoUtils.adicionarErroSemantico(
                    ctx.campo_mes().start,
                    String.format(Mensagens.ERRO_DATA_INVALIDA, ctx.campo_mes().getText()));
        }

        for (var tarefa : ctx.tarefa_mensal()) {
            EntradaTabelaDeSimbolos etds = tds.verificar(tarefa.TAREFA().getText());
            if (etds != null) { // Já existe tarefa com este nome
                PlannerSemanticoUtils.adicionarErroSemantico(
                        tarefa.TAREFA().getSymbol(),
                        String.format(
                                Mensagens.ERRO_TAREFA_JA_CRIADA,
                                tarefa.TAREFA().getText()));
            } else {
                String nomeTarefa = tarefa.TAREFA().getText();
                tds.adicionar(nomeTarefa, new EntradaTabelaDeSimbolos(nomeTarefa));
            }
            visitData_mensal(tarefa.data_mensal());

            PlannerSemanticoUtils.verificarIntervalo(ano, mes, tarefa.data_mensal());
        }

        return null;
    }

    @Override
    public Void visitCorpo_anual(PlannerParser.Corpo_anualContext ctx) {
        int ano = Integer.parseInt(ctx.campo_ano().ano.getText());

        for (var tarefa : ctx.tarefa_anual()) {
            EntradaTabelaDeSimbolos etds = tds.verificar(tarefa.TAREFA().getText());
            if (etds != null) { // Já existe uma tarefa com este nome
                PlannerSemanticoUtils.adicionarErroSemantico(
                        tarefa.TAREFA().getSymbol(),
                        String.format(
                                Mensagens.ERRO_TAREFA_JA_CRIADA,
                                tarefa.TAREFA().getText()));
            } else {
                String nomeTarefa = tarefa.TAREFA().getText();
                tds.adicionar(nomeTarefa, new EntradaTabelaDeSimbolos(nomeTarefa));
            }
            visitData_anual(tarefa.data_anual());

            PlannerSemanticoUtils.verificarIntervalo(ano, tarefa.data_anual());
        }
        return null;
    }

    @Override
    public Void visitHorario(PlannerParser.HorarioContext ctx) {
        int hora = Integer.parseInt(ctx.hora.getText());
        int minuto = Integer.parseInt(ctx.minuto.getText());
        if (hora < 0 || 23 < hora || minuto < 0 || 59 < minuto) { //data invalida
            PlannerSemanticoUtils.adicionarErroSemantico(
                    ctx.start,
                    String.format(Mensagens.ERRO_HORARIO_INVALIDO, ctx.getText()));
        }
        return null;
    }
}
