/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import java.util.Arrays;
import java.util.List;
import jo.just.api.VMAPI;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Zakarea Al Shara
 */
public class VMChildFactory extends ChildFactory<VMAPI> {

    @Override
    protected boolean createKeys(List<VMAPI> toPopulate) {
        VMAPI[] vm = new VMAPI[]{new VMAPI()};
        toPopulate.addAll(Arrays.asList(vm));
        return true;
    }

    @Override
    protected Node createNodeForKey(VMAPI key) {
        return new VMNode((VMAPI) key);
    }
}
