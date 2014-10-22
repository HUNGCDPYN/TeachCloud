/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node.widget;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Date;
import jo.just.api.BrokerAPI;
import jo.just.api.DatacenterAPI;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zakarea Al Shara
 */
public class BrokerNode extends AbstractNode implements PropertyChangeListener {

    private int id = 0;
    private String name = "";
    BrokerAPI obj;

    public BrokerNode(BrokerAPI obj) {
        super(Children.LEAF, Lookups.singleton(obj));
        this.obj = obj;
        setDisplayName("Broker [" + obj.getId() + "]");
        this.obj.addPropertyChangeListener(WeakListeners.propertyChange(this, obj));
//        addNodeListener(new DCNodeListener());
        
    }

    public BrokerAPI getObj() {
        return obj;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.fireDisplayNameChange(null, getDisplayName());
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("jo/just/node/resources/broker16.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = sheet.createPropertiesSet();

        BrokerAPI obj = getLookup().lookup(BrokerAPI.class);

        try {

            Property name = new PropertySupport.Reflection(obj, String.class, "name");
            name.setName("Name");
            set.put(name);
            
            
            set.setPreferred(true);
            set.setShortDescription("Broker characteristics");

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);
        return sheet;

    }

    @Override
    public boolean canCopy() {
        return super.canCopy();
    }

    @Override
    public boolean canCut() {
        return super.canCut();
    }

    @Override
    public boolean canRename() {
        return super.canRename();
    }

    @Override
    public boolean canDestroy() {
//        System.err.println("super.canDestroy() = "+super.canDestroy());
//        return super.canDestroy();
        return true;
    }

 
    @Override
    public void destroy() throws IOException {
        System.err.println("destroy()");
        this.obj.removePropertyChangeListener(this);
        
        super.destroy();
//        setChildren(Children.LEAF);       
        fireNodeDestroyed();        
    }
    
    
    @Override
    public String toString() {
        return "Broker";
    }

    
}
