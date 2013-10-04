/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.decorator;

/**
 *
 * @author daniel
 */
public class DevelopperInformation {

    private String developperName;
    private Integer totalCommits;

    public DevelopperInformation(String developperName, Integer totalCommits) {
        this.developperName = developperName;
        this.totalCommits = totalCommits;
    }

    
    /**
     * @return the totalCommits
     */
    public Integer getTotalCommits() {
        return totalCommits;
    }

    /**
     * @param totalCommits the totalCommits to set
     */
    public void setTotalCommits(Integer totalCommits) {
        this.totalCommits = totalCommits;
    }

    /**
     * @return the developperName
     */
    public String getDevelopperName() {
        return developperName;
    }

    /**
     * @param developperName the developperName to set
     */
    public void setDevelopperName(String developperName) {
        this.developperName = developperName;
    }
}
