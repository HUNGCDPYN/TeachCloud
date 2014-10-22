/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.Action;
import jo.just.api.DatacenterAPI;
import org.openide.ErrorManager;
import org.openide.actions.DeleteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zakarea Al Shara
 */
public class DatacenterNode extends AbstractNode implements PropertyChangeListener {

    private int id = 0;
    private String name = "";
    DatacenterAPI obj;

    public DatacenterNode(DatacenterAPI obj) {
        super(Children.create(new DatacenterChildFactory(), true), Lookups.singleton(obj));
        this.obj = obj;
        setDisplayName("Datacenter [" + obj.getId() + "]");
        this.obj.addPropertyChangeListener(WeakListeners.propertyChange(this, obj));
//        addNodeListener(new DCNodeListener());
        
    }

    public DatacenterAPI getObj() {
        return obj;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        System.err.println("evt >>>" + evt);
//        System.err.println("evt name >>>" + evt.getPropertyName());
//        System.err.println("evt old >>>" + evt.getOldValue());
//        System.err.println("evt new >>>" + evt.getNewValue());
//        System.err.println("");
        this.fireDisplayNameChange(null, getDisplayName());
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("jo/just/node/resources/datacenter16.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set resorce = sheet.createPropertiesSet();
        Sheet.Set network = sheet.createPropertiesSet();
        network.setDisplayName("Network");
        network.setName("Network");

        DatacenterAPI obj = getLookup().lookup(DatacenterAPI.class);

        try {

            Property name = new PropertySupport.Reflection(obj, String.class, "name");
            name.setName("Name");
            resorce.put(name);

            Property arch = new PropertySupport.Reflection(obj, String.class, "getArch", null);
            arch.setName("Arch");
            resorce.put(arch);

            Property os = new PropertySupport.Reflection(obj, String.class, "getOs", null);
            os.setName("OS");
            resorce.put(os);

            Property vmm = new PropertySupport.Reflection(obj, String.class, "getVmm", null);
            vmm.setName("VMM");
            resorce.put(vmm);

            Property time = new PropertySupport.Reflection(obj, Date.class, "time");
            time.setName("Time");
            resorce.put(time);

            Property costPerSecond = new PropertySupport.Reflection(obj, Double.class, "costPerSecond");
            costPerSecond.setName("Cost Per Second");
            resorce.put(costPerSecond);

            Property costPerMemory = new PropertySupport.Reflection(obj, Double.class, "costPerMemory");
            costPerMemory.setName("Cost Per Memory");
            resorce.put(costPerMemory);

            Property costPerStorage = new PropertySupport.Reflection(obj, Double.class, "costPerStorage");
            costPerStorage.setName("Cost Per Storage");
            resorce.put(costPerStorage);

            Property costPerBandwidth = new PropertySupport.Reflection(obj, Double.class, "costPerBandwidth");
            costPerBandwidth.setName("Cost Per Bandwidth");
            resorce.put(costPerBandwidth);

            Property vmAllocationPolicy = new PropertySupport.Reflection(obj, String.class, "vmAllocationPolicy");
            vmAllocationPolicy.setName("VM Allocation Policy");
            resorce.put(vmAllocationPolicy);

            Property networkTopology = new PropertySupport.Reflection(obj, String.class, "networkTopology");
            networkTopology.setName("Netwok Topology");
            network.put(networkTopology);

            Property rootSwitchDelay = new PropertySupport.Reflection(obj, Integer.class, "rootSwitchDelay");
            rootSwitchDelay.setName("Root Switch Delay");
            network.put(rootSwitchDelay);

            Property rootSwitchBandwidth = new PropertySupport.Reflection(obj, Integer.class, "rootSwitchBandwidth");
            rootSwitchBandwidth.setName("Root Switch Bandwidth");
            network.put(rootSwitchBandwidth);

            Property aggregateSwitchDelay = new PropertySupport.Reflection(obj, Integer.class, "aggregateSwitchDelay");
            aggregateSwitchDelay.setName("Aggregate Switch Delay");
            network.put(aggregateSwitchDelay);

            Property aggregateSwitchBandwidth = new PropertySupport.Reflection(obj, Integer.class, "aggregateSwitchBandwidth");
            aggregateSwitchBandwidth.setName("Aggregate Switch Bandwidth");
            network.put(aggregateSwitchBandwidth);

            Property edgeSwitchDelay = new PropertySupport.Reflection(obj, Integer.class, "edgeSwitchDelay");
            edgeSwitchDelay.setName("Edge Switch Delay");
            network.put(edgeSwitchDelay);

            Property edgeSwitchBandwidth = new PropertySupport.Reflection(obj, Integer.class, "edgeSwitchBandwidth");
            edgeSwitchBandwidth.setName("edgeSwitchBandwidth");
            network.put(edgeSwitchBandwidth);

            resorce.setValue("tabName", "Datacenter");
            resorce.setPreferred(true);
            resorce.setShortDescription("Datacenter resource characteristics");

            network.setValue("tabName", "Network");
            network.setPreferred(true);
            network.setShortDescription("Datacenter network topology and characteristics");

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(resorce);
        sheet.put(network);
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
        return "DataCenter";
    }

    private class DCNodeListener implements NodeListener {

        @Override
        public void childrenAdded(NodeMemberEvent nme) {
//            System.err.println("childrenAdded > " + nme);
        }

        @Override
        public void childrenRemoved(NodeMemberEvent nme) {
//            System.err.println("childrenRemoved > " + nme);
        }

        @Override
        public void childrenReordered(NodeReorderEvent nre) {
//            System.err.println("childrenReordered > " + nre);
        }

        @Override
        public void nodeDestroyed(NodeEvent ne) {
//            System.err.println("nodeDestroyed > " + ne);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
//            System.err.println("propertyChange > " + evt);
//            System.err.println("evt >>>" + evt);
//            System.err.println("evt name >>>" + evt.getPropertyName());
//            System.err.println("evt old >>>" + evt.getOldValue());
//            System.err.println("evt new >>>" + evt.getNewValue());
//            System.err.println("");
            
        }
    }
}
