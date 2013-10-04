package br.uff.ic.oceano.peixeespada.controller;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.view.SelectOneDataModel;
import br.uff.ic.oceano.peixeespada.contexto.ContextoAmbiente;
import br.uff.ic.oceano.controller.BaseBean;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.QualityAttribute;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.QualityAttributeService;
import br.uff.ic.oceano.core.service.RefactoringService;
import br.uff.ic.oceano.peixeespada.model.Agent;
import br.uff.ic.oceano.peixeespada.service.AgentService;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author Kann
 */
public class AmbienteAgenteBean extends BaseBean{

    //Variavel corrente
    private String PAGINA_FORM_AGENTE = "def:/privado/peixeespada/agente/formAgente";
    private String PAGINA_AMBIENTE = "def:/privado/peixeespada/ambiente/listaContexto";
    private String PAGINA_REFACTORING = "def:/privado/peixeespada/refactoring/listRefactoring";
    private SelectOneDataModel<Revision> selectConfiguracao;
    private SelectOneDataModel<SoftwareProject> selectProjeto;
    private SelectOneDataModel<QualityAttribute> selectAtributo;
    private DataModel tabelaAgentes;
    private DataModel tabelaConhecimento;
    private DataModel tabelaRefactoring;
    private Agent agenteCorrente;
    private Revision configuracaoCorrente;
    private List<SelectItem> listaPapel;
    private String strPapelCorente;

    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private QualityAttributeService qualityAttributeService = ObjectFactory.getObjectWithDataBaseDependencies(QualityAttributeService.class);
    private AgentService agentService = ObjectFactory.getObjectWithDataBaseDependencies(AgentService.class);

    public AmbienteAgenteBean() {
        super("AmbienteAgenteBean");
        sessao.setPerfilPeixeEspada();
    }

    public void mudancaConfiguracao(){
        configuracaoCorrente = selectConfiguracao.getObjetoSelecionado();
    }

    public String paginaNovoAgente(){
        if(selectAtributo != null){
            selectAtributo.setSelecao(null);
        }
        if(selectProjeto != null){
            selectProjeto.setSelecao(null);
        }
        agenteCorrente = new Agent();
        return PAGINA_FORM_AGENTE;
    }



    public boolean  validaData(Date dataInicio, Date dataFim){
        Date dataAgora = new Date();
        boolean valida = true;
        if(dataAgora.compareTo(dataInicio)>0){
            error("A data INICIAL não aceita datas passadas");
            valida = false;
        }
        if(dataInicio.compareTo(dataFim)>=0){
            error("A data FINAL tem que ser maior que a data INICIAL");
            valida = false;
        }
        return valida;
    }

    public String cadastrarAgente(){
        agenteCorrente.setQualityAttribute(selectAtributo.getObjetoSelecionado());
        agenteCorrente.setProject(selectProjeto.getObjetoSelecionado());
        try {
            agentService.salve(agenteCorrente);
        } catch (ServiceException ex) {
            error(ex.getMessage());
            return null;
        }
        ContextoAmbiente.getInstance().registraAgente(agenteCorrente);
        info("Agente cadastrado com sucesso, clique em Ambiente para acompanhar o andamento");
        return paginaNovoAgente();
    }


    public String paginaAmbiente(){
        return PAGINA_AMBIENTE;
    }

    public String pageRefactoring(){
        return PAGINA_REFACTORING;
    }

    /**
     * @param selectConfiguracao the selectConfiguracao to set
     */
    public void setSelectConfiguracao(SelectOneDataModel<Revision> selectConfiguracao) {
        this.selectConfiguracao = selectConfiguracao;
    }

    /**
     * @return the tabelaAgentes
     */
    public DataModel getTabelaAgentes() {
        if(tabelaAgentes == null){
           tabelaAgentes  = new ListDataModel();
        }
        tabelaAgentes.setWrappedData(ContextoAmbiente.getInstance().getListaAgentes());
        return tabelaAgentes;
    }

    /**
     * @param tabelaAgentes the tabelaAgentes to set
     */
    public void setTabelaAgentes(DataModel tabelaAgentes) {
        this.tabelaAgentes = tabelaAgentes;
    }

    /**
     * @return the tabelaConhecimento
     */
    public DataModel getTabelaConhecimento() {
        if(tabelaConhecimento == null){
           tabelaConhecimento  = new ListDataModel();
        }
        tabelaConhecimento.setWrappedData(ContextoAmbiente.getInstance().getKnowledgeAcumulado());
        return tabelaConhecimento;
    }

    /**
     * @param tabelaConhecimento the tabelaConhecimento to set
     */
    public void setTabelaConhecimento(DataModel tabelaConhecimento) {
        this.tabelaConhecimento = tabelaConhecimento;
    }

    /**
     * @return the agenteCorrente
     */
    public Agent getAgenteCorrente() {
        return agenteCorrente;
    }

    /**
     * @param agenteCorrente the agenteCorrente to set
     */
    public void setAgenteCorrente(Agent agenteCorrente) {
        this.agenteCorrente = agenteCorrente;
    }

    /**
     * @return the listaPapel
     */
    public List<SelectItem> getListaPapel() {
        return listaPapel;
    }

    /**
     * @param listaPapel the listaPapel to set
     */
    public void setListaPapel(List<SelectItem> listaPapel) {
        this.listaPapel = listaPapel;
    }

    /**
     * @return the strPapelCorente
     */
    public String getStrPapelCorente() {
        return strPapelCorente;
    }

    /**
     * @param strPapelCorente the strPapelCorente to set
     */
    public void setStrPapelCorente(String strPapelCorente) {
        this.strPapelCorente = strPapelCorente;
    }

    /**
     * @return the selectMetrica
     */
    public SelectOneDataModel<QualityAttribute> getSelectAtributo() {
        if(selectAtributo == null){

//            selectAtributo = SelectOneDataModel.criaComTextoInicialPersonalizado(qualityAttributeService.getAll(), "Selecione O Atributo de Qualidade");
            selectAtributo = SelectOneDataModel.criaComTextoInicialPersonalizado(qualityAttributeService.getAll(), getMessageResourceString("selectQualityAttribute", null));
        }
        return selectAtributo;
    }

    /**
     * @param selectMetrica the selectMetrica to set
     */
    public void setSelectAtributo(SelectOneDataModel<QualityAttribute> selectAtributo) {
        this.selectAtributo = selectAtributo;
    }

    /**
     * @return the configuracaoCorrente
     */
    public Revision getConfiguracaoCorrente() {
        return configuracaoCorrente;
    }

    /**
     * @param configuracaoCorrente the configuracaoCorrente to set
     */
    public void setConfiguracaoCorrente(Revision configuracaoCorrente) {
        this.configuracaoCorrente = configuracaoCorrente;
    }

    /**
     * @return the selectProjeto
     */
    public SelectOneDataModel<SoftwareProject> getSelectProjeto() {
        if(selectProjeto == null){
            selectProjeto = SelectOneDataModel.criaComTextoInicialPersonalizado(projectService.getProjectsByOceanoUser(sessao.getUsuarioCorrente()), getMessageResourceString("selectProject", null));

        }
        return selectProjeto;
    }

    /**
     * @param selectProjeto the selectProjeto to set
     */
    public void setSelectProjeto(SelectOneDataModel<SoftwareProject> selectProjeto) {
        this.selectProjeto = selectProjeto;
    }

    /**
     * @return the tabelaRefactoring
     */
    public DataModel getTabelaRefactoring() {
        if(tabelaRefactoring == null){
            tabelaRefactoring = new ListDataModel(ContextoAmbiente.getInstance().getRefactorings());
        }
        return tabelaRefactoring;
    }

    /**
     * @param tabelaRefactoring the tabelaRefactoring to set
     */
    public void setTabelaRefactoring(DataModel tabelaRefactoring) {
        this.tabelaRefactoring = tabelaRefactoring;
    }
}