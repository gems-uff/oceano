/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente;

import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.utils.ProjectUtils;
import br.uff.ic.gems.peixeespadacliente.utils.SetupUtils;
import java.util.List;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.query.usage.FieldIndexer;
import net.sf.refactorit.query.usage.InvocationData;
import net.sf.refactorit.query.usage.ManagingIndexer;
import net.sf.refactorit.query.usage.MethodIndexer;
import net.sf.refactorit.query.usage.filters.BinMethodSearchFilter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Heliomar
 */
public class TesteBadSmeels {

    private static Project project;

    public TesteBadSmeels() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        project = ProjectUtils.getProjectRefactoring(getProjectVCSFake());
        project.getProjectLoader().build();
        SetupUtils.setup(project);

    }

    private static ProjectVCS getProjectVCSFake() {
        ProjectVCS projectVCS = new ProjectVCS();
        projectVCS.setName("Oceano");
        String separetor = System.getProperty("file.separator");
        projectVCS.setLocalPath(System.getProperty("user.home").concat(separetor).concat("Documents").concat(separetor).concat("NetBeansProjects").concat(separetor).concat("PE_Cliente"));
//        projectVCS.setLocalPath(System.getProperty("user.home").concat(separetor).concat("Documents").concat(separetor).concat("NetBeansProjects").concat(separetor).concat("PeixeEspadaCliente"));
        return projectVCS;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

//    @Before
//    public void setUp() {
//    }
//
//    @After
//    public void tearDown() {
//    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
//    @Test
    public void testeFindFieldsNotUsedInSubClass() {
        ManagingIndexer indexer = new ManagingIndexer();
        for (Object object : project.getDefinedTypes()) {
//            SetupUtils.setup();

            BinCITypeRef bcitr = (BinCITypeRef) object;
            // pega todas as dependências
            List<BinCITypeRef> listaSubClasses = bcitr.getAllSubclasses();

            if (listaSubClasses != null && !listaSubClasses.isEmpty()) {
                // verifica se algum campo da superClasse está sendo utilizado por alguma classe.
                for (BinField field : bcitr.getBinCIType().getDeclaredFields()) {
                    // precisa para buscar corretamente os campos e dependências
                    new FieldIndexer(indexer, field, true);
                    if (field.isPrivate()) {
                        indexer.visit(field.getCompilationUnit());
                    } else {
                        indexer.visit(field.getOwner().getProject());
                    }

                    List lista = indexer.getInvocationsForProject(project);
                    for (Object object1 : lista) {
                        System.out.println("object1 = " + object1);
                    }
                }
            }
            BinCIType classe = bcitr.getBinCIType();
            System.out.println("classe = " + classe);
//            archive.openAppendAndClose("    LackOfCohesionMetric =" + LackOfCohesionMetric.calculate(.getde));
        }
    }

    @Test
    public void testeFindMethodNotUsedInSubClass() {
        ManagingIndexer indexer = new ManagingIndexer();

        for (Object object : project.getDefinedTypes()) {

            BinCITypeRef bcitr = (BinCITypeRef) object;
            // pega todas as dependências
            List<BinCITypeRef> listaSubClasses = bcitr.getAllSubclasses();

            if (listaSubClasses != null && !listaSubClasses.isEmpty()) {
                // verifica se algum campo da superClasse está sendo utilizado por alguma classe.
                BinCIType classe = bcitr.getBinCIType();
                System.out.println("Classe ===> " + classe.getName());

                for (BinMethod method : bcitr.getBinCIType().getDeclaredMethods()) {

                    System.out.println("    Método -> "+method.getQualifiedName());
                    try {
                        new MethodIndexer(indexer, method, new BinMethodSearchFilter(false, true));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("e = " + e);
                    }
                    if (method.isPrivate()) {
                        indexer.visit(method.getCompilationUnit());
                    } else {
                        indexer.visit(method.getOwner().getProject());
                    }

                    List<InvocationData> lista = indexer.getInvocations();
                    for (InvocationData data : lista) {
                        System.out.println("    Classe da chamada = " + data.getCompilationUnit().getName());
                        System.out.println("    Método Onde é chamado = " + data.getWhere());
                        System.out.println("    Qual é o método chamado = " + data.getWhat());
                        System.out.println("");
                    }
                    System.out.println("\n");
                }
            }
//            archive.openAppendAndClose("    LackOfCohesionMetric =" + LackOfCohesionMetric.calculate(.getde));
        }
    }
}
