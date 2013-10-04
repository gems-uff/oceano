/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.MetricValueService;
import br.uff.ic.oceano.util.DateUtil;
import br.uff.ic.oceano.ostra.decorator.RevisionMetricValueDto;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XIntervalSeries;
import org.jfree.data.xy.XIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Wallace
 */
public class MetricChartService {

    private static final String DEFAULT_CHART_PATH = "/privado/ostra/chart/temp/";
    private MetricValueService metricValueService = ObjectFactory.getObjectWithDataBaseDependencies(MetricValueService.class);
    private OstraMetricValueService ostraMetricValueService = ObjectFactory.getObjectWithDataBaseDependencies(OstraMetricValueService.class);
    //---- control
    private final boolean USE_RELATIVE_REVISION_NUMBER = false;
    private final boolean USE_RELATIVE_COMMIT_DATE = !USE_RELATIVE_REVISION_NUMBER;
    //----
    //Buffers for charts
    private SoftwareProject softwareProjectBuffer; //marks current data origen
    private Metric metricBuffer; //marks current data origen
    private List<RevisionMetricValueDto> dataValuesBuffer;
    private List<RevisionMetricValueDto> deltaValuesBuffer;
    private List<RevisionMetricValueDto> averageValuesBuffer;

    public ChartValue getChartValue(SoftwareProject softwareProject, Metric metric, int x, int y, boolean isDelta, ServletContext sc) {

        String metricName = metric.getName();
        System.out.println("criando grafico de " + metricName);

        final ChartValue chartValue = new ChartValue();

        List<RevisionMetricValueDto> dataValues = getDataValues(softwareProject, metric);
        List<RevisionMetricValueDto> deltaValues = getDeltaValues(softwareProject, metric);

        final String chartPath = DEFAULT_CHART_PATH + "DynamicChart" + System.currentTimeMillis() + ".png";
        chartValue.setChartPath(chartPath);
        XYSeriesCollection dataSet = createDataSet(dataValues, false);
        final ChartRenderingInfo info = this.createChart(x, y, dataSet, metricName, chartPath, sc);
        final String tag = this.createImageMap(info, dataValues, false, "imgMap");
        chartValue.setTag(tag);

        final String deltaChartPath = DEFAULT_CHART_PATH + "DynamicChart" + System.currentTimeMillis() + ".png";
        XYSeriesCollection deltaSet = createDataSet(deltaValues, true);
        final ChartRenderingInfo deltaInfo = this.createChart(x, y, deltaSet, metricName, deltaChartPath, sc);
        final String deltaTag = this.createImageMap(deltaInfo, deltaValues, true, "deltaimgMap");
        chartValue.setDeltaChart(deltaChartPath);
        chartValue.setDeltaTag(deltaTag);

        if (metric.getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT) {
            chartValue.setProjectMetric(true);
        } else {
            System.out.println("Average Chart");
            List<RevisionMetricValueDto> averageValues = getAverageValue(softwareProject, metric, dataValues);
            final String avgChartPath = DEFAULT_CHART_PATH + "DynamicChart" + System.currentTimeMillis() + ".png";
            XYSeriesCollection avgset = createDataSet(averageValues, false);
            final ChartRenderingInfo avgInfo = this.createChart(x, y, avgset, metricName, avgChartPath, sc);
            final String avgTag = this.createImageMap(avgInfo, averageValues, false, "averageimgMap");
            chartValue.setAverageChart(avgChartPath);
            chartValue.setAverageTag(avgTag);
            chartValue.setProjectMetric(false);
        }

        return chartValue;
    }

