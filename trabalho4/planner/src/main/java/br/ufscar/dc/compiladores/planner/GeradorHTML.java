/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.compiladores.planner;

/**
 *
 * @author vanessa
 */
public class GeradorHTML extends PlannerBaseVisitor<Void> {

    StringBuilder saida;

    public GeradorHTML() {
        saida = new StringBuilder();
    }
}
