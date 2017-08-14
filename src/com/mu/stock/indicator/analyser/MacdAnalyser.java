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
public class MacdAnalyser
{
    static private int startIndex = 50;
    static public void detectSignal(Stock s)
    {
        LinkedList<ProcessedDailyData> list = s.getPriceHistory();
        if(list.size()<=startIndex)
            return ;

        float[] macd = new float[list.size()];
        float[] macdSignal = new float[list.size()];
        int i=0;
        for(ProcessedDailyData p : list)
        {
            macd[i] = p.getMacd();
            macdSignal[i] = p.getMacdSignal();
            i++;
        }
        for(i=startIndex; i<list.size(); i++)
        {
            ProcessedDailyData p = list.get(i);
            if(macd[i]>=0 && macd[i-1]<0)
                p.addSignal(StockSignal.macd_cross_x_up);
            else if(macd[i] <= 0 && macd[i - 1] > 0)
                p.addSignal(StockSignal.macd_cross_x_down);
            
            if(macdSignal[i]>=0 && macdSignal[i-1]<0)
                p.addSignal(StockSignal.macd_signal_cross_x_up);
            else if(macdSignal[i] <= 0 && macdSignal[i - 1] > 0)
                p.addSignal(StockSignal.macd_signal_cross_x_down);
            
            if(macd[i]>=macdSignal[i] && macd[i-1]<macdSignal[i-1])
                p.addSignal(StockSignal.macd_cross_signal_up);
            else if(macd[i]<=macdSignal[i] && macd[i-1]>macdSignal[i-1])
                p.addSignal(StockSignal.macd_cross_signal_down);
        }
        //Log.info(Arrays.toString(macd));
        //Log.info(Arrays.toString(macdSignal));

    }
}
