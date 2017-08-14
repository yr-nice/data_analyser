package com.mu.stock.chart;

import com.mu.stock.ui.ChartView;
import com.mu.util.DateUtil;
import com.mu.util.MathUtil;
import com.mu.util.ui.ChartUtil;
import java.awt.Point;
import java.util.Date;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;

/**
 *
 * @author Peng Mu
 */
public class CoordinateUpdater implements ChartMouseListener
{
    boolean isActive=true;
    ChartView cv;
    public CoordinateUpdater(ChartView cv)
    {
        this.cv =cv;
    }
    public void chartMouseMoved(ChartMouseEvent event)
    {
        Point p = event.getTrigger().getPoint();
        double chartX = ChartUtil.x2val(cv.getPanel(), p);
        double chartY = ChartUtil.y2val(cv.getPanel(), p);
        if(chartX!=0F || chartY!=0)
        {
            String s = DateUtil.toDateStr(new Date((long)chartX))+",  " + MathUtil.round((float)chartY, 3);
            cv.updateCdntLable(s);
            //cv.getPanel().updateUI();
            if(isActive)
                event.getEntity().setToolTipText(s);
        }
    }
    public void chartMouseClicked(ChartMouseEvent event)
    {
    }

    public boolean isIsActive()
    {
        return isActive;
    }

    public void setIsActive(boolean isActive)
    {
        this.isActive = isActive;
    }



}
