/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.List;
import jo.just.api.AbstractDatacenterAPI;
import jo.just.api.DatacenterAPI;
import jo.just.api.HostAPI;
import jo.just.api.HostsAPI;
import jo.just.api.VMAPI;
import jo.just.api.VMsAPI;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zakarea Al Shara
 */
public class DatacenterChildFactory extends ChildFactory<AbstractDatacenterAPI> implements NodeListener {

    List<AbstractDatacenterAPI> toPopulate;
    
    @Override
    protected boolean createKeys(List<AbstractDatacenterAPI> toPopulate) {
        this.toPopulate = toPopulate;
        HostsAPI[] host = new HostsAPI[]{new HostsAPI()};
        toPopulate.addAll(Arrays.asList(host));
        VMsAPI[] vm = new VMsAPI[]{new VMsAPI()};
        toPopulate.addAll(Arrays.asList(vm));
        return true;
    }

    @Override
    protected Node createNodeForKey(AbstractDatacenterAPI key) {
        if(key instanceof HostsAPI){
            HostNodes hn = new HostNodes((HostsAPI)key);
            hn.addNodeListener(this);
            return hn;
        }else if(key instanceof VMsAPI){
            VMNodes vmn = new VMNodes((VMsAPI)key);
            vmn.addNodeListener(this);
            return vmn;
        }else{
            return new AbstractDatacenterNode(key);
        }
    }

    @Override
    public void childrenAdded(NodeMemberEvent nme) {
        
    }

    @Override
    public void childrenRemoved(NodeMemberEvent nme) {
        
    }

    @Override
    public void childrenReordered(NodeReorderEvent nre) {
       
    }

    @Override
    public void nodeDestroyed(NodeEvent ne) {
        System.err.println("nodeDestroyed("+ ne+")");
        toPopulate.remove(ne.getNode());
        refresh(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
    }
    
    

}
