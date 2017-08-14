package com.mu.stock.strategy;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.Stock;
import com.mu.stock.indicator.analyser.StockSignal;
import com.mu.util.DateUtil;
import com.mu.util.log.Log;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Peng Mu
 */
public class BaseOnSignals
{
    public void applyTo(VirtualAccount acct, String stockCode, String startDay, String toDay, HashMap<StockSignal, Integer> signals, int minDayBetweenTxn)
    {
        Stock s = DataMgr.getStock(stockCode);
        startDay = s.getTxnDay(startDay);
        acct.moveForwardTo(startDay);
        LinkedList<Integer> boundedQueue = new LinkedList<Integer>(); //conside latest 3 days signals
        for(;acct.getCurDate().before(DateUtil.toDate(toDay)); acct.nextDay())
        {
            ProcessedDailyData p = s.getProcessedDataByDay(acct.getCurDateStr());
            if(p==null)
            {
                continue;
            }

            int i = 0;
            for(StockSignal sg : p.getSignals())
                if(signals.containsKey(sg))
                    i += signals.get(sg);
            int credit = getCredit(boundedQueue, i, 0.68F);

            if(credit>99)
            {
                Log.info("%s, Buy, credit, %d", p.getDate(), credit);
                int numOfSharesCanBuy = s.numOfSharesCanBuy(acct.getCurDateStr(), acct.getCash());
                acct.buy(stockCode, numOfSharesCanBuy);
                acct.moveForward(minDayBetweenTxn);
                continue;
            }
            if(credit<-99)
            {
                Integer numOfSharesCanSell = acct.getPortfolio().get(stockCode);
                if(numOfSharesCanSell == null || numOfSharesCanSell == 0)
                    continue;
                //Log.info("Last txn profit %.2f%%", acct.getProfitPerByStock(stockCode));
                Log.info("%s, Sell, credit, %d", p.getDate(), credit);
                acct.sell(stockCode, numOfSharesCanSell);
                acct.moveForward(minDayBetweenTxn);
                continue;
            }
        }
    }

    private int getCredit(LinkedList<Integer> boundedQueue, int todayCredit, float multifier)
    {
        int re=todayCredit;
        if(boundedQueue.size()>=3)
            boundedQueue.poll();
        boundedQueue.add(todayCredit);
        int size = boundedQueue.size();
        if(size>1)
        {
            for(int i=size-2; i>=0; i-- )
            {
                re += boundedQueue.get(i)*multifier;
                multifier *=multifier;
            }
        }
        return re;
    }
}
