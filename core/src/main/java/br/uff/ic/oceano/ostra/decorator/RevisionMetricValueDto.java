/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.decorator;

import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.util.NumberUtil;
import java.util.Calendar;

/**
 *
 * @author daniel
 */
public class RevisionMetricValueDto {

    private String revisionNumber;
    private String commiter;
    private String sumMetricValue = "N/A";
    private String countItems;
    private String avgMetricValue = "N/A";
    private Calendar commitDate;
    private double absoluteMetricValue;
    private double deltaMetricValue;

    public RevisionMetricValueDto(String revisionNumber, String commiter, String countItems, Calendar commitDate, double absoluteMetricValue, double deltaMetricValue) {
        this.revisionNumber = revisionNumber;
        this.commiter = commiter;
        this.countItems = countItems;
        this.commitDate = commitDate;
        this.absoluteMetricValue = absoluteMetricValue;
        this.deltaMetricValue = deltaMetricValue;
    }

    public RevisionMetricValueDto() {
    }

    static public RevisionMetricValueDto createFromMetricValue(MetricValue mv) {
        RevisionMetricValueDto dto = new RevisionMetricValueDto();
        dto.revisionNumber = mv.getRevision().getNumber().toString();

        if (mv.isDelta()) {
            dto.deltaMetricValue = mv.getDoubleValue();
        } else {
            dto.absoluteMetricValue = mv.getDoubleValue();
        }
        dto.commiter = mv.getRevision().getCommiter();
        dto.countItems = "1";
        dto.commitDate = mv.getRevision().getCommitDate();

        return dto;
    }

    @Override
    public String toString() {
        return "#" + revisionNumber + " - " + commiter + " [ sum=" + sumMetricValue + ", count=" + countItems + ", avg=" + avgMetricValue;
    }

    public double getRevisionNumberAsDouble() {
        return Double.valueOf(revisionNumber);
    }

    /**
     * @return the revisionNumber
     */
    public String getRevisionNumber() {
        return revisionNumber;
    }

    /**
     * @param revisionNumber the revisionNumber to set
     */
    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    /**
     * @return the commiter
     */
    public String getCommiter() {
        return commiter;
    }

    /**
     * @param commiter the commiter to set
     */
    public void setCommiter(String commiter) {
        this.commiter = commiter;
    }

    /**
     * @return the sumMetricValue
     */
    public String getSumMetricValue() {
        return sumMetricValue;
    }

    /**
     * @param sumMetricValue the sumMetricValue to set
     */
    public void setSumMetricValue(String sumMetricValue) {
        this.sumMetricValue = sumMetricValue;
    }

    /**
     * @return the countItems
     */
    public String getCountItems() {
        return countItems;
    }

    /**
     * @param countItems the countItems to set
     */
    public void setCountItems(String countItems) {
        this.countItems = countItems;
    }

    /**
     * @return the avgMetricValue
     */
    public String getAvgMetricValue() {
        return avgMetricValue;
    }

    /**
     * @param avgMetricValue the avgMetricValue to set
     */
    public void setAvgMetricValue(String avgMetricValue) {
        this.avgMetricValue = avgMetricValue;
    }

    /**
     * @return the commitDate
     */
    public Calendar getCommitDate() {
        return commitDate;
    }

    /**
     * @param commitDate the commitDate to set
     */
    public void setCommitDate(Calendar commitDate) {
        this.commitDate = commitDate;
    }

    /**
     * @return the absoluteMetricValue
     */
    public double getAbsoluteMetricValue() {
        return absoluteMetricValue;
    }

    /**
     * @param absoluteMetricValue the absoluteMetricValue to set
     */
    public void setAbsoluteMetricValue(double absoluteMetricValue) {
        this.absoluteMetricValue = absoluteMetricValue;
    }

    /**
     * @return the deltaMetricValue
     */
    public double getDeltaMetricValue() {
        return deltaMetricValue;
    }

    /**
     * @param deltaMetricValue the deltaMetricValue to set
     */
    public void setDeltaMetricValue(double deltaMetricValue) {
        this.deltaMetricValue = deltaMetricValue;
    }
}
