package br.uff.ic.gems.peixeespadacliente.resolution;

import br.uff.ic.gems.peixeespadacliente.conflicts.ConflictManager;
import br.uff.ic.gems.peixeespadacliente.conflicts.ConflictWithResolution;
import br.uff.ic.gems.peixeespadacliente.conflicts.PullPushConflictResolution;
import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.symptom.PullPushSymptom;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinMember;

/**
 *
 * @author João Felipe
 */
public class PullPushResolution extends Resolution {

    private Map<String, PullPushConflictResolution> resolutions;

    public Map<String, PullPushConflictResolution> getResolutions() {
        return this.resolutions;
    }

    public PullPushResolution(PullPushResolution oldResolution, List<ConflictWithResolution> newResolution) {
        super(oldResolution.getSymptom());
        this.resolutions = new TreeMap<String, PullPushConflictResolution>();
        this.resolutions.putAll(oldResolution.getResolutions());
        for (ConflictWithResolution conflictWithResolution : newResolution) {
            this.resolutions.put(conflictWithResolution.getConflict().getDescription(), new PullPushConflictResolution(conflictWithResolution.getResolution()));
        }
    }

    public PullPushResolution(PullPushSymptom symptom, List<ConflictWithResolution> resolution) {
        super(symptom);
        this.resolutions = new TreeMap<String, PullPushConflictResolution>();

        for (ConflictWithResolution conflictWithResolution : resolution) {
            this.resolutions.put(conflictWithResolution.getConflict().getDescription(), new PullPushConflictResolution(conflictWithResolution.getResolution()));
        }
    }

    public PullPushResolution(PullPushSymptom symptom) {
        super(symptom);
        this.resolutions = new TreeMap<String, PullPushConflictResolution>();
    }

    public PullPushResolution(PullPushSymptom symptom, List<ConflictWithResolution> resolution, BinMember member, BinCIType target) {
        super(symptom);
        this.resolutions = new TreeMap<String, PullPushConflictResolution>();

        for (ConflictWithResolution conflictWithResolution : resolution) {
            this.resolutions.put(conflictWithResolution.getConflict().getDescription(), new PullPushConflictResolution(conflictWithResolution.getResolution()));
        }
    }

    public PullPushSymptom getPullPushSymptom() {
        return (PullPushSymptom) getSymptom();
    }

    public void reloadResolutions(ConflictManager conflictManager, StringBuilder builder) throws RefactoringException {
        if (builder == null) {
            builder = new StringBuilder();
        }
        conflictManager.reloadResolutions(this, builder);
    }

    public void reloadResolutions(StringBuilder builder) throws RefactoringException {
        this.getPullPushSymptom().getPullPushRefactoringTool().prepareSymptom(getSymptom());
        reloadResolutions(new ConflictManager(this.getPullPushSymptom().getPullPushRefactoringTool().getResolver()), builder);
    }

    @Override
    public boolean apply(StringBuilder builder) throws RefactoringException {
        this.reloadResolutions(builder);
        return this.getPullPushSymptom().getPullPushRefactoringTool().applyCheckingPreAndPosCondictions(this);
    }
}
