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
public class SMAAnalyser
{
    static private int startIndex = 201;
    static public void detectSignal(Stock s)
    {
        LinkedList<ProcessedDailyData> list = s.getPriceHistory();
        if(list.size()<=startIndex)
            return ;

        for(int i=startIndex; i<list.size(); i++)
        {
            ProcessedDailyData p = list.get(i);
            ProcessedDailyData p0 = list.get(i-1);
            float diff = p.getSma10() - p.getSma200();
            float diff0 = p0.getSma10() - p0.getSma200();
            if(diff>=0 && diff0<=0)
                p.addSignal(StockSignal.sma_10_cross_200_up);
            else if(diff <= 0 && diff0 >= 0)
                p.addSignal(StockSignal.sma_10_cross_200_down);

            if(p.getSma10() > p.getSma200()*1.05)
                p.addSignal(StockSignal.sma_upper_zone_10_200);
            else if(p.getSma10() < p.getSma200()*0.965)
                p.addSignal(StockSignal.sma_lower_zone_10_200);
            else
                p.addSignal(StockSignal.sma_mid_zone_10_200);

            if(p0.hasSignal(StockSignal.sma_mid_zone_10_200) && p.hasSignal(StockSignal.sma_lower_zone_10_200))
                p.addSignal(StockSignal.sma_enter_lower_zone);
            if(p0.hasSignal(StockSignal.sma_mid_zone_10_200) && p.hasSignal(StockSignal.sma_upper_zone_10_200))
                p.addSignal(StockSignal.sma_enter_upper_zone);

        }
    }
}
