/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.palette;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Heiko
 */
public class CloudPaletteNodeFactory extends ChildFactory<String> {

    String genre;

    public CloudPaletteNodeFactory(String genre) {
        this.genre = genre;
    }

    @Override
    protected boolean createKeys(List<String> toPopulate) {
        toPopulate.add(genre);
        return true;
    }

    @Override
    protected Node[] createNodesForKey(String key) {
        if (key == "Datacenters") {
            return new Node[]{
                        new CloudPaletteNode(new CloudPalette("Datacenter", "jo/just/palette/resources/datacenter32.png", "jo/just/palette/resources/datacenter32.png")),
                        new CloudPaletteNode(new CloudPalette("Host", "jo/just/palette/resources/host32.png", "jo/just/palette/resources/host32.png")),
                        new CloudPaletteNode(new CloudPalette("Pe", "jo/just/palette/resources/pe32.png", "jo/just/palette/resources/pe32.png.jpg")),
                        new CloudPaletteNode(new CloudPalette("VM", "jo/just/palette/resources/vm32.png", "jo/just/palette/resources/vm32.png"))
                    };
        } else if (key == "Clouds") {
            return new Node[]{new CloudPaletteNode(new CloudPalette("Broker", "jo/just/palette/resources/broker32.png", "jo/just/palette/resources/broker32.png")),
                        new CloudPaletteNode(new CloudPalette("Cloudlet", "jo/just/palette/resources/cloudlet32.png", "jo/just/palette/resources/cloudlet32.png"))};
        } else if (key == "Networks") {
            return new Node[]{
                        new CloudPaletteNode(new CloudPalette("Root Switch", "jo/just/palette/resources/root_switch32.png", "jo/just/palette/resources/root_switch32.png")),
                        new CloudPaletteNode(new CloudPalette("Aggregate Switch", "jo/just/palette/resources/aggregate_switch32.png", "jo/just/palette/resources/aggregate_switch32.png")),
                        new CloudPaletteNode(new CloudPalette("Edge Switch", "jo/just/palette/resources/edge_switch32.png", "jo/just/palette/resources/edge_switch32.png")),
                        new CloudPaletteNode(new CloudPalette("Connector", "jo/just/palette/resources/connector32.png", "jo/just/palette/resources/connector32.png"))
                    };
        }

        System.err.println("return NULL");




        return null;
    }
}
