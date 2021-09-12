package br.ufscar.dc.compiladores.la.sintatico;

import br.ufscar.dc.compiladores.la.sintatico.LALexer;
import br.ufscar.dc.compiladores.la.sintatico.LAParser;
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
            CustomErrorListener cel = new CustomErrorListener(pw);
            CharStream cs = CharStreams.fromFileName(arquivoEntrada);

            LALexer lexer = new LALexer(cs);
            lexer.removeErrorListeners();
            lexer.addErrorListener(cel);

            CommonTokenStream tokens = new CommonTokenStream(lexer);


            LAParser parser = new LAParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(cel);

            parser.programa();
        } catch (ParseCancellationException e) {
            // Sair no primeiro erro
        }
    }
}
