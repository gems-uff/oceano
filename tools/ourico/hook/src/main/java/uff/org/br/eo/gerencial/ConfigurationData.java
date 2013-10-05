/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uff.org.br.eo.gerencial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author marapao
 */
public class ConfigurationData {


    public static final String MAVEN_REPOSITORY = "mavenRepository";
    public static final String MAVEN_SETTINGS = "mavenSettings";
    public static final String WORKSPACE = "workspace";
    public static final String DIRETORIO_AUTOBRANCH = "diretoryAutobranch";
    public static final String DIRETORIO_PROTECTED = "diretoryProtected";
    public static final String URL_OCEANO = "urlOceano";

    private String mavenRepository;
    private String mavenSettings;
    private String workspace;
    private String diretoryAutobranch;
    private String diretoryProtected;
    private String urlOceano;

    /**
     * @return the mavenRepository
     */
    public String getMavenRepository() {
        return mavenRepository;
    }

    /**
     * @param mavenRepository the mavenRepository to set
     */
    public void setMavenRepository(String mavenRepository) {
        this.mavenRepository = mavenRepository;
    }

    /**
     * @return the mavenSettings
     */
    public String getMavenSettings() {
        return mavenSettings;
    }

    /**
     * @param mavenSettings the mavenSettings to set
     */
    public void setMavenSettings(String mavenSettings) {
        this.mavenSettings = mavenSettings;
    }

    /**
     * @return the workspace
     */
    public String getWorkspace() {
        return workspace;
    }

    /**
     * @param workspace the workspace to set
     */
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    /**
     * @return the diretorioAutobranch
     */
    public String getDiretoryAutobranch() {
        return diretoryAutobranch;
    }

    /**
     * @param diretorioAutobranch the diretorioAutobranch to set
     */
    public void setDiretoryAutobranch(String diretorioAutobranch) {
        this.diretoryAutobranch = diretorioAutobranch;
    }

    public void set(ConfigurationData conf, String field, String value) {

        if (field.equals(MAVEN_REPOSITORY)) {
            conf.setMavenRepository(value);
        } else if (field.equals(MAVEN_SETTINGS)) {
            conf.setMavenSettings(value);
        } else if (field.equals(WORKSPACE)) {
            conf.setWorkspace(value);
        } else if (field.equals(DIRETORIO_AUTOBRANCH)) {
            conf.setDiretoryAutobranch(value);
        } else if (field.equals(DIRETORIO_PROTECTED)) {
            conf.setDiretoryProtected(value);
        } else if (field.equals(URL_OCEANO))
            conf.setUrlOceano(value);


    }

    /**
     * @return the diretorioProtected
     */
    public String getDiretoryProtected() {
        return diretoryProtected;
    }

    /**
     * @param diretorioProtected the diretorioProtected to set
     */
    public void setDiretoryProtected(String diretorioProtected) {
        this.diretoryProtected = diretorioProtected;
    }

    @Override
    public String toString() {
        StringBuffer saida = new StringBuffer();

//        saida.append(MAVEN_REPOSITORY).append(" = ").append(mavenRepository).append("\n");
//        saida.append(MAVEN_SETTINGS).append(" = ").append(mavenSettings).append("\n");
//        saida.append(WORKSPACE).append(" = ").append(workspace).append("\n");
        saida.append(DIRETORIO_AUTOBRANCH).append(" = ").append(diretoryAutobranch).append("\n");
//        saida.append(DIRETORIO_PROTECTED).append(" = ").append(diretoryProtected).append("\n");

        return saida.toString();
    }

    public static ConfigurationData getConfiguration(String pathArquivo) {

        File arquivo = new File(pathArquivo);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String linha = null;
        String variavel = "";
        ConfigurationData configurationData = new ConfigurationData();


        char[] caracteres = null;
        boolean achouColcheteAbrindo = false;
        boolean achouColcheteFechando = false;
        boolean identificouVariavel = false;
        boolean identificouValorVariavel = false;


        try {


            fileReader = new FileReader(arquivo);
            bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.ready()) {

                linha = bufferedReader.readLine();
                caracteres = linha.toCharArray();

                if (linha.isEmpty()) {
                    continue;
                }

                String valorVariavel = "";


                for (char c : caracteres) {
                    if (!identificouVariavel) {
                        if (c == '>') {
                            achouColcheteFechando = true;
                            identificouVariavel = true;
                            linha = "";
                        }

                        if (achouColcheteAbrindo && (!achouColcheteFechando)) {
                            variavel += c;
                        } else if (c == '<') {
                            achouColcheteAbrindo = true;
                        }
                    } else {
                        linha += c;
                    }
                }

                String[] split = null;

                String padrao = ("</" + variavel);
                if (linha.contains(padrao)) {
                    split = linha.split(padrao);
                    valorVariavel = split[0];
                    System.out.println(variavel + " = " + valorVariavel);
                    configurationData.set(configurationData, variavel, valorVariavel);


                }

                variavel = "";

                achouColcheteAbrindo = false;
                achouColcheteFechando = false;
                identificouVariavel = false;
                identificouValorVariavel = false;


            }
            System.out.println(configurationData.toString());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Arquivo (" + arquivo.getAbsolutePath() + ") não existe! ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return configurationData;
    }

    /**
     * @return the urlOceano
     */
    public String getUrlOceano() {
        return urlOceano;
    }

    /**
     * @param urlOceano the urlOceano to set
     */
    public void setUrlOceano(String urlOceano) {
        this.urlOceano = urlOceano;
    }

}
