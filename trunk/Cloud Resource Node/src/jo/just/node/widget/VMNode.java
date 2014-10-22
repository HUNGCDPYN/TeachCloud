/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node.widget;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import jo.just.api.VMAPI;
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
public class VMNode extends AbstractNode implements PropertyChangeListener {
    
    VMAPI obj;

    public VMNode() {
       super(Children.LEAF);
    }

    
    
    public VMNode(VMAPI obj) {
        super(Children.LEAF, Lookups.singleton(obj));
        this.obj = obj;
        setDisplayName("VM[" + obj.getId()+"]");
        obj.addPropertyChangeListener(WeakListeners.propertyChange(this, obj));
    }

//    public VMNode(Children children, Lookup lookup) {
//        super(children, lookup);
//    }

    public VMAPI getObj() {
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


    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = sheet.createPropertiesSet();
        
        VMAPI obj = getLookup().lookup(VMAPI.class);

        try {

            Property id = new PropertySupport.Reflection(obj, Integer.class, "getId", null);
            id.setName("Id");
            set.put(id);
            
            Property VMNo = new PropertySupport.Reflection(obj, Integer.class, "VMNo");
            VMNo.setName("VM Number");
            set.put(VMNo);
            
            Property userId = new PropertySupport.Reflection(obj, Integer.class, "userId");
            userId.setName("User Id");
            set.put(userId);
            
            Property mips = new PropertySupport.Reflection(obj, Integer.class, "mips");
            mips.setName("MIPS");
            set.put(mips);
            
            Property cpuNo = new PropertySupport.Reflection(obj, Integer.class, "cpuNo");
            cpuNo.setName("CPU Number");
            set.put(cpuNo);
            
            Property ram = new PropertySupport.Reflection(obj, Integer.class, "ram");
            ram.setName("RAM");
            set.put(ram);
            
            Property bw = new PropertySupport.Reflection(obj, Integer.class, "bw");
            bw.setName("Bandwidth");
            set.put(bw);
            
            Property size = new PropertySupport.Reflection(obj, Long.class, "size");
            size.setName("Image Size");
            set.put(size);
            
            Property storage = new PropertySupport.Reflection(obj, Integer.class, "storage");
            storage.setName("Storage");
            set.put(storage);
            
            Property vmm = new PropertySupport.Reflection(obj, String.class, "getVmm", null);
            vmm.setName("VMM");
            set.put(vmm);
            
            DropDownList taskSchedular = new DropDownList(obj, "taskSchedular", "Task Schedular", "", obj.getItems("taskSchedular"));
            set.put(taskSchedular);

        

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);
        return sheet;

    }
}
