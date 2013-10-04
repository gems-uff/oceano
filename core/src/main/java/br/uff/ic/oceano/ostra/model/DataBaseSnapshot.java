/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.model;

import br.uff.ic.oceano.util.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class DataBaseSnapshot {

    private List<String> instances;
    private List<String> attributes;
    private Date snapshotTime;

    public DataBaseSnapshot() {
        this.snapshotTime = new Date();

        this.attributes = new ArrayList<String>();
        this.instances = new ArrayList<String>();

    }

    public DataBaseSnapshot(List<String> instances, List<String> attributes) {
        this.snapshotTime = new Date();
        this.instances = instances;
        this.attributes = attributes;
    }

    /**
     * @return the instances
     */
    public List<String> getInstances() {
        return instances;
    }

    /**
     * @param instances the instances to set
     */
    public void setInstances(List<String> instances) {
        this.instances = instances;
    }

    /**
     * @return the attributes
     */
    public List<String> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the snapshotTime
     */
    public Date getSnapshotTime() {
        return snapshotTime;
    }

    /**
     * @return the snapshotTime
     */
    public String getFormatedSnapshotTime() {
        return DateUtil.format(snapshotTime);
    }

    /**
     * @param snapshotTime the snapshotTime to set
     */
    public void setSnapshotTime(Date snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    public void setInstancesSize() {
    }

    public int getInstancesSize() {
        if (instances == null) {
            return 0;
        }
        return instances.size();
    }

    public void setAttributesSize() {
    }

    public int getAttributesSize() {
        if (attributes == null) {
            return 0;
        }
        return attributes.size();
    }
}
