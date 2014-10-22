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
public class VMAPI implements DropDownInterface{

    private static Integer id = -1;
    private Integer userId = 0;
    private Integer VMNo = 50;
    private Integer mips = 2000;
    private Integer cpuNo = 1;
    private Integer ram = 1740;
    private Integer bw = 100000;
    private Long size = 2500L;
    private Integer storage = 1000000;
    private String vmm = "Xen";
    private Integer priority = 1;
    private Double schedulingInterval = 300.0;//zax add to properties
    private String taskSchedular = "Dynamic Workload";
    private String[] taskSchedularItems = new String[]{"Time Shared", "Space Shared", "Dynamic Workload", "MapReduce"};
    
    private List listeners = Collections.synchronizedList(new LinkedList());

    public VMAPI() {
        id++;
    }

    public Integer getId() {
        return id;
    }

    public void setVMNo(Integer VMNo) {
        Integer old = this.VMNo;
        this.VMNo = VMNo;
        fire("VMAPI.VMNo", old, this.VMNo);
    }

    public Integer getVMNo() {
        return VMNo;
    }
    

    public void setBw(Integer bw) {
        Integer old = this.bw;
        this.bw = bw;
        fire("VMAPI.cpuNo", old, this.bw);
    }

    public Integer getBw() {
        return bw;
    }

    public void setCpuNo(Integer cpuNo) {
        Integer old = this.cpuNo;
        this.cpuNo = cpuNo;
        fire("VMAPI.cpuNo", old, this.cpuNo);
    }

    public Integer getCpuNo() {
        return cpuNo;
    }

    public void setMips(Integer mips) {
        Integer old = this.mips;
        this.mips = mips;
        fire("VMAPI.mips", old, this.mips);
    }

    public Integer getMips() {
        return mips;
    }

    public void setRam(Integer ram) {
        Integer old = this.ram;
        this.ram = ram;
        fire("VMAPI.ram", old, this.ram);
    }

    public Integer getRam() {
        return ram;
    }

    public void setSize(Long size) {
        Long old = this.size;
        this.size = size;
        fire("VMAPI.size", old, this.size);
    }

    public Long getSize() {
        return size;
    }
    

    public void setStorage(Integer storage) {
        Integer old = this.storage;
        this.storage = storage;
        fire("VMAPI.storage", old, this.storage);
    }

    public Integer getStorage() {
        return storage;
    }
    
    @Override
    public void setSelectedDropDownList(String Name, String newValue) {
        setTaskSchedular(newValue);
    }

    @Override
    public String getSelectedDropDownList(String Name) {
        return getTaskSchedular();
    }

    @Override
    public String[] getItems(String Name) {
        return taskSchedularItems;
    }

    public void setTaskSchedular(String taskSchedular) {
        String old = this.taskSchedular;
        this.taskSchedular = taskSchedular;
        fire("VMAPI.taskSchedular", old, this.taskSchedular);
    }

    public String getTaskSchedular() {
        return taskSchedular;
    }

    public void setUserId(Integer userId) {
        Integer old = this.userId;
        this.userId = userId;
        fire("VMAPI.userId", old, this.userId);
    }

    public Integer getUserId() {
        return userId;
    }

//    public void setVmm(String vmm) {
//        String old = this.vmm;
//        this.vmm = vmm;
//        fire("HostAPI.vmm", old, this.vmm);
//    }

    public String getVmm() {
        return vmm;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setSchedulingInterval(Double schedulingInterval) {
        this.schedulingInterval = schedulingInterval;
    }

    public Double getSchedulingInterval() {
        return schedulingInterval;
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
