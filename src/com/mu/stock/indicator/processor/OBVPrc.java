package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class OBVPrc
{
    static public void process(List<ProcessedDailyData> list)
    {
        list.get(0).setObv(0);
        for(int i=1; i<list.size(); i++)
        {
            ProcessedDailyData p1 = list.get(i);
            ProcessedDailyData p0 = list.get(i-1);
            int a = Float.compare(p1.getAdjClose(),p0.getAdjClose());
            p1.setObv(p0.getObv()+ a*p1.getVolume());
        }

    }

}
