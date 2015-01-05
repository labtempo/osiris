/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.vnodes.vbox;

/**
 *
 * @author Felipe
 */
public class VNode {

    private String name;
    private long ID;
    private String dataSource;
    private String lastMeasure;
    private boolean active;

    public VNode() {
        this.ID = System.currentTimeMillis();
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    /**
     * @return the ID
     */
    public long getID() {
        return ID;
    }

    /**
     * @return the dataSource
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the lastMeasure
     */
    public String getLastMeasure() {
        return lastMeasure;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }
}
