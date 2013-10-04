package br.uff.ic.oceano.polvo.controller;

import br.uff.ic.oceano.controller.BaseBean;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Branch;
import br.uff.ic.oceano.core.model.BranchingMetric;
import br.uff.ic.oceano.core.model.BranchingModel;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.BranchService;
import br.uff.ic.oceano.core.service.BranchingMetricService;
import br.uff.ic.oceano.core.service.BranchingModelService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.polvo.service.PolvoBranchService;
import br.uff.ic.oceano.polvo.service.PolvoGraphicService;
import br.uff.ic.oceano.polvo.util.MetricBranch;
import br.uff.ic.oceano.view.SelectOneDataModel;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Rafael Santos
 */
public class PolvoBean extends BaseBean{

    private String PAGINA_FORM_CONFIG_RAMOS = "def:/privado/polvo/formConfigRamos";
    private String PAGINA_VISAO_GERAL_PROJETO = "def:/privado/polvo/visaoGeralProjeto";
    private String PAGINA_VISAO_TEMPORAL_RAMO = "def:/privado/polvo/visaoTemporalRamo";
    private String PAGINA_VISAO_ESPECIFICA_RAMO = "def:/privado/polvo/visaoEspecificaRamo";
    private String PAGINA_VISAO_DETALHADA_RAMO = "def:/privado/polvo/visaoDetalhadaRamo";

    private SelectOneDataModel<SoftwareProject> selectProject;
    private SelectOneDataModel<Branch> selectMainBranch;
    private SelectOneDataModel<Branch> selectSecondaryBranch;
    private SelectOneDataModel<BranchingMetric> selectBranchingMetric;
    private SelectOneDataModel<BranchingModel> selectBranchingModel;
    //private String revision;

    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private BranchService branchService = ObjectFactory.getObjectWithDataBaseDependencies(BranchService.class);
    private BranchingMetricService branchingMetricService = ObjectFactory.getObjectWithDataBaseDependencies(BranchingMetricService.class);
    private BranchingModelService branchingModelService = ObjectFactory.getObjectWithDataBaseDependencies(BranchingModelService.class);
    private PolvoBranchService polvoBranchService = ObjectFactory.getObjectWithDataBaseDependencies(PolvoBranchService.class);

    private ListDataModel tableBranch;
    private ListDataModel tableRevision;

    private SoftwareProject currentProject;
    private Branch currentMainBranch;
    private Branch currentSecondaryBranch;
    private BranchingModel currentBranchingModel;
    private BranchingMetric currentBranchingMetric;
    private Long currentRevision;

    private Long idProject = null;
    private String nameMainBranch = null;
    private String nameSecondaryBranch = null;
    private Long idBranchingModel = null;
    private Long idBranchingMetric = null;
    private Long beginRevision = null;
//    private Integer flagCompareBranch = null;

    private String qtdArqMainBranch;
    private String qtdArqSecondaryBranch;
    private String qtdMetric;
    private String qtdDiverg;
    private String nodesGraph1;
    private String nodesGraph2;

    public PolvoBean() {
        super("PolvoBean");
        sessao.setPerfilPolvo();
    }

    public PolvoBean(String nomeDoLogger) {
        super(nomeDoLogger);
    }

    public String paginaNovaConfigRamos(){
        reset();

        return PAGINA_FORM_CONFIG_RAMOS;
    }

    public String paginaNovaVisaoGeralProjeto(){
        reset();

        return PAGINA_VISAO_GERAL_PROJETO;
    }

    public String paginaNovaVisaoEspecificaRamo(){
        reset();

        return PAGINA_VISAO_ESPECIFICA_RAMO;
    }

    private void reset() {
        if (selectProject != null) {
            selectProject.setSelecao(null);
        }
        setCurrentProject(null);

        if (selectMainBranch != null) {
            selectMainBranch.setSelecao(null);
        }
        setCurrentMainBranch(null);

        if (selectSecondaryBranch != null) {
            selectSecondaryBranch.setSelecao(null);
        }
        setCurrentSecondaryBranch(null);

        if (selectBranchingMetric != null) {
            selectBranchingMetric.setSelecao(null);
        }
        setCurrentBranchingMetric(null);

        if (selectBranchingModel != null) {
            selectBranchingModel.setSelecao(null);
        }
        setCurrentBranchingModel(null);

        setCurrentRevision(null);

    }

