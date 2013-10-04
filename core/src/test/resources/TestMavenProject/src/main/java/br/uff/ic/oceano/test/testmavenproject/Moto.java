/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.test.testmavenproject;
import br.uff.ic.oceano.test.testmavenproject.newpackage2.*;

/**
 *
 * @author wallace
 */
public class Moto extends Veiculo{

    Gasolina gasolina;
    Gasolina gasolina2;
    protected int speed;
    protected int rodas;
    private int segredo;
    static int estatico;
    public void acelerar(int num){
        System.out.println("Acelerando a "+num);
    }
    public void setGasolina(Gasolina gasolina){
        this.gasolina=gasolina;
    }
    public Gasolina getGasolina(){
        return gasolina;
    }
    public String toString(){
        return "Moto: "+marca;
    }
    static public void parar(){
        System.out.println("Parar");
    }
    
}
