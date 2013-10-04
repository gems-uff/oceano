package br.uff.ic.oceano.polvo.service;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import br.uff.ic.oceano.core.tools.metrics.service.MeasurementService;
import java.util.Calendar;

/**
 *
 * @author Rafael
 */
public class PolvoMetricService extends MetricService {
    private br.uff.ic.oceano.core.service.vcs.VCSService vCSService;
    private ProjectUserService projectUserService;

    @Override
    public void setup(){
        projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
    }

    public PolvoMetricService() {
        super();
        super.setup();
        vCSService = ObjectFactory.getObjectWithDataBaseDependencies(VCSService.class);
    }

    public MetricValue extractMetricFromRevision(SoftwareProject project, OceanoUser oceanoUser) throws ServiceException {
        System.out.println("Pegando usuário... ");
        ProjectUser projectUser = projectUserService.getByProjectAndOceanoUser(project, oceanoUser);
        System.out.println("Login: <" + projectUser.getLogin() + ">");

        MetricValue metricValue = null;
        String revisionsPath = null;
        try {

            System.out.println("project="+project.getConfigurationItem().getName());
            Revision revision = vCSService.getRevision(project, projectUser, Calendar.getInstance());

            revision.setProject(project);

            revision.setLocalPath(ApplicationConstants.DIR_BASE_CHECKOUTS + revision.getProject().getConfigurationItem().getName().replaceAll(" ", "") + "-rDeltaMetricsBuilder");
            vCSService.doCheckout(revision, projectUser, true);
            revisionsPath = revision.getLocalPath();
            System.out.println("revisionsPath = " + revisionsPath);



            MetricManagerFactory  metricManagerFactory = MetricManagerFactory.getInstance();
            metricValue = MeasurementService.extractMetric(metricManagerFactory.getMetricManager(getMetric("Design Size In Classes")), revision);
            System.out.println("metricValue="+metricValue.getDoubleValue());
        } catch (VCSException ex) {
            throw new ServiceException(ex);
        }
        
        return metricValue;
    }
}
