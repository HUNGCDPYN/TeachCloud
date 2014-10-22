package jo.just.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

public class GenreNode extends AbstractNode{

   public GenreNode(String genre) {
       
      super(Children.create(new CloudPaletteNodeFactory(genre), false));
      this.setDisplayName(genre);
   }
}
