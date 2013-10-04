/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.util;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.OceanoCoreException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.*;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.service.*;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.expression.QMOOD;
import br.uff.ic.oceano.core.tools.metrics.extractors.cpp.LinesOfCodeExtractorCpp;
import br.uff.ic.oceano.core.tools.metrics.extractors.cpp.LinesOfCodeTotalExtractorCpp;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.MetricsLoader;
import br.uff.ic.oceano.core.tools.metrics.extractors.java.*;
import br.uff.ic.oceano.core.tools.metrics.service.MetricExtractorService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_SVNKit;
import br.uff.ic.oceano.ourico.model.Estado;
import br.uff.ic.oceano.ourico.service.EstadoService;
import br.uff.ic.oceano.peixeespada.model.Agent;
import br.uff.ic.oceano.peixeespada.model.Refactoring;
import br.uff.ic.oceano.peixeespada.service.AgentService;
import br.uff.ic.oceano.util.Output;
import br.uff.ic.oceano.util.file.PathUtil;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Heliomar
 *
 *
 */
public class DefaultDatabaseLoader {

    private static MetricExtractorService metricExtractorService = ObjectFactory.getObjectWithDataBaseDependencies(MetricExtractorService.class);
    private static MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);
    private static RevisionService configurationService = ObjectFactory.getObjectWithDataBaseDependencies(RevisionService.class);
    private static OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
    private static RepositoryService repositoryService = ObjectFactory.getObjectWithDataBaseDependencies(RepositoryService.class);
    private static SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private static ProjectUserService projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
    private static RefactoringService transformationService = ObjectFactory.getObjectWithDataBaseDependencies(RefactoringService.class);
    private static QualityAttributeService qualityAttributeService = ObjectFactory.getObjectWithDataBaseDependencies(QualityAttributeService.class);
    private static ConfigurationItemService configurationItemService = ObjectFactory.getObjectWithDataBaseDependencies(ConfigurationItemService.class);
    private static Repository SVN;
    private static SoftwareProject oceano;
    private static AgentService agentService = ObjectFactory.getObjectWithDataBaseDependencies(AgentService.class);
    private static EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);

    public static void insertDefaultDataProduction() {

        try {
            OceanoUser user = new OceanoUser();
            user.setName("Administrador");
            user.setEmail("@");
            user.setLogin("admin");
            user.setPassword("admin");
            oceanoUserService.save(user);

            insertMetrics();
            insertDerivedMetricQualityAttributes();

            insertRepositories();
            insertQualityAttributes();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        insertRefactorings();
    }

    public static synchronized void insertDefaultData() throws OceanoCoreException {
        try {
            insertOceanoUsers();
            insertMetrics();
            insertDerivedMetricQualityAttributes();

            insertRefactorings();
            insertRepositories();

            insertConfigurationItemsAndProjects();

            insereCheckOut();

            insereEstado();

            insertUserProjects();
            insertRevisions();
            insertQualityAttributes();
            insertAgents();
        } catch (Exception ex) {
            throw new OceanoCoreException(ex);
        }

    }

    private static void insereCheckOut() {
//        CheckOut co;
//
//        co = new CheckOut();
//        co.setAutobranch(1l);
//        co.setDiretorioAutobranch("diretorio");
//        co.setDiretorioProtegido("protegido");
//        co.setEstado("Testando");
//        co.setPolitica("moderada");
//        co.setProjeto("teste");
//        co.setRevisao(1l);
//        co.setUrlRepositorio("url repositorio");
//        co.setUsuario("marapao");
//        co.setWorkspace("workspace");
//
//        checkOutService.save(co);
//
//        co = new CheckOut();
//        co.setAutobranch(2l);
//        co.setDiretorioAutobranch("diretorio");
//        co.setDiretorioProtegido("protegido");
//        co.setEstado("Testando");
//        co.setPolitica("moderada");
//        co.setProjeto("teste");
//        co.setRevisao(2l);
//        co.setUrlRepositorio("url repositorio");
//        co.setUsuario("marapao");
//        co.setWorkspace("workspace");
//
//        checkOutService.save(co);
    }

    private static void insereEstado() {
        Estado es;

        es = new Estado();
        es.setInicio(new Date());
        es.setFim(new Date());
        es.setAutobranch(1l);
        es.setDescricao("Carga Default 1");

        estadoService.save(es);

        es = new Estado();
        es.setInicio(new Date());
        es.setFim(new Date());
        es.setAutobranch(1l);
        es.setDescricao("Carga Default 2");

        estadoService.save(es);

    }

    private static void insertRefactorings() {
        System.out.println("Inserindo Transformations...");

        Refactoring t = null;
        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_ENCAPSULE_FIELDS);
        t.setDescription("Transformação que substitui variáveis públicas por privadas e troca o acesso para gets e sets");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_CLEAN_IMPORTS);
        t.setDescription("Transformação que remove os imports não utilizados");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_PULL_UP_METHODS);
        t.setDescription("Transformação que move métodos para a super classe");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_PULL_UP_FIELDS);
        t.setDescription("Transformação que move campos para a super classe");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_PUSH_DOWN_METHODS);
        t.setDescription("Transformação que move métodos para a sub classe");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_PUSH_DOWN_FIELDS);
        t.setDescription("Transformação que move campos para a sub classe");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_ADD_DELEGATE_METHODS);
        t.setDescription("Transformação que adiciona métodos delegados para métodos de um field");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_EXTRACT_INTERFACES);
        t.setDescription("Transformação que extrai interfaces através dos métodos de uma classe");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_USE_SUPER_TYPES);
        t.setDescription("Transformação que substitui o uso dos tipos pelos seus supertipos onde for possível");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_CREATE_FACTORY_METHODS);
        t.setDescription("Transformação que cria métodos factory pela definição do construtor");
        transformationService.save(t);

        t = new Refactoring();
        t.setName(Refactoring.REFACTORING_INLINE_METHODS);
        t.setDescription("Transformação que coloca o corpo do método onde ele é chamado e o remove");
        transformationService.save(t);

