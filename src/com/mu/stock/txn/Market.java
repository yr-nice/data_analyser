package com.mu.stock.txn;

import com.mu.stock.dao.DailyPriceDAO;
import com.mu.stock.entity.Account;
import com.mu.stock.entity.Company;
import com.mu.stock.entity.DailyPriceLog;
import com.mu.stock.entity.TxnLog;
import com.mu.util.DateUtil;
import java.util.Date;

/**
 *
 * @author Peng Mu
 */
public class Market
{
    static public void nextDay(Account acct)
    {
        acct.settleToday();
        Date nextDay = DateUtil.nextWeekday(acct.getCurrentDate());
        nextDay = DateUtil.getDayEnd(nextDay);
        acct.setCurrentDate(nextDay);
        acct.settleTxns();
        acct.updateDailyPrice();
    }
}
