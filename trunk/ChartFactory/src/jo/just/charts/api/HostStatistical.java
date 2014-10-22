/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jo.just.charts.api;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zakarea Al Shara
 */
public class HostStatistical {

    private String name;
    private int id;
    private static List<Double> time = new ArrayList<Double>();
    private List<Double> utilization = new ArrayList<Double>();
    private List<Double> energy = new ArrayList<Double>();
    
    public HostStatistical(int id) {
        this.id = id;
        this.name = "Host["+id+"]";
    }

    public HostStatistical(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public static void addTime(double time) {
        HostStatistical.time.add(time);
    }

    public static double getTime(int idx) {
        return time.get(idx);
    }

    public void addUtilization(double utilization) {
        this.utilization.add(utilization);
    }

    public double getUtilization(int idx) {
        return utilization.get(idx);
    }

    public void addEnergy(double energy) {
        this.energy.add(energy);
    }

    public double getEnergy(int idx) {
        return energy.get(idx);
    }

    public static List<Double> getTime() {
        return time;
    }

    public List<Double> getUtilization() {
        return utilization;
    }

    public List<Double> getEnergy() {
        return energy;
    }
    
    public double getAllUtilization(){
        double sum = 0.0;
        for(int i = 0; i < utilization.size(); i++)
        {
            sum += (utilization.get(i) == null) ? 0.0 : utilization.get(i);
        }
        return sum;
    }
    
    public double getallEnergy(){
        double sum = 0.0;
        for(int i = 0; i < energy.size(); i++)
        {
            sum += (energy.get(i) == null) ? 0.0 : energy.get(i);
        }
        return sum;
    }
}
