/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service.plano;

import br.uff.ic.oceano.core.exception.ExecutionPlanException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ostra.model.Task;
import br.uff.ic.oceano.core.service.MetricValueService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_SVNKit;
import br.uff.ic.oceano.core.tools.vcs.VCS;

/**
 *
 * @author DanCastellani
 */
public class ExtractMetricsPlan implements Plan {

    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private MetricValueService metricValueService = ObjectFactory.getObjectWithDataBaseDependencies(MetricValueService.class);
    private VCS svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);

    public void execute(Task task) throws ExecutionPlanException {
        System.out.println("************************************************************************************************************************");
        System.out.println("************************************************************************************************************************");
        final SoftwareProject project = task.getProject();

        System.out.println("verificando o que deve ser feito");

        if (mayCheckout(project)) {
            System.out.println("fazendo checkout do projeto");
            checkoutProject(project);
        }
        if (mayMeasure(project)) {
            System.out.println("extraindo metricas do projeto");
            extractAndSaveMetrics(task);
        }
        System.out.println("************************************************************************************************************************");
        System.out.println("************************************************************************************************************************");
    }

    private boolean mayCheckout(SoftwareProject project) {
        throw new RuntimeException("Este método deve ser refatorado!");
//        final int situation = project.getSituation();
//        return situation == Project.SITUATION_REGISTERED
//                || situation == Project.SITUATION_CHECKOUT_SCHEDULED
//                || situation == Project.SITUATION_CHECKING_OUT;
    }

    private boolean mayMeasure(final SoftwareProject project) {
        throw new RuntimeException("Este método deve ser refatorado!");
//        final int situation = project.getSituation();
//        return situation == Project.SITUATION_UNMEASURED || situation == Project.SITUATION_MEASUREMENT_SCHEDULED || situation == Project.SITUATION_MEASURING;
    }

    private void checkoutProject(SoftwareProject project) throws ExecutionPlanException {
        throw new RuntimeException("Este método deve ser refatorado!");

//        try {
//            System.out.println(">>>>>>>>>> chekout_ " + new Date());
//            projectService.checkingOut(project);
//
//            svn.doCheckout(project.getConfiguration());
//
//            System.out.println(">>>>>>>>>> chekout terminado _ " + new Date());
//            projectService.unmeasured(project);
//
//        } catch (VCSException ex) {
//            projectService.checkOutError(project);
//            throw new ExecutionPlanException(ex);
//        }
    }

    private void extractAndSaveMetrics(Task task) throws ExecutionPlanException {
        throw new RuntimeException("Este método deve ser refatorado!");
//        try {
//            System.out.println(">>>>>>>> extractAndSaveMetrics from task: " + task);
//            projectService.measuring(task.getProject());
//            final Project project = task.getProject();
//
//            List<MetricValue> metricsExtracted = new LinkedList<MetricValue>();
//            for (MetricManager metric : MetricsFactory.getInstance().getMetrics()) {
//                System.out.println("Extraindo métrica " + metric.getName());
//                metricsExtracted.add(metric.extractMetric(project.getConfiguration()));
//                System.out.println("Valor: " + metricsExtracted.get(metricsExtracted.size() - 1).getValue());
//            }
//
//            metricValueService.saveAll(metricsExtracted);
//            projectService.measured(task.getProject());
//
//        } catch (MetricException ex) {
//            projectService.measurementError(task.getProject());
//            throw new ExecutionPlanException(ex);
//        }
    }
}
