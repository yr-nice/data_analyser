package com.mu.stock.strategy.test;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.account.analyser.AcctAnalyser;
import com.mu.stock.strategy.BuyMonthlyAndHold;
import com.mu.util.DateUtil;
import com.mu.util.RandomUtil;
import com.mu.util.log.Log;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Peng Mu
 */
public class TestBuyMonthlyAndHold
{
    
    static public void main(String[] argu)
    {
        //trySingle("KS_Energy_578");
        tryRandomly(50);
    }

    static public void tryRandomly(int num)
    {
        File folder = new File("historical_data");
        HashMap<String, VirtualAccount> res = new HashMap<String, VirtualAccount>();
        for(int i=0; i<num; i++)
        {
            String code = RandomUtil.pickFile(folder).getName().replace(".csv", "");
            String[] date = RandomUtil.pickDate("2004-01-01", "2010-10-20", 30*24);
            res.put(String.format("%s, from %s to %s", code, date[0], date[1]), trySingle(code, date[0], date[1]));
        }

        Log.info("Result:");
        int pos =0;
        int neg =0;
        for(String s : res.keySet())
        {
            VirtualAccount va = res.get(s);
            AcctAnalyser aa = new AcctAnalyser(va.getAcctHist());
            Log.info("%s, Now:%.2f, Average:%.2f, Highest:%.2f, Lowest:%.2f, Mid:%.2f, Positve:%d days, Negative:%d days",
                s, aa.getLatestProfitPer(), aa.getAverProfitPer(), aa.getHighestProfitPer(), aa.getLowestProfitPer(), aa.getMidProfitPer(), aa.getPositiveDays(), aa.getNegativeDays());
            if(aa.getLatestProfitPer()>0)
                pos++;
            else if(aa.getLatestProfitPer()<0)
                neg++;
        }
        Log.info("Total:%d, Positive:%d, Negative:%d", num, pos, neg);
    }
    static public VirtualAccount trySingle(String stockCode, String startDate, String endDate)
    {
        VirtualAccount va = new VirtualAccount(0F, 3000F, DateUtil.toDate("2004-01-01"));
        BuyMonthlyAndHold bh = new BuyMonthlyAndHold();
        bh.applyTo(va, stockCode, startDate,  endDate);
        /*for(AcctHistory a : va.getAcctHist())
            Log.info(a);
        for(StockTxn st : va.getTxnList())
            Log.info(st);*/
        AcctAnalyser aa = new AcctAnalyser(va.getAcctHist());
        Log.info("Now:%.2f, Average:%.2f, Highest:%.2f, Lowest:%.2f, Mid:%.2f, Positve:%d days, Negative:%d days\n",
            aa.getLatestProfitPer(), aa.getAverProfitPer(), aa.getHighestProfitPer(), aa.getLowestProfitPer(), aa.getMidProfitPer(), aa.getPositiveDays(), aa.getNegativeDays());
        return va;
    }

}
