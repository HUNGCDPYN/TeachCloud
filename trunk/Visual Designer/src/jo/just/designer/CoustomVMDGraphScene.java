/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.designer;

import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author apple
 */
public class CoustomVMDGraphScene extends VMDGraphScene{
    
    private LayerWidget mainLayer = new LayerWidget (this);
    private LayerWidget connectionLayer = new LayerWidget (this);
    private LayerWidget interractionLayer = new LayerWidget(this);

    private WidgetAction createAction = new SceneCreateAction ();
    private WidgetAction connectAction = ActionFactory.createConnectAction (interractionLayer, new SceneConnectProvider ());
    private WidgetAction reconnectAction = ActionFactory.createReconnectAction (new SceneReconnectProvider ());

    public CoustomVMDGraphScene() {
//        addChild (mainLayer);
        addChild (connectionLayer);
        addChild (interractionLayer);

//        getActions ().addAction (createAction);
    }


    @Override
    protected Widget attachPinWidget(String node, String pin) {
        Widget pinWidget =  super.attachPinWidget(node, pin);
        pinWidget.getActions ().addAction (connectAction);
        return pinWidget;
    }


    

    @Override
    protected Widget attachEdgeWidget (String edge) {
        ConnectionWidget connection = new ConnectionWidget (this);
        connection.setTargetAnchorShape (AnchorShape.NONE);
        connection.setEndPointShape (PointShape.SQUARE_FILLED_BIG);
        connection.getActions ().addAction (createObjectHoverAction ());
        connection.getActions ().addAction (createSelectAction ());
        connection.getActions ().addAction (reconnectAction);
        connectionLayer.addChild (connection);
        return connection;
    }



    private class SceneCreateAction extends WidgetAction.Adapter {

    }

    private class SceneConnectProvider implements ConnectProvider {

        private String source = null;
        private String target = null;

        @Override
        public boolean isSourceWidget (Widget sourceWidget) {
            Object object = findObject (sourceWidget);
            source = isPin (object) ? (String) object : null;
            return source != null;
        }

        @Override
        public ConnectorState isTargetWidget (Widget sourceWidget, Widget targetWidget) {
            Object object = findObject (targetWidget);
            target = isPin (object) ? (String) object : null;
            if (target != null)
                return ! source.equals (target) ? ConnectorState.ACCEPT : ConnectorState.REJECT_AND_STOP;
            return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
        }

        @Override
        public boolean hasCustomTargetWidgetResolver (Scene scene) {
            return false;
        }

        @Override
        public Widget resolveTargetWidget (Scene scene, Point sceneLocation) {
            return null;
        }

        @Override
        public void createConnection (Widget sourceWidget, Widget targetWidget) {
            String edge = "edge"+source+target;
            
            if(!findEdgesBetween(source, target).isEmpty())
            {
                return;
            }
            
            
            addEdge(edge);
            setEdgeSource(edge, source);
            setEdgeTarget(edge, target);
            
        }

    }

    private class SceneReconnectProvider implements ReconnectProvider {

        String edge;
        String originalNode;
        String replacementNode;

        @Override
        public void reconnectingStarted (ConnectionWidget connectionWidget, boolean reconnectingSource) {
        }

        @Override
        public void reconnectingFinished (ConnectionWidget connectionWidget, boolean reconnectingSource) {
        }

        @Override
        public boolean isSourceReconnectable (ConnectionWidget connectionWidget) {
            Object object = findObject (connectionWidget);
            edge = isEdge (object) ? (String) object : null;
            originalNode = edge != null ? getEdgeSource (edge) : null;
            return originalNode != null;
        }

        @Override
        public boolean isTargetReconnectable (ConnectionWidget connectionWidget) {
            Object object = findObject (connectionWidget);
            edge = isEdge (object) ? (String) object : null;
            originalNode = edge != null ? getEdgeTarget (edge) : null;
            return originalNode != null;
        }

        @Override
        public ConnectorState isReplacementWidget (ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
            Object object = findObject (replacementWidget);
            replacementNode = isPin (object) ? (String) object : null;
            if (replacementNode != null)
                return ConnectorState.ACCEPT;
            return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
        }

        @Override
        public boolean hasCustomReplacementWidgetResolver (Scene scene) {
            return false;
        }

        @Override
        public Widget resolveReplacementWidget (Scene scene, Point sceneLocation) {
            return null;
        }
        
        @Override
        public void reconnect (ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
            if (replacementWidget == null)
                removeEdge (edge);
            else if (reconnectingSource)
                setEdgeSource (edge, replacementNode);
            else
                setEdgeTarget (edge, replacementNode);
        }

    }

    
}
