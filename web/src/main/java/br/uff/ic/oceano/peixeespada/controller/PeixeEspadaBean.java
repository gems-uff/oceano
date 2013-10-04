/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.peixeespada.controller;

import br.uff.ic.oceano.contexto.ConstantesAplicacao;
import br.uff.ic.oceano.controller.BaseBean;
import br.uff.ic.oceano.util.file.FileUtils;
import java.io.File;
import java.util.ArrayList;
import br.uff.ic.oceano.util.file.Archive;
import java.util.Date;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

/**
 *
 * @author Heliomar
 */
public class PeixeEspadaBean extends BaseBean {

    private ArrayList<UploadItem> files = new ArrayList<UploadItem>();
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

    public void listener(UploadEvent event) throws Exception {
        UploadItem item = event.getUploadItem();
        File newFileLocation = new File(ConstantesAplicacao.DIR_BASE_JNLP, item.getFileName());
        File f = item.getFile();
        System.out.println("Moveu?: " + FileUtils.move(f, newFileLocation));

        if (!item.getFileName().endsWith(".zip") && !item.getFileName().endsWith(".ZIP")) {
            error("Only files end with '.zip' are accepteds");
            return;
        }

        if(FileUtils.extractZip(newFileLocation, newFileLocation.getParentFile())){
            String currentVersion = item.getFileName().substring(0, item.getFileName().length()-4);
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

        files.add(item);
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

    public ArrayList<UploadItem> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<UploadItem> files) {
        this.files = files;
    }

}

