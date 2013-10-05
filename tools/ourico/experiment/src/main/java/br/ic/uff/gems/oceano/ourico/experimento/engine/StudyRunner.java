package br.ic.uff.gems.oceano.ourico.experimento.engine;

import br.ic.uff.gems.oceano.ourico.experimento.VCS.Subversion;
import br.ic.uff.gems.oceano.ourico.experimento.VCS.type.VCS;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.service.CheckOutService;
import java.io.File;

import java.io.IOException;
import sandbox.experimento.utils.FileUtils;

/**
 * Executes the study
 * 
 * @author murta, heliomar
 */
public class StudyRunner {

    CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);
    private String subversionRepository;
    private int initialConfiguration;
    private int finalConfiguration;

    public StudyRunner(String subversionRepository, int initialConfiguration, int finalConfiguration) {
        this.subversionRepository = subversionRepository;
        this.initialConfiguration = initialConfiguration;
        this.finalConfiguration = finalConfiguration;
    }

    public void run(VCS vcs, String studyRepository, String studyRepositoryURL, String studyWorkSpace, String exportedWorkSpacePath, String fontPath) {
        try {
//            FileUtils.recursiveDelete(new File(studyRepository));

            // Create the study repository
//            vcs.create(studyRepository);

            // Work with the remaining configurations
            File exportedWorkspace = null;

            // Prepare the study to initial confituratin
            exportedWorkspace = prepareWorkSpaceTempMensuringTime(initialConfiguration, exportedWorkSpacePath, fontPath);
            vcs.insert(exportedWorkspace.getPath(), studyRepositoryURL);



            for (int i = initialConfiguration + 1; i <= finalConfiguration; i++) {

               
                // Export configuration from subversion
                exportedWorkspace = prepareWorkSpaceTempMensuringTime(i, exportedWorkSpacePath, fontPath);

                // execute the plan
                vcs.plan(studyRepositoryURL, exportedWorkspace, finalConfiguration - i, studyWorkSpace);

                System.out.println("Versão "+i+" sucesso.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Free memory for the next iteration.
     * While GC can free memory, keep freeing it.
     */
    private void freeMemory() {
        long memoryBeforeGC;
        long memoryAfterGC = Runtime.getRuntime().totalMemory();
        do {
            memoryBeforeGC = memoryAfterGC;
            Runtime.getRuntime().gc();
            memoryAfterGC = Runtime.getRuntime().totalMemory();
        } while (memoryBeforeGC > memoryAfterGC);
    }

    private File prepareWorkSpaceTempMensuringTime(int i, String workspaceDir, String fontPath) throws IOException {
        freeMemory();
        // Create workspace
        File workspace = new File(workspaceDir);
        FileUtils.recursiveDelete(workspace);
        // Export configuration from subversion
        Subversion.getInstanceToFont(fontPath).export(String.valueOf(i), subversionRepository, workspace.getPath());
        return workspace;
    }
//    private File prepareWorkSpaceTempMensuringTime(int i, String workspaceDir, String fontPath) throws IOException {
//        freeMemory();
//        // Create workspace
//
//        if(!workspaceDir.endsWith("/"))
//            workspaceDir += "/";
//
//        String workspaceDirCO = workspaceDir + "co";
//        String workspaceDirEX = workspaceDir + "ex";
//
//        File workspaceCO = new File(workspaceDirCO);
//        FileUtils.recursiveDelete(workspaceCO);
//        File workspaceEX = new File(workspaceDirEX);
//        FileUtils.recursiveDelete(workspaceEX);
//        // Export configuration from subversion
////        Subversion.getInstanceToFont(fontPath).export(String.valueOf(i), subversionRepository, workspace.getPath());
//        Subversion.getInstanceToFont(fontPath).export(String.valueOf(i), subversionRepository, "trunk", workspaceDir+"co", workspaceDir+"ex");
//        return workspaceEX;
//    }

    private void savingStatistics(String studyRepositoryDir) throws IOException {
    }
}
