/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.controller;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.model.Repository;
import br.uff.ic.oceano.core.service.ConfigurationItemService;
import br.uff.ic.oceano.core.service.RepositoryService;
import br.uff.ic.oceano.view.SelectOneDataModel;

/**
 *
 * @author marapao
 */
public class ConfigurationItemBean extends BaseBean {
    private String ERROR_CREATE_CONFIGURATION_ITEM_REPOSITORY_MISSING = "Select one repository";

    public ConfigurationItemBean() {
        super("ConfigurationItemBean");
        currentConfigurationItem = new ConfigurationItem();
    }

    private String caminho = "def:/privado/oceano/configurationItem/ConfigurationItemForm";
    private ConfigurationItem currentConfigurationItem;

    private ConfigurationItemService configurationItemService = ObjectFactory.getObjectWithDataBaseDependencies(ConfigurationItemService.class);
    private RepositoryService repositoryService = ObjectFactory.getObjectWithDataBaseDependencies(RepositoryService.class);

    private SelectOneDataModel<Repository> selectOneRepository;


    /**
     * @return the configurationItemService
     */
    public ConfigurationItemService getConfigurationItemService() {
        return configurationItemService;
    }

    /**
     * @param configurationItemService the configurationItemService to set
     */
    public void setConfigurationItemService(ConfigurationItemService configurationItemService) {
        this.configurationItemService = configurationItemService;
    }

    public String doSave() {

        
        if(selectOneRepository.getObjetoSelecionado() == null){
            error(ERROR_CREATE_CONFIGURATION_ITEM_REPOSITORY_MISSING);
            return null;
        }

        currentConfigurationItem.setRepository(selectOneRepository.getObjetoSelecionado());
        configurationItemService.save(currentConfigurationItem);

        info("Configuration Item Saved.");

        return null;
    }

    public String doCancel() {
        currentConfigurationItem = null;
        return null;
    }

    /**
     * @return the caminho
     */
    public String getCaminho() {
        return caminho;
    }

    /**
     * @param caminho the caminho to set
     */
    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String doCadastro(){
        return caminho;
    }

    /**
     * @return the selectOneRepository
     */
    public SelectOneDataModel<Repository> getSelectOneRepository() {
        if(selectOneRepository == null){
            selectOneRepository = selectOneRepository.criaComTextoInicialPersonalizado(repositoryService.getAll(), "Select a repository.");
        }
        return selectOneRepository;
    }

    /**
     * @param selectOneRepository the selectOneRepository to set
     */
    public void setSelectOneRepository(SelectOneDataModel<Repository> selectOneRepository) {
        this.selectOneRepository = selectOneRepository;
    }

    /**
     * @return the currentConfigurationItem
     */
    public ConfigurationItem getCurrentConfigurationItem() {
        return currentConfigurationItem;
    }

    /**
     * @param currentConfigurationItem the currentConfigurationItem to set
     */
    public void setCurrentConfigurationItem(ConfigurationItem currentConfigurationItem) {
        this.currentConfigurationItem = currentConfigurationItem;
    }
    }
