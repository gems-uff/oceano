/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.TokenMgrError;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.VoidType;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author heron
 */
public class NumberOfAccessorMethods extends AbstractMetricExtractor{

     private CompilationUnit compUnit;
    
    public NumberOfAccessorMethods()
    {       
    }
    
    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }
    
    private boolean isAccessorMethod(MethodDeclaration method)
    {        
        if (isGetterMethod(method) || isSetterMethod(method))
            return true;
        else
            return false;                
    }
    
    private int countAccessorMethods()
    {        
        int count = 0;
        
        List<TypeDeclaration> types = compUnit.getTypes();
        
        if (types != null)
            for (TypeDeclaration type : types)
            {
                List<BodyDeclaration> members = type.getMembers();
                if (members != null)                
                    for (BodyDeclaration member : members)
                    {
                        if (member instanceof MethodDeclaration)
                        {
                            MethodDeclaration method = (MethodDeclaration) member;                   

                            if (isAccessorMethod(method))
                                ++count;         
                        }
                    }
                
            }
        
        return count;
    }
    
    private boolean isGetterMethod(MethodDeclaration method)
    {
        if ((method.getBody() != null) && (method.getBody().getStmts() != null))
        {
            LinkedList<String> attNames = getAttributesNames();
            
            if (attNames.size() == 0)
                return false;
            
            List<Statement> stmts = method.getBody().getStmts();        

            for (Statement stmt : stmts)
                for (String name : attNames)
                    if ((stmt.toString().matches("(.*)return(\\s+)" + name + "(\\s*);")) && (method.getModifiers() == ModifierSet.PUBLIC) && (method.getParameters() == null))
                        return true;
        }
        
        return false;
    }
    
    private boolean isSetterMethod(MethodDeclaration method)
    {
        if ((method.getBody() != null) && (method.getBody().getStmts() != null))
        {
            LinkedList<String> attNames = getAttributesNames();
            
            if (attNames.size() == 0)
                return false;
            
            List<Statement> stmts = method.getBody().getStmts();        

            for (Statement stmt : stmts)
                for (String name : attNames)
                    if ((method.getModifiers() == ModifierSet.PUBLIC) && (method.getParameters() != null) && (method.getParameters().size() == 1) && (method.getType().equals(new VoidType())) && (stmt.toString().matches("(.*)"+name+"(\\s*)=(.*)"+method.getParameters().get(0).getId().getName()+"(.*);")))
                        return true;
        }
        
        return false;
    }
    
    private LinkedList<String> getAttributesNames()
    {
        List<TypeDeclaration> types = compUnit.getTypes();
        LinkedList<String> attNames = new LinkedList<String>();
        
        if (types != null)
        {
            for (TypeDeclaration type : types)
            {
                List<BodyDeclaration> members = type.getMembers();
                if (members != null)
                    for (BodyDeclaration member : members)
                        if (member instanceof FieldDeclaration)
                        {
                            FieldDeclaration t = (FieldDeclaration) member;
                            List<VariableDeclarator> variables = t.getVariables();
                            for (VariableDeclarator v : variables)
                                attNames.add(v.getId().getName());
                        }
            }
        }
        
        return attNames;
    }
    
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try 
        {
            compUnit = JavaParser.parse(new FileInputStream(path));
            
            int count = countAccessorMethods();
            
            return createMetricValue(revision, count);
        } 
        catch (ParseException ex) 
        {            
            throw new MetricException("Source code not valid. " + ex.getMessage());
        }
        catch (TokenMgrError ex)
        {
            throw new MetricException("Source code not valid. " + ex.getMessage());
        }
        catch(FileNotFoundException ex)
        {
            throw new MetricException("File " + path + " not found.");
        }
    }
    
}
