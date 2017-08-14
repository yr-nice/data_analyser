package com.mu.stock.chart;

import com.mu.stock.entity.DailyPriceLog;
import java.util.Date;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.DefaultXYDataset;

/**
 *
 * @author Peng Mu
 */
public class CandlestickChart
{

    static public JFreeChart createChart(List<DailyPriceLog> data)
    {
        int l = data.size();
        Date[] date = new Date[l];
        double[] high = new double[l];
        double[] low = new double[l];
        double[] open = new double[l];
        double[] close = new double[l];
        double[] volume = new double[l];

        double[][] d2 = new double[2][date.length];
        double[][] dup = new double[2][date.length];
        double[][] ddown = new double[2][date.length];

        for (int i = 0; i < l; i++)
        {
            DailyPriceLog d = data.get(i);
            float adj = d.getAdjClose()/d.getClose();
            date[i] = d.getDate();
            high[i] = d.getHigh()*adj;
            low[i] = d.getLow()*adj;
            open[i] = d.getOpen()*adj;
            close[i] = d.getAdjClose();
            volume[i] = d.getVolume();
            d2[0][i]=d.getDate().getTime();
            d2[1][i]=d.getHLYGravity();
            dup[0][i]=d.getDate().getTime();
            dup[1][i]=d.getHLYGravity()*1.1;
            ddown[0][i]=d.getDate().getTime();
            ddown[1][i]=d.getHLYGravity()*0.9;

        }
        DefaultHighLowDataset dataset = new DefaultHighLowDataset("c", date, high, low, open, close, volume);
        JFreeChart chart= ChartFactory.createCandlestickChart("Candlestick Demo", "Date", "Price", dataset, false);
        XYPlot plot = chart.getXYPlot();
        /*CandlestickRenderer cRender = (CandlestickRenderer)plot.getRenderer(0);
        cRender.setUpPaint(Color.WHITE);
        cRender.setDownPaint(Color.BLACK);
        cRender.setCandleWidth(12);
        //cRender.setBasePaint(Color.white);
         *
         */
        //DefaultHighLowDataset dataset2 = new DefaultHighLowDataset("d", date, high, low, open, close, volume);

        DefaultXYDataset dataset2 = new DefaultXYDataset();
        dataset2.addSeries("d", d2);
        dataset2.addSeries("dup", dup);
        dataset2.addSeries("ddown", ddown);
        plot.setDataset(1, dataset2);
        XYItemRenderer renderer1 = new StandardXYItemRenderer();
        plot.setRenderer(1, renderer1);
        
        return chart;
    }

    static public void setHLYGravity(JFreeChart chart, int lSMA, int sSMA, float scope)
    {
        XYPlot plot = chart.getXYPlot();
       // plot.getDataset().
    }
}
