/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import jo.just.api.HostAPI;
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
public class HostNode extends AbstractNode implements PropertyChangeListener {
    
    HostAPI obj;

    public HostNode() {
       super(Children.LEAF);
    }
    
    public HostNode(HostAPI obj) {
        super(Children.LEAF, Lookups.singleton(obj));
        this.obj = obj;
        setDisplayName("Host[" + obj.getId()+"]");
        this.obj.addPropertyChangeListener(WeakListeners.propertyChange(this, obj));
    }

//    public HostNode(Children children, Lookup lookup) {
//        super(children, lookup);
//    }

    public HostAPI getObj() {
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


    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set host = sheet.createPropertiesSet();
        
        HostAPI obj = getLookup().lookup(HostAPI.class);

        try {

            Property id = new PropertySupport.Reflection(obj, Integer.class, "getId", null);
            id.setName("Id");
            host.put(id);
            
            Property storage = new PropertySupport.Reflection(obj, Integer.class, "storage");
            storage.setName("Storage");
            host.put(storage);
            
            Property cpuNo = new PropertySupport.Reflection(obj, Integer.class, "cpuNo");
            cpuNo.setName("CPU Number");
            host.put(cpuNo);
            
            Property mips = new PropertySupport.Reflection(obj, Integer.class, "mips");
            mips.setName("MIPS");
            host.put(mips);
            
            Property ram = new PropertySupport.Reflection(obj, Integer.class, "ram");
            id.setName("RAM");
            host.put(ram);
            
            Property bw = new PropertySupport.Reflection(obj, Integer.class, "bw");
            id.setName("Bandwidth");
            host.put(bw);
            
            Property vmSchedular = new PropertySupport.Reflection(obj, String.class, "vmSchedular");
            vmSchedular.setName("VM Schedular");
            host.put(vmSchedular);

           

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(host);
        
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
