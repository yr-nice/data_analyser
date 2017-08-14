package com.mu.stock.strategy;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.Stock;
import com.mu.stock.indicator.analyser.StockSignal;
import com.mu.util.DateUtil;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Peng Mu
 */
public class LowBuyHighSell
{
    public void applyTo(VirtualAccount acct, String stockCode, String startDay, String toDay,int minDayBetweenTxn)
    {
        Stock s = DataMgr.getStock(stockCode);
        startDay = s.getTxnDay(startDay);
        acct.moveForwardTo(startDay);
        LinkedList<Float> buyBenchMarkPrice = new LinkedList<Float>();
        LinkedList<Float> sellBenchMarkPrice = new LinkedList<Float>();
        for(;acct.getCurDate().before(DateUtil.toDate(toDay)); acct.nextDay())
        {
            ProcessedDailyData p = s.getProcessedDataByDay(acct.getCurDateStr());
            if(p==null)
            {
                continue;
            }

            if(p.hasSignal(StockSignal.sma_lower_zone_10_200))
            {
                if(buyBenchMarkPrice.size()==0)
                    buyBenchMarkPrice.add(p.getSma10());

                if(p.getAdjClose() <= buyBenchMarkPrice.getLast()*0.93)
                {
                    float lotPri = s.getAdjClose(acct.getCurDateStr());
                    int numOfSharesCanBuy =0;
                    for(int i=1; i<=buyBenchMarkPrice.size()&&i*lotPri*1000<acct.getCash(); i++)
                        numOfSharesCanBuy = i*1000;

                    buyBenchMarkPrice.add(p.getAdjClose());
                    acct.buy(stockCode, numOfSharesCanBuy);
                    acct.moveForward(minDayBetweenTxn);
                    continue;
                }

            }


            if(p.hasSignal(StockSignal.sma_upper_zone_10_200))
            {
                if(sellBenchMarkPrice.size()==0)
                    sellBenchMarkPrice.add(p.getSma10());

                if(p.getAdjClose() >= sellBenchMarkPrice.getLast()*1.03)
                {
                    Integer hold = acct.getPortfolio().get(stockCode);
                    if(hold == null || hold == 0)
                        continue;

                    int numOfSharesCanSell =hold<sellBenchMarkPrice.size()*1000? hold:sellBenchMarkPrice.size();
                    numOfSharesCanSell*=1000;
                    sellBenchMarkPrice.add(p.getAdjClose());
                    acct.sell(stockCode, numOfSharesCanSell);
                    acct.moveForward(minDayBetweenTxn);
                    continue;
                }

            }

        }
    }

}
