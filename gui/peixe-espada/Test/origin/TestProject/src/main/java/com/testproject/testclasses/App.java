package com.testproject.testclasses;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DownClass downClass = new DownClass();
        Inline inline = new Inline("xyz");
        System.out.println( "321" + inline.inlineMethod() );

    }
}
