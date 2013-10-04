/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ostra.dao.MeasurementLogDao;
import br.uff.ic.oceano.ostra.dao.impl.MeasurementLogDaoImpl;
import br.uff.ic.oceano.ostra.model.MeasurementLog;
import java.util.Date;

/**
 *
 * @author DanCastellani
 */
public class LogService implements PersistenceService {

    private MeasurementLogDao MeasurementLogDao;

    public void setup() {
        MeasurementLogDao = ObjectFactory.getObjectWithDataBaseDependencies(MeasurementLogDaoImpl.class);
    }

    public LogService() {
    }

    @Transacional
    public void log(Revision revision, String logMessage) throws ServiceException {
        MeasurementLog ml = new MeasurementLog();
        ml.setRevisionNumber(revision.getNumber());
        ml.setSoftwareProject(revision.getProject());
        ml.setLog(logMessage);
        ml.setDate(new Date());

        MeasurementLogDao.inclui(ml);
    }
}
