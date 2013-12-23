package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util;

import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.cpp.NeoPZHelper;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.DependometerException;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util.ConfigurationHelper;
import br.uff.ic.oceano.core.tools.metrics.util.XMLUtil;
import br.uff.ic.oceano.core.tools.revision.JavaRevisionTool;
import java.io.File;
import java.io.IOException;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;

/**
 *
 * @author Daniel
 */
public class ConfigurationFactory {

    public static File createConfigFile(Revision revision, String outputPath, String configXmlPath) throws DependometerException {
        final Language language = revision.getProject().getLanguage();
        try {
            if (Language.isJava(language)) {
                return createJavaConfigFile(revision, outputPath, configXmlPath);
            } else if (Language.isCpp(language)) {
                return createCppConfigFile(revision, outputPath, configXmlPath);
            }
        } catch (Exception ex) {
            throw new DependometerException(ex);
        }
        throw new DependometerException("Unsupported language");
    }

    private static File createJavaConfigFile(Revision revision, String outputPath, String configXmlPath) throws DependometerException, Exception {

        Element project = createProject(revision);

        //DIRECTORIES
        ConfigurationHelper.addComment(project, "DIRECTORIES");
        //Source files
        JavaRevisionTool rev = new JavaRevisionTool();
        ConfigurationHelper.addComment(project, "Main source path");
        ConfigurationHelper.addInputDir(project, rev.getMainSourcePath(revision));

        //Compiled files
        ConfigurationHelper.addComment(project, "Compilation paths");
        ConfigurationHelper.addInputDir(project, rev.getMainCompilationFolder(revision));

        //INPUT PACKAGE FILTER
        addInputFilters(project);

        //OUTPUT LISTENER
        addOutputListener(project, outputPath);

        //ASSERTIONS
        ConfigurationHelper.addComment(project, "ASSERTIONS");
        ConfigurationHelper.addAssertionPattern(project, "assert");

        return createConfigFile(project, configXmlPath);
    }

    private static File createCppConfigFile(Revision revision, String outputPath, String configXmlPath) throws DependometerException {
        try {
            Element project = createProject(revision);

            //DIRECTORIES
            ConfigurationHelper.addComment(project, "DIRECTORIES");
            ConfigurationHelper.addInputDir(project, revision.getLocalPath());

            //INPUT FILTERS
            addInputFilters(project);
            if (NeoPZHelper.isNeoPZRevision(revision)) {
                NeoPZHelper.addInputFilters(revision, project);
            }

            //OUTPUT LISTENER
            addOutputListener(project, outputPath);

            //ASSERTIONS
            ConfigurationHelper.addComment(project, "ASSERTIONS");
            ConfigurationHelper.addAssertionPattern(project, "ASSERT");

            ConfigurationHelper.addThresholds(project);
            
            //Logical layers            
            if (NeoPZHelper.isNeoPZRevision(revision)) {
                NeoPZHelper.addLogicalArchitecture(revision, project);
            }

            return createConfigFile(project, configXmlPath);
        } catch (Exception ex) {
            throw new DependometerException(ex);
        }
    }

    private static File createConfigFile(Element project, String configXmlPath) throws IOException {
        //Create xml
        final String dtd = "Configuration.dtd";
        final String encoding = "ISO-8859-1";
        final String elName = "project";
        DocType docType = new DocType(elName, dtd);
        Document doc = new Document(project, docType);
        XMLUtil.writeXML(doc, configXmlPath, encoding);
        return new File(configXmlPath);
    }

    public static Element createProject(Revision revision) {
        Element project = new Element("project");
        ConfigurationHelper.setAttribute(project, "name", revision.toString());
        ConfigurationHelper.setAttribute(project, "numberOfCyclesFeedbackOnConsole", "1");
        ConfigurationHelper.setAttribute(project, "numberOfCycleAnalysisProgressFeedbackOnConsole", "1");
        ConfigurationHelper.setAttribute(project, "checkLayerDependencies", "true");
        ConfigurationHelper.setAttribute(project, "checkSubsystemDependencies", "true");
        ConfigurationHelper.setAttribute(project, "checkPackageDependencies", "true");
        ConfigurationHelper.setAttribute(project, "cumulateLayerDependencies", "true");
        ConfigurationHelper.setAttribute(project, "cumulateSubsystemDependencies", "true");
        ConfigurationHelper.setAttribute(project, "cumulatePackageDependencies", "true");
        ConfigurationHelper.setAttribute(project, "cumulateCompilationUnitDependencies", "true");
        ConfigurationHelper.setAttribute(project, "cumulateTypeDependencies", "true");
        ConfigurationHelper.setAttribute(project, "maxLayerCycles", "0");
        ConfigurationHelper.setAttribute(project, "maxSubsystemCycles", "0");
        ConfigurationHelper.setAttribute(project, "maxPackageCycles", "0");
        ConfigurationHelper.setAttribute(project, "maxCompilationUnitCycles", "0");
        ConfigurationHelper.setAttribute(project, "maxTypeCycles", "0");

        return project;
    }

    private static void addOutputListener(Element project, String outputPath) {
        ConfigurationHelper.addComment(project, "OUTPUT LISTENERS");
        Element listener = new Element("listener");
        listener.setAttribute("class", DependomenterListener.class.getName());
        listener.setAttribute("args", outputPath);
        project.addContent(listener);
    }

    private static Element addInputFilters(Element project) {

        ConfigurationHelper.addComment(project, "INPUT PACKAGE FILTER");
        //ATTENTION: Analysis only happen if there is at least one package filter
        //using always all packages
        return ConfigurationHelper.addIncludePackageFilter(project, ".*");
    }
}
