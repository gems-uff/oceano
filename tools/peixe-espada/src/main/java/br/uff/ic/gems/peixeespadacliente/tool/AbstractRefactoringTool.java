package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.utils.ProjectUtils;
import br.uff.ic.gems.peixeespadacliente.utils.SetupUtils;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.util.MavenUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinClass;
import net.sf.refactorit.classmodel.BinConstructor;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinParameter;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.refactorings.Refactoring;
import net.sf.refactorit.refactorings.RefactoringStatus;

/**
 *
 * @author Heliomar, João Felipe
 */
public abstract class AbstractRefactoringTool implements RefactoringTool {

    public final static boolean CLEAN = true;
    protected Refactoring refactoring;
    protected ProjectVCS projectVCS;
    protected Project project;

    public AbstractRefactoringTool(ProjectVCS projectVCS) {
        this.projectVCS = projectVCS;
    }

    public Project getProject() {
        return project;
    }

    protected RefactoringStatus checkPreconditions() {
        return refactoring.checkPreconditions();
    }

    protected RefactoringStatus checkUserInput() {
        return refactoring.checkUserInput();
    }

    protected boolean loadEnvironment() {
        try {
            SetupUtils.restoreRefactoritStaticFields(false);
            project = ProjectUtils.getProjectRefactoring(projectVCS);
            project.getProjectLoader().build();
//            getProject().getProjectLoader().projectCleanup();
        } catch (Exception ex) {
            Logger.getLogger(PullUpMethods.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        SetupUtils.setup(getProject());
        return true;
    }

    protected boolean classShouldBeVerified(BinCITypeRef classe) {
        String absolutPath = classe.getPackage().getBaseDir().getFileOrNull().getAbsolutePath();
        // Considerando apenas /src/main/java
        return (absolutPath.contains("\\src\\main\\java") || absolutPath.contains("/src/main/java"));
    }

    public static boolean isWorkByRefactorIt(Project project) {
        try {
            Project tempProject = new Project(project.getName(), project.getPaths());
            SetupUtils.setup(tempProject);
            tempProject.getProjectLoader().build();
            SetupUtils.setup(project);
            return !tempProject.getProjectLoader().getErrorCollector().hasCriticalUserErrors();
        } catch (Exception ex) {
        }
        SetupUtils.setup(project);
        return false;
    }

    @Override
    public boolean isWorking() throws RefactoringException {
        try {
            List<Throwable> lista;
            if (CLEAN) {
                lista = MavenUtil.execute(projectVCS.getLocalPath().toString(), Arrays.asList("-Dmaven.test.skip=true", "clean", "compile"), null);
            } else {
                lista = MavenUtil.execute(projectVCS.getLocalPath().toString(), Arrays.asList("-Dmaven.test.skip=true", "compile"), null);
            }

            if (!(lista == null || lista.isEmpty())) {
                lista.get(0).printStackTrace();
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new RefactoringException(e);
        }
    }

    @Override
    public void revertMoDifications() throws RefactoringException {
        try {
            projectVCS.doReset();
        } catch (VCSException ex) {
            throw new RefactoringException(ex);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    protected BinField getFieldManually(String memberName, BinCIType onwer) {
        BinField[] bm = onwer.getDeclaredFields();
        for (BinField binField : bm) {
            if (memberName.equals(binField.getName())) {
                return binField;
            }
        }
        return null;
    }

    protected BinMethod getMethodManually(String memberName, BinCIType owner, BinParameter[] parameters) {
        BinMethod[] bm = owner.getDeclaredMethods();
        for (BinMethod binMethod : bm) {
            if (memberName.equals(binMethod.getName())) {
                BinParameter[] currentParameters = ((BinMethod) binMethod).getParameters();
                if (parameters.length == currentParameters.length) {
                    for (int i = 0; i < currentParameters.length; i++) {
                        if (!parameters[i].getMemberType().equals(currentParameters[i].getMemberType())) {
                            break;
                        }
                    }
                    return binMethod;
                }
            }
        }
        return null;
    }

    protected BinConstructor getConstructorManually(BinCIType owner, BinParameter[] parameters) {
        BinClass cls = (BinClass) owner;
        BinConstructor[] constructors = cls.getConstructors();
        for (BinConstructor constructor : constructors) {
            BinParameter[] currentParameters = constructor.getParameters();
            if (parameters.length == currentParameters.length) {
                for (int i = 0; i < currentParameters.length; i++) {
                    if (!parameters[i].getMemberType().equals(currentParameters[i].getMemberType())) {
                        break;
                    }
                }
                return constructor;
            }
        }
        return null;
    }
}