//        for (int i = 0; i < 20; i++) {
//            t = new Refactoring();
//            t.setNome("Transformacao_" + i);
//            t.setDescricao("transformação gerada para efeito de testes_" + i);
//            t.setTipo(i % 2 + 1);
//            transformationService.save(t);
//        }

        System.out.println("    ok");
    }

    private static void insertMetrics() throws OceanoCoreException {
        try {
            Output.print("Inserting metrics...");

            for (Metric metric : MetricEnumeration.getMetrics()) {
                metricService.save(metric);
                Output.print("Inserted: " + metric);
            }
            insertMetricExtractors();
            
            MetricsLoader.load();

            Output.println("ok");
        } catch (Exception ex) {
            throw new OceanoCoreException(ex);
        }
    }

    private static void insertMetricExtractors() throws OceanoCoreException {
        System.out.print("Insering MetricExtractors...");

        insertMetricExtractor(MetricEnumeration.ACC, Language.JAVA, CyclomaticComplexityExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.ANA, Language.JAVA, AverageNumberOfAncestorsExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.CAM, Language.JAVA, CohesionAmongMethodsInClassExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.CIS, Language.JAVA, ClassInterfaceSizeExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.DAM, Language.JAVA, DataAccessExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.DCC, Language.JAVA, DirectClassCouplingExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.DSC, Language.JAVA, DesignSizeInClassesExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.LCOM, Language.JAVA, LackOfCohesionOfMethodsExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.LOC, Language.JAVA, LinesOfCodeExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.LOC, Language.CPP, LinesOfCodeExtractorCpp.class);
        insertMetricExtractor(MetricEnumeration.MFA, Language.JAVA, MeasureOfFunctionalAbstractionExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.MLOC, Language.JAVA, MethodLinesOfCodeExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.MOA, Language.JAVA, MeasureOfAggregationExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.NOA, Language.JAVA, NumberOfAttributesExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.NOH, Language.JAVA, NumberOfHierarchiesExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.NOI, Language.JAVA, NumberOfInterfacesExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.NOM, Language.JAVA, NumberOfMethodsExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.NOP, Language.JAVA, NumberOfPolymorphicMethodsExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.NORM, Language.JAVA, NumberOfOverriddenMethodsExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.NSF, Language.JAVA, NumberOfStaticAttributesExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.NSM, Language.JAVA, NumberOfStaticMethodsExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.RMA, Language.JAVA, AbstractnessExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.TCC, Language.JAVA, TotalCyclomaticComplexityExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.TLOC, Language.JAVA, LinesOfCodeTotalExtractorJava.class);
        insertMetricExtractor(MetricEnumeration.TLOC, Language.CPP, LinesOfCodeTotalExtractorCpp.class);
        insertMetricExtractor(MetricEnumeration.NOAM, Language.JAVA, NumberOfAccessorMethods.class);
        insertMetricExtractor(MetricEnumeration.NPA, Language.JAVA, NumberOfPublicAttributes.class);
        System.out.println("  ok");
    }

    private static void insertMetricExtractor(MetricEnumeration metricEnum, Language language, Class extractorClass) throws OceanoCoreException {
        try {
            //Get database metric, with ID
            Metric metric = metricService.getByAcronym(metricEnum.getAcronym());

            MetricExtractor metricExtractor = new MetricExtractor();
            metricExtractor.setMetric(metric);
            metricExtractor.setLanguage(language.name());
            metricExtractor.setMetricExtractorClass(extractorClass.getCanonicalName());
            metricExtractorService.save(metricExtractor);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new OceanoCoreException(ex);
        }
    }

    private static void insertOceanoUsers() {
        System.out.println("Inserindo Oceano Users...");

        OceanoUser user;

        user = new OceanoUser();
        user.setName("Daniel Castellani");
        user.setEmail("dancastellani@gmail.com");
        user.setLogin("xan");
        user.setPassword("xan");
        oceanoUserService.save(user);

        user = new OceanoUser();
        user.setName("Heliomar Kann da Rocha Santos");
        user.setEmail("heliokann@gmail.com");
        user.setLogin("kann");
        user.setPassword("kann");
        oceanoUserService.save(user);

        user = new OceanoUser();
        user.setName("Leonardo Gresta Paulino Murta");
        user.setEmail("leomurta@gmail.com");
        user.setLogin("leo");
        user.setPassword("leo");
        oceanoUserService.save(user);

        user = new OceanoUser();
        user.setName("Gleiph Ghiotto Lima de Menezes");
        user.setEmail("gleiphgh@gmail.com");
        user.setLogin("gleiph");
        user.setPassword("gleiph");
        oceanoUserService.save(user);

        user = new OceanoUser();
        user.setName("Leandro Ribeiro De Cicco");
        user.setEmail("leandrocicco@gmail.com");
        user.setLogin("cicco");
        user.setPassword("cicco");
        oceanoUserService.save(user);

        user = new OceanoUser();
        user.setName("Rafael de Souza Santos");
        user.setEmail("rafaelss@ic.uff.br");
        user.setLogin("rss");
        user.setPassword("rss");
        oceanoUserService.save(user);

        user = new OceanoUser();
        user.setName("Wallace Ribeiro");
        user.setEmail("...");
        user.setLogin("wallace");
        user.setPassword("wallace");
        oceanoUserService.save(user);

        user = new OceanoUser();
        user.setName("Daniel Heraclio");
        user.setEmail("dheraclio@gmail.com");
        user.setLogin("dheraclio");
        user.setPassword("dheraclio");
        oceanoUserService.save(user);

        System.out.println("    ok");
    }

    private static void insertRepositories() {
        System.out.println("Inserindo repositorios suportados...");
        try {
            Repository repository;

            repository = new Repository();
            repository.setName(new SVN_By_SVNKit().getName());
            repositoryService.save(repository);
            SVN = repository;

//            repository = new Repository();
//            repository.setName("CVS");
//            repositoryService.save(repository);
//
//            repository = new Repository();
//            repository.setName("Git");
//            repositoryService.save(repository);

        } catch (ServiceException ex) {
            System.out.println("    FAIL!");
        }
        System.out.println("    ok");
    }

    private static void insertConfigurationItemsAndProjects() {
        System.out.println("Inserindo Projetos...");
        SoftwareProject project = null;
        ConfigurationItem ci = null;
        try {
            ci = new ConfigurationItem();
            ci.setBaseUrl("http://svn.desenvolvimento.uff.br/uff/proac/academico-graduacao/");
            ci.setBranchPath("trunk");
            ci.setTrunkPath("branches");
            ci.setName("IdUFF");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("http://svn.desenvolvimento.uff.br/uff/proac/academico-graduacao/trunk/");
            project.setMavenProject(true);
            projectService.save(project);

            // local
            ci = new ConfigurationItem();
            ci.setBaseUrl("file:///C:/RepsPeixeEspada/rep_oceano/peixeespada/peixeespadacliente");
            ci.setBranchPath("trunk");
            ci.setTrunkPath("branches");
            ci.setName("PeixeEspada-Local");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("file:///C:/RepsPeixeEspada/rep_oceano/peixeespada/peixeespadacliente/trunk/");
            project.setMavenProject(true);
            projectService.save(project);

            ci = new ConfigurationItem();
            ci.setBaseUrl("http://svn.desenvolvimento.uff.br/uff/publico/publico-core");
            ci.setBranchPath("branches");
            ci.setTrunkPath("trunk");
            ci.setName("Publico Core");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("http://svn.desenvolvimento.uff.br/uff/publico/publico-core/trunk/");
            project.setMavenProject(true);
            projectService.save(project);
            oceano = project;

            ci = new ConfigurationItem();
            ci.setBaseUrl("http://svn.desenvolvimento.uff.br/uff/commons/utils");
            ci.setBranchPath("branches");
            ci.setTrunkPath("trunk");
            ci.setName("Commons Utils");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("http://svn.desenvolvimento.uff.br/uff/commons/utils/trunk/");
            project.setMavenProject(true);
            projectService.save(project);
            oceano = project;

            ci = new ConfigurationItem();
            ci.setBaseUrl("http://svn.desenvolvimento.uff.br/uff/proac/monitoria/monitoria-core");
            ci.setBranchPath("branches");
            ci.setTrunkPath("trunk");
            ci.setName("Monitoria - Core");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("http://svn.desenvolvimento.uff.br/uff/proac/monitoria/monitoria-core/trunk/");
            project.setMavenProject(true);
            projectService.save(project);
            oceano = project;

            ci = new ConfigurationItem();
            ci.setBaseUrl("http://svn.desenvolvimento.uff.br/propp-efomento/");
            ci.setBranchPath("branches");
            ci.setTrunkPath("trunk");
            ci.setName("Pibic");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("http://svn.desenvolvimento.uff.br/propp-efomento/trunk/");
            project.setMavenProject(true);
            projectService.save(project);
            oceano = project;

            ci = new ConfigurationItem();
            ci.setBaseUrl("http://svn.desenvolvimento.uff.br/uff/proppi/academico-pos-graduacao/academico-pos-graduacao-core/");
            ci.setBranchPath("branches");
            ci.setTrunkPath("trunk");
            ci.setName("Acadêmico de Pós Graduação - Core");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("http://svn.desenvolvimento.uff.br/uff/proppi/academico-pos-graduacao/academico-pos-graduacao-core/trunk/");
            project.setMavenProject(true);
            projectService.save(project);
            oceano = project;

            ci = new ConfigurationItem();
            ci.setBaseUrl("https://gems.ic.uff.br/svn/oceano/oceano-core");
            ci.setBranchPath("branches");
            ci.setTrunkPath("trunk");
            ci.setName("Oceano-Core");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("https://gems.ic.uff.br/svn/oceano/oceano-core/trunk");
            project.setMavenProject(true);
            projectService.save(project);
            oceano = project;

            ci = new ConfigurationItem();
            ci.setBaseUrl("https://gems.ic.uff.br/svn/oceano/oceano-web/");
            ci.setBranchPath("branches");
            ci.setTrunkPath("trunk");
            ci.setName("Oceano-web");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("https://gems.ic.uff.br/svn/oceano/oceano-web/trunk");
            project.setMavenProject(true);
            projectService.save(project);
            oceano = project;

            ci = new ConfigurationItem();
            ci.setBaseUrl("http://svn.apache.org/repos/asf/subversion/");
            ci.setBranchPath("branches");
            ci.setTrunkPath("trunk");
            ci.setName("subversion");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setRepositoryUrl("http://svn.apache.org/repos/asf/subversion/trunk");
            projectService.save(project);

            //NEOPZ
            ci = new ConfigurationItem();
            ci.setBaseUrl("http://neopz.googlecode.com/svn/");
            ci.setBranchPath("branches");
            ci.setTrunkPath("trunk");
            ci.setName("NEOPZ");
            ci.setRepository(SVN);
            configurationItemService.save(ci);
            project = new SoftwareProject();
            project.setConfigurationItem(ci);
            project.setLanguage(Language.CPP);
            project.setMavenProject(false);
            project.setRepositoryUrl("http://neopz.googlecode.com/svn/trunk");
            projectService.save(project);

            //NEOPZ Local
            String neopzLocalPath = "D:/DaNSIS/NeopzProjects/neopz_clone";
            if (PathUtil.exists(neopzLocalPath)) {
                ci = new ConfigurationItem();
                ci.setBaseUrl("http://neopz.googlecode.com/svn/");
                ci.setBranchPath("branches");
                ci.setTrunkPath("trunk");
                ci.setName("NEOPZ local");
                ci.setRepository(SVN);
                configurationItemService.save(ci);
                project = new SoftwareProject();
                project.setConfigurationItem(ci);
                project.setLanguage(Language.CPP);
                project.setMavenProject(false);
                project.setRepositoryUrl("file:///" + neopzLocalPath + "/trunk");
                projectService.save(project);
            }

        } catch (ServiceException ex) {
            System.out.println("    FAIL");
            throw new RuntimeException(ex);
        }
        System.out.println("    ok");
    }

    private static void insertUserProjects() throws ServiceException {
        List<SoftwareProject> projects = projectService.getAll();
        OceanoUser oceanoUsersKann = oceanoUserService.autenticarUsuario("kann", "kann");
        OceanoUser oceanoUsersDaniel = oceanoUserService.autenticarUsuario("xan", "xan");
        OceanoUser oceanoUsersLeo = oceanoUserService.autenticarUsuario("leo", "leo");
        OceanoUser oceanoUsersHeraclio = oceanoUserService.autenticarUsuario("dheraclio", "dheraclio");

        List<OceanoUser> listUsers = Arrays.asList(oceanoUsersKann, oceanoUsersDaniel, oceanoUsersLeo, oceanoUsersHeraclio);

        for (SoftwareProject project : projects) {
            //dados preenchidos apenas para testes, não necessariamente estão corretos
            for (OceanoUser oceanoUser : listUsers) {
                ProjectUser pu = new ProjectUser();
                pu.setProject(project);
                pu.setOceanoUser(oceanoUser);
                pu.setPassword(oceanoUser.getPassword());
                pu.setLogin(oceanoUser.getLogin());
                projectUserService.save(pu);
            }

        }
    }

    private static void insertQualityAttributes() throws ServiceException {
//1
        QualityAttribute attribute = new QualityAttribute();
        attribute.setDescricao("efetividade");
        attribute.setName(QualityAttribute.NAME_EFFECTIVENESS);
        qualityAttributeService.save(attribute);
//2
        attribute = new QualityAttribute();
        attribute.setDescricao("EXTENDIBILITY");
        attribute.setName(QualityAttribute.NAME_EXTENDIBILITY);
        qualityAttributeService.save(attribute);
//3
        attribute = new QualityAttribute();
        attribute.setDescricao("Flexibilidade");
        attribute.setName(QualityAttribute.NAME_FLEXIBILITY);
        qualityAttributeService.save(attribute);
//4
        /**
         * <FALTA a métrica CAM> attribute = new QualityAttribute();
         * attribute.setDescricao("Funcionalidade");
         * attribute.setNome(QualityAttribute.NAME_FUNCIONALITY);
         * qualityAttributeService.save(attribute); //5 attribute = new
         * QualityAttribute(); attribute.setDescricao("Reusabilidade");
         * attribute.setNome(QualityAttribute.NAME_REUSABILITY);
         * qualityAttributeService.save(attribute); //6 attribute = new
         * QualityAttribute(); attribute.setDescricao("Entendibilidade,
         * legibilidade");
         * attribute.setNome(QualityAttribute.NAME_UNDERSTANDIBILITY);
         * qualityAttributeService.save(attribute);
         */
    }

    private static void insertRevisions() {
        System.out.println("Inserindo Revisões...");

        Revision revision = new Revision();
        revision.setProject(oceano);
        revision.setNumber(0L);
        revision.setCommitDate(Calendar.getInstance());
        revision.setCommiter("Commiter" + System.currentTimeMillis());
        configurationService.save(revision);

        System.out.println("    ok");
    }

    private static void insertAgents() throws Exception {

        SoftwareProject peixeEspadaLocal = projectService.getByRepositoryUrl("file:///C:/RepsPeixeEspada/rep_oceano/peixeespada/peixeespadacliente/trunk/");

        Agent a = new Agent();
        a.setName("Kann");
        a.setActive(true);
        a.setQualityAttribute(qualityAttributeService.getByName(QualityAttribute.NAME_EFFECTIVENESS));
        a.setProject(oceano);
        agentService.salve(a);

        a = new Agent();
        a.setName("Kann2");
        a.setActive(true);
        a.setQualityAttribute(qualityAttributeService.getByName(QualityAttribute.NAME_EXTENDIBILITY));
        a.setProject(oceano);
        agentService.salve(a);

        a = new Agent();
        a.setName("Kann3");
        a.setActive(true);
        a.setQualityAttribute(qualityAttributeService.getByName(QualityAttribute.NAME_FLEXIBILITY));
        a.setProject(oceano);
        agentService.salve(a);

        a = new Agent();
        a.setName("Kann4");
        a.setActive(true);
        a.setQualityAttribute(qualityAttributeService.getByName(QualityAttribute.NAME_FLEXIBILITY));
        a.setProject(peixeEspadaLocal);
        agentService.salve(a);

        a = new Agent();
        a.setName("Kann5");
        a.setActive(true);
        a.setQualityAttribute(qualityAttributeService.getByName(QualityAttribute.NAME_EFFECTIVENESS));
        a.setProject(peixeEspadaLocal);
        agentService.salve(a);

        a = new Agent();
        a.setName("Kann6");
        a.setActive(true);
        a.setQualityAttribute(qualityAttributeService.getByName(QualityAttribute.NAME_EXTENDIBILITY));
        a.setProject(peixeEspadaLocal);
        agentService.salve(a);
    }

    private static void insertDerivedMetricQualityAttributes() {
        Metric metric;
//1
        metric = new Metric();
        metric.setAcronym(QMOOD.QA_EFFECTIVENESS);
        metric.setName(QMOOD.QA_EFFECTIVENESS);
        metric.setDescription(QMOOD.QA_EFFECTIVENESS);
        metric.setPreRelease(true);
        metric.setType(Metric.TYPE_FLOAT);
        metric.setExtratcsFrom(Metric.EXTRACTS_FROM_PROJECT);
        metric.setExtractsFromFont(false);
        metric.setExpression(QMOOD.QUALITY_ATTRIBUTE_EFFECTIVENESS);
        metric.setDerived(true);
        metricService.save(metric);
//2
        metric = new Metric();
        metric.setAcronym(QMOOD.QA_EXTENDABILITY);
        metric.setName(QMOOD.QA_EXTENDABILITY);
        metric.setDescription(QMOOD.QA_EXTENDABILITY);
        metric.setPreRelease(true);
        metric.setType(Metric.TYPE_FLOAT);
        metric.setExtratcsFrom(Metric.EXTRACTS_FROM_PROJECT);
        metric.setExtractsFromFont(false);
        metric.setExpression(QMOOD.QUALITY_ATTRIBUTE_EXTENDABILITY);
        metric.setDerived(true);

        metricService.save(metric);
//3
        metric = new Metric();
        metric.setAcronym(QMOOD.QA_FLEXIBILITY);
        metric.setName(QMOOD.QA_FLEXIBILITY);
        metric.setDescription(QMOOD.QA_FLEXIBILITY);
        metric.setPreRelease(true);
        metric.setType(Metric.TYPE_FLOAT);
        metric.setExtratcsFrom(Metric.EXTRACTS_FROM_PROJECT);
        metric.setExtractsFromFont(false);
        metric.setExpression(QMOOD.QUALITY_ATTRIBUTE_FLEXIBILITY);
        metric.setDerived(true);

        metricService.save(metric);
//4
        metric = new Metric();
        metric.setAcronym(QMOOD.QA_FUNCTIONALITY);
        metric.setName(QMOOD.QA_FUNCTIONALITY);
        metric.setDescription(QMOOD.QA_FUNCTIONALITY);
        metric.setPreRelease(true);
        metric.setType(Metric.TYPE_FLOAT);
        metric.setExtratcsFrom(Metric.EXTRACTS_FROM_PROJECT);
        metric.setExtractsFromFont(false);
        metric.setExpression(QMOOD.QUALITY_ATTRIBUTE_FUNCTIONALITY);
        metric.setDerived(true);

        metricService.save(metric);
//5
        metric = new Metric();
        metric.setAcronym(QMOOD.QA_REUSABILITY);
        metric.setName(QMOOD.QA_REUSABILITY);
        metric.setDescription(QMOOD.QA_REUSABILITY);
        metric.setPreRelease(true);
        metric.setType(Metric.TYPE_FLOAT);
        metric.setExtratcsFrom(Metric.EXTRACTS_FROM_PROJECT);
        metric.setExtractsFromFont(false);
        metric.setExpression(QMOOD.QUALITY_ATTRIBUTE_REUSABILITY);
        metric.setDerived(true);

        metricService.save(metric);
//6
        metric = new Metric();
        metric.setAcronym(QMOOD.QA_UNDERSTANDABILITY);
        metric.setName(QMOOD.QA_UNDERSTANDABILITY);
        metric.setDescription(QMOOD.QA_UNDERSTANDABILITY);
        metric.setPreRelease(true);
        metric.setType(Metric.TYPE_FLOAT);
        metric.setExtratcsFrom(Metric.EXTRACTS_FROM_PROJECT);
        metric.setExtractsFromFont(false);
        metric.setExpression(QMOOD.QUALITY_ATTRIBUTE_UNDERSTANDABILITY);
        metric.setDerived(true);

        metricService.save(metric);
    }

    public static boolean isDefaultDataInserted() {
        boolean result = true;
        List buffer;

        buffer = metricService.getAll();
        result &= (buffer != null && !buffer.isEmpty());

        buffer = oceanoUserService.getAll();
        result &= (buffer != null && !buffer.isEmpty());

        buffer = metricExtractorService.getAll();
        result &= (buffer != null && !buffer.isEmpty());
        return result;
    }
}
