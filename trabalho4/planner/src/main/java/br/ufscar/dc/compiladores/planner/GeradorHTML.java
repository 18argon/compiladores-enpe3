/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.compiladores.planner;

import java.time.YearMonth;
import java.util.*;

public class GeradorHTML extends PlannerBaseVisitor<Void> {

    Map<Integer, ArrayList<Tarefa>> tds;
    StringBuilder saida;

    public GeradorHTML() {
        saida = new StringBuilder();
        tds = new HashMap<>();
    }


    @Override
    public Void visitPrograma(PlannerParser.ProgramaContext ctx) {
        saida.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("<meta charset=\"UTF-8\"/>\n");
        visitFormato(ctx.formato());
        saida.append("</body>\n")
        .append("</html>\n");
        return null;
    }

    @Override
    public Void visitCorpo_anual(PlannerParser.Corpo_anualContext ctx) {
        // Styles
        saida.append(GeradorHTMLUtils.ANUAL_STYLES);
        saida.append("</head>\n<body>\n");
        //
        saida.append("<div class=\"container\">\n")
                .append("<h1>Planner Anual - ")
                .append(ctx.campo_ano().ano.getText())
                .append("</h1>")
                .append("<div class=\"calendar\">\n");

        // Obter todas as tarefas
        ctx.tarefa_anual().forEach(this::visitTarefa_anual);
        // gerar o html
        for (int i = 1; i <= 12; i++) {
            List<Tarefa> tarefas = tds.get(i);
            saida.append("<div class=\"mes\">\n")
                    .append("<h3 class=\"titulo-mes\">")
                    .append(i) // todo: mudar para nome do mês
                    .append("</h3>\n<ul>\n");
            if (tarefas != null) {
                for (var tarefa : tarefas) {
                    saida.append("<li><span class=\"bold\">");
                    int iDia = tarefa.getInicio().get(Calendar.DATE);
                    int fDia = tarefa.getFim().get(Calendar.DATE);
                    if (iDia != fDia) {
                        saida.append(iDia).append("-").append(fDia);
                    } else {
                        saida.append(iDia);
                    }
                    saida.append(":</span>")
                            .append(tarefa.getDescricao())
                            .append("</li>\n");
                }
            }
            saida.append("</ul>\n</div>\n");
        }

        saida.append("</div>\n")
                .append("<h2>Tarefas</h2>\n")
                .append("<div class=\"container-tarefa\">\n");

        for (int i = 1; i <= 12; i++) {
            List<Tarefa> tarefas = tds.get(i);
            if (tarefas != null) {

                for (var tarefa : tarefas) {
                    saida.append("<div class=\"tarefa\">\n");
                    saida.append("<h4>")
                            .append(tarefa.getId())
                            .append("</h4>\n");
                    saida.append("<p><span class=\"bold\">Inicio:</span>")
                            .append(formatDate(tarefa.getInicio()))
                            .append(" - <span class=\"bold\">Fim:</span>")
                            .append(formatDate(tarefa.getFim()))
                            .append("</p>\n");
                    saida.append("<p><span class=\"bold\">Descrição:</span>")
                            .append(tarefa.getDescricao())
                            .append("</p>\n");
                    saida.append("</div>\n");
                }
            }
        }
        saida.append("</div>\n</div>\n");

        return null;
    }

    private String formatDate(Calendar c) {
        return "";
    }

    @Override
    public Void visitCorpo_mensal(PlannerParser.Corpo_mensalContext ctx){
        int anoNum = Integer.parseInt(ctx.campo_mes().ano.getText());
        int mesNum = Integer.parseInt(ctx.campo_mes().mes.getText());
        int diasNoMes = YearMonth.of(anoNum, mesNum).lengthOfMonth();
        // 01/12
        // header do planner

        saida.append("<h1>Planner Mensal - ")
                .append(ctx.campo_mes().mes.getText())
                .append(ctx.campo_mes().ano.getText())
                .append("</h1>");
        // Obter todas as tarefas
        ctx.tarefa_mensal().forEach(this::visitTarefa_mensal);
        //gerar html

        for(int i = 1; i <= diasNoMes; i++){

            List<Tarefa> tarefas = tds.get(i);
            if(tarefas != null){
                for(var tarefa : tarefas){

                    int iDia = tarefa.getInicio().get(Calendar.DATE);
                    int fDia = tarefa.getFim().get(Calendar.DATE);
                    if (iDia != fDia) {

                    } else {

                    }

                }

            }

        }
        return null;
    }
}
