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
public class BrokerAPI {

    private static int id = -1;
    private String name;
    private List listeners = Collections.synchronizedList(new LinkedList());

    public BrokerAPI() {
        id++;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        fire("BrokerAPI.name", old, this.name);
    }

    public String getName() {
        if (name == null) {
            setName("Broker[" + id+"]");
        }
        return name;
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
