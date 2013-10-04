/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ostra.controller;

import br.uff.ic.oceano.controller.BaseBean;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.tools.metrics.service.DerivedMetricService;
import br.uff.ic.oceano.core.tools.metrics.expression.MetricExpression;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.view.SelectOneDataModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author wallace
 *
 * Revision by DHeraclio
 *  removed unsed attributes
 *  removed setMetricManagerClass from new derived metric
 */
public class CreateMetricBean extends BaseBean {

    private String PAGINA_DETAIL_METRIC = "def:/privado/ostra/metric/detailMetric";
    MetricService metricService=ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);
    private String expression="";
    private String name;
    private String acronym;
    private String description;
    private boolean showError=false;
    private String errorMessage;
    private ListDataModel metricsTable;

    List<SelectItem> metricAcronyms;

    private SelectOneDataModel<String> dataModelMetricAcronyms;
    private String novo;


    public CreateMetricBean() {
        super("CreateMetricBean");
        initializeMetricsAcronyms();
        sessao.setPerfilOstra();
//        List<Metric> metrics=metricService.getAll();
//        for(Metric mv: metrics){
//            metricAcronyms.add(mv.getAcronym());
//        }
    }
    private void initializeMetricsAcronyms() {
        List<Metric> metrics = metricService.getAll();
        List<String> metricAcronym = new ArrayList<String>(metrics.size());
        metricAcronyms=new LinkedList<SelectItem>();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        int i=1;
        for (Metric metric : metrics) {
            metricAcronym.add(metric.getAcronym());
            System.out.println("metric = " + metric);
            metricAcronyms.add(new SelectItem(i,metric.getAcronym()));
            i++;
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        this.dataModelMetricAcronyms = new SelectOneDataModel<String>(metricAcronym);

    }

     public String detailMetric(){
         return PAGINA_DETAIL_METRIC;
     }
     public String createMetric(){
         showError=false;
         DerivedMetricService derivedMetricService=new DerivedMetricService();
         List<Metric> list=metricService.getAll();
         for(Metric m: list){
             System.out.println("Metrica: "+m.getName());
         }
         try{
             MetricExpression metricExpression=derivedMetricService.buildExpression(expression);
             Metric metric=new Metric();
             metric.setAcronym(acronym);
             metric.setDescription(description);
             metric.setPreRelease(true);
             metric.setType(Metric.TYPE_FLOAT);
             metric.setExtratcsFrom(metricExpression.getExtratcsFrom());
             metric.setExtractsFromFont(false);
             metric.setDerived(true);
             metric.setExpression(expression);
             metric.setName(name);
             metricService.save(metric);
             System.out.println(name);
             System.out.println(acronym);
             System.out.println(expression);
             metricsTable = new ListDataModel(metricService.getAll());
         }catch(ServiceException e){
             showError=true;
             errorMessage=e.getMessage();
             System.out.println("Erro "+e.getMessage());
         }
         return null;
     }
     public SelectOneDataModel<String> getDataModelMetricAcronyms() {
        return dataModelMetricAcronyms;
    }
     void updateMetricExpression(){
         expression=expression+novo;
     }
     void novoSimbolo(String simbolo){
         System.out.println(simbolo);
     }
     public List<SelectItem> getMetricAcronyms(){
         return metricAcronyms;
     }
     public String getExpression(){
         return expression;
     }
     public void setExpression(String expression){
         System.out.println(expression);
         this.expression=expression;
     }
     public String getName(){
         return name;
     }
     public void setName(String name){
         this.name=name;
     }
     public String getAcronym(){
         return acronym;
     }
     public void setAcronym(String acronym){
         this.acronym=acronym;
     }
     public String getDescription(){
         return description;
     }
     public void setDescription(String description){
         this.description=description;
     }
     public boolean isShowError(){
         return showError;
     }
     public String getErrorMessage(){
         return errorMessage;
     }



     public void addMetric(ActionEvent event){
         UIParameter parameter = (UIParameter) event.getComponent().findComponent("metricAcronym");
         String metricAcronym = parameter.getValue().toString();
         System.out.println("-->> " + metricAcronym + " <<--");
         expression=expression+metricAcronym;
     }
     public ListDataModel getMetricsTable() {
        if (metricsTable == null) {
            metricsTable = new ListDataModel(metricService.getAll());
        }
        return metricsTable;
    }

}
