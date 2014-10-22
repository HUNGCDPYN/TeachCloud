/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zakarea Al Shara
 */
public class AbstractDatacenterNode extends AbstractNode{
    
    public AbstractDatacenterNode(Object obj) {
        super(Children.LEAF, Lookups.singleton(obj));
        
    }
    
}
