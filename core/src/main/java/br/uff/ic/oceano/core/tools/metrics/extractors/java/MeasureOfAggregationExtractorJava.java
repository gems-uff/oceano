/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import br.uff.ic.oceano.core.tools.maven.MavenUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.util.SystemUtil;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

/**
 * refactored by dheraclio
 *
 * @author wallace
 */
public class MeasureOfAggregationExtractorJava extends AbstractMetricExtractor {

    public MeasureOfAggregationExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        Iterator i;
        ClassParser cp;
        JavaClass jc;
        int count = 0;

        String classpath = revision.getLocalPath();
        if (!classpath.endsWith(SystemUtil.FILESEPARATOR)) {
            classpath = classpath.concat(SystemUtil.FILESEPARATOR);
        }
        
        classpath = classpath.concat(MavenUtil.MAVEN2_BASE_COMPILED_FILES);

        File ark = new File(classpath);
        Collection filenames = FileUtils.getAllFilesInFolderAndSubFolders(ark, ".class");

        HashSet classnames = new HashSet();
        Collection attributenames;
        //Collection attributeclasses=new LinkedList();
        i = filenames.iterator();
        while (i.hasNext()) {
            String caminho = (String) i.next();
            try {
                cp = new ClassParser(caminho);
                jc = cp.parse();
                classnames.add(jc.getClassName());
            } catch (Exception e) {
            }
        }

        String filename;
        Iterator it = filenames.iterator();
        while (it.hasNext()) {
            filename = (String) it.next();
            //System.out.println(filename);
            try {
                cp = new ClassParser(filename);
                jc = cp.parse();

                org.apache.bcel.classfile.Field fields[];
                fields = jc.getFields();
                int j = 0;
                //attributeclasses=new LinkedList();
                attributenames = new LinkedList();
                while (j < fields.length) {
                    org.apache.bcel.generic.Type type;
                    type = fields[j].getType();
                    attributenames.add(type.toString());
                    j++;
                }

                i = attributenames.iterator();
                while (i.hasNext()) {
                    String nameclass = (String) i.next();
                    //System.out.println(nameclass);
                    if (classnames.contains(nameclass)) {
                        //System.out.println(nameclass);
                        //attributeclasses.add(nameclass);
                        count++;
                    }
                }


            } catch (Exception e) {
                throw new MetricException(e);
            }

            /*
             * i=attributeclasses.iterator(); System.out.println("\nclasses");
             * while(i.hasNext()) { System.out.println((String) i.next()); }
             * System.out.println("Numero data declarations: "+attributeclasses.size());
             */
        }

        return createMetricValue(revision, count);
    }
}
