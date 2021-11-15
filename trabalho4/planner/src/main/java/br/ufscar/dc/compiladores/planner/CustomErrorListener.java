package br.ufscar.dc.compiladores.planner;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.PrintWriter;
import java.util.BitSet;

//Classe responsável por customizar as mensagens de erro
public class CustomErrorListener {
    
    //Variável para escrever no arquivo .txt
    PrintWriter pw;

    public CustomErrorListener(PrintWriter pw) {
        this.pw = pw;
    }
    
    
    public void syntaxeError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e){
        Token t = (Token) offendingSymbol;
        String message;
        String tokenTypeName = PlannerLexer.VOCABULARY.getDisplayName(t.getType());
        
        //Verifica se o erro é devido a um caractere desconhecido
        if (tokenTypeName.equals("DESCONHECIDO")) {
            String invalidChar = t.getText();
            
            //Erro devido ao não fechamento de comentário
            if (invalidChar.equals("/*")) {
                message = "Linha " + line + ": comentario nao fechado";
            }else if (invalidChar.equals("\"")) {      //Erro devido ao não fechamento de cadeia literal
                message = "Linha " + line + ": cadeia descricao nao fechada"; 
            }else {        //Erro devido ao não reconhecimento de símbolos
                message = "Linha " + line + ": " + t.getText() + " - simbolo nao identificado";
            }
        } else if (t.getType() == Token.EOF) {      //Erro devido à não finalização do algoritmo
            message = "Linha " + line + ": erro sintatico proximo a EOF";
        } else {        //Erro sintático
            message = "Linha " + line + ": erro sintatico proximo a " + t.getText();
        }

        pw.write(message + "\n");
        pw.write("Fim da compilacao\n");
        throw new ParseCancellationException("Fim");
        
    }
    

}
