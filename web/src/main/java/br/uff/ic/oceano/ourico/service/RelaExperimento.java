/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.model.Estado;
import java.io.File;
import java.io.IOException;
import java.util.List;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author marapao
 */
public class RelaExperimento {

    private static EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);
    private String inputFile;

    public static void main(String[] args) throws IOException, WriteException {

        WritableWorkbook workbook = null;
        
        JPAUtil.setCurrentPersistenceUnit(JPAUtil.PERSISTENCE_UNIT_LOCAL);
        JPAUtil.startUp();


        /*
        Tarefas:
         * Check-out
         * Analise sintatica 1
         * Analise semantica 1
         * merge
         * Analise sintatica 1
         * Analise semantica 1
         * Integração
         */

        int beginingAutobranch = 387;
        int endAutobranch = 529;


        workbook = Workbook.createWorkbook(new File("/home/marapao/output.xls"));

        WritableSheet sheet = workbook.createSheet("First Sheet", 0);

        Label label = null;
        for (int i = beginingAutobranch; i < endAutobranch; i++) {//linha
            if (i - beginingAutobranch == 0) {
                for (int j = 0; j < 15; j++) {//coluna


                    switch (j) {
                        case 0:
                            label = new Label(j, i - beginingAutobranch, "autobranch");
                            break;
                        case 1:
                            label = new Label(j, i - beginingAutobranch, "TCO1 inicial");
                            break;
                        case 2:
                            label = new Label(j, i - beginingAutobranch, "TCO1 Final");
                            break;
                        case 3:
                            label = new Label(j, i - beginingAutobranch, "TSi1 inicial");
                            break;
                        case 4:
                            label = new Label(j, i - beginingAutobranch, "TSi1 final");
                            break;
                        case 5:
                            label = new Label(j, i - beginingAutobranch, "TSe1 inicial");
                            break;
                        case 6:
                            label = new Label(j, i - beginingAutobranch, "TSe1 final");
                            break;
                        case 7:
                            label = new Label(j, i - beginingAutobranch, "TF1 inicial");
                            break;
                        case 8:
                            label = new Label(j, i - beginingAutobranch, "TF1 final");
                            break;
                        case 9:
                            label = new Label(j, i - beginingAutobranch, "TSi2 inicial");
                            break;
                        case 10:
                            label = new Label(j, i - beginingAutobranch, "TSi2 final");
                            break;
                        case 11:
                            label = new Label(j, i - beginingAutobranch, "TSe2 inicial");
                            break;
                        case 12:
                            label = new Label(j, i - beginingAutobranch, "TSe2 final");
                            break;
                        case 13:
                            label = new Label(j, i - beginingAutobranch, "TI inicial");
                            break;
                        case 14:
                            label = new Label(j, i - beginingAutobranch, "TI final");
                            break;
                    }

                    sheet.addCell(label);
                }
            }

            long ii = i;
            List<Estado> estados = estadoService.getByAutobranch(ii);

            label = new Label(0, i - beginingAutobranch + 1, i + "");
            sheet.addCell(label);

            int j = 0;
            for (Estado estado : estados) {

                ++j;

                label = new Label(2 * j - 1, i - beginingAutobranch + 1, estado.getInicio().toString());
                sheet.addCell(label);

                label = new Label(2 * j, i - beginingAutobranch + 1, estado.getFim().toString());
                sheet.addCell(label);

            }




        }
        workbook.write();
        workbook.close();




    }
}