   public void setIdProject(Long idProject) {
        if (idProject != null) {
            try {
                currentProject = projectService.getById(idProject);
            }
            catch (ObjetoNaoEncontradoException e) {
            }
        }
        this.idProject = null;
    }

    public void setNameMainBranch(String nameMainBranch) {
        if (nameMainBranch != null) {
            try {
                currentMainBranch = branchService.getbyProjectName(currentProject.getId(), nameMainBranch);
            }
            catch (ObjetoNaoEncontradoException e) {
            }
        }
        this.nameMainBranch = null;
    }

    public void setNameSecondaryBranch(String nameSecondaryBranch) {
        if (nameSecondaryBranch != null) {
            try {
                currentSecondaryBranch = branchService.getbyProjectName(currentProject.getId(), nameSecondaryBranch);
            }
            catch (ObjetoNaoEncontradoException e) {
            }
        }
        this.nameSecondaryBranch = null;
    }

    public void setIdBranchingModel(Long idBranchingModel) {
        if (idBranchingModel != null) {
            try {
                currentBranchingModel = branchingModelService.getById(idBranchingModel);
            }
            catch (ObjetoNaoEncontradoException e) {
            }
        }
        this.idBranchingModel = null;
    }

    public void setIdBranchingMetric(Long idBranchingMetric) throws Exception {
        if (idBranchingMetric != null) {
            try {
                currentBranchingMetric = branchingMetricService.getById(idBranchingMetric);
            }
            catch (ObjetoNaoEncontradoException e) {
            }
        }
        this.idBranchingMetric = null;
    }

    public void setBeginRevision(Long beginRevision) throws Exception {
        System.out.println("setBeginRevision="+beginRevision);
        if (beginRevision != null) {
            String nodesGraph1 = "";
            String nodesGraph2 = "";
            //try {
                // dependendo da estrategia de ramificacao escolhida, inverte sentido da avaliacao do merge, ou calcula nos 2 sentidos
                if (currentBranchingModel.getDirectionMerge() == -1 || currentBranchingModel.getDirectionMerge() == 0) {
                    nodesGraph1 = polvoBranchService.compareBranchTime(
                        currentProject, currentMainBranch, currentSecondaryBranch, currentBranchingModel, currentBranchingMetric, beginRevision);
                    //nodesGraph1="242299,61:236047,63:229795,78:223543,81:217291,82:211039,82:204787,83:198535,86:192283,88:186031,96:179779,100";

                }
                if (currentBranchingModel.getDirectionMerge() == 0 || currentBranchingModel.getDirectionMerge() == 1) {
                    nodesGraph2 = polvoBranchService.compareBranchTime(
                        currentProject, currentSecondaryBranch, currentMainBranch, currentBranchingModel, currentBranchingMetric, beginRevision);
                    //nodesGraph2="242299,85:236047,84:229795,93:223543,92:217291,91:211039,92:204787,91:198535,92:192283,91:186031,98:179779,100";
                }

                System.out.println("nodesGraph1="+nodesGraph1);
                System.out.println("nodesGraph2="+nodesGraph2);
                this.setNodesGraph1(nodesGraph1);
                this.setNodesGraph2(nodesGraph2);

            //}
            //catch (ObjetoNaoEncontradoException e) {
            //}
        }
        this.beginRevision = null;
    }

/*
    public void setFlagCompareBranch(Integer flagCompareBranch) throws Exception {
        System.out.println("setFlagCompareBranch="+flagCompareBranch);
        
        if (flagCompareBranch != null) {
            try {
                String nodesGraph = polvoBranchService.compareBranchTime(
                    currentProject, currentMainBranch, currentSecondaryBranch, currentBranchingModel, currentBranchingMetric);
                System.out.println("nodesGraph="+nodesGraph);
                this.setNodesGraph(nodesGraph);

            }
            catch (ObjetoNaoEncontradoException e) {
            }
        }
         
        this.flagCompareBranch = null;
    }
*/
    public void mudancaProjeto(){
        currentProject = selectProject.getObjetoSelecionado();
    }

    public void mudancaProjetoVisaoGeral() throws Exception{
        mudancaProjeto();
        setBranches();
        PolvoGraphicService.gerarVisaoGeral(currentProject, currentBranchingMetric);
    }

