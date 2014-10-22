package jo.just.palette;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.BeanInfo;
import java.io.IOException;
import org.openide.util.ImageUtilities;

public class CloudPalette implements Transferable {

    public static final DataFlavor DATA_FLAVOR = new DataFlavor(CloudPalette.class, "palette");
    private String title;
    private String icon16;
    private String icon32;
    
    private int type;
    public static final int DATACENTER = 0;
    public static final int HOST = 1;
    public static final int PE = 2;
    public static final int VM = 3;
    public static final int BROKER = 4;
    public static final int CLOUDLET = 5;
    public static final int ROOT_SWITCH = 6;
    public static final int AGGREGATE_SWITCH = 7;
    public static final int EDGE_SWITCH = 8;
    public static final int CONNECTOR = 9;

    public CloudPalette(String title, String icon16, String icon32) {
        this.title = title;
        this.icon16 = icon16;
        this.icon32 = icon32;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        if(title == "Datacenter"){
            return DATACENTER;
        }else if(title == "Host"){
            return HOST;
        }else if(title == "Pe"){
            return PE;
        }else if(title == "VM"){
            return VM;
        }else if(title == "Broker"){
            return BROKER;
        }else if(title == "Cloudlet"){
            return CLOUDLET;
        }else if(title == "Root Switch"){
            return ROOT_SWITCH;
        }else if(title == "Aggregate Switch"){
            return AGGREGATE_SWITCH;
        }else if(title == "Edge Switch"){
            return EDGE_SWITCH;
        }else if(title == "Connector"){
            return CONNECTOR;
        }else{
            return -1;
        }
    }
    
    

    public Image getIcon(int type) {
        if (BeanInfo.ICON_COLOR_16x16 == type) {
            return ImageUtilities.loadImage(icon16);
        } else {
            return ImageUtilities.loadImage(icon32);
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DATA_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == DATA_FLAVOR;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
            IOException {
        if (flavor == DATA_FLAVOR) {
            return this;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
