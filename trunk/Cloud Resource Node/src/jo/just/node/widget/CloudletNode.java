/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node.widget;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import jo.just.api.CloudletAPI;
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
public class CloudletNode extends AbstractNode implements PropertyChangeListener {
    
    CloudletAPI obj;

    public CloudletNode() {
       super(Children.LEAF);
    }
    
    public CloudletNode(CloudletAPI obj) {
        super(Children.LEAF, Lookups.singleton(obj));
        this.obj = obj;
        setDisplayName("Cloudlet[" + obj.getId()+"]");
        this.obj.addPropertyChangeListener(WeakListeners.propertyChange(this, obj));
    }

//    public HostNode(Children children, Lookup lookup) {
//        super(children, lookup);
//    }

    public CloudletAPI getObj() {
        return obj;
    }

    
    
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.fireDisplayNameChange(null, getDisplayName());
    }
    
    
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("jo/just/node/resources/cloudlet16.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }


    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = sheet.createPropertiesSet();
        
        CloudletAPI obj = getLookup().lookup(CloudletAPI.class);

        try {

            Property id = new PropertySupport.Reflection(obj, Integer.class, "getId", null);
            id.setName("Id");
            set.put(id);
            
            Property cloudletNo = new PropertySupport.Reflection(obj, Integer.class, "cloudletNo");
            cloudletNo.setName("Cloudlet No");
            set.put(cloudletNo);
            
            DropDownList type = new DropDownList(obj, "type", "Type", "", obj.getItems("type"));
            set.put(type);
            
//            Property path = new PropertySupport.Reflection(obj, String.class, "path");
//            cloudletNo.setName("Path");
//            set.put(path);
            
            FileExplorer path = new FileExplorer(obj, "path", "Path", "");
            set.put(path);
            
            Property cloudletLength = new PropertySupport.Reflection(obj, Long.class, "cloudletLength");
            cloudletLength.setName("Cloudlet Length (MI)");
            set.put(cloudletLength);
            
            Property cpuNo = new PropertySupport.Reflection(obj, Integer.class, "cpuNo");
            cpuNo.setName("CPU Number");
            set.put(cpuNo);
            
            Property cloudletFileSize = new PropertySupport.Reflection(obj, Long.class, "cloudletFileSize");
            cloudletFileSize.setName("Cloudlet File Size (Byte)");
            set.put(cloudletFileSize);
            
            Property cloudletOutputSize = new PropertySupport.Reflection(obj, Long.class, "cloudletOutputSize");
            cloudletOutputSize.setName("Cloudlet Output Size (Byte)");
            set.put(cloudletOutputSize);
            
            Property memory = new PropertySupport.Reflection(obj, Integer.class, "memory");
            memory.setName("Memory");
            set.put(memory);
   
            DropDownList utilizationModelCpu = new DropDownList(obj, "utilizationModelCpu", "CPU Utilization Model", "", obj.getItems("utilizationModelCpu"));
            set.put(utilizationModelCpu);

            DropDownList utilizationModelRam = new DropDownList(obj, "utilizationModelRam", "RAM Utilization Model", "", obj.getItems("utilizationModelRam"));
            set.put(utilizationModelRam);
            
            DropDownList utilizationModelBw = new DropDownList(obj, "utilizationModelBw", "BW Utilization Model", "", obj.getItems("utilizationModelBw"));
            set.put(utilizationModelBw);
           

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
