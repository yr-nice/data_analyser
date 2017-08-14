package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class MomentumPrc
{
    static public void process(List<ProcessedDailyData> list,  int dateNum)
    {
        for(int i=dateNum; i<list.size(); i++)
        {
            ProcessedDailyData p1 = list.get(i);
            ProcessedDailyData p0 = list.get(i-dateNum);
            float f = 100*(p1.getAdjClose()/p0.getAdjClose());
            p1.setMomentum(f);
        }

    }

}