    public List<Double> getSoftwareProjectMetricMinMaxValues(SoftwareProject softwareProject, Metric metric) {
        System.out.println("getSoftwareProjectMetricMinMaxValues");
        List<RevisionMetricValueDto> dataValues = getDataValues(softwareProject, metric);
        double maxValue = dataValues.get(0).getAbsoluteMetricValue();
        double minValue = maxValue;
        for (RevisionMetricValueDto mv : dataValues) {
            minValue = getMinValue(mv, minValue);
            maxValue = getMaxValue(mv, maxValue);
        }

        List<Double> minMax = new ArrayList<Double>(2);
        minMax.add(minValue);
        minMax.add(maxValue);
        return minMax;
    }

//    public double getSoftwareProjectMetricMaxValue(SoftwareProject softwareProject, Metric metric) {
//        List<RevisionMetricValueDto> dataValues = getDataValues(softwareProject, metric);
//        double maxValue = dataValues.get(0).getAbsoluteMetricValue();
//        for (RevisionMetricValueDto mv : dataValues) {
//            maxValue = getMaxValue(mv, maxValue);
//        }
//        return maxValue;
//    }
//
//    public double getSoftwareProjectMetricMinValue(SoftwareProject softwareProject, Metric metric) {
//        List<RevisionMetricValueDto> dataValues = getDataValues(softwareProject, metric);
//        double minValue = dataValues.get(0).getAbsoluteMetricValue();
//        for (RevisionMetricValueDto mv : dataValues) {
//            minValue = getMinValue(mv, minValue);
//        }
//        return minValue;
//    }
    private double getMaxValue(RevisionMetricValueDto mv, double currentMaxValue) {
        if (!Double.isNaN(mv.getAbsoluteMetricValue())) {
            if ((mv.getAbsoluteMetricValue() > currentMaxValue) || (Double.isNaN(currentMaxValue))) {
                return mv.getAbsoluteMetricValue();
            }
        }
        return currentMaxValue;
    }

    private double getMinValue(RevisionMetricValueDto mv, double currentMinValue) {
        if (!Double.isNaN(mv.getAbsoluteMetricValue())) {
            if ((mv.getAbsoluteMetricValue() < currentMinValue) || (Double.isNaN(currentMinValue))) {
                return mv.getAbsoluteMetricValue();
            }
        }
        return currentMinValue;
    }

    public ChartValue getHistogramValue(SoftwareProject softwareProject, Metric metric, int numberOfSets, double doubleHistogram[], int x, int y, ServletContext sc) {
        System.out.println("getHistogramValue");
        List<RevisionMetricValueDto> dataValues = getDataValues(softwareProject, metric);

        boolean isDelta = false;
        XIntervalSeriesCollection dataset = createHistogramDataSet(dataValues, numberOfSets, doubleHistogram, isDelta);

        System.out.println("criando histograma");
        String metricName = metric.getName();
        String chartPath = DEFAULT_CHART_PATH + "DynamicHistogram" + System.currentTimeMillis() + ".png";

        ChartRenderingInfo info = this.createHistogram(x, y, dataset, metricName, chartPath, sc);
        String tag = this.createHistogramImageMap(info);
        System.out.println("terminado histograma");
        ChartValue chartValue = new ChartValue();
        chartValue.setTag(tag);
        chartValue.setChartPath(chartPath);

        return chartValue;

    }

    private XIntervalSeriesCollection createHistogramDataSet(List<RevisionMetricValueDto> dataValues, int numberOfSets, double doubleHistogram[], boolean isDelta) {

        XIntervalSeriesCollection dataset = new XIntervalSeriesCollection();
        XIntervalSeries series = new XIntervalSeries("Metric Values");
        int y[] = new int[numberOfSets + 1];
        int i;
        for (i = 0; i < (numberOfSets + 1); i++) {
            y[i] = 0;
        }
        double auxValue;
        for (RevisionMetricValueDto dataValue : dataValues) {
            if (isDelta) {

                auxValue = dataValue.getDeltaMetricValue();
            } else {
                auxValue = dataValue.getAbsoluteMetricValue();
            }
            if (!Double.isNaN(auxValue)) {
                i = this.getIndexOfHistogram(auxValue, numberOfSets, doubleHistogram);
                if (i >= 0) {
                    y[i] = y[i] + 1;
                }
            }

        }
        for (i = 0; i < (numberOfSets); i++) {
            series.add((doubleHistogram[i] + doubleHistogram[i + 1]) / 2, doubleHistogram[i], doubleHistogram[i + 1], y[i]);
        }
        dataset.addSeries(series);

        return dataset;
    }

    private int getIndexOfHistogram(double auxValue, int numberOfSets, double doubleHistogram[]) {
        int i = -1;
        int j = 0;
        if (auxValue == doubleHistogram[numberOfSets]) {
            i = numberOfSets;
        } else {
            while (j < (numberOfSets) && i < 0) {
                if ((auxValue >= doubleHistogram[j]) && (auxValue < doubleHistogram[j + 1])) {
                    i = j;
                }
                j++;
            }
        }
        return i;
    }

    private ChartRenderingInfo createHistogram(int x, int y, XIntervalSeriesCollection dataset, String metricName, String chartPath, ServletContext sc) {
        try {
            JFreeChart chart = ChartFactory.createXYBarChart(metricName + " Histogram", "Value", false, "Number", dataset, PlotOrientation.VERTICAL, false, true, false);
            File chartFile = new File(sc.getRealPath(chartPath));
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            ChartUtilities.saveChartAsPNG(chartFile, chart, x, y, info);



            return info;
        } catch (Exception e) {
            return null;
        }
    }

