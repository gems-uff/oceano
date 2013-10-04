/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.test.testmavenproject.newpackage;
import org.jfree.chart.ChartColor;

/**
 *
 * @author wallace
 */
public class NewChartColor extends ChartColor{
    
    public NewChartColor(int r, int g, int b){
        super(r, g, b);
    }
    public int getBlue(){
        return 255;
    }
    public int getRed(){
        return 0;
    }
}
