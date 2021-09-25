package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.TiposLA.TipoLA;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class LASemanticoUtils {
    public static List<String> errosSemanticos = new ArrayList<>();

    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
        int coluna = t.getCharPositionInLine();
        // TODO: fix error format
        errosSemanticos.add(String.format("Erro %d:%d - %s", linha, coluna, mensagem));
    }

    public static TipoLA verificarTipo(LAParser.Tipo_basicoContext ctx) {
        TipoLA ret;
        String strTipo = ctx.getText();
        switch (strTipo) {
            case "literal":
                ret = TiposLA.LITERAL;
                break;
            case "inteiro":
                ret = TiposLA.INTEIRO;
                break;
            case "real":
                ret = TiposLA.REAL;
                break;
            default:  // logico
                ret = TiposLA.LOGICO;
                break;
        }
        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.Exp_aritmeticaContext ctx) {
        TipoLA ret = null;


        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.TipoContext ctx) {
        TipoLA ret = null;


        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.Tipo_estendidoContext tipo_estendido) {
        TipoLA ret = null;

        return ret;
    }
}
