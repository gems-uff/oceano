package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util;

import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.compiler.CompilerService;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.DependometerException;
import br.uff.ic.oceano.core.tools.metrics.util.XMLUtil;
import br.uff.ic.oceano.core.tools.revision.RevisionUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.util.SystemUtil;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import com.valtech.source.dependometer.app.core.provider.ProviderFactory;
import com.valtech.source.dependometer.ui.console.Dependometer;
import java.io.File;
import java.util.List;
import java.util.Set;
import org.jdom.Document;

/**
 *
 *
 * @author Daniel
 */
public class Adapter {

    private final Revision revision;
    private String outputPath;
    private Document document;

    public Adapter(Revision revision) {
        this.revision = revision;        
    }

    public Double getMetric(Metric metric, String path) throws DependometerException {        
        try {
            return DependomenterDocumentReader.getMetric(metric, path, getDocument());
        } catch (Exception ex) {
            throw new DependometerException("Fail to get metric " + metric.getName() + " from " + path, ex);
        }
    }

    private synchronized Document getDocument() throws DependometerException {
        if (document == null) {
            document = processRevision();
        }
        return this.document;
    }

    /**
     * Allow checking results on tests
     *
     * @param path
     * @throws DependometerException
     */
    public synchronized void writeMetricsToXML(String path) throws DependometerException {
        try {
            final String encoding = "ISO-8859-1";            
            XMLUtil.writeXML(getDocument(), path, encoding);
        } catch (Exception e) {
            throw new DependometerException(e);
        }
    }

    /**
     *
     * @param metric
     * @param revision
     * @param path
     * @return
     * @throws DependometerException
     */
    private Document processRevision() throws DependometerException {
        try {
            //Validate parameters
            validateParameter();

            //Set up output path
            prepareOutputPath();

            //Compile Java project if necessary
            Language language = getRevision().getProject().getLanguage();
            if (Language.JAVA.equals(language)) {
                CompilerService.compile(getRevision());
            }

            //create xml config file from revision software project
            File config = ConfigurationFactory.createConfigFile(getRevision(), getOutputXmlPath(), getConfigXmlPath());

            File outputFile = execDependometer(config);

            //read output xml
            Document result = readOutputXml(outputFile);

            //clean output path
            deleteOutputPath();

            return result;
        } catch (Exception ex) {
            throw new DependometerException(ex);
        }
    }

    private Document readOutputXml(File resultFile) throws DependometerException {
        try {
            return XMLUtil.readXml(resultFile.getPath());
        } catch (Exception ex) {
            throw new DependometerException(ex);
        }
    }

    private String getOutputXmlPath() throws Exception {
        return getOutputPath() + SystemUtil.FILESEPARATOR + "output.xml";
    }

    private String getConfigXmlPath() throws Exception {
        return getOutputPath() + "config.xml";
    }

    private String createOutputPath() throws Exception {
        String path = SystemUtil.getTempDirectory();

        path += SystemUtil.FILESEPARATOR;
        //allow mult threading
        path += "dependometer_" + System.currentTimeMillis();
        path += SystemUtil.FILESEPARATOR;

        return path;
    }

    private String getOutputPath() throws Exception {
        if(this.outputPath == null){
            this.outputPath = createOutputPath();
        }
        return this.outputPath;
    }

    /**
     * Clear output directory.
     *
     * @param revision
     */
    private void prepareOutputPath() throws DependometerException {
        try {
            String path = getOutputPath();
            File file = new File(path);

            //clear previous run
            boolean dirExisted = false;
            if (file.exists()) {
                dirExisted = true;
                deleteOutputPath();
            }

            //Create output path
            //one try is not enough
            PathUtil.mkDirs(path);

            //verify
            if (!file.exists()) {
                System.out.println("Existed: " + dirExisted);
                throw new DependometerException("Fail to prepare output path. Directory not created.");
            }
        } catch (Exception ex) {
            throw new DependometerException("Fail to prepare output path.", ex);
        }
    }

    /**
     * Delete output directory
     *
     * @param revision
     */
    private void deleteOutputPath() throws Exception {
        FileUtils.deleteDirectory(new File(getOutputPath()));
    }

    /**
     * Validate necessary parameter for processing.
     *
     * @throws DependometerException
     */
    private void validateParameter() throws DependometerException {
        //check Parameter
        if (getRevision() == null) {
            throw new DependometerException("Null parameter 'revision'");
        } else if (getRevision().getProject() == null) {
            throw new DependometerException("SoftwareProject not set on revision");
        }
        SoftwareProject project = getRevision().getProject();

        Language lang = project.getLanguage();
        if (!Language.CPP.equals(lang) && !Language.JAVA.equals(lang)) {
            throw new DependometerException("Unsupported language: " + lang);
        } else if (Language.JAVA.equals(lang)) {
            if (!project.isMavenProject()) {
                throw new DependometerException("Unsupported non Maven java projects");
            }
            Set col = null;
            try {
                col = RevisionUtil.get().getCompilationFolders(getRevision());
            } catch (Exception ex) {
                throw new DependometerException(ex);
            }
            if (col.size() > 1) {
                throw new DependometerException("Unsupported Maven module projects");
            }
        }
    }

    /**
     * @return the revision
     */
    public Revision getRevision() {
        return revision;
    }

    private synchronized File execDependometer(File configFile) throws Exception {

        Dependometer dependometer = new Dependometer();
        setLanguageTypeProvider();
        dependometer.startAnalysis(configFile);
        return new File(getOutputXmlPath());
    }

    /**
     * Set system property which dependometer uses to parse project
     *
     * @throws DependometerException
     */
    private void setLanguageTypeProvider() throws DependometerException {
        Language language = getRevision().getProject().getLanguage();
        String typedefProviderClass = "";
        if (Language.CPP.equals(language)) {
            typedefProviderClass = com.valtech.source.dependometer.app.typedefprovider.filebased.cpp.TypeDefinitionProvider.class.getName();
        } else if (Language.JAVA.equals(language)) {
            typedefProviderClass = com.valtech.source.dependometer.app.typedefprovider.filebased.java.TypeDefinitionProvider.class.getName();
        } else {
            throw new DependometerException("Unsupported language for TypeDefinitionProvider");
        }
        System.setProperty(ProviderFactory.TYPE_DEFINITION_PROVIDER_KEY, typedefProviderClass);
    }

    public List<Double> getCompilationUnitValues(MetricEnum metric) throws DependometerException {
        Document doc = getDocument();        
        return DependomenterDocumentReader.getCompilationUnitValues(metric,doc);
    }

    public List<Double> getTypeValues(MetricEnum metric) throws DependometerException{
        Document doc = getDocument();   
        return DependomenterDocumentReader.getTypeValues(metric,doc);
    }

}
