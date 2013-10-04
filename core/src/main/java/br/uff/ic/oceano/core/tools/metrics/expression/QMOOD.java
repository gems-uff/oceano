/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.expression;

/**
 *
 * @author DanCastellani
 */
public class QMOOD {

    // --  METRICS ACRONYMS
    public static final String DSC = "DSC";
    public static final String NOH = "NOH";
    public static final String ANA = "ANA";
    public static final String DAM = "DAM";
    public static final String DCC = "DCC";
    public static final String CAM = "CAM";
    public static final String MOA = "MOA";
    public static final String MFA = "MFA";
    public static final String NOP = "NOP";
    public static final String CIS = "CIS";
    public static final String NOM = "NOM";
    // -- METRICS
    public static final String METRIC_DESIGN_SIZE_IN_CLASSES = "Design Size In Classes";
    public static final String METRIC_NUMBER_OF_HIERARCHIES = "Number Of Hierarchies";
    public static final String METRIC_AVERAGE_NUMBER_OF_ANCESTORS = "Average Number Of Ancestors";
    public static final String METRIC_DATA_ACCESS = "Data Access";
    public static final String METRIC_DIRECT_CLASS_COUPLING = "Direct Class Coupling";
    public static final String METRIC_COHESION_AMONG_METHODS_IN_CLASS = "Cohesion Among Methods In Class";
    public static final String METRIC_MEASURE_OF_AGGREGATION = "Measure Of Aggregation";
    public static final String METRIC_MEASURE_OF_FUNCTIONAL_ABSTRACTION = "Measure Of Functional Abstraction";
    public static final String METRIC_NUMBER_OF_POLYMORPHIC_METHODS = "Number Of Polymorphic Methods";
    public static final String METRIC_CLASS_INTERFACE_SIZE = "Class Interface Size";
    public static final String METRIC_NUMBER_OF_METHODS = "Number Of Methods";
    // -- DESIGN PROPERTIES
    public static final String DESIGN_SIZE = DSC;
    public static final String HIERARCHIES = NOM;
    public static final String ABSTRACTION = ANA;
    public static final String ENCAPSULATION = DAM;
    public static final String COUPLING = DCC;
    public static final String COHESION = CAM;
    public static final String COMPOSITION = MOA;
    public static final String INHERITANCE = MFA;
    public static final String POLYMORPHISM = NOP;
    public static final String MESSAGING = CIS;
    public static final String COMPLEXITY = NOM;
    // -- QUALITY ATTRIBUTES
    public static final String QUALITY_ATTRIBUTE_REUSABILITY = "-0.25*" + COUPLING + "+0.25*" + COHESION + "+0.5*" + MESSAGING + "+0.5*" + DESIGN_SIZE;
    public static final String QUALITY_ATTRIBUTE_FLEXIBILITY = "0.25*" + ENCAPSULATION + "-0.25*" + COUPLING + "+0.5*" + COMPOSITION + "+0.5*" + POLYMORPHISM;
    public static final String QUALITY_ATTRIBUTE_UNDERSTANDABILITY = "-0.33*" + ABSTRACTION + "+0.33*" + ENCAPSULATION + "-0.33*" + COUPLING + "+0.33*" + COHESION + "-0.33*" + POLYMORPHISM + "-0.33*" + COMPLEXITY + "-0.33*" + DESIGN_SIZE;
    public static final String QUALITY_ATTRIBUTE_FUNCTIONALITY = "0.12*" + COHESION + "+0.22*" + POLYMORPHISM + "+0.22*" + MESSAGING + "+0.22*" + DESIGN_SIZE + "+0.22*" + HIERARCHIES;
    public static final String QUALITY_ATTRIBUTE_EXTENDABILITY = "0.5*" + ABSTRACTION + "-0.5*" + COUPLING + "+0.5*" + INHERITANCE + "+0.5*" + POLYMORPHISM;
    public static final String QUALITY_ATTRIBUTE_EFFECTIVENESS = "0.2*" + ABSTRACTION + "+0.2*" + ENCAPSULATION + "+0.2*" + COMPOSITION + "+0.2*" + INHERITANCE + "+0.2*" + POLYMORPHISM;
    // -- QUALITY ATTRIBUTE NAMES
    public static final String QA_REUSABILITY = "Reusability";
    public static final String QA_FLEXIBILITY = "Flexibility";
    public static final String QA_UNDERSTANDABILITY = "Understandability";
    public static final String QA_FUNCTIONALITY = "Functionality";
    public static final String QA_EXTENDABILITY = "Extendability";
    public static final String QA_EFFECTIVENESS = "Effectiveness";
    //all metrics
    public static final String[] QMOOD_METRICS = {METRIC_DESIGN_SIZE_IN_CLASSES, METRIC_NUMBER_OF_HIERARCHIES, METRIC_AVERAGE_NUMBER_OF_ANCESTORS, METRIC_DATA_ACCESS,
        METRIC_DIRECT_CLASS_COUPLING, METRIC_COHESION_AMONG_METHODS_IN_CLASS, METRIC_MEASURE_OF_AGGREGATION, METRIC_MEASURE_OF_FUNCTIONAL_ABSTRACTION,
        METRIC_NUMBER_OF_POLYMORPHIC_METHODS, METRIC_CLASS_INTERFACE_SIZE, METRIC_NUMBER_OF_METHODS};
    // -- Quality Attributes
    public static final String[] QMOOD_QUALITY_ATTRIBUTES = {QA_REUSABILITY, QA_FLEXIBILITY, QA_UNDERSTANDABILITY, QA_FUNCTIONALITY, QA_EXTENDABILITY, QA_EFFECTIVENESS};
}
