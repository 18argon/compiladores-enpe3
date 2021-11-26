package br.ufscar.dc.compiladores.planner.gerador;

import br.ufscar.dc.compiladores.planner.PlannerBaseVisitor;
import br.ufscar.dc.compiladores.planner.PlannerParser;
import br.ufscar.dc.compiladores.planner.semantico.PlannerSemanticoUtils;
import br.ufscar.dc.compiladores.planner.Tarefa;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;

/*
    Visitante usado para geração de uma saida HTML
 */
public class GeradorHTML extends PlannerBaseVisitor<Void> {

    // Estrutura para fazer o agrupamento das tarefas
    private final Map<Integer, ArrayList<Tarefa>> tds;
    public StringBuilder saida;

    public GeradorHTML() {
        saida = new StringBuilder();
        tds = new HashMap<>();
    }

    @Override
    public Void visitPrograma(PlannerParser.ProgramaContext ctx) {
        saida.append("<!DOCTYPE html>\n")
                .append("<html>\n");

        visitFormato(ctx.formato());
        saida.append("</html>\n");
        return null;
    }

    private void adicionarHead(String titulo, String styles) {
        saida.append("<head>\n")
                .append("<meta charset=\"UTF-8\"/>\n")
                .append("<title>").append(titulo).append("</title>\n")
                .append(styles)
                .append("</head>\n");
    }

    @Override
    public Void visitCorpo_anual(PlannerParser.Corpo_anualContext ctx) {
        // Obter todas as tarefas
        int ano = Integer.parseInt(ctx.campo_ano().ano.getText());
        for (var tarefa : ctx.tarefa_anual()) {
            Calendar inicio = PlannerSemanticoUtils.parseData(ano,
                    tarefa.data_anual().dia_inicio, tarefa.data_anual().horario_inicio);
            Calendar fim;
            if (tarefa.data_anual().dia_fim != null) {
                fim = PlannerSemanticoUtils.parseData(ano,
                        tarefa.data_anual().dia_fim, tarefa.data_anual().horario_fim);
            } else {
                fim = inicio;
            }
            String descricao;
            if (tarefa.campo_descricao() != null) {
                descricao = tarefa.campo_descricao().DESCRICAO().getText();
            } else {
                descricao = "";
            }
            String id = tarefa.TAREFA().getText();

            // As tarefas são agrupadas por mês
            Integer key = inicio.get(Calendar.MONTH);
            tds.computeIfAbsent(key, k -> new ArrayList<>());
            tds.get(key).add(new Tarefa(id, inicio, fim, descricao));
        }

        // Gerando head and body da saida HTML
        adicionarHead("Planner anual", GeradorHTMLUtils.ANUAL_STYLES);

        saida.append("<body>\n");
        saida.append("<div class=\"container\">\n");

        // Titulo principal
        saida.append("<h1>Planner Anual - ")
                .append(ctx.campo_ano().ano.getText())
                .append("</h1>");

        saida.append("<div class=\"calendar\">\n");

        // Listando tarefas agrupadas por mês e de modo simplificado
        for (int i = 0; i < GeradorHTMLUtils.MESES_NO_ANO; i++) {
            List<Tarefa> tarefas = tds.get(i);
            saida.append("<div class=\"mes\">\n");

            // Nome do mês
            saida.append("<h3 class=\"titulo-mes\">")
                    .append(GeradorHTMLUtils.meses[i])
                    .append("</h3>\n");

            if (tarefas != null) {
                saida.append("<ul>\n");
                for (var tarefa : tarefas) {
                    saida.append("<li>")
                            .append("<a href=\"#").append(tarefa.getId()).append("\">")
                            .append("<span class=\"bold\">");
                    int iDia = tarefa.getInicio().get(Calendar.DATE);
                    int fDia = tarefa.getFim().get(Calendar.DATE);
                    if (iDia != fDia) {
                        saida.append(iDia).append("-").append(fDia);
                    } else {
                        saida.append(iDia);
                    }
                    saida.append(": </span>")
                            .append(tarefa.getId())
                            .append("</a></li>\n");
                }
                saida.append("</ul>\n");
            }
            saida.append("</div>\n"); // .mes
        }

        // Listando todas as tarefas de modo detalhado
        saida.append("</div>\n") // .calendar
                .append("<h2>Tarefas</h2>\n")
                .append("<div class=\"container-tarefa\">\n");

        for (int i = 0; i < GeradorHTMLUtils.MESES_NO_ANO; i++) {
            List<Tarefa> tarefas = tds.get(i);
            if (tarefas != null) {

                for (var tarefa : tarefas) {
                    saida.append("<div id=\"")
                            .append(tarefa.getId())
                            .append("\" class=\"tarefa\">\n");
                    saida.append("<h4>")
                            .append(tarefa.getId())
                            .append("</h4>\n");
                    saida.append("<p><span class=\"bold\">Inicio: </span>")
                            .append(formatDate(tarefa.getInicio()))
                            .append(" - <span class=\"bold\">Fim: </span>")
                            .append(formatDate(tarefa.getFim()))
                            .append("</p>\n");
                    saida.append("<p><span class=\"bold\">Descrição:</span>")
                            .append(tarefa.getDescricao())
                            .append("</p>\n");
                    saida.append("</div>\n"); // .tarefa
                }
            }
        }
        saida.append("</div>\n") // .container-tarefa
                .append("</div>\n") // .container
                .append("</body>\n");

        return null;
    }

