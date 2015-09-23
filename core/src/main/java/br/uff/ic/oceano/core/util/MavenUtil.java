package br.uff.ic.oceano.core.util;

import br.uff.ic.oceano.util.CommandLineIinterfaceUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.maven.MavenException;
import br.uff.ic.oceano.core.tools.revision.JavaRevisionTool;
import static br.uff.ic.oceano.ostra.controle.Constantes.SHOW_OUTPUT_COMPILATION;
import br.uff.ic.oceano.ostra.exception.CompilerException;
import br.uff.ic.oceano.util.Output;
import br.uff.ic.oceano.util.SystemUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.embedder.DefaultConfiguration;
import org.apache.maven.embedder.Configuration;
import org.apache.maven.embedder.ConfigurationValidationResult;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;

/**
 *
 * @author Heliomar
 */
public class MavenUtil {
    private static final String MVN_COMMAND_DEPENDENCY_CLASSPATH = " dependency:build-classpath -f ";
    private static final String MVN_COMMAND_DEPENDENCY_CLASSPATH_RESULT_INITIAL_DELIMITER = "[INFO] Dependencies classpath:\n";
    private static final String MVN_COMMAND_DEPENDENCY_CLASSPATH_RESULT_FINAL_DELIMITER = "\n[INFO]";
    private static final String MVN_COMMAND_DEPENDENCY_CLASSPATH_DELIMITER = ";";
    private static final String MVN_POM = "pom.xml";

    private static final String M2 = getAndVerifyM2();
    public static final String MAVEN = (SystemUtil.isWindows() ? (M2 + SystemUtil.FILESEPARATOR + "mvn.bat") : "mvn ");
    private static final String MAVEN2_CLEAN_INSTALL = MAVEN + " clean install -DskipTests -f ";
    private static final String MAVEN2_CLEAN_COMPILE = MAVEN + " -Dmaven.test.skip=true clean compile -f ";


    public static final String MAVEN2_ERROR_BUILD = "[ERROR] BUILD";
    public static final String MAVEN2_ERROR_FATAL = "[ERROR] FATAL ERROR"; //POM not found kind, parent pom not found...

    public static final String MAVEN2_BASE_MAIN_SOURCE_FILES = "src" + SystemUtil.FILESEPARATOR + "main" + SystemUtil.FILESEPARATOR + "java";
    public static final String MAVEN2_BASE_TEST_FOLDER = "src" + SystemUtil.FILESEPARATOR + "test" + SystemUtil.FILESEPARATOR;
    public static final String MAVEN2_BASE_COMPILED_FILES = "target" + SystemUtil.FILESEPARATOR + "classes";
    public static final String SETTINGS_MAVEN_DEFAULT = System.getProperty("user.home").concat(SystemUtil.FILESEPARATOR).concat(".m2").concat(SystemUtil.FILESEPARATOR).concat("settings.xml");
    public static final String REPOSITORY_MAVEN_LOCAL_DEFAULT = System.getProperty("user.home").concat(SystemUtil.FILESEPARATOR).concat(".m2").concat(SystemUtil.FILESEPARATOR).concat("repository");

    public static boolean COMPILE_WITH_COMMAND_LINE = true;

