/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.controle;

import weka.associations.Apriori;
import weka.core.SelectedTag;
import weka.core.Tag;

/**
 *
 * @author DanCastellani
 */
public class DataMiningControl {

    private Double minSup;
    private Double maxSup = 1D;
    private Double minMetric;
    private SelectedTag selectedTag;
    private int maxRules;
    private boolean verboseMode;
    //copied from Apriori
    /** Metric types. */
    public static final int CONFIDENCE = 0;
    public static final int LIFT = 1;
    public static final int LEVERAGE = 2;
    public static final int CONVICTION = 3;
    public static final Tag[] TAGS_SELECTION = {
        new Tag(CONFIDENCE, "Confidence"),
        new Tag(LIFT, "Lift"),
        new Tag(LEVERAGE, "Leverage"),
        new Tag(CONVICTION, "Conviction")
    };

    public DataMiningControl() {
        this.selectedTag = new SelectedTag(0, Apriori.TAGS_SELECTION); //confiddence ref weka.Apriori.
        this.minMetric = Constantes.CONFIANCA_MIN_PADRAO;
        this.minSup = Constantes.SUPORTE_MIN_PADRAO;
        this.verboseMode = Constantes.VERBOSE_MODE;
        this.maxRules = Constantes.MAX_RULES;
    }

    public static String[] getPossibleMetricTypes() {
        String[] metricTypes = new String[Apriori.TAGS_SELECTION.length];
        for (int i = 0; i < metricTypes.length; i++) {
            metricTypes[i] = Apriori.TAGS_SELECTION[i].getReadable();
        }

        return metricTypes;
    }

    public void setMetricType(int tagID) {
        try {
            TAGS_SELECTION[tagID].getID();
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new RuntimeException("TagId not known. Metric's TagId type must be one of: 0, 1, 2, 3");
        }
        this.setSelectedTag(new SelectedTag(tagID, Apriori.TAGS_SELECTION));
    }

    public void setMetricType(String readableMetricType) {
        this.setSelectedTag(null);
        for (int i = 0; i < Apriori.TAGS_SELECTION.length; i++) {
            if (Apriori.TAGS_SELECTION[i].getReadable().equals(readableMetricType)) {
                this.setSelectedTag(new SelectedTag(Apriori.TAGS_SELECTION[i].getID(), Apriori.TAGS_SELECTION));
            }
        }

        if (getSelectedTag() == null) {
            throw new RuntimeException(readableMetricType + " not known. Readable Metric type must be one of: " + getPossibleMetricTypes());
        }
    }
    
    public String getMetricType(){
        if (getSelectedTag() == null || getSelectedTag().getSelectedTag() == null) {
            return null;
        }
        return getSelectedTag().getSelectedTag().getReadable();
    }

    /**
     * @return the verboseMode
     */
    public boolean isVerboseMode() {
        return verboseMode;
    }

    /**
     * @param verboseMode the verboseMode to set
     */
    public void setVerboseMode(boolean verboseMode) {
        this.verboseMode = verboseMode;
    }

    /**
     * @return the minSup
     */
    public Double getMinSup() {
        return minSup;
    }

    /**
     * @param minSup the minSup to set
     */
    public void setMinSup(Double minSup) {
        this.minSup = minSup;
    }

    /**
     * @return the minConf
     */
    public Double getMinMetric() {
        return minMetric;
    }

    /**
     * @param minConf the minConf to set
     */
    public void setMinMetric(Double minConf) {
        this.minMetric = minConf;
    }

    /**
     * @return the maxRules
     */
    public int getMaxRules() {
        return maxRules;
    }

    /**
     * @param maxRules the maxRules to set
     */
    public void setMaxRules(int maxRules) {
        this.maxRules = maxRules;
    }

    /**
     * @return the selectedTag
     */
    public SelectedTag getSelectedTag() {
        return selectedTag;
    }

    /**
     * @param selectedTag the selectedTag to set
     */
    public void setSelectedTag(SelectedTag selectedTag) {
        this.selectedTag = selectedTag;
    }

    public String getReadableMetricType() {
        if (selectedTag == null) {
            return null;
        }
        return this.selectedTag.getSelectedTag().getReadable();
    }

    /**
     * This method does nothing, it's an empty method.
     * @param readableMetrictype
     */
    @Deprecated
    public void SetReadableMetricType(String readableMetrictype) {
    }

    /**
     * @return the maxSup
     */
    public Double getMaxSup() {
        return maxSup;
    }

    /**
     * @param maxSup the maxSup to set
     */
    public void setMaxSup(Double maxSup) {
        this.maxSup = maxSup;
    }

}
