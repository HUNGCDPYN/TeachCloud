/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.designer;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.graph.layout.GridGraphLayout;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.vmd.VMDColorScheme;
import org.netbeans.api.visual.vmd.VMDConnectionWidget;
import org.netbeans.api.visual.vmd.VMDFactory;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.vmd.VMDPinWidget;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author apple
 */
public class VCCDGraphScene extends GraphPinScene<String, String, String> {

    public static final String PIN_ID_DEFAULT_SUFFIX = "#default"; // NOI18N
    private LayerWidget backgroundLayer = new LayerWidget(this);
    private LayerWidget mainLayer = new LayerWidget(this);
    private LayerWidget connectionLayer = new LayerWidget(this);
    private LayerWidget upperLayer = new LayerWidget(this);
    private Router router;
    private WidgetAction moveAction = ActionFactory.createMoveAction();
    private WidgetAction connectionAction = ActionFactory.createConnectAction(upperLayer, new ConnectProviderImpl());
    private WidgetAction selectAction = ActionFactory.createSelectAction(new SelectProvider(), true);
    private WidgetAction hoverAction = ActionFactory.createHoverAction(new HoverProvider());
    private SceneLayout sceneLayout;
    private VMDColorScheme scheme;
    private int edgeID;
    private Map<Widget, Node> mapWidgetNode = new HashMap<Widget, Node>();

    private class ReconnectProviderImpl implements ReconnectProvider {

        private Object replacementNode;
        private Object originalNode;
        private Object edge;

        @Override
        public boolean isSourceReconnectable(ConnectionWidget connectionWidget) {

            Object object = findObject(connectionWidget);
            edge = (isEdge(object) ? object : null);
            originalNode = edge != null ? VCCDGraphScene.this.getEdgeSource(edge.toString()) : null;

            if (originalNode != null) {
                Widget widget = VCCDGraphScene.this.findWidget(originalNode);
                if (widget != null && widget instanceof VMDPinWidget) {

                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean isTargetReconnectable(ConnectionWidget connectionWidget) {

            Object object = findObject(connectionWidget);
            edge = isEdge(object) ? object : null;
            originalNode = edge != null ? getEdgeTarget(edge.toString()) : null;

            if (originalNode != null) {
                String temp = originalNode.toString();//.substring(0, originalNode.toString().length() - 8);zax
                Widget widget = VCCDGraphScene.this.findWidget(temp);
                if (widget != null && widget instanceof VMDPinWidget) {

                    return true;
                }
            }

            return false;
        }

        @Override
        public void reconnectingStarted(ConnectionWidget cw, boolean bln) {
        }

        @Override
        public void reconnectingFinished(ConnectionWidget cw, boolean bln) {
        }

        @Override
        public ConnectorState isReplacementWidget(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {

//            if (reconnectingSource) {
//                return ConnectorState.REJECT_AND_STOP;
//            }

            Object object = findObject(replacementWidget);

            replacementNode = isPin(object) ? object : null;

            if (replacementNode != null) {
                if (true/*reconnectingSource*/) {
                    Widget widget = VCCDGraphScene.this.findWidget(replacementNode.toString());
                    if (widget != null && widget instanceof VMDPinWidget) {

                        return ConnectorState.ACCEPT;
                    }
                }


            }

            return ConnectorState.REJECT;
        }

        @Override
        public boolean hasCustomReplacementWidgetResolver(Scene scene) {
            return false;
        }

        @Override
        public Widget resolveReplacementWidget(Scene scene, Point point) {
            return null;
        }

        @Override
        public void reconnect(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
//            if (reconnectingSource) {
//                return;
//            }
            if (replacementWidget == null) {
                removeEdge(edge.toString());
            } else if (reconnectingSource) {
                setEdgeSource(edge.toString(), replacementNode.toString());
            } else {
                setEdgeTarget(edge.toString(), replacementNode.toString()/* + VCCDGraphScene.PIN_ID_DEFAULT_SUFFIX zax*/);
            }
            validate();
        }
    }

    private class ConnectProviderImpl implements ConnectProvider {

        @Override
        public boolean isSourceWidget(Widget widget) {
            if (widget != null && widget instanceof VMDPinWidget) {
                return true;
            }
            return false;
        }

        @Override
        public ConnectorState isTargetWidget(Widget widget, Widget widget1) {
            if (widget != null && widget instanceof VMDPinWidget && widget1 != null && widget1 instanceof VMDPinWidget) {
                if (widget.getParentWidget() != widget1) {
                    return ConnectorState.ACCEPT;
                }
            }
            return ConnectorState.REJECT;
        }

        @Override
        public boolean hasCustomTargetWidgetResolver(Scene scene) {
            return false;
        }

        @Override
        public Widget resolveTargetWidget(Scene scene, Point point) {
            return null;
        }

        @Override
        public void createConnection(Widget widget, Widget widget1) {
            String edgeID = "edge" + VCCDGraphScene.this.edgeID++;
            VCCDGraphScene.this.addEdge(edgeID);
            VCCDGraphScene.this.setEdgeSource(edgeID, VCCDGraphScene.this.findObject(widget).toString());
            VCCDGraphScene.this.setEdgeTarget(edgeID, VCCDGraphScene.this.findObject(widget1).toString());
        }
    }

    public VCCDGraphScene() {
        this(VMDFactory.getNetBeans60Scheme());
        getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {

            @Override
            public JPopupMenu getPopupMenu(Widget widget, Point point) {
                JPopupMenu menu = new JPopupMenu();
                Action[] action = createMenuActions();
                for (int i = 0; i < action.length; i++) {
                    menu.add(action[i]);
                }
                return menu;
            }
        }));
    }

    public VCCDGraphScene(VMDColorScheme scheme) {
        this.scheme = scheme;
        setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_PARENTS);

        addChild(backgroundLayer);
        addChild(mainLayer);
        addChild(connectionLayer);
        addChild(upperLayer);

        router = RouterFactory.createOrthogonalSearchRouter(mainLayer, connectionLayer);

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createRectangularSelectAction(this, backgroundLayer));
        getActions().addAction(selectAction);
        

