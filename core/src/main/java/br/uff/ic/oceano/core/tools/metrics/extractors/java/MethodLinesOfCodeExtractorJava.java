/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import br.uff.ic.oceano.util.file.FileUtils;
import com.jeantessier.classreader.ClassfileLoader;
import com.jeantessier.classreader.ClassfileScanner;
import com.jeantessier.classreader.LoadListenerVisitorAdapter;
import com.jeantessier.classreader.TransientClassfileLoader;
import com.jeantessier.commandline.CommandLine;
import com.jeantessier.commandline.CommandLineUsage;
import com.jeantessier.commandline.NullParameterStrategy;
import com.jeantessier.metrics.Measurement;
import com.jeantessier.metrics.Metrics;
import com.jeantessier.metrics.MetricsConfigurationLoader;
import com.jeantessier.metrics.MetricsFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * refactored by dheraclio
 *
 * @author wallace
 */
public class MethodLinesOfCodeExtractorJava extends AbstractMetricExtractor {

    public MethodLinesOfCodeExtractorJava() {
    }

    public MetricValue extractMetric(Revision configuration) throws MetricException {
        return this.extractMetric(configuration, configuration.getLocalPath());
    }

    /**
     * Esta métrica calcula o numero total de linhas que cada metodo possui. O
     * valor dessa metrica e a soma total das linhas de todos os metodos Quando
     * nao houver um return no final de um metodo ,o java compiler adicionara um
     * return ao final do metodo, o que acarretara no acrescimo de uma linha em
     * cada metodo sem return no final.
     *
     * @param revision
     * @param path
     * @return
     * @throws MetricException -
     */
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        Iterator i;
        Metrics m;
        Measurement medida;
        Number number;
        int num = 0;
        Collection col;
        int sum = 0;

        CommandLine commandLine = new CommandLine(new NullParameterStrategy());
        commandLine.addSingleValueSwitch("default-configuration", true);
        commandLine.addSingleValueSwitch("configuration");
        commandLine.addToggleSwitch("validate");
        commandLine.addToggleSwitch("enable-cross-class-measurements");
        commandLine.addToggleSwitch("help");

        File arquivo = new File("MetricsConfig.xml");
        String caminho = arquivo.getAbsolutePath();

        try {
            FileOutputStream saida = new FileOutputStream(arquivo);

            Class cl = commandLine.getClass();

            InputStream fin = cl.getResourceAsStream("MetricsConfig.xml");
            int n = 0;

            n = fin.read();
            while (n != -1) {
                saida.write(n);
                n = fin.read();
            }
            fin.close();
            saida.close();

        } catch (Exception e) {
            throw new MetricException(e);

        }


        String[] arg = {"-default-configuration", caminho};
        CommandLineUsage usage = new CommandLineUsage("OOMetrics");
        commandLine.accept(usage);

        try {
            commandLine.parse(arg);
        } catch (IllegalArgumentException ex) {
//            System.exit(1);
            throw new MetricException(ex);
        }

        if (commandLine.getToggleSwitch("help")) {
            System.exit(1);
        }
        com.jeantessier.metrics.MetricsFactory factory;
        try {

            if (commandLine.isPresent("configuration")) {
                factory = new com.jeantessier.metrics.MetricsFactory("Project", new MetricsConfigurationLoader(commandLine.getToggleSwitch("validate")).load(commandLine.getSingleSwitch("configuration")));
            } else {
                factory = new com.jeantessier.metrics.MetricsFactory("Project", new MetricsConfigurationLoader(commandLine.getToggleSwitch("validate")).load(commandLine.getSingleSwitch("default-configuration")));
            }

            File ark = new File(path);
            Collection filenames = FileUtils.getAllFilesInFolderAndSubFolders(ark, ".class");

            ClassfileScanner scanner = new ClassfileScanner();
            scanner.load(filenames);

            com.jeantessier.metrics.MetricsGatherer gatherer = new com.jeantessier.metrics.MetricsGatherer(factory);

            ClassfileLoader loader = new TransientClassfileLoader();
            loader.addLoadListener(new LoadListenerVisitorAdapter(gatherer));
            loader.load(filenames);


            col = factory.getMethodMetrics();
            i = col.iterator();

            while (i.hasNext()) {
                m = (Metrics) i.next();
                medida = m.getMeasurement("SLOC");
                number = medida.getValue();
                num = num + number.intValue();                
            }
        } catch (Exception e) {
            throw new MetricException(e);
        }

        sum += num;
        arquivo.delete();

        return createMetricValue(revision, sum);
    }
}
