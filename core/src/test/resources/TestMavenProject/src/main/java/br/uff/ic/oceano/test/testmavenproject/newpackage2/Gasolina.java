/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.test.testmavenproject.newpackage2;

/**
 *
 * @author wallace
 */
public class Gasolina extends Combustivel{
    int nafta;
    double litros;
    public int getNafta(){
        return nafta;
    }
    public void setNafta(int nafta){
        this.nafta=nafta;
    }
    public double getLitros(){
        return litros;
    }
    public void setLitros(double litros){
        this.litros=litros;
    }
}
