package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Peng Mu
 */
public class LocalMinMaxPrc
{
    static public void process(List<ProcessedDailyData> list, TreeMap<String, ProcessedDailyData> localMin, TreeMap<String, ProcessedDailyData> localMax)
    {
        if(list.size()<3)
            return;

        for(int i=1; i<list.size()-1; i++)
        {
            ProcessedDailyData p = list.get(i);
            float left = list.get(i-1).getAdjClose();
            float right = list.get(i+1).getAdjClose();
            float mid = p.getAdjClose();
            if(mid<=left && mid<=right)
                localMin.put(p.getDateStr(), p);
            if(mid>=left && mid>=right)
                localMax.put(p.getDateStr(), p);
        }

    }
}
