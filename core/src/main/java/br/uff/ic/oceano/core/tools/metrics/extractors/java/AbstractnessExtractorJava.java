/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdepend.framework.*;

/**
 * refactored by dheraclio
 *
 * @author wallace
 *
 */
public class AbstractnessExtractorJava extends AbstractMetricExtractor {

    public AbstractnessExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    /**
     * Require path to .class files.
     *
     * @param revision
     * @param path
     * @return
     * @throws MetricException
     */
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {

        JavaPackage clazzPackage = null;
        File directory = new File(path);
        for (String filePath : directory.list()) {
            File ark = new File(directory, filePath);
            if (!ark.isFile()) {
                continue;
            }

            JavaClass clazz = buildClass(ark);
            if (clazzPackage == null) {
                clazzPackage = new JavaPackage(clazz.getPackageName());
            }
            if (clazzPackage != null) {
                clazzPackage.addClass(clazz);
            }
        }

        if (clazzPackage == null) {
            //No files in path
            return null;
        }
        return createMetricValue(revision, clazzPackage.abstractness());

    }

    private JavaClass buildClass(File file) throws MetricException {
        FileInputStream fis = null;
        try {
            AbstractParser parser = new ClassFileParser(new PackageFilter());
            fis = new FileInputStream(file);
            return parser.parse(fis);
        } catch (IOException ioe) {
            throw new MetricException(ioe);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
            }
        }
    }
}
