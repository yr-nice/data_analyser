package com.mu.stock.strategy.test;

import com.mu.stock.account.StockTxn;
import com.mu.stock.account.VirtualAccount;
import com.mu.stock.account.analyser.AcctAnalyser;
import com.mu.stock.strategy.BuyAndHold;
import com.mu.util.DateUtil;
import com.mu.util.RandomUtil;
import com.mu.util.log.Log;
import java.io.File;

/**
 *
 * @author Peng Mu
 */
public class TestBuyAndHold
{
    static public void main(String[] argu)
    {
        File folder = new File("historical_data");
        for(int i=0; i<10; i++)
            TestBuyAndHold.tryDbs(RandomUtil.pickFile(folder).getName().replace(".csv", ""));
    }

    static public void tryDbs(String stockCode)
    {
        VirtualAccount va = new VirtualAccount(50000F, 0F, DateUtil.toDate("2004-01-01"));
        BuyAndHold bh = new BuyAndHold();
        bh.applyTo(va, stockCode, "2004-05-05", "2010-10-10");
        /*for(AcctHistory a : va.getAcctHist())
            Log.info(a);
        for(StockTxn st : va.getTxnList())
            Log.info(st);*/
        AcctAnalyser aa = new AcctAnalyser(va.getAcctHist());
        Log.info("Now:%.2f, Average:%.2f, Highest:%.2f, Lowest:%.2f, Mid:%.2f, Positve:%d days, Negative:%d days\n",
            aa.getLatestProfitPer(), aa.getAverProfitPer(), aa.getHighestProfitPer(), aa.getLowestProfitPer(), aa.getMidProfitPer(), aa.getPositiveDays(), aa.getNegativeDays());
    }

}
