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
public class RSIAnalyser
{
    static private int startIndex = 20;
    static public void detectSignal(Stock s)
    {
        LinkedList<ProcessedDailyData> list = s.getPriceHistory();
        if(list.size()<=startIndex)
            return ;

        float[] rsi = new float[list.size()];
        int i=0;
        for(ProcessedDailyData p : list)
        {
            rsi[i] = p.getRsi();
            i++;
        }
        for(i=startIndex; i<list.size(); i++)
        {
            ProcessedDailyData p = list.get(i);
            if(rsi[i]>=70)
                p.addSignal(StockSignal.rsi_over_bought_70);
            if(rsi[i]>=80)
                p.addSignal(StockSignal.rsi_over_bought_80);
            if(rsi[i]<=30)
                p.addSignal(StockSignal.rsi_over_sold_30);
            if(rsi[i]<=20)
                p.addSignal(StockSignal.rsi_over_sold_20);
        }
        //Log.info(Arrays.toString(macd));
        //Log.info(Arrays.toString(macdSignal));

    }
}