        sceneLayout = LayoutFactory.createSceneGraphLayout(this, new GridGraphLayout<String, String>().setChecker(true));
    }

    @Override
    protected Widget attachNodeWidget(final String node) {
        VMDNodeWidget widget = new VMDNodeWidget(this, scheme);
        mainLayer.addChild(widget);

        widget.getActions().addAction(selectAction);
        widget.getHeader().getActions().addAction(createObjectHoverAction());
        
        widget.getActions().addAction(moveAction);
        
        widget.getActions().addAction(createSelectAction());

            
        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {

            @Override
            public JPopupMenu getPopupMenu(Widget widget, Point point) {
                JPopupMenu menu = new JPopupMenu();
                Action[] action = createNodeActions(node);
                for (int i = 0; i < action.length; i++) {
                    menu.add(action[i]);
                }
                return menu;
            }
        }));

        return widget;
    }

    @Override
    protected Widget attachPinWidget(String node, String pin) {
        if (pin.endsWith(PIN_ID_DEFAULT_SUFFIX)) {
            return null;
        }

        VMDPinWidget widget = new VMDPinWidget(this, scheme);
        ((VMDNodeWidget) findWidget(node)).attachPinWidget(widget);
        widget.getActions().addAction(createObjectHoverAction());
        widget.getActions().addAction(createSelectAction());
        widget.getActions().addAction(connectionAction);
        


        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(final String edge) {
        VMDConnectionWidget connectionWidget = new VMDConnectionWidget(this, scheme);
        connectionWidget.setRouter(router);
        connectionLayer.addChild(connectionWidget);
        connectionWidget.getActions().addAction(createObjectHoverAction());
        connectionWidget.getActions().addAction(ActionFactory.createReconnectAction(new ReconnectProviderImpl()));
        connectionWidget.getActions().addAction(createSelectAction());
        connectionWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {

            @Override
            public JPopupMenu getPopupMenu(Widget widget, Point point) {
                JPopupMenu menu = new JPopupMenu();
                Action[] action = createEdgeActions(edge);
                for (int i = 0; i < action.length; i++) {
                    menu.add(action[i]);
                }
                return menu;
            }
        }));
