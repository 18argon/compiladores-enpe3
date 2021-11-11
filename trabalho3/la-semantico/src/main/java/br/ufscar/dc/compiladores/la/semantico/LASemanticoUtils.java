package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.TiposLA.TipoLA;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LASemanticoUtils {
    public static List<String> errosSemanticos = new ArrayList<>();

    public static void adicionarErroSemantico(Token t, String mensagem) {
        int linha = t.getLine();
//        int coluna = t.getCharPositionInLine();
        // TODO: fix error format
        errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
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

    public static TipoLA verificarTipo(LAParser.Valor_constanteContext ctx) {
        TipoLA ret;
        if (ctx.CADEIA() != null) {
            ret = TiposLA.LITERAL;
        } else if (ctx.NUM_INT() != null) {
            ret = TiposLA.INTEIRO;
        } else if (ctx.NUM_REAL() != null) {
            ret = TiposLA.REAL;
        } else {
            ret = TiposLA.LOGICO;
        }
        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.TipoContext ctx) {
        TipoLA ret;
        if (ctx.start.getText().equals("registro")) {
            ret = verificarTipo(escopos, ctx.registro());
        } else {
            ret = verificarTipo(escopos, ctx.tipo_estendido());
        }

        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.Tipo_estendidoContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.tipo_basico_ident());

        if (ctx.start.getText().equals("^")) {
            ret = new TiposLA.Ponteiro(ret);
        }

        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.Tipo_basico_identContext ctx) {
        TipoLA ret;
        if (ctx.tipo_basico() != null) {
            ret = verificarTipo(ctx.tipo_basico());
        } else {
            // TODO: checar se é um tipo
            EntradaTabelaDeSimbolos etds = escopos.verificar(ctx.IDENT().getText());
            ret = etds == null ? TiposLA.INVALIDO : etds.tipo;
        }
        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.RegistroContext ctx) {
        Map<String, TipoLA> campos = new HashMap<>();
        boolean temErro = false;
        for (var variavel : ctx.variavel()) {
            TipoLA tipoVar = verificarTipo(escopos, variavel.tipo());
            for (var id : variavel.identificador()) {
                String strId = id.getText();
                if (campos.containsKey(strId)) {
                    // TODO: erro - identificador ja declarado
                    temErro = true;
                } else {
                    campos.put(strId, tipoVar);
                }
            }
        }
        return temErro ? TiposLA.INVALIDO : new TiposLA.Registro(campos);
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.IdentificadorContext ctx) {
        TipoLA ret = TiposLA.INVALIDO;
        EntradaTabelaDeSimbolos etds = escopos.verificar(ctx.id1.getText());

        if (etds != null) {
            ret = etds.tipo;

            for (var id : ctx.outrosIds) {
                if (ret instanceof TiposLA.Registro) {
                    ret = ((TiposLA.Registro) ret).campos.get(id.getText());
                    if (ret == null) {
                        ret = TiposLA.INVALIDO;
                    }
                } else {
                    ret = TiposLA.INVALIDO;
                }
            }
            if (ctx.dimensao() != null) {
                for (var exp : ctx.dimensao().exp_aritmetica()) {
                    TipoLA tipoIndice = verificarTipo(escopos, exp);
                    if (tipoIndice != TiposLA.INTEIRO) {
                        ret = TiposLA.INVALIDO;
                    } else if (ret instanceof TiposLA.Arranjo) {
                        ret = ((TiposLA.Arranjo) ret).tipo;
                    }
                }
            }
        }

        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.ExpressaoContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.termo1);


        if (ret == TiposLA.LOGICO) {
            for (var termo : ctx.outrosTermos) {
                TipoLA tipoTermo = verificarTipo(escopos, termo);
                if (tipoTermo != TiposLA.LOGICO) {
                    ret = TiposLA.INVALIDO;
                    break;
                }
            }
        } else if (!ctx.outrosTermos.isEmpty()) {
            ret = TiposLA.INVALIDO;
        }
        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.Termo_logicoContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.fator1);

        if (ret == TiposLA.LOGICO) {
            for (var fator : ctx.outrosFatores) {
                TipoLA tipoFator = verificarTipo(escopos, fator);
                if (tipoFator != TiposLA.LOGICO) {
                    ret = TiposLA.INVALIDO;
                    break;
                }
            }
        } else if (!ctx.outrosFatores.isEmpty()) {
            ret = TiposLA.INVALIDO;
        }
        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.Fator_logicoContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.parcela_logica());

        if (ctx.start.getText().equals("nao") && ret != TiposLA.LOGICO) {
            ret = TiposLA.INVALIDO;
        }

        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.Parcela_logicaContext ctx) {
        TipoLA ret;
        if (ctx.exp_relacional() != null) {
            ret = verificarTipo(escopos, ctx.exp_relacional());
        } else { // "verdadeiro" ou "falso"
            ret = TiposLA.LOGICO;
        }
        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.Exp_relacionalContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.exp1);

        if (ctx.exp2 != null) {
            TipoLA tipoExp2 = verificarTipo(escopos, ctx.exp2);
            if (ret == TiposLA.REAL && (tipoExp2 == TiposLA.REAL || tipoExp2 == TiposLA.INTEIRO)) {
                ret = TiposLA.LOGICO;
            } else if (ret == TiposLA.INTEIRO && (tipoExp2 == TiposLA.REAL || tipoExp2 == TiposLA.INTEIRO)) {
                ret = TiposLA.LOGICO;
            } else if (ret == TiposLA.LITERAL && ret == tipoExp2) {
                ret = TiposLA.LOGICO;
            } else if (ret instanceof TiposLA.Ponteiro || ret == TiposLA.LOGICO) {
                String op = ctx.op_relacional().getText();
                if (ret == tipoExp2 && (op.equals("=") || op.equals("<>"))) {
                    ret = TiposLA.LOGICO;
                } else {
                    ret = TiposLA.INVALIDO;
                }
            } else {
                ret = TiposLA.INVALIDO;
            }
        }

        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.Exp_aritmeticaContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.termo1);

        if (ret == TiposLA.LITERAL) {
            for (int i = 0; i < ctx.outrosTermos.size(); i++) {
                String op = ctx.op.get(i).getText();
                TipoLA tipoTermo = verificarTipo(escopos, ctx.outrosTermos.get(i));
                if (!op.equals("+") || ret != tipoTermo) {
                    ret = TiposLA.INVALIDO;
                    break;
                }

            }
        } else if (ret == TiposLA.INTEIRO || ret == TiposLA.REAL) {
            for (var termo : ctx.outrosTermos) {
                TipoLA tipoTermo = verificarTipo(escopos, termo);
                if (ret == TiposLA.REAL && tipoTermo == TiposLA.INTEIRO ||
                        ret == TiposLA.INTEIRO && tipoTermo == TiposLA.REAL) {
                    ret = TiposLA.REAL;
                } else if (ret != tipoTermo) {
                    ret = TiposLA.INVALIDO;
                    break;
                }
            }
        } else if (!ctx.outrosTermos.isEmpty()) {
            ret = TiposLA.INVALIDO;
        }

        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.TermoContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.fator1);

        if (ret == TiposLA.INTEIRO || ret == TiposLA.REAL) {
            for (var fator : ctx.outrosFatores) {
                TipoLA tipoFator = verificarTipo(escopos, fator);
                if (tipoFator != TiposLA.INTEIRO && tipoFator != TiposLA.REAL) {
                    ret = TiposLA.INVALIDO;
                    break;
                } else if (tipoFator == TiposLA.REAL) {
                    ret = TiposLA.REAL;
                }
            }
        } else if (!ctx.outrosFatores.isEmpty()) {
            ret = TiposLA.INVALIDO;
        }

        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.FatorContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.parcela1);

        if (ret == TiposLA.INTEIRO) {
            for (var parcela : ctx.outrasParcelas) {
                TipoLA tipoParcela = verificarTipo(escopos, parcela);
                if (tipoParcela != TiposLA.INTEIRO) {
                    ret = TiposLA.INVALIDO;
                    break;
                }
            }
        } else if (!ctx.outrasParcelas.isEmpty()) {
            ret = TiposLA.INVALIDO;
        }

        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.ParcelaContext ctx) {
        TipoLA ret;
        if (ctx.parcela_unario() != null) {
            ret = verificarTipo(escopos, ctx.parcela_unario());
            if (ctx.op != null && (ret != TiposLA.INTEIRO && ret != TiposLA.REAL)) {
                ret = TiposLA.INVALIDO;
            }
        } else {
            ret = verificarTipo(escopos, ctx.parcela_nao_unario());
        }
        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.Parcela_unarioContext ctx) {
        TipoLA ret = TiposLA.INVALIDO;
        if (ctx.identificador() != null) {
            ret = verificarTipo(escopos, ctx.identificador());
            if (ctx.OP_PONTEIRO() != null) {
                if (ret instanceof TiposLA.Ponteiro) {
                    ret = ((TiposLA.Ponteiro) ret).tipoConteudo;
                } else {
                    ret = TiposLA.INVALIDO;
                }
            }
        } else if (ctx.IDENT() != null) {
            EntradaTabelaDeSimbolos etds = escopos.verificar(ctx.IDENT().getText());
            if (etds != null && etds.tipo instanceof TiposLA.Funcao) {
                ret = etds.tipo;
                if (ctx.args.size() == ((TiposLA.Funcao) ret).tipoParametros.size()) {
                    for (int i = 0; i < ctx.args.size(); i++) {
                        TipoLA tipoExp = verificarTipo(escopos, ctx.expressao(i));
                        if (tipoExp != ((TiposLA.Funcao) ret).tipoParametros.get(i)) {
                            ret = TiposLA.INVALIDO;
                            break;
                        }
                    }
                } else {
                    ret = TiposLA.INVALIDO;
                }
                if (ret != TiposLA.INVALIDO) {
                    ret = ((TiposLA.Funcao) ret).tipoRetorno;
                } else {
                    LASemanticoUtils.adicionarErroSemantico(
                            ctx.IDENT().getSymbol(),
                            String.format(
                                    Mensagens.ERRO_PARAMETROS_INCOMPATIVEIS,
                                    ctx.IDENT().getText()));
                }
            }
        } else if (ctx.NUM_INT() != null) {
            ret = TiposLA.INTEIRO;
        } else if (ctx.NUM_REAL() != null) {
            ret = TiposLA.REAL;
        } else {
            ret = verificarTipo(escopos, ctx.expParam);
        }
        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.Parcela_nao_unarioContext ctx) {
        TipoLA ret;
        if (ctx.CADEIA() != null) {
            ret = TiposLA.LITERAL;
        } else {
            ret = verificarTipo(escopos, ctx.identificador());
            if (ret != TiposLA.INVALIDO) {
                // TODO: Fazer tipo endereço ter um tipo
                ret = TiposLA.ENDERECO;
            }
        }
        return ret;
    }
}