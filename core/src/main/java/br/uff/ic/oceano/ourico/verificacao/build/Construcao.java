/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.verificacao.build;

import java.util.List;

/**
 *
 * @author marapao
 */
public interface Construcao {

    public List<Throwable> compila() throws Exception;

    public List<Throwable> testa() throws Exception;

    public List<Throwable> limpa() throws Exception;

    public List<Throwable> executaAcoes(String[] objetivos) throws Exception;

    public  List<Throwable> instala() throws Exception;

}