//        connectionWidget.getActions().addAction(moveControlPointAction);
        return connectionWidget;
    }

    @Override
    protected void attachEdgeSourceAnchor(String edge, String oldSourcePin, String sourcePin) {
        ((ConnectionWidget) findWidget(edge)).setSourceAnchor(getPinAnchor(sourcePin));
    }

    @Override
    protected void attachEdgeTargetAnchor(String edge, String oldTargetPin, String targetPin) {
        ((ConnectionWidget) findWidget(edge)).setTargetAnchor(getPinAnchor(targetPin));
    }

    private Anchor getPinAnchor(String pin) {
        if (pin == null) {
            return null;
        }
        VMDNodeWidget nodeWidget = (VMDNodeWidget) findWidget(getPinNode(pin));
        Widget pinMainWidget = findWidget(pin);
        Anchor anchor;
        if (pinMainWidget != null) {
            anchor = AnchorFactory.createDirectionalAnchor(pinMainWidget, AnchorFactory.DirectionalAnchorKind.HORIZONTAL, 8);
            anchor = nodeWidget.createAnchorPin(anchor);
        } else {
            anchor = nodeWidget.getNodeAnchor();
        }
        return anchor;
    }

    public void addWidgetNode(Widget w, Node n) {
        mapWidgetNode.put(w, n);
    }

    public Map<Widget, Node> getMapWidgetNode() {
        return mapWidgetNode;
    }
    
    public Node getNode(Widget widget){
        return mapWidgetNode.get(widget);
    }

    
    protected Action[] createMenuActions() {
        return new Action[]{new AbstractAction("Layout") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    VCCDGraphScene.this.layoutScene();
                }
            }};
    }


    protected Action[] createNodeActions(final String nodeID) {
        return new Action[]{new AbstractAction("Remove") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    VCCDGraphScene.this.removeNode(nodeID);
                }
            }}; 
    }

    protected Action[] createEdgeActions(final String edgeID) {
        return new Action[]{new AbstractAction("Remove") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    VCCDGraphScene.this.removeEdge(edgeID);
                }
            }};
    }

    public void layoutScene() {
        sceneLayout.invokeLayout();
    }
    
    private class SelectProvider implements org.netbeans.api.visual.action.SelectProvider{

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean invertSelection) {
            System.err.println("isAimingAllowed");
            return false;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean invertSelection) {
            System.err.println("isSelectionAllowed");
            if(widget != null && mapWidgetNode.get(widget) != null)
                return true;
            System.err.println(">> return false");
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean invertSelection) {
            System.err.println("Select = "+widget);
            
//            Node node = (mapWidgetNode.get(widget) != null) ? mapWidgetNode.get(widget) : NodeExplorer.getDefultRootNood();
//
//            NodeExplorer.getNodeExplorerManager().setRootContext(node);
//
//            TopComponent tc = WindowManager.getDefault().findTopComponent("NodeExplorerTopComponent");
//            System.err.println("tc = "+tc);
//            tc.setActivatedNodes(new Node[]{node});
//            
//            try {
//                NodeExplorer.getNodeExplorerManager().setSelectedNodes(new Node[]{node});
//            } catch (PropertyVetoException ex) {
//                Exceptions.printStackTrace(ex);
//            }
            
             try {
                 Node node = (mapWidgetNode.get(widget) != null) ? mapWidgetNode.get(widget) : NodeExplorer.getDefultRootNood();
                ObjectScene scene = ((ObjectScene) widget.getScene());
                Object object = scene.findObject(widget);

                scene.setFocusedObject(object);
                if (object != null) {
                    if (!invertSelection && scene.getSelectedObjects().contains(object)) {
                        return;
                    }

                    scene.userSelectionSuggested(Collections.singleton(object), invertSelection);
                } else {
                    scene.userSelectionSuggested(Collections.emptySet(), invertSelection);
                }

                NodeExplorer.getNodeExplorerManager().setRootContext(node);
                TopComponent tc = WindowManager.getDefault().findTopComponent("NodeExplorerTopComponent");
//                tc.setActivatedNodes(new Node[]{node});
                NodeExplorer.getNodeExplorerManager().setSelectedNodes(new Node[]{node});
                tc.requestActive();

            } catch (PropertyVetoException ex) {
                Exceptions.printStackTrace(ex);
            }

            
        }
        
    }
    
    
    private class HoverProvider implements org.netbeans.api.visual.action.HoverProvider{

        @Override
        public void widgetHovered(Widget widget) {
            
            System.err.println("Hover = "+widget);
            NodeExplorer.getNodeExplorerManager().setRootContext(mapWidgetNode.get(widget));
            try {
                NodeExplorer.getNodeExplorerManager().setSelectedNodes(new Node[]{NodeExplorer.getNodeExplorerManager().getRootContext()});
            } catch (PropertyVetoException ex) {
                Exceptions.printStackTrace(ex);
            }
            try {
                NodeExplorer.getNodeExplorerManager().setExploredContextAndSelection(NodeExplorer.getNodeExplorerManager().getRootContext(),new Node[]{NodeExplorer.getNodeExplorerManager().getRootContext()});
            } catch (PropertyVetoException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
    }
    
}
