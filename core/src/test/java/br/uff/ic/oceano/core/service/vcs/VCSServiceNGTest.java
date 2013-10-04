/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service.vcs;

import br.uff.ic.oceano.CppProjectsHelper;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import br.uff.ic.oceano.core.dao.OceanoUserDao;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.dao.impl.OceanoUserDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.*;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.tools.vcs.VCSUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.HashUtil;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.util.SystemUtil;
import br.uff.ic.oceano.util.test.DiffUtil;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author dheraclio
 */
public class VCSServiceNGTest extends AbstractNGTest {

    private VCSService vcsService;
    private SoftwareProject project;
    private ProjectUser projectUser;
    private String tempPath;
    protected static final CppProjectsHelper testConstantsCpp = new CppProjectsHelper();

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        try {
            JPAUtil.startUp();

            project = testConstantsCpp.getDBNeoPZProject();
            assertNotNull(project);

            OceanoUserDao oceanoUserDao = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserDaoImpl.class);
            assertNotNull(oceanoUserDao);

            OceanoUser oceanoUser;
            oceanoUser = oceanoUserDao.getByLogin("dheraclio");
            assertNotNull(oceanoUser);

            ProjectUserService projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
            assertNotNull(projectUserService);

            projectUser = projectUserService.getByProjectAndOceanoUser(project, oceanoUser);
            assertNotNull(projectUser);

            vcsService = ObjectFactory.getObjectWithDataBaseDependencies(VCSService.class);
            assertNotNull(vcsService);

