/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.tools.datamining.util;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.ostra.model.DataBaseSnapshot;
import static br.uff.ic.oceano.ostra.controle.Constantes.*;
import br.uff.ic.oceano.ostra.discretizer.Discretizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DanCastellani
 */
public class DatabaseToArffService {

    private static final String RELATION = "@RELATION ";
    private static final String ATTRIBUTE = "@ATTRIBUTE ";
    private static final String DATA = "@DATA ";
    private static final String REAL = " REAL";
    private static final String NUMERIC = " NUMERIC";

    public static String dataBaseToARFF(DataBaseSnapshot dbs, List<Discretizer> discretizers) throws ServiceException {

        if (dbs.getInstancesSize() == 0) {
            return null;
        }
        Map<String, Discretizer> discretizersMap = initializeDiscretizers(dbs, discretizers);

        final List<String> instanciasFormatadas = formataDados(dbs.getInstances());

        final String dataBaseName = "\"Oceano " + dbs.getFormatedSnapshotTime() + "\"";
        final String header = getHeader(dbs.getAttributes(), instanciasFormatadas, dataBaseName, discretizersMap);
        final String data = getData(instanciasFormatadas);
        final String strARFF = header + data;

//        System.out.println("********************************* ARFF TO RETURN ************************************");
//        System.out.println(strARFF);
//        System.out.println("*************************************************************************************");
        return strARFF;
    }

    private static String getHeader(List<String> attributeNames, List<String> instanciasFormatadas, String dataBaseName, Map<String, Discretizer> discretizersMap) {
        List<StringTokenizer> valores = new ArrayList<StringTokenizer>(instanciasFormatadas.size());
        List<Set<String>> atributos = new ArrayList<Set<String>>(instanciasFormatadas.size());
//
        inicializaListaDeValores(instanciasFormatadas, valores);
//
        inicializaConjuntoDeValores(valores, atributos);
//
        preencheValoresDeAtributos(valores, atributos);

        final StringBuffer header = new StringBuffer();
        header.append(RELATION).append(dataBaseName).append("\n%\n");

        preencheCabecalhoComAtributos(attributeNames, atributos, header, discretizersMap);

        header.append("%\n");
        header.append("% Instancias: ").append(instanciasFormatadas.size()).append("\n");
        header.append("%\n");

        return header.toString();
    }

    private static String getData(List<String> instancias) {
        final StringBuffer data = new StringBuffer();
        data.append(DATA);
        data.append("\n%\n");
        for (String linha : instancias) {
            data.append(linha).append("\n");
        }
        return data.toString();
    }

    private static void inicializaConjuntoDeValores(List<StringTokenizer> valores, List<Set<String>> atributos) {
        //inicializa os conjuntos de valores de cada atributo
        for (int i = 0; i < valores.get(0).countTokens(); i++) {
            atributos.add(new HashSet<String>());
        }
    }

    private static void inicializaListaDeValores(List<String> instancias, List<StringTokenizer> valores) {
        //inicializa a lista de valores e atributos
        for (String instancia : instancias) {
            StringTokenizer st = new StringTokenizer(instancia, ARFF_VALUE_SEPARATOR);
            valores.add(st);
        }
    }

