package br.uff.ic.oceano.core.service;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.uff.ic.oceano.service;
//
//import br.uff.ic.oceano.core.dao.TaskDao;
//import br.uff.ic.oceano.core.dao.impl.TaskDaoImpl;
//import br.uff.ic.oceano.core.exception.ExecutionPlanException;
//import br.uff.ic.oceano.core.factory.ObjectFactory;
//import br.uff.ic.oceano.ostra.model.Task;
//import br.uff.ic.oceano.core.service.controletransacao.Transacional;
//import br.uff.ic.oceano.util.ThreadTask;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// *
// * @author daniel
// */
//public class TaskService {
//
//    private static Map<ThreadTask, Boolean> mapTasks = new ConcurrentHashMap<ThreadTask, Boolean>();
//    private static final Map<String, Integer> mapTypes = new HashMap<String, Integer>();
//    private TaskDao taskDao;
//
//    static {
//        mapTypes.put(Task.NAME_EXTRACT_METRIC, Task.TYPE_EXTRACT_METRIC);
//        mapTypes.put(Task.NAME_MINE_DATABASE, Task.TYPE_MINE_DATABASE);
//    }
//
//    public TaskService() {
//        taskDao = ObjectFactory.getObj(TaskDaoImpl.class);
//    }
//
//    @Transacional
//    public void save(Task task) {
//        if (task.getId() == null) {
//            taskDao.inclui(task);
//        } else {
//            taskDao.altera(task);
//        }
//    }
//
//    public List<Task> getAll() {
//        return taskDao.getAll();
//    }
//
//    public List<String> getTypeNames() {
//        return new ArrayList(mapTypes.keySet());
//    }
//
//    public Integer getTypeByName(String typeName) {
//        return mapTypes.get(typeName);
//    }
//
//    public List<Task> getScheduledTasks() {
//        return taskDao.getScheduledTasks();
//    }
//
//    public boolean executeScheduledTasks() {
//        System.out.println(">>> executeScheduledTasks");
//        List<Task> scheduledTasks = taskDao.getScheduledTasks();
//        for (Task task : scheduledTasks) {
//            addTaskToRun(task);
//        }
//        return startTasks();
//    }
//
//    synchronized private static void addTaskToRun(Task Task) {
//        mapTasks.put(new ThreadTask(Task), Boolean.FALSE);
//    }
//
//    synchronized private static boolean startTasks() {
//        boolean atLeastOneStarted = false;
//        System.out.println(">>> startTasks");
//        for (ThreadTask threadTask : mapTasks.keySet()) {
//            //ainda nao está rodando
//            if (!mapTasks.get(threadTask)) {
//                mapTasks.put(threadTask, Boolean.TRUE);
//                threadTask.start();
//                atLeastOneStarted = true;
//                System.out.println(">>> iniciada: " + threadTask.getName());
//            }
//        }
//        return atLeastOneStarted;
//    }
//
//    synchronized public void stopTasks() {
//        for (ThreadTask threadTask : mapTasks.keySet()) {
//            mapTasks.put(threadTask, Boolean.FALSE);
//            threadTask.suspend();
//        }
//    }
//
//    synchronized private void removeTask(Task task) {
//        for (ThreadTask threadTask : mapTasks.keySet()) {
//            if (threadTask.getTask().equals(task)) {
//                mapTasks.remove(threadTask);
//            }
//        }
//    }
//
//    public void done(Task task) {
//        removeTask(task);
//        task.setStatus(Task.STATUS_DONE);
//        save(task);
//    }
//
//    public void running(Task task) {
//        task.setStatus(Task.STATUS_RUNNING);
//    }
//
//    public void problemExecution(Task task, ExecutionPlanException ex) {
//        removeTask(task);
//        task.setStartTime(null);
//        task.setStatus(Task.STATUS_CREATED);
//    }
//}
//
