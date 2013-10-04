package br.uff.ic.oceano.core.tools.metrics.util;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.compiler.CompilerService;
import br.uff.ic.oceano.core.tools.revision.JavaRevisionTool;
import br.uff.ic.oceano.core.tools.maven.MavenUtil;
import br.uff.ic.oceano.core.tools.revision.RevisionUtil;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.aspectj.apache.bcel.classfile.ClassParser;

/**
 *
 * @author Daniel
 */
public class ClassLoaderUtil {

    public static List<String> loadClassNames(Revision revision) throws Exception, NoClassDefFoundError {
        if(!revision.getProject().isMavenProject()){
            throw new Exception("Build tool not supported.");
        }

        JavaRevisionTool jvTool = new JavaRevisionTool();
        List<String> classNames = jvTool.getCompiledClassNames(revision);
        //Is compiled
        if (classNames.isEmpty()) {
            //compile
            CompilerService.compile(revision);
            classNames = jvTool.getCompiledClassNames(revision);
        }

        return classNames;
    }

    public static List<Class> loadClasses(Revision revision) throws Exception, NoClassDefFoundError {
        if(!revision.getProject().isMavenProject()){
            throw new Exception("Build tool not supported.");
        }

        List<String> classNames = loadClassNames(revision);
        if (classNames.isEmpty()) {
            throw new Exception("No compiled classes found");
        }

        ClassLoader customLoader = MavenUtil.getProjectClassLoaderByCommandLine(revision);

        List<Class> classes = new LinkedList<Class>();
        for (String classname : classNames) {
            Class oneClass = customLoader.loadClass(classname);
            classes.add(oneClass);
        }
        return classes;
    }

    public static Class loadClass(Revision revision, String path) throws Exception, NoClassDefFoundError {
        if(!revision.getProject().isMavenProject()){
            throw new Exception("Build tool not supported.");
        }

        ClassParser cp = new ClassParser(path);
        org.aspectj.apache.bcel.classfile.JavaClass jc = cp.parse();

        ClassLoader customLoader = MavenUtil.getProjectClassLoaderByCommandLine(revision);
        return customLoader.loadClass(jc.getClassName());
    }
}
