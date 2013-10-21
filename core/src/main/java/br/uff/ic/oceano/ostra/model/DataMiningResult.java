/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.model;

import br.uff.ic.oceano.util.DateUtil;
import br.uff.ic.oceano.ostra.controle.DataMiningControl;
import br.uff.ic.oceano.ostra.service.DataMiningResultService;
import br.uff.ic.oceano.ostra.tools.datamining.util.DataMiningPatternComparator;
import br.uff.ic.oceano.util.NumberUtil;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author DanCastellani
 */
@NamedQueries({
    @NamedQuery(name = "DataMiningResult.getAll", query = "select dmr from DataMiningResult dmr"),
    @NamedQuery(name = "DataMiningResult.getToDetailById", query = "select dmr from DataMiningResult dmr left outer join fetch dmr.dataMiningPatterns where dmr.id = ?")
})
@Entity
@Table(name = "ostra_DataMiningResult")
@SequenceGenerator(name = "ostra_dataminingresult_seq", sequenceName = "ostra_dataminingresult_seq")
public class DataMiningResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ostra_dataminingresult_seq")
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date minedInTime;
    @Lob
    private String arff;
    @Lob
    private String resultData;
    private String usedAlgorithmName;
    private String usedAlgorithmDescription;
    private Double minSupport;
    private Double minConfidence;
    @Transient
    private Task task;
    @Transient
    private DataBaseSnapshot dataBaseSnapshot;
    @OneToMany(mappedBy = "dataMiningResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DataMiningPattern> dataMiningPatterns;
    @Transient
    private boolean prepared = false;
    @Lob
    private String description;
    private String ruleMetricName;
    private int numberOfInstances;
    @Transient
    private boolean ordered = false;

    public List<String> getAttributes() {
        List<String> attributes = new LinkedList<String>();

        for (String line : this.getArffAsStringList()) {
            if (line.startsWith("@ATTRIBUTE")) {
                attributes.add(getAttribute(line));
            } else if (line.startsWith("@DATA")) {
                break;
            }
        }
        return attributes;
    }

    private String getAttribute(String line) {
        if (line.startsWith("@ATTRIBUTE")) {
            String attribute = line.substring(line.indexOf(" ") + 1);

            if (attribute.startsWith("\"")) {
                attribute = attribute.substring(attribute.indexOf("\"") + 1);
                attribute = attribute.substring(0, attribute.indexOf("\""));
            } else {
                attribute = attribute.split(" ")[0];
            }
            return attribute;
        }
        return null;
    }

    public void prepare() {
        //preparation to make the dataMiningResult saveble
        if (!isPrepared()) {
            for (DataMiningPattern dataMiningPattern : dataMiningPatterns) {
                dataMiningPattern.setDataMiningResult(this);
            }
            DataMiningResultService.cleanDataMiningResult(this);
            prepared = true;
        }
    }

    public int getNumberOfMinedPatterns() {
        if (dataMiningPatterns == null) {
            return 0;
        }
        return dataMiningPatterns.size();
    }

    /**
     * This method returns the result as a list of Strings.
     * Each String represents a line of the original output.
     * The delimiter \n is used to break the lines
     *
     * @return A list of Strings representing each line of the original output
     */
    public List<String> getResultAsStringList() {
        StringTokenizer st = new StringTokenizer(resultData, "\n");
        List<String> lines = new LinkedList<String>();
        while (st.hasMoreElements()) {
            lines.add(st.nextToken());
        }
        return lines;
    }

    /**
     * This method arff used to mine as a list of Strings.
     * Each String represents a line of the original input arff file.
     * The delimiter \n is used to break the lines
     *
     * @return A list of Strings representing each line of the original input
     */
    public List<String> getArffAsStringList() {
        StringTokenizer st = new StringTokenizer(arff, "\n");
        List<String> lines = new LinkedList<String>();
        while (st.hasMoreElements()) {
            lines.add(st.nextToken());
        }
        return lines;
    }

    public String getFormatedMinedInTime() {
        return DateUtil.format(minedInTime);
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the minedInTime
     */
    public Date getMinedInTime() {
        return minedInTime;
    }

    /**
     * @param minedInTime the minedInTime to set
     */
    public void setMinedInTime(Date minedInTime) {
        this.minedInTime = minedInTime;
    }

    /**
     * @return the arff
     */
    public String getArff() {
        return arff;
    }

    /**
     * @param arff the arff to set
     */
    public void setArff(String arff) {
        this.arff = arff;
    }

    /**
     * @return the result
     */
    public String getResultData() {
        return resultData;
    }

    /**
     * @param result the result to set
     */
    public void setResultData(String result) {
        this.resultData = result;
    }

    /**
     * @return the usedAlgorithmName
     */
    public String getUsedAlgorithmName() {
        return usedAlgorithmName;
    }

    /**
     * @param usedAlgorithmName the usedAlgorithmName to set
     */
    public void setUsedAlgorithmName(String usedAlgorithmName) {
        this.usedAlgorithmName = usedAlgorithmName;
    }

    /**
     * @return the usedAlgorithmDescription
     */
    public String getUsedAlgorithmDescription() {
        return usedAlgorithmDescription;
    }

    /**
     * @param usedAlgorithmDescription the usedAlgorithmDescription to set
     */
    public void setUsedAlgorithmDescription(String usedAlgorithmDescription) {
        this.usedAlgorithmDescription = usedAlgorithmDescription;
    }

    /**
     * @return the task
     */
    public Task getTask() {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * @return the dataBaseSnapshot
     */
    public DataBaseSnapshot getDataBaseSnapshot() {
        return dataBaseSnapshot;
    }

    /**
     * @param dataBaseSnapshot the dataBaseSnapshot to set
     */
    public void setDataBaseSnapshot(DataBaseSnapshot dataBaseSnapshot) {
        this.dataBaseSnapshot = dataBaseSnapshot;
    }

    /**
     * @return the dataMiningPatterns
     */
    public List<DataMiningPattern> getDataMiningPatterns() {
        if (!ordered) {
            //order results
            System.out.println("Ordenando resultados");
            DataMiningPatternComparator dataMiningPatternComparator = new DataMiningPatternComparator();
            dataMiningPatternComparator.orderByMetric(DataMiningControl.LIFT);
            Collections.sort(dataMiningPatterns, dataMiningPatternComparator);
            ordered = true;
        }

        return dataMiningPatterns;
    }

    /**
     * @param dataMiningPatterns the dataMiningPatterns to set
     */
    public void setDataMiningPatterns(List<DataMiningPattern> dataMiningPatterns) {
        this.dataMiningPatterns = dataMiningPatterns;
    }

    /**
     * @return the minSupport
     */
    public Double getMinSupport() {
        return minSupport;
    }

    /**
     * @param minSupport the minSupport to set
     */
    public void setMinSupport(Double minSupport) {
        this.minSupport = minSupport;
    }

    /**
     * @return the minConfidence
     */
    public Double getMinConfidence() {
        return minConfidence;
    }
    
    /**
     * 
     * @return formated string from getMinConfidence
     */
    public String getFormatedMinConfidence() {
        return NumberUtil.format(getMinConfidence());
    }

    /**
     * @param minConfidence the minConfidence to set
     */
    public void setMinConfidence(Double minConfidence) {
        this.minConfidence = minConfidence;
    }

    /**
     * @return the prepared
     */
    public boolean isPrepared() {
        return prepared;
    }

    /**
     * @param prepared the prepared to set
     */
    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the numberOfInstances
     */
    public int getNumberOfInstances() {
        return numberOfInstances;
    }

    /**
     * @param numberOfInstances the numberOfInstances to set
     */
    public void setNumberOfInstances(int numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
    }

    /**
     * @return the ruleMetricName
     */
    public String getRuleMetricName() {
        return ruleMetricName;
    }

    /**
     * @param ruleMetricName the ruleMetricName to set
     */
    public void setRuleMetricName(String ruleMetricName) {
        this.ruleMetricName = ruleMetricName;
    }
}
