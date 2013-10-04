/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.model;

/**
 *
 * @author heron
 */
public class RevisionGit extends Revision{
    private String sha1;
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RevisionGit other = (RevisionGit) obj;
        if (this.sha1 != other.sha1 && (this.sha1 == null || !this.sha1.equals(other.sha1))) {
            return false;
        }
        if (this.project != other.project && (this.project == null || !this.project.equals(other.project))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.sha1 != null ? this.sha1.hashCode() : 0);
        hash = 19 * hash + (this.project != null ? this.project.hashCode() : 0);
        return hash;
    }
    
    public String getSHA1()
    {
        return sha1;
    }
    
    public void setSHA1(String sha1)
    {            
        this.sha1 = sha1;
    }
            
}
