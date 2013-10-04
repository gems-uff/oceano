/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.controle;

import br.uff.ic.oceano.util.SystemUtil;

/**
 *- * @author DanCastellani
 */
public final class Constantes {

// ==========================  Ostra General ===================================
    public static boolean SHOW_OUTPUT = true;
    public static boolean SHOW_OUTPUT_CLI = false;
    public static boolean SHOW_OUTPUT_COMPILATION = false;
// ==========================  Source Code Constants ===========================
    public static final String BASE_SOURCE_CODE_PATH = "checkouts" + SystemUtil.FILESEPARATOR;
    public static final String DOT_CLASS = ".class";
    public static final String DOT_JAVA = ".java";
//
// ==========================  Discretizer =====================================
    public static final String REAL = "real";
    public static final String NUMERIC = "numeric";
    public static final String NOMINAL = "{";
//
// ==========================  Data Mining =====================================
    public static final boolean VERBOSE_MODE = true;
    public static final double SUPORTE_MIN_PADRAO = 0.6;
    public static final double CONFIANCA_MIN_PADRAO = 0.6;
    public static final int MAX_RULES = 5000;
//
// ==========================  Database & ARFF =================================
    public static final String PREFIX_ATTRIBUTE_NUMBER = "#";
    public static final String PREFIX_DELTA_METRIC_AVARAGE = "dAvg-";
    public static final String PREFIX_DELTA_METRIC_STANDARD_DEVIATON = "dSd-";
    public static final String ARFF_EXTENSION = ".ARFF";
    public static final String ARFF_VALUE_SEPARATOR = ",";
    public static final String ARFF_STRING_DELIMITER = "\"";
    public static final String DELIMITADOR_PADRAO_SQL = ",";
    public static final String ATTRIBUTE_SEPARATOR = "|";
    public static final String ATTRIBUTE_NOT_KNOWN_SYMBOL = "?";
    public static final int NUMBER_OF_DECIMAL_NUMBERS = 2;

}
