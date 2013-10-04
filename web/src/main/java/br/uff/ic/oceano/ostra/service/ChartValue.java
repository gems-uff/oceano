/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ostra.service;

/**
 *
 * @author Wallace
 */
public class ChartValue {
    private String tag;
    private String chartPath;
    private String deltaChart;
    private String averageChart;
    private String deltaTag;
    private String averageTag;
    private boolean projectMetric;

    public ChartValue() {
    }
    
    public String getTag(){
        return tag;
    }
    public String getChartPath(){
        return chartPath;
    }
    public String getAverageChart(){
        return averageChart;
    }
    public String getDeltaChart(){
        return deltaChart;
    }
    public String getAverageTag(){
        return averageTag;
    }
    public String getDeltaTag(){
        return deltaTag;
    }
    public boolean isProjectMetric(){
        return projectMetric;
    }
    public void setChartPath(String chartPath){
        this.chartPath=chartPath;
    }
    public void setTag(String tag){
        this.tag=tag;
    }
    public void setAverageChart(String averageChart){
        this.averageChart=averageChart;
    }
    public void setDeltaChart(String deltaChart){
        this.deltaChart=deltaChart;
    }
    public void setDeltaTag(String deltaTag){
        this.deltaTag=deltaTag;
    }
    public void setAverageTag(String averageTag){
        this.averageTag=averageTag;
    }
    public void setProjectMetric(boolean projectMetric){
        this.projectMetric=projectMetric;
    }

}
