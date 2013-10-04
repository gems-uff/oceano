/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.service;

/**
 *
 * @author Heliomar
 */
public interface PersistenceService {

    /**
     * Use this method to initialize DAOS and others Services tath work with persistence models (use database)
     */
    public void setup();

}
