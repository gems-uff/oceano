/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import csdl.locc.measures.java.parser.javacc.CompilationUnit;
import csdl.locc.measures.java.parser.javacc.JavaParser;
import csdl.locc.measures.java.parser.javacc.ParseException;
import csdl.locc.measures.java.parser.javacc.TokenMgrError;
import csdl.locc.sys.DirTree;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 * refactored by dheraclio
 *
 * @author wallace
 */
public class NumberOfInterfacesExtractorJava extends AbstractMetricExtractor {

    public NumberOfInterfacesExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {        
        try {
            String fileName = "";
            int numInterfaces = 0;
            DirTree dirTree = new DirTree(path, ".java");
            for (Iterator j = dirTree.getFileList().iterator(); j.hasNext();) {
                fileName = (String) j.next();
                JavaParser p = new JavaParser(new FileInputStream(fileName));
                CompilationUnit top = p.TopLevel();
                numInterfaces += top.getNumInterfaces();
            }
            return createMetricValue(revision, numInterfaces);
        } catch (Exception ex) {
            throw new MetricException(ex);
        } 
        
    }
}
