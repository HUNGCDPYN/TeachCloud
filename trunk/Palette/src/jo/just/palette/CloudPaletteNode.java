package jo.just.palette;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

public class CloudPaletteNode extends AbstractNode {
   
   private CloudPalette album = null;

   public CloudPaletteNode(CloudPalette album) {
      super(Children.LEAF);
      this.album = album;

      this.setDisplayName(getLabel());
   }

   @Override
   public String getHtmlDisplayName() {
      return "<b>   " + album.getTitle() + "</b>";
   }

   @Override
   public Image getIcon(int type) {
      return album.getIcon(type);
   }

    private String getLabel() {
       String label = "<html>" +
    "<table cellspacing=\"0\" cellpadding=\"1\">" +
       "<tr>" +
//          "<td><b>Title </b></td>" +
          "<td><b>" + album.getTitle() + "</b></td>" +
       "</tr>" +
//       "<tr>" +
//          "<td><b>Tracks </b></td>" +
//          "<td>" + album.getTracks() + "</td>" +
//       "</tr>" +
//       "<tr>" +
//          "<td><b>CDs </b></td>" +
//          "<td>" + album.getCDs() + "</td>" +
//       "</tr>" +
//       "<tr>" +
//          "<td><b>Year </b></td>" +
//          "<td>" + album.getYear() + "</td>" +
//       "</tr>" +
    "</table>" +
 "</html>";

       return label;
    }
    
   @Override
   public Transferable drag() throws IOException {
      return album;
   }
}
