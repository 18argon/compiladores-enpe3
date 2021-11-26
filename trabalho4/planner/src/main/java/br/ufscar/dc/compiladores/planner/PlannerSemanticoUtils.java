package br.ufscar.dc.compiladores.planner;

import org.antlr.v4.runtime.Token;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

public class PlannerSemanticoUtils {
    
    public static List<String> errosSemanticos = new ArrayList<>();
    
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        int coluna = t.getCharPositionInLine();
        errosSemanticos.add(String.format("Linha %d:%d - %s", linha, coluna, mensagem));
    }

    public static void verificarDiaDoMes(int ano, int mes, PlannerParser.Dia_do_mesContext ctx) {
        int dia = Integer.parseInt(ctx.dia.getText());
        int diasNoMes = YearMonth.of(ano, mes).lengthOfMonth();
        if (dia < 1 || diasNoMes < dia) { //data incorreta
            // todo: erro
            PlannerSemanticoUtils.adicionarErroSemantico(ctx.start,
                    String.format(Mensagens.ERRO_DATA_INVALIDA, ctx.start.getText()));
        }
    }

    public static void verificarDiaMes(int ano, PlannerParser.Dia_mesContext ctx) {
        int dia = Integer.parseInt(ctx.dia.getText());
        int mes = Integer.parseInt(ctx.mes.getText());
        int diasNoMes = YearMonth.of(ano, mes).lengthOfMonth();
        if (dia < 1 || diasNoMes < dia) { //data incorreta
            PlannerSemanticoUtils.adicionarErroSemantico(ctx.start,
                    String.format(Mensagens.ERRO_DATA_INVALIDA, ctx.start.getText()));
        }
    }

    public static void verificarIntervalo(int ano, PlannerParser.Data_anualContext ctx) {
        Calendar inicio = parseData(ano, ctx.dia_inicio, ctx.horario_inicio);
        if (ctx.dia_fim != null) { //verifica apenas se dia_fim existir
            Calendar fim = parseData(ano, ctx.dia_fim, ctx.horario_fim);
            if (inicio.compareTo(fim) > 0) { //fim da tarefa antes de inicio
                PlannerSemanticoUtils.adicionarErroSemantico(
                        ctx.start,
                        String.format(Mensagens.ERRO_INICIO_FIM_INCOMPATIVES, ctx.dia_inicio.getText(),
                                ctx.horario_inicio.getText(), ctx.dia_fim.getText(),
                                ctx.horario_fim.getText()));
            }
        }
    }

    public static void verificarIntervalo(int ano, int mes, PlannerParser.Data_mensalContext ctx) {
        Calendar inicio = parseData(ano, mes, ctx.dia_inicio, ctx.horario_inicio);
        if (ctx.dia_fim != null) {
            Calendar fim = parseData(ano, mes, ctx.dia_fim, ctx.horario_fim);
            if (inicio.compareTo(fim) > 0) {
                // todo: erro
                PlannerSemanticoUtils.adicionarErroSemantico(
                        ctx.start,
                        String.format(Mensagens.ERRO_INICIO_FIM_INCOMPATIVES, ctx.dia_inicio.getText(),
                                ctx.horario_inicio.getText(), ctx.dia_fim.getText(),
                                ctx.horario_fim.getText()));
            }
        }
    }

    public static void verificarIntervalo(PlannerParser.Data_semanalContext ctx) {
        DayOfWeek iDia = getDiaDaSemana(ctx.dia_inicio);
        LocalTime iHorario = LocalTime.of(0, 0);
        if (ctx.horario_inicio != null) {
            int iHora = Integer.parseInt(ctx.horario_inicio.hora.getText());
            int iMinuto = Integer.parseInt(ctx.horario_inicio.minuto.getText());
            iHorario = LocalTime.of(iHora, iMinuto);
        }

        if (ctx.dia_fim != null) {
            DayOfWeek fDia = getDiaDaSemana(ctx.dia_fim);
            LocalTime fHorario = LocalTime.of(0, 0);

            if (ctx.horario_fim != null) {
                int fHora = Integer.parseInt(ctx.horario_fim.hora.getText());
                int fMinuto = Integer.parseInt(ctx.horario_fim.minuto.getText());
                fHorario = LocalTime.of(fHora, fMinuto);

            }

            if (iDia.compareTo(fDia) > 0 ||
                    iDia.compareTo(fDia) == 0 && iHorario.compareTo(fHorario) > 0) {
                // todo: erro de intervalo
                PlannerSemanticoUtils.adicionarErroSemantico(
                        ctx.start,
                        String.format(Mensagens.ERRO_INICIO_FIM_INCOMPATIVES, ctx.dia_inicio.getText(),
                                ctx.horario_inicio.getText(), ctx.dia_fim.getText(),
                                ctx.horario_fim.getText()));
            }
        }
    }

    public static Calendar parseData(int ano, int mes, PlannerParser.Dia_do_mesContext diaCtx, PlannerParser.HorarioContext horarioCtx) {
        int dia = Integer.parseInt(diaCtx.getText());
        int hora;
        int minuto;
        if (horarioCtx != null) {
            hora = Integer.parseInt(horarioCtx.hora.getText());
            minuto = Integer.parseInt(horarioCtx.minuto.getText());
        } else {
            hora = 0;
            minuto = 0;
        }

        Calendar cal =  Calendar.getInstance();
        cal.set(ano, mes-1, dia, hora, minuto);
        return cal;
    }

    public static Calendar parseData(int ano, PlannerParser.Dia_mesContext diaMesContext, PlannerParser.HorarioContext horarioCtx) {
        int dia = Integer.parseInt(diaMesContext.dia.getText());
        int mes = Integer.parseInt(diaMesContext.mes.getText());
        int hora;
        int minuto;
        if (horarioCtx != null) {
            hora = Integer.parseInt(horarioCtx.hora.getText());
            minuto = Integer.parseInt(horarioCtx.minuto.getText());
        } else {
            hora = 0;
            minuto = 0;
        }

        Calendar cal =  Calendar.getInstance();
        cal.set(ano, mes-1, dia, hora, minuto);
        return cal;
    }
    public static Calendar parseData(PlannerParser.Dia_da_semanaContext diaDaSemanaCtx,
                                     PlannerParser.HorarioContext horarioCtx) {
        DayOfWeek diaDaSemana = getDiaDaSemana(diaDaSemanaCtx);
        int hora;
        int minuto;
        if (horarioCtx != null) {
            hora = Integer.parseInt(horarioCtx.hora.getText());
            minuto = Integer.parseInt(horarioCtx.minuto.getText());
        } else {
            hora = 0;
            minuto = 0;
        }

        Calendar cal =  Calendar.getInstance();
        System.out.println(diaDaSemana.getValue());
        cal.set(2021, Calendar.NOVEMBER, 20 + diaDaSemana.getValue(), hora, minuto);
        return cal;
    }

    private static DayOfWeek getDiaDaSemana(PlannerParser.Dia_da_semanaContext ctx) {
        switch (ctx.start.getType()) {
            case PlannerParser.SEGUNDA:
                return DayOfWeek.MONDAY;
            case PlannerParser.TERCA:
                return DayOfWeek.TUESDAY;
            case PlannerParser.QUARTA:
                return DayOfWeek.WEDNESDAY;
            case PlannerParser.QUINTA:
                return DayOfWeek.THURSDAY;
            case PlannerParser.SEXTA:
                return DayOfWeek.FRIDAY;
            case PlannerParser.SABADO:
                return DayOfWeek.SATURDAY;
            default: // PlannerParser.DOMINGO:
                return DayOfWeek.SUNDAY;
        }
    }
}
