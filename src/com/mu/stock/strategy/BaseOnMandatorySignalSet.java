package com.mu.stock.strategy;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.Stock;
import com.mu.stock.indicator.analyser.StockSignal;
import com.mu.util.DateUtil;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class BaseOnMandatorySignalSet
{
    public void applyTo(VirtualAccount acct, String stockCode, String startDay, String toDay, Collection<StockSignal> buySignals, Collection<StockSignal> sellSignals, int minDayBetweenTxn)
    {
        Stock s = DataMgr.getStock(stockCode);
        startDay = s.getTxnDay(startDay);
        acct.moveForwardTo(startDay);
        for(;acct.getCurDate().before(DateUtil.toDate(toDay)); acct.nextDay())
        {
            ProcessedDailyData p = s.getProcessedDataByDay(acct.getCurDateStr());
            if(p==null)
            {
                continue;
            }

            if(p.hasAllSignals(buySignals))
            {
                int numOfSharesCanBuy = s.numOfSharesCanBuy(acct.getCurDateStr(), acct.getCash());
                acct.buy(stockCode, numOfSharesCanBuy);
                acct.moveForward(minDayBetweenTxn);
                continue;
            }
            if(p.hasAllSignals(sellSignals))
            {
                Integer numOfSharesCanSell = acct.getPortfolio().get(stockCode);
                if(numOfSharesCanSell == null || numOfSharesCanSell == 0)
                    continue;
                //Log.info("Last txn profit %.2f%%", acct.getProfitPerByStock(stockCode));
                //Log.info("%s, Sell, credit, %d", p.getDate(), credit);
                acct.sell(stockCode, numOfSharesCanSell);
                acct.moveForward(minDayBetweenTxn);
                continue;
            }
        }
    }

}
