/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.tools.metrics.expression;

/**
 *
 * @author wallace
 */
public class ExpressionPackage {
    private int type;
    private double doubleValue;
    private MetricExpression metricExpression;

    public ExpressionPackage(int type){
        this.type=type;
        this.doubleValue=0;
        this.metricExpression=null;
    }
    public ExpressionPackage(int type, double doubleValue){
        this.type=type;
        this.doubleValue=doubleValue;
        metricExpression=null;
    }
    public ExpressionPackage(MetricExpression metricExpression){
        this.type=-5;
        doubleValue=0;
        this.metricExpression=metricExpression;
    }
    public int getType(){
        return type;
    }
    public double getDoubleValue(){
        return doubleValue;
    }
    public MetricExpression getMetricExpression(){
        return metricExpression;
    }
    public void setDoubleValue(double doubleValue){
        this.doubleValue=doubleValue;
    }
    public void setType(int type){
        this.type=type;
    }
    public void setMetricExpression(MetricExpression metricExpression){
        this.metricExpression=metricExpression;
    }

}
