/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.peixeespada.controller;

import br.uff.ic.oceano.contexto.ConstantesAplicacao;
import br.uff.ic.oceano.controller.BaseBean;
import br.uff.ic.oceano.util.file.FileUtils;
import java.util.ArrayList;
import br.uff.ic.oceano.util.file.Archive;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author Heliomar
 */
public class PeixeEspadaBean extends BaseBean {

    private ArrayList<UploadedFile> files = new ArrayList<UploadedFile>();
    private int uploadsAvailable = 1;
    private Archive archiveInfo = new Archive(ConstantesAplicacao.DIR_FILE_INFO_UPLOAD);
    private static final String PAGINA_DOWN_JNLP = "def:/privado/peixeespada/formPeixeEspada";
    private static final String PAGINA_UP_JNLP = "def:/privado/peixeespada/uploadPeixeCliente";

    public PeixeEspadaBean() {
        super("PeixeEspadaBean");
        sessao.setPerfilPeixeEspada();
    }
    
    public String pageDownloadJNPL(){
        return PAGINA_DOWN_JNLP;
    }

    public String pageUploadJNPL(){
        return PAGINA_UP_JNLP;
    }

    public int getSize() {
        return  (files != null) ? files.size(): 0;
    }

    public String getInfoUpload() {
        StringBuilder sb = new StringBuilder();
        if (archiveInfo.existsFile()) {
            // pegando todas as linhas do arquivo
            String line = null;
            sb.append("<br/>");
            while ((line = archiveInfo.readLine()) != null) {
                sb.append(line);
                sb.append("<br/>");
            }
        } else {
            sb.append(getMessageResourceString("msgNoPeixeEspadaClientFileInServer", null));
        }
        return sb.toString();
    }

    public void listener(FileUploadEvent event) throws Exception {
        final UploadedFile uploadedFile = event.getUploadedFile();
        
        if (uploadedFile.getFileExtension().compareToIgnoreCase("zip")!=0 ) {
            error("Only files ended with '.zip' are accepted");
            return;
        }
        
        final Path folder = Paths.get(ConstantesAplicacao.DIR_BASE_JNLP);
        String filename = FilenameUtils.getBaseName(uploadedFile.getName()); 
        String extension = FilenameUtils.getExtension(uploadedFile.getName());
        Path tempPath = Files.createTempFile(folder, filename, "." + extension);
        
        try (InputStream input = uploadedFile.getInputStream()) {
            Files.copy(input, tempPath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        if(FileUtils.extractZip(tempPath.toFile(), tempPath.toFile().getParentFile())){
            String currentVersion = uploadedFile.getName().substring(0, uploadedFile.getName().length()-4);
            StringBuilder sb = new StringBuilder();
            if(archiveInfo.existsFile()){
                // pegando as duas primeiras linhas do arquivo
                String infoUltimo = archiveInfo.readLine();
                sb.append("Último upload");
                sb.append(infoUltimo.substring(infoUltimo.indexOf(":")));
                sb.append("\n");
                infoUltimo = archiveInfo.readLine();
                sb.append("Última versão");
                sb.append(infoUltimo.substring(infoUltimo.indexOf(":")));
                sb.append("\n");
                archiveInfo.closeFileReader();
                archiveInfo.deleteFile();
            }else{
                sb.append("Último upload: Nunca\n");
                sb.append("Última versão: Nenhuma\n");
            }
            archiveInfo.openAppendAndClose("Upload atual em: "+new Date());
            archiveInfo.openAppendAndClose("Versão atual: "+currentVersion);
            archiveInfo.openAppendAndClose("-----------------------------------");
            archiveInfo.openAppendAndClose(sb.toString());
        }

        files.add(uploadedFile);
        uploadsAvailable--;
    }

    public int getUploadsAvailable(){
        return uploadsAvailable;
    }

    public String clearUploadData() {
        files.clear();
        return null;
    }

    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public ArrayList<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<UploadedFile> files) {
        this.files = files;
    }

}

