package com.mu.stock.strategy;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.Stock;
import com.mu.util.DateUtil;
import com.mu.util.log.Log;

/**
 *
 * @author Peng Mu
 */
public class BaseOnPriceTrend
{
    public void applyTo(VirtualAccount acct, String stockCode, String startDay, String toDay, float buySignal, float sellSignal, int minDayBetweenTxn, float lossLimit)
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
            //Log.info(p.getPriceTrendAngle());

            if(p.getPriceTrendAngle()>=buySignal)
            {
                int numOfSharesCanBuy = s.numOfSharesCanBuy(acct.getCurDateStr(), acct.getCash());
                acct.buy(stockCode, numOfSharesCanBuy);
                acct.moveForward(minDayBetweenTxn);
                continue;
            }
            if(p.getPriceTrendAngle()<=sellSignal || acct.getProfitPerByStock(stockCode)<=lossLimit)
            {
                Integer numOfSharesCanSell = acct.getPortfolio().get(stockCode);
                if(numOfSharesCanSell == null || numOfSharesCanSell == 0)
                    continue;
                Log.info("Last txn profit %.2f%%", acct.getProfitPerByStock(stockCode));
                acct.sell(stockCode, numOfSharesCanSell);
                acct.moveForward(minDayBetweenTxn);
                continue;
            }
        }
    }
}