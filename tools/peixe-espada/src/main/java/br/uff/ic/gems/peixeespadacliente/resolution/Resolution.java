package br.uff.ic.gems.peixeespadacliente.resolution;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.model.QualityAttribute;

/**
 *
 * @author João Felipe
 */
public abstract class Resolution implements Comparable {

    private Symptom symptom;
    private QualityAttribute resolutionQuality = null;

    protected Resolution(Symptom symptom) {
        this.symptom = symptom;
    }

    public QualityAttribute getResolutionQuality() {
        return resolutionQuality;
    }

    public void setResolutionQuality(QualityAttribute resolutionQuality) {
        this.resolutionQuality = resolutionQuality;
    }

    public boolean apply(StringBuilder builder) throws RefactoringException {
        symptom.getRefactoringTool().prepareSymptom(symptom);
        return symptom.getRefactoringTool().applyCheckingPreAndPosCondictions(this);
    }
    
    public boolean applyWorking(StringBuilder builder) throws RefactoringException {
        return this.apply(builder) && this.getSymptom().getRefactoringTool().isWorking();
    }

    public boolean applyCalculateQA(LocalManagerAgent agentPeixeEspada, StringBuilder builder) throws RefactoringException, ServiceException {
        if (this.applyWorking(builder)) {
            this.calculateQA(agentPeixeEspada);
            return true;
        } else {
            return false;
        }
    }

    public void calculateQA(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        QualityAttribute calculadeAttribute = new QualityAttribute();

        if (agentPeixeEspada.isTesting()) {
            calculadeAttribute.setCurrentValue(1.0D);
        } else {
            try {
                calculadeAttribute = agentPeixeEspada.getMeterAgent().calculateNormalizedQualityAttributeValue(agentPeixeEspada);
            } catch (Exception ex) {
                throw new ServiceException(ex);
            }
        }

        this.setResolutionQuality(calculadeAttribute);
    }

    @Override
    public int compareTo(Object o) {
        if (this.resolutionQuality == null) {
            return -1;
        }
        if (((Resolution) o).resolutionQuality == null) {
            return 1;
        }
        if (resolutionQuality.getCurrentValue().compareTo(((Resolution) o).getResolutionQuality().getCurrentValue()) > 0) {
            return 1;
        } else if (resolutionQuality.getCurrentValue().compareTo(((Resolution) o).getResolutionQuality().getCurrentValue()) < 0) {
            return -1;
        }
        return 0;
    }

    public Symptom getSymptom() {
        return symptom;
    }

    @Override
    public String toString() {
        String result = symptom.toString();
        if (getResolutionQuality() != null) {
            result = result + " Q.A=" + getResolutionQuality().getCurrentValue();
        }
        return result;
    }
}
