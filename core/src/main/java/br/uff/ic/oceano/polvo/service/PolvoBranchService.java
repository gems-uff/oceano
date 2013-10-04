/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.polvo.service;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Branch;
import br.uff.ic.oceano.core.model.BranchingMetric;
import br.uff.ic.oceano.core.model.BranchingModel;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_CommandLineInterface;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_SVNKit;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.ourico.rcs.Subversion;
import br.uff.ic.oceano.ourico.util.Casting;
import br.uff.ic.oceano.ourico.verificacao.build.Maven;
import br.uff.ic.oceano.polvo.controle.Constantes;
import br.uff.ic.oceano.polvo.util.MetricBranch;
import br.uff.ic.oceano.polvo.util.BranchUtil;
import java.io.File;
import java.util.List;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 *
 * @author Rafael
 */
public class PolvoBranchService {
    private SVN_By_SVNKit svnBySVNKit = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
    private SVN_By_CommandLineInterface svnByCLI = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_CommandLineInterface.class);

    public String compareBranchTime(SoftwareProject project, Branch mainBranch, Branch secondaryBranch, BranchingModel branchingModel, BranchingMetric branchingMetric, long beginRevision) throws Exception {
        ProjectUser projectUser = BranchUtil.getProjectUser(project);
        projectUser.setProject(project);

        String nodesGraph = "";
        MetricBranch metricBranch;

        Long numberOfHEADRevision = svnByCLI.getNumberOfHEADRevision(projectUser);
        System.out.println("numberOfHEADRevision="+numberOfHEADRevision);

        int numberOfInterval = 10;
        long interval = (numberOfHEADRevision - beginRevision)/numberOfInterval;
        long numberRevision;

        metricBranch = compareBranch(project, mainBranch, secondaryBranch, branchingModel, branchingMetric, numberOfHEADRevision);
        nodesGraph = addNodesGraph(nodesGraph, numberOfHEADRevision, metricBranch.getQtdMetrica());

        for (int i = 1; i < numberOfInterval; i++){
            numberRevision = numberOfHEADRevision - (interval * i);
            metricBranch = compareBranch(project, mainBranch, secondaryBranch, branchingModel, branchingMetric, numberRevision);
            nodesGraph = addNodesGraph(nodesGraph, numberRevision, metricBranch.getQtdMetrica());
        }

        int iniGrafico = 0;
        long idBranchingMetric = branchingMetric.getId().longValue();
        if (idBranchingMetric == Constantes.METR_RAMIF_ARQ_COBERTURA || idBranchingMetric == Constantes.METR_RAMIF_ARQ_PRECISAO) {
            iniGrafico = 100;
        }
        nodesGraph = addNodesGraph(nodesGraph, beginRevision, iniGrafico);

        /*
        metricBranch = compareBranch(project, mainBranch, secondaryBranch, branchingModel, branchingMetric, numberOfHEADRevision);
        nodesGraph = addNodesGraph(nodesGraph, numberOfHEADRevision, metricBranch.getQtdMetrica());

        metricBranch = compareBranch(project, mainBranch, secondaryBranch, branchingModel, branchingMetric, ((beginRevision + numberOfHEADRevision)/2));
        nodesGraph = addNodesGraph(nodesGraph, (beginRevision + numberOfHEADRevision)/2, metricBranch.getQtdMetrica());

        nodesGraph = addNodesGraph(nodesGraph, beginRevision, 0);
         */

        return nodesGraph;
    }

    private String addNodesGraph(String nodesGraph, long revision, int metrica) {
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(dateRevision);
        if (!nodesGraph.equals("")) {
            nodesGraph += ":";
        }
        //nodesGraph += (calendar.get(calendar.MONTH) + 1) + "," + calendar.get(calendar.YEAR) + "," + metrica;
        nodesGraph += revision + "," + metrica;
        System.out.println("nodesGraph="+nodesGraph);
        return nodesGraph;
    }

    public MetricBranch compareBranch(SoftwareProject project, Branch mainBranch, Branch secondaryBranch, BranchingModel branchingModel, BranchingMetric branchingMetric) throws Exception {
        ProjectUser projectUser = BranchUtil.getProjectUser(project);
        projectUser.setProject(project);
        Long numberOfHEADRevision = svnByCLI.getNumberOfHEADRevision(projectUser);
        System.out.println("numberOfHEADRevision="+numberOfHEADRevision);
        MetricBranch metricBranch = compareBranch(project, mainBranch, secondaryBranch, branchingModel, branchingMetric, numberOfHEADRevision);
        metricBranch.setRevision(numberOfHEADRevision);
        return metricBranch;
    }

    public MetricBranch compareBranch(SoftwareProject project, Branch mainBranch, Branch secondaryBranch, BranchingModel branchingModel, BranchingMetric branchingMetric, Long numberRevision) throws Exception {
        MetricBranch metricBranch = new MetricBranch();
        String urlMainBranch = BranchUtil.getUrl(project, mainBranch);
        String urlSecondaryBranch = BranchUtil.getUrl(project, secondaryBranch);

        System.out.println("urlMainBranch="+urlMainBranch);
        System.out.println("urlSecondaryBranch="+urlSecondaryBranch);

        
        //OceanoUserDao oceanoUserDao = ObjectFactory.getObj(OceanoUserDaoImpl.class);
        // TODO pegar do usuario logado
        //OceanoUser userRafaelOceano = oceanoUserDao.getByLogin("rafaelss");
        //PolvoMetricService polvoMetricService = ObjectFactory.getObj(PolvoMetricService.class);
        //MetricValue metricValue = polvoMetricService.extractMetricFromRevision(project, userRafaelOceano);

        // total de arquivos do ramo principal
        //int qtdArqRamoPrincipal;
        // total de arquivos do ramo secundario
        //int qtdArqRamoSecundario;
        // getAllFiles nao esta funcionando para o repositorio de teste
        //qtdArqRamoPrincipal = getAllFiles(urlMainBranch, project).size();
        //qtdArqRamoSecundario = getAllFiles(urlSecondaryBranch, project).size();
 
        String ws = "c:\\checkout\\" + project.getConfigurationItem().getName() + "\\" + BranchUtil.getLastValuePath(urlMainBranch);
        String ws2 = "";
        // work around para o oceano-core
        if (urlSecondaryBranch.contains("Oceano-Core-Peixe-Espada")){
            ws2 = "c:\\checkout\\" + project.getConfigurationItem().getName() + "\\Oceano-Core-Peixe-Espada";
        }
        else {
            ws2 = "c:\\checkout\\" + project.getConfigurationItem().getName() + "\\" + BranchUtil.getLastValuePath(urlSecondaryBranch);
        }
        
        // metricas com o calculo feito no servidor (nao necessitam de checkout)
        long idBranchingMetric = branchingMetric.getId().longValue();
        System.out.println("idBranchingMetric=" + idBranchingMetric);
        if (idBranchingMetric == Constantes.METR_RAMIF_ARQ_COBERTURA || idBranchingMetric == Constantes.METR_RAMIF_ARQ_PRECISAO ||
            idBranchingMetric >= Constantes.METR_RAMIF_CONFL_FISICOS) {

            checkout_update(ws, urlMainBranch, numberRevision);
            checkout_update(ws2, urlSecondaryBranch, numberRevision);
        }


        // total de arquivos do ramo principal
        int qtdArqRamoPrincipal = contarArquivos(ws);
        //int qtdArqRamoPrincipal = 1;
        // total de arquivos do ramo secundario
        int qtdArqRamoSecundario = contarArquivos(ws2);
        //int qtdArqRamoSecundario = 1;

        System.out.println("qtdArqRamoPrincipal=" + qtdArqRamoPrincipal);
        System.out.println("qtdArqRamoSecundario=" + qtdArqRamoSecundario);
        
        metricBranch.setQtdArqRamoPrincipal(qtdArqRamoPrincipal);
        metricBranch.setQtdArqRamoSecundario(qtdArqRamoSecundario);

        //String diffString = diffBranch(project, urlMainBranch, urlSecondaryBranch, numberRevision);
        //metricBranch = metricDiverg(diffString, metricBranch);


        if (idBranchingMetric < Constantes.METR_RAMIF_CONFL_FISICOS) {
            String diffString = diffBranch(project, urlMainBranch, urlSecondaryBranch, numberRevision);
            metricBranch = metricDiff(diffString, metricBranch, branchingMetric);
        }
        else {
            //String ws = "c:\\checkout\\" + project.getConfigurationItem().getName() + "\\" + BranchUtil.getLastValuePath(urlMainBranch) + "\\";
            String wsTmp = "c:\\checkout\\tmp\\";
            //checkout_update(ws, urlMainBranch, numberRevision);
            try{
                FileUtils.deleteDirectory(new File(wsTmp));
                System.out.println("c:\\checkout\\tmp\\ deletado");
            }
            catch (Exception ex) {
                System.out.println("c:\\checkout\\tmp\\ não encontrado!");
            }
            FileUtils.copyDirectory(new File(ws), new File(wsTmp));

            ProjectUser projectUser = BranchUtil.getProjectUser(project);
            int qtdConfFisico = svnByCLI.doMerge(projectUser, urlSecondaryBranch, wsTmp, numberRevision);
            System.out.println("qtdConfFisico=" + qtdConfFisico);
            if (idBranchingMetric == Constantes.METR_RAMIF_CONFL_FISICOS) {
                 metricBranch.setQtdMetrica(qtdConfFisico);
            }
            else {
                if (qtdConfFisico > 0) {
                    // Sera tratado para informar ao usuario que nao será possivel analisar o merge sintatico, pois há conflitos fisicos
                    metricBranch.setQtdMetrica(-1);
                }
                else {
                    String pathSettings = "c:\\Users\\Rafael\\.m2\\settings.xml";
                    String repositorioLocal = "c:\\Users\\Rafael\\.m2\\repository";

                    Maven mvn = new Maven();
                    mvn.setPathSettings(pathSettings);
                    mvn.setRepositorioLocal(repositorioLocal);
                    mvn.setUrlProjeto(wsTmp);

                    String ret;
                    int qtdConfSintatico = 0;

                    try{
                        List<Throwable> listSintatico = mvn.compila();
                        System.out.println(listSintatico);
                        ret = Casting.ListTrowableToString(listSintatico).toString();
                        System.out.println("retSintatico=" + ret);
                    }
                    catch(NullPointerException ne){
                        qtdConfSintatico = 0;
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    System.out.println("qtdSintatico=" + qtdConfSintatico);

                    if (idBranchingMetric == Constantes.METR_RAMIF_CONFL_SINTATICOS) {
                        metricBranch.setQtdMetrica(qtdConfSintatico);
                    }
                    else {
                        if (qtdConfSintatico > 0) {
                            // Sera tratado para informar ao usuario que nao será possivel analisar o merge semantico, pois há conflitos sintaticos
                            metricBranch.setQtdMetrica(-2);
                        }
                        else {
                            int qtdConfSemantico = 0;
                            try{
                                List<Throwable> listSemantico = mvn.testa();
                                ret = Casting.ListTrowableToString(listSemantico).toString();
                                System.out.println("retSemantico=" + ret);
                            }catch(NullPointerException ne){
                                qtdConfSemantico = 0;
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            System.out.println("qtdSemantico=" + qtdConfSemantico);
                        }
                    }
                }
            }
        }
       

        return metricBranch;
    }

    private static int contarArquivos(String path) throws Exception {
        //System.out.println(path);
        int contArq = 0;
        File file=new File(path);

        for(File arq:file.listFiles()){
            //condição para pegar só os arquivos, e nao diretórios
            if(arq.isFile()){
                contArq++;
                //System.out.println("Arquivo "+(contArq)+": "+arq.getName());
            }
            else {
                contArq = contArq + contarArquivos(path + "\\" + arq.getName());
            }
        }

        return contArq;
    }

    private MetricBranch metricDiverg(String diffString, MetricBranch metricBranch) {
        //System.out.println("diffString="+diffString);
        int qtd = 0;
        boolean newFile = false;

        String[] lines = diffString.split("\n");

        for (String line : lines) {
            if (line.startsWith("+++")) {
                newFile = true;
            }
            if (newFile && line.startsWith("@@")) {
                if (!line.contains("-0,0")) {
                    qtd++;
                }
                newFile = false;
            }
        }

        System.out.println("Qtd Diverg=" + qtd);
        if (metricBranch.getQtdDivergencia() == 0) {
            float calcDiverg = new Integer(metricBranch.getQtdArqRamoPrincipal() - qtd).floatValue() / new Integer(metricBranch.getQtdArqRamoPrincipal()).floatValue();
            metricBranch.setQtdDivergencia(new Float(calcDiverg * 100).intValue());
            System.out.println("Metrica Diverg=" + metricBranch.getQtdDivergencia());
        }
        return metricBranch;
    }


    private void checkout_update(String ws, String urlMainBranch, Long numberRevision) throws SVNException {
        Subversion svn = new Subversion();
        if (!(new File(ws)).exists()) {
            svn.iniciaRepositorio(urlMainBranch);
            long revCheckout = svn.checkout(urlMainBranch, ws);
            System.out.println("revCheckout=" + revCheckout);
        } else {
            svn.update(ws, SVNRevision.create(numberRevision.longValue()));
            System.out.println("revUpdate=" + numberRevision);
        }
    }

    private String diffBranch(SoftwareProject project, String urlMainBranch, String urlSecondaryBranch, Long numberRevision) throws Exception {
        String diffString = null;
        ProjectUser projectUser = BranchUtil.getProjectUser(project);
        projectUser.setProject(project);

        diffString = svnBySVNKit.doDiff(projectUser, urlMainBranch, urlSecondaryBranch, numberRevision).toString();

        return diffString;
    }

    private MetricBranch metricDiff(String diffString, MetricBranch metricBranch, BranchingMetric branchingMetric) {
        //System.out.println("diffString="+diffString);
        int qtd = 0;
        int ret = 0;
        long idBranchingMetric = branchingMetric.getId().longValue();

        String[] lines = diffString.split("\n");

        //Metrica Qtd Arq Alterados LP
            qtd = metricQtdFileUpdateMB(lines);
            System.out.println("Quantidade de Artefatos Modificados na Linha Principal=" + qtd);
        if (idBranchingMetric == Constantes.METR_RAMIF_ARQ_ALTERADOS_RP) {
            ret = qtd;
        }

        //Metrica Qtd Arq Alterados Ramo
        int qtdFileUpdateSB = metricQtdFileUpdateSB(lines);
        System.out.println("Quantidade de Artefatos Modificados no Ramo=" + qtdFileUpdateSB);
        if (idBranchingMetric == Constantes.METR_RAMIF_ARQ_ALTERADOS_RS) {
            ret = qtdFileUpdateSB;
        }

        //Metrica Qtd Arq modificados em comum
        qtd = 0;
            boolean newFile = true;
            for (String line : lines) {
                if (line.startsWith("Index")) {
                    newFile = true;
                }
                if (newFile && line.startsWith("@@")) {
                    if (!line.contains("0,0")) {
                        qtd++;
                    }
                    newFile = false;
                }
            }
            System.out.println("Quantidade de Artefatos Modificados em Comum=" + qtd);
        if (idBranchingMetric == Constantes.METR_RAMIF_ARQ_MODIF_EM_COMUM) {
            ret = qtd;
        }

        //Metrica Qtd Arq Conflito
//        qtd = 0;
//        if (flagAllMetric || idBranchingMetric == Constantes.METR_RAMIF_ARQ_CONFLITOS) {
//            boolean flag = false;
//            for (String line : lines) {
//                if (flag && line.startsWith("+") && !line.startsWith("+++")) {
//                    qtd++;
//                }
//                flag = false;
//
//                if (line.startsWith("-") && !line.startsWith("---")) {
//                    flag = true;
//                }
//            }
//            System.out.println("Quantidade de Blocos de Linhas Alteradas em Comum=" + qtd);
//        }

        //Metrica Cobertura por Arq
        int qtdFileNewSB = metricQtdFileNewSB(lines);
        int qtdFileDeletedSB = metricQtdFileDeletedSB(lines);
        // qtd de arq antes da criação do ramo
        int qtdFileOrig = metricBranch.getQtdArqRamoSecundario() - qtdFileNewSB + qtdFileDeletedSB;
        System.out.println("Qtd Arq no inicio do ramo=" + qtdFileOrig);
        // qtd de arq alterados em ambos
        int qtdFileUpdBoth = metricQtdFileUpdateBoth(lines);
        // qtd arq na criacao do ramo que nao foram modificados
        int qtdInters = qtdFileOrig - qtdFileUpdBoth;
        System.out.println("Qtd INTERSECAO=" + qtdInters);
        qtd = (qtdInters) * 100 / metricBranch.getQtdArqRamoPrincipal();
        System.out.println("Cobertura por Quantidade de Artefatos (%)=" + qtd);
        if (idBranchingMetric == Constantes.METR_RAMIF_ARQ_COBERTURA) {
            ret = qtd;
        }

        //Metrica Precisao por Arq
        qtd = (qtdInters) * 100 / metricBranch.getQtdArqRamoSecundario();
        System.out.println("Precisão por Quantidade de Artefatos (%)=" + qtd);
        if (idBranchingMetric == Constantes.METR_RAMIF_ARQ_PRECISAO) {
            ret = qtd;
        }

        //Metrica Qtd Linhas Diferentes LP
        qtd = 0;
            for (String line : lines) {
                if (line.startsWith("-") && !line.startsWith("---")) {
                    qtd++;
                }
            }
            System.out.println("Quantidade de Linhas Diferentes na Linha Principal=" + qtd);
        if (idBranchingMetric == Constantes.METR_RAMIF_LINHAS_ALTERADAS_RP) {
            ret = qtd;
        }

        //Metrica Qtd Linhas Diferentes no Ramo
        qtd = 0;
            for (String line : lines) {
                if (line.startsWith("+") && !line.startsWith("+++")) {
                    qtd++;
                }
            }
            System.out.println("Quantidade de Linhas Diferentes no Ramo=" + qtd);
        if (idBranchingMetric == Constantes.METR_RAMIF_LINHAS_ALTERADAS_RS) {
            ret = qtd;
        }

        metricBranch.setQtdMetrica(ret);
        return metricBranch;
    }

    private int metricQtdFileUpdate(String[] lines) {

        int qtd = 0;
        for (String line : lines) {
            if (line.startsWith("Index")) {
                qtd++;
            }
        }
        System.out.println("Qtd Arq Alterados =" + qtd);

        return qtd;
    }

    private int metricQtdFileUpdateMB(String[] lines) {

        int qtd = 0;
        boolean newFile = true;
        for (String line : lines) {
            if (line.startsWith("Index")) {
                newFile = true;
            }
            if (newFile && line.startsWith("@@")) {
                if (!line.contains("-0,0")) {
                    qtd++;
                }
                newFile = false;
            }
        }
        //System.out.println("Qtd Arq Alterados RP=" + qtd);

        return qtd;
    }

    private int metricQtdFileUpdateSB(String[] lines) {

        int qtd = 0;
        boolean newFile = true;
        for (String line : lines) {
            if (line.startsWith("Index")) {
                newFile = true;
            }
            if (newFile && line.startsWith("@@")) {
                if (!line.contains("+0,0")) {
                    qtd++;
                }
                newFile = false;
            }
        }
        //System.out.println("Qtd Arq Alterados RS=" + qtd);

        return qtd;
    }

    private static int metricQtdFileNewSB(String[] lines) {

        int qtd = 0;
        boolean newFile = true;
        for (String line : lines) {
            if (line.startsWith("Index")) {
                newFile = true;
            }
            if (newFile && line.startsWith("---") && !line.contains("(revision 0)")){
               newFile = false;
            }
            if (newFile && line.startsWith("@@")) {
                if (line.contains("-0,0")) {
                    qtd++;
                }
                newFile = false;
            }
        }
        System.out.println("Qtd Arq Novos RS=" + qtd);

        return qtd;
    }

    private static int metricQtdFileDeletedSB(String[] lines) {

        int qtd = 0;
        boolean newFile = true;
        for (String line : lines) {
            if (line.startsWith("Index")) {
                newFile = true;
            }
            if (newFile && line.startsWith("+++") && line.contains("(revision 0)")){
               newFile = false;
            }
            if (newFile && line.startsWith("@@")) {
                if (line.contains("+0,0")) {
                    qtd++;
                }
                newFile = false;
            }
        }
        System.out.println("Qtd Arq Deletados RS=" + qtd);

        return qtd;
    }

    private int metricQtdFileUpdateBoth(String[] lines) {

        int qtd = 0;
        boolean newFile = true;
        for (String line : lines) {
            if (line.startsWith("Index")) {
                newFile = true;
            }
            if (newFile && line.startsWith("@@")) {
                if (!line.contains("-0,0") || !line.contains("+0,0")) {
                    qtd++;
                }
                newFile = false;
            }
        }
        System.out.println("Qtd Arq Alterados Ambos=" + qtd);

        return qtd;
    }

    public List<String> getBranches(SoftwareProject project) throws Exception {
        ProjectUser projectUser = BranchUtil.getProjectUser(project);

        return svnBySVNKit.getBranches(project, projectUser);
    }

    public List<String> getAllFiles(String url, SoftwareProject project) throws Exception {
        ProjectUser projectUser = BranchUtil.getProjectUser(project);

        return svnByCLI.getAllFiles(url, projectUser);
    }
}
