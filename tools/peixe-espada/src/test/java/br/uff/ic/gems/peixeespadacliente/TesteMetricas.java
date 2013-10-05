package br.uff.ic.gems.peixeespadacliente;

import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.utils.Archive;
import br.uff.ic.gems.peixeespadacliente.utils.ProjectUtils;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinPackage;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.metrics.CyclicDependencyMetric;
import net.sf.refactorit.metrics.DipMetric;
import net.sf.refactorit.metrics.DirectCyclicDependencyMetric;
import net.sf.refactorit.metrics.DistanceMetric;
import net.sf.refactorit.metrics.EpMetric;
import net.sf.refactorit.metrics.LackOfCohesionMetric;
import net.sf.refactorit.metrics.LspMetric;
import net.sf.refactorit.metrics.NumberOfTrampsMetric;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class TesteMetricas {
    /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */
    private static Project project;

    @BeforeClass
    public static void setUpClass() throws Exception {
//        SetupUtils.setup();
        project = ProjectUtils.getProjectRefactoring(getProjectVCSFake());
         project.getProjectLoader().build();
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

    private static Archive getArquivo(String nomeArquivo){
        return new Archive(nomeArquivo);
    }

    @Test
    public void testDipMetric() {
        Archive archive = getArquivo("DipMetric.txt");
        for (Object object : project.getDefinedTypes()) {
            BinCITypeRef bcitr = (BinCITypeRef) object;
            archive.openAppendAndClose("Classe = " + bcitr.getName());
            archive.openAppendAndClose("    DipMetric =" + DipMetric.calculate(bcitr.getBinCIType()));
        }
    }

    @Test
    public void testDirectCyclicDependencyMetric() {
        Archive archive = getArquivo("DirectCyclicDependencyMetric.txt");
        for (BinPackage binPackage : project.getAllPackages()) {
            archive.openAppendAndClose("Package =" + binPackage.getQualifiedName());
            archive.openAppendAndClose("    DirectCyclicDependencyMetric = " + DirectCyclicDependencyMetric.calculate(binPackage));
        }
        
    }

    @Test
    public void testEpMetric() {
        Archive archive = getArquivo("EpMetric.txt");
        for (BinPackage binPackage : project.getAllPackages()) {
            archive.openAppendAndClose("Package =" + binPackage.getQualifiedName());
            archive.openAppendAndClose("    EpMetric = " + EpMetric.calculate(binPackage));
        }
    }

//    @Test
    public void testCyclicDependencyMetric() {
        CyclicDependencyMetric.calculate(null, null);
    }

    @Test
    public void testDisgnSizeInClass() {
         Archive archive = getArquivo("DisgnSizeInClass.txt");
         int count = 0;
         for (Object object : project.getDefinedTypes()) {
            final BinCITypeRef bcitr = (BinCITypeRef) object;
            BinCIType bcit = bcitr.getBinCIType();
            if(bcit.isClass()){
                count++;
                archive.openAppendAndClose("Classe = "+bcitr.getName());
            }else{
                System.out.println("Interface ou Abstrato "+bcitr.getName());
            }
        }
         archive.openAppendAndClose("Quantidade = "+count);
    }

//    @Test
    public void testLspMetric() {
        Archive archive = getArquivo("LspMetric.txt");
        for (BinPackage binPackage : project.getAllPackages()) {
             archive.openAppendAndClose("Package =" + binPackage.getQualifiedName());
             archive.openAppendAndClose("    LspMetric = " + LspMetric.calculate(binPackage));
        }
    }

    @Test
    public void testDistanceMetric() {
        Archive archive = getArquivo("DistanceMetric.txt");
        for (BinPackage binPackage : project.getAllPackages()) {
            archive.openAppendAndClose("Package =" + binPackage.getQualifiedName());
            archive.openAppendAndClose("    DistanceMetric = " + DistanceMetric.calculate(binPackage));
        }
//         DistanceMetric.calculate(instability, abstractness);
    }

    @Test
    public void testNumberOfTrampsMetric() {
        Archive archive = getArquivo("NumberOfTrampsMetric.txt");
        for (Object object : project.getDefinedTypes()) {
            final BinCITypeRef bcitr = (BinCITypeRef) object;
            BinMethod[] binMethods = bcitr.getBinCIType().getDeclaredMethods();
            archive.openAppendAndClose("Metodos da Classe = "+bcitr.getName());
            if(binMethods != null) {
                for (BinMethod binMethod : binMethods) {
                    archive.openAppendAndClose("    " + binMethod);
                    archive.openAppendAndClose("        NumberOfTrampsMetric = " + NumberOfTrampsMetric.calculate(binMethod));
                }
            }
        }
        
    }

    @Test
    public void testLackOfCohesionMetric() {
        Archive archive = getArquivo("LackOfCohesionMetric.txt");
         for (Object object : project.getDefinedTypes()) {
            BinCITypeRef bcitr = (BinCITypeRef) object;
            archive.openAppendAndClose("Classe = " + bcitr.getName());
            archive.openAppendAndClose("    LackOfCohesionMetric =" + LackOfCohesionMetric.calculate(bcitr.getBinCIType()));
        }
    }

    
    private static ProjectVCS getProjectVCSFake() {
        ProjectVCS projectVCS = new ProjectVCS();
        projectVCS.setName("Oceano");
        projectVCS.setLocalPath("G:\\Users\\Heliomar\\Documents\\NetBeansProjects\\P6\\");
        return projectVCS;
    }
}
