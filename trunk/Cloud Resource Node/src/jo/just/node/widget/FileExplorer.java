/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node.widget;

import jo.just.api.DropDownInterface;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.nodes.PropertySupport;


public class FileExplorer extends PropertySupport.ReadWrite<String> {

    DropDownInterface obj;
    String name;

    public FileExplorer(DropDownInterface obj, String name, String displayName, String shortDescription) {
        super(name, String.class, displayName, shortDescription);

        this.obj = obj;
        this.name = name;
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return obj.getSelectedDropDownList(name);

    }

    @Override
    public PropertyEditor getPropertyEditor() {

        return new CoustomFileExplorerPropertyEditorSupport(obj, name);

    }

    @Override
    public void setValue(String newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        obj.setSelectedDropDownList(name, newValue);

    }
}
class CoustomFileExplorerPropertyEditorSupport extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private final DropDownInterface obj;
    String name;
    private InplaceEditor ed = null;

    public CoustomFileExplorerPropertyEditorSupport(DropDownInterface obj, String name) {

        this.obj = obj;
        this.name = name;

    }

    @Override
public String getAsText() {
    Object s = getValue();
    if (s == null) {
        return "";
    }
    return s.toString();
}

@Override
public void setAsText(String s) {

        setValue (s);
    
}
    @Override
    public void attachEnv(PropertyEnv env) {

        env.registerInplaceEditorFactory(this);

    }

    @Override
    public InplaceEditor getInplaceEditor() {

        if (ed == null) {

            ed = new FileExplorerInplace(obj, name);

        }

        return ed;

    }
}

class FileExplorerInplace implements InplaceEditor {

    private final JFileChooser fileChooser = new JFileChooser();
    JLabel label = new JLabel();
    private PropertyEditor editor = null;
    private PropertyModel model;
    private final DropDownInterface obj;
    String name;

    public FileExplorerInplace(final DropDownInterface obj, final String name) {
        fileChooser.showOpenDialog(fileChooser);
        this.obj = obj;
        this.name = name;
    }

    @Override
    public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
        editor = propertyEditor;
        reset();

    }

    @Override
    public JComponent getComponent() {
        return label;
    }

    @Override
    public void clear() {
        editor = null;
        model = null;
    }

    @Override
    public Object getValue() {
        label.repaint();
        label.updateUI();
        ((JComponent) label.getParent()).requestFocus();
        label.repaint();
        label.updateUI();
         System.err.println("getPath = "+fileChooser.getSelectedFile().getPath());
          System.err.println("getAbsolutePath = "+fileChooser.getSelectedFile().getAbsolutePath());
        return fileChooser.getSelectedFile().getPath();
    }

    @Override
    public void setValue(Object object) {
        fileChooser.setSelectedFile(new File((String)object));
        label.setText((String)object);
        label.repaint();
        label.updateUI();
        System.err.println("setPath = "+fileChooser.getSelectedFile().getPath());
         System.err.println("setAbsolutePath = "+fileChooser.getSelectedFile().getAbsolutePath());
        ((DropDownInterface) object).setSelectedDropDownList(name, fileChooser.getSelectedFile().getPath());
    }

    @Override
    public boolean supportsTextEntry() {
        return true;
    }

    @Override
    public void reset() {
        String s = (String) editor.getValue();
        if (s != null) {
            fileChooser.setSelectedFile(new File(s));
            label.setText(s);
        }

    }

    @Override
    public KeyStroke[] getKeyStrokes() {

        return new KeyStroke[0];

    }

    @Override
    public PropertyEditor getPropertyEditor() {
        return editor;
    }

    @Override
    public PropertyModel getPropertyModel() {
        return model;
    }

    @Override
    public void setPropertyModel(PropertyModel propertyModel) {
        this.model = propertyModel;
    }

    @Override
    public boolean isKnownComponent(Component component) {
        return component == fileChooser || fileChooser.isAncestorOf(component) || component == label || label.isAncestorOf(component);
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
    }

    @Override
    public void removeActionListener(ActionListener actionListener) {
    }
}
