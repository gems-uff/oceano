/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.ostra.model.DataBaseSnapshot;
import br.uff.ic.oceano.core.dao.MetricValueDao;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.SoftwareProject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DanCastellani
 */
public class DataMiningService {

    private static final int PREFIX_GET_SIZE = 3;
    private static final int PREFIX_IS_SIZE = 2;
    private static final String PREFIX_IS = "is";
    private static final String PREFIX_GET = "get";
    private static final String SEPARADOR_VALORES = ", ";

    public static DataBaseSnapshot getDataBaseSnapshot() {
        throw new RuntimeException("Refatorar: DataMiningService.getDataBaseSnapshot()");
//        final ProjectDao projectDao = ObjectFactory.getObj(ProjectDaoImpl.class);
//        final MetricValueDao metricValueDao = ObjectFactory.getObj(MetricValueDaoImpl.class);
//
//        final Map<Revision, List<MetricValue>> metricasPorRevision = montaMapMetricas(metricValueDao);
//
//        List<Project> projects = projectDao.getMeasured();
//        final ArrayList<String> listaNomesAtributos = getProjectAttributeNames();
//        final ArrayList<String> listaInstancias = new ArrayList<String>();
//
//        for (Project project : projects) {
//            String s = getProjectAtributeValuesByAtributeNames(project, listaNomesAtributos) + getMetricasDoProjetoComSeparador(metricasPorRevision, revision);
//            listaInstancias.add(s);
//        }
//
//        atualizaNomesAtributosComNomesMetricas(listaNomesAtributos, metricasPorRevision);
//
//
//        DataBaseSnapshot dbSnapshot = new DataBaseSnapshot(listaInstancias, listaNomesAtributos);
//
//        return dbSnapshot;
    }

    public static String getValueByAtrtibuteName(SoftwareProject project, String attributeName) throws SecurityException {
        boolean getMethod = false;
        Method method = null;
        try {
            //tenta pegar o método com prefixo "get"
            method = project.getClass().getMethod(PREFIX_GET + attributeName, null);
            getMethod = true;
        } catch (NoSuchMethodException ex) {
            try {
                //como não é get, pega com "is", é um boolean.
                method = project.getClass().getMethod(PREFIX_IS + attributeName, null);
            } catch (NoSuchMethodException ex1) {
                Logger.getLogger(DataMiningService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (SecurityException ex1) {
            Logger.getLogger(DataMiningService.class.getName()).log(Level.SEVERE, null, ex1);
        }
        String retorno = null;
        try {
            retorno = "" + method.invoke(project, null);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DataMiningService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DataMiningService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(DataMiningService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private static Map<Revision, List<MetricValue>> montaMapMetricas(MetricValueDao metricValueDao) {
        final List<MetricValue> metricasExtraidas = metricValueDao.getAllSortedByConfigurationAndMetric();

        final Map<Revision, List<MetricValue>> map = new LinkedHashMap<Revision, List<MetricValue>>();

        for (MetricValue metricValue : metricasExtraidas) {
            //inicializa se necessário
            if (map.get(metricValue.getRevision()) == null) {
                map.put(metricValue.getRevision(), new ArrayList<MetricValue>());
            }
            map.get(metricValue.getRevision()).add(metricValue);
        }

        return map;
    }

    private static final DataBaseSnapshot getFakeSnapshot() {
        //fake
        ArrayList<String> listaInstancias = new ArrayList<String>();
        listaInstancias.add("13,Contains Cycles,false,Oceano,1.0 beta,dan");
        listaInstancias.add("14,Number of Classes,0,Oceano,1.0 beta,dan");
        listaInstancias.add("15,Number of Packages,0,Oceano,1.0 beta,dan");
        listaInstancias.add("16,Contains Cycles,false,Oceano,metricas desenv,dan");
        listaInstancias.add("17,Number of Classes,0,Oceano,metricas desenv,dan");
        listaInstancias.add("18,Number of Packages,0,Oceano,metricas desenv,dan");

        ArrayList<String> listaAtributos = new ArrayList<String>();
        listaAtributos.add("id");
        listaAtributos.add("Metrica");
        listaAtributos.add("Valor");
        listaAtributos.add("Projeto");
        listaAtributos.add("Release");
        listaAtributos.add("Usuário");

        DataBaseSnapshot fakeSnapshot = new DataBaseSnapshot(listaInstancias, listaAtributos);
        return fakeSnapshot;
    }

    public static ArrayList<String> getProjectAttributeNames() {
        ArrayList<String> attributeNames = new ArrayList<String>();

        for (Method method : SoftwareProject.class.getDeclaredMethods()) {
            if (isValid(method)) {
                String attributeName = getAttributeName(method);
                if (attributeName != null) {
                    attributeNames.add(attributeName);
                }
            }
        }

        Collections.sort(attributeNames);
        return attributeNames;
    }

    private static boolean isValid(Method method) {
        if (method.getModifiers() != Modifier.PUBLIC) {
            return false;
        }
        final String name = method.getName();
        if (name.startsWith(PREFIX_IS)) {
            return true;
        }
        if (name.startsWith(PREFIX_GET)) {
            return true;
        }
        return false;
    }

    private static String getAttributeName(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith(PREFIX_IS)) {
            return methodName.substring(PREFIX_IS_SIZE);
        } else if (methodName.startsWith(PREFIX_GET)) {
            return methodName.substring(PREFIX_GET_SIZE);
        }
        return null;
    }

    private static String getProjectAtributeValuesByAtributeNames(SoftwareProject project, ArrayList<String> listaNomesAtributos) {
        StringBuilder sb = new StringBuilder();
        for (String attributeName : listaNomesAtributos) {
            String retorno = SEPARADOR_VALORES + getValueByAtrtibuteName(project, attributeName);
            sb.append(retorno);
        }
        return sb.toString().substring(1);
    }

    private static String getMetricasDoProjetoComSeparador(Map<Revision, List<MetricValue>> metricasPorRevision, Revision revision) {
        StringBuilder sb = new StringBuilder();
        for (MetricValue metricValue : metricasPorRevision.get(revision)) {
            sb.append(SEPARADOR_VALORES).append(metricValue.getDoubleValue());
        }
        return sb.toString();
    }

    private static void atualizaNomesAtributosComNomesMetricas(ArrayList<String> listaNomesAtributos, Map<Revision, List<MetricValue>> metricasPorRevision) {
        if (metricasPorRevision.values().isEmpty()) {
            return;
        }

        List<MetricValue> metricValues = metricasPorRevision.values().iterator().next();
        for (MetricValue metricValue : metricValues) {
            listaNomesAtributos.add(metricValue.getMetric().getName());
        }
    }
}


