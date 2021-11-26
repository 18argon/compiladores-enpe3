package br.ufscar.dc.compiladores.planner;

public class GeradorHTMLUtils {
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
}
