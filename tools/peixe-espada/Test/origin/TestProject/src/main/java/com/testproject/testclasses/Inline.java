/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testproject.testclasses;

/**
 *
 * @author Joao
 */
public class Inline {
    
    private String text;
    
    public Inline(String string){
        text = string;
    }
    
    public String inlineMethod() {
        return text+"123";
    }
}
