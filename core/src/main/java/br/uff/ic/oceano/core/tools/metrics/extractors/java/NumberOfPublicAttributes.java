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
 *
 * @author heron
 */
public class NumberOfPublicAttributes extends AbstractMetricExtractor{

    public NumberOfPublicAttributes()
    {
        
    }
    
    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try {
            final InputStream configStream = this.getClass().getResourceAsStream("MetricsConfig.xml");
            MetricsConfigurationLoader configLoader = new MetricsConfigurationLoader(true);
            MetricsConfiguration configuration = configLoader.load(configStream);
            MetricsFactory factory = new MetricsFactory("Project", configuration);
            
            com.jeantessier.metrics.MetricsGatherer gatherer = new com.jeantessier.metrics.MetricsGatherer(factory);
            ClassfileLoader loader = new TransientClassfileLoader();
            loader.addLoadListener(new LoadListenerVisitorAdapter(gatherer));
            
            //Collection<String> filenames = FileUtils.getAllFilesInFolderAndSubFolders(new File(path), ".class");
            Collection<String> filenames = new LinkedList<String>();
            filenames.add(path);
            loader.load(filenames);

            Collection<Metrics> col = factory.getClassMetrics();
            Iterator<Metrics> it = col.iterator();

            int result = 0;
            final String pum = "PuA";
            while (it.hasNext()) {
                Metrics m = it.next();
                Measurement medida = m.getMeasurement(pum);
                result += medida.getValue().intValue();
            }
            return createMetricValue(revision, result);
        } catch (Exception e) {
            throw new MetricException(e);
        }
    }    
    
}
