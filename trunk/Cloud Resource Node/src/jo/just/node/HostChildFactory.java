/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import java.util.Arrays;
import java.util.List;
import jo.just.api.AbstractDatacenterAPI;
import jo.just.api.DatacenterAPI;
import jo.just.api.HostAPI;
import jo.just.api.VMAPI;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zakarea Al Shara
 */
public class HostChildFactory extends ChildFactory<HostAPI> {

    @Override
    protected boolean createKeys(List<HostAPI> toPopulate) {
        HostAPI[] host = new HostAPI[]{new HostAPI()};
        toPopulate.addAll(Arrays.asList(host));
        return true;
    }

    @Override
    protected Node createNodeForKey(HostAPI key) {

        return new HostNode((HostAPI) key);

    }
}
