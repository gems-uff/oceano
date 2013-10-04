/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.tools.metrics.expression;

import br.uff.ic.oceano.core.tools.metrics.service.DerivedMetricService;

/**
 *
 * @author wallace
 */
public class Token {
    int type;
    double doubleValue;
    String metricAcronym;
    Token prox;
    Token left;
    Token right;

    public Token(int type){
        prox=null;
        left=null;
        right=null;
        this.type=type;
        doubleValue=0;
    }
    public Token(double doubleValue){
        prox=null;
        left=null;
        right=null;
        this.doubleValue=doubleValue;
        this.type=DerivedMetricService.DOUBLE_VALUE;
    }
    public Token(String metricAcronym){
        prox=null;
        left=null;
        right=null;
        this.type=DerivedMetricService.METRIC_MANAGER;
        doubleValue=0;
        this.metricAcronym=metricAcronym;
    }
    public int getType(){
        return type;
    }
    public double getDoubleValue(){
        return doubleValue;
    }
    public String getMetricAcronym(){
        return metricAcronym;
    }
    public Token getProx(){
        return prox;
    }
    public Token getLeft(){
        return left;
    }
    public Token getRight(){
        return right;
    }
    public void setDoubleValue(double doubleValue){
        this.doubleValue=doubleValue;
    }
    public void setType(int type){
        this.type=type;
    }
    public void setMetricAcronym(String metricAcronym){
        this.metricAcronym=metricAcronym;
    }
    public void setProx(Token prox){
        this.prox=prox;
    }
    public void setLeft(Token left){
        this.left=left;
    }
    public void setRight(Token right){
        this.right=right;
    }


}
