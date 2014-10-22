/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Zakarea Al Shara
 */
public class HostAPI implements DropDownInterface {

    private static Integer id = -1;
    private Integer hostNo = 50;
    private Integer storage = 1000000;
    private Integer cpuNo = 2;
    private Integer mips = 2660;
    private Integer ram = 4096;
    private Integer bw = 1000000;
    private String hostPower = "86, 89.4, 92.6, 96, 99.5, 102, 106, 108, 112, 114, 117";
    
    private String ramProvisioner = "Simple";
    private String[] ramProvisionerItems = new String[]{"Simple"};
    
    private String bwProvisioner = "Simple";
    private String[] bwProvisionerItems = new String[]{"Simple"};
    
    private String vmSchedular = "Time Shared Over Subscription";
    private String[] vmSchedularItems = new String[]{"Time Shared", "Space Shared", "Time Shared Over Subscription"};
    
    private String peProvisioner = "Simple";
    private String[] peProvisionerItems = new String[]{"Simple"};
    
    private List listeners = Collections.synchronizedList(new LinkedList());

    public HostAPI() {
        id++;
    }

    public Integer getId() {
        return id;
    }

    public void setHostNo(Integer hostNo) {
        Integer old = this.hostNo;
        this.hostNo = hostNo;
        fire("HostAPI.hostNo", old, this.hostNo);
    }

    public Integer getHostNo() {
        return hostNo;
    }

    public void setStorage(Integer storage) {
        Integer old = this.storage;
        this.storage = storage;
        fire("HostAPI.storage", old, this.storage);
    }

    public Integer getStorage() {
        return storage;
    }

    public void setBw(Integer bw) {
        Integer old = this.bw;
        this.bw = bw;
        fire("HostAPI.bw", old, this.bw);
    }

    public Integer getBw() {
        return bw;
    }

    public void setCpuNo(Integer cpuNo) {
        Integer old = this.cpuNo;
        this.cpuNo = cpuNo;
        fire("HostAPI.cpuNo", old, this.cpuNo);
    }

    public Integer getCpuNo() {
        return cpuNo;
    }

    public void setMips(Integer mips) {
        Integer old = this.mips;
        this.mips = mips;
        fire("HostAPI.mips", old, this.mips);
    }

    public Integer getMips() {
        return mips;
    }

    public void setRam(Integer ram) {
        Integer old = this.ram;
        this.ram = ram;
        fire("HostAPI.ram", old, this.ram);
    }

    public Integer getRam() {
        return ram;
    }

    @Override
    public void setSelectedDropDownList(String name, String newValue) {
        if (name == "vmSchedular") {
            setVmSchedular(newValue);
        } else if (name == "peProvisioner") {
            setPeProvisioner(newValue);
        }  else if(name == "ramProvisioner"){
            setRamProvisioner(newValue);
        } else if(name == "bwProvisioner"){
           setBwProvisioner(newValue);
        } else {
            System.err.println("Error setSelectedDropDownList(" + name + ", " + newValue + ")");
        }

    }

    @Override
    public String getSelectedDropDownList(String name) {
        if (name == "vmSchedular") {
            return getVmSchedular();
        } else if (name == "peProvisioner") {
            return getPeProvisioner();
        } else if(name == "ramProvisioner"){
            return getRamProvisioner();
        } else if(name == "bwProvisioner"){
           return getBwProvisioner();
        } else {
            System.err.println("Error getSelectedDropDownList(" + name + ")");
            return "Error Name";
        }

    }

    @Override
    public String[] getItems(String name) {
        if (name == "vmSchedular") {
            return vmSchedularItems;
        } else if (name == "peProvisioner") {
            return peProvisionerItems;
        } else if(name == "ramProvisioner"){
            return ramProvisionerItems;
        } else if(name == "bwProvisioner"){
           return bwProvisionerItems;
        } else {
            return new String[]{"Error Name"};
        }

    }

    public void setBwProvisioner(String bwProvisioner) {
        String old = this.bwProvisioner;
        this.bwProvisioner = bwProvisioner;
        fire("HostAPI.bwProvisioner", old, this.bwProvisioner);
    }

    public String getBwProvisioner() {
        return bwProvisioner;
    }

    public void setRamProvisioner(String ramProvisioner) {
        String old = this.ramProvisioner;
        this.ramProvisioner = ramProvisioner;
        fire("HostAPI.ramProvisioner", old, this.ramProvisioner);
    }

    public String getRamProvisioner() {
        return ramProvisioner;
    }
    
    

    public void setVmSchedular(String vmSchedular) {
        String old = this.vmSchedular;
        this.vmSchedular = vmSchedular;
        fire("HostAPI.vmSchedular", old, this.vmSchedular);
    }

    public String getVmSchedular() {
        return vmSchedular;
    }

    public void setPeProvisioner(String peProvisionerSimple) {
        String old = this.peProvisioner;
        this.peProvisioner = peProvisionerSimple;
        fire("HostAPI.peProvisionerSimple", old, this.peProvisioner);
    }

    public String getPeProvisioner() {
        return peProvisioner;
    }

    public void setHostPower(String hostPower) {
        this.hostPower = hostPower;
    }

    public String getHostPower() {
        return hostPower;
    }
    

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    private void fire(String propertyName, Object old, Object nue) {
        //Passing 0 below on purpose, so you only synchronize for one atomic call:
        PropertyChangeListener[] pcls = (PropertyChangeListener[]) listeners.toArray(new PropertyChangeListener[0]);
        for (int i = 0; i < pcls.length; i++) {
            pcls[i].propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
        }
    }
}
