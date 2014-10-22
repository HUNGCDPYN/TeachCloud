/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import jo.just.api.VMsAPI;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zakarea Al Shara
 */
public class VMNodes extends AbstractNode implements PropertyChangeListener {
    
    VMsAPI obj;
    private int id = 0;
    private String name = "";

    public VMNodes() {
       super(Children.create(new VMChildFactory(), true));
    }

    
    
    public VMNodes(VMsAPI obj) {
        super(Children.create(new VMChildFactory(), true), Lookups.singleton(obj));
        this.obj = obj;
        setDisplayName("VMs");
        obj.addPropertyChangeListener(WeakListeners.propertyChange(this, obj));
    }

//    public VMNode(Children children, Lookup lookup) {
//        super(children, lookup);
//    }
   
    public VMsAPI getObj() {
        return obj;
    }
    
    
    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.fireDisplayNameChange(null, getDisplayName());
    }
    
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("jo/just/node/resources/vm16.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }
}
