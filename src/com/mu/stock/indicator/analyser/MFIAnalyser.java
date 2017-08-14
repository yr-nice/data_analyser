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
public class MFIAnalyser
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
            float mfi = p.getMfi();
            if(mfi>=70)
                p.addSignal(StockSignal.mfi_over_bought_70);
            if(mfi>=80)
                p.addSignal(StockSignal.mfi_over_bought_80);
            if(mfi<=30)
                p.addSignal(StockSignal.mfi_over_sold_30);
            if(mfi<=20)
                p.addSignal(StockSignal.mfi_over_sold_20);
        }
        //Log.info(Arrays.toString(macd));
        //Log.info(Arrays.toString(macdSignal));

    }
}
