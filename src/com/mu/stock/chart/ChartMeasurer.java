package com.mu.stock.chart;

import com.mu.stock.ui.ChartView;
import com.mu.util.ui.ChartUtil;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.MouseInputAdapter;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.DefaultXYDataset;

/**
 *
 * @author Peng Mu
 */
public class ChartMeasurer extends MouseInputAdapter
{

    private boolean isActive;
    private ChartView cv;
    ArrayList<PointDiff> diffs=new ArrayList<PointDiff>();
    List<Point> points = new ArrayList<Point>();

    public ChartMeasurer(ChartView cv)
    {
        this.cv = cv;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (!isActive)
        {
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            points.add(e.getPoint());
            processPressed();
        }
    }
    @Override
    public void mouseDragged(MouseEvent e)
    {
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
    }
    private void processPressed()
    {
        if(points.size()>0)
        {
            //int s = points.size()-1;
            //Point p1 = points.get(s-1);
            Point p2 = points.get(points.size()-1);
            //double x1 = ChartUtil.x2val(cv.getPanel(), p1);
            //double y1 = ChartUtil.y2val(cv.getPanel(), p1);
            double x2 = ChartUtil.x2val(cv.getPanel(), p2);
            double y2 = ChartUtil.y2val(cv.getPanel(), p2);
            Date d2= new Date((long)x2);
            PointDiff last = null;
            if(!diffs.isEmpty())
            {
                last = diffs.get(diffs.size()-1);
                Date d1= last.getD1();
                int i1 = cv.getDataMgr().getDateIndex(d1);
                int i2 = cv.getDataMgr().getDateIndex(d2);
                int txnDays = i2-i1+1;
                last.setD2(d2);
                last.setP2(y2);
                last.setTxnDays(txnDays);
            }
            diffs.add(new PointDiff(y2,d2,0,null,0,last));
            cv.updateMeasure(diffs);
        }

    }

    public ChartView getCv()
    {
        return cv;
    }

    public void setCv(ChartView cv)
    {
        this.cv = cv;
    }

    public boolean isIsActive()
    {
        return isActive;
    }

    public void enable()
    {
        this.isActive = true;
    }
    public void disable()
    {
        this.isActive = false;
        points.clear();
        diffs.clear();

    }

    public ArrayList<PointDiff> getDiffs()
    {
        return diffs;
    }

    public void setDiffs(ArrayList<PointDiff> diffs)
    {
        this.diffs = diffs;
    }

}
