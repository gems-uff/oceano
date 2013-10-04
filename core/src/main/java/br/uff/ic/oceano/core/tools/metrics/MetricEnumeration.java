package br.uff.ic.oceano.core.tools.metrics;

import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.tools.metrics.expression.QMOOD;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public enum MetricEnumeration {

    ANA(
    QMOOD.METRIC_AVERAGE_NUMBER_OF_ANCESTORS,
    "ANA",
    "Indica o número médio de classes que cada classe do projeto herda informações.",
    Metric.TYPE_FLOAT,
    Metric.EXTRACTS_FROM_PROJECT,
    false),
    RMA(
    "Abstractness",
    "RMA",
    "This metric extract the Abstractness of a given configuration.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_PACKAGE,
    false),
    CIS(
    QMOOD.METRIC_CLASS_INTERFACE_SIZE,
    "CIS",
    "Indica o número de métodos públicos em uma classe.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    CAM(
    QMOOD.METRIC_COHESION_AMONG_METHODS_IN_CLASS,
    "CAM",
    "Indicates the cohesion among methods in class.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    ACC(
    "Cyclomatic Complexity",
    "ACC",
    "This metric returns the Cyclomatic Complexity Number.",
    Metric.TYPE_FLOAT,
    Metric.EXTRACTS_FROM_FILE,
    true),
    DAM(
    QMOOD.METRIC_DATA_ACCESS,
    "DAM",
    "Indica a razão entre os atributos privados (e protegidos) e o número total de atributos.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    DSC(
    QMOOD.METRIC_DESIGN_SIZE_IN_CLASSES,
    "DSC",
    "Indica o número de classes de um projeto.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_PROJECT,
    false),
    DCC(
    QMOOD.METRIC_DIRECT_CLASS_COUPLING,
    "DCC",
    "Indica o número de classes diferentes com que uma classe se relaciona.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    LCOM(
    "Lack Of Cohesion Of Methods",
    "LCOM",
    "This metric returns the Lack Of Cohesion Of Methods.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    LOC(
    "Lines Of Code",
    "LOC",
    "This metric returns the Lines Of Code of a file.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    true),
    MOA(
    QMOOD.METRIC_MEASURE_OF_AGGREGATION,
    "MOA",
    "Indica o número de declarações de dados, cujos dados são definidos pelo usuário.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_PROJECT,
    false),
    MFA(
    QMOOD.METRIC_MEASURE_OF_FUNCTIONAL_ABSTRACTION,
    "MFA",
    "Indica a razão entre os métodos herdados e todos os métodos acessíveis de uma classe.",
    Metric.TYPE_FLOAT,
    Metric.EXTRACTS_FROM_PROJECT,
    false),
    MLOC(
    "Method Lines Of Code",
    "MLOC",
    "This metric returns the Method Lines Of Code.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    NOA(
    "Number Of Attributes",
    "NOA",
    "This metric returns the Number of Attributes.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    NOH(
    QMOOD.METRIC_NUMBER_OF_HIERARCHIES,
    "NOH",
    "This metric extract the number of hierarchies of a project.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_PROJECT,
    false),
    NOI(
    "Number of Interfaces",
    "NOI",
    "This metric extract the number of interfaces of a given configuration.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_PACKAGE,
    true),
    NOM(
    QMOOD.METRIC_NUMBER_OF_METHODS,
    "NOM",
    "This metric extract the number of methods of a class.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    NORM(
    "Number of Overridden Methods",
    "NORM",
    "This metric returns the number of Overriden Methods.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_PROJECT,
    false),
    NOP(
    QMOOD.METRIC_NUMBER_OF_POLYMORPHIC_METHODS,
    "NOP",
    "This metric returns the number of polymorphic methods.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_PROJECT,
    false),
    NSF(
    "Number Of Static Attributes",
    "NSF",
    "Indica o numero de atributos estáticos.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    NSM(
    "Number Of Static Methods",
    "NSM",
    "This metric returns the Number Of Static Methods.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    false),
    TCC(
    "Total Cyclomatic Complexity",
    "TCC",
    "This metric returns the Weighted Methods Per Class (Sum of the McCabe Cyclomatic Complexity for all methods in a class).",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    true),
    NPA(
    "Number of Public Attributes",
    "NPA",
    "This metric returns the Number of Public Attributes.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    true),
    NOAM(
    "Number of Accessor Methods",
    "NOAM",
    "This metric returns the Number of Accessor Methods.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_FILE,
    true),
    TLOC(
    "Lines of Code Total",
    "TLOC",
    "This metric returns the Lines of Code of a project.",
    Metric.TYPE_INTEGER,
    Metric.EXTRACTS_FROM_PROJECT,
    true),;

    public static MetricEnumeration getMetricByName(String metricName) {
        for (MetricEnumeration metric : MetricEnumeration.values()) {
            if (metric.getName().compareTo(metricName) == 0) {
                return metric;
            }
        }
        return null;
    }

    private Metric metric;

    private MetricEnumeration(String name, String acronym, String description, int resultType, int extractsFrom, boolean extractsFromFont) {
        this.metric = new Metric();
        metric.setName(name);
        metric.setAcronym(acronym);
        metric.setDescription(description);
        metric.setType(resultType);
        metric.setExtratcsFrom(extractsFrom);
        metric.setExtractsFromFont(extractsFromFont);
        metric.setPreRelease(true);
    }

    /**
     * @return the name
     */
    public String getName() {
        return metric.getName();
    }

    /**
     * 
     * @return 
     */
    public String getAcronym() {
        return this.metric.getAcronym();
    }

    /**
     * 
     * @return 
     */
    public String getDescription() {
        return this.metric.getDescription();
    }
    
    /**
     * 
     * @param otherMetric
     * @return 
     */
    public boolean same(Metric otherMetric) {
        return this.metric.equals(otherMetric);
    }
    
    public static List<Metric> getMetrics() {
        List<Metric> metrics = new LinkedList<Metric>();
        for (MetricEnumeration meEnum : MetricEnumeration.values()) {
            metrics.add(meEnum.metric);
        }
        return metrics;
    }

    
}
