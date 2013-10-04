/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import com.jeantessier.classreader.ClassfileLoader;
import com.jeantessier.classreader.LoadListenerVisitorAdapter;
import com.jeantessier.classreader.TransientClassfileLoader;
import com.jeantessier.metrics.Measurement;
import com.jeantessier.metrics.Metrics;
import com.jeantessier.metrics.MetricsConfiguration;
import com.jeantessier.metrics.MetricsConfigurationLoader;
import com.jeantessier.metrics.MetricsFactory;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * refactored by dhreaclio
 *
 * @author wallace
 */
public class NumberOfAttributesExtractorJava extends AbstractMetricExtractor {

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }
    
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try {
            final InputStream configStream = this.getClass().getResourceAsStream("MetricsConfig.xml");
            MetricsConfigurationLoader configLoader = new MetricsConfigurationLoader(false);
            MetricsConfiguration configuration = configLoader.load(configStream);
            MetricsFactory factory = new MetricsFactory("Project", configuration);

            com.jeantessier.metrics.MetricsGatherer gatherer = new com.jeantessier.metrics.MetricsGatherer(factory);
            ClassfileLoader loader = new TransientClassfileLoader();
            loader.addLoadListener(new LoadListenerVisitorAdapter(gatherer));

            Collection<String> filenames = new LinkedList<String>();
            filenames.add(path);
            loader.load(filenames);

            Collection<Metrics> col = factory.getProjectMetrics();
            Iterator<Metrics> it = col.iterator();

            int result = 0;
            final String measure = "A";
            while (it.hasNext()) {
                Metrics m = it.next();
                Measurement measureResult = m.getMeasurement(measure);
                result += measureResult.getValue().intValue();
            }
            return createMetricValue(revision, result);
        } catch (Exception e) {
            throw new MetricException(e);
        }
    }
    
//    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
//        Iterator i;
//        Metrics m;
//        Measurement medida;
//        Number number;
//        int num = 0;
//        Collection col;
//        CommandLine commandLine = new CommandLine(new NullParameterStrategy());
//        commandLine.addSingleValueSwitch("default-configuration", true);
//        commandLine.addSingleValueSwitch("configuration");
//        commandLine.addToggleSwitch("validate");
//        commandLine.addToggleSwitch("enable-cross-class-measurements");
//        commandLine.addToggleSwitch("help");
//
//        File arquivo = new File("MetricsConfig.xml");
//        String caminho = arquivo.getAbsolutePath();
//
//        try {
//
//            FileOutputStream saida = new FileOutputStream(arquivo);
//
//            Class cl = commandLine.getClass();
//
//            InputStream fin = cl.getResourceAsStream("MetricsConfig.xml");
//            int n = 0;
//
//            n = fin.read();
//            while (n != -1) {
//                saida.write(n);
//                n = fin.read();
//            }
//            fin.close();
//            saida.close();
//
//        } catch (Exception e) {
//            throw new MetricException(e);
//        }
//
//
//        String[] arg = {"-default-configuration", caminho};
//        CommandLineUsage usage = new CommandLineUsage("OOMetrics");
//        commandLine.accept(usage);
//
//        try {
//            commandLine.parse(arg);
//        } catch (IllegalArgumentException ex) {
////            System.exit(1);
//            throw new MetricException(ex);
//        }
//
////        if (commandLine.getToggleSwitch("help")) {
////            System.exit(1);
////        }
//        MetricsFactory factory;
//
//        try {
//
//            if (commandLine.isPresent("configuration")) {
//                factory = new MetricsFactory("Project", new MetricsConfigurationLoader(commandLine.getToggleSwitch("validate")).load(commandLine.getSingleSwitch("configuration")));
//            } else {
//                factory = new MetricsFactory("Project", new MetricsConfigurationLoader(commandLine.getToggleSwitch("validate")).load(commandLine.getSingleSwitch("default-configuration")));
//            }
//
//            File ark = new File(path);
//            Collection filenames = FileUtils.getAllFilesInFolderAndSubFolders(ark, ".class");
//
//            ClassfileScanner scanner = new ClassfileScanner();
//            scanner.load(filenames);
//
//            com.jeantessier.metrics.MetricsGatherer gatherer = new com.jeantessier.metrics.MetricsGatherer(factory);
//
//            ClassfileLoader loader = new TransientClassfileLoader();
//            loader.addLoadListener(new LoadListenerVisitorAdapter(gatherer));
//            loader.load(filenames);
//
//
//            col = factory.getProjectMetrics();
//            i = col.iterator();
//
//            while (i.hasNext()) {
//                m = (Metrics) i.next();
//                medida = m.getMeasurement("A");
//                number = medida.getValue();
//                num = num + number.intValue();
//            }
//        } catch (Exception e) {
//            throw new MetricException(e);
//        }
//
//        arquivo.delete();
//
//
//        return createMetricValue(revision, num);
//    }
}
