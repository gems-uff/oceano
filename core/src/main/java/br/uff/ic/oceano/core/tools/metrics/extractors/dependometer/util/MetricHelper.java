/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util;

import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.DependometerException;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import com.valtech.source.dependometer.app.core.metrics.MetricDefinition;
import static com.valtech.source.dependometer.app.core.provider.MetricDefinitionIf.TYPE_NUMBER;
import com.valtech.source.dependometer.app.core.provider.MetricIf;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Helper class to translate Dependometer metrics to/from Oceano.
 *
 * @author daniel heráclio
 */
public class MetricHelper {

    private static List<MetricEnum> projectMetrics;
    private static List<MetricEnum> layerMetrics;
    private static List<MetricEnum> subsystemMetrics;
    private static List<MetricEnum> verticalSliceMetrics;
    private static List<MetricEnum> packageMetrics;
    private static List<MetricEnum> compilationUnitMetrics;
    private static List<MetricEnum> typeMetrics;
    private static List<MetricEnum> canBeProjectMetrics;
    private static List<MetricEnum> notImplementedMetrics;
    private static EnumMap<MetricEnumeration, MetricEnum> oceano2dependometer;

    static {
        initDependometerMetricTargets();
        initMapping();
        initCalculable();
    }

    public static MetricEnumeration getOceanoMetric(final MetricEnum dependometer) throws DependometerException {
        Iterator<Entry<MetricEnumeration, MetricEnum>> it = oceano2dependometer.entrySet().iterator();
        while (it.hasNext()) {
            Entry<MetricEnumeration, MetricEnum> pair = it.next();
            if (pair.getValue().equals(dependometer)) {
                return pair.getKey();
            }
        }
        return null;
    }

    public static MetricEnum getDependometerMetric(final MetricEnumeration oceano) throws DependometerException {
        return oceano2dependometer.get(oceano);
    }

    private static void initMapping() {
        oceano2dependometer = new EnumMap<MetricEnumeration, MetricEnum>(MetricEnumeration.class);

        //oceano metrics to dependometer metrics        
        //Project metrics
        //project to project
        oceano2dependometer.put(MetricEnumeration.DSC, MetricEnum.NUMBER_OF_TYPES);
                
        //ANA (QMOOD.METRIC_AVERAGE_NUMBER_OF_ANCESTORS)
        //needs to be calculated
        oceano2dependometer.put(MetricEnumeration.ANA, MetricEnum.DEPTH_OF_CLASS_INHERITANCE);
        
        //NOH (QMOOD.METRIC_NUMBER_OF_HIERARCHIES)
        //needs to be calculated
        oceano2dependometer.put(MetricEnumeration.NOH, MetricEnum.DEPTH_OF_CLASS_INHERITANCE);

        //Package metrics
        //package to package
        oceano2dependometer.put(MetricEnumeration.RMA, MetricEnum.ABSTRACTNESS);

        //File metrics
        //file to CompilationUnit (also package, layer, subsystem)
        oceano2dependometer.put(MetricEnumeration.DCC, MetricEnum.DEPENDS_UPON);

        //no mapping
        //CIS(QMOOD.METRIC_CLASS_INTERFACE_SIZE)- MetricEnum.Interface is boolean
        //CAM(QMOOD.METRIC_COHESION_AMONG_METHODS_IN_CLASS) -
        //ACC("Cyclomatic Complexity")

    }

    private static void initCalculable() {
        canBeProjectMetrics = new LinkedList<MetricEnum>();

        //adding compilation units number of types
        canBeProjectMetrics.add(MetricEnum.NUMBER_OF_TYPES);
        
        //average of DepthOfClassInheritance is the same of MetricEnumeration.ANA
        canBeProjectMetrics.add(MetricEnum.DEPTH_OF_CLASS_INHERITANCE);        
        
        
    }

    public static boolean isPackageMetric(MetricEnum mEnum) {
        return packageMetrics.contains(mEnum);
    }

    public static boolean isProjectMetric(MetricEnum mEnum) {
        return projectMetrics.contains(mEnum);
    }

