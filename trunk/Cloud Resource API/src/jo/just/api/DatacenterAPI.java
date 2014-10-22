/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Zakarea Al Shara
 */
public class DatacenterAPI implements DropDownInterface, DateInterface{
    
    private static int id = -1;
    private String name;
    private String arch = "X86";
    private String os = "Linux";
    private String vmm = "Xen";
    private Date time = new Date();
    private Double costPerSecond = 3.0;
    private Double costPerMemory = 0.05;
    private Double costPerStorage =  0.001;
    private Double costPerBandwidth = 0.0;
//    private String vmAllocationPolicy = "Simple";
    
    private String networkTopology = "VL2";
    private String[] networkTopologyItems = new String[]{"VL2", "BCube", "PortLand", "DCell"};
    private Integer rootSwitchDelay = 0;
    private Integer rootSwitchBandwidth = 0;
    private Integer aggregateSwitchDelay = 0;
    private Integer aggregateSwitchBandwidth = 0;
    private Integer edgeSwitchDelay = 0;
    private Integer edgeSwitchBandwidth = 0;
    
    private String vmAllocationPolicy = "IQR";
    private String[] vmAllocationPolicyItems = new String[]{"DVFS", "IQR", "LR", "MAD", "THR", "Non"};
    private String vmSelectionPolicy = "MMT";
    private String[] vmSelectionPolicyItems = new String[]{"MC", "MMT", "MU", "RS", "Non"};
    private Double threshold = 1.5;
    
    private List listeners = Collections.synchronizedList(new LinkedList());

    public DatacenterAPI() {
        id++;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        fire("DatacenterAPI.name", old, this.name);
    }

    public int getId() {
        return id;
    }
    

    public String getName() {
        if(name == null){
            setName("Datacenter["+id+"]");
        }
        return name;
    }

    public String getArch() {
        return arch;
    }

    public String getOs() {
        return os;
    }

    public String getVmm() {
        return vmm;
    }

    public void setTime(Date time) {
        Date old = this.time;
        this.time = time;
        fire("DatacenterAPI.time", old, this.time);
    }

    public Date getTime() {
        return time;
    }

    public void setCostPerBandwidth(Double costPerBandwidth) {
        Double old = this.costPerBandwidth;
        this.costPerBandwidth = costPerBandwidth;
        fire("DatacenterAPI.costPerBandwidth", old, this.costPerBandwidth);
    }

    public Double getCostPerBandwidth() {
        return costPerBandwidth;
    }

    public void setCostPerMemory(Double costPerMemory) {
        Double old = this.costPerMemory;
        this.costPerMemory = costPerMemory;
        fire("DatacenterAPI.costPerMemory", old, this.costPerMemory);
    }

    public Double getCostPerMemory() {
        return costPerMemory;
    }

    public void setCostPerSecond(Double costPerSecond) {
        Double old = this.costPerSecond;
        this.costPerSecond = costPerSecond;
        fire("DatacenterAPI.costPerSecond", old, this.costPerSecond);
    }

    public Double getCostPerSecond() {
        return costPerSecond;
    }

    public void setCostPerStorage(Double costPerStorage) {
        Double old = costPerStorage;
        this.costPerStorage = costPerStorage;
        fire("DatacenterAPI.costPerStorage", old, this.costPerStorage);
    }

    public Double getCostPerStorage() {
        return costPerStorage;
    }

    public void setVmAllocationPolicy(String vmAllocationPolicy) {
        String old = this.vmAllocationPolicy;
        this.vmAllocationPolicy = vmAllocationPolicy;
        fire("DatacenterAPI.vmAllocationPolicy", old, this.vmAllocationPolicy);
    }

    public String getVmAllocationPolicy() {
        return vmAllocationPolicy;
    }

    public void setVmSelectionPolicy(String vmSelectionPolicy) {
        String old = this.vmSelectionPolicy;
        this.vmSelectionPolicy = vmSelectionPolicy;
        fire("DatacenterAPI.vmSelectionPolicy", old, this.vmSelectionPolicy);
    }

    public String getVmSelectionPolicy() {
        return vmSelectionPolicy;
    }

    public void setThreshold(Double threshold) {
        Double old = threshold;
        this.threshold = threshold;
        fire("DatacenterAPI.threshold", old, this.threshold);    
    }

