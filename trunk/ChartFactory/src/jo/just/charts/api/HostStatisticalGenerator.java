/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.charts.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Zakarea Al Shara
 */
public class HostStatisticalGenerator {
    
    private static HostStatisticalGenerator instance = null;
    private Map<Integer, HostStatistical> HostsStatisticalMap = new HashMap<Integer, HostStatistical>();
    private List<HostStatistical> HostsStatisticalList = new ArrayList<HostStatistical>();
    
    public static HostStatisticalGenerator getInstance(){
        
        if(instance == null){
            instance = new HostStatisticalGenerator();
        }
        return instance;
    }
    
    public void addTime(double time) {
        if(!HostStatistical.getTime().isEmpty() && time == HostStatistical.getTime(HostStatistical.getTime().size()-1)){return;}
        HostStatistical.addTime(time);
    }


    public void addUtilization(int id, double time, double utilization) {
//        if(!HostStatistical.getTime().isEmpty() && time == HostStatistical.getTime(HostStatistical.getTime().size()-1)){return;}
        if(!HostsStatisticalMap.containsKey(id)){
            HostsStatisticalMap.put(id, new HostStatistical(id));
            HostsStatisticalList.add(HostsStatisticalMap.get(id));
        }
        HostsStatisticalMap.get(id).addUtilization(utilization);
    }


    public void addEnergy(int id, double time, double energy) {
//        if(!HostStatistical.getTime().isEmpty() && time == HostStatistical.getTime(HostStatistical.getTime().size()-1)){return;}
        if(!HostsStatisticalMap.containsKey(id)){
            HostsStatisticalMap.put(id, new HostStatistical(id));
            HostsStatisticalList.add(HostsStatisticalMap.get(id));
        }
        HostsStatisticalMap.get(id).addEnergy(energy);
    }

    public Map<Integer, HostStatistical> getHostsStatisticalMap() {
        return HostsStatisticalMap;
    }

    public List<HostStatistical> getHostsStatisticalList() {
        return HostsStatisticalList;
    }
    
    

    
}
