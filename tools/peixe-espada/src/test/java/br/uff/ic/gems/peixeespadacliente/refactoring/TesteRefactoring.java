package br.uff.ic.gems.peixeespadacliente.refactoring;

import br.uff.ic.gems.peixeespadacliente.tool.InlineMethods;
import br.uff.ic.gems.peixeespadacliente.tool.CreateFactoryMethods;
import br.uff.ic.gems.peixeespadacliente.tool.UseSuperTypes;
import br.uff.ic.gems.peixeespadacliente.tool.ExtractInterfaces;
import br.uff.ic.gems.peixeespadacliente.tool.AddDelegateMethods;
import br.uff.ic.gems.peixeespadacliente.tool.CleanImports;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.tool.EncapsulateFields;
import br.uff.ic.gems.peixeespadacliente.tool.PushDownFields;
import br.uff.ic.gems.peixeespadacliente.tool.PushDownMethods;
import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;
import br.uff.ic.gems.peixeespadacliente.symptom.PullPushSymptom;
import br.uff.ic.gems.peixeespadacliente.resolution.PullPushResolution;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCSTest;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import br.uff.ic.gems.peixeespadacliente.tool.PullUpFields;
import br.uff.ic.gems.peixeespadacliente.tool.PullUpMethods;
import br.uff.ic.gems.peixeespadacliente.tool.applier.RefactoringApplier;
import br.uff.ic.gems.peixeespadacliente.tool.factory.FactoryRefactoringTool;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.QualityAttribute;
import java.io.File;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
/**
 * Unit test for simple App.
 */
public class TesteRefactoring {
    /*
     * To change this template, choose Too7ls | Templates
     * and open the template in the editor.
     */

    private static ProjectVCS vcs = null;

    @AfterClass
    public static void tearDownClass() throws Exception {
        ProjectVCSTest.deleteDir(new File("Test"+File.separator+"target").getAbsoluteFile());
    }

    public ProjectVCS getTestProject() {
        if (vcs != null) {
//            return vcs;
        }
        File target = new File("Test"+File.separator+"target").getAbsoluteFile();
        File origin = new File("Test"+File.separator+"origin"+File.separator+"TestProject").getAbsoluteFile();
        ProjectVCSTest projectVCS = new ProjectVCSTest(target);
        projectVCS.setRepositoryUrl(origin.getAbsolutePath());
        projectVCS.setName("TestProject");
        
//        ProjectVCS projectVCS = new ProjectVCS();
//        projectVCS.setRepositoryUrl("file:///C:/experimentos/repositorios/peixe-espada/trunk");
//        projectVCS.setName("Peixe Espada");
        vcs = projectVCS;
        return projectVCS;
    }
    
    public Object assertStringInObjectList(String verified, List list) throws Throwable {
        for (Object object : list) {
            if (verified.equals(object.toString()))
                return object;
        }
        assertTrue(false);
        return null;
    }
    
