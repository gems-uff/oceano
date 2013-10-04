/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util;

import br.uff.ic.oceano.core.exception.OceanoCoreException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.BranchingMetric;
import br.uff.ic.oceano.core.model.BranchingModel;
import br.uff.ic.oceano.core.service.BranchingMetricService;
import br.uff.ic.oceano.core.service.BranchingModelService;
import br.uff.ic.oceano.core.util.DefaultDatabaseLoader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafaelss
 */
public class CargaDefaultWeb {

    private static BranchingModelService branchingModelService = ObjectFactory.getObjectWithDataBaseDependencies(BranchingModelService.class);
    private static BranchingMetricService branchingMetricService = ObjectFactory.getObjectWithDataBaseDependencies(BranchingMetricService.class);

    public static void insertDefaultData() {
        try {
            if (!DefaultDatabaseLoader.isDefaultDataInserted()) {
                DefaultDatabaseLoader.insertDefaultData();
            }
            insertBranchingModels();
            insertBranchingMetrics();
        } catch (OceanoCoreException ex) {
            ex.printStackTrace();
        }
    }

    private static void insertBranchingModels() {
        System.out.println("Inserindo Estratégias de Ramificação...");
        BranchingModel branchingModel;

        branchingModel = new BranchingModel();
        branchingModel.setName("In Cascade");
        branchingModel.setDirectionMerge(1);
        branchingModelService.save(branchingModel);

        branchingModel = new BranchingModel();
        branchingModel.setName("By Customization");
        branchingModel.setDirectionMerge(1);
        branchingModelService.save(branchingModel);

        branchingModel = new BranchingModel();
        branchingModel.setName("In Series");
        branchingModel.setDirectionMerge(-1);
        branchingModelService.save(branchingModel);

        branchingModel = new BranchingModel();
        branchingModel.setName("By Requests");
        branchingModel.setDirectionMerge(-1);
        branchingModelService.save(branchingModel);

        branchingModel = new BranchingModel();
        branchingModel.setName("By Components");
        branchingModel.setDirectionMerge(-1);
        branchingModelService.save(branchingModel);

        branchingModel = new BranchingModel();
        branchingModel.setName("By Developer");
        branchingModel.setDirectionMerge(0);
        branchingModelService.save(branchingModel);

        branchingModel = new BranchingModel();
        branchingModel.setName("By Subprojects");
        branchingModel.setDirectionMerge(0);
        branchingModelService.save(branchingModel);

        System.out.println("    ok");
    }

    private static void insertBranchingMetrics() {
        System.out.println("Inserindo Métricas de Ramificação...");

        BranchingMetric branchingMetric = new BranchingMetric();
        branchingMetric.setName("Amount of Artifacts Modified in the Mainline");
        branchingMetricService.save(branchingMetric);

        branchingMetric = new BranchingMetric();
        branchingMetric.setName("Amount of Artifacts Modified in the Branch");
        branchingMetricService.save(branchingMetric);

        branchingMetric = new BranchingMetric();
        branchingMetric.setName("Amount of Artifacts Modified in Common");
        branchingMetricService.save(branchingMetric);

        branchingMetric = new BranchingMetric();
        branchingMetric.setName("Recall by Amount of Artifacts (%)");
        branchingMetricService.save(branchingMetric);

        branchingMetric = new BranchingMetric();
        branchingMetric.setName("Precison by Amount of Artifacts (%)");
        branchingMetricService.save(branchingMetric);

        branchingMetric = new BranchingMetric();
        branchingMetric.setName("Amount of Lines Differents in the Mainline");
        branchingMetricService.save(branchingMetric);

        branchingMetric = new BranchingMetric();
        branchingMetric.setName("Amount of Lines Differents in the Branch");
        branchingMetricService.save(branchingMetric);

        branchingMetric = new BranchingMetric();
        branchingMetric.setName("Amount of Physical Conflicts");
        branchingMetricService.save(branchingMetric);

        branchingMetric = new BranchingMetric();
        branchingMetric.setName("Amount of Syntactic Conflicts");
        branchingMetricService.save(branchingMetric);

        branchingMetric = new BranchingMetric();
        branchingMetric.setName("Amount of Semantic Conflicts");
        branchingMetricService.save(branchingMetric);

        System.out.println("    ok");
    }

    public static boolean isDefaultDataInserted() {
        List buffer;
        boolean result = DefaultDatabaseLoader.isDefaultDataInserted();

        buffer = branchingModelService.getAll();
        result &= (buffer != null && !buffer.isEmpty());

        buffer = branchingMetricService.getAll();
        result &= (buffer != null && !buffer.isEmpty());

        return result;
    }
}
