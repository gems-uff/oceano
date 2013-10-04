package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util;

import br.uff.ic.oceano.core.tools.metrics.util.XMLUtil;
import com.valtech.source.dependometer.app.controller.compilationunit.CompilationUnitCycleParticipantsCollectedEvent;
import com.valtech.source.dependometer.app.controller.compilationunit.CompilationUnitCycleParticipationCollectedEvent;
import com.valtech.source.dependometer.app.controller.compilationunit.CompilationUnitLevelCollectedEvent;
import com.valtech.source.dependometer.app.controller.compilationunit.CompilationUnitManager;
import com.valtech.source.dependometer.app.controller.compilationunit.CompilationUnitTangleCollectedEvent;
import com.valtech.source.dependometer.app.controller.compilationunit.HandleCompilationUnitCycleParticipantsCollectedEventIf;
import com.valtech.source.dependometer.app.controller.compilationunit.HandleCompilationUnitCycleParticipationCollectedEventIf;
import com.valtech.source.dependometer.app.controller.compilationunit.HandleCompilationUnitLevelCollectedEventIf;
import com.valtech.source.dependometer.app.controller.compilationunit.HandleCompilationUnitTangleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.compilationunit.HandleMultiplePackageCompilationUnitCycleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.compilationunit.HandleSinglePackageCompilationUnitCycleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.compilationunit.MultiplePackageCompilationUnitCycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.compilationunit.SinglePackageCompilationUnitCycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.layer.HandleLayerCycleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.layer.HandleLayerCycleParticipantsCollectedEventIf;
import com.valtech.source.dependometer.app.controller.layer.HandleLayerCycleParticipationCollectedEventIf;
import com.valtech.source.dependometer.app.controller.layer.HandleLayerLevelCollectedEventIf;
import com.valtech.source.dependometer.app.controller.layer.HandleLayerTangleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.layer.LayerCycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.layer.LayerCycleParticipantsCollectedEvent;
import com.valtech.source.dependometer.app.controller.layer.LayerCycleParticipationCollectedEvent;
import com.valtech.source.dependometer.app.controller.layer.LayerLevelCollectedEvent;
import com.valtech.source.dependometer.app.controller.layer.LayerManager;
import com.valtech.source.dependometer.app.controller.layer.LayerTangleCollectedEvent;
import com.valtech.source.dependometer.app.controller.main.DependometerContext;
import com.valtech.source.dependometer.app.controller.pack.HandlePackageCycleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.pack.HandlePackageCycleParticipantsCollectedEventIf;
import com.valtech.source.dependometer.app.controller.pack.HandlePackageCycleParticipationCollectedEventIf;
import com.valtech.source.dependometer.app.controller.pack.HandlePackageLevelCollectedEventIf;
import com.valtech.source.dependometer.app.controller.pack.HandlePackageTangleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.pack.PackageCycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.pack.PackageCycleParticipantsCollectedEvent;
import com.valtech.source.dependometer.app.controller.pack.PackageCycleParticipationCollectedEvent;
import com.valtech.source.dependometer.app.controller.pack.PackageLevelCollectedEvent;
import com.valtech.source.dependometer.app.controller.pack.PackageManager;
import com.valtech.source.dependometer.app.controller.pack.PackageTangleCollectedEvent;
import com.valtech.source.dependometer.app.controller.project.AnalysisFinishedEvent;
import com.valtech.source.dependometer.app.controller.project.CycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.project.HandleAnalysisFinishedEventIf;
import com.valtech.source.dependometer.app.controller.project.HandleProjectInfoCollectedEventIf;
import com.valtech.source.dependometer.app.controller.project.ProjectInfoCollectedEvent;
import com.valtech.source.dependometer.app.controller.project.ProjectManager;
import com.valtech.source.dependometer.app.controller.subsystem.HandleSubsystemCycleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.subsystem.HandleSubsystemCycleParticipantsCollectedEventIf;
import com.valtech.source.dependometer.app.controller.subsystem.HandleSubsystemCycleParticipationCollectedEventIf;
import com.valtech.source.dependometer.app.controller.subsystem.HandleSubsystemLevelCollectedEventIf;
import com.valtech.source.dependometer.app.controller.subsystem.HandleSubsystemTangleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.subsystem.SubsystemCycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.subsystem.SubsystemCycleParticipantsCollectedEvent;
import com.valtech.source.dependometer.app.controller.subsystem.SubsystemCycleParticipationCollectedEvent;
import com.valtech.source.dependometer.app.controller.subsystem.SubsystemLevelCollectedEvent;
import com.valtech.source.dependometer.app.controller.subsystem.SubsystemManager;
import com.valtech.source.dependometer.app.controller.subsystem.SubsystemTangleCollectedEvent;
import com.valtech.source.dependometer.app.controller.type.HandleMultiplePackageTypeCycleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.type.HandleSingleCompilationUnitTypeCycleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.type.HandleSinglePackageTypeCycleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.type.HandleTypeCycleParticipantsCollectedEventIf;
import com.valtech.source.dependometer.app.controller.type.HandleTypeCycleParticipationCollectedEventIf;
import com.valtech.source.dependometer.app.controller.type.HandleTypeLevelCollectedEventIf;
import com.valtech.source.dependometer.app.controller.type.HandleTypeTangleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.type.MultiplePackageTypeCycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.type.SingleCompilationUnitTypeCycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.type.SinglePackageTypeCycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.type.TypeCycleParticipantsCollectedEvent;
import com.valtech.source.dependometer.app.controller.type.TypeCycleParticipationCollectedEvent;
import com.valtech.source.dependometer.app.controller.type.TypeLevelCollectedEvent;
import com.valtech.source.dependometer.app.controller.type.TypeManager;
import com.valtech.source.dependometer.app.controller.type.TypeTangleCollectedEvent;
import com.valtech.source.dependometer.app.controller.verticalslice.HandleVerticalSliceCycleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.verticalslice.HandleVerticalSliceCycleParticipantsCollectedEventIf;
import com.valtech.source.dependometer.app.controller.verticalslice.HandleVerticalSliceCycleParticipationCollectedEventIf;
import com.valtech.source.dependometer.app.controller.verticalslice.HandleVerticalSliceLevelCollectedEventIf;
import com.valtech.source.dependometer.app.controller.verticalslice.HandleVerticalSliceTangleCollectedEventIf;
import com.valtech.source.dependometer.app.controller.verticalslice.VerticalSliceCycleCollectedEvent;
import com.valtech.source.dependometer.app.controller.verticalslice.VerticalSliceCycleParticipantsCollectedEvent;
import com.valtech.source.dependometer.app.controller.verticalslice.VerticalSliceCycleParticipationCollectedEvent;
import com.valtech.source.dependometer.app.controller.verticalslice.VerticalSliceLevelCollectedEvent;
import com.valtech.source.dependometer.app.controller.verticalslice.VerticalSliceManager;
import com.valtech.source.dependometer.app.controller.verticalslice.VerticalSliceTangleCollectedEvent;
import com.valtech.source.dependometer.app.core.elements.EntityTypeEnum;
import com.valtech.source.dependometer.app.core.elements.NotParsedCompilationUnit;
import com.valtech.source.dependometer.app.core.elements.NotParsedPackage;
import com.valtech.source.dependometer.app.core.elements.NotParsedType;
import com.valtech.source.dependometer.app.core.elements.ParsedClass;
import com.valtech.source.dependometer.app.core.elements.ParsedCompilationUnit;
import com.valtech.source.dependometer.app.core.elements.ParsedInterface;
import com.valtech.source.dependometer.app.core.elements.ParsedPackage;
import com.valtech.source.dependometer.app.core.elements.Type;
import com.valtech.source.dependometer.app.core.provider.DependencyElementIf;
import com.valtech.source.dependometer.app.core.provider.MetricIf;
import com.valtech.source.dependometer.app.core.provider.ProjectIf;
import com.valtech.source.dependometer.ui.console.Dependometer;
import java.io.IOException;
import java.util.List;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Based on com.valtech.source.dependometer.ui.console.output.XMLWriter class
 * from oliver.rohr.
 *
 * @author Daniel
 */
