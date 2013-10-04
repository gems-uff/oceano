package br.uff.ic.oceano.controller;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Daniel Castellani
 *
 * Revision by DHeraclio
 *  getAutoExecutableImage always return same image, all metrics are auto executable
 */
public class MetricBean extends BaseBean {

    final static String vImage = "/recurso/img/end.png";
    final static String xImage = "/recurso/img/ico_excluir.png";
    //Pages
    private String PAGINA_LIST_METRICS = "def:/privado/oceano/metric/list";
    //Control
    private ListDataModel metricsTable;
    //Services
    private MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);

    public MetricBean() {
        super("MetricBean");
        sessao.setPerfilOceano();
    }

    public String returnListMetrics() {
        return PAGINA_LIST_METRICS;
    }

    public String getAutoExecutableImage() {
            return vImage;
    }

    public String getDerivedMetricImage() {
        Metric rowMetric = (Metric) metricsTable.getRowData();
        if (rowMetric.isDerived()) {
            return vImage;
        } else {
            return xImage;
        }
    }

    public String getMustCompileImage() {
        Metric rowMetric = (Metric) metricsTable.getRowData();
        if (rowMetric.isExtractsFromFont()) {
            return xImage;
        } else {
            return vImage;
        }
    }

    /**
     * @return the metricsTable
     */
    public ListDataModel getMetricsTable() {
        if (metricsTable == null) {
            metricsTable = new ListDataModel(metricService.getAll());
        }
        return metricsTable;
    }

    /**
     * @param metricsTable the metricsTable to set
     */
    public void setMetricsTable(ListDataModel metricsTable) {
        this.metricsTable = metricsTable;
    }
}