    public static List<Throwable> execute(String pathProject, List<String> goals, String absolutePathSettings) throws Exception {
        List<String> retorno = mavenExecutionCommandLine(pathProject, goals);
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < retorno.size(); i++) {
            output.append(retorno.get(i)).append("\n");
        }
        if (output.toString().contains("[ERROR]")) {
            return new ArrayList(Arrays.asList(new Exception(output.toString())));
        }
        return null;
    }

    private static String setupPath(String path) {
        final String scape = "\"";

        //already prepared
        if (path.contains(scape)) {
            return path;
        }
        //fix file separator
        path = PathUtil.getWellFormedPath(path);

        //convert to absolute path
        if(PathUtil.isRelativePath(path)){
            path = PathUtil.getAbsolutePathFromRelativetoCurrentPath(path);
        }

        if(new File(path).isDirectory()){
            //add System file separator
            path += path.endsWith(SystemUtil.FILESEPARATOR) ? "" : SystemUtil.FILESEPARATOR;
        }

        //support paths with blank spaces
        path = scape + path + scape;

        return path;
    }

    private static String addFileToDirectoryPath(String path, String file) {
        //prepare path
        path = setupPath(path);

        //remove " from start & end
        path = path.replace("\"", "");

        //add file
        path += file;

        return path;
    }

    private static List<String> mavenExecutionCommandLine(String pathProject, List<String> goals) throws Exception {
        final String[] environmentVariable = SystemUtil.getEnvironmentVariables();
        File filePomDirectory = new File(addFileToDirectoryPath(pathProject, "pom.xml"));
        StringBuilder command = new StringBuilder(MAVEN);
        for (String goal : goals) {
            command.append(" ").append(goal);
        }
        command.append(" -f ");

        String path = filePomDirectory.getAbsolutePath();
        path = setupPath(path);
        command.append(path);

        Process p = Runtime.getRuntime().exec(command.toString(), environmentVariable);
//        p.waitFor();
        BufferedReader saida = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        List<String> retorno = new ArrayList<String>();
        while ((line = saida.readLine()) != null) {
            retorno.add(line);
        }
        return retorno;
    }

    private static MavenExecutionResult getResult(String pathProject, List<String> goals, String absolutePathSettings) throws Exception {
        File filePom = new File(pathProject, "pom.xml");

        MavenExecutionRequest request = new DefaultMavenExecutionRequest().setBaseDirectory(filePom.getParentFile()).setGoals(goals);
        Configuration con = new DefaultConfiguration().setUserSettingsFile(MavenEmbedder.DEFAULT_USER_SETTINGS_FILE).setClassLoader(Thread.currentThread().getContextClassLoader());
        if (absolutePathSettings != null) {
            request.setUserSettingsFile(new File(absolutePathSettings));
            con.setUserSettingsFile(new File(absolutePathSettings));
        }

        request.setLoggingLevel(MavenExecutionRequest.LOGGING_LEVEL_ERROR);
        request.setShowErrors(false);



        con.setLocalRepository(MavenEmbedder.defaultUserLocalRepository);

        ConfigurationValidationResult validationResult = MavenEmbedder.validateConfiguration(con);
        if (!validationResult.isValid()) {
            System.out.println(validationResult.getGlobalSettingsException().getMessage());
        }

//        System.out.println("verificando " + pathProject + " absolutePathSettings " + absolutePathSettings);

        return new MavenEmbedder(con).execute(request);

    }

    public static List<String> getProjectClassPaths(String localPathRevision) throws Exception {
        MavenExecutionResult result = getResult(localPathRevision, Arrays.asList("dependency:resolve"), null);

//        List<Throwable> exceptions = result.getExceptions();
//        for (Throwable throwable : exceptions) {
//            throwable.printStackTrace();
//        }
        if (result.hasExceptions()) {
            throw new MavenException(result.getExceptions().toString());
        }

        Map<String, Artifact> artifactMap = result.getProject().getArtifactMap();
        List<String> classpath = new ArrayList<String>(artifactMap.size());
        for (String artifact : artifactMap.keySet()) {
            String path = artifactMap.get(artifact).getFile().getAbsolutePath();
//            System.out.println("artifact = " + path);
            classpath.add(path);
//            for (String string : artifact.getDependencyTrail()) {
//                System.out.println("                 " + string);
//            }
        }
        Collections.sort(classpath);
        return classpath;
    }

    private static ClassLoader makeProjectClassLoader(Revision revision) throws Exception {

        //TODO:Rever se essa parte pode ser reitrada daqui.
        String path = revision.getLocalPath();
        if (!path.endsWith(SystemUtil.FILESEPARATOR)) {
            path = path.concat(SystemUtil.FILESEPARATOR);
        }
        //---

        String pathclasses;
        pathclasses = path;
        pathclasses = pathclasses.concat(MAVEN2_BASE_COMPILED_FILES);

        List<String> projectClassPaths = getProjectClassPaths(revision.getLocalPath());
        projectClassPaths.addAll(Arrays.asList(System.getProperty("sun.boot.class.path").split(";")));
        projectClassPaths.addAll(Arrays.asList(System.getProperty("java.ext.dirs").split(";")));
        URL urls[] = new URL[projectClassPaths.size() + 1];

        for (int i = 0; i < projectClassPaths.size(); i++) {
            //urls[i] = new URL((String) projectClassPaths.get(i));
            urls[i] = (new File(projectClassPaths.get(i))).toURL();
        }
        urls[projectClassPaths.size()] = (new File(pathclasses)).toURL();
        return new URLClassLoader(urls);

    }
    private static Revision revisionFromLastClassLoader;
    private static ClassLoader lastClassLoader;

    synchronized public static ClassLoader getProjectClassLoaderByCommandLine(Revision revision) throws MavenException {
        if (revisionFromLastClassLoader != null && revisionFromLastClassLoader.equals(revision)) {
            return lastClassLoader;
        }

        final JavaRevisionTool revTool = new JavaRevisionTool();
        final List<String> classPaths = new ArrayList<String>(Arrays.asList(getClassPathsByCommandLine(revision)));
        classPaths.addAll(Arrays.asList(System.getProperty("sun.boot.class.path").split(";")));
        classPaths.addAll(Arrays.asList(System.getProperty("java.ext.dirs").split(";")));
        try {
            classPaths.addAll(revTool.getCompilationFolders(revision));
        } catch (Exception ex) {
            throw new MavenException(ex);
        }

        //make classloader
        try {
            URL[] urls = new URL[classPaths.size()];
            for (int i = 0; i < classPaths.size(); i++) {
                urls[i] = (new File(classPaths.get(i))).toURL();
            }
            return new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        } catch (MalformedURLException ex) {
            throw new MavenException(ex);
        }
    }

    /**
     * Este métdo retorna o classpath de um projeto Maven, porém utiliza linha
     * de comando. Ese método não depende de boa formação do pom como o
     * getProjectClassPaths e sempre funcionado, ao contrário do método citado.
     *
     * @param revision
     * @return
     * @throws MavenException
     */
    synchronized public static String[] getClassPathsByCommandLine(Revision revision) throws MavenException {
        //get project class paths
        try {
            String preparedPath = setupPath(addFileToDirectoryPath(revision.getLocalPath(), MVN_POM));

            final String createGetClassPathCommand = MAVEN + MVN_COMMAND_DEPENDENCY_CLASSPATH + preparedPath;
            CommandLineIinterfaceUtils ecs = new CommandLineIinterfaceUtils(createGetClassPathCommand);
            String classPathAnswer = ecs.executa();

            Set<String> classPaths = new HashSet<String>();
            while (classPathAnswer.contains(MVN_COMMAND_DEPENDENCY_CLASSPATH_RESULT_INITIAL_DELIMITER)) {
                final int indexOfInitialDelimiter = classPathAnswer.indexOf(MVN_COMMAND_DEPENDENCY_CLASSPATH_RESULT_INITIAL_DELIMITER);
                classPathAnswer = classPathAnswer.substring(indexOfInitialDelimiter + MVN_COMMAND_DEPENDENCY_CLASSPATH_RESULT_INITIAL_DELIMITER.length());
                final int indexOfFinalDelimiter = classPathAnswer.indexOf(MVN_COMMAND_DEPENDENCY_CLASSPATH_RESULT_FINAL_DELIMITER);

                final String oneClassPathAnswer = classPathAnswer.substring(0, indexOfFinalDelimiter);
                if (!oneClassPathAnswer.isEmpty()) {
                    classPaths.addAll(Arrays.asList(oneClassPathAnswer.split(MVN_COMMAND_DEPENDENCY_CLASSPATH_DELIMITER)));
                }

                classPathAnswer = classPathAnswer.substring(indexOfFinalDelimiter);
            }
//            for (String string : classPaths) {
//                System.out.println("classPath = " + string);
//            }
            return classPaths.toArray(new String[0]);

        } catch (Throwable ex) {
            throw new MavenException(ex);
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    final static String DEFAULT_SETTINGS_XML_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<settings xmlns=\"http://maven.apache.org/SETTINGS/1.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "xsi:schemaLocation=\"http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd\">\n"
            + "<!-- This settings.xml was created by Oceano. And it follows the default. -->\n"
            + "</settings>";

    public static File createSettingsXml() {
        try {
            File settingXml = new File(SETTINGS_MAVEN_DEFAULT);
            BufferedWriter bw = new BufferedWriter(new FileWriter(settingXml));
            bw.append(DEFAULT_SETTINGS_XML_CONTENT);
            bw.close();

            return settingXml;
        } catch (IOException ex) {
            throw new br.uff.ic.oceano.core.exception.InfraestruturaException(ex);
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    public synchronized static void compile(Revision revision) throws MavenException {

        if (!revision.getProject().isMavenProject()) {
            throw new MavenException("Unsupported project type.");
        }

        //check if it is already compiled
        File file = new File(revision.getLocalPath());
        Collection filenames = FileUtils.getAllFilesInFolderAndSubFolders(file, ".class");
        if (!filenames.isEmpty()) {
            return;
        }

        try {
            if (COMPILE_WITH_COMMAND_LINE) {
                compileWithCommandLineMaven2(revision.getLocalPath());
            } else {
                List<Throwable> exceptions = execute(revision.getLocalPath(), Arrays.asList("compile"), null);

                if (exceptions != null && !exceptions.isEmpty()) {
                    throw new MavenException(exceptions.get(0));
                }
            }
        } catch (Exception ex) {
            revision.setCannotCompile(true);
            throw new MavenException(ex);
        }
        revision.setCannotCompile(false);
    }

    /**
     * This method should not be directly used. The compile(Revision) should me
     * called.
     *
     * @param path
     * @throws CompilerException
     */
    public static void compileWithCommandLineMaven2(String path) throws CompilerException {
        try {
            if (SHOW_OUTPUT_COMPILATION) {
                Output.println("-----> Begin Maven2 compilation");
            }

            final String preparedPath = path + (path.endsWith("/") ? "" : "/");
            CommandLineIinterfaceUtils ecs = new CommandLineIinterfaceUtils(MAVEN2_CLEAN_INSTALL + preparedPath + MVN_POM);
            String compilerOutput = ecs.executa();
            if (compilerOutput.contains(MAVEN2_ERROR_BUILD) || compilerOutput.contains(MAVEN2_ERROR_FATAL)) {

                //ups, an error installing. Maybe it can compile. Lets try.
                //"Compile" goal only compile the src/main, while the install compiles the test too.
                Output.println("============= Trying to compile only, but not install.");
                ecs = new CommandLineIinterfaceUtils(MAVEN2_CLEAN_COMPILE + preparedPath + MVN_POM);
                compilerOutput = ecs.executa();

                //if fails again, then throw exception
                if (compilerOutput.contains(MAVEN2_ERROR_BUILD) || compilerOutput.contains(MAVEN2_ERROR_FATAL)) {
                    throw new CompilerException("Build Error!");
                }
            }

            if (SHOW_OUTPUT_COMPILATION) {
                System.out.println("-----> End Maven2 compilation");
            }

        } catch (Exception ex) {
            throw new CompilerException(ex);
        }
    }

    private static String getAndVerifyM2() {
        final String m2 = System.getenv("M2");

        if (m2 != null && m2.contains(" ")) {
            throw new ExceptionInInitializerError("Please set up your maven instalation with a path without spaces. Actual is: \"" + m2 + "\"");
        }
        return m2;
    }
}
