/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.view;

import br.uff.ic.oceano.core.model.QualityAttribute;
import br.uff.ic.oceano.peixeespada.model.Refactoring;

/**
 *
 * @author Heliomar
 */

public class QualityAttributeRefactoring {
    
    private Refactoring refactoring;
    private QualityAttribute qualityAttribute;


    /**
     * @return the transformacao
     */
    public Refactoring getTransformacao() {
        return refactoring;
    }

    /**
     * @param transformacao the transformacao to set
     */
    public void setTransformacao(Refactoring transformacao) {
        this.refactoring = transformacao;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof QualityAttributeRefactoring)){
            return false;
        }
        QualityAttributeRefactoring mt = (QualityAttributeRefactoring) obj;
        return mt.qualityAttribute.equals(this.qualityAttribute) && mt.refactoring.equals(this.refactoring);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + (this.refactoring != null ? this.refactoring.hashCode() : 0);
        hash = 13 * hash + (this.qualityAttribute != null ? this.qualityAttribute.hashCode() : 0);
        return hash;
    }

    public void setRefactoring(Refactoring refactoring) {
        this.refactoring = refactoring;
    }

    public Refactoring getRefactoring() {
        return this.refactoring;
    }

    public void setQualityAtributte(QualityAttribute qualityAttribute) {
        this.qualityAttribute = qualityAttribute;
    }

    public QualityAttribute getQualityAtributte() {
        return this.qualityAttribute;
    }



}