    @Test
    public void testPullUpMethods() throws Throwable {
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
//        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "PullUpMethods");

        PullUpMethods pullUpMethods = FactoryRefactoringTool.getRefactoringTool(PullUpMethods.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = pullUpMethods.findAllSymptoms();
        
        Symptom symptom = (Symptom) assertStringInObjectList("xyz to MidClass", symptoms);
        List<PullPushResolution> resolutions = ((PullPushSymptom) symptom).generateResolutions(agenteTrabalhador, false);
        assertEquals(resolutions.size(), 1);
        PullPushResolution resolution = resolutions.get(0);
        StringBuilder sb = new StringBuilder();
        assertEquals(resolution.toString(), "xyz to MidClass");
        assertTrue(resolution.applyCalculateQA(agenteTrabalhador, sb));
        QualityAttribute qa = new QualityAttribute();
        qa.setCurrentValue(new Double(1.0));
        resolution.setResolutionQuality(qa);
        pullUpMethods.revertMoDifications();
        
        symptom = (Symptom) assertStringInObjectList("xyz to UpClass", symptoms);
        resolutions.addAll(((PullPushSymptom) symptom).generateResolutions(agenteTrabalhador, false));
        for (PullPushResolution r : resolutions) {
            if (r.getResolutionQuality() == null) {
                assertTrue(resolution.applyCalculateQA(agenteTrabalhador, sb));
                if (r.getResolutions().keySet().contains("Implementation of xyz() must be added into the following types")) {
                    qa = new QualityAttribute();
                    qa.setCurrentValue(new Double(1.5));
                    r.setResolutionQuality(qa);
                } else {
                    qa = new QualityAttribute();
                    qa.setCurrentValue(new Double(2.0));
                    r.setResolutionQuality(qa);
                }
                pullUpMethods.revertMoDifications();
            }
        }
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
//        new Thread(agenteTrabalhador).start();
//        while(agenteTrabalhador.getOutput().getParent().isVisible()){
//            Thread.sleep(1000);
//        }
    }

    @Test
    public void testPullUpFields() throws Throwable {
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
//        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "PullUpFields");

        PullUpFields pullUpFields = FactoryRefactoringTool.getRefactoringTool(PullUpFields.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = pullUpFields.findAllSymptoms();
        
        Map<String, List<Symptom>> mapa = new TreeMap<String, List<Symptom>>();
        for (Symptom symptom : symptoms) {
            PullPushSymptom pps = (PullPushSymptom) symptom; //Apenas pelo memberName não é um bom criterio de selecao
            try {                                            //Estou usando aqui, pois o projeto de Test é pequeno
                if (mapa.get(pps.getMemberName()) == null) 
                    throw new Exception();
            } catch (Exception e) {
                mapa.put(pps.getMemberName(), new ArrayList<Symptom>());
            }
            mapa.get(pps.getMemberName()).add(symptom);
        }
        
        assertTrue(mapa.containsKey("field"));
        Symptom symptom = mapa.get("field").get(0);
        List<PullPushResolution> resolutions = ((PullPushSymptom) symptom).generateResolutions(agenteTrabalhador, false);
        assertEquals(resolutions.size(), 1);
        PullPushResolution resolution = resolutions.get(0);
        StringBuilder sb = new StringBuilder();
        assertTrue(resolution.applyCalculateQA(agenteTrabalhador, sb));
        QualityAttribute qa = new QualityAttribute();
        qa.setCurrentValue(new Double(1.0));
        resolution.setResolutionQuality(qa);
        pullUpFields.revertMoDifications();
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
    }

    @Test
    public void testPushDownMethods() throws Throwable {
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
//        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "PushDownMethods");

        PushDownMethods pushDownMethods = FactoryRefactoringTool.getRefactoringTool(PushDownMethods.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = pushDownMethods.findAllSymptoms();
        
        Symptom symptom = (Symptom) assertStringInObjectList("desce3 to MidClass", symptoms);
        List<PullPushResolution> resolutions = ((PullPushSymptom) symptom).generateResolutions(agenteTrabalhador, false);
        assertEquals(resolutions.size(), 1);
        PullPushResolution resolution = resolutions.get(0);
        StringBuilder sb = new StringBuilder();
        assertEquals(resolution.toString(), "desce3 to MidClass");
        assertTrue(resolution.applyCalculateQA(agenteTrabalhador, sb));
        QualityAttribute qa = new QualityAttribute();
        qa.setCurrentValue(new Double(1.0));
        resolution.setResolutionQuality(qa);
        pushDownMethods.revertMoDifications();
        
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
    }
    
    @Test
    public void testPushDownFields() throws Throwable {
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
//        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "PushDownFields");

        PushDownFields pushDownFields = FactoryRefactoringTool.getRefactoringTool(PushDownFields.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = pushDownFields.findAllSymptoms();
        
        Symptom symptom = (Symptom) assertStringInObjectList("field3 to DownClass", symptoms);
        List<PullPushResolution> resolutions = ((PullPushSymptom) symptom).generateResolutions(agenteTrabalhador, true);
        assertEquals(resolutions.size(), 1);
        pushDownFields.revertMoDifications();
        
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
        
    }
    
    @Test
    public void testEncapsulateFields() throws Throwable {
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
//        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "EncapsulateFields");
        EncapsulateFields encapsuleFields = FactoryRefactoringTool.getRefactoringTool(EncapsulateFields.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = encapsuleFields.findAllSymptoms();
        
        Symptom symptom = (Symptom) assertStringInObjectList("Encapsulate field2", symptoms);
        List<? extends Resolution> resolutions = symptom.generateResolutions(agenteTrabalhador, true);
        assertEquals(resolutions.size(), 1);
        encapsuleFields.revertMoDifications();
        
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
           
    }

    @Test
    public void testCleanImports() throws Throwable {
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
        //        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "CleanImports");

        CleanImports cleanImports = FactoryRefactoringTool.getRefactoringTool(CleanImports.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = cleanImports.findAllSymptoms();
        
        Symptom symptom = (Symptom) assertStringInObjectList("CleanImport import java.lang.Boolean", symptoms);
        List<? extends Resolution> resolutions = symptom.generateResolutions(agenteTrabalhador, true);
        assertEquals(resolutions.size(), 1);
        cleanImports.revertMoDifications();
        
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
           
    }
    
    @Test
    public void testAddDelegateMethods() throws Throwable {
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
        //        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "AddDelegateMethods");
        AddDelegateMethods addDelegates = FactoryRefactoringTool.getRefactoringTool(AddDelegateMethods.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = addDelegates.findAllSymptoms();
        
        Symptom symptom = (Symptom) assertStringInObjectList("Delegate Method: com.testproject.testclasses.DownClass.fieldX.toLowerCase", symptoms);
        List<? extends Resolution> resolutions = symptom.generateResolutions(agenteTrabalhador, true);
        assertEquals(resolutions.size(), 1);
        addDelegates.revertMoDifications();
//        
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
           
    }
    
    @Test
    public void testExtractInterfaces() throws Throwable {
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
        //        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "ExtractInterfaces");
        ExtractInterfaces extractInterface = FactoryRefactoringTool.getRefactoringTool(ExtractInterfaces.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = extractInterface.findAllSymptoms();
        
        Symptom symptom = (Symptom) assertStringInObjectList("Extract TextInterface from com.testproject.testclasses.Text", symptoms);
        List<? extends Resolution> resolutions = symptom.generateResolutions(agenteTrabalhador, true);
        assertEquals(resolutions.size(), 1);
        extractInterface.revertMoDifications();
        
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
           
    }
    
    @Test
    public void testUseSuperTypes() throws Throwable {
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
        //        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "UseSuperTypes");
        UseSuperTypes useSuperTypes = FactoryRefactoringTool.getRefactoringTool(UseSuperTypes.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = useSuperTypes.findAllSymptoms();
        
        Symptom symptom = (Symptom) assertStringInObjectList("Use supertype com.testproject.testclasses.Interface where is possible", symptoms);
        List<? extends Resolution> resolutions = symptom.generateResolutions(agenteTrabalhador, true);
        assertEquals(resolutions.size(), 1);
        useSuperTypes.revertMoDifications();
//        
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
           
    }

    @Test
    public void testCreateFactoryMethods() throws Throwable {
        ProjectVCS vcs = this.getTestProject();
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(vcs);
        //        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "CreateFactoryMethods");

        CreateFactoryMethods createFactoryMethods = FactoryRefactoringTool.getRefactoringTool(CreateFactoryMethods.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = createFactoryMethods.findAllSymptoms();
        Symptom symptom = (Symptom) assertStringInObjectList("Create Factory Method for com.testproject.testclasses.Text(String string)", symptoms);
        List<? extends Resolution> resolutions = symptom.generateResolutions(agenteTrabalhador, true);
        assertEquals(resolutions.size(), 1);
        createFactoryMethods.revertMoDifications();
//        
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));

    }
    
    @Test
    public void testInlineMethods() throws Throwable {
        // Está falhando quando aplicado depois de CreateFactoryMethods por não conseguir encontrar todas as utilizações. Alguma variavel deve ser resetada no loadEnvironment
        LocalManagerAgent agenteTrabalhador = LocalManagerAgent.createToTests(this.getTestProject());
        //        agenteTrabalhador.planTesting();
        agenteTrabalhador.initializeTests();
        agenteTrabalhador.setTestLevel(999);
        agenteTrabalhador.testMessage(0, "InlineMethods");
        InlineMethods inlineMethods = FactoryRefactoringTool.getRefactoringTool(InlineMethods.class, agenteTrabalhador.getProjectVCS());
        List<Symptom> symptoms = inlineMethods.findAllSymptoms();
        Symptom symptom = (Symptom) assertStringInObjectList("Inline Method: com.testproject.testclasses.Inline.inlineMethod", symptoms);
        List<? extends Resolution> resolutions = symptom.generateResolutions(agenteTrabalhador, true);
        assertEquals(resolutions.size(), 1);
        inlineMethods.revertMoDifications();
//        
        assertTrue(RefactoringApplier.applyBestCalculatedResolution(agenteTrabalhador, resolutions));
    }

    
    
}