            tempPath = SystemUtil.getTempDirectory();
            assertNotNull(tempPath);
        } catch (ServiceException ex) {
            fail(ex.getMessage());
        } catch (ObjetoNaoEncontradoException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testCheckOut_FilesDiff() {
        try {
            println("testCheckOut_FilesDiff");
            Calendar date = Calendar.getInstance();

            Set<String> leftFiles = getCheckoutFiles(date, "checkoutTest", "checkout1", false);
            Set<String> rightFiles = getCheckoutFiles(date, "checkoutTest", "checkout2", false);

            assertTrue(leftFiles.size() == rightFiles.size(), "Different sizes");
            assertFalse(DiffUtil.hasDiff(leftFiles, rightFiles));

            deleteCheckoutDirectory("checkoutTest", "checkout1");
            deleteCheckoutDirectory("checkoutTest", "checkout2");

        } catch (VCSException ex) {
            fail(ex.getMessage());
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testCheckOut_Files() {
        println("testCheckOut_Files");
        try {
            Calendar date = Calendar.getInstance();
            Set<String> leftFiles = getCheckoutFiles(date, "checkoutTest", "checkout1", true);
            Set<String> rightFiles = getCheckoutFiles(date, "checkoutTest", "checkout2", true);

            assertTrue(leftFiles.size() == rightFiles.size(), "Different sizes");
            assertEqualsNoOrder(leftFiles.toArray(), rightFiles.toArray(), "Not equal ignoring order");
            assertTrue(leftFiles.containsAll(rightFiles), "Left doesn´t contains Right");
            assertTrue(rightFiles.containsAll(leftFiles), "Right doesn´t contains Left");
        } catch (VCSException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testDirectMD5_SameDirectory() {
        println("testDirectMD5_SameDirectory");
        try {
            Calendar date = Calendar.getInstance();

            deleteCheckoutDirectory("checkoutTest", "checkout1");
            Collection files = getCheckoutFiles(date, "checkoutTest", "checkout1", false);
            String expected = HashUtil.getMD5(files);
            deleteCheckoutDirectory("checkoutTest", "checkout1");

            deleteCheckoutDirectory("checkoutTest", "checkout1");
            files = getCheckoutFiles(date, "checkoutTest", "checkout1", false);
            String actual = HashUtil.getMD5(files);
            deleteCheckoutDirectory("checkoutTest", "checkout1");

            assertEquals(actual, expected);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testDirectMD5_DifferentDirectory() {
        println("testDirectMD5_DifferentDirectory");
        try {
            Calendar date = Calendar.getInstance();

            deleteCheckoutDirectory("checkoutTest", "checkout1");
            Set files1 = getCheckoutFiles(date, "checkoutTest", "checkout1", false);
            List sorted1 = new LinkedList(files1);
            Collections.sort(sorted1);
            String expected = HashUtil.getMD5(sorted1);
            deleteCheckoutDirectory("checkoutTest", "checkout1");

            deleteCheckoutDirectory("checkoutTest", "checkout2");
            Set files2 = getCheckoutFiles(date, "checkoutTest", "checkout2", false);
            List sorted2 = new LinkedList(files2);
            Collections.sort(sorted2);
            String actual = HashUtil.getMD5(sorted2);
            deleteCheckoutDirectory("checkoutTest", "checkout2");

            assertEquals(actual, expected);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test clean checkout.
     */
    @Test
    public void testCheckOutMD5_SameDirectory() {
        println("testCheckOutMD5_SameDirectory");
        try {
            Calendar date = Calendar.getInstance();

            String firstMD5 = getCheckoutMD5(date, "checkoutTest", "checkout1");
            String secondMD5 = getCheckoutMD5(date, "checkoutTest", "checkout1");

            assertEquals(firstMD5, secondMD5);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testCheckOutMD5_DifferentDirectory() {
        println("testCheckOutMD5_DifferentDirectory");
        try {
            Calendar date = Calendar.getInstance();

            String firstMD5 = getCheckoutMD5(date, "checkoutTest", "checkout1");
            String secondMD5 = getCheckoutMD5(date, "checkoutTest", "checkout2");

            assertEquals(firstMD5, secondMD5);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testCheckoutMD5vsDirectMD5() {
        println("testCheckoutMD5vsDirectMD5");
        try {
            Calendar date = Calendar.getInstance();

            Collection files = getCheckoutFiles(date, "checkoutTest", "checkout1", false);
            List sorted = new LinkedList(files);
            Collections.sort(sorted);
            String expected = HashUtil.getMD5(sorted);
            deleteCheckoutDirectory("checkoutTest", "checkout1");

            String actual = getCheckoutMD5(date, "checkoutTest", "checkout1");
            deleteCheckoutDirectory("checkoutTest", "checkout1");

            assertEquals(actual, expected);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test checkout then update.
     */
    @Test
    public void testUpdate() {
        println("testUpdate");
        try {
            String expected = getCheckoutMD5(Calendar.getInstance(), "updateTest", "checkout");

            //month back from today
            Calendar updateFromDate = Calendar.getInstance();
            updateFromDate.roll(Calendar.MONTH, -1);

            String actual = getHeadUpdateMD5(updateFromDate, "updateTest", "update");

            assertEquals(actual, expected);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test checkout head revision and compare to checkout revision 0 then
     * update to head revision.
     */
    @Test
    public void testUpdateFirstToHead() {
        println("testUpdateFirstToHead");
        try {
            String actual = getFirstRevisionUpdatedToHeadMD5("updateTest", "update");

            String expected = getCheckoutMD5(Calendar.getInstance(), "updateTest", "checkout");

            assertEquals(actual, expected);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     *
     */
    @Test
    public void testSequencialUpdatesAgainstCheckout() {
        println("testSequencialUpdatesAgainstCheckout");
        try {
            long numHeadRevision = getNumberOfHeadRevision();
            long firstRevision = numHeadRevision - 10L;
            if (firstRevision < 1L) {
                firstRevision = Math.abs(firstRevision) + 1L;
            }
            checkoutRevision(firstRevision, "updateTest", "update", false);
            firstRevision++;
            for (long revNumber = firstRevision; revNumber <= numHeadRevision; revNumber++) {
                String checkoutHash = checkoutRevision(revNumber, "updateTest", "checkout", true);
                String updateHash = updateToRevision(revNumber, "updateTest", "update", true);
                assertEquals(checkoutHash, updateHash, "Not same hash code");
            }
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testSequencialUpdatesUntilHead() {
        println("testSequencialUpdatesUntilHead");
        
        try {
            final String checkout = "checkout";
            final String update = "update";
            final String updateTest = "updateTest";
            long numHeadRevision = getNumberOfHeadRevision();
            long checkpoint = numHeadRevision / 100; //
            long revNumber = checkpoint * 80; //test last 20% revisions
            checkoutRevision(revNumber, updateTest, update, false);
            for (revNumber += 1L; revNumber < numHeadRevision; revNumber++) {                
                if(!checkRevisionAvailable(revNumber)){
                    //there is a bug on recovering revision from svn
                    //remove when bug is fixed
                    continue; 
                }
                if (revNumber % checkpoint == 0) {
                    //check every checkpoint
                    String updateHash = updateToRevision(revNumber, updateTest, update, true);
                    String checkoutHash = checkoutRevision(revNumber, updateTest, checkout, true);
                    assertEquals(checkoutHash, updateHash, "Not same hash code");
                } else {
                    updateToRevision(revNumber, updateTest, update, false);
                }
            }
            String updateHash = updateToRevision(numHeadRevision, updateTest, update, true);
            String checkoutHash = checkoutRevision(numHeadRevision, updateTest, "checkout", true);
            assertEquals(checkoutHash, updateHash, "Not same hash code");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Bug found in svnkit revision recovery. Several revisions are ignored.
     * No reason found. Try to implement method on SVN_by_CommandLine may solve it.
     * 
     * @throws VCSException
     */
    @Test
    public void testRevisionsAvailable() throws VCSException {
        long numHeadRevision = getNumberOfHeadRevision();
        long notFoundRevisions = 0L;
        for (long revNumber = 1L; revNumber <= numHeadRevision; revNumber++) {            
            if(checkRevisionAvailable(revNumber)){
                println("Not found revision " + revNumber);
                notFoundRevisions++;
            }
        }
        println("Missed " + notFoundRevisions + " revisions");
    }

    private boolean checkRevisionAvailable(long revNumber) throws VCSException {
        return vcsService.getRevision(project, projectUser, revNumber) != null;
    }
    
    private long getNumberOfHeadRevision() throws VCSException {
        return vcsService.getNumberOfHEADRevision(projectUser);
    }

    private String checkoutRevision(long revNumber, String mainDir, String subDir, boolean calchash) throws VCSException {
        String fixedDir = PathUtil.getWellFormedPath(tempPath, mainDir, subDir);
        FileUtils.deleteDirectory(new File(fixedDir));

        Revision revision = vcsService.getRevision(project, projectUser, revNumber);
        assertNotNull(revision);

        revision.setLocalPath(fixedDir);
        vcsService.doCheckout(revision, projectUser, false);

        if (calchash) {
            return vcsService.getMD5(revision);
        } else {
            return null;
        }

    }

    private String updateToRevision(long revNumber, String mainDir, String subDir, boolean calchash) throws VCSException {
        String fixedDir = PathUtil.getWellFormedPath(tempPath, mainDir, subDir);

        Revision revision = vcsService.getRevision(project, projectUser, revNumber);
        assertNotNull(revision, "Revision " + revNumber + " not found");

        revision.setLocalPath(fixedDir);
        vcsService.doUpdate(revision, projectUser, false);

        if (calchash) {
            return vcsService.getMD5(revision);
        } else {
            return null;
        }
    }

    /**
     * Returns MD5 of resulting project from checkout of last revision from a
     * deserid date.
     *
     * @param date
     * @param mainPath
     * @param subdir
     * @return
     * @throws VCSException
     */
    private String getCheckoutMD5(Calendar date, String mainPath, String subdir) throws VCSException {
        String checkoutPath = PathUtil.getWellFormedPath(tempPath, mainPath, subdir);
        FileUtils.deleteDirectory(new File(checkoutPath));

        Revision revision = vcsService.getRevision(project, projectUser, date);
        assertNotNull(revision);

        revision.setLocalPath(checkoutPath);

        vcsService.doCheckout(revision, projectUser, false);

        String md5 = vcsService.getMD5(revision);

        FileUtils.deleteDirectory(new File(checkoutPath));

        return md5;
    }

    /**
     * Returns MD5 resulting project update to head on checkout of last revision
     * from a desired date.
     *
     * @param checkoutFromDate
     * @param mainPath
     * @param subdir
     * @return
     * @throws VCSException
     */
    private String getHeadUpdateMD5(Calendar checkoutFromDate, String mainPath, String subdir) throws VCSException {
        String fixedPath = PathUtil.getWellFormedPath(tempPath, mainPath, subdir);
        FileUtils.deleteDirectory(new File(fixedPath));

        Revision revision = vcsService.getRevision(project, projectUser, checkoutFromDate);
        assertNotNull(revision);

        revision.setLocalPath(fixedPath);
        vcsService.doCheckout(revision, projectUser, false);

        Long number = vcsService.getNumberOfHEADRevision(projectUser);
        revision.setNumber(number);
        vcsService.doUpdate(revision, projectUser, false);

        String oldMD5 = vcsService.getMD5(revision);
        FileUtils.deleteDirectory(new File(fixedPath));

        return oldMD5;
    }

    /**
     * Returns MD5 resulting project of update to head a checkout of the first
     * revision.
     *
     * @param mainPath
     * @param subdir
     * @return MD5
     * @throws VCSException
     */
    private String getFirstRevisionUpdatedToHeadMD5(String mainPath, String subdir) throws VCSException {
        String updatedPath = PathUtil.getWellFormedPath(tempPath, mainPath, subdir);
        FileUtils.deleteDirectory(new File(updatedPath));

        Revision revision = vcsService.getRevision(project, projectUser, 1L);
        assertNotNull(revision);

        revision.setLocalPath(updatedPath);
        vcsService.doCheckout(revision, projectUser, false);

        Long number = vcsService.getNumberOfHEADRevision(projectUser);
        revision.setNumber(number);
        vcsService.doUpdate(revision, projectUser, false);

        String oldMD5 = vcsService.getMD5(revision);
        FileUtils.deleteDirectory(new File(updatedPath));

        return oldMD5;
    }

    

    /**
     * Does not delete files from checkout.
     *
     * @param date
     * @param mainPath
     * @param subdir
     * @param relative
     * @param ignoreVCSFiles
     * @return
     * @throws VCSException
     */
    private Set<String> getCheckoutFiles(Calendar date, String mainPath, String subdir, boolean relative) throws VCSException {
        String updatedPath = PathUtil.getWellFormedPath(tempPath, mainPath, subdir);
        File updateDirectory = new File(updatedPath);
        FileUtils.deleteDirectory(updateDirectory);

        Revision revision = vcsService.getRevision(project, projectUser, date);
        assertNotNull(revision);

        revision.setLocalPath(updatedPath);

        vcsService.doCheckout(revision, projectUser, false);

        Set<String> files = FileUtils.getAllFilesInFolderAndSubFolders(updateDirectory);
        //FileUtils.deleteDirectory(updateDirectory);

        Set<String> relativeFile = new HashSet<String>(files.size());
        for (String string : files) {
            if (VCSUtil.isVCSpath(string)) {
                continue;
            }
            if (relative) {
                relativeFile.add(string.substring(updatedPath.length()));
            } else {
                relativeFile.add(string);
            }
        }
        return relativeFile;
    }

    private void deleteCheckoutDirectory(String mainPath, String subdir) {
        String updatedPath = PathUtil.getWellFormedPath(tempPath, mainPath, subdir);
        File updateDirectory = new File(updatedPath);
        FileUtils.deleteDirectory(updateDirectory);
    }
}
