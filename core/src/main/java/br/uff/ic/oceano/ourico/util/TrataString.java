/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.util;

/**
 *
 * @author marapao
 */
public class TrataString {

    public String contatBarra(String s){
        if(!s.endsWith("/"))
            s = s.concat("/");
            return s;
    }
}
