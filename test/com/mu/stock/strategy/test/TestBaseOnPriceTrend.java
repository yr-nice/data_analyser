package com.mu.stock.strategy.test;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.account.analyser.AcctAnalyser;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.Stock;
import com.mu.stock.strategy.BaseOnPriceTrend;
import com.mu.util.DateUtil;
import com.mu.util.RandomUtil;
import com.mu.util.log.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Peng Mu
 */
public class TestBaseOnPriceTrend
{
    static public void main(String[] argu)
    {
        //trySingle("KS_Energy_578");
        tryRandomly(20, "0.3");
    }

    static public void tryRandomly(int num, String subFolder)
    {
        HashMap<String, VirtualAccount> res = new HashMap<String, VirtualAccount>();
        ArrayList<Stock> stocks = DataMgr.getRandomStocks(num, subFolder);
        for(Stock s: stocks)
        {
            VirtualAccount va = new VirtualAccount(50000F, 0F, DateUtil.toDate("2004-01-01"));
            String[] date = RandomUtil.pickDate("2005-01-01", "2010-10-20", 30*24);
            res.put(String.format("%s, from %s to %s", s.getCode(), date[0], date[1]), trySingle(va, s.getCode(), date[0], date[1],10, -10,  3));
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
    static public VirtualAccount trySingle(VirtualAccount va, String stockCode, String startDate, String endDate, float buySignal, float sellSignal, int minDayBetweenTxn)
    {
        //VirtualAccount va = new VirtualAccount(5000F, 3000F, DateUtil.toDate("2004-01-01"));
        BaseOnPriceTrend bh = new BaseOnPriceTrend();
        bh.applyTo(va, stockCode, startDate,  endDate, buySignal, sellSignal, minDayBetweenTxn,-100F);
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
