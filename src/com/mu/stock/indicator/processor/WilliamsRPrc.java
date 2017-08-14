package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import com.mu.util.log.Log;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class WilliamsRPrc
{
    static public void process(List<ProcessedDailyData> list,  int dateNum)
    {
        setWilliamsR(list, dateNum);
    }

    static private void setWilliamsR(List<ProcessedDailyData> list, int dateNum)
    {
        for(int i=dateNum; i<list.size(); i++)
        {
            float h = 0;
            float l = 100000;
            for(int j=i-dateNum+1; j<=i; j++)
            {
                float hn = list.get(j).getHigh();
                float ln = list.get(j).getLow();
                h = hn > h ? hn : h;
                l = ln < l ? ln :l;
            }
            float c = list.get(i).getClose();
            float f = 100*(c-h)/(h-l);
            list.get(i).setWilliams(f);
        }
    }

}
