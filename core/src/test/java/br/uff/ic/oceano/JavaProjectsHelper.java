/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.compiler.CompilerService;
import br.uff.ic.oceano.ostra.exception.CompilerException;
import br.uff.ic.oceano.util.SystemUtil;
import static org.testng.Assert.*;

/**
 *
 * @author DanCastellani
 */
public class JavaProjectsHelper {

    public static final String NEWPACKAGE1 = "/src/main/java/br/uff/ic/oceano/test/testmavenproject/newpackage1";
    public static final String NEWPACKAGE2_CLASS = "/target/classes/br/uff/ic/oceano/test/testmavenproject/newpackage2";
    public static final String CARRO_SOURCE = "/src/main/java/br/uff/ic/oceano/test/testmavenproject/Carro.java";
    public static final String MOTO_CLASS = "/target/classes/br/uff/ic/oceano/test/testmavenproject/Moto.class";
    public static final String CARRO_CLASS = "/target/classes/br/uff/ic/oceano/test/testmavenproject/Carro.class";
    public static final String PACKAGE_TESTMAVENPROJECT = "/target/classes/br/uff/ic/oceano/test/testmavenproject";
    private Revision revisionTestMavenProject;
    private Revision revisionMavenprojectMFA;
    private Revision revisionAnimalSniffer;
    private Revision revisionMaven3;

    public JavaProjectsHelper() {
//        try {
            this.revisionTestMavenProject = createRevision("TestMavenProject");
            //CompilerService.compile(this.revisionTestMavenProject);

            this.revisionAnimalSniffer = createRevision("animal-sniffer");
            //CompilerService.compile(this.revisionAnimalSniffer);

            this.revisionMavenprojectMFA = createRevision("mavenprojectMFA");
            //CompilerService.compile(this.revisionMavenprojectMFA);

            this.revisionMaven3 = createRevision("maven-3-trunk");
            //CompilerService.compile(this.revisionMaven3);
//        } catch (CompilerException ex) {
//            fail("Compilation error", ex);
//        }
    }

    private Revision createRevision(String path) {
        final String fs = SystemUtil.FILESEPARATOR;
        //Where tests are running
        final String basePath = "." + fs + "target" + fs + "test-classes" + fs;

        SoftwareProject softwareProject = new SoftwareProject();
        softwareProject.setMavenProject(true);

        Revision revision = new Revision();
        revision.setLocalPath(basePath + fs + path);
        revision.setProject(softwareProject);
        revision.setNumber(0L);
        return revision;
    }

    /**
     * @return the revision
     */
    public Revision getRevisionTestMavenProject() {
        return revisionTestMavenProject;
    }

    /**
     * @return the revisionAnimalSniffer
     */
    public Revision getRevisionAnimalSniffer() {
        return revisionAnimalSniffer;
    }

    /**
     * @return the revisionMavenprojectMFA
     */
    public Revision getRevisionMavenprojectMFA() {
        return revisionMavenprojectMFA;
    }

    /**
     *
     * @return the revisionMaven3
     */
    public Revision getRevisionMaven3() {
        return revisionMaven3;
    }
}
