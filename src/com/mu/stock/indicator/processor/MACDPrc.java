package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class MACDPrc
{
    static public void process(List<ProcessedDailyData> list, int shortDate, int longDate, int signalDate)
    {
        float[] emaShort = getEMAPri(list, shortDate);
        float[] emaLong = getEMAPri(list, longDate);
        for(int i=0; i<list.size(); i++)
        {
            ProcessedDailyData p = list.get(i);
            p.setMacd(emaShort[i] - emaLong[i]);
        }
        float[] macdSignal = getEMAMacd(list, signalDate);
        for(int i=0; i<list.size(); i++)
        {
            ProcessedDailyData p = list.get(i);
            p.setMacdSignal(macdSignal[i]);
        }

    }

    static private float[] getEMAPri(List<ProcessedDailyData> list, int dateNum)
    {
        float[] re = new float[list.size()];
        float multi = 2F/(dateNum+1);
        re[0]  = list.get(0).getAdjClose();
        for(int i=1; i<re.length; i++)
        {
            re[i] = list.get(i).getAdjClose()*multi + re[i-1]*(1-multi);
        }
  
        return re;
    }

    static private float[] getEMAMacd(List<ProcessedDailyData> list, int dateNum)
    {
        float[] re = new float[list.size()];
        float multi = 2F/(dateNum+1);
        re[0]  = list.get(0).getMacd();
        for(int i=1; i<re.length; i++)
        {
            re[i] = list.get(i).getMacd()*multi + re[i-1]*(1-multi);
        }
        return re;
    }

}
