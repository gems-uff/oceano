package br.uff.ic.oceano.util;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.uff.ic.oceano.util;
//
//import br.uff.ic.oceano.core.exception.ExecutionPlanException;
//import br.uff.ic.oceano.core.factory.ObjectFactory;
//import br.uff.ic.oceano.ostra.model.Task;
//import br.uff.ic.oceano.core.service.TaskService;
//import br.uff.ic.oceano.core.service.plano.ExtractMetricsPlan;
//import br.uff.ic.oceano.core.service.plano.Plan;
//import java.util.Date;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author DanCastellani
// */
//public class ThreadTask extends Thread {
//
//    private Task task;
//    private TaskService taskService;
//    private Plan extracMetricsPlan;
//
//    public ThreadTask(Task task) {
//        this.task = task;
//        taskService = ObjectFactory.getObj(TaskService.class);
//        extracMetricsPlan = ObjectFactory.getObj(ExtractMetricsPlan.class);
//    }
//
//    @Override
//    public void run() {
//        try {
//            System.out.println("Executando tarefa: " + task);
//            task.setStartTime(new Date());
//            taskService.running(task);
//
//            executeByType(task);
//
//            System.out.println("Tarefa: " + task + " executada com sucesso.");
//
//            task.setFinishTime(new Date());
//            taskService.done(task);
//
//        } catch (ExecutionPlanException ex) {
//            taskService.problemExecution(task, ex);
//            Logger.getLogger(ThreadTask.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    /**
//     * @return the task
//     */
//    public Task getTask() {
//        return task;
//    }
//
//    private void executeByType(Task task) throws ExecutionPlanException {
//        System.out.println("++++++++ executeByType");
//        if (task.getType().equals(Task.TYPE_EXTRACT_METRIC)) {
//            System.out.println("++++++++++++++++++> Task.TYPE_EXTRACT_METRIC");
//            extracMetricsPlan.execute(task);
//        } else {
//            System.out.println("++++++++++++++++++> else");
//        }
//    }
//}
