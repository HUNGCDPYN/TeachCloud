/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2011, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * -------------------------
 * TimeSeriesChartDemo1.java
 * -------------------------
 * (C) Copyright 2003-2011, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   ;
 *
 * Changes
 * -------
 * 09-Mar-2005 : Version 1, copied from the demo collection that ships with
 *               the JFreeChart Developer Guide (DG);
 *
 */
package jo.just.charts;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import jo.just.charts.api.HostStatistical;
import jo.just.charts.api.HostStatisticalGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 * An example of a time series chart.  For the most part, default settings are
 * used, except that the renderer is modified to show filled shapes (as well as
 * lines) at each data point.
 */
// ******************************************************************
//  More than 150 demo applications are included with the JFreeChart
//  Developer Guide...for more information, see:
//
//  >   http://www.object-refinery.com/jfreechart/guide.html
//
// ******************************************************************
public class TimeSeriesChartDemo1 extends JPanel {

    private static final long serialVersionUID = 1L;
    ChartPanel chartPanel;

    {
        // set a theme using the new shadow generator feature available in
        // 1.0.14 - for backwards compatibility it is not enabled by default
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",
                true));
    }

    /**
     * A demonstration application showing how to create a simple time series
     * chart.  This example uses monthly data.
     *
     * @param title  the frame title.
     */
    public TimeSeriesChartDemo1(String title) {
        super(new BorderLayout());
        chartPanel = (ChartPanel) createDemoPanel(title);
        chartPanel.setPreferredSize(new java.awt.Dimension(2000, 810));
        chartPanel.validate();
        chartPanel.setVisible(true);
//        add(chartPanel);
    }
    
    public ChartPanel getChartPanel(){
        return chartPanel;
    }

    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private JFreeChart createChart(String title, XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title, // title
                "Time (s)", // x-axis label
                "Utilization (%)", // y-axis label
                dataset, // data
                true, // create legend?
                true, // generate tooltips?
                false // generate URLs?
                );

        chart.setBackgroundPaint(Color.white);
        chart.addChangeListener(new ChartChangeAction());
        

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

//        DateAxis axis = (DateAxis) plot.getDomainAxis();
//        axis.setDateFormatOverride(new SimpleDateFormat("MS-SS-MM"));

        return chart;

    }

    /**
     * Creates a dataset, consisting of two series of monthly data.
     *
     * @return The dataset.
     */
    private XYDataset createDataset() {

        TimeSeriesCollection dataset = new TimeSeriesCollection();

        for (int i = 0; i < HostStatisticalGenerator.getInstance().getHostsStatisticalList().size(); i++) {
            HostStatistical host = HostStatisticalGenerator.getInstance().getHostsStatisticalList().get(i);
            TimeSeries s = new TimeSeries(host.getName());

            for (int j = 0; j < HostStatistical.getTime().size(); j++) {
                int second = (int) HostStatistical.getTime(j);
                int miliSecond = (int) ((double) (HostStatistical.getTime(j) * 100) - (double) (second * 100));
                int minute = second / 60;
                int hour = minute / 60;
                int day = 1 + hour / 24;
                int month = 1 + day / 30;
                int year = month / 12;
                second %= 60;
                minute %= 60;
                hour %= 24;
                day %= 30;
                month %= 12;
                year = 1970;

                double utilization = host.getUtilization(j);

//                s.add(new Month(second, 2013), host.getUtilization(j));
                s.add(new Millisecond(miliSecond, second, minute, hour, day, month, year), utilization);

            }
            dataset.addSeries(s);
        }
        
        dataset.addChangeListener(new DatasetChangeAction());

        return dataset;

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public JPanel createDemoPanel(String title) {
        JFreeChart chart = createChart(title, createDataset());
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
    
  
    private class ChartChangeAction implements ChartChangeListener{

        @Override
        public void chartChanged(ChartChangeEvent cce) {
            System.err.println("ChartChangeListener ***");
        }
        
    }
    
    private class DatasetChangeAction implements DatasetChangeListener{

        @Override
        public void datasetChanged(DatasetChangeEvent dce) {
            System.err.println("DatasetChangeListener ***");
        }
        
    }
}