public class DependomenterListener implements HandleProjectInfoCollectedEventIf,
        HandlePackageCycleCollectedEventIf, HandleLayerCycleCollectedEventIf, HandleVerticalSliceCycleCollectedEventIf,
        HandleSubsystemCycleCollectedEventIf, HandleSinglePackageCompilationUnitCycleCollectedEventIf,
        HandleMultiplePackageCompilationUnitCycleCollectedEventIf, HandleSinglePackageTypeCycleCollectedEventIf,
        HandleMultiplePackageTypeCycleCollectedEventIf, HandleSingleCompilationUnitTypeCycleCollectedEventIf,
        HandleLayerLevelCollectedEventIf, HandleVerticalSliceLevelCollectedEventIf, HandleSubsystemLevelCollectedEventIf,
        HandlePackageLevelCollectedEventIf, HandleCompilationUnitLevelCollectedEventIf, HandleTypeLevelCollectedEventIf,
        HandleLayerCycleParticipationCollectedEventIf, HandleVerticalSliceCycleParticipationCollectedEventIf,
        HandleSubsystemCycleParticipationCollectedEventIf, HandlePackageCycleParticipationCollectedEventIf,
        HandleCompilationUnitCycleParticipationCollectedEventIf, HandleTypeCycleParticipationCollectedEventIf,
        HandleLayerCycleParticipantsCollectedEventIf, HandleVerticalSliceCycleParticipantsCollectedEventIf,
        HandleSubsystemCycleParticipantsCollectedEventIf, HandlePackageCycleParticipantsCollectedEventIf,
        HandleCompilationUnitCycleParticipantsCollectedEventIf, HandleTypeCycleParticipantsCollectedEventIf,
        HandleAnalysisFinishedEventIf, HandleVerticalSliceTangleCollectedEventIf, HandleLayerTangleCollectedEventIf,
        HandleSubsystemTangleCollectedEventIf, HandlePackageTangleCollectedEventIf,
        HandleCompilationUnitTangleCollectedEventIf, HandleTypeTangleCollectedEventIf {

    public static final String XML_ROOT = "dependometer-results";
    public static final String XML_CYCLES = "cycles";
    public static final String XML_CYCLE = "cycle";
    public static final String XML_LEVEL = "level";
    public static final String XML_PARTICIPANT = "participant";
    public static final String XML_PROJECT = "Project";
    public static final String XML_METRIC = "metric";
    public static final String XML_NAME = "name";
    public static final String XML_TYPES = "Types";
    public static final String XML_TYPE = "type";
    public static final String XML_COMPILATIONUNITS = "CompilationUnits";
    public static final String XML_COMPILATIONUNIT = "compilationUnit";
    public static final String XML_PACKAGES = "Packages";
    public static final String XML_PACKAGE = "package";
    public static final String XML_SUBSYSTEMS = "subsystems";
    public static final String XML_SUBSYSTEM = "subsystem";
    public static final String XML_LAYERS = "layers";
    public static final String XML_LAYER = "layer";
    public static final String XML_VERTICALSLICES = "verticalSlices";
    public static final String XML_VERTICALSLICE = "verticalSlice";
    private Element root;
    private String xmlPath;

    public DependomenterListener(String[] arguments) {
        attachToDependometerInstance();

        if (arguments.length < 1) {
            throw new IllegalArgumentException("Need at least one argument - output file path");
        }        
        this.xmlPath = arguments[0];
        this.root = new Element(XML_ROOT);        
    }

    private void attachToDependometerInstance() {
        DependometerContext context = Dependometer.getContext();

        ProjectManager projectManager = context.getProjectManager();
        projectManager.attach((HandleProjectInfoCollectedEventIf) this);
        projectManager.attach((HandleAnalysisFinishedEventIf) this);

        VerticalSliceManager verticalSliceManager = context.getVerticalSliceManager();
        verticalSliceManager.attach((HandleVerticalSliceCycleCollectedEventIf) this);
        verticalSliceManager.attach((HandleVerticalSliceLevelCollectedEventIf) this);
        verticalSliceManager.attach((HandleVerticalSliceCycleParticipationCollectedEventIf) this);
        verticalSliceManager.attach((HandleVerticalSliceCycleParticipantsCollectedEventIf) this);
        verticalSliceManager.attach((HandleVerticalSliceTangleCollectedEventIf) this);

        LayerManager layerManager = context.getLayerManager();
        layerManager.attach((HandleLayerCycleCollectedEventIf) this);
        layerManager.attach((HandleLayerLevelCollectedEventIf) this);
        layerManager.attach((HandleLayerCycleParticipationCollectedEventIf) this);
        layerManager.attach((HandleLayerCycleParticipantsCollectedEventIf) this);
        layerManager.attach((HandleLayerTangleCollectedEventIf) this);

        SubsystemManager subsystemManager = context.getSubsystemManager();
        subsystemManager.attach((HandleSubsystemCycleCollectedEventIf) this);
        subsystemManager.attach((HandleSubsystemLevelCollectedEventIf) this);
        subsystemManager.attach((HandleSubsystemCycleParticipationCollectedEventIf) this);
        subsystemManager.attach((HandleSubsystemCycleParticipantsCollectedEventIf) this);
        subsystemManager.attach((HandleSubsystemTangleCollectedEventIf) this);

        PackageManager packageManager = context.getPackageManager();
        packageManager.attach((HandlePackageCycleCollectedEventIf) this);
        packageManager.attach((HandlePackageLevelCollectedEventIf) this);
        packageManager.attach((HandlePackageCycleParticipationCollectedEventIf) this);
        packageManager.attach((HandlePackageCycleParticipantsCollectedEventIf) this);
        packageManager.attach((HandlePackageTangleCollectedEventIf) this);

        CompilationUnitManager compilationUnitManager = context.getCompilationUnitManager();
        compilationUnitManager.attach((HandleSinglePackageCompilationUnitCycleCollectedEventIf) this);
        compilationUnitManager.attach((HandleMultiplePackageCompilationUnitCycleCollectedEventIf) this);
        compilationUnitManager.attach((HandleCompilationUnitLevelCollectedEventIf) this);
        compilationUnitManager.attach((HandleCompilationUnitCycleParticipationCollectedEventIf) this);
        compilationUnitManager.attach((HandleCompilationUnitCycleParticipantsCollectedEventIf) this);
        compilationUnitManager.attach((HandleCompilationUnitTangleCollectedEventIf) this);

        TypeManager typeManager = context.getTypeManager();
        typeManager.attach((HandleSinglePackageTypeCycleCollectedEventIf) this);
        typeManager.attach((HandleMultiplePackageTypeCycleCollectedEventIf) this);
        typeManager.attach((HandleSingleCompilationUnitTypeCycleCollectedEventIf) this);
        typeManager.attach((HandleTypeLevelCollectedEventIf) this);
        typeManager.attach((HandleTypeCycleParticipationCollectedEventIf) this);
        typeManager.attach((HandleTypeCycleParticipantsCollectedEventIf) this);
        typeManager.attach((HandleTypeTangleCollectedEventIf) this);
    }


    public void writeXML() throws IOException {
        DocType docType = new DocType("result");
        XMLUtil.writeXML(new Document(root, docType), xmlPath);
    }

    public void handleEvent(LayerCycleCollectedEvent event) {
    }

    public void handleEvent(VerticalSliceCycleCollectedEvent event) {
    }

    public void handleEvent(LayerLevelCollectedEvent event) {
    }

    public void handleEvent(VerticalSliceLevelCollectedEvent event) {
    }

    public void handleEvent(SubsystemLevelCollectedEvent event) {
    }

    public void handleEvent(PackageLevelCollectedEvent event) {
    }

    public void handleEvent(CompilationUnitLevelCollectedEvent event) {
    }

    public void handleEvent(TypeLevelCollectedEvent event) {
    }

    public void handleEvent(LayerCycleParticipationCollectedEvent event) {
    }

    public void handleEvent(VerticalSliceCycleParticipationCollectedEvent event) {
    }

    public void handleEvent(SubsystemCycleParticipationCollectedEvent event) {
    }

    public void handleEvent(PackageCycleParticipationCollectedEvent event) {
    }

    public void handleEvent(CompilationUnitCycleParticipationCollectedEvent event) {
    }

    public void handleEvent(TypeCycleParticipationCollectedEvent event) {
    }

    public void handleEvent(LayerCycleParticipantsCollectedEvent event) {
    }

    public void handleEvent(VerticalSliceCycleParticipantsCollectedEvent event) {
    }

    public void handleEvent(SubsystemCycleParticipantsCollectedEvent event) {
    }

    public void handleEvent(PackageCycleParticipantsCollectedEvent event) {
    }

    public void handleEvent(CompilationUnitCycleParticipantsCollectedEvent event) {
    }

    public void handleEvent(TypeCycleParticipantsCollectedEvent event) {
    }

    public void handleEvent(VerticalSliceTangleCollectedEvent event) {
    }

    public void handleEvent(LayerTangleCollectedEvent event) {
    }

    public void handleEvent(SubsystemTangleCollectedEvent event) {
    }

    public void handleEvent(PackageTangleCollectedEvent event) {
    }

    public void handleEvent(CompilationUnitTangleCollectedEvent event) {
    }

    public void handleEvent(TypeTangleCollectedEvent event) {
    }
    
    public void handleEvent(SingleCompilationUnitTypeCycleCollectedEvent event) {
        writeCycleEvent(EntityTypeEnum.Type.name(), event);
    }

    public void handleEvent(SinglePackageTypeCycleCollectedEvent event) {
        writeCycleEvent(EntityTypeEnum.Type.name(), event);
    }

    public void handleEvent(MultiplePackageTypeCycleCollectedEvent event) {
        writeCycleEvent(EntityTypeEnum.Type.name(), event);
    }

    public void handleEvent(SinglePackageCompilationUnitCycleCollectedEvent event) {
        writeCycleEvent(EntityTypeEnum.CompilationUnit.name(), event);
    }

    public void handleEvent(MultiplePackageCompilationUnitCycleCollectedEvent event) {
        writeCycleEvent(EntityTypeEnum.CompilationUnit.name(), event);
    }

    public void handleEvent(PackageCycleCollectedEvent event) {
        writeCycleEvent(EntityTypeEnum.Package.name(), event);
    }

    public void handleEvent(SubsystemCycleCollectedEvent event) {
        writeCycleEvent(EntityTypeEnum.Subsystem.name(), event);
    }
    
    /**
     * This is not the moment to collect all metrics
     *
     * @param event
     */
    public void handleEvent(AnalysisFinishedEvent event) {
        try {
            ProjectIf projectIf = event.getProject();
            writeProjectMetrics(projectIf);
            writeMetrics(projectIf.getTypes(), EntityTypeEnum.Type);
            writeMetrics(projectIf.getCompilationUnits(), EntityTypeEnum.CompilationUnit);
            writeMetrics(projectIf.getPackages(), EntityTypeEnum.Package);
            
            writeXML();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Happens before AnalysisFinishedEvent
     *
     * @param event
     */
    public void handleEvent(ProjectInfoCollectedEvent event) {
        try {
            ProjectIf projectIf = event.getProject();
            writeProjectMetrics(projectIf);
            writeMetrics(projectIf.getTypes(), EntityTypeEnum.Type);
            writeMetrics(projectIf.getCompilationUnits(), EntityTypeEnum.CompilationUnit);
            writeMetrics(projectIf.getPackages(), EntityTypeEnum.Package);
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    

    private void writeCycleEvent(String entityTypeName, CycleCollectedEvent cycleEvent) {
        Element cycle = new Element(XML_CYCLE).setAttribute(XML_LEVEL, entityTypeName);
        for (DependencyElementIf el : cycleEvent.getCycle()) {
            Element participant = new Element(XML_PARTICIPANT);
            participant.setText(el.getFullyQualifiedName());
            cycle.setContent(participant);
        }
        root.addContent(cycle);
    }

    private void writeProjectMetrics(ProjectIf projectIf) throws Exception {
        Element project = root.getChild(XML_PROJECT);
        if (project == null) {
            project = new Element(XML_PROJECT);
            root.addContent(project);
        }
        for (MetricIf metricIf : projectIf.getMetrics()) {
            String metricName = MetricHelper.getMetricName(metricIf);

            Element metric = project.getChild(metricName);
            if (metric == null) {
                metric = new Element(metricName);
                project.addContent(metric);
            }
            metric.setText(metricIf.getValueAsString());
        }

    }

    private void writeMetrics(DependencyElementIf[] elements, EntityTypeEnum element) throws Exception {
        String name = MetricHelper.buildID(element.getXmlName());
        Element elementsRoot = root.getChild(name + "s");
        if (elementsRoot == null) {
            elementsRoot = new Element(name + "s");
            root.addContent(elementsRoot);
        }

        for (DependencyElementIf depElementIf : elements) {
            Element elementRoot = getChildByAttributeName(elementsRoot, name, depElementIf.getFullyQualifiedName());
            if (elementRoot == null) {
                elementRoot = new Element(name);
                elementsRoot.addContent(elementRoot);
                elementRoot.setAttribute(XML_NAME, depElementIf.getFullyQualifiedName());
            }

            //Details
            String typeName = "";
            String absoluteSourcePath = "";
            if (depElementIf instanceof ParsedPackage) {
                typeName = "Package";
            } else if (depElementIf instanceof ParsedClass) {
                typeName = "Class";
                absoluteSourcePath = depElementIf.getAbsoluteSourcePath().getAbsolutePath();
            } else if (depElementIf instanceof ParsedCompilationUnit) {
                typeName = "CompilationUnit";
                absoluteSourcePath = depElementIf.getAbsoluteSourcePath().getAbsolutePath();
            } else if (depElementIf instanceof ParsedInterface) {
                typeName = "Interface";
                absoluteSourcePath = depElementIf.getAbsoluteSourcePath().getAbsolutePath();
            } else if (depElementIf instanceof NotParsedCompilationUnit) {
                typeName = "NotParsedCompilationUnit";
            } else if (depElementIf instanceof NotParsedPackage) {
                typeName = "NotParsedPackage";
            } else if (depElementIf instanceof NotParsedType) {
                typeName = "NotParsedType";
            } else {
                throw new Exception("Unsupported type for:" + depElementIf);
            }
            if (elementRoot.getChild("ElementType") == null) {
                Element elementType = new Element("ElementType");
                elementType.setText(typeName);
                elementRoot.addContent(elementType);
            }

            if (elementRoot.getChild("AbsoluteSourcePath") == null) {
                Element elementAbsoluteSourcePath = new Element("AbsoluteSourcePath");
                elementAbsoluteSourcePath.setText(absoluteSourcePath);
                elementRoot.addContent(elementAbsoluteSourcePath);
            }

            if (elementRoot.getChild("FullyQualifiedName") == null) {
                Element fullyQualifiedName = new Element("FullyQualifiedName");
                fullyQualifiedName.setText(depElementIf.getFullyQualifiedName());
                elementRoot.addContent(fullyQualifiedName);
            }

            if (elementRoot.getChild("FullyQualifiedContainmentName") == null) {
                Element fullyQualifiedContainmentName = new Element("FullyQualifiedContainmentName");
                fullyQualifiedContainmentName.setText(depElementIf.getFullyQualifiedContainmentName());
                elementRoot.addContent(fullyQualifiedContainmentName);
            }

            if (depElementIf instanceof Type) {
                Type tp = (Type) depElementIf;

                if (elementRoot.getChild("PackageName") == null) {
                    Element newElement = new Element("PackageName");
                    newElement.setText(tp.getPackageName());
                    elementRoot.addContent(newElement);
                }

//                newElement = new Element("Info");
//                newElement.setText(tp.getInfo());
//                elementRoot.addContent(newElement);

//                newElement = new Element("Description");
//                newElement.setText(tp.getDescription());
//                elementRoot.addContent(newElement);

//                newElement = new Element("Source");
//                newElement.setText(String.valueOf(tp.getSource()));
//                elementRoot.addContent(newElement);
            }

            //Metrics
            for (MetricIf metric : depElementIf.getMetrics()) {
                String metricName = MetricHelper.getMetricName(metric);
                Element metricElement = elementRoot.getChild(metricName);
                if (metricElement == null) {
                    metricElement = new Element(metricName);
                    metricElement.setText(metric.getValueAsString());
                    elementRoot.addContent(metricElement);                    
                }                
            }
        }
    }

    private Element getChildByAttributeName(Element elementsRoot, String name, String fullyQualifiedName) {
        List<Element> children = elementsRoot.getChildren(name);
        if (children == null || children.isEmpty()) {
            return null;
        }

        for (Element element : children) {
            if (element.getAttributeValue(XML_NAME).contentEquals(fullyQualifiedName)) {
                return element;
            }
        }
        return null;
    }
    
    

    
}
