/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.cpp;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util.ConfigurationHelper;
import br.uff.ic.oceano.core.tools.revision.RevisionUtil;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.jdom.Element;

/**
 *
 * @author daniel heráclio
 */
public class NeoPZHelper {

    public static boolean isNeoPZRevision(Revision revision) {
        String url = revision.getProject().getRepositoryUrl();
        return (url != null && url.contains("neopz"));
    }

    public static void addInputFilters(Revision revision, Element config) throws Exception {

        ConfigurationHelper.addComment(config, "COMPILATION UNIT FILTERS");
        //ignore doxygen config files
        ConfigurationHelper.addExcludeCompilationUnit(config, "dox*.h");

    }

    public static void addLogicalArchitecture(Revision revision, Element config) throws Exception {

        ConfigurationHelper.addComment(config, "LOGICAL ARCHITECTURE");

        //Path at current revision
        Set<String> paths = RevisionUtil.get().getSourceClassPaths(revision);
        if (paths == null || paths.isEmpty()) {
            //empty revision
            return;
        }

        //modules determined from doxygen

        //Analysis classes
        String moduleName = "Analysis classes";
        String sourcePath = "Analysis";
        if (hasSourcePath(sourcePath, paths)) {
            addModule(moduleName, sourcePath, config);
        }

        //Common classes
        moduleName = "Common classes";
        sourcePath = "Common";
        if (hasSourcePath(sourcePath, paths)) {
            addModule(moduleName, sourcePath, config);
        }

        //Geometric approximation classes
        moduleName = "Geometric approximation classes";
        sourcePath = "Geom";
        if (hasSourcePath(sourcePath, paths)) {
            addModule(moduleName, sourcePath, config);
        }

        //Numerical Integration classes
        moduleName = "Numerical Integration classes";
        sourcePath = "Integral";
        if (hasSourcePath(sourcePath, paths)) {
            addModule(moduleName, sourcePath, config);
        }

        //Matrix classes        
        sourcePath = "Matrix";
        if (hasSourcePath(sourcePath, paths)) {
            //Matrix classes
            moduleName = "Matrix classes";
            String packageName = sourcePath;
            Element layerEl = ConfigurationHelper.addLayer(config, moduleName);
            Element subsystem = ConfigurationHelper.addSubsystem(layerEl, packageName);
            ConfigurationHelper.addIncludePackageFilter(subsystem, packageName);

            //Matrix utility classes                
            //only marked as doxygen anotations. 
        }

        //Solver classes
        //need file filter and more, since does not have namespace in code
        //its classes are scatered around the code
        moduleName = "Solver classes";
        sourcePath = "LinearSolvers"; //only directory identified
        if (hasSourcePath(sourcePath, paths)) {
            addModule(moduleName, sourcePath, config);
        }

        //Frontal Matrix classes
        moduleName = "Frontal Matrix classes";
        sourcePath = "Frontal";
        if (hasSourcePath(sourcePath, paths)) {
            addModule(moduleName, sourcePath, config);
        }

        //Generation of an approximation space
        //  Computational Mesh classes
        //  Computational Element classes
        // mixed because of lack of namespace for diferenciat 
        sourcePath = "Mesh";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Generation of an approximation space";
            Element layerEl = ConfigurationHelper.addLayer(config, moduleName);
            String packageName = "Computational Mesh and Element classes";
            Element subsystem = ConfigurationHelper.addSubsystem(layerEl, packageName);
            ConfigurationHelper.addIncludePackageFilter(subsystem, sourcePath);
        }

        //Post processing classes
        sourcePath = "Post";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Post processing classes";
            addModule(moduleName, sourcePath, config);
        }

        //Getting Mesh classes
        sourcePath = "Pre";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Getting Mesh classes";
            addModule(moduleName, sourcePath, config);
        }

        //Application projects 
        sourcePath = "Projects";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Application projects";
            addModule(moduleName, sourcePath, config);
        }

        //Tutorial projects
        //no files located

        //Unit test projects for validation
        sourcePath = "UnitTest_PZ";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Unit test projects for validation";
            addModule(moduleName, sourcePath, config);
        }

        //Refine classes
        sourcePath = "Refine";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Refine classes";
            addModule(moduleName, sourcePath, config);
        }

        //Classes supporting persistency
        sourcePath = "Save";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Classes supporting persistency";
            addModule(moduleName, sourcePath, config);
        }

        //Shape functions classes
        sourcePath = "Shape";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Shape functions classes";
            addModule(moduleName, sourcePath, config);
        }

        //Structural matrix classes
        sourcePath = "StrMatrix";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Structural matrix classes";
            addModule(moduleName, sourcePath, config);
        }

        //Matrix divided in sub structure classes
        sourcePath = "SubStruct";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Matrix divided in sub structure classes";
            addModule(moduleName, sourcePath, config);
        }

        //Master element topology classes
        sourcePath = "Topology";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Master element topology classes";
            addModule(moduleName, sourcePath, config);

            //  Utilities for topology classes
            //no namespace for processing
        }

        //Utility classes
        sourcePath = "Util";
        if (hasSourcePath(sourcePath, paths)) {
            moduleName = "Utility classes";
            addModule(moduleName, sourcePath, config);
        }

        //Tutorials
        //no namespace or directory found. classes are mixed in project.

    }

    private static boolean hasSourcePath(final String path, Set<String> paths) throws Exception {
        Object result = CollectionUtils.find(paths, new Predicate() {
            public boolean evaluate(Object object) {
                String value = (String) object;
                //ignore case using always lower.
                return value.toLowerCase().contains(path.toLowerCase());
            }
        });

        return result != null;
    }

    private static Element addModule(String moduleName, String packageName, Element config) {
        return addModule(moduleName, packageName, packageName, config);
    }

    /**
     *
     * @param moduleName
     * @param subsystemName
     * @param packageName
     * @param config
     */
    private static Element addModule(String moduleName, String subsystemName, String packageName, Element config) {
        Element layerEl = ConfigurationHelper.addLayer(config, moduleName);
        //use package name as subsystem name
        Element subsystemEl = ConfigurationHelper.addSubsystem(layerEl, subsystemName);
        ConfigurationHelper.addIncludePackageFilter(subsystemEl, packageName);

        return layerEl;
    }
}
