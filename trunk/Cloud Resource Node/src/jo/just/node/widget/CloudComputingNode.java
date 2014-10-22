/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node.widget;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import jo.just.api.CloudComputingAPI;
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
public class CloudComputingNode extends AbstractNode implements PropertyChangeListener {

    CloudComputingAPI obj;
    
    public CloudComputingNode() {
       super(Children.LEAF);
    }
    
    public CloudComputingNode(CloudComputingAPI obj) {
        super(Children.LEAF, Lookups.singleton(obj));
        this.obj = obj;
        setDisplayName("CloudComputing");
        this.obj.addPropertyChangeListener(WeakListeners.propertyChange(this, obj));
    }

   
    public CloudComputingAPI getObj() {
        return obj;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
            this.fireDisplayNameChange(null, getDisplayName());
        
    }
    
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("jo/just/node/resources/cloud_closed16.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return ImageUtilities.loadImage("jo/just/node/resources/cloud_opened16.png");
    }
    


    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = sheet.createPropertiesSet();
        
        
        CloudComputingAPI obj = getLookup().lookup(CloudComputingAPI.class);

        try {

            Property userNo = new PropertySupport.Reflection(obj, Integer.class, "userNo");
            userNo.setName("User Number");
            set.put(userNo);
            
        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);
        return sheet;

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
        super.destroy();
//        setChildren(Children.LEAF);       
        fireNodeDestroyed();        
    }
    
}
