package br.ufscar.dc.compiladores.planner.semantico;

import br.ufscar.dc.compiladores.planner.Mensagens;
import br.ufscar.dc.compiladores.planner.PlannerParser;
import org.antlr.v4.runtime.Token;

import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

/*
    Utilitários para o Analisador Semântico
 */
public class PlannerSemanticoUtils {
    
    public static List<String> errosSemanticos = new ArrayList<>();
    
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        int coluna = t.getCharPositionInLine();
        errosSemanticos.add(String.format("Linha %d:%d - %s", linha, coluna, mensagem));
    }

    private static boolean verificarDiaDoMes(int ano, int mes, PlannerParser.Dia_do_mesContext ctx) {
        int dia = Integer.parseInt(ctx.dia.getText());
        int diasNoMes = YearMonth.of(ano, mes).lengthOfMonth();
        if (dia < 1 || diasNoMes < dia) { //data incorreta
            PlannerSemanticoUtils.adicionarErroSemantico(ctx.start,
                    String.format(Mensagens.ERRO_DATA_INVALIDA, ctx.start.getText()));
            return false;
        }
        return true;
    }

    private static boolean verificarDiaMes(int ano, PlannerParser.Dia_mesContext ctx) {
        int dia = Integer.parseInt(ctx.dia.getText());
        int mes = Integer.parseInt(ctx.mes.getText());
        int diasNoMes = YearMonth.of(ano, mes).lengthOfMonth();
        if (dia < 1 || diasNoMes < dia) { //data incorreta
            PlannerSemanticoUtils.adicionarErroSemantico(ctx.start,
                    String.format(Mensagens.ERRO_DATA_INVALIDA, ctx.start.getText()));
            return false;
        }
        return true;
    }

    /*
        Verifica se as datas estão corretas e se representam um intervalo válido
     */
    public static void verificarIntervalo(int ano, PlannerParser.Data_anualContext ctx) {
        boolean datasCorretas = verificarDiaMes(ano, ctx.dia_inicio);
        if (ctx.dia_fim != null) {
            datasCorretas = verificarDiaMes(ano, ctx.dia_fim) && datasCorretas;
        }
        if (!datasCorretas) {
            return;
        }
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

    /*
        Verifica se as datas estão corretas e se representam um intervalo válido
     */
    public static void verificarIntervalo(int ano, int mes, PlannerParser.Data_mensalContext ctx) {
        boolean datasCorretas = verificarDiaDoMes(ano, mes, ctx.dia_inicio);
        if (ctx.dia_fim != null) {
            datasCorretas = verificarDiaDoMes(ano, mes, ctx.dia_fim) && datasCorretas;
        }
        if (!datasCorretas) {
            return;
        }
        Calendar inicio = parseData(ano, mes, ctx.dia_inicio, ctx.horario_inicio);
        if (ctx.dia_fim != null) {
            Calendar fim = parseData(ano, mes, ctx.dia_fim, ctx.horario_fim);
            if (inicio.compareTo(fim) > 0) {
                PlannerSemanticoUtils.adicionarErroSemantico(
                        ctx.start,
                        String.format(Mensagens.ERRO_INICIO_FIM_INCOMPATIVES, ctx.dia_inicio.getText(),
                                ctx.horario_inicio.getText(), ctx.dia_fim.getText(),
                                ctx.horario_fim.getText()));
            }
        }
    }

    /*
        Verifica se as datas estão corretas e se representam um intervalo válido
     */
    public static void verificarIntervalo(PlannerParser.Data_semanalContext ctx) {
        int iDia = getDiaDaSemana(ctx.dia_inicio);
        LocalTime iHorario = LocalTime.of(0, 0);
        if (ctx.horario_inicio != null) {
            int iHora = Integer.parseInt(ctx.horario_inicio.hora.getText());
            int iMinuto = Integer.parseInt(ctx.horario_inicio.minuto.getText());
            iHorario = LocalTime.of(iHora, iMinuto);
        }

        if (ctx.dia_fim != null) {
            int fDia = getDiaDaSemana(ctx.dia_fim);
            LocalTime fHorario = LocalTime.of(0, 0);

            if (ctx.horario_fim != null) {
                int fHora = Integer.parseInt(ctx.horario_fim.hora.getText());
                int fMinuto = Integer.parseInt(ctx.horario_fim.minuto.getText());
                fHorario = LocalTime.of(fHora, fMinuto);
            }

            if (iDia > fDia ||
                    iDia == fDia && iHorario.compareTo(fHorario) > 0) {
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
        int diaDaSemana = getDiaDaSemana(diaDaSemanaCtx);
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
        cal.set(2021, Calendar.NOVEMBER, 20 + diaDaSemana, hora, minuto);
        return cal;
    }

    private static int getDiaDaSemana(PlannerParser.Dia_da_semanaContext ctx) {
        switch (ctx.start.getType()) {
            case PlannerParser.DOMINGO:
                return 0;
            case PlannerParser.SEGUNDA:
                return 1;
            case PlannerParser.TERCA:
                return 2;
            case PlannerParser.QUARTA:
                return 3;
            case PlannerParser.QUINTA:
                return 4;
            case PlannerParser.SEXTA:
                return 5;
            default: // PlannerParser.SABADO:
                return 6;
        }
    }
}
