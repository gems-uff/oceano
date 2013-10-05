package br.ic.uff.gems.oceano.ourico.experimento.VCS.type;

import java.io.File;
import java.io.IOException;

public interface DistributedVCS extends VCS {

    public void clone(String configuration, String repository, String workspace) throws Exception;

    public void checkin(String workspace) throws Exception;

}
