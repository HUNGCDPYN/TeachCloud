/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.node.widget;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import jo.just.api.DateInterface;
import org.jdesktop.swingx.JXDatePicker;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import org.openide.nodes.PropertySupport;


public class DateTime extends PropertySupport.ReadWrite<Date> {

    DateInterface obj;
    String name;

    public DateTime(DateInterface obj, String name, String displayName, String shortDescription) {
        super(name, Date.class, displayName, shortDescription);

        this.obj = obj;
        this.name = name;
    }

    @Override
    public Date getValue() throws IllegalAccessException, InvocationTargetException {
        return obj.getSelectedDate(name);

    }

    @Override
    public PropertyEditor getPropertyEditor() {

        return new CoustomDateTimePropertyEditorSupport(obj, name);

    }

    @Override
    public void setValue(Date newValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        obj.setSelectedDate(name, newValue);

    }
}
class CoustomDateTimePropertyEditorSupport extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private final DateInterface obj;
    String name;
    private InplaceEditor ed = null;

    public CoustomDateTimePropertyEditorSupport(DateInterface obj, String name) {

        this.obj = obj;
        this.name = name;

    }

    @Override
public String getAsText() {
    Date d = (Date) getValue();
    if (d == null) {
        return "Current Time Zoon";
    }
    return new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(d);
}

@Override
public void setAsText(String s) {
    try {
        setValue (new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse(s));
    } catch (ParseException pe) {
        IllegalArgumentException iae = new IllegalArgumentException ("Could not parse date");
        throw iae;
    }
}
    @Override
    public void attachEnv(PropertyEnv env) {

        env.registerInplaceEditorFactory(this);

    }

    @Override
    public InplaceEditor getInplaceEditor() {

        if (ed == null) {

            ed = new DateTimeInplace(obj, name);

        }

        return ed;

    }
}

class DateTimeInplace implements InplaceEditor {

    private final JXDatePicker dateTime = new JXDatePicker();
    private PropertyEditor editor = null;
    private PropertyModel model;
    private final DateInterface obj;
    String name;

    public DateTimeInplace(final DateInterface obj, final String name) {
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
        return dateTime;
    }

    @Override
    public void clear() {
        editor = null;
        model = null;
    }

    @Override
    public Object getValue() {
        return dateTime.getDate();
    }

    @Override
    public void setValue(Object object) {
        dateTime.setDate((Date) object);
    }

    @Override
    public boolean supportsTextEntry() {
        return true;
    }

    @Override
    public void reset() {
        Date d = (Date) editor.getValue();
        if (d != null) {
            dateTime.setDate(d);
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
        return component == dateTime || dateTime.isAncestorOf(component);
    }

    @Override
    public void addActionListener(ActionListener actionListener) {
    }

    @Override
    public void removeActionListener(ActionListener actionListener) {
    }
}
