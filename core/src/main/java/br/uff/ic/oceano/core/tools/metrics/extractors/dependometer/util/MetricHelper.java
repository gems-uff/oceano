/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util;

import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.DependometerException;
import br.uff.ic.oceano.util.NumberUtil;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import com.valtech.source.dependometer.app.core.metrics.MetricDefinition;
import static com.valtech.source.dependometer.app.core.provider.MetricDefinitionIf.TYPE_NUMBER;
import com.valtech.source.dependometer.app.core.provider.MetricIf;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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
        oceano2dependometer.put(MetricEnumeration.DSC, MetricEnum.NumberOfTypes);
                
        //ANA (QMOOD.METRIC_AVERAGE_NUMBER_OF_ANCESTORS)
        //needs to be calculated
        oceano2dependometer.put(MetricEnumeration.ANA, MetricEnum.DepthOfClassInheritance);
        
        //NOH (QMOOD.METRIC_NUMBER_OF_HIERARCHIES)
        //needs to be calculated
        oceano2dependometer.put(MetricEnumeration.NOH, MetricEnum.DepthOfClassInheritance);

        //Package metrics
        //package to package
        oceano2dependometer.put(MetricEnumeration.RMA, MetricEnum.Abstractness);

        //File metrics
        //file to CompilationUnit (also package, layer, subsystem)
        oceano2dependometer.put(MetricEnumeration.DCC, MetricEnum.DependsUpon);

        //no mapping
        //CIS(QMOOD.METRIC_CLASS_INTERFACE_SIZE)- MetricEnum.Interface is boolean
        //CAM(QMOOD.METRIC_COHESION_AMONG_METHODS_IN_CLASS) -
        //ACC("Cyclomatic Complexity")

    }

    private static void initCalculable() {
        canBeProjectMetrics = new LinkedList<MetricEnum>();

        //adding compilation units number of types
        canBeProjectMetrics.add(MetricEnum.NumberOfTypes);
        
        //average of DepthOfClassInheritance is the same of MetricEnumeration.ANA
        canBeProjectMetrics.add(MetricEnum.DepthOfClassInheritance);        
        
        
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
        projectMetrics.add(MetricEnum.AverageComponentDependency);
        projectMetrics.add(MetricEnum.CumulativeComponentDependency);
        projectMetrics.add(MetricEnum.CumulativeComponentDependencyForBalancedBinaryTree);
        projectMetrics.add(MetricEnum.CumulativeComponentDependencyForCyclicallyDependentGraph);
        projectMetrics.add(MetricEnum.NormalizedCumulativeComponentDependency);

        //Number of compilation units is SIZE or number of files
        //
        //internal are those who passed the filter and external the rest
        //so NumberOfProjectInternalCompilationUnits means SIZE
        projectMetrics.add(MetricEnum.NumberOfProjectExternalCompilationUnits);
        projectMetrics.add(MetricEnum.NumberOfProjectInternalCompilationUnits);
        //same as NumberOfProjectInternalCompilationUnits according to dependometer-core code
        projectMetrics.add(MetricEnum.NumberOfComponents);

        //
        projectMetrics.add(MetricEnum.PercentageOfProjectInternalLayersWithARelationalCohesionGreaterThanOne);
        projectMetrics.add(MetricEnum.PercentageOfProjectInternalPackagesWithARelationalCohesionGreaterThanOne);
        projectMetrics.add(MetricEnum.PercentageOfProjectInternalSubsystemsWithARelationalCohesionGreaterThanOne);
        projectMetrics.add(MetricEnum.PercentageOfProjectInternalVerticalSlicesWithARelationalCohesionGreaterThanOne);

        projectMetrics.add(MetricEnum.NumberOfAssertions);
        projectMetrics.add(MetricEnum.AverageUsageOfAssertionsPerClass);

        //Metric described as "total number of efferent package dependencies" in docs
        projectMetrics.add(MetricEnum.NumberOfOutgoingDependencies);
        //Metric described as "total number of forbidden efferent package dependencies"
        projectMetrics.add(MetricEnum.NumberOfForbiddenOutgoingDependencies);
        //Metric described as "total number of project internal efferent package dependencies"
        projectMetrics.add(MetricEnum.NumberOfOutgoingDependenciesToProjectExternal);

        //undocumented
        //determined by dependometer-core code
        projectMetrics.add(MetricEnum.NumberOfProjectInternalLayers);
        projectMetrics.add(MetricEnum.NumberOfProjectInternalPackages);
        projectMetrics.add(MetricEnum.NumberOfProjectInternalSubsystems);
        projectMetrics.add(MetricEnum.NumberOfProjectInternalTypes);
        projectMetrics.add(MetricEnum.NumberOfProjectInternalVerticalSlices);
        projectMetrics.add(MetricEnum.NumberOfProjectExternalLayers);
        projectMetrics.add(MetricEnum.NumberOfProjectExternalPackages);
        projectMetrics.add(MetricEnum.NumberOfProjectExternalSubsystems);
        projectMetrics.add(MetricEnum.NumberOfProjectExternalTypes);
        projectMetrics.add(MetricEnum.CyclesExistBetweenProjectInternalCompilationUnits);
        projectMetrics.add(MetricEnum.CyclesExistBetweenProjectInternalLayers);
        projectMetrics.add(MetricEnum.CyclesExistBetweenProjectInternalPackages);
        projectMetrics.add(MetricEnum.CyclesExistBetweenProjectInternalSubsystem);
        projectMetrics.add(MetricEnum.CyclesExistBetweenProjectInternalTypes);
        projectMetrics.add(MetricEnum.CyclesExistBetweenProjectInternalVerticalSlices);
        projectMetrics.add(MetricEnum.MaxDepthOfPackageHierarchy);
        projectMetrics.add(MetricEnum.MaxDepthOfTypeInheritance);
        projectMetrics.add(MetricEnum.NumberOfAllowedOutgoingLayerDependencies);
        projectMetrics.add(MetricEnum.NumberOfAllowedOutgoingPackageDependencies);
        projectMetrics.add(MetricEnum.NumberOfAllowedOutgoingSubsystemDependencies);
        projectMetrics.add(MetricEnum.NumberOfCompilationUnitCycles);
        projectMetrics.add(MetricEnum.AfferentIncomingCouplingProjectExternal);
        projectMetrics.add(MetricEnum.NumberOfForbiddenOutgoingCompilationUnitDependencies);
        projectMetrics.add(MetricEnum.NumberOfForbiddenOutgoingLayerDependencies);
        projectMetrics.add(MetricEnum.NumberOfForbiddenOutgoingPackageDependencies);
        projectMetrics.add(MetricEnum.NumberOfForbiddenOutgoingSubsystemDependencies);
        projectMetrics.add(MetricEnum.NumberOfForbiddenOutgoingTypeDependencies);
        projectMetrics.add(MetricEnum.NumberOfForbiddenOutgoingVerticalSliceDependencies);
        projectMetrics.add(MetricEnum.NumberOfNotAssignedPackages);
        projectMetrics.add(MetricEnum.NumberOfNotImplementedSubsystems);
        projectMetrics.add(MetricEnum.NumberOfOutgoingCompilationUnitDependencies);
        projectMetrics.add(MetricEnum.NumberOfOutgoingLayerDependencies);
        projectMetrics.add(MetricEnum.NumberOfOutgoingPackageDependencies);
        projectMetrics.add(MetricEnum.NumberOfOutgoingSubsystemDependencies);
        projectMetrics.add(MetricEnum.NumberOfOutgoingTypeDependencies);
        projectMetrics.add(MetricEnum.NumberOfOutgoingVerticalSliceDependencies);
        projectMetrics.add(MetricEnum.NumberOfPackageCycles);
        projectMetrics.add(MetricEnum.NumberOfTypeCycles);
        projectMetrics.add(MetricEnum.NumberOfSubsystemCycles);
        projectMetrics.add(MetricEnum.NumberOfLayerCycles);
        projectMetrics.add(MetricEnum.AverageUsageOfAssertionsPerClass);
        projectMetrics.add(MetricEnum.NumberOfTypeTangles);
        projectMetrics.add(MetricEnum.NumberOfCompilationUnitTangles);
        projectMetrics.add(MetricEnum.NumberOfSubsystemTangles);
        projectMetrics.add(MetricEnum.NumberOfPackageTangles);
        projectMetrics.add(MetricEnum.NumberOfLayerTangles);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Layer metrics">
        layerMetrics = new LinkedList<MetricEnum>();
        //"contained subsystems and their inner layer dependencies
        layerMetrics.add(MetricEnum.NumberOfContainedSubsystems);
        //"afferent (incoming) and efferent (outgoing) dependencies - subsystems causing these dependencies"
        //not found in code        

        //common for the elements layer, subsystem and package
        //abstract types (Na)
        layerMetrics.add(MetricEnum.NumberOfAbstractTypes);
        //abstractness (A)
        layerMetrics.add(MetricEnum.Abstractness);
        //accessible types
        layerMetrics.add(MetricEnum.NumberOfAccessibleTypes);
        //afferent (incoming) dependencies
        layerMetrics.add(MetricEnum.NumberOfIncomingDependencies);
        //afferent coupling (Ca)
        layerMetrics.add(MetricEnum.AfferentIncomingCoupling);
        //assertions
        layerMetrics.add(MetricEnum.NumberOfAssertions);
        //average component dependency (ACD)
        layerMetrics.add(MetricEnum.AverageComponentDependency);
        //cumulative component dependency (CCD)
        layerMetrics.add(MetricEnum.CumulativeComponentDependency);
        //depends upon elements (incl. self)
        layerMetrics.add(MetricEnum.DependsUpon);
        //distance (D)
        layerMetrics.add(MetricEnum.Distance);
        //efferent (outgoing) dependencies (internal and external)
        layerMetrics.add(MetricEnum.NumberOfOutgoingDependencies);
        //efferent coupling (Ce)
        layerMetrics.add(MetricEnum.EfferentOutgoingCoupling);
        //external type relations
        layerMetrics.add(MetricEnum.NumberOfExternalTypeRelations);
        //forbidden efferent (outgoing) dependencies
        layerMetrics.add(MetricEnum.NumberOfForbiddenOutgoingDependencies);
        //instability (I)
        layerMetrics.add(MetricEnum.Instability);
        //internal type relations
        layerMetrics.add(MetricEnum.NumberOfInternalTypeRelations);
        //number of compilation units (SIZE)
        //not found in code or list
        //relational cohesion (Rc)
        layerMetrics.add(MetricEnum.RelationalCohesion);
        //types (Nc)
        layerMetrics.add(MetricEnum.NumberOfTypes);
        //no physical project internal dependencies detected
        layerMetrics.add(MetricEnum.NoDepenciesDetected);
        //project internal/external
        layerMetrics.add(MetricEnum.ProjectInternal);
        layerMetrics.add(MetricEnum.ProjectExternal);
        //abstract/concrete
        layerMetrics.add(MetricEnum.Abstract);
        //accessible/not accessible
        layerMetrics.add(MetricEnum.Accessible);
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
        verticalSliceMetrics.add(MetricEnum.NumberOfContainedSubsystems);
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
        subsystemMetrics.add(MetricEnum.NumberOfContainedPackages);

        //common for the elements layer, subsystem and package        
        //abstract types (Na)
        subsystemMetrics.add(MetricEnum.NumberOfAbstractTypes);
        //abstractness (A)
        subsystemMetrics.add(MetricEnum.Abstractness);
        //accessible types
        subsystemMetrics.add(MetricEnum.NumberOfAccessibleTypes);
        //afferent (incoming) dependencies
        subsystemMetrics.add(MetricEnum.NumberOfIncomingDependencies);
        //afferent coupling (Ca)
        subsystemMetrics.add(MetricEnum.AfferentIncomingCoupling);
        //assertions
        subsystemMetrics.add(MetricEnum.NumberOfAssertions);
        //average component dependency (ACD)
        subsystemMetrics.add(MetricEnum.AverageComponentDependency);
        //cumulative component dependency (CCD)
        subsystemMetrics.add(MetricEnum.CumulativeComponentDependency);
        //depends upon elements (incl. self)
        subsystemMetrics.add(MetricEnum.DependsUpon);
        //distance (D)
        subsystemMetrics.add(MetricEnum.Distance);
        //efferent (outgoing) dependencies (internal and external)
        subsystemMetrics.add(MetricEnum.NumberOfOutgoingDependencies);
        //efferent coupling (Ce)
        subsystemMetrics.add(MetricEnum.EfferentOutgoingCoupling);
        //external type relations
        subsystemMetrics.add(MetricEnum.NumberOfExternalTypeRelations);
        //forbidden efferent (outgoing) dependencies
        subsystemMetrics.add(MetricEnum.NumberOfForbiddenOutgoingDependencies);
        //instability (I)
        subsystemMetrics.add(MetricEnum.Instability);
        //internal type relations
        subsystemMetrics.add(MetricEnum.NumberOfInternalTypeRelations);
        //number of compilation units (SIZE)
        //not found in code or list
        //relational cohesion (Rc)
        subsystemMetrics.add(MetricEnum.RelationalCohesion);
        //types (Nc)
        subsystemMetrics.add(MetricEnum.NumberOfTypes);
        //no physical project internal dependencies detected
        subsystemMetrics.add(MetricEnum.NoDepenciesDetected);
        //project internal/external
        subsystemMetrics.add(MetricEnum.ProjectInternal);
        subsystemMetrics.add(MetricEnum.ProjectExternal);
        //abstract/concrete
        subsystemMetrics.add(MetricEnum.Abstract);
        //accessible/not accessible
        subsystemMetrics.add(MetricEnum.Accessible);
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
        packageMetrics.add(MetricEnum.NumberOfAbstractTypes);
        packageMetrics.add(MetricEnum.Abstractness);
        packageMetrics.add(MetricEnum.NumberOfAccessibleTypes);
        packageMetrics.add(MetricEnum.NumberOfIncomingDependencies);
        packageMetrics.add(MetricEnum.AfferentIncomingCoupling);
        packageMetrics.add(MetricEnum.NumberOfAssertions);
        packageMetrics.add(MetricEnum.AverageComponentDependency);
        packageMetrics.add(MetricEnum.CumulativeComponentDependency);
        packageMetrics.add(MetricEnum.DependsUpon);
        packageMetrics.add(MetricEnum.Distance);
        packageMetrics.add(MetricEnum.NumberOfOutgoingDependencies);
        packageMetrics.add(MetricEnum.EfferentOutgoingCoupling);
        packageMetrics.add(MetricEnum.NumberOfExternalTypeRelations);
        packageMetrics.add(MetricEnum.NumberOfForbiddenOutgoingDependencies);
        packageMetrics.add(MetricEnum.Instability);
        packageMetrics.add(MetricEnum.NumberOfInternalTypeRelations);
        //"number of compilation units (SIZE)"
        //not found in code or list
        packageMetrics.add(MetricEnum.RelationalCohesion);
        packageMetrics.add(MetricEnum.NumberOfTypes);
        //no physical project internal dependencies detected
        packageMetrics.add(MetricEnum.NoDepenciesDetected);
        packageMetrics.add(MetricEnum.ProjectInternal);
        packageMetrics.add(MetricEnum.ProjectExternal);
        packageMetrics.add(MetricEnum.Abstract);
        packageMetrics.add(MetricEnum.Accessible);
        //"check actual dependencies against explicitly allowed dependencies"
        //not found
        //"usage detection of explicitly allowed dependencies"
        //not found
        packageMetrics.add(MetricEnum.NumberOfPackageCycles);
        packageMetrics.add(MetricEnum.DepthOfPackageHierarchy);
        //"cycles and cumulated participation of dependencies"
        //not found
        //"levelization"
        //not found

        //found in code only
        packageMetrics.add(MetricEnum.DepthOfPackageHierarchy);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Compilation unit metrics">        
        compilationUnitMetrics = new LinkedList<MetricEnum>();
        //containing package
        //attribute not metric  
        //contained types and their inner compilation unit dependencies
        //attribute not metric

        //abstract types
        compilationUnitMetrics.add(MetricEnum.NumberOfAbstractTypes);
        //accessible types
        compilationUnitMetrics.add(MetricEnum.NumberOfAccessibleTypes);
        //afferent (incoming) dependencies
        compilationUnitMetrics.add(MetricEnum.NumberOfIncomingDependencies);
        //assertions        
        compilationUnitMetrics.add(MetricEnum.NumberOfAssertions);
        //concrete types
        compilationUnitMetrics.add(MetricEnum.NumberOfConcreteTypes);
        //depends upon elements (incl. self)
        compilationUnitMetrics.add(MetricEnum.DependsUpon);
        //efferent (outgoing) dependencies (internal and external)
        compilationUnitMetrics.add(MetricEnum.NumberOfOutgoingDependencies);
        //forbidden efferent (outgoing) dependencies
        compilationUnitMetrics.add(MetricEnum.NumberOfForbiddenOutgoingDependencies);
        //more external than internal relations per package exist
        compilationUnitMetrics.add(MetricEnum.MorePackageExternalThanInternalRelationsExist);  //boolean
        //the most external relations exist with package
        compilationUnitMetrics.add(MetricEnum.TheMostExternalRelationsExistWithPackage);  //boolean        
        //total external package relations
        compilationUnitMetrics.add(MetricEnum.NumberOfPackageExternalRelations);
        //total internal package relations
        compilationUnitMetrics.add(MetricEnum.NumberOfPackageInternalRelations);
        //types
        compilationUnitMetrics.add(MetricEnum.NumberOfTypes);
        //no physical project internal dependencies detected        
        compilationUnitMetrics.add(MetricEnum.NoIncomingDepenciesDetected);
        compilationUnitMetrics.add(MetricEnum.NoDepenciesDetected);

        //abstract/concrete
        //not found
        //accessible/not accessible
        //not found
        //cycles and cumulated participation of dependencies
        //not found
        //levelization
        //not found
        //from code
        compilationUnitMetrics.add(MetricEnum.ContainsAccessibleTypesButNoIncomingOuterPackageDependenciesExist);
        compilationUnitMetrics.add(MetricEnum.Refactored);
        compilationUnitMetrics.add(MetricEnum.ProjectInternal);
        compilationUnitMetrics.add(MetricEnum.ProjectExternal);
        compilationUnitMetrics.add(MetricEnum.NumberOfOutgoingDependenciesToProjectExternal);
        compilationUnitMetrics.add(MetricEnum.NumberOfIncomingDependenciesProjectExternal);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Type metrics">        
        typeMetrics = new LinkedList<MetricEnum>();
        //containing compilation unit
        //attribute not metric  
        //abstract/concrete
        typeMetrics.add(MetricEnum.Abstract);
        //accessible/not accessible
        typeMetrics.add(MetricEnum.Accessible);
        //abstract types
        typeMetrics.add(MetricEnum.NumberOfAbstractTypes);
        //accessible types
        typeMetrics.add(MetricEnum.NumberOfAccessibleTypes);
        //afferent (incoming) dependencies
        typeMetrics.add(MetricEnum.NumberOfIncomingDependencies);
        //efferent (outgoing) dependencies (internal and external)
        typeMetrics.add(MetricEnum.NumberOfOutgoingDependencies);
        //forbidden efferent (outgoing) dependencies
        typeMetrics.add(MetricEnum.NumberOfForbiddenOutgoingDependencies);
        //interface
        typeMetrics.add(MetricEnum.Interface);
        //no physical project internal dependencies detected        
        typeMetrics.add(MetricEnum.NoIncomingDepenciesDetected);
        typeMetrics.add(MetricEnum.NoDepenciesDetected);
        //number of childs (NOC)
        typeMetrics.add(MetricEnum.NumberOfChildren);
        //DepthOfClassInheritance
        typeMetrics.add(MetricEnum.DepthOfClassInheritance);
        //DepthOfInterfaceInheritance
        typeMetrics.add(MetricEnum.DepthOfInterfaceInheritance);
        //cycles and cumulated participation of dependencies
        //not found
        //levelization
        //not found

        //from code        
        typeMetrics.add(MetricEnum.Nested);
        typeMetrics.add(MetricEnum.Refactored);
        typeMetrics.add(MetricEnum.ProjectInternal);
        typeMetrics.add(MetricEnum.DependsUpon);
        typeMetrics.add(MetricEnum.NumberOfOutgoingDependenciesToProjectExternal);
        typeMetrics.add(MetricEnum.ProjectExternal);
        typeMetrics.add(MetricEnum.NumberOfIncomingDependenciesProjectExternal);
        typeMetrics.add(MetricEnum.Extendable);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Not implemented metrics">        
        notImplementedMetrics = new LinkedList<MetricEnum>();
        notImplementedMetrics.add(MetricEnum.NumberOfSkipNodes);
        notImplementedMetrics.add(MetricEnum.NumberOfIgnoreNodes);
        notImplementedMetrics.add(MetricEnum.NumberOfRefactoringNodes);
        notImplementedMetrics.add(MetricEnum.NumberOfVerticalSlicesCycles);
        notImplementedMetrics.add(MetricEnum.NumberOfVerticalSliceTangles);
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

    private static List<MetricEnum> getAllMetric() {
        List<MetricEnum> metrics = new LinkedList<MetricEnum>();

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
