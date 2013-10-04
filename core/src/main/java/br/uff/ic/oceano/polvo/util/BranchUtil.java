/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.polvo.util;

import br.uff.ic.oceano.core.model.Branch;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import java.util.Set;

/**
 *
 * @author Rafael
 */
public class BranchUtil {

    /**
     * Dado o branch, retorna a url onde está localizado
     *
     * @param project
     * @param branch
     * @return url
     */
    public static String getUrl(SoftwareProject project, Branch branch){
        String url = project.getConfigurationItem().getBaseUrl();
        if (url.lastIndexOf('/') != (url.length() - 1)) {
            url = url + "/";
        }
        String nameBranch = branch.getName();
        if (nameBranch.equals(project.getConfigurationItem().getTrunkPath())){
            url = url + project.getConfigurationItem().getTrunkPath() + "/";
        }
        else {
            url = url + project.getConfigurationItem().getBranchPath() + "/" + nameBranch + "/";
        }

        return url;
    }

    public static String getLastValuePath(String path) {
        int index = path.lastIndexOf('/');
        // se termina com "/"
        if (index == (path.length() - 1)) {
            // tira a ultima barra
            path = path.substring(0, path.length()-1);
            index = path.lastIndexOf('/');
        }
        return path.substring(index+1);
    }

    /**
     * Seta os dados para acesso ao repositorio
     * obs.: solucao temporaria enquanto o Polvo nao está usando acesso ao BD Postgre
     *
     * @return ProjectUser
     */
    public static ProjectUser getProjectUser(SoftwareProject project) {
        ProjectUser projectUser = new ProjectUser();
        projectUser.setLogin("");
        projectUser.setPassword("");
        projectUser.setAnonymous(true);

        Set<ProjectUser> listProjectUser = project.getProjectUser();
        for (ProjectUser projectUserTmp : listProjectUser) {
            // testando com meu usuario
            if ("rss".equals(projectUserTmp.getOceanoUser().getLogin())) {
                projectUser = projectUserTmp;
                System.out.println("Login:" + projectUser.getLogin());
                System.out.println("Senha:" + projectUser.getPassword());
            }
        }

        return projectUser;
    }

}
