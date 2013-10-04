/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.util;

import java.io.IOException;
import java.util.List;
import org.apache.maven.ProjectBuildFailureException;
import org.apache.maven.artifact.resolver.MultipleArtifactsNotFoundException;
import org.apache.maven.lifecycle.LifecycleExecutionException;
import org.apache.maven.lifecycle.LifecycleSpecificationException;
import org.apache.maven.lifecycle.plan.LifecyclePlannerException;
//import org.apache.maven.plugin.CompilationFailureException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.reactor.MavenExecutionException;

/**
 *
 * @author marapao
 */
public class Casting {

    public static StringBuffer ListTrowableToString(List<Throwable> erros) {

        StringBuffer stringBuffer = new StringBuffer();


        if (erros != null && !erros.isEmpty()) {
            for (Object obj : erros) {
                Object aux = obj;
                while (aux != null) {
                    if (aux instanceof ProjectBuildFailureException) {
                        ProjectBuildFailureException pro = (ProjectBuildFailureException) aux;
                        stringBuffer.append(pro.getMessage()).append("\n");
                        aux = pro.getCause();
                    } else if (aux instanceof LifecycleExecutionException) {
                        LifecycleExecutionException ex = (LifecycleExecutionException) aux;
                        stringBuffer.append(ex.getMessage()).append("\n");
                        aux = ex.getCause();
                    } else if (aux instanceof MultipleArtifactsNotFoundException) {
                        MultipleArtifactsNotFoundException ex = (MultipleArtifactsNotFoundException) aux;
//                        stringBuffer.append(ex.getMessage()).append("\n");
                        aux = ex.getCause();
                    } else if (aux instanceof MavenExecutionException) {
                        MavenExecutionException ex = (MavenExecutionException) aux;
                        stringBuffer.append(ex.getMessage()).append("\n");
                        aux = ex.getCause();
                    } else if (aux instanceof ProjectBuildingException) {
                        ProjectBuildingException ex = (ProjectBuildingException) aux;
                        stringBuffer.append(ex.getMessage()).append("\n");
                        aux = ex.getCause();
                    } else if (aux instanceof IOException) {
                        IOException ex = (IOException) aux;
                        stringBuffer.append(ex.getMessage()).append("\n");
                        aux = ex.getCause();
                    } else if (aux instanceof LifecyclePlannerException) {
                        LifecyclePlannerException ex = (LifecyclePlannerException) aux;
                        stringBuffer.append(ex.getMessage()).append("\n");
                        aux = ex.getCause();
                    } else if (aux instanceof LifecycleSpecificationException) {
                        LifecycleSpecificationException ex = (LifecycleSpecificationException) aux;
                        stringBuffer.append(ex.getMessage()).append("\n");
                        aux = ex.getCause();
                    } else {
                        MojoFailureException mojoFailureException = (MojoFailureException) aux;
                        stringBuffer.append(mojoFailureException.getLongMessage()).append("\n");
                        aux = mojoFailureException.getCause();
                    }
                }
            }

            return stringBuffer;
        } else {
            return null;
        }

    }
}
