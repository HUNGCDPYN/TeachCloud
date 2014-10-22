/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.designer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "View",
id = "jo.just.designer.LayoutAction")
@ActionRegistration(iconBase = "jo/just/designer/resources/layout_edit32.png",
displayName = "#CTL_LayoutAction")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 0),
    @ActionReference(path = "Toolbars/View", position = 0),
    @ActionReference(path = "Shortcuts", name = "D-V")
})
@Messages("CTL_LayoutAction=Layout")
public final class LayoutAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        VisualDesignerTopComponent.getScene().layoutScene();
    }
}
