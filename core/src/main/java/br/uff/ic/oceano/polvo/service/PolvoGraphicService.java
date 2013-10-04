    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.polvo.service;

import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Branch;
import br.uff.ic.oceano.core.model.BranchingMetric;
import br.uff.ic.oceano.core.model.BranchingModel;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_SVNKit;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.polvo.controle.Constantes;
import br.uff.ic.oceano.polvo.util.EdgeGraph;
import br.uff.ic.oceano.polvo.util.BranchUtil;
import br.uff.ic.oceano.polvo.util.MetricBranch;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 *
 * @author Rafael
 */
public class PolvoGraphicService {

    private static List<EdgeGraph> edgesGraph;
    private static Long idProject;
    private static Long idBranchingMetric;
    private static PolvoBranchService polvoBranchService = ObjectFactory.getObjectWithDataBaseDependencies(PolvoBranchService.class);
    private static int lessLimitMetric;
    private static int upperLimitMetric;

    /**
     * @param args the command line arguments
     */
    public static void gerarVisaoGeral(SoftwareProject project, BranchingMetric branchingMetric) throws Exception {
        idProject = project.getId();
        idBranchingMetric = branchingMetric.getId();

        try {
            SVN_By_SVNKit svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
            svn.initRepositoryFactory(project.getRepositoryUrl());

            ProjectUser projectUser = BranchUtil.getProjectUser(project);
            ISVNAuthenticationManager aManager = SVNWCUtil.createDefaultAuthenticationManager(projectUser.getLogin(), projectUser.getPassword());

            System.out.println("project: " + project);
            ConfigurationItem ci = project.getConfigurationItem();
            String urlBranch = PathUtil.getWellFormedURL(ci.getBaseUrl(), ci.getBranchPath());
            SVNURL url = SVNURL.parseURIEncoded(urlBranch);

            SVNRepository repos = SVNRepositoryFactory.create(url);
            repos.setAuthenticationManager(aManager);

            long headRevision = repos.getLatestRevision();
            Collection<SVNDirEntry> entriesList = repos.getDir("", headRevision, null, (Collection) null);

            String[] targetPaths = new String[1];
            targetPaths[0] = "/" + project.getConfigurationItem().getName() + "/" + project.getConfigurationItem().getBranchPath() + "/";
            Collection<SVNLogEntry> logEntryList = repos.log(targetPaths, null, 0, -1, true, true);
            Set changedPathsSet;
            Iterator changedPaths;
            boolean achouPai;
            String copyPath;

            edgesGraph = new ArrayList<EdgeGraph>();
            EdgeGraph edgeGraph;
            lessLimitMetric = Integer.MAX_VALUE;
            upperLimitMetric = Integer.MIN_VALUE;

            for (SVNDirEntry entry : entriesList) {
                achouPai = false;
                for (SVNLogEntry logEntry : logEntryList) {
                    //System.out.println("logEntry: " + logEntry);
                    changedPathsSet = logEntry.getChangedPaths().keySet();
                    changedPaths = changedPathsSet.iterator();

                    while (changedPaths.hasNext() && !achouPai) {
                        SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                        if (entryPath.getCopyPath() != null && entryPath.getPath().endsWith(entry.getName())) {
                            copyPath = BranchUtil.getLastValuePath(entryPath.getCopyPath());
                            for (SVNDirEntry entry2 : entriesList) {
                                if (copyPath.equals("trunk") || copyPath.equals(entry2.getName())) {
                                    edgeGraph = new EdgeGraph();
                                    edgeGraph.setSource(copyPath);
                                    edgeGraph.setTarget(BranchUtil.getLastValuePath(entryPath.getPath()));
                                    edgeGraph.setBeginRev(entryPath.getCopyRevision());
                                    edgeGraph.setFlag(false);

                                    Branch mainBranch = new Branch();
                                    mainBranch.setName(edgeGraph.getSource());
                                    Branch secondaryBranch = new Branch();
                                    secondaryBranch.setName(edgeGraph.getTarget());
                                    BranchingModel branchingModel = new BranchingModel();

                                    try {
                                        // comparar os branches
                                        MetricBranch metricBranch = polvoBranchService.compareBranch(project, mainBranch, secondaryBranch, branchingModel, branchingMetric);
                                        //MetricBranch metricBranch = new MetricBranch();
                                        //metricBranch.setQtdMetrica(0);

                                        edgeGraph.setMetric(metricBranch.getQtdMetrica());
                                        if (metricBranch.getQtdMetrica() < lessLimitMetric) {
                                            lessLimitMetric = metricBranch.getQtdMetrica();
                                        }
                                        if (metricBranch.getQtdMetrica() > upperLimitMetric) {
                                            upperLimitMetric = metricBranch.getQtdMetrica();
                                        }

                                        edgesGraph.add(edgeGraph);

                                        System.out.println("path: " + entryPath.getPath());
                                        System.out.println("copyPath: " + entryPath.getCopyPath());

                                         achouPai = true;
                                    }
                                    catch (VCSException ex) {
                                        System.out.println("Deu erro!!!");
                                        System.out.println("path: " + entryPath.getPath());
                                        System.out.println("copyPath: " + entryPath.getCopyPath());
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    if (achouPai) {
                        break;
                    }
                }
            }

        }
        catch (SVNException e) {
            e.printStackTrace();
        }

        Element treebolic = new Element("treebolic");
        treebolic.setAttribute("toolbar", "false");

        Element tree = new Element("tree");
        tree.setAttribute("backcolor", "e3e3ff");
        tree.setAttribute("expansion", "0.8");
        tree.setAttribute("fontface", "Lucida Sans Regular");
        tree.setAttribute("fontsize", "20");
        tree.setAttribute("fontsizestep", "2");
        tree.setAttribute("sweep", "1.0");

        Element nodes = new Element("nodes");
        nodes.setAttribute("backcolor", "aeaeff");
        nodes.setAttribute("forecolor", "000000");

        Element default_treeedge = new Element("default.treeedge");
        default_treeedge.setAttribute("color", "000000");
        default_treeedge.setAttribute("stroke", "solid");
        default_treeedge.setAttribute("toterminator", "tf");

        nodes.addContent(default_treeedge);

        Element nodeRoot = new Element("node");
        nodeRoot.setAttribute("backcolor", "0000ff");
        nodeRoot.setAttribute("forecolor", "ffffff");
        nodeRoot.setAttribute("id", "root");

        Element label = new Element("label");
        label.addContent(project.getConfigurationItem().getTrunkPath());
        nodeRoot.addContent(label);

        System.out.println("lessLimitMetric=" + lessLimitMetric + " / upperLimitMetric=" + upperLimitMetric);
        procuraFilhos(project.getConfigurationItem().getTrunkPath(), nodeRoot);

        nodes.addContent(nodeRoot);
        tree.addContent(nodes);
        treebolic.addContent(tree);

        Document doc = new Document();
        doc.setRootElement(treebolic);


        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("C:/Users/Rafael/Documents/NetBeansProjects/oceano-web/trunk/target/oceano-web- 2.0.0-glassfish/privado/polvo/visaoGeral.xml"), "UTF8"));
            XMLOutputter xout = new XMLOutputter();
            xout.output(doc,out);

            System.out.println("XML criado com sucesso!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void procuraFilhos(String ramo, Element nodePai) {
        Element node, label, treeedge, img, a;
        String colorEdge;
        int middleLimitMetric = (lessLimitMetric + upperLimitMetric) / 2;

        String color1 = "00FF00"; //green
        String color2 = "FFFF00"; //yellow
        String color3 = "FFA500"; //orange
        String color4 = "FF0000"; //red
        // se for metrica cobertura ou precisao, inverte logica das cores
        if (idBranchingMetric == Constantes.METR_RAMIF_ARQ_COBERTURA || idBranchingMetric == Constantes.METR_RAMIF_ARQ_PRECISAO) {
            color1 = "FF0000"; //red
            color2 = "FFA500"; //orange
            color3 = "FFFF00"; //yellow
            color4 = "00FF00"; //green
        }

        for (EdgeGraph edgeGraph : edgesGraph) {
            if (edgeGraph.getSource().equals(ramo)) {
                System.out.println(edgeGraph.getSource() + " -> " + edgeGraph.getTarget() + ": " + edgeGraph.getMetric());

                node = new Element("node");
                //***treeedge.setAttribute("backcolor", "ff0000");
                node.setAttribute("backcolor", "ffcc00");
                node.setAttribute("id", "id-" + edgeGraph.getTarget());

                label = new Element("label");
                //label.addContent(edgeGraph.getTarget() + " (" + edgeGraph.getMetric() + ")");
                label.addContent(edgeGraph.getTarget());
                node.addContent(label);

                treeedge = new Element("treeedge");
                if (edgeGraph.getMetric() < middleLimitMetric) {
                    if (edgeGraph.getMetric() <= (lessLimitMetric + middleLimitMetric) / 2) {
                        colorEdge = color1;
                    }
                    else {
                        colorEdge = color2;
                    }
                }
                else {
                    if (edgeGraph.getMetric() <= (middleLimitMetric + upperLimitMetric) / 2) {
                        colorEdge = color3;
                    }
                    else {
                        colorEdge = color4;
                    }
                }

                treeedge.setAttribute("color", colorEdge);
                treeedge.setAttribute("toterminator", "tf");
                node.addContent(treeedge);

                //img = new Element("img");
                //img.setAttribute("src", "light.png");
                //node.addContent(img);

                a = new Element("a");
                a.setAttribute("href", "http://localhost:8080/oceano/privado/polvo/visaoTemporalRamo.oceano"
                //a.setAttribute("href", "#{facesContext.externalContext.request.contextPath}/privado/polvo/visaoDetalhadaRamo.oceano"
                        + "?idProject=" + idProject
                        + "&nameMainBranch=" + edgeGraph.getSource()
                        + "&nameSecondaryBranch=" + edgeGraph.getTarget()
                        + "&idBranchingModel=7"  // TODO pendente pegar o vigente
                        + "&idBranchingMetric=" + idBranchingMetric
                        + "&beginRevision=" + edgeGraph.getBeginRev());
                //        + "&flagCompareBranch=1");
                node.addContent(a);

                procuraFilhos(edgeGraph.getTarget(), node);

                nodePai.addContent(node);
            }
        }
    }


}
