package com.mu.stock.strategy;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.Stock;

/**
 *
 * @author Peng Mu
 */
public class BuyAndHold
{
    public void applyTo(VirtualAccount acct, String stockCode, String buyDay, String holdToDay)
    {
        Stock s = DataMgr.getStock(stockCode);
        buyDay = s.getTxnDay(buyDay);
        acct.moveForwardTo(buyDay);
        int numOfSharesCanBuy = s.numOfSharesCanBuy(buyDay, acct.getCash());
        acct.buy(stockCode, numOfSharesCanBuy);
        acct.moveForwardTo(holdToDay);
    }
}