    public Double getThreshold() {
        return threshold;
    }
    

    public void setRootSwitchDelay(Integer rootSwitchDelay) {
        Integer old = this.rootSwitchDelay;
        this.rootSwitchDelay = rootSwitchDelay;
        fire("DatacenterAPI.rootSwitchDelay", old, this.rootSwitchDelay);
    }

    public Integer getRootSwitchDelay() {
        return rootSwitchDelay;
    }

    public void setRootSwitchBandwidth(Integer rootSwitchBandwidth) {
        Integer old = this.rootSwitchBandwidth;
        this.rootSwitchBandwidth = rootSwitchBandwidth;
        fire("DatacenterAPI.rootSwitchBandwidth", old, this.rootSwitchBandwidth);
    }

    public Integer getRootSwitchBandwidth() {
        return rootSwitchBandwidth;
    }

    public void setAggregateSwitchBandwidth(Integer aggregateSwitchBandwidth) {
        Integer old = this.aggregateSwitchBandwidth;
        this.aggregateSwitchBandwidth = aggregateSwitchBandwidth;
        fire("DatacenterAPI.aggregateSwitchBandwidth", old, this.aggregateSwitchBandwidth);
    }

    public Integer getAggregateSwitchBandwidth() {
        return aggregateSwitchBandwidth;
    }

    public void setAggregateSwitchDelay(Integer aggregateSwitchDelay) {
        Integer old = this.aggregateSwitchDelay;
        this.aggregateSwitchDelay = aggregateSwitchDelay;
        fire("DatacenterAPI.aggregateSwitchDelay", old, this.aggregateSwitchDelay);
    }

    public Integer getAggregateSwitchDelay() {
        return aggregateSwitchDelay;
    }

    public void setEdgeSwitchBandwidth(Integer edgeSwitchBandwidth) {
        Integer old = this.edgeSwitchBandwidth;
        this.edgeSwitchBandwidth = edgeSwitchBandwidth;
        fire("DatacenterAPI.edgeSwitchBandwidth", old, this.edgeSwitchBandwidth);
    }

    public Integer getEdgeSwitchBandwidth() {
        return edgeSwitchBandwidth;
    }

    public void setEdgeSwitchDelay(Integer edgeSwitchDelay) {
        Integer old = this.edgeSwitchDelay;
        this.edgeSwitchDelay = edgeSwitchDelay;
        fire("DatacenterAPI.edgeSwitchDelay", old, this.edgeSwitchDelay);
    }

    public Integer getEdgeSwitchDelay() {
        return edgeSwitchDelay;
    }
    
    @Override
    public void setSelectedDropDownList(String name, String newValue) {
        if(name.equals("networkTopology")){
            setNetworkTopology(newValue);
        }else if(name.equals("vmAllocationPolicy")){
            setVmAllocationPolicy(newValue);
        }else if(name.equals("vmSelectionPolicy")){
            setVmSelectionPolicy(newValue);
        }
    }

    @Override
    public String getSelectedDropDownList(String name) {
        if(name.equals("networkTopology")){
            return getNetworkTopology();
        }else if(name.equals("vmAllocationPolicy")){
            return getVmAllocationPolicy();
        }else if(name.equals("vmSelectionPolicy")){
            return getVmSelectionPolicy();
        }else{
            System.err.println("Error getSelectedDropDownList("+name+")");
           return "Error Name";
        }
        
    }
    
     @Override
    public String[] getItems(String name) {
        if(name.equals("networkTopology")){
            return networkTopologyItems;
        }else if(name.equals("vmAllocationPolicy")){
            return vmAllocationPolicyItems;
        }else if(name.equals("vmSelectionPolicy")){
            return vmSelectionPolicyItems;
        }else{
            return new String[]{"Error Name"};
        }
    }

    public void setNetworkTopology(String networkTopology) {
        String old = this.networkTopology;
        this.networkTopology = networkTopology;
        fire("DatacenterAPI.networkTopology", old, this.networkTopology);
    }

    public String getNetworkTopology() {
        return networkTopology;
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
    public void setSelectedDate(String Name, Date newValue) {
        if(name =="time"){
            setTime(newValue);
        }
        
    }

    @Override
    public Date getSelectedDate(String Name) {
        if(name == "time"){
            return getTime();
        }else{
            return new Date();
        }
        
    }
    
}
