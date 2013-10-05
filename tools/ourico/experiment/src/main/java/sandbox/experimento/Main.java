/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sandbox.experimento;

import br.ic.uff.gems.oceano.ourico.experimento.VCS.Subversion;
import br.ic.uff.gems.oceano.ourico.experimento.VCS.type.VCS;
import br.ic.uff.gems.oceano.ourico.experimento.engine.StudyRunner;

/**
 *
 * @author marapao
 */
public class Main {

    public static void main(String[] args) {

//        String subversionRepository = "file:///home/marapao/experiementos/oceano/trunk";
//        String subversionRepository = "file:///home/marapao/copia/repositorios/findbugs-maven-plugin";
//        String subversionRepository = "file:///home/marapao/copia/repositorios/maven2";
//        String subversionRepository = "file:///home/marapao/copia/repositorios/mavenNative";
        String subversionRepository = "file:///home/marapao/copia/repositorios/sqlPlugin";
        int initialConfiguration = 0;
        int finalConfiguration = 136;

        //394 - começa
        //529
        VCS vcs = Subversion.getInstance();
//        String studyRepository = "/home/marapao/repositorio/experimento/mavenNative";
        String studyRepository = "/home/marapao/repositorio/experimento/sql1";
//        String studyRepositoryURL = "file:///home/marapao/repositorio/experimento/mavenNative/trunk";
        String studyRepositoryURL = "file:///home/marapao/repositorio/experimento/sql1/trunk";
//        String studyRepository = "/home/marapao/repositorio/experimento/oceano";
//        String studyRepositoryURL = "file:///home/marapao/repositorio/experimento/oceano/trunk";
        String studyWorkSpace = "/home/marapao/repositorio/experimento/workspaceSQL1";
        String exportedWorkSpacePath = "/home/marapao/repositorio/experimento/exportedWSQL1";
//        String exportedWorkSpacePath = "/home/marapao/repositorio/experimento/exportedWMaven";
        //path to subversion
        String fontPath = "/usr/bin/";



        StudyRunner studyRunner = new StudyRunner(subversionRepository, initialConfiguration, finalConfiguration);
        

        studyRunner.run(vcs, studyRepository, studyRepositoryURL, studyWorkSpace, exportedWorkSpacePath, fontPath);


    }
}
