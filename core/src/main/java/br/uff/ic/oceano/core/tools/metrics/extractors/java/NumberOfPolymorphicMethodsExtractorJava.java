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
import java.util.*;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

/**
 * refactored by dheraclio
 *
 * @author wallace
 */
public class NumberOfPolymorphicMethodsExtractorJava extends AbstractMetricExtractor {

    public NumberOfPolymorphicMethodsExtractorJava() {
    }

    public MetricValue extractMetric(Revision configuration) throws MetricException {
        return this.extractMetric(configuration, configuration.getLocalPath());
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        Iterator i;
        ClassParser cp1, cp2;
        JavaClass jc1, jc2;
        org.apache.bcel.classfile.Method met1[];
        org.apache.bcel.classfile.Method met2[];

        String pathclasses = revision.getLocalPath();
        if (!revision.getLocalPath().endsWith(SystemUtil.FILESEPARATOR)) {
            pathclasses = pathclasses.concat(SystemUtil.FILESEPARATOR);
        }
        pathclasses = pathclasses.concat(MavenUtil.MAVEN2_BASE_COMPILED_FILES);

        File ark = new File(pathclasses);
        Collection<String> filenames = FileUtils.getAllFilesInFolderAndSubFolders(ark, ".class");

        i = filenames.iterator();
        Map<String, String> hash = new HashMap<String, String>();
        HashSet polymorphic = new HashSet();
        HashSet map = new HashSet();
        String caminho1, caminho2, mn1, mn2;
        String polymethods[];

        while (i.hasNext()) {
            caminho1 = (String) i.next();
            try {
                cp1 = new ClassParser(caminho1);
                jc1 = cp1.parse();
                hash.put(jc1.getClassName(), caminho1);
            } catch (Exception e) {
                throw new MetricException(e);
            }
        }


        i = filenames.iterator();
        while (i.hasNext()) {
            caminho1 = (String) i.next();
            try {
                cp1 = new ClassParser(caminho1);
                jc1 = cp1.parse();
                met1 = jc1.getMethods();
                polymethods = new String[met1.length];
                map.clear();
                caminho2 = (String) hash.get(jc1.getSuperclassName());

                while (caminho2 != null) {
                    cp2 = new ClassParser(caminho2);
                    jc2 = cp2.parse();
                    met2 = jc2.getMethods();
                    for (int j = 0; j < met1.length; j++) {
                        mn1 = met1[j].getName();

                        for (int z = 0; z < met2.length; z++) {
                            mn2 = met2[z].getName();

                            if (mn1.equals(mn2)) {
                                map.add(j);

                                String namemethod = jc2.getClassName();
                                namemethod = namemethod.concat(".");
                                namemethod = namemethod.concat(mn2);

                                polymethods[j] = namemethod;
                            }
                        }

                    }
                    caminho2 = (String) hash.get(jc2.getSuperclassName());
                }

                int y = 0;
                while (y < polymethods.length) {
                    if (map.contains(y)) {
                        if (!polymorphic.contains(polymethods[y])) {
                            //System.out.println(polymethods[y]);
                            polymorphic.add(polymethods[y]);
                        }
                    }
                    y++;
                }

            } catch (Exception e) {
                throw new MetricException(e);
            }
        }

        return createMetricValue(revision, polymorphic.size());

    }
}