    private static void preencheCabecalhoComAtributos(List<String> names, List<Set<String>> atributos, final StringBuffer header, Map<String, Discretizer> discretizersMap) {
        //constroi o string de cabeçalho
        for (int numeroDoAtributo = 0; numeroDoAtributo < atributos.size(); numeroDoAtributo++) {
            //valores deste atributo
            final StringBuffer attributeValues = new StringBuffer();
            for (String valor : atributos.get(numeroDoAtributo)) {
                attributeValues.append(valor).append(ARFF_VALUE_SEPARATOR);
            }

            final int stringSize = attributeValues.toString().length();
            String attributeName = names.get(numeroDoAtributo);
            if (attributeName.contains(" ")) {
                attributeName = ARFF_STRING_DELIMITER + attributeName + ARFF_STRING_DELIMITER;
            }

            String attributeDeclarationValue = null;
            //Verify and Apply discretizer
//            System.out.println("Construindo cabeçalho do atributo: " + attributeName);
            if (discretizersMap.containsKey(attributeName.replace("\"", ""))) {
                Discretizer discretizer = discretizersMap.get(attributeName.replace("\"", ""));
                attributeDeclarationValue = " {" + discretizer.getHeaderDeclaration(attributeValues.toString().substring(0, stringSize - 1)) + "}";

            } else if (attributeName.startsWith(PREFIX_ATTRIBUTE_NUMBER)) {
                attributeDeclarationValue = NUMERIC;

            } else if (names.get(numeroDoAtributo).startsWith(PREFIX_DELTA_METRIC_AVARAGE)) {
                attributeDeclarationValue = REAL;

            } else if (names.get(numeroDoAtributo).startsWith(PREFIX_DELTA_METRIC_STANDARD_DEVIATON)) {
                attributeDeclarationValue = REAL;

            } else {
                attributeDeclarationValue = " {" + attributeValues.toString().substring(0, stringSize - 1) + "}";
            }

//            System.out.println("attributeDeclarationValue = " + attributeDeclarationValue);
            header.append(ATTRIBUTE).append(attributeName).append(attributeDeclarationValue).append("\n");
        }
    }

    private static void preencheValoresDeAtributos(List<StringTokenizer> valores, List<Set<String>> atributos) {
        //preenche os valores de cada atributo
        for (StringTokenizer stringTokenizer : valores) {
            int posicaoAtributo = 0;

            while (stringTokenizer.hasMoreTokens()) {
                final String valor = stringTokenizer.nextToken();
                atributos.get(posicaoAtributo).add(valor);

                posicaoAtributo++;
            }
        }
    }

    private static String preparaString(String s) {
        final String trimS = s.trim();
        if (trimS.contains(" ")) {
            return "\"" + trimS + "\"";
        } else {
            return trimS;
        }
    }

    private static List<String> formataDados(List<String> instancias) {
        List<String> instanciasFormatadas = new ArrayList<String>(instancias.size());

        StringTokenizer st;
        for (String linha : instancias) {
            st = new StringTokenizer(linha, ATTRIBUTE_SEPARATOR);

            StringBuilder sb = new StringBuilder();
            while (st.hasMoreTokens()) {
                final String s = st.nextToken();

                if (s.trim().equals("null")) {
                    sb.append(ATTRIBUTE_NOT_KNOWN_SYMBOL);
                } else {
                    sb.append(preparaString(s));
                }
                sb.append(ARFF_VALUE_SEPARATOR);
            }
            final String linhaFormada = sb.toString();
            instanciasFormatadas.add(linhaFormada.substring(0, linhaFormada.length() - 1));
        }

        return instanciasFormatadas;
    }

    /**
     * Initializes the discretizer's list validating it and updating the attribute's names when necessary.
     * @param discretizers
     * @throws ServiceException
     */
    private static Map<String, Discretizer> initializeDiscretizers(DataBaseSnapshot dbs, List<Discretizer> discretizers) throws ServiceException {
        Map<String, Discretizer> discretizersMap = new HashMap<String, Discretizer>();
        if (discretizers == null) {
            return discretizersMap;
        }

        for (Discretizer discretizer : discretizers) {
            //validate
            if (!dbs.getAttributes().contains(discretizer.getAttributeTarget())) {
                final String msg = "Attribute target not known " + discretizer.getAttributeTarget() + " of " + discretizer.getClass().getCanonicalName();
                Logger.getLogger(DatabaseToArffService.class.getName()).log(Level.WARNING, null);
            }
            //insert into map
            discretizersMap.put(discretizer.getAttributeTarget(), discretizer);
        }

        return discretizersMap;
    }
}
