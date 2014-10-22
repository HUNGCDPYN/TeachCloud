/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.JComboBox;
import jo.just.api.CloudComputingAPI;
import jo.just.node.widget.DropDownList;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zakarea Al Shara
 */
public class CloudComputingNode extends AbstractNode implements PropertyChangeListener {

    public CloudComputingNode() {
       super(Children.create(new CloudComputingChildFactory(), true));
    }
    
    public CloudComputingNode(CloudComputingAPI obj) {
        super(Children.create(new CloudComputingChildFactory(), true), Lookups.singleton(obj));
        setDisplayName("CloudComputing");
        obj.addPropertyChangeListener(WeakListeners.propertyChange(this, obj));
    }

   
    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("User Number".equals(evt.getPropertyName())) {
            this.fireDisplayNameChange(null, getDisplayName());
        }
    }
    
    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("jo/just/node/resources/cloud_closed16.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return ImageUtilities.loadImage("jo/just/node/resources/cloud_opened16.png");
    }
    


//    @Override
//    protected Sheet createSheet() {
//
//        Sheet sheet = Sheet.createDefault();
//        Sheet.Set set = sheet.createPropertiesSet();
//        
//        
//        CloudComputingAPI obj = getLookup().lookup(CloudComputingAPI.class);
//
//        try {
//
//            Property userNo = new PropertySupport.Reflection(obj, Integer.class, "userNo");
//            userNo.setName("User Number");
//            set.put(userNo);
//
//            DropDownList workload = new DropDownList(obj, "workload", "Workload Type", "", obj.getItems("workload"));
//            set.put(workload);
//            
//            Property taskNo = new PropertySupport.Reflection(obj, Integer.class, "taskNo");
//            taskNo.setName("Tasks Number");
//            set.put(taskNo);
//            
//        } catch (NoSuchMethodException ex) {
//            ErrorManager.getDefault();
//        }
//
//        sheet.put(set);
//        return sheet;
//
//    }
    
}
