/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node.widget;

import jo.just.api.DropDownInterface;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.nodes.PropertySupport;


public class DropDownList extends PropertySupport.ReadWrite<String> {

    DropDownInterface obj;
    String name;
    String[] items;

    public DropDownList(DropDownInterface obj, String name, String displayName, String shortDescription, String[] items) {
        super(name, String.class, displayName, shortDescription);
     
        this.obj = obj;
        this.name = name;
        this.items = items;
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return obj.getSelectedDropDownList(name);
    
    }

    @Override
    public PropertyEditor getPropertyEditor() {

        return new CoustomPropertyEditorSupport(obj, items, name);

    }

    @Override
    public void setValue(String newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        obj.setSelectedDropDownList(name, newValue);

    }
}

class CoustomPropertyEditorSupport extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private final DropDownInterface obj;
    String name;
    String[] items;

    public CoustomPropertyEditorSupport(DropDownInterface obj, String[] items, String name) {

        this.obj = obj;
        this.items = items;
        this.name = name;

    }

    @Override
    public String getAsText() {

        String s = (String) getValue();

        if (s == null) {

            return items[0];

        }

        return s;

    }

    @Override
    public void setAsText(String s) {

        setValue(s);

    }

    @Override
    public void attachEnv(PropertyEnv env) {

        env.registerInplaceEditorFactory(this);

    }
    private InplaceEditor ed = null;

    @Override
    public InplaceEditor getInplaceEditor() {

        if (ed == null) {

            ed = new Inplace(obj, items, name);

        }

        return ed;

    }
}

class Inplace implements InplaceEditor {

    private final JComboBox comboBox = new JComboBox();
    private PropertyEditor editor = null;
    private final DropDownInterface obj;
    String name;
    String[] items;

    public Inplace(final DropDownInterface obj, String[] items, final String name) {

        this.obj = obj;
        this.items = items;
        this.name = name;
        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                obj.setSelectedDropDownList(name, comboBox.getModel().getSelectedItem().toString());

            }
        });

    }

    @Override
    public void connect(PropertyEditor propertyEditor, PropertyEnv env) {

        editor = propertyEditor;

        reset();

    }

    @Override
    public JComponent getComponent() {

        return comboBox;

    }

    @Override
    public void clear() {

        editor = null;

        model = null;

    }

    @Override
    public Object getValue() {

        comboBox.repaint();

        comboBox.updateUI();

        ((JComponent) comboBox.getParent()).requestFocus();

        comboBox.updateUI();

        comboBox.repaint();

        return comboBox.getSelectedItem();

    }

    @Override
    public void setValue(Object object) {
        
        System.err.println("public void setValue(Object object)");
        comboBox.setSelectedItem(object);

        comboBox.repaint();

        comboBox.updateUI();

        ((DropDownInterface) object).setSelectedDropDownList(name, comboBox.getSelectedItem().toString());

        ((JComponent) comboBox.getParent()).requestFocus();

    }

    @Override
    public boolean supportsTextEntry() {

        return true;

    }

    @Override
    public void reset() {
        
        for(int i = 0; i < items.length; i++)
        {
            comboBox.addItem(items[i]);
        }

        String str = (String) editor.getValue();

        if (str != null) {

            comboBox.setSelectedItem(str);



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
    private PropertyModel model;

    @Override
    public void setPropertyModel(PropertyModel propertyModel) {

        this.model = propertyModel;

    }

    @Override
    public boolean isKnownComponent(Component component) {

        return component == comboBox || comboBox.isAncestorOf(component);

    }

    @Override
    public void addActionListener(ActionListener actionListener) {
//do nothing - not needed for this component
    }

    @Override
    public void removeActionListener(ActionListener actionListener) {
//do nothing - not needed for this component
    }
}