    public String gerarGraficoVisaGeral() {
        return PAGINA_VISAO_GERAL_PROJETO;
    }

    public void mudancaRamoPrincipal(){
        currentMainBranch = selectMainBranch.getObjetoSelecionado();
    }

    public void mudancaRamoSecundario(){
        currentSecondaryBranch = selectSecondaryBranch.getObjetoSelecionado();
    }

    public void mudancaMetrica(){
        currentBranchingMetric = selectBranchingMetric.getObjetoSelecionado();
    }

    public void mudancaEstrategiaRamificacao(){
        currentBranchingModel = selectBranchingModel.getObjetoSelecionado();
    }

    public String configurarRamos(){
        // TODO
        info("Ramos configurados com sucesso, clique em Visão Geral do Projeto para visualiza-los");
        return paginaNovaConfigRamos();
    }

    public String compararRamos() throws Exception {
        String metric = "";    
        MetricBranch metricBranch = null;

        // dependendo da estrategia de ramificacao escolhida, inverte sentido da avaliacao do merge, ou calcula nos 2 sentidos
        if (currentBranchingModel.getDirectionMerge() == -1 || currentBranchingModel.getDirectionMerge() == 0) {
            if (currentRevision == null) {
                metricBranch = polvoBranchService.compareBranch(currentProject, currentMainBranch, currentSecondaryBranch, currentBranchingModel, currentBranchingMetric);
                setCurrentRevision(metricBranch.getRevision());
            }
            else {
                System.out.println("currentRevision="+currentRevision);
                metricBranch = polvoBranchService.compareBranch(currentProject, currentMainBranch, currentSecondaryBranch, currentBranchingModel, currentBranchingMetric, currentRevision);
            }
            //metric = currentMainBranch.getName() + " --> " + currentSecondaryBranch.getName() + " = " + tratarMetrica(metricBranch.getQtdMetrica());
            metric = getMessageResourceString("lblMB-SB", null) + " = " + tratarMetrica(metricBranch.getQtdMetrica());


        }
        if (currentBranchingModel.getDirectionMerge() == 0 || currentBranchingModel.getDirectionMerge() == 1) {
            if (currentRevision == null) {
                metricBranch = polvoBranchService.compareBranch(currentProject, currentSecondaryBranch, currentMainBranch,  currentBranchingModel, currentBranchingMetric);
                setCurrentRevision(metricBranch.getRevision());
            }
            else {
                System.out.println("currentRevision="+currentRevision);
                metricBranch = polvoBranchService.compareBranch(currentProject, currentSecondaryBranch, currentMainBranch,  currentBranchingModel, currentBranchingMetric, currentRevision);
            }
            if (!metric.isEmpty()) {
                metric = metric + " / ";
            }
            //metric = metric + " / " + currentMainBranch.getName() + " <-- " + currentSecondaryBranch.getName() + " = " + tratarMetrica(metricBranch.getQtdMetrica());
            metric = metric + getMessageResourceString("lblSB-MB", null) + " = " + tratarMetrica(metricBranch.getQtdMetrica());
        }

        this.setQtdArqMainBranch(new Integer(metricBranch.getQtdArqRamoPrincipal()).toString());
        this.setQtdArqSecondaryBranch(new Integer(metricBranch.getQtdArqRamoSecundario()).toString());
        //this.setQtdMetric(new Integer(metricaRamos.getQtdMetrica()).toString());
        this.setQtdDiverg(new Integer(metricBranch.getQtdDivergencia()).toString());
        this.setQtdMetric(metric);


        return PAGINA_VISAO_DETALHADA_RAMO;
    }

    private String tratarMetrica(int metrica){
        String ret = "";
        if (metrica == -1){
            ret = "Não é possivel analisar o merge sintático, pois há conflitos fisicos!";
        }
        else {
            if (metrica == -2){
                ret = "Não é possivel analisar o merge semântico, pois há conflitos sintáticos!";
            }
            else {
                ret = new Integer(metrica).toString();
            }
        }
        return ret;
    }

    /*
    private void verifyBranchingModel(Branch mainBranch, Branch secondaryBranch, BranchingModel branchingModel){
        Branch tempBranch = new Branch();
        // dependendo da estrategia de ramificacao escolhida, inverte sentido da avaliacao do merge
        if (branchingModel.getDirectionMerge() == -1) {
            baseBranch = currentSecondaryBranch;
            compareBranch = currentMainBranch;
        }
    }
    */

