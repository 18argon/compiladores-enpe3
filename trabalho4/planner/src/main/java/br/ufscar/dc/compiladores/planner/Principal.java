package br.ufscar.dc.compiladores.planner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.IOException;
import java.io.PrintWriter;

public class Principal {
    
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Uso: java -jar <programa> <arquivo-de-entrada> <arquivo-de-saida>");
            return;
        }

        // Nome do arquivo de entrada
        String arquivoEntrada = args[0];

        // Nome do arquivo de saída
        String arquivoSaida = args[1];

        try (PrintWriter pw = new PrintWriter(arquivoSaida)) {
//            CustomErrorListener cel = new CustomErrorListener(pw);
            CharStream cs = CharStreams.fromFileName(arquivoEntrada);

            PlannerLexer lexer = new PlannerLexer(cs);
//            lexer.removeErrorListeners();
//            lexer.addErrorListener(cel);

            CommonTokenStream tokens = new CommonTokenStream(lexer);


            PlannerParser parser = new PlannerParser(tokens);
//            parser.removeErrorListeners();

            PlannerParser.ProgramaContext arvore = parser.programa();
            PlannerSemantico pls = new PlannerSemantico();
            pls.visitPrograma(arvore);

            if (!PlannerSemanticoUtils.errosSemanticos.isEmpty()) {
                PlannerSemanticoUtils.errosSemanticos.forEach(pw::println);
                pw.println("Fim da compilacao");
            } /*else {
                GeradorC gc = new GeradorC();
                gc.visitPrograma(arvore);
                pw.write(gc.saida.toString());
            }*/

//            parser.addErrorListener(cel);

//        } catch (ParseCancellationException e) {
//             Sair no primeiro erro
        }
    }
    
}
