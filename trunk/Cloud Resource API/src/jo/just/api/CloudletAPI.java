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
public class CloudletAPI implements DropDownInterface{

    private static Integer id = -1;
    private Integer cloudletNo = 50;//-1 = all
    private String type = "Cloudlet";
    private String[] typeItems = new String[]{"Cloudlet", "Mapper", "Reducer", "Rain","MapReduce", "SWF"};
    private String path = "";
    private Long cloudletLength = 2500 * 24 * 60 * 60L;//the length or size (in MI) of this cloudlet to be executed in a PowerDatacenter
    private Integer cpuNo = 1;
    private Long cloudletFileSize = 300L;
    private Long cloudletOutputSize = 300L;
    private Integer memory = 1024;
    private String utilizationModelCpu = "UtilizationModelFull";//ToDo make it drop down list 
    private String utilizationModelRam = "UtilizationModelFull";
    private String utilizationModelBw = "UtilizationModelFull";
    private String[] utilizationModelCpuItems = new String[]{"UtilizationModelFull"};
    private String[] utilizationModelRamItems = new String[]{"UtilizationModelFull"};
    private String[] utilizationModelBwItems = new String[]{"UtilizationModelFull"};
    
    public static final int TYPE_CPU = 0;
    public static final int TYPE_RAM = 1;
    public static final int TYPE_BW = 2;
    
    private List listeners = Collections.synchronizedList(new LinkedList());

    public CloudletAPI() {
        id++;
    }

    public Integer getId() {
        return id;
    }

    public void setCloudletNo(Integer cloudletNo) {
        Integer old = this.cloudletNo;
        this.cloudletNo = cloudletNo;
        fire("CloudletAPI.cloudletNo", old, this.cloudletNo);
    }

    public Integer getCloudletNo() {
        return cloudletNo;
    }

    public void setType(String type) {
        String old = this.type;
        this.type = type;
        fire("CloudletAPI.type", old, this.type);
        
    }

    public String getType() {
        return type;
    }

    public void setPath(String path) {
        String old = this.path;
        this.path = path;
        fire("CloudletAPI.path", old, this.path); 
    }

    public String getPath() {
        return path;
    }
    
    

    public void setCloudletLength(Long cloudletLength) {
        Long old = this.cloudletLength;
        this.cloudletLength = cloudletLength;
        fire("CloudletAPI.cloudletLength", old, this.cloudletLength);
    }

    public Long getCloudletLength() {
        return cloudletLength;
    }
  
    public void setCpuNo(Integer cpuNo) {
        Integer old = this.cpuNo;
        this.cpuNo = cpuNo;
        fire("CloudletAPI.cpuNo", old, this.cpuNo);
    }

    public Integer getCpuNo() {
        return cpuNo;
    }

    public void setCloudletFileSize(Long cloudletFileSize) {
        Long old = this.cloudletFileSize;
        this.cloudletFileSize = cloudletFileSize;
        fire("CloudletAPI.cloudletFileSize", old, this.cloudletFileSize);
    }

    public Long getCloudletFileSize() {
        return cloudletFileSize;
    }

    public void setCloudletOutputSize(Long cloudletOutputSize) {
        Long old = this.cloudletOutputSize;
        this.cloudletOutputSize = cloudletOutputSize;
        fire("CloudletAPI.cloudletOutputSize", old, this.cloudletOutputSize);
    }

    public Long getCloudletOutputSize() {
        return cloudletOutputSize;
    }

    public void setMemory(Integer memory) {
        Integer old = this.memory;
        this.memory = memory;
        fire("CloudletAPI.memory", old, this.memory); 
    }

    public Integer getMemory() {
        return memory;
    }
    

    public void setUtilizationModelBw(String utilizationModelBw) {
        String old = this.utilizationModelBw;
        this.utilizationModelBw = utilizationModelBw;
        fire("CloudletAPI.utilizationModelBw", old, this.utilizationModelBw);
    }

    public String getUtilizationModelBw() {
        return utilizationModelBw;
    }

    public void setUtilizationModelCpu(String utilizationModelCpu) {
        String old = this.utilizationModelCpu;
        this.utilizationModelCpu = utilizationModelCpu;
        fire("CloudletAPI.utilizationModelCpu", old, this.utilizationModelCpu);
    }

    public String getUtilizationModelCpu() {
        return utilizationModelCpu;
    }

    public void setUtilizationModelRam(String utilizationModelRam) {
        String old = this.utilizationModelRam;
        this.utilizationModelRam = utilizationModelRam;
        fire("CloudletAPI.utilizationModelRam", old, this.utilizationModelRam);
    }

    public String getUtilizationModelRam() {
        return utilizationModelRam;
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

    @Override
    public void setSelectedDropDownList(String name, String newValue) {
        if("type".equals(name)){
            setType(newValue);
        }else if("utilizationModelCpu".equals(name)){
           setUtilizationModelCpu(newValue);
       }else if("utilizationModelRam".equals(name)){
           setUtilizationModelRam(newValue);
       }else if("utilizationModelBw".equals(name)){
           setUtilizationModelBw(newValue);
       }else if("path".equals(name)){
           setPath(newValue);
       }else{
            System.err.println("Error setSelectedDropDownList("+name+", "+newValue+")");
       }
    }

    @Override
    public String getSelectedDropDownList(String name) {
        if("type".equals(name)){
            return getType();
        }else if("utilizationModelCpu".equals(name)){
           return getUtilizationModelCpu();
       }else if("utilizationModelRam".equals(name)){
           return getUtilizationModelRam();
       }else if("utilizationModelBw".equals(name)){
           return getUtilizationModelBw();
       }else if("path".equals(name)){
           return getPath();
       }else{
           System.err.println("Error getSelectedDropDownList("+name+")");
           return "Error Name";
       }
    }

    @Override
    public String[] getItems(String name) {
        if("type".equals(name)){
            return typeItems;
        }else if("utilizationModelCpu".equals(name)){
           return utilizationModelCpuItems;
       }else if("utilizationModelRam".equals(name)){
           return utilizationModelRamItems;
       }else if("utilizationModelBw".equals(name)){
           return utilizationModelBwItems;
       }else{
           return new String[]{"Error Name"};
       }
         
    }
}
