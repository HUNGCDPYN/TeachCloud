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
import javax.swing.JComboBox;
import javax.swing.JTextField;
/**
 *
 * @author Zakarea Al Shara
 */
public class CloudComputingAPI /*implements DropDownInterface*/{
    
     private List listeners = Collections.synchronizedList(new LinkedList());
     private Integer userNo = 1;
//     private Integer taskNo = -1;
//     private String workload;
//     private String[] workloadItems = new String[]{"Rain","MapReduce", "Random", "Static"};
     

    public CloudComputingAPI() {
        
    }

    public void setUserNo(Integer userNo) {
        Integer old = this.userNo;
        this.userNo = userNo;
        fire("CloudComputingAPI.userNo", old, this.userNo);
    }

    public Integer getUserNo() {
        return userNo;
    }
//    
//    @Override
//    public void setSelectedDropDownList(String Name, String newValue) {
//        setWorkload(newValue);
//    }
//
//    @Override
//    public String getSelectedDropDownList(String Name) {
//        return getWorkload();
//    }
//
//    public void setWorkload(String workload) {
//        String old = this.workload;
//        this.workload = workload;
//        fire("CloudComputingAPI.workload", old, this.workload);
//    }
//
//    public String getWorkload() {
//        return workload;
//    }
//
//    public void setTaskNo(Integer taskNo) {
//        Integer old = this.taskNo;
//        this.taskNo = taskNo;
//        fire("CloudComputingAPI.taskNo", old, this.taskNo);
//    }
//
//    public Integer getTaskNo() {
//        return taskNo;
//    }
//
//    @Override
//    public String[] getItems(String Name) {
//        return workloadItems;
//    }
//    
    
    
    
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