    private String createHistogramImageMap(ChartRenderingInfo info) {
        return ChartUtilities.getImageMap("imgHistogramMap", info);
    }

    private XYSeriesCollection createDataSet(List<RevisionMetricValueDto> dataValues, boolean isDelta) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries principalSeries = null;
        System.out.println("isDelta = " + isDelta);
        if (isDelta) {
            principalSeries = new XYSeries("Delta Values");
        } else {
            principalSeries = new XYSeries("Absolute Values");
        }

        Number x, y;
        int index = 1;
        Long firstDay = null;
        for (RevisionMetricValueDto dataValue : dataValues) {
            x = Long.parseLong(dataValue.getRevisionNumber());

            if (isDelta) {
                y = dataValue.getDeltaMetricValue();
            } else {
                y = dataValue.getAbsoluteMetricValue();
            }
            if (USE_RELATIVE_REVISION_NUMBER) {
                principalSeries.add(index, y);
            } else if (USE_RELATIVE_COMMIT_DATE) {
                if (firstDay == null) {
                    firstDay = dataValue.getCommitDate().getTimeInMillis();
                }
                long relativeDayOfDevelopment = Math.round(Math.floor((dataValue.getCommitDate().getTimeInMillis() - firstDay + 1) / 60000));
                principalSeries.add(relativeDayOfDevelopment, y);
            } else {
                principalSeries.add(x, y);
            }
            index++;
        }

        if (dataValues.size() > 0) {
            final Number firstRevisionNumber, lastRevisionNumber;
            firstRevisionNumber = principalSeries.getMinX();
            lastRevisionNumber = principalSeries.getMaxX();

            XYSeries averageSeries = new XYSeries("Average");
            double average = calculateAverage(dataValues, isDelta);
            averageSeries.add(firstRevisionNumber, average);
            averageSeries.add(lastRevisionNumber, average);

            XYSeries positiveSDSeries = new XYSeries("Stardard Deviation +");
            XYSeries negativeSDSeries = new XYSeries("Stardard Deviation -");
            double standardDerivation = calculateStandardDerivation(dataValues, average, isDelta);
            positiveSDSeries.add(firstRevisionNumber, average + standardDerivation);
            positiveSDSeries.add(lastRevisionNumber, average + standardDerivation);
            negativeSDSeries.add(firstRevisionNumber, average - standardDerivation);
            negativeSDSeries.add(lastRevisionNumber, average - standardDerivation);

            XYSeries positiveSDSeries3 = new XYSeries("Stardard Deviation + (3)");
            XYSeries negativeSDSeries3 = new XYSeries("Stardard Deviation - (3)");
            positiveSDSeries3.add(firstRevisionNumber, average + (3 * standardDerivation));
            positiveSDSeries3.add(lastRevisionNumber, average + (3 * standardDerivation));
            negativeSDSeries3.add(firstRevisionNumber, average - (3 * standardDerivation));
            negativeSDSeries3.add(lastRevisionNumber, average - (3 * standardDerivation));

            System.out.println("average: " + average);
            System.out.println("stardarddeviation: " + standardDerivation);
            System.out.println("firstRevision: " + firstRevisionNumber);
            System.out.println("lasRevisation: " + lastRevisionNumber);


            dataset.addSeries(principalSeries);
            dataset.addSeries(averageSeries);
            dataset.addSeries(positiveSDSeries);
            dataset.addSeries(negativeSDSeries);
            dataset.addSeries(positiveSDSeries3);
            dataset.addSeries(negativeSDSeries3);
        }

