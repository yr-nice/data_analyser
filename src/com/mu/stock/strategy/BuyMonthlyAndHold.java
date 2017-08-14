package com.mu.stock.strategy;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.Stock;
import com.mu.util.DateUtil;

/**
 *
 * @author Peng Mu
 */
public class BuyMonthlyAndHold
{
    public void applyTo(VirtualAccount acct, String stockCode, String buyDay, String holdToDay)
    {
        Stock s = DataMgr.getStock(stockCode);
        while(buyDay.compareTo(holdToDay)<0)
        {
            buyDay = s.getTxnDay(buyDay);
            acct.moveForwardTo(buyDay);
            int numOfSharesCanBuy = s.numOfSharesCanBuy(buyDay, acct.getCash());
            acct.buy(stockCode, numOfSharesCanBuy);
            buyDay = DateUtil.nextMonthStr(buyDay);
        }
        acct.moveForwardTo(holdToDay);
    }
}
