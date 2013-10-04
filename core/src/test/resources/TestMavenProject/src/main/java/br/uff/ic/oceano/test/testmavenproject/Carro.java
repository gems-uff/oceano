/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.test.testmavenproject;

import br.uff.ic.oceano.test.testmavenproject.newpackage.NewChartColor;
import br.uff.ic.oceano.test.testmavenproject.newpackage2.Alcool;
import br.uff.ic.oceano.test.testmavenproject.newpackage2.Combustivel;
import br.uff.ic.oceano.test.testmavenproject.newpackage2.Gasolina;
import org.jfree.chart.ChartColor;

/**
 *
 * @author wallace
 */
public class Carro extends Veiculo {

    Gasolina gasolina1;
    protected Gasolina gasolina2;
    Alcool alcool;
    static int parada;
    static String Modelo;

    public void acelerar(int num) {
        int i=0;
        while(i<10){
             i++;
        }
        if(i==10 || i==9 || i==8  || i==9){

        }
        else{

        }
        System.out.println("Acelerando a " + num);
    }

    public void setGasolina(Gasolina gasolina) {
        System.out.println(alcool.getGraus());
        this.gasolina1 = gasolina;
    }

    public Gasolina getGasolina() {
        return gasolina1;
    }

    public void setAlcool(Alcool alcool) {
        this.alcool = alcool;
    }

    public Alcool getAlcool() {
        return alcool;
    }

    private void colorir(ChartColor color) {
        int a = 0;
    }

    private void colorir(NewChartColor color) {
        int i=0;
        while(i<10){
             i++;
        }
        if(i==10 || i==9 || i==8){

        }
        else{

        }
        int b = 0;
    }

    private void colorir(NewChartColor color, Combustivel combustivel) {
        int b = 0;
    }

    static void setar() {
        System.out.print("Setar");
    }
}
