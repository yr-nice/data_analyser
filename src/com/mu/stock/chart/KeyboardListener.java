package com.mu.stock.chart;

import com.mu.stock.ui.ChartView;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Peng Mu
 */
public class KeyboardListener extends KeyAdapter
{
    private boolean isActive=true;
    private ChartView cv;
    public KeyboardListener(ChartView cv)
    {
        this.cv = cv;
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_LEFT: cv.move(-0.25);break;
            case KeyEvent.VK_RIGHT: cv.move(0.25);break;
            case KeyEvent.VK_HOME: cv.move(Integer.MIN_VALUE);break;
            case KeyEvent.VK_END: cv.move(Integer.MAX_VALUE);break;
        }
    }


}
