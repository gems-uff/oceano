/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util;

import br.uff.ic.oceano.util.file.PathUtil;
import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Element;

/**
 *
 * @author daniel heráclio
 */
public class ConfigurationHelper {

    public static final String skip = "skip";
    public static final String name = "name";
    public static final String value = "value";
    public static final String layer = "layer";
    public static final String subsystem = "subsystem";
    public static final String includepackage = "include-package";
    public static final String excludepackage = "exclude-package";
    public static final String dependsupon = "depends-upon";
    public static final String prefix = "prefix";
    public static final String assertion = "assertion";
    public static final String pattern = "pattern";

    public static Element addComment(Element project, String comment) {
        return project.addContent(new Comment(comment));
    }

    public static Element addLayer(Element project, String layerName) {
        Element layerEl = new Element(layer);
        layerName = layerName.replace(" ", "");
        layerEl.setAttribute(name, layerName);        
        project.addContent(layerEl);
        return layerEl;
    }

    public static Element addSubsystem(Element layer, String subsystemName) {
        Element subsystemEl = new Element(subsystem);
        subsystemName = subsystemName.replace(" ", ""); //names must be tokens
        subsystemEl.setAttribute(name, subsystemName);
        layer.addContent(subsystemEl);
        return subsystemEl;
    }

    /**
     * Add skip element. Seams to set the package to be ignored by dependometer
     *
     * @param project
     * @param skippedPackage
     * @return
     */
    public static Element addSkipped(Element project, String skippedPackage) {
        Element skippedEl = new Element(skip);
        skippedEl.setAttribute(prefix, skippedPackage);
        return project.addContent(skippedEl);
    }

    public static Element addExcludeCompilationUnit(Element project, String skipped) {
        Element skippedEl = new Element("exclude-compilation-unit");
        skippedEl.setAttribute(name, skipped);
        return project.addContent(skippedEl);
    }

    public static void addAssertionPattern(Element project, String assertPattern) {
        Element assertionEl = new Element(assertion);
        assertionEl.setAttribute(pattern, assertPattern);
        project.addContent(assertionEl);
    }

    public static Element setAttribute(Element element, String attribute, String value) {
        return element.setAttribute(new Attribute(attribute, value));
    }

    public static Element addInputDir(Element project, String path) {
        //must be absolute path
        path = PathUtil.getAbsolutePathFromRelativetoCurrentPath(path);
        
        //remove file separator, so dependometer goes recursive
        path = PathUtil.trimLastFileSeparator(path);

        Element element = new Element("input");

        element.setAttribute("dir", path);

        return project.addContent(element);
    }

    /**
     * Can be added to project or to subsystem layer.
     *
     * @param root
     * @param filter
     * @return
     */
    public static Element addIncludePackageFilter(Element root, String filter) {
        Element element = new Element(includepackage);
        element.setAttribute(name, filter);
        return root.addContent(element);
    }

    /**
     * Can be added to project or to subsystem layer.
     *
     * @param root
     * @param filter
     * @return
     */
    public static Element addExcludePackageFilter(Element root, String filter) {
        Element element = new Element(excludepackage);
        element.setAttribute(name, filter);
        return root.addContent(element);
    }
    
    /**
     * Use :: as separator. Ex layer::subsystem
     * @param subsystem
     * @param pathToSubsystem
     * @return 
     */
    public static Element addDependsUponFilter(Element subsystem, String pathToSubsystem) {
        Element element = new Element(excludepackage);
        element.setAttribute(name, pathToSubsystem);
        return subsystem.addContent(element);
    }

    public static void addThresholds(Element project) {        
        addElement(project,"lower-threshold","Project.PercentageOfPackagesWithRcNotLessThanOne",String.valueOf(70));
        addElement(project,"lower-threshold","Project.AverageNumberOfAssertionsPerProjectInternalClass",String.valueOf(1));
        addElement(project,"upper-threshold","Project.ACD",String.valueOf(20));
        addElement(project,"upper-threshold","Project.PackageCyclesExist",String.valueOf(0));
        addElement(project,"upper-threshold","Project.CompilationUnitCyclesExist",String.valueOf(0));
        addElement(project,"upper-threshold","Project.TypeCyclesExist",String.valueOf(0));
        addElement(project,"upper-threshold","Project.MaxDepthOfInheritance",String.valueOf(6));
    }

    private static Element addElement(Element rootNode, String elementName, String attributeName, String attributeValue) {
        Element elementNode = new Element(elementName);
        elementNode.setAttribute(name, attributeName);
        elementNode.setAttribute(value, attributeValue);
        return rootNode.addContent(elementNode);
    }
}
