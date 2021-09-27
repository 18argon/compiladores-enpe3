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



    public static TipoLA verificarTipo(Escopos escopos, LAParser.TipoContext ctx) {
        TipoLA ret = null;


        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.Tipo_estendidoContext ctx) {
        TipoLA ret = null;

        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.IdentificadorContext ctx) {
        TipoLA ret = TiposLA.INVALIDO;
        TabelaDeSimbolos.EntradaTabelaDeSimbolos etds = escopos.verificar(ctx.id1.getText());
        if (etds != null) {
            ret = etds.tipo;

            for (var id : ctx.outrosIds) {
                if (ret instanceof TiposLA.Registro) {
                    ret = ((TiposLA.Registro) ret).campos.get(id.getText());
                    if (ret != null) {
                        ret = TiposLA.INVALIDO;
                    }
                } else {
                    ret = TiposLA.INVALIDO;
                }
            }
            if (ctx.dimensao() != null) {
                for(var exp: ctx.dimensao().exp_aritmetica()) {
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

        if(ctx.outrosTermos.size() != 0 && ret != TiposLA.LOGICO) {
            ret = TiposLA.INVALIDO;
        } else {
            for (var termo : ctx.outrosTermos) {
                TipoLA tipoTermo = verificarTipo(escopos, termo);
                if (tipoTermo != TiposLA.LOGICO) {
                    ret = TiposLA.INVALIDO;
                }
            }
        }
        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.Termo_logicoContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.fator1);

        if(ctx.outrosFatores.size() != 0 && ret != TiposLA.LOGICO) {
            ret = TiposLA.INVALIDO;
        } else {
            for (var fator : ctx.outrosFatores) {
                TipoLA tipoFator = verificarTipo(escopos, fator);
                if (tipoFator != TiposLA.LOGICO) {
                    ret = TiposLA.INVALIDO;
                }
            }
        }
        return null;
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
            } else if (ret == TiposLA.INTEIRO) {
                if (tipoExp2 == TiposLA.INTEIRO) {
                    ret = TiposLA.LOGICO;
                } else if (tipoExp2 == TiposLA.REAL) {
                    ret = TiposLA.LOGICO;
                } else {
                    ret = TiposLA.INVALIDO;
                }
            } else if (ret == TiposLA.LITERAL && ret == tipoExp2) {
                ret = TiposLA.LOGICO;
            } else if (ret instanceof TiposLA.Endereco || ret == TiposLA.LOGICO) {
                String op = ctx.OP_RELACIONAL().getText();
                if (ret != tipoExp2 && (!op.equals("=") && !op.equals("<>"))) {
                    ret = TiposLA.INVALIDO;
                } else {
                    ret = TiposLA.LOGICO;
                }
            } else if (ret == tipoExp2) {
                ret = TiposLA.LOGICO;
            } else {
                ret = TiposLA.INVALIDO;
            }
        }

        return ret;
    }

    public static TipoLA verificarTipo(Escopos escopos, LAParser.Exp_aritmeticaContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.termo1);

        if(ctx.outrosTermos.size() != 0 && (ret != TiposLA.INTEIRO && ret != TiposLA.REAL)) {
            ret = TiposLA.INVALIDO;
        } else {
            for (var termo : ctx.outrosTermos) {
                TipoLA tipoTermo = verificarTipo(escopos, termo);
                if (tipoTermo != TiposLA.INTEIRO && tipoTermo != TiposLA.REAL) {
                    ret = TiposLA.INVALIDO;
                } else if (tipoTermo == TiposLA.REAL) {
                    ret = TiposLA.REAL;
                }
            }
        }

        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.TermoContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.fator1);

        if(ctx.outrosFatores.size() != 0 && (ret != TiposLA.INTEIRO && ret != TiposLA.REAL)) {
            ret = TiposLA.INVALIDO;
        } else {
            for (var fator : ctx.outrosFatores) {
                TipoLA tipoFator = verificarTipo(escopos, fator);
                if (tipoFator != TiposLA.INTEIRO && tipoFator != TiposLA.REAL) {
                    ret = TiposLA.INVALIDO;
                } else if (tipoFator == TiposLA.REAL) {
                    ret = TiposLA.REAL;
                }
            }
        }
        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.FatorContext ctx) {
        TipoLA ret = verificarTipo(escopos, ctx.parcela1);

        if(ctx.outrasParcelas.size() != 0 && ret != TiposLA.INTEIRO) {
            ret = TiposLA.INVALIDO;
        } else {
            for (var parcela : ctx.outrasParcelas) {
                TipoLA tipoParcela = verificarTipo(escopos, parcela);
                if (tipoParcela != TiposLA.INTEIRO) {
                    ret = TiposLA.INVALIDO;
                }
            }
        }
        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.ParcelaContext ctx) {
        TipoLA ret;
        if (ctx.parcela_unario() != null) {
            ret = verificarTipo(escopos, ctx.parcela_unario());
            if (ctx.op_unario() != null && (ret != TiposLA.INTEIRO && ret != TiposLA.REAL)) {
                ret = TiposLA.INVALIDO;
            }
        } else {
            ret = verificarTipo(escopos, ctx.parcela_nao_unario());
        }
        return ret;
    }

    private static TipoLA verificarTipo(Escopos escopos, LAParser.Parcela_unarioContext ctx) {
        TipoLA ret = TiposLA.INVALIDO;
        if (ctx.identificador() != null) {
            ret = verificarTipo(escopos, ctx.identificador());
            if (ctx.start.getText().equals("^")) {
                if (ret instanceof TiposLA.Endereco) {
                    ret = ((TiposLA.Endereco) ret).tipoConteudo;
                } else {
                    ret = TiposLA.INVALIDO;
                }
            }
        } else if (ctx.IDENT() != null) {
            // TODO: verificar chamada de função ou procedimento
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
                ret = new TiposLA.Endereco(ret);
            }
        }
        return ret;
    }
}