    public String visaoTemporalRamo() throws Exception {
        // TODO teria que pegar a rev. inicial, só feito na geração do gráfico de visão geral
        setBeginRevision(new Long("1"));
        return PAGINA_VISAO_TEMPORAL_RAMO;
    }

    public SelectOneDataModel<SoftwareProject> getSelectProject() {
        if(selectProject == null){
            selectProject = SelectOneDataModel.criaComTextoInicialPersonalizado(projectService.getAll(), getMessageResourceString("selectProject", null));
        }
        return selectProject;
    }

    public void setSelectProject(SelectOneDataModel<SoftwareProject> selectProject) {
        this.selectProject = selectProject;
    }

    public SelectOneDataModel<Branch> getSelectMainBranch() throws Exception {
        List<Branch> listBranch = setBranches();

        selectMainBranch = SelectOneDataModel.criaComTextoInicialPersonalizado(listBranch, getMessageResourceString("selectBranch", null));
        return selectMainBranch;
    }

    private List<Branch> setBranches() throws Exception {
        List<Branch> listBranch = new ArrayList<Branch>();
        if (getCurrentProject() != null) {
            System.out.println("getCurrentProject().getId()=" + getCurrentProject().getId());
            listBranch = branchService.getbyProject(getCurrentProject().getId());
            System.out.println("listBranch.size()=" + listBranch.size());
            if (listBranch.isEmpty()) {
                List<String> listaRamos = polvoBranchService.getBranches(getCurrentProject());
                Branch branch;
                for (String ramos : listaRamos) {
                    branch = new Branch();
                    // work around para o oceano-core
                    if (getCurrentProject().getRepositoryUrl().contains("oceano-core")){
                        if (!ramos.equals("trunk")) {
                            branch.setName(ramos + "/trunk");
                        }
                        else {
                            branch.setName(ramos);
                        }
                    }
                    else{
                        branch.setName(ramos);
                    }
                    //branch.setBranchingModel(porDesenvolvedor);
                    branch.setProject(getCurrentProject());
                    branchService.save(branch);
                }
                listBranch = branchService.getbyProject(getCurrentProject().getId());
            }
        }
        return listBranch;
    }

    public void setSelectMainBranch(SelectOneDataModel<Branch> selectMainBranch) {
        this.selectMainBranch = selectMainBranch;
    }

    public SelectOneDataModel<Branch> getSelectSecondaryBranch() {
        List<Branch> listBranch = new ArrayList<Branch>();
        if (getCurrentMainBranch() != null){
            System.out.println("getCurrentMainBranch().getId()="+getCurrentMainBranch().getId());
            listBranch = branchService.getbyProject(getCurrentProject().getId());

        }
        selectSecondaryBranch = SelectOneDataModel.criaComTextoInicialPersonalizado(listBranch, getMessageResourceString("selectBranch", null));
        return selectSecondaryBranch;
    }

    public void setSelectSecondaryBranch(SelectOneDataModel<Branch> selectSecondaryBranch) {
        this.selectSecondaryBranch = selectSecondaryBranch;
    }

    public SelectOneDataModel<BranchingMetric> getSelectBranchingMetric() {
        if(selectBranchingMetric == null){
            selectBranchingMetric = SelectOneDataModel.criaComTextoInicialPersonalizado(branchingMetricService.getAll(), getMessageResourceString("selectMetrics", null));
        }
        return selectBranchingMetric;
    }

    public void setSelectBranchingMetric(SelectOneDataModel<BranchingMetric> selectBranchingMetric) {
        this.selectBranchingMetric = selectBranchingMetric;
    }

    public SelectOneDataModel<BranchingModel> getSelectBranchingModel() {
        if(selectBranchingModel == null){
            selectBranchingModel = SelectOneDataModel.criaComTextoInicialPersonalizado(branchingModelService.getAll(), getMessageResourceString("selectModel", null));
        }
        return selectBranchingModel;
    }

    public void setSelectBranchingModel(SelectOneDataModel<BranchingModel> selectBranchingModel) {
        this.selectBranchingModel = selectBranchingModel;
    }

    //public String getRevision() {
    //    return revision;
    //}

    //public void setRevision(String txRevision) {
    //    this.revision = txRevision;
    //}

