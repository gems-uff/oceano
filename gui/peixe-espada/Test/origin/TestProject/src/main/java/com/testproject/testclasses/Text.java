/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testproject.testclasses;

/**
 *
 * @author Joao
 */
public class Text {
    
    private String text;
    
    public Text(String string){
        text = string;
    }
    
    public String toLowerCase(){
        return text.toLowerCase();
    }
    
    public String inlineMethod() {
        return text+"123";
    }
}
