/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.test.testmavenproject;

/**
 *
 * @author wallace
 */
public class Veiculo {
     String marca;
     double peso;
     private String chassi;
     private int numero;

     public void acelerar(){
         System.out.println("Acelerando");
     }
     public String getMarca(){
         return marca;
     }
     public double getPeso(){
         return peso;
     }
     public void setMarca(String marca){
         this.marca=marca;
     }
     public void setPeso(double peso){
         this.peso=peso;
     }
}