    public SoftwareProject getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(SoftwareProject currentProject) {
        this.currentProject = currentProject;
        System.out.println("currentProject = " + currentProject);
    }

    public ListDataModel getTableBranch() throws Exception {
        if (getCurrentProject() != null) {

            List<Branch> listBranch = branchService.getbyProject(getCurrentProject().getId());
            if (listBranch.isEmpty()){
                List<String> listaRamos = polvoBranchService.getBranches(getCurrentProject());
                Branch branch;
                for (String ramos : listaRamos) {
                    branch = new Branch();
                    branch.setName(ramos);
                    //branch.setBranchingModel(porDesenvolvedor);
                    branch.setProject(getCurrentProject());
                    branchService.save(branch);
                }

                listBranch = branchService.getbyProject(getCurrentProject().getId());
            }
            tableBranch = new ListDataModel(listBranch);
            for (Branch branch : branchService.getbyProject(getCurrentProject().getId())) {
                System.out.println("branch = " + branch);
            }
        }
        return tableBranch;
    }

    public void setTableBranch(ListDataModel tableBranch) {
        this.tableBranch = tableBranch;
    }

    public ListDataModel getTableRevision() throws Exception {
        if (getNodesGraph1() != null) {
            List<String> listRevision = new ArrayList<String>();
            String[] vertices = getNodesGraph1().split(":");
            for (int i=0; i < vertices.length; i++){
                // só inclui os vértices que possuem métrica > 0
                if (Long.parseLong(vertices[i].split(",")[1]) > 0) {
                    listRevision.add(vertices[i].split(",")[0]);
                }
            }

            tableRevision = new ListDataModel(listRevision);
        }
        return tableRevision;
    }

    public void setTableRevision(ListDataModel tableRevision) {
        this.tableRevision = tableRevision;
    }

    public String detailRevision() throws Exception {

            System.out.println("currentRevision=" + tableRevision.getRowData());
            setCurrentRevision(new Long((String)tableRevision.getRowData()));

            return compararRamos();

    }

    public BranchingMetric getCurrentBranchingMetric() {
        return currentBranchingMetric;
    }

    public void setCurrentBranchingMetric(BranchingMetric currentBranchingMetric) {
        this.currentBranchingMetric = currentBranchingMetric;
    }

    public BranchingModel getCurrentBranchingModel() {
        return currentBranchingModel;
    }

    public void setCurrentBranchingModel(BranchingModel currentBranchingModel) {
        this.currentBranchingModel = currentBranchingModel;
    }

    public Branch getCurrentMainBranch() {
        return currentMainBranch;
    }

    public void setCurrentMainBranch(Branch currentMainBranch) {
        this.currentMainBranch = currentMainBranch;
    }

    public Branch getCurrentSecondaryBranch() {
        return currentSecondaryBranch;
    }

    public void setCurrentSecondaryBranch(Branch currentSecondaryBranch) {
        this.currentSecondaryBranch = currentSecondaryBranch;
    }

    public String getQtdArqMainBranch() {
        return qtdArqMainBranch;
    }

    public void setQtdArqMainBranch(String qtdArqMainBranch) {
        this.qtdArqMainBranch = qtdArqMainBranch;
    }

    public String getQtdArqSecondaryBranch() {
        return qtdArqSecondaryBranch;
    }

    public void setQtdArqSecondaryBranch(String qtdArqSecondaryBranch) {
        this.qtdArqSecondaryBranch = qtdArqSecondaryBranch;
    }

    public String getQtdDiverg() {
        return qtdDiverg;
    }

    public void setQtdDiverg(String qtdDiverg) {
        this.qtdDiverg = qtdDiverg;
    }

    public String getQtdMetric() {
        return qtdMetric;
    }

    public void setQtdMetric(String qtdMetric) {
        this.qtdMetric = qtdMetric;
    }

    public String getNodesGraph1() {
        return nodesGraph1;
    }

    public void setNodesGraph1(String nodesGraph1) {
        this.nodesGraph1 = nodesGraph1;
    }

    public String getNodesGraph2() {
        return nodesGraph2;
    }

    public void setNodesGraph2(String nodesGraph2) {
        this.nodesGraph2 = nodesGraph2;
    }

    public Long getCurrentRevision() {
        return currentRevision;
    }

    public void setCurrentRevision(Long currentRevision) {
        this.currentRevision = currentRevision;
    }

}