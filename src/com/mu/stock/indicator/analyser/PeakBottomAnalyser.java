package com.mu.stock.indicator.analyser;

import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.Stock;
import com.mu.util.log.Log;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author Peng Mu
 */
public class PeakBottomAnalyser
{
static private int startIndex = 10;
    static public void detectSignal(Stock s)
    {
        LinkedList<ProcessedDailyData> list = s.getPriceHistory();
        if(list.size()<=startIndex)
            return ;

        float[] pri = new float[list.size()];
        int i=0;
        for(ProcessedDailyData p : list)
        {
            pri[i] = p.getRsi();
            i++;
        }
        for(i=startIndex; i<list.size(); i++)
        {
            if(isPeak(pri, i, 5))
                list.get(i).addSignal(StockSignal.peak_10_days);
            else if(isBottom(pri, i, 5))
                list.get(i).addSignal(StockSignal.bottom_10_days);

            if(isPeak(pri, i, 10))
                list.get(i).addSignal(StockSignal.peak_20_days);
            else if(isBottom(pri, i, 10))
                list.get(i).addSignal(StockSignal.bottom_20_days);

            if(isPeak(pri, i, 30))
                list.get(i).addSignal(StockSignal.peak_60_days);
            else if(isBottom(pri, i, 30))
                list.get(i).addSignal(StockSignal.bottom_60_days);
        }
    }

    static private boolean isBottom(float[] pri, int i, int range)
    {
        float cur = pri[i];
        int left = i-range>0? i-range:0;
        int right = i+range<pri.length? i+range:pri.length;
        for(int j=left; j<right; j++)
        {
            if(pri[j]<cur)
                return false;
        }
        return true;
    }
    static private boolean isPeak(float[] pri, int i, int range)
    {
        float cur = pri[i];
        int left = i-range>0? i-range:0;
        int right = i+range<pri.length? i+range:pri.length;
        for(int j=left; j<right; j++)
        {
            if(pri[j]>cur)
                return false;
        }
        return true;
    }
}
