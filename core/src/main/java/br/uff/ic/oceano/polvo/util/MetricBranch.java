/*
 * Dados calculado no Polvo
 */

package br.uff.ic.oceano.polvo.util;

/**
 *
 * @author Rafael
 */
public class MetricBranch {
    private int qtdArqRamoPrincipal;
    private int qtdArqRamoSecundario;
    private int qtdMetrica;
    private int qtdDivergencia;
    private Long revision;

    public int getQtdArqRamoPrincipal() {
        return qtdArqRamoPrincipal;
    }

    public void setQtdArqRamoPrincipal(int qtdArqRamoPrincipal) {
        this.qtdArqRamoPrincipal = qtdArqRamoPrincipal;
    }

    public int getQtdArqRamoSecundario() {
        return qtdArqRamoSecundario;
    }

    public void setQtdArqRamoSecundario(int qtdArqRamoSecundario) {
        this.qtdArqRamoSecundario = qtdArqRamoSecundario;
    }

    public int getQtdDivergencia() {
        return qtdDivergencia;
    }

    public void setQtdDivergencia(int qtdDivergencia) {
        this.qtdDivergencia = qtdDivergencia;
    }

    public int getQtdMetrica() {
        return qtdMetrica;
    }

    public void setQtdMetrica(int qtdMetrica) {
        this.qtdMetrica = qtdMetrica;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

}
