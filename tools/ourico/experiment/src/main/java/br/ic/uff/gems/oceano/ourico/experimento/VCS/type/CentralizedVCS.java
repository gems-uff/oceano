package br.ic.uff.gems.oceano.ourico.experimento.VCS.type;

public interface CentralizedVCS extends VCS {

    public void checkout(String configuration, String repository, String workspace) throws Exception;

    public void checkin(String workspace) throws Exception;

}
