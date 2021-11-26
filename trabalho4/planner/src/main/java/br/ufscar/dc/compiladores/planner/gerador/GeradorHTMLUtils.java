package br.ufscar.dc.compiladores.planner.gerador;

/*
    Utilitários usados na geração da saida HTML
 */
public class GeradorHTMLUtils {

    public static int DIAS_NA_SEMANA = 7;

    public static int MESES_NO_ANO = 12;

    public static final String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    public static final String[] diasDaSemana = {"Domingo", "Segunda-feira", "Terça-feira",
            "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"};

    public static final String ANUAL_STYLES = "<style>\n" +
            "        .container {\n" +
            "            padding: 0 5%;\n" +
            "        }\n" +
            "        .calendar {\n" +
            "            justify-content: space-between;\n" +
            "            display: flex;\n" +
            "            flex-wrap: wrap;\n" +
            "        }\n" +
            "        .container-tarefa {\n" +
            "            justify-content: space-between;\n" +
            "            display: flex;\n" +
            "            flex-wrap: wrap;\n" +
            "        }\n" +
            "        .tarefa {\n" +
            "            width: 45%;\n" +
            "        }\n" +
            "        .mes {\n" +
            "            margin: 5px;\n" +
            "            width: 45%;\n" +
            "            min-height: 300px;\n" +
            "            border: 2px solid black;\n" +
            "            border-radius: 3%;\n" +
            "        }\n" +
            "        h1 {\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        h3 {\n" +
            "            margin: 0;\n" +
            "            padding: 15px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        h4 {\n" +
            "            font-style: italic;\n" +
            "        }\n" +
            "        ul {\n" +
            "            list-style-type: none;\n" +
            "            margin: 0 20px;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "        li {\n" +
            "            margin-bottom: 5px;\n" +
            "        }\n" +
            "        li:hover {\n" +
            "            display: inline-block;\n" +
            "            overflow: visible;\n" +
            "            background-color: yellow;\n" +
            "        }\n" +
            "        a {\n" +
            "            text-decoration: none;\n" +
            "        }\n" +
            "        .bold {\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "    </style>";

    public static final String SEMANAL_STYLES = "    <style>\n" +
            "        .container {\n" +
            "            margin: auto 20px;\n" +
            "        }\n" +
            "        .calendar {\n" +
            "            display: flex;\n" +
            "        }\n" +
            "        .dia-da-semana {\n" +
            "            min-width: 180px;\n" +
            "            border: 1px solid black;\n" +
            "        }\n" +
            "        .container-tarefa {\n" +
            "            justify-content: space-between;\n" +
            "            display: flex;\n" +
            "            flex-wrap: wrap;\n" +
            "        }\n" +
            "        .tarefa {\n" +
            "            width: 45%;\n" +
            "        }\n" +
            "        h1 {\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        h3 {\n" +
            "            margin: 0;\n" +
            "            padding: 15px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        h4 {\n" +
            "            font-style: italic;\n" +
            "        }\n" +
            "        ul {\n" +
            "            list-style-type: none;\n" +
            "            margin: 0 20px;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "        li {\n" +
            "            margin-bottom: 5px;\n" +
            "            font-size: 1rem;\n" +
            "            white-space: nowrap;\n" +
            "            overflow: hidden;\n" +
            "            text-overflow: ellipsis;\n" +
            "        }\n" +
            "        li:hover {\n" +
            "            display: inline-block;\n" +
            "            overflow: visible;\n" +
            "            background-color: yellow;\n" +
            "\n" +
            "        }\n" +
            "        .bold {\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "    </style>\n";

    public static final String MENSAL_STYLES = "<style>\n" +
            "        .container {\n" +
            "            margin: auto 20px;\n" +
            "        }\n" +
            "        .dia-da-semana {\n" +
            "            min-width: 180px;\n" +
            "            border: 1px solid black;\n" +
            "        }\n" +
            "        .tarefa {\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        h1 {\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        h3 {\n" +
            "            margin: 0;\n" +
            "            padding: 15px;\n" +
            "            text-align: left;\n" +
            "        }\n" +
            "        h4 {\n" +
            "            font-style: italic;\n" +
            "        }\n" +
            "        ul {\n" +
            "            list-style-type: none;\n" +
            "            margin: 0 20px;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "        li {\n" +
            "            margin-bottom: 5px;\n" +
            "            font-size: 1rem;\n" +
            "            white-space: nowrap;\n" +
            "            overflow: hidden;\n" +
            "            text-overflow: ellipsis;\n" +
            "        }\n" +
            "        .bold {\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "    </style>";
}
