package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util;

import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.DependometerException;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.util.Output;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 *
 * @author Daniel
 */
public class DependomenterDocumentReader {

    public static Double getMetric(Metric metric, String path, Document document) throws DependometerException {
        if(document == null){
            throw new DependometerException("Null document");
        }        
        if (path == null) {
            throw new DependometerException("Null path");
        }
        if (metric == null) {
            throw new DependometerException("Null metric");
        }
        Element root = document.getRootElement();
        String metricName = MetricHelper.getMetricName(metric);
        if (metric.getExtratcsFrom() == Metric.EXTRACTS_FROM_FILE) {
            return getFileMetric(root, metricName, path);
        } else if (metric.getExtratcsFrom() == Metric.EXTRACTS_FROM_PACKAGE) {
            return getPackageMetric(root, metricName, path);
        } else if (metric.getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT) {
            return getProjectMetric(root, metricName, path);
        } else {
            throw new DependometerException("Unsupported metric source: " + metric.getExtratcsFrom());
        }
    }

    public static List<Double> getCompilationUnitValues(MetricEnum metric, Document doc) throws DependometerException {
        if (metric == null) {
            throw new DependometerException("Null metric");
        }
        if (doc == null) {
            throw new DependometerException("Null document");
        }

        Element root = doc.getRootElement();
        root = root.getChild(DependomenterListener.XML_COMPILATIONUNITS);
        if (root == null) {
            throw new DependometerException("No files found");
        }
        List files = root.getChildren();
        if (files == null || files.isEmpty()) {
            throw new DependometerException("No compilation units found");
        }

        List<Double> values = new LinkedList<Double>();
        for (Iterator it = files.iterator(); it.hasNext();) {
            Element file = (Element) it.next();
            String metricName = MetricHelper.getMetricName(metric);
            Element metricElem = file.getChild(metricName);
            if (metricElem == null) {
                continue;
            }

            Double value = parseDouble(metricElem.getValue());
            if (value == null) {
                continue;
            }
            values.add(value);
        }
        return values;
    }

    public static List<Double> getTypeValues(MetricEnum metric, Document doc) throws DependometerException {
        if (metric == null) {
            throw new DependometerException("Null metric");
        }
        if (doc == null) {
            throw new DependometerException("Null document");
        }

        Element root = doc.getRootElement();
        root = root.getChild(DependomenterListener.XML_TYPES);
        if (root == null) {
            throw new DependometerException("No types root found");
        }
        List types = root.getChildren();
        if (types == null || types.isEmpty()) {
            throw new DependometerException("No types in type root found");
        }

        List<Double> values = new LinkedList<Double>();
        for (Iterator it = types.iterator(); it.hasNext();) {
            Element type = (Element) it.next();
            String metricName = MetricHelper.getMetricName(metric);
            Element metricElem = type.getChild(metricName);
            if (metricElem == null) {
                continue;
            }

            Double value = parseDouble(metricElem.getValue());
            if (value == null) {
                continue;
            }
            values.add(value);
        }
        return values;
    }

    private static Double getProjectMetric(Element root, String metricName, String path) throws DependometerException {
        root = root.getChild(DependomenterListener.XML_PROJECT);
        if (root == null) {
            throw new DependometerException("No project metrics found");
        }
        List metrics = root.getChildren();
        if (metrics == null || metrics.isEmpty()) {
            throw new DependometerException("No metrics found");
        }

        Iterator it = metrics.iterator();
        while (it.hasNext()) {
            Element metric = (Element) it.next();
            if (metric.getName().compareTo(metricName) != 0) {
                continue;
            }
            return parseDouble(metric.getValue());
        }
        return null;
    }

    private static Double getPackageMetric(Element root, String metricName, String pakageName) throws DependometerException {
        root = root.getChild(DependomenterListener.XML_PACKAGES);
        if (root == null) {
            throw new DependometerException("No package metrics found");
        }
        List packages = root.getChildren();
        if (packages == null || packages.isEmpty()) {
            throw new DependometerException("No packages found");
        }

        Iterator it = packages.iterator();
        while (it.hasNext()) {
            Element pakage = (Element) it.next();
            if (!pakage.getName().contains(pakageName)) {
                continue;
            }
            Element metric = pakage.getChild(metricName);
            if (metric == null) {
                continue;
            }
            return parseDouble(metric.getValue());
        }
        return null;
    }

    private static Double getFileMetric(Element root, String metricName, String path) throws DependometerException {
        try {
            root = root.getChild(DependomenterListener.XML_COMPILATIONUNITS);
            if (root == null) {
                throw new DependometerException("No metrics for files found");
            }
            List files = root.getChildren();
            if (files == null || files.isEmpty()) {
                throw new DependometerException("No files found");
            }

            Iterator it = files.iterator();
            while (it.hasNext()) {

                Element file = (Element) it.next();
                if (file == null) {
                    continue;
                }
                Attribute attName = file.getAttribute(DependomenterListener.XML_NAME);
                if (attName == null) {
                    continue;
                }
                String attNameValue = attName.getValue();
                if (attNameValue == null) {
                    continue;
                }

                if (!path.contains(attNameValue)) {
                    continue;
                }
                Element metric = file.getChild(metricName);
                if (metric == null) {
                    continue;
                }
                return parseDouble(metric.getValue());

            }
        } catch (Exception ex) {
            Output.println(ex.getMessage());
            ex.printStackTrace();            
        } catch (Throwable tw) {
            Output.println(tw.getMessage());
            tw.printStackTrace();
        }
        return null;
    }

    private static Double parseDouble(String value) {
        final String invalidResult = "! not analyzed !";

        if (value.compareTo(invalidResult) == 0) {
            return null;
        }

        Double result;
        try {
            result = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            result = null;
        }

        if (NumberUtil.isNAN(result)) {
            return null;
        }

        return result;
    }
}
