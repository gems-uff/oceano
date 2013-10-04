/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics;

import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.Tool;

/**
 *
 *
 */
public abstract class MetricManager implements Tool{

    private Metric metric;

    public abstract MetricValue extractMetric(Revision revision) throws MetricException;

    public abstract MetricValue extractMetric(Revision revision, String path) throws MetricException;

    public abstract boolean isLanguageSupported(Language lang);

    public MetricManager(Metric metric) {
        this.metric = metric;
    }

    public Metric getMetric(){
        return this.metric;
    }

    public void setMetric(Metric metric){
        this.metric = metric;
    }
    
    /**
     * Return Metric name
     * @see Tool
     */
    public String getName() {
        return getMetric().getName();
    }

    /**
     * @see Tool
     */
    public String getRationale() {
        return getMetric().getDescription();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        return ((MetricManager) obj).getMetric().equals(this.getMetric());
    }

    @Override
    public int hashCode() {
        return 13 + getMetric().hashCode();
    }

    @Override
    public String toString() {
        return getMetric().getName();
    }
}
