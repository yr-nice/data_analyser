package com.mu.stock.dao.test;

import com.mu.db.jpa.dao.BaseEmbeddedDAO;
import com.mu.stock.dao.AccountDAO;
import com.mu.stock.dao.CompanyDAO;
import com.mu.stock.dao.DailyPriceDAO;
import com.mu.stock.entity.Account;
import com.mu.stock.entity.AccountLog;
import com.mu.stock.entity.Company;
import com.mu.stock.entity.DailyPriceLog;
import com.mu.stock.entity.TxnLog;
import com.mu.stock.txn.Market;
import com.mu.util.DateUtil;
import com.mu.util.log.Log;

/**
 *
 * @author Peng Mu
 */
public class AcctDAOTest
{
    static public void main(String[] argu)
    {
        BaseEmbeddedDAO.init("stock.odb");

        AcctDAOTest ins = new AcctDAOTest();
        String acctName = "test2";
        ins.testCreate(acctName);
        //ins.testBuy(acctName);
        //ins.testSell(acctName);
        //ins.testSaveAcctLog(acctName);
        ins.testNextDay(acctName, 1);
        //ins.testGetLogByDate();
        //ins.removeAll();
        //ins.testCreateFixed(acctName);
    }
    private void testCreate(String acctName)
    {
        AccountDAO.createAcct("test2", 100000F, "2005-01-01", "2010-01-01", "");
    }
    private void testCreateFixed(String acctName)
    {
        Account acct = AccountDAO.createAcct(acctName, 100000F, "2005-01-03", "2005-01-03", "");
        acct.setDateDrift(0);
        acct.setPriceDrift(1F);
        AccountDAO.update(acct);
    }

    private void testSaveAcctLog(String acctName)
    {
        Account acct = AccountDAO.getByName(acctName).get(0);
        AccountLog log = acct.getTodayLog();
        AccountDAO.insert(log);
    }

    private void testBuy(String acctName)
    {
        Account acct = AccountDAO.getByName(acctName).get(0);
        Company c = CompanyDAO.getOrCreate("D05", "sgx", null);
        acct.submitTxn(c, TxnLog.Buy, 100, 18);

    }
    private void testSell(String acctName)
    {
        Account acct = AccountDAO.getByName(acctName).get(0);
        Company c = CompanyDAO.getOrCreate("D05", "sgx", null);
        acct.submitTxn(c, TxnLog.Sell, 100, 6);

    }

    private void testNextDay(String acctName, int i)
    {
        Log.info("testNextDay");
        long l = System.currentTimeMillis();
        l = Log.interval("now", l);
        Account acct = AccountDAO.getByName(acctName).get(0);
        for(int a =0; a<i; a++)
            Market.nextDay(acct);
        l = Log.interval("after", l);

    }
    private void testGetLogByDate()
    {
        long l = System.currentTimeMillis();
        Company c = CompanyDAO.getOrCreate("D05", "sgx", null);
        DailyPriceLog d = DailyPriceDAO.getLogByDate(c, DateUtil.toDate("2005-02-03"));


        l = Log.interval("get log by day", l);
        d = DailyPriceDAO.getLogByDate(c, DateUtil.toDate("2005-02-11"));
        l = Log.interval("get log by day", l);

    }
    private void removeAll()
    {
        AccountDAO.removeAll(TxnLog.class);
        AccountDAO.removeAll(AccountLog.class);
        AccountDAO.removeAll(Account.class);
    }

}
