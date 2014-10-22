/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import java.util.Arrays;
import java.util.List;
import jo.just.api.DatacenterAPI;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zakarea Al Shara
 */
public class CloudComputingChildFactory extends ChildFactory<DatacenterAPI>{

    @Override
    protected boolean createKeys(List<DatacenterAPI> toPopulate) {
        DatacenterAPI[] objs = new DatacenterAPI[]{new DatacenterAPI()};
    toPopulate.addAll(Arrays.asList(objs));
    return true;
    }
    
    @Override
protected Node createNodeForKey(DatacenterAPI key) {
    return new DatacenterNode(key);
}
}
