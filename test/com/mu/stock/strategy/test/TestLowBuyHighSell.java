package com.mu.stock.strategy.test;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.account.analyser.AcctAnalyser;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.Stock;
import com.mu.stock.indicator.analyser.StockSignal;
import com.mu.stock.strategy.LowBuyHighSell;
import com.mu.util.DateUtil;
import com.mu.util.RandomUtil;
import com.mu.util.log.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Peng Mu
 */
public class TestLowBuyHighSell
{
  
    static public void main(String[] argu)
    {
        
        tryRandomly(10, "5");
        /*
        VirtualAccount va = new VirtualAccount(100000F, 0F, DateUtil.toDate("2004-01-01"));
        String[] date = RandomUtil.pickDate("2005-01-01", "2010-10-20", 30*72);
        trySingle(va, "DBS_D05", date[0], date[1], 1);
        */
    }

    static public void tryRandomly(int num, String subFolder)
    {
        File folder = new File("historical_data");
        HashMap<String, VirtualAccount> res = new HashMap<String, VirtualAccount>();
        ArrayList<Stock> stocks = DataMgr.getRandomStocks(num, subFolder);
        for(Stock s : stocks)
        {
            VirtualAccount va = new VirtualAccount(50000F, 0F, DateUtil.toDate("2004-01-01"));
            String[] date = RandomUtil.pickDate("2005-01-01", "2010-10-20", 30*72);
            res.put(String.format("%s, from %s to %s", s.getCode(), date[0], date[1]),
                //trySingle(va, code, date[0], date[1],  StockSignal.macd_signal_cross_x_down, StockSignal.macd_signal_cross_x_up, 6));
                trySingle(va, s.getCode(), date[0], date[1], 1));
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
    static public VirtualAccount trySingle(VirtualAccount va, String stockCode, String startDate, String endDate, int minDayBetweenTxn)
    {
        //VirtualAccount va = new VirtualAccount(5000F, 3000F, DateUtil.toDate("2004-01-01"));
        LowBuyHighSell bh = new LowBuyHighSell();
        bh.applyTo(va, stockCode, startDate,  endDate, minDayBetweenTxn);
        AcctAnalyser aa = new AcctAnalyser(va.getAcctHist());
        Log.info("%s ~ %s, last:%.2f, Average:%.2f, Highest:%.2f, Lowest:%.2f, Mid:%.2f, Positve:%d days, Negative:%d days\n",
            startDate, endDate, aa.getLatestProfitPer(), aa.getAverProfitPer(), aa.getHighestProfitPer(), aa.getLowestProfitPer(), aa.getMidProfitPer(), aa.getPositiveDays(), aa.getNegativeDays());
        return va;
    }
}
