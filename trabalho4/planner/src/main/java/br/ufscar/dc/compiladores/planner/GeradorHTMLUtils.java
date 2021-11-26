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

    public static final String SEMANAL_STYLES = "       <style>"
            +"        .container {"
            +"    margin: auto 20px;"
            +"}"
            +"    .calendar {"
            +"    display: flex;"
            +"}"
            +"    .dia-da-semana {"
            +"    min-width: 180px;"
            +"    border: 1px solid black;"
            +"}"
            +"    .container-tarefa {"
            +"    justify-content: space-between;"
            +"    display: flex;"
            +"    flex-wrap: wrap;"
            +"}"
            +"    .tarefa {"
            +"    width: 45%;"
            +"}"
            +"    .mes {"
            +"    margin: 5px;"
            +"    width: 45%;"
            +"    min-height: 300px;"
            +"    border: 2px solid black;"
            +"    border-radius: 3%;"
            +"}"
            +"h1 {"
            +"    text-align: center;"
            +"}"
            +"h3 {"
            +"    margin: 0;"
            +"    padding: 15px;"
            +"    text-align: center;"
            +"}"
            +"ul {"
            +"    list-style-type: none;"
            +"    margin: 0 20px;"
            +"    padding: 0;"
            +"}"
            +"li {"
            +"    margin-bottom: 5px;"
            +"    font-size: 1rem;"
            +"    white-space: nowrap;"
            +"    overflow: hidden;"
            +"    text-overflow: ellipsis;"
            +"}"
            +"li:hover {"
            +"    display: inline-block;"
            +"    overflow: visible;"
            +"    background-color: yellow;"
            +"}"
            +"    .bold {"
            +"    font-weight: bold;"
            +"}"
            +"</style>";
}