    /**
     * Metrics which can be calculated for the hole project but aren't by
     * dependometer
     *
     * @param metricEnum
     * @return
     */
    public static boolean canBeProjectMetric(MetricEnum metricEnum) {
        return canBeProjectMetrics.contains(metricEnum);
    }
    
    public static boolean isImplemented(MetricEnum mEnum) {
        return !notImplementedMetrics.contains(mEnum);
    }

    public static boolean isSubsystemMetric(MetricEnum mEnum) {
        return subsystemMetrics.contains(mEnum);
    }

    public static boolean isVerticalSliceMetric(MetricEnum mEnum) {
        return verticalSliceMetrics.contains(mEnum);
    }

    public static boolean isLayerMetric(MetricEnum mEnum) {
        return layerMetrics.contains(mEnum);
    }

    public static boolean isTypeMetric(MetricEnum mEnum) {
        return typeMetrics.contains(mEnum);
    }

    public static boolean isCompilationUnitMetric(MetricEnum mEnum) {
        return compilationUnitMetrics.contains(mEnum);
    }

    private static void initDependometerMetricTargets() {
        //dependometer documentation is confusing and flawed
        //metrics extrated from test dependometerServices


        // <editor-fold defaultstate="collapsed" desc="Project metrics">
        projectMetrics = new LinkedList<MetricEnum>();
        projectMetrics.add(MetricEnum.AVERAGE_COMPONENT_DEPENDENCY);
        projectMetrics.add(MetricEnum.CUMULATIVE_COMPONENT_DEPENDENCY);
        projectMetrics.add(MetricEnum.CUMULATIVE_COMPONENT_DEPENDENCY_FOR_BALANCED_BINARY_TREE);
        projectMetrics.add(MetricEnum.CUMULATIVE_COMPONENT_DEPENDENCY_FOR_CYCLICALLY_DEPENDENT_GRAPH);
        projectMetrics.add(MetricEnum.NORMALIZED_CUMULATIVE_COMPONENT_DEPENDENCY);

        //Number of compilation units is SIZE or number of files
        //
        //internal are those who passed the filter and external the rest
        //so NumberOfProjectInternalCompilationUnits means SIZE
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_EXTERNAL_COMPILATION_UNITS);
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_INTERNAL_COMPILATION_UNITS);
        //same as NumberOfProjectInternalCompilationUnits according to dependometer-core code
        projectMetrics.add(MetricEnum.NUMBER_OF_COMPONENTS);

        //
        projectMetrics.add(MetricEnum.PERCENTAGE_OF_PROJECT_INTERNAL_LAYERS_WITH_A_RELATIONAL_COHESION_GREATER_THAN_ONE);
        projectMetrics.add(MetricEnum.PERCENTAGE_OF_PROJECT_INTERNAL_PACKAGES_WITH_A_RELATIONAL_COHESION_GREATER_THAN_ONE);
        projectMetrics.add(MetricEnum.PERCENTAGE_OF_PROJECT_INTERNAL_SUBSYSTEMS_WITH_A_RELATIONAL_COHESION_GREATER_THAN_ONE);
        projectMetrics.add(MetricEnum.PERCENTAGE_OF_PROJECT_INTERNAL_VERTICAL_SLICES_WITH_A_RELATIONAL_COHESION_GREATER_THAN_ONE);

        projectMetrics.add(MetricEnum.NUMBER_OF_ASSERTIONS);
        projectMetrics.add(MetricEnum.AVERAGE_USAGE_OF_ASSERTIONS_PER_CLASS);

        //Metric described as "total number of efferent package dependencies" in docs
        projectMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_DEPENDENCIES);
        //Metric described as "total number of forbidden efferent package dependencies"
        projectMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_DEPENDENCIES);
        //Metric described as "total number of project internal efferent package dependencies"
        projectMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_DEPENDENCIES_TO_PROJECT_EXTERNAL);

        //undocumented
        //determined by dependometer-core code
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_INTERNAL_LAYERS);
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_INTERNAL_PACKAGES);
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_INTERNAL_SUBSYSTEMS);
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_INTERNAL_TYPES);
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_INTERNAL_VERTICAL_SLICES);
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_EXTERNAL_LAYERS);
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_EXTERNAL_PACKAGES);
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_EXTERNAL_SUBSYSTEMS);
        projectMetrics.add(MetricEnum.NUMBER_OF_PROJECT_EXTERNAL_TYPES);
        projectMetrics.add(MetricEnum.CYCLES_EXIST_BETWEEN_PROJECT_INTERNAL_COMPILATION_UNITS);
        projectMetrics.add(MetricEnum.CYCLES_EXIST_BETWEEN_PROJECT_INTERNAL_LAYERS);
        projectMetrics.add(MetricEnum.CYCLES_EXIST_BETWEEN_PROJECT_INTERNAL_PACKAGES);
        projectMetrics.add(MetricEnum.CYCLES_EXIST_BETWEEN_PROJECT_INTERNAL_SUBSYSTEM);
        projectMetrics.add(MetricEnum.CYCLES_EXIST_BETWEEN_PROJECT_INTERNAL_TYPES);
        projectMetrics.add(MetricEnum.CYCLES_EXIST_BETWEEN_PROJECT_INTERNAL_VERTICAL_SLICES);
        projectMetrics.add(MetricEnum.MAX_DEPTH_OF_PACKAGE_HIERARCHY);
        projectMetrics.add(MetricEnum.MAX_DEPTH_OF_TYPE_INHERITANCE);
        projectMetrics.add(MetricEnum.NUMBER_OF_ALLOWED_OUTGOING_LAYER_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_ALLOWED_OUTGOING_PACKAGE_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_ALLOWED_OUTGOING_SUBSYSTEM_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_COMPILATION_UNIT_CYCLES);
        projectMetrics.add(MetricEnum.AFFERENT_INCOMING_COUPLING_PROJECT_EXTERNAL);
        projectMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_COMPILATION_UNIT_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_LAYER_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_PACKAGE_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_SUBSYSTEM_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_TYPE_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_VERTICAL_SLICE_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_NOT_ASSIGNED_PACKAGES);
        projectMetrics.add(MetricEnum.NUMBER_OF_NOT_IMPLEMENTED_SUBSYSTEMS);
        projectMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_COMPILATION_UNIT_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_LAYER_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_PACKAGE_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_SUBSYSTEM_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_TYPE_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_VERTICAL_SLICE_DEPENDENCIES);
        projectMetrics.add(MetricEnum.NUMBER_OF_PACKAGE_CYCLES);
        projectMetrics.add(MetricEnum.NUMBER_OF_TYPE_CYCLES);
        projectMetrics.add(MetricEnum.NUMBER_OF_SUBSYSTEM_CYCLES);
        projectMetrics.add(MetricEnum.NUMBER_OF_LAYER_CYCLES);
        projectMetrics.add(MetricEnum.AVERAGE_USAGE_OF_ASSERTIONS_PER_CLASS);
        projectMetrics.add(MetricEnum.NUMBER_OF_TYPE_TANGLES);
        projectMetrics.add(MetricEnum.NUMBER_OF_COMPILATION_UNIT_TANGLES);
        projectMetrics.add(MetricEnum.NUMBER_OF_SUBSYSTEM_TANGLES);
        projectMetrics.add(MetricEnum.NUMBER_OF_PACKAGE_TANGLES);
        projectMetrics.add(MetricEnum.NUMBER_OF_LAYER_TANGLES);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Layer metrics">
        layerMetrics = new LinkedList<MetricEnum>();
        //"contained subsystems and their inner layer dependencies
        layerMetrics.add(MetricEnum.NUMBER_OF_CONTAINED_SUBSYSTEMS);
        //"afferent (incoming) and efferent (outgoing) dependencies - subsystems causing these dependencies"
        //not found in code        

        //common for the elements layer, subsystem and package
        //abstract types (Na)
        layerMetrics.add(MetricEnum.NUMBER_OF_ABSTRACT_TYPES);
        //abstractness (A)
        layerMetrics.add(MetricEnum.ABSTRACTNESS);
        //accessible types
        layerMetrics.add(MetricEnum.NUMBER_OF_ACCESSIBLE_TYPES);
        //afferent (incoming) dependencies
        layerMetrics.add(MetricEnum.NUMBER_OF_INCOMING_DEPENDENCIES);
        //afferent coupling (Ca)
        layerMetrics.add(MetricEnum.AFFERENT_INCOMING_COUPLING);
        //assertions
        layerMetrics.add(MetricEnum.NUMBER_OF_ASSERTIONS);
        //average component dependency (ACD)
        layerMetrics.add(MetricEnum.AVERAGE_COMPONENT_DEPENDENCY);
        //cumulative component dependency (CCD)
        layerMetrics.add(MetricEnum.CUMULATIVE_COMPONENT_DEPENDENCY);
        //depends upon elements (incl. self)
        layerMetrics.add(MetricEnum.DEPENDS_UPON);
        //distance (D)
        layerMetrics.add(MetricEnum.DISTANCE);
        //efferent (outgoing) dependencies (internal and external)
        layerMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_DEPENDENCIES);
        //efferent coupling (Ce)
        layerMetrics.add(MetricEnum.EFFERENT_OUTGOING_COUPLING);
        //external type relations
        layerMetrics.add(MetricEnum.NUMBER_OF_EXTERNAL_TYPE_RELATIONS);
        //forbidden efferent (outgoing) dependencies
        layerMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_DEPENDENCIES);
        //instability (I)
        layerMetrics.add(MetricEnum.INSTABILITY);
        //internal type relations
        layerMetrics.add(MetricEnum.NUMBER_OF_INTERNAL_TYPE_RELATIONS);
        //number of compilation units (SIZE)
        //not found in code or list
        //relational cohesion (Rc)
        layerMetrics.add(MetricEnum.RELATIONAL_COHESION);
        //types (Nc)
        layerMetrics.add(MetricEnum.NUMBER_OF_TYPES);
        //no physical project internal dependencies detected
        layerMetrics.add(MetricEnum.NO_DEPENCIES_DETECTED);
        //project internal/external
        layerMetrics.add(MetricEnum.PROJECT_INTERNAL);
        layerMetrics.add(MetricEnum.PROJECT_EXTERNAL);
        //abstract/concrete
        layerMetrics.add(MetricEnum.ABSTRACT);
        //accessible/not accessible
        layerMetrics.add(MetricEnum.ACCESSIBLE);
        //check actual dependencies against explicitly allowed dependencies
        //not found
        //usage detection of explicitly allowed dependencies                       
        //not found                                  
        //"cycles and cumulated participation of dependencies"
        //not found
        //"levelization"
        //not found
        // </editor-fold>    

        // <editor-fold defaultstate="collapsed" desc="Vertical slice metrics">
        verticalSliceMetrics = new LinkedList<MetricEnum>();
        //"contained subsystems and their inner layer dependencies
        verticalSliceMetrics.add(MetricEnum.NUMBER_OF_CONTAINED_SUBSYSTEMS);
        //"afferent (incoming) and efferent (outgoing) dependencies - subsystems causing these dependencies"
        //not found in code
        // </editor-fold>    

        // <editor-fold defaultstate="collapsed" desc="Subsystem metrics">
        subsystemMetrics = new LinkedList<MetricEnum>();
        //containing layer
        //containing vertical-slice
        //contained packages and their inner subsystem dependencies
        //afferent (incoming) and efferent (outgoing) dependencies - packages causing these dependencies
        //Not implemented subsystems
        subsystemMetrics.add(MetricEnum.NUMBER_OF_CONTAINED_PACKAGES);

        //common for the elements layer, subsystem and package        
        //abstract types (Na)
        subsystemMetrics.add(MetricEnum.NUMBER_OF_ABSTRACT_TYPES);
        //abstractness (A)
        subsystemMetrics.add(MetricEnum.ABSTRACTNESS);
        //accessible types
        subsystemMetrics.add(MetricEnum.NUMBER_OF_ACCESSIBLE_TYPES);
        //afferent (incoming) dependencies
        subsystemMetrics.add(MetricEnum.NUMBER_OF_INCOMING_DEPENDENCIES);
        //afferent coupling (Ca)
        subsystemMetrics.add(MetricEnum.AFFERENT_INCOMING_COUPLING);
        //assertions
        subsystemMetrics.add(MetricEnum.NUMBER_OF_ASSERTIONS);
        //average component dependency (ACD)
        subsystemMetrics.add(MetricEnum.AVERAGE_COMPONENT_DEPENDENCY);
        //cumulative component dependency (CCD)
        subsystemMetrics.add(MetricEnum.CUMULATIVE_COMPONENT_DEPENDENCY);
        //depends upon elements (incl. self)
        subsystemMetrics.add(MetricEnum.DEPENDS_UPON);
        //distance (D)
        subsystemMetrics.add(MetricEnum.DISTANCE);
        //efferent (outgoing) dependencies (internal and external)
        subsystemMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_DEPENDENCIES);
        //efferent coupling (Ce)
        subsystemMetrics.add(MetricEnum.EFFERENT_OUTGOING_COUPLING);
        //external type relations
        subsystemMetrics.add(MetricEnum.NUMBER_OF_EXTERNAL_TYPE_RELATIONS);
        //forbidden efferent (outgoing) dependencies
        subsystemMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_DEPENDENCIES);
        //instability (I)
        subsystemMetrics.add(MetricEnum.INSTABILITY);
        //internal type relations
        subsystemMetrics.add(MetricEnum.NUMBER_OF_INTERNAL_TYPE_RELATIONS);
        //number of compilation units (SIZE)
        //not found in code or list
        //relational cohesion (Rc)
        subsystemMetrics.add(MetricEnum.RELATIONAL_COHESION);
        //types (Nc)
        subsystemMetrics.add(MetricEnum.NUMBER_OF_TYPES);
        //no physical project internal dependencies detected
        subsystemMetrics.add(MetricEnum.NO_DEPENCIES_DETECTED);
        //project internal/external
        subsystemMetrics.add(MetricEnum.PROJECT_INTERNAL);
        subsystemMetrics.add(MetricEnum.PROJECT_EXTERNAL);
        //abstract/concrete
        subsystemMetrics.add(MetricEnum.ABSTRACT);
        //accessible/not accessible
        subsystemMetrics.add(MetricEnum.ACCESSIBLE);
        //check actual dependencies against explicitly allowed dependencies
        //not found
        //usage detection of explicitly allowed dependencies                       
        //not found                                  
        //"cycles and cumulated participation of dependencies"
        //not found
        //"levelization"
        //not found
        // </editor-fold>    

        // <editor-fold defaultstate="collapsed" desc="Package metrics">
        packageMetrics = new LinkedList<MetricEnum>();
        //common for the elements layer, subsystem and package
        packageMetrics.add(MetricEnum.NUMBER_OF_ABSTRACT_TYPES);
        packageMetrics.add(MetricEnum.ABSTRACTNESS);
        packageMetrics.add(MetricEnum.NUMBER_OF_ACCESSIBLE_TYPES);
        packageMetrics.add(MetricEnum.NUMBER_OF_INCOMING_DEPENDENCIES);
        packageMetrics.add(MetricEnum.AFFERENT_INCOMING_COUPLING);
        packageMetrics.add(MetricEnum.NUMBER_OF_ASSERTIONS);
        packageMetrics.add(MetricEnum.AVERAGE_COMPONENT_DEPENDENCY);
        packageMetrics.add(MetricEnum.CUMULATIVE_COMPONENT_DEPENDENCY);
        packageMetrics.add(MetricEnum.DEPENDS_UPON);
        packageMetrics.add(MetricEnum.DISTANCE);
        packageMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_DEPENDENCIES);
        packageMetrics.add(MetricEnum.EFFERENT_OUTGOING_COUPLING);
        packageMetrics.add(MetricEnum.NUMBER_OF_EXTERNAL_TYPE_RELATIONS);
        packageMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_DEPENDENCIES);
        packageMetrics.add(MetricEnum.INSTABILITY);
        packageMetrics.add(MetricEnum.NUMBER_OF_INTERNAL_TYPE_RELATIONS);
        //"number of compilation units (SIZE)"
        //not found in code or list
        packageMetrics.add(MetricEnum.RELATIONAL_COHESION);
        packageMetrics.add(MetricEnum.NUMBER_OF_TYPES);
        //no physical project internal dependencies detected
        packageMetrics.add(MetricEnum.NO_DEPENCIES_DETECTED);
        packageMetrics.add(MetricEnum.PROJECT_INTERNAL);
        packageMetrics.add(MetricEnum.PROJECT_EXTERNAL);
        packageMetrics.add(MetricEnum.ABSTRACT);
        packageMetrics.add(MetricEnum.ACCESSIBLE);
        //"check actual dependencies against explicitly allowed dependencies"
        //not found
        //"usage detection of explicitly allowed dependencies"
        //not found
        packageMetrics.add(MetricEnum.NUMBER_OF_PACKAGE_CYCLES);        
        //"cycles and cumulated participation of dependencies"
        //not found
        //"levelization"
        //not found

        //found in code only
        packageMetrics.add(MetricEnum.DEPTH_OF_PACKAGE_HIERARCHY);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Compilation unit metrics">        
        compilationUnitMetrics = new LinkedList<MetricEnum>();
        //containing package
        //attribute not metric  
        //contained types and their inner compilation unit dependencies
        //attribute not metric

        //abstract types
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_ABSTRACT_TYPES);
        //accessible types
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_ACCESSIBLE_TYPES);
        //afferent (incoming) dependencies
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_INCOMING_DEPENDENCIES);
        //assertions        
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_ASSERTIONS);
        //concrete types
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_CONCRETE_TYPES);
        //depends upon elements (incl. self)
        compilationUnitMetrics.add(MetricEnum.DEPENDS_UPON);
        //efferent (outgoing) dependencies (internal and external)
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_DEPENDENCIES);
        //forbidden efferent (outgoing) dependencies
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_DEPENDENCIES);
        //more external than internal relations per package exist
        compilationUnitMetrics.add(MetricEnum.MORE_PACKAGE_EXTERNAL_THAN_INTERNAL_RELATIONS_EXIST);  //boolean
        //the most external relations exist with package
        compilationUnitMetrics.add(MetricEnum.THE_MOST_EXTERNAL_RELATIONS_EXIST_WITH_PACKAGE);  //boolean        
        //total external package relations
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_PACKAGE_EXTERNAL_RELATIONS);
        //total internal package relations
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_PACKAGE_INTERNAL_RELATIONS);
        //types
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_TYPES);
        //no physical project internal dependencies detected        
        compilationUnitMetrics.add(MetricEnum.NO_INCOMING_DEPENCIES_DETECTED);
        compilationUnitMetrics.add(MetricEnum.NO_DEPENCIES_DETECTED);

        //abstract/concrete
        //not found
        //accessible/not accessible
        //not found
        //cycles and cumulated participation of dependencies
        //not found
        //levelization
        //not found
        //from code
        compilationUnitMetrics.add(MetricEnum.CONTAINS_ACCESSIBLE_TYPES_BUT_NO_INCOMING_OUTER_PACKAGE_DEPENDENCIES_EXIST);
        compilationUnitMetrics.add(MetricEnum.REFACTORED);
        compilationUnitMetrics.add(MetricEnum.PROJECT_INTERNAL);
        compilationUnitMetrics.add(MetricEnum.PROJECT_EXTERNAL);
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_DEPENDENCIES_TO_PROJECT_EXTERNAL);
        compilationUnitMetrics.add(MetricEnum.NUMBER_OF_INCOMING_DEPENDENCIES_PROJECT_EXTERNAL);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Type metrics">        
        typeMetrics = new LinkedList<MetricEnum>();
        //containing compilation unit
        //attribute not metric  
        //abstract/concrete
        typeMetrics.add(MetricEnum.ABSTRACT);
        //accessible/not accessible
        typeMetrics.add(MetricEnum.ACCESSIBLE);
        //abstract types
        typeMetrics.add(MetricEnum.NUMBER_OF_ABSTRACT_TYPES);
        //accessible types
        typeMetrics.add(MetricEnum.NUMBER_OF_ACCESSIBLE_TYPES);
        //afferent (incoming) dependencies
        typeMetrics.add(MetricEnum.NUMBER_OF_INCOMING_DEPENDENCIES);
        //efferent (outgoing) dependencies (internal and external)
        typeMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_DEPENDENCIES);
        //forbidden efferent (outgoing) dependencies
        typeMetrics.add(MetricEnum.NUMBER_OF_FORBIDDEN_OUTGOING_DEPENDENCIES);
        //interface
        typeMetrics.add(MetricEnum.INTERFACE);
        //no physical project internal dependencies detected        
        typeMetrics.add(MetricEnum.NO_INCOMING_DEPENCIES_DETECTED);
        typeMetrics.add(MetricEnum.NO_DEPENCIES_DETECTED);
        //number of childs (NOC)
        typeMetrics.add(MetricEnum.NUMBER_OF_CHILDREN);
        //DepthOfClassInheritance
        typeMetrics.add(MetricEnum.DEPTH_OF_CLASS_INHERITANCE);
        //DepthOfInterfaceInheritance
        typeMetrics.add(MetricEnum.DEPTH_OF_INTERFACE_INHERITANCE);
        //cycles and cumulated participation of dependencies
        //not found
        //levelization
        //not found

        //from code        
        typeMetrics.add(MetricEnum.NESTED);
        typeMetrics.add(MetricEnum.REFACTORED);
        typeMetrics.add(MetricEnum.PROJECT_INTERNAL);
        typeMetrics.add(MetricEnum.DEPENDS_UPON);
        typeMetrics.add(MetricEnum.NUMBER_OF_OUTGOING_DEPENDENCIES_TO_PROJECT_EXTERNAL);
        typeMetrics.add(MetricEnum.PROJECT_EXTERNAL);
        typeMetrics.add(MetricEnum.NUMBER_OF_INCOMING_DEPENDENCIES_PROJECT_EXTERNAL);
        typeMetrics.add(MetricEnum.EXTENDABLE);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Not implemented metrics">        
        notImplementedMetrics = new LinkedList<MetricEnum>();
        notImplementedMetrics.add(MetricEnum.NUMBER_OF_SKIP_NODES);
        notImplementedMetrics.add(MetricEnum.NUMBER_OF_IGNORE_NODES);
        notImplementedMetrics.add(MetricEnum.NUMBER_OF_REFACTORING_NODES);
        notImplementedMetrics.add(MetricEnum.NUMBER_OF_VERTICAL_SLICES_CYCLES);
        notImplementedMetrics.add(MetricEnum.NUMBER_OF_VERTICAL_SLICE_TANGLES);
        // </editor-fold>
    }

    public static String getDescription(MetricEnum metricEnum) throws DependometerException {
        MetricDefinition[] metrics = MetricDefinition.getMetricDefinitions();
        if (metrics == null) {
            throw new DependometerException("No metrics definition found");
        }
        for (MetricDefinition metricDefinition : metrics) {
            if (metricEnum.equals(getMetricEnum(metricDefinition))) {
                return metricDefinition.getDescription();
            }
        }
        return null;
    }

    private static MetricDefinition getMetricDefinition(MetricEnum metricEnum) throws DependometerException {
        MetricDefinition[] metrics = MetricDefinition.getMetricDefinitions();
        if (metrics == null) {
            throw new DependometerException("No metrics definition found");
        }
        for (MetricDefinition metricDefinition : metrics) {
            if (metricEnum.equals(getMetricEnum(metricDefinition))) {
                return metricDefinition;
            }
        }
        return null;
    }

    public static String buildID(String label) {
        String s[] = label.split(" ");
        String id = "";
        for (int i = 0; i < s.length; i++) {
            id += capitalize(s[i]);
        }
        id = id.replace("(", "_").replace(")", "").trim();
        return id.replace(">=", "MoreThen").replace("<=", "LessThen");
    }

    public static String capitalize(String s) {
        if (s.length() == 0) {
            return "";
        } else {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
    }

    public static String getMetricName(Metric metric) throws DependometerException {
        MetricEnum meEnum;
        String name = metric.getName();
        MetricEnumeration meEnumeration = MetricEnumeration.getMetricByName(name);
        if (meEnumeration != null) {
            //Dependometer metric
            meEnum = getDependometerMetric(meEnumeration);
        } else {
            //Dependometer metric which exists in Oceano
            name = metric.getAcronym();
            meEnum = getMetricEnum(name);
        }

        return getMetricName(meEnum);
    }

    public static String getMetricName(MetricIf metricIf) throws Exception {
        MetricEnum metricEnum = getMetricEnum(metricIf.getName());
        if (metricEnum != null) {
            return getMetricName(metricEnum);
        }
        throw new Exception("Metric not found: " + metricIf.getName());
    }

    public static String getMetricName(MetricEnum metric) {
        return metric.name();
    }

    public static MetricEnum getMetricEnum(MetricDefinition metricDefinition) {
        return getMetricEnum(metricDefinition.getName());
    }

    public static MetricEnum getMetricEnum(String displayName) {
        MetricEnum[] values = MetricEnum.values();
        for (int i = 0; i < values.length; i++) {
            MetricEnum metricEnum = values[i];
            if (displayName.compareToIgnoreCase(metricEnum.getDisplayName()) == 0) {
                return metricEnum;
            }
        }
        return null;
    }

    public static boolean isNumberMetric(MetricEnum metricEnum) throws DependometerException {
        MetricDefinition metric = getMetricDefinition(metricEnum);
        if (metric == null) {
            throw new DependometerException("Metric definition not found for " + metricEnum);
        }
        return TYPE_NUMBER.equals(metric.getValueType());
    }

    

    /**
     * Returns metrics that : result in number exist in Oceano with different
     * names
     *
     * @return
     * @throws DependometerException
     */
    public static List<MetricEnum> getValidProjectMetrics() throws DependometerException {
        List<MetricEnum> metrics = new LinkedList<MetricEnum>();

        for (MetricEnum metricEnum : getAllMetric()) {
            if (!isNumberMetric(metricEnum)) {
                continue;
            }
            if (!isProjectMetric(metricEnum) && !canBeProjectMetric(metricEnum)) {
                continue;
            }

            metrics.add(metricEnum);
        }

        return metrics;
    }

    public static List<MetricEnum> getValidPackageMetrics() throws DependometerException {
        List<MetricEnum> metrics = new LinkedList<MetricEnum>();
        for (MetricEnum metricEnum : getAllMetric()) {
            if (!isNumberMetric(metricEnum)) {
                continue;
            } else if (!isPackageMetric(metricEnum)) {
                continue;
            } else if (canBeProjectMetric(metricEnum)) {
                continue;
            }

            metrics.add(metricEnum);
        }

        return metrics;
    }

    public static List<MetricEnum> getValidCompilationUnitMetrics() throws DependometerException {
        List<MetricEnum> metrics = new LinkedList<MetricEnum>();
        for (MetricEnum metricEnum : getAllMetric()) {
            if (!isNumberMetric(metricEnum)) {
                continue;
            } else if (!isCompilationUnitMetric(metricEnum)) {
                continue;
            } else if (canBeProjectMetric(metricEnum)) {
                continue;
            }

            metrics.add(metricEnum);
        }
        return metrics;

    }

    public static List<MetricEnum> getValidTypeMetrics() throws DependometerException {
        List<MetricEnum> metrics = new LinkedList<MetricEnum>();

        for (MetricEnum metricEnum : getAllMetric()) {
            if (!isNumberMetric(metricEnum)) {
                continue;
            } else if (!isTypeMetric(metricEnum)) {
                continue;
            } else if (canBeProjectMetric(metricEnum)) {
                continue;
            }

            metrics.add(metricEnum);
        }

        return metrics;
    }

    private static Set<MetricEnum> getAllMetric() {
        Set<MetricEnum> metrics = new HashSet<MetricEnum>();

        metrics.addAll(projectMetrics);
        metrics.addAll(layerMetrics);
        metrics.addAll(subsystemMetrics);
        metrics.addAll(verticalSliceMetrics);
        metrics.addAll(packageMetrics);
        metrics.addAll(compilationUnitMetrics);
        metrics.addAll(typeMetrics);

        return metrics;
    }
}
