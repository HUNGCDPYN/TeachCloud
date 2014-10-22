/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import jo.just.api.HostsAPI;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;


/**
 *
 * @author Zakarea Al Shara
 */
public class HostNodes extends AbstractNode implements PropertyChangeListener {
    
    HostsAPI obj;

    public HostNodes() {
       super(Children.create(new HostChildFactory(), true));
    }
    
    public HostNodes(HostsAPI obj) {
        super(Children.create(new HostChildFactory(), true), Lookups.singleton(obj));
        this.obj = obj;
        setDisplayName("Hosts");
        obj.addPropertyChangeListener(WeakListeners.propertyChange(this, obj));
    }

//    public HostNode(Children children, Lookup lookup) {
//        super(children, lookup);
//    }

    public HostsAPI getObj() {
        return obj;
    }

    
    
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.fireDisplayNameChange(null, getDisplayName());
    }
    
    
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("jo/just/node/resources/host16.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

}
