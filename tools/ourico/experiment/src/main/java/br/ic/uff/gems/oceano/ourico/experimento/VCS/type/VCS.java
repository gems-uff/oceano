package br.ic.uff.gems.oceano.ourico.experimento.VCS.type;

import java.io.File;

public interface VCS {

    public void create(String repository) throws Exception;

    public void insert(String workspace, String repository) throws Exception;

    public void list(String repository) throws Exception;

    public void merge(File workspace, File studyWorkspace);

    public void setConfigureMainCommands(String paths);

    public File createWorkSpace(String workSpaceStudy);

    public void plan(String studyRepositoryURL, File workspace, int countConfiguration, String workspaceStudy) throws Exception;
}
