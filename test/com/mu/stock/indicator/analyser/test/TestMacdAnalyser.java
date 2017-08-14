package com.mu.stock.indicator.analyser.test;

import com.mu.stock.data.DataMgr;
import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.Stock;
import com.mu.stock.indicator.analyser.MacdAnalyser;
import com.mu.stock.indicator.analyser.StockSignal;
import com.mu.util.log.Log;
import java.util.Arrays;

/**
 *
 * @author Peng Mu
 */
public class TestMacdAnalyser
{

    static public void main(String[] argu)
    {
        Stock s = DataMgr.getStock("DBS_D05");
        MacdAnalyser.detectSignal(s);
        for(ProcessedDailyData p : s.getPriceHistory())
        {
            //||p.hasSignal(StockSignal.peak_60_days)
            if(p.hasSignal(StockSignal.bottom_60_days))
                Log.info("%s, %s, price:%.2f, VMA10:%.2f, SMA10:%.2f, SMA200:%.2f, SMA10-200:%.4f, %s",
                    p.getDate(), p.filterSingleSignal("zone"), p.getAdjClose(),s.getAverPriceByVol(p.getDateStr(), 10), p.getSma10(), p.getSma200(), p.getSma10()-p.getSma200(), Arrays.toString(p.getSignals().toArray()));
             /**/
            /*if(p.hasSignal(StockSignal.williams_over_sold_more) && p.hasSignal(StockSignal.stochastic_over_sold) && p.hasSignal(StockSignal.rsi_over_sold_30))
                Log.info("%s, %.2f, %s", p.getDate(), p.getAdjClose(), Arrays.toString(p.getSignals().toArray()));
             
            if(p.hasSignal(StockSignal.bottom_60_days) || p.hasSignal(StockSignal.peak_60_days) )
                Log.info("%s, %.2f, %s", p.getDate(), p.getAdjClose(), Arrays.toString(p.getSignals().toArray()));
            
            float vma=s.getAverPriceByVol(p.getDate(), 20);
            float pri = p.getAdjClose();
            if(pri<=vma*0.96||pri>=vma*1.05)
                Log.info("%s, %s, price:%.2f, VMA Scope:%.2f~%.2f,VMA10:%.2f, SMA10:%.2f, SMA200:%.2f, SMA10-200:%.4f",
                    p.getDate(), p.filterSingleSignal("zone"), p.getAdjClose(),vma*0.96, vma*1.05, vma, p.getSma10(), p.getSma200(), p.getSma10()-p.getSma200());
            */
        }
        
        /* for(String k : s.getLocalMin().navigableKeySet())
            Log.info("LOCAL MIN, %s, %.2f", k, s.getLocalMin().get(k).getAdjClose());
        for(String k : s.getLocalMax().navigableKeySet())
            Log.info("LOCAL Max, %s, %.2f", k, s.getLocalMax().get(k).getAdjClose());

        String date = "2008-06-10";
        ProcessedDailyData p = s.getProcessedDataByDay(date);
        Log.info("%s, current:%.2f, support: %.2f, resistance: %.2f", date, p.getAdjClose(), s.getSupportPrice(date, 10), s.getResistancePrice(date, 10));

       
        for(int i=0; i<10; i++)
        {
            String date = RandomUtil.pickDate("2008-03-01", "2010-10-01");
            ProcessedDailyData p = s.getProcessedDataByDay(date);
            if(p==null)
                continue;
            Log.info("%s, current:%.2f, support: %.2f, resistance: %.2f", date, p.getAdjClose(), s.getSupportPrice(date, 60), s.getResistancePrice(date, 60));
        }
         * 
         */
    }

}
