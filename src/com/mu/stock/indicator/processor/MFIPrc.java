package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class MFIPrc
{
    static public void process(List<ProcessedDailyData> list,  int dateNum)
    {
        setMFI(list, dateNum);
    }

    static private void setMFI(List<ProcessedDailyData> list, int dateNum)
    {
        for(int i=dateNum; i<list.size(); i++)
        {
            float sumUp =0;
            float sumDown =0;
            for(int j=i-dateNum+1; j<=i; j++)
            {
                float tp1 = (list.get(j).getHigh() + list.get(j).getLow() + list.get(j).getClose())/3;
                float tp0 = (list.get(j-1).getHigh() + list.get(j-1).getLow() + list.get(j-1).getClose())/3;;
                float mf = tp1 * list.get(j).getVolume();
                if(tp1 > tp0)
                    sumUp += mf;
                else
                    sumDown += mf;
            }
            float f = 100 - 100/(1+sumUp/sumDown);
            list.get(i).setMfi(f);
        }
    }

}
