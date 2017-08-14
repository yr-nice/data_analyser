package com.mu.stock.chart;

import com.mu.stock.ui.ChartView;
import com.mu.util.ui.ChartUtil;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author Peng Mu
 */
public class WheelZoomer extends MouseInputAdapter
{
    private boolean isActive=true;
    private ChartView cv;
    public WheelZoomer(ChartView cv)
    {
        this.cv = cv;
    }
    @Override
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt)
    {
        if(!isActive)
            return;
        if(evt.getScrollType()==java.awt.event.MouseWheelEvent.WHEEL_UNIT_SCROLL)
        {
            double x = ChartUtil.x2val(cv.getPanel(), evt.getPoint());
            double y = ChartUtil.y2val(cv.getPanel(), evt.getPoint());
            int i = (-1)*evt.getUnitsToScroll()/evt.getScrollAmount();
            if(i>0)
                cv.getPanel().zoomInRange(x, y);
            else
                cv.getPanel().zoomOutRange(x, y);
        }
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
