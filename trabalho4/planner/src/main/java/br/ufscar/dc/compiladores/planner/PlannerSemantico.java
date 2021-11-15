package br.ufscar.dc.compiladores.planner;

//import br.ufscar.dc.compiladores.planner.TiposPlanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlannerSemantico extends PlannerBaseVisitor<Void>{
    private final Escopos escopos;
    
    PlannerSemantico() {
        escopos = new Escopos();
    }
    
    @Override
    public Void visitPrograma(PlannerParser.ProgramaContext ctx) {
        escopos.init();
        return super.visitPrograma(ctx);
    }
    
    @Override
    public Void visitFormato(PlannerParser.FormatoContext ctx){
        String start = ctx.start.getText();
       
        if(start.equals("semanal")){
            visitCorpo_semanal(ctx.semanal().corpo_semanal());
           visitSemanal(ctx.semanal());
        } else if(start.equals("mensal")){
            visitCorpo_mensal(ctx.mensal().corpo_mensal());
        } else if(start.equals("anual")){
            visitCorpo_semanal(ctx.anual().corpo_semanal());
        }
        
        return null;

    }
    
    @Override
    public Void visitCorpo_semanal(PlannerParser.Corpo_semanalContext ctx){
        
        
        
        for(var tarefa_semanal : ctx.tarefa_semanal()){
            EntradaTabelaDeSimbolos etds = escopos.verificar(tarefa_semanal.getText());
            if(etds != null){
                 PlannerSemanticoUtils.adicionarErroSemantico(
                    ctx.start,
                    String.format(
                            Mensagens.ERRO_TAREFA_JA_CRIADA,
                            ctx.start.getText()));
            }
            
            else{
        //        escopos.obterEscopoAtual().adicionar();
            }
        }
        
        return null;
    }
    
    
    
}