        return dataset;
    }

    private ChartRenderingInfo createChart(int x, int y, XYSeriesCollection dataset, String metricName, String chartPath, ServletContext sc) {
        try {
            final String xAxisLabel = (USE_RELATIVE_COMMIT_DATE ? "Development Time (min)" : "Number of commit");
            JFreeChart chart = ChartFactory.createXYLineChart(metricName, xAxisLabel, "Value", dataset, PlotOrientation.VERTICAL, false, true, false);
            XYPlot plot = chart.getXYPlot();
            plot.getRenderer().setSeriesPaint(0, Color.BLUE);
            plot.getRenderer().setSeriesPaint(1, Color.GREEN);
            plot.getRenderer().setSeriesPaint(2, Color.YELLOW);
            plot.getRenderer().setSeriesPaint(3, Color.YELLOW);
            plot.getRenderer().setSeriesPaint(4, Color.RED);
            plot.getRenderer().setSeriesPaint(5, Color.RED);
            Double minY = null;
            Double maxY = null;
//            System.out.println("minY = " + minY);
//            System.out.println("maxY = " + maxY);
            for (Object object : dataset.getSeries()) {
                XYSeries series = (XYSeries) object;

                if (minY == null || series.getMinY() < minY) {
                    minY = series.getMinY();
//                    System.out.println("minY = " + minY);
                }
                if (maxY == null || series.getMaxY() > maxY) {
//                    System.out.println("maxY = " + maxY);
                    maxY = series.getMaxY();
                }
            }

//            System.out.println("minY = " + minY);
//            System.out.println("maxY = " + maxY);
            if (minY != null && maxY != null) {
                minY -= minY * 0.1;
                maxY += maxY * 0.1;
                plot.getRangeAxis().setRange(minY, maxY);
            }


//            System.out.println("sc.getRealPath() = " + sc.getRealPath(""));

            File chartFile = new File(sc.getRealPath(chartPath));
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            ChartUtilities.saveChartAsPNG(chartFile, chart, x, y, info);
            //chartImage = new DefaultStreamedContent(new FileInputStream(chartFile), "image/png");
//            System.out.println("chartFile.getAbsolutePath() = " + chartFile.getAbsolutePath());

            return info;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    private String createImageMap(ChartRenderingInfo info, List<RevisionMetricValueDto> dataValues, boolean isDelta, String nameMap) {
        EntityCollection newEntityCollection = new StandardEntityCollection();
        EntityCollection entityCollection = info.getEntityCollection();
        Iterator iteratorEntityCollection = entityCollection.iterator();
        String text;
        ChartEntity chartEntity;

        while (iteratorEntityCollection.hasNext()) {
            chartEntity = (ChartEntity) iteratorEntityCollection.next();
            text = chartEntity.getClass().toString();
            if (text.equals("class org.jfree.chart.entity.XYItemEntity")) {

                if (((XYItemEntity) chartEntity).getSeriesIndex() == 0) {

                    RevisionMetricValueDto dataValue = (RevisionMetricValueDto) dataValues.get(((XYItemEntity) chartEntity).getItem());
                    double metricValue = (isDelta ? dataValue.getDeltaMetricValue() : dataValue.getAbsoluteMetricValue());

                    String commitDate = "";
                    if (dataValue.getCommitDate() == null) {
                        System.out.println("dataValue.getCommitDate()  = " + dataValue.getCommitDate() + " revision: " + dataValue.getRevisionNumber() + " : " + dataValue);
                    } else {
                        commitDate = DateUtil.format(dataValue.getCommitDate());
                    }

                    chartEntity.setToolTipText("Revision: " + dataValue.getRevisionNumber() + "  -  Value: " + metricValue + "  -  Commiter: " + dataValue.getCommiter() + "  -  Commit Date: " + commitDate);
                }

            }
            newEntityCollection.add(chartEntity);
        }
        info.setEntityCollection(newEntityCollection);
        String tag = null;
        tag = ChartUtilities.getImageMap(nameMap, info);
        return tag;

    }

    private double calculateAverage(List<RevisionMetricValueDto> dataValues, boolean isDelta) {
        double average = 0;
        double value;
        int count = 0;
        for (RevisionMetricValueDto dataValue : dataValues) {
            //average += (isDelta ? dataValue.getDeltaMetricValue() : dataValue.getAbsoluteMetricValue());
            value = (isDelta ? dataValue.getDeltaMetricValue() : dataValue.getAbsoluteMetricValue());
            if (!Double.isNaN(value)) {
                average += value;
                count++;
            }
            System.out.println("Revisao: " + dataValue.getRevisionNumber() + "   valor: " + (isDelta ? dataValue.getDeltaMetricValue() : dataValue.getAbsoluteMetricValue()));

        }
        if (count != 0) {
            average /= count;
        }
        return average;
    }

    private double calculateStandardDerivation(List<RevisionMetricValueDto> RevisionMetricValueDtos, double average, boolean isDelta) {
        double x;
        double total = 0;
        double standardDerivation;
        int count = 0;
        for (RevisionMetricValueDto dataValue : RevisionMetricValueDtos) {
            x = (isDelta ? dataValue.getDeltaMetricValue() : dataValue.getAbsoluteMetricValue()) - average;
            if (!Double.isNaN(x)) {
                x = x * x;
                total = total + x;
                count++;
            }
        }
        double count2 = count - 1;
        standardDerivation = (1.0 / (count2));
        standardDerivation = standardDerivation * total;
        standardDerivation = Math.sqrt(standardDerivation);
        return standardDerivation;
    }

    private List<RevisionMetricValueDto> getAverageValue(SoftwareProject softwareProject, Metric metric, List<RevisionMetricValueDto> dataValues) {
        System.out.println("getAverageValue");
        if (softwareProject == softwareProjectBuffer && metric == metricBuffer) {
            if ( averageValuesBuffer != null) {
                System.out.println("returning buffer");
                return this.averageValuesBuffer;
            }
        } else {
            resetBuffers(softwareProject, metric);
        }

        //Average is for non project metrics
        if (metric.getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT) {
            return null;
        }
        System.out.println("extracting average values");
        this.averageValuesBuffer = new ArrayList<RevisionMetricValueDto>(dataValues.size());
        for (RevisionMetricValueDto dt : dataValues) {
            final double averageValue;
            if (dt.getCountItems() == null || dt.getCountItems().equals("N/A")) {
                averageValue = Double.NaN;
            } else {
                averageValue = dt.getAbsoluteMetricValue() / Double.parseDouble(dt.getCountItems());
            }
            RevisionMetricValueDto newDataValue = new RevisionMetricValueDto(dt.getRevisionNumber(), dt.getCommiter(), dt.getCountItems(), dt.getCommitDate(), averageValue, 0D);
            this.averageValuesBuffer.add(newDataValue);
        }

        return this.averageValuesBuffer;
    }

    private List<RevisionMetricValueDto> getDeltaValues(SoftwareProject softwareProject, Metric metric) {
        if (softwareProject == softwareProjectBuffer && metric == metricBuffer) {
            if (deltaValuesBuffer != null) {
                System.out.println("returning buffer");
                return this.deltaValuesBuffer;
            }
        } else {
            resetBuffers(softwareProject, metric);
        }
        System.out.println("extracting delta values");
        List<MetricValue> deltaMetricValues = metricValueService.getDeltaValuesByProjectAndMetric(softwareProject, metric);
        deltaValuesBuffer = new ArrayList<RevisionMetricValueDto>(deltaMetricValues.size());
        for (MetricValue metricValue : deltaMetricValues) {
            //System.out.println("delta metricValue = " + metricValue);
            deltaValuesBuffer.add(RevisionMetricValueDto.createFromMetricValue(metricValue));
        }
        //System.out.println("tamanho: " + deltaMetricValues.size());
        return deltaValuesBuffer;
    }

    private List<RevisionMetricValueDto> getDataValues(SoftwareProject softwareProject, Metric metric) {
        System.out.println("getDataValues");
        if (softwareProject == softwareProjectBuffer && metric == metricBuffer) {
            if (dataValuesBuffer != null) {
                System.out.println("returning buffer");
                return this.dataValuesBuffer;
            }
        } else {
            resetBuffers(softwareProject, metric);
        }

        System.out.println("extracting data values");
        if (metric.getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT) {
            List<MetricValue> absoluteMetricValues = metricValueService.getAbsoluteValuesByProjectAndMetric(softwareProject, metric);
            dataValuesBuffer = new ArrayList<RevisionMetricValueDto>(absoluteMetricValues.size());
            for (MetricValue metricValue : absoluteMetricValues) {
                dataValuesBuffer.add(RevisionMetricValueDto.createFromMetricValue(metricValue));
            }
        } else {
            dataValuesBuffer = ostraMetricValueService.getProjectMetricsToDetail(softwareProject, metric.getName());
            for (RevisionMetricValueDto mv : dataValuesBuffer) {
                if (mv.getSumMetricValue().equals("N/A")) {
                    mv.setAbsoluteMetricValue(Double.NaN);
                } else {
                    mv.setAbsoluteMetricValue(Double.parseDouble(mv.getSumMetricValue()));
                }
            }
        }
        return dataValuesBuffer;
    }

    private void resetBuffers(SoftwareProject softwareProject, Metric metric) {
        System.out.println("resetBuffers");
        this.softwareProjectBuffer = softwareProject;
        this.metricBuffer = metric;
        this.averageValuesBuffer = null;
        this.deltaValuesBuffer = null;
        this.dataValuesBuffer = null;
    }
}
