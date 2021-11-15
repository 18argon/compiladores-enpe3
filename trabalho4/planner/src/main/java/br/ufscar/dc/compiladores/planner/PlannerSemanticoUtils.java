package br.ufscar.dc.compiladores.planner;

import br.ufscar.dc.compiladores.planner.TiposPlanner.TipoPlanner;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlannerSemanticoUtils {
    
    public static List<String> errosSemanticos = new ArrayList<>();
    
    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
//        int coluna = t.getCharPositionInLine();
        // TODO: fix error format
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
    }
}