    @Override
    public Void visitCorpo_mensal(PlannerParser.Corpo_mensalContext ctx){
        int ano = Integer.parseInt(ctx.campo_mes().ano.getText());
        int mes = Integer.parseInt(ctx.campo_mes().mes.getText());
        int diasNoMes = YearMonth.of(ano, mes).lengthOfMonth();
        for (var tarefa : ctx.tarefa_mensal()) {
            Calendar inicio = PlannerSemanticoUtils.parseData(ano, mes,
                    tarefa.data_mensal().dia_inicio, tarefa.data_mensal().horario_inicio);
            Calendar fim;
            if (tarefa.data_mensal().dia_fim != null) {
                fim = PlannerSemanticoUtils.parseData(ano, mes,
                        tarefa.data_mensal().dia_fim, tarefa.data_mensal().horario_fim);
            } else {
                fim = inicio;
            }
            String descricao;
            if (tarefa.campo_descricao() != null) {
                descricao = tarefa.campo_descricao().DESCRICAO().getText();
            } else {
                descricao = "";
            }
            String id = tarefa.TAREFA().getText();

            // Tarefas são agrupadas por dia
            Integer key = inicio.get(Calendar.DATE);
            tds.computeIfAbsent(key, k -> new ArrayList<>());
            tds.get(key).add(new Tarefa(id, inicio, fim, descricao));
        }

        // Gerar head and body
        adicionarHead("Planner Mensal", GeradorHTMLUtils.MENSAL_STYLES);

        saida.append("<body>\n");

        saida.append("<div class=\"container\">\n");
        saida.append("<h1>Planner Mensal - ")
                .append(ctx.campo_mes().mes.getText())
                .append("/")
                .append(ctx.campo_mes().ano.getText())
                .append("</h1>");

        saida.append("<div class=\"calendar\">\n");

        for(int i = 1; i <= diasNoMes; i++){
            List<Tarefa> tarefas = tds.get(i);
            // Pular dias sem tarefas
            if (tarefas == null) continue;
            saida.append("<div class=\"dia-da-semana\">\n");

            saida.append("<h3>")
                    .append(i)
                    .append("</h3>\n");
            saida.append("<ul>\n");
            for (int j = 0, tarefasSize = tarefas.size(); j < tarefasSize; j++) {
                Tarefa tarefa = tarefas.get(j);
                saida.append("<li><div class=\"tarefa\">");
                saida.append("<h4>")
                        .append(tarefa.getId())
                        .append("</h4>\n");
                saida.append("<p><span class=\"bold\">Inicio: </span>")
                        .append(formatDate(tarefa.getInicio()))
                        .append(" - <span class=\"bold\">Fim: </span>")
                        .append(formatDate(tarefa.getFim()))
                        .append("</p>\n");
                saida.append("<p><span class=\"bold\">Descrição:</span>")
                        .append(tarefa.getDescricao())
                        .append("</p>\n");
                if (j != tarefasSize -1) {
                    // Divisor para tarefas em um mesmo dia
                    saida.append("<hr />");
                }
                saida.append("</div>") // .tarefa
                        .append("</li>\n");

            }
            saida.append("</ul>\n");
            saida.append("</div>\n"); // .dia-da-semana

        }
        saida.append("</div>\n") // .calendar
                .append("</div>\n") // .container
                .append("</body>\n");
        return null;
    }

