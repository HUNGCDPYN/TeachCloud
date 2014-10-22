/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.api;

/**
 *
 * @author Zakarea Al Shara
 */
public interface DropDownInterface {
    
    public void setSelectedDropDownList(String Name, String newValue);
    public String getSelectedDropDownList(String Name);
    public String[] getItems(String Name);

}
