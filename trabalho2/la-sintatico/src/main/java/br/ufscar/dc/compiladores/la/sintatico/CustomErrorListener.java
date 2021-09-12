package br.ufscar.dc.compiladores.la.sintatico;

import br.ufscar.dc.compiladores.la.sintatico.LALexer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.PrintWriter;
import java.util.BitSet;

public class CustomErrorListener implements ANTLRErrorListener {

    PrintWriter pw;

    public CustomErrorListener(PrintWriter pw) {
        this.pw = pw;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        Token t = (Token) offendingSymbol;
        String message;
        String tokenTypeName = LALexer.VOCABULARY.getDisplayName(t.getType());

        if (tokenTypeName.equals("DESCONHECIDO")) {
            String invalidChar = t.getText();
            if (invalidChar.equals("{")) {
                message = "Linha " + line + ": comentario nao fechado";
            } else if (invalidChar.equals("\"")) {
                message = "Linha " + line + ": cadeia literal nao fechada";
            } else {
                message = "Linha " + line + ": " + t.getText() + " - simbolo nao identificado";
            }
        } else if (t.getType() == Token.EOF) {
            message = "Linha " + line + ": erro sintatico proximo a EOF";
        } else {
            message = "Linha " + line + ": erro sintatico proximo a " + t.getText();
        }

        pw.write(message + "\n");
        pw.write("Fim da compilacao\n");
        throw new ParseCancellationException("Fim");
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

    }
}