    @Override
    public Void visitCorpo_semanal(PlannerParser.Corpo_semanalContext ctx){
        for (var tarefa : ctx.tarefa_semanal()) {
            Calendar inicio = PlannerSemanticoUtils.parseData(
                    tarefa.data_semanal().dia_inicio, tarefa.data_semanal().horario_inicio);
            Calendar fim;
            if (tarefa.data_semanal().dia_fim != null) {
                fim = PlannerSemanticoUtils.parseData(
                        tarefa.data_semanal().dia_fim, tarefa.data_semanal().horario_fim);
            } else {
                fim = inicio;
            }
            String descricao;
            if (tarefa.campo_descricao() != null) {
                descricao = tarefa.campo_descricao().DESCRICAO().getText();
            } else {
                descricao = "";
            }
            String id = tarefa.TAREFA().getText();

            // Tarefas são agrupadas por dia da semana
            Integer key = inicio.get(Calendar.DAY_OF_WEEK);
            tds.computeIfAbsent(key, k -> new ArrayList<>());
            tds.get(key).add(new Tarefa(id, inicio, fim, descricao));
        }

        // Gera head e body
        adicionarHead("Planner Semanal", GeradorHTMLUtils.SEMANAL_STYLES);

        saida.append("<body>\n");

        saida.append("<div class=\"container\">\n");
        saida.append("<h1>Planner Semanal</h1>");

        saida.append("<div class=\"calendar\">\n");

        // Lista as tarefas agrupadas por dia da semana (simplificado)
        for(int i = 0; i < GeradorHTMLUtils.DIAS_NA_SEMANA; i++){
            List<Tarefa> tarefas = tds.get(i);
            saida.append("<div class=\"dia-da-semana\">\n");
            saida.append("<h3>")
                    .append(GeradorHTMLUtils.diasDaSemana[i])
                    .append("</h3>\n<ul>\n");
            if(tarefas != null){
                for(var tarefa : tarefas){
                    saida.append("<li>")
                            .append(tarefa.getId())
                            .append("</li>\n");
                }
            }
            saida.append("</ul>\n")
                    .append("</div>\n"); // .dia-da-semana
        }
        saida.append("</div>\n"); // .calendar

        // Lista as tarefas de modo mais detalhado
        saida.append("<h2>Tarefas</h2>\n")
                .append("<div class=\"container-tarefa\">\n");
        for (int i = 0; i < GeradorHTMLUtils.DIAS_NA_SEMANA; i++) {
            List<Tarefa> tarefas = tds.get(i);
            if (tarefas != null) {
                for (var tarefa : tarefas) {
                    saida.append("<div class=\"tarefa\">\n");
                    saida.append("<h4>")
                            .append(tarefa.getId())
                            .append("</h4>\n");
                    saida.append("<p><span class=\"bold\">Inicio: </span>")
                            .append(formatDate_semanal(tarefa.getInicio()))
                            .append(" - <span class=\"bold\">Fim: </span>")
                            .append(formatDate_semanal(tarefa.getFim()))
                            .append("</p>\n");
                    saida.append("<p><span class=\"bold\">Descrição: </span>")
                            .append(tarefa.getDescricao())
                            .append("</p>\n");
                    saida.append("</div>\n"); // .tarefa
                }
            }
        }
        saida.append("</div>\n") // .container-tarefa
                .append("</div>\n") // .container
                .append("</body>\n");

        return null;
    }

    /*
        Função de ajuda para formatar datas
     */
    private String formatDate(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM [HH:mm]");

        return dateFormat.format(c.getTime());
    }

    /*
        Função de ajuda para formatar datas semanais
     */
    private String formatDate_semanal(Calendar c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(" [HH:mm]");
        return GeradorHTMLUtils.diasDaSemana[c.get(Calendar.DAY_OF_WEEK) - 1] + dateFormat.format(c.getTime());
    }
}
