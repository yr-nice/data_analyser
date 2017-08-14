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
public class RocAnalyser
{
    static private int startIndex = 20;
    static public void detectSignal(Stock s)
    {
        LinkedList<ProcessedDailyData> list = s.getPriceHistory();
        if(list.size()<=startIndex)
            return ;

        for(int i=startIndex; i<list.size(); i++)
        {
            ProcessedDailyData p = list.get(i);
            float w = p.getRoc();
            if(w>=10)
                p.addSignal(StockSignal.roc_over_bought);
            if(w<=-10)
                p.addSignal(StockSignal.roc_over_sold);
        }
        //Log.info(Arrays.toString(macd));
        //Log.info(Arrays.toString(macdSignal));

    }
}
