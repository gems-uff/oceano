///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package sandbox.ouricoupdate;
//
//import br.uff.ic.oceano.ourico.rcs.Subversion;
//import java.io.File;
//import java.util.List;
//import org.testng.annotations.Test;
//import org.tmatesoft.svn.core.SVNException;
//
///**
// *
// * @author marapao
// */
//public class TesteMerge {
//
//    @Test
//    public void merge() throws SVNException{
//
//        String workspace = "/home/marapao/Desktop/sandbox/7";
//        String url = "https://10.0.0.100/svn/trunk";
//
//        Subversion svn = new Subversion(url, "marapao", "marapa");
//        List<String> mergePath = svn.mergePath(url, workspace);
//
//        for (String string : mergePath) {
//            System.out.println("String = "+string);
//        }
//    }
//}
