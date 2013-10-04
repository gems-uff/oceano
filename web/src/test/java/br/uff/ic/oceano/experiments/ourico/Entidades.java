/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.ConfigurationItemService;
import br.uff.ic.oceano.core.service.RepositoryService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.util.CargaDefaultWeb;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author marapao
 */
public class Entidades {

    private SoftwareProjectService projectService= ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private ConfigurationItemService configurationItemService = ObjectFactory.getObjectWithDataBaseDependencies(ConfigurationItemService.class);
    private RepositoryService repositoryService = ObjectFactory.getObjectWithDataBaseDependencies(RepositoryService.class);

    
    @BeforeClass
    public void antes(){
        JPAUtil.setCurrentPersistenceUnit(JPAUtil.PERSISTENCE_UNIT_MEMORY);
        JPAUtil.startUp();

        CargaDefaultWeb.insertDefaultData();
    }

    @AfterClass
    public void depois(){

    }

    @Test
    public void adicionaConfigurationItem() throws ObjetoNaoEncontradoException{
    ConfigurationItem configurationItem;

    configurationItem = new ConfigurationItem();
    configurationItem.setBaseUrl("https://10.0.0.102/svn");
    configurationItem.setBranchPath("/branch");
    configurationItem.setName("pc");
    configurationItem.setRepository(repositoryService.getByName("SVN"));
    configurationItem.setTrunkPath("/trunk");

    configurationItemService.save(configurationItem);
    }

    @Test(dependsOnMethods="adicionaConfigurationItem")
    public void adicionaSoftwareProject() throws ServiceException{

        SoftwareProject project;

        project = new SoftwareProject();
        List<ConfigurationItem> all = configurationItemService.getAll();
        ConfigurationItem ci = null;

        for (ConfigurationItem configurationItem : all) {
            if(configurationItem.getBaseUrl().equals("https://10.0.0.102/svn"))
                ci = configurationItem;
        }

        project.setConfigurationItem(ci);
        project.setMavenProject(true);
        project.setProjectUser(null);
        project.setRepositoryUrl("https://10.0.0.102/svn/trunk");
        project.setRevisions(null);

        projectService.save(project);

    }

    @Test
    public void pegaTodos(){
        List<SoftwareProject> all = projectService.getAll();

        for (SoftwareProject softwareProject : all) {
            System.out.println(softwareProject.getRepositoryUrl());
        }
    }



}
