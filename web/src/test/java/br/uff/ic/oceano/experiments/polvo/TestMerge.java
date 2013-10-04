/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.polvo;

import br.uff.ic.oceano.ourico.rcs.Subversion;
import br.uff.ic.oceano.ourico.util.Casting;
import br.uff.ic.oceano.ourico.verificacao.build.Maven;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.String;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author rafaelss
 */
public class TestMerge {


    @BeforeClass
    public void before(){
    
    }

    @AfterClass
    public void after(){

    }

    @Test
    public void checkout() throws Exception{
        System.out.println("Teste");
        //String url1 = "file:///C:/repositorio/teste/rafaelLocal/trunk";
        //String url2 = "file:///C:/repositorio/teste/rafaelLocal/branches/branch1";
        //String ws = "c:\\checkout\\rafaelLocal\\";

        //String url1 = "file:///C:/repositorio/findbugs/trunk/mojo/maven-native/";
        //String ws = "c:\\checkout\\maven-native\\";

        //String url1 = "file:///C:/repositorio/findbugs/trunk/mojo/gwt-maven-plugin";
        //String url2 = "file:///C:/repositorio/findbugs/branches/gwt-maven-plugin-1.3";
        //String ws = "c:\\checkout\\gwt-maven-plugin\\";

        //String url1 = "file:///C:/repositorio/findbugs/trunk/mojo/tomcat-maven-plugin/tomcat-maven-plugin";
        //String url2 = "file:///C:/repositorio/findbugs/branches/tomcat-maven-plugin-restructure/mojo/tomcat-maven-plugin";
        //String ws = "c:\\checkout\\tomcat-maven-plugin\\";

        //String url1 = "file:///C:/repositorio/findbugs/trunk/mojo/clirr-maven-plugin";
        //String url2 = "file:///C:/repositorio/findbugs/branches/clirr-maven-plugin-2.3.0";
        //String ws = "c:\\checkout\\clirr-maven-plugin\\";

        //String url1 = "file:///C:/repositorio/findbugs/trunk/mojo/weblogic-maven-plugin";
        //String url2 = "file:///C:/repositorio/findbugs/branches/weblogic-maven-plugin-2.8.0";
        //String ws = "c:\\checkout\\weblogic-maven-plugin\\";

        String url1 = "file:///C:/repositorio/cactus/jakarta/cactus/trunk";
        String url2 = "file:///C:/repositorio/cactus/jakarta/cactus/branches/CACTUS_TRUNK_MAVEN2_BRANCH";
        String ws = "c:\\checkout\\cactus\\";


        String wsTmp = "c:\\checkout\\tmp\\";
        //int rev =
        String pathSettings = "c:\\Users\\Rafael\\.m2\\settings.xml";
        String repositorioLocal = "c:\\Users\\Rafael\\.m2\\repository";

        Subversion svn = new Subversion();
        svn.iniciaRepositorio(url1);

        /*
        long checkout = svn.checkout(url1, ws);
        System.out.println("revCheckout=" + checkout);

        deleteDirectory(new File(wsTmp));
        System.out.println("deleteDirectory!");
        copyDirectory(new File(ws), new File(wsTmp));
        System.out.println("copyDirectory!");
         */

        // NAO ESTA FUNCIONANDO...
        //List<String> conflitosFisicos = new ArrayList<String>();
        //conflitosFisicos = svn.mergePath(url2, ws);
        //System.out.println("conflitosFisicos=" + conflitosFisicos);

        /*
        String[] comandLastRevision = null;

        comandLastRevision = new String[]{"svn", "merge", "-r", "1:" + checkout, url2, wsTmp, "--non-interactive"};

        Process p;
        try {
            p = CommandLineIinterfaceUtils.executeComand(comandLastRevision);
        } catch (IOException ex) {
            throw new Exception(ex);
        }
        //readerError(p);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line = null;
        try {
            line = reader.readLine();
            //System.out.println("line="+line);

            while (line != null) {
                if (line.indexOf("Text conflicts:") != -1) {
                    System.out.println("Text conflicts:" + line.substring(line.lastIndexOf(":") + 2));
                }

                line = reader.readLine();
                System.out.println("line="+line);
            }
        } catch (IOException ex) {
            throw new Exception(ex);
        }
        */

        
            Maven mvn = new Maven();
            mvn.setPathSettings(pathSettings);
            mvn.setRepositorioLocal(repositorioLocal);
            mvn.setUrlProjeto(wsTmp);


            String detalhe = null;
            try{
                List<Throwable> listSintatico = mvn.compila();
                detalhe = Casting.ListTrowableToString(listSintatico).toString();
            }catch(NullPointerException ne){
                detalhe = null;
            }catch(Exception ex){
                ex.printStackTrace();
            }
            System.out.println("qtdSintatico=" + detalhe);

            List<Throwable> listSemantico = mvn.testa();

            try{
                detalhe = Casting.ListTrowableToString(listSemantico).toString();
            }catch(NullPointerException ne){
                detalhe = null;
            }

            System.out.println("qtdSemantico=" + detalhe);

          
    }

    private static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
               boolean success = deleteDirectory(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // Agora o diretório está vazio, restando apenas deletá-lo.
        return dir.delete();
    }

    // If targetLocation does not exist, it will be created.
    private void copyDirectory(File sourceLocation , File targetLocation)
    throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    private void readerError(Process p) throws Exception {
        BufferedReader readerError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        if (readerError != null) {
            String erro;
            try {
                erro = readerError.readLine();
            } catch (IOException ex) {
                throw new Exception(ex);
            }
            if (erro != null) {
                throw new Exception(erro);
            }
        }
    }


}
