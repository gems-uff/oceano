/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import br.uff.ic.oceano.core.tools.revision.JavaRevisionTool;
import br.uff.ic.oceano.core.tools.revision.RevisionUtil;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

/**
 * Refactored by dheraclio
 *
 * @author wallace
 */
public class DirectClassCouplingExtractorJava extends AbstractMetricExtractor {

    private JavaRevisionTool revTool = new JavaRevisionTool();

    public DirectClassCouplingExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    @Override
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try {
            JavaClass jc = new ClassParser(path).parse();
            Set<String> attributenames = new HashSet<String>();

            org.apache.bcel.classfile.Field fields[] = jc.getFields();
            for (int j=0; j < fields.length; j++) {
                attributenames.add(fields[j].getType().toString());
            }
            org.apache.bcel.classfile.Method met[] = jc.getMethods();
            for (int j=0; j < met.length; j++) {
                org.apache.bcel.generic.Type types[] = met[j].getArgumentTypes();
                for (int l=0; l < types.length; l++){
                    String attributename = types[l].toString();
                    if (attributename.endsWith("[]")) {
                        attributename = attributename.substring(0, (attributename.length()) - 2);
                    }
                    attributenames.add(attributename);
                }
            }

            Set<String> classnames = new HashSet<String>(revTool.getCompiledClassNames(revision));
            Set<String> attributeclasses = new HashSet<String>();
            for (Iterator<String> it = attributenames.iterator(); it.hasNext();) {
                String nameclass = it.next();
                if (classnames.contains(nameclass)) {
                    attributeclasses.add(nameclass);
                }
            }
            return createMetricValue(revision, attributeclasses.size());
        } catch (Exception e) {
            throw new MetricException(e);
        }
    }

}
