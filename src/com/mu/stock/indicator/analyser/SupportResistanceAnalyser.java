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
public class SupportResistanceAnalyser
{
    static private int startIndex = 10;
    static public void detectSignal(Stock s)
    {
        LinkedList<ProcessedDailyData> list = s.getPriceHistory();
        if(list.size()<=startIndex)
            return ;

        for(ProcessedDailyData p : list)
        {
            float f = p.getAdjClose();
            String date = p.getDateStr();
            if(f > s.getResistancePrice(date, 10))
                p.addSignal(StockSignal.break_10_day_resistance);
            if(f > s.getResistancePrice(date, 20))
                p.addSignal(StockSignal.break_20_day_resistance);
            if(f > s.getResistancePrice(date, 30))
                p.addSignal(StockSignal.break_30_day_resistance);
            if(f > s.getResistancePrice(date, 60))
                p.addSignal(StockSignal.break_60_day_resistance);
            if(f > s.getResistancePrice(date, 90))
                p.addSignal(StockSignal.break_90_day_resistance);
            if(f > s.getResistancePrice(date, 180))
                p.addSignal(StockSignal.break_180_day_resistance);

            if(f < s.getSupportPrice(date, 10))
                p.addSignal(StockSignal.break_10_day_support);
            if(f < s.getSupportPrice(date, 20))
                p.addSignal(StockSignal.break_20_day_support);
            if(f < s.getSupportPrice(date, 30))
                p.addSignal(StockSignal.break_30_day_support);
            if(f < s.getSupportPrice(date, 60))
                p.addSignal(StockSignal.break_60_day_support);
            if(f < s.getSupportPrice(date, 90))
                p.addSignal(StockSignal.break_90_day_support);
            if(f < s.getSupportPrice(date, 180))
                p.addSignal(StockSignal.break_180_day_support);

        }
    }
}
