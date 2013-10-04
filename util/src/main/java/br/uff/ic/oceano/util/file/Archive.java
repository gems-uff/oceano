package br.uff.ic.oceano.util.file;
/*
 * Arquivo.java
 *
 * Created on 20 de Outubro de 2006, 16:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 *
 * @author Heliomar kann
 */
public class Archive {

    // <editor-fold defaultstate="collapsed" desc=" vars of the Class ">
    private FileInputStream stream;
    private InputStreamReader streamReader;
    private BufferedReader reader;
    private String nome;
    private FileOutputStream fileOut;
    private OutputStreamWriter saida;
    private BufferedWriter intercalado;
    private boolean aberto = false;
    private boolean fechado = false;
    private String charSet = "ISO-8859-1";
    // </editor-fold>

    public Archive(String nomeArquivo) {
        this.nome = PathUtil.getWellFormedPath(nomeArquivo);
    }

    public Archive(String nomeArquivo, String charSet) {
        this.nome = PathUtil.getWellFormedPath(nomeArquivo);;
        this.charSet = charSet;
    }

    private void openFile() {
        try {
            stream = new FileInputStream(this.nome);
            streamReader = new InputStreamReader(stream, Charset.forName(charSet));
            reader = new BufferedReader(streamReader);
            aberto = true;
            fechado = false;
        } catch (FileNotFoundException e) {
            //No caso do arquivo nao existir
            System.err.println("Erro no Arquivo do tipo : " + e.getMessage() + "  Classe " + this.getClass().getName());
        }
    }

    public void closeFileReader() {
        try {
            reader.close();
            streamReader.close();
            stream.close();
            aberto = false;
            fechado = true;
        } catch (FileNotFoundException e) {
            //No caso do arquivo nao existir
            System.err.println("Erro no Arquivo do tipo : " + e.getMessage() + "  Classe " + this.getClass().getName());
        } catch (IOException e) {
            System.err.println("Erro no Arquivo do tipo : " + e.getMessage() + "  Classe " + this.getClass().getName());
        }
    }

    private File createFile() throws IOException {
        File file = new File(nome);

        //already exists
        if (file.exists()) {
            return file;
        }

        //creating parent directories if necessary
        String parentDir = file.getParent();
        if (parentDir != null) {
            File fileParent = new File(file.getParent());
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
        }


        //create file
        file.createNewFile();

        return file;
    }

    private BufferedWriter createFileOutputAppend(boolean append) {
        try {
            File file = createFile();
            fileOut = new FileOutputStream(file, append);
            saida = new OutputStreamWriter(fileOut);
            intercalado = new BufferedWriter(saida);
            aberto = true;
            append = true;
        } catch (IOException ex) {
            System.err.println("Erro no Arquivo do tipo : " + ex.getMessage() + "  Classe " + this.getClass().getName());
            ex.printStackTrace();
        }
        return intercalado;

    }

    private BufferedWriter createFileOutput() {
        return createFileOutputAppend(false);
    }

    public void closeOutFile() {
        try {
            if (!aberto) {
                return;
            }
            intercalado.close();
            aberto = false;
        } catch (FileNotFoundException e) {
            //No caso do arquivo nao existir
            System.err.println("Erro no Arquivo do tipo : " + e.getMessage() + "  Classe " + this.getClass().getName());
        } catch (IOException e) {
            System.err.println("Erro no Arquivo do tipo : " + e.getMessage() + "  Classe " + this.getClass().getName());
        }
    }

    public void openAppendAndClose(String string) {
        try {
            this.createFileOutputAppend(true);
            intercalado.write(string);
            intercalado.newLine();
            this.closeOutFile();
        } catch (IOException e) {
            System.err.println("Erro no Arquivo do tipo : " + e.getMessage() + "  Classe " + this.getClass().getName());
        }

    }

    public void writeNewLine(String linha) {

        try {
            if (!aberto) {
                this.createFileOutput();
            }
            intercalado.write(linha);
            intercalado.newLine();
        } catch (IOException e) {
            System.err.println("Erro no Arquivo do tipo : " + e.getMessage() + "  Classe " + this.getClass().getName());
        }
    }

    public void deleteFile() {
        if (!new File(nome).delete()) {
            System.err.println("Erro no Arquivo do tipo : ERRO AO DELETAR ARQUIVO,  Classe " + this.getClass().getName());
        }
    }

    public boolean existsFile() {
        return new File(nome).exists();
    }

    public String readLine() {
        if (!aberto) {
            openFile();
        }
        String temp = null;
        try {
            temp = reader.readLine();
        } catch (IOException erro) {
            javax.swing.JOptionPane.showMessageDialog(null, "entrou no erro do lelinha");
            System.err.println("Erro no Arquivo do tipo : " + erro.getMessage() + "  Classe " + this.getClass().getName());
        }
        if ((temp == null) && !fechado) {
            closeFileReader();
        }
        return temp;
    }

    public String getName() {
        return nome;
    }
}
