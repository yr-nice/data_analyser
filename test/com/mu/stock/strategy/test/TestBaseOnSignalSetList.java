package com.mu.stock.strategy.test;

import com.mu.stock.account.VirtualAccount;
import com.mu.stock.account.analyser.AcctAnalyser;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.Stock;
import com.mu.stock.indicator.analyser.StockSignal;
import com.mu.stock.strategy.BaseOnSignalSetList;
import com.mu.util.DateUtil;
import com.mu.util.RandomUtil;
import com.mu.util.log.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Peng Mu
 */
public class TestBaseOnSignalSetList
{
    static ArrayList<Collection<StockSignal>> buyList = new ArrayList<Collection<StockSignal>> ();
    static ArrayList<Collection<StockSignal>> sellList = new ArrayList<Collection<StockSignal>> ();

    static {
        HashSet<StockSignal> sell1 = new HashSet<StockSignal> ();
        HashSet<StockSignal> sell2 = new HashSet<StockSignal> ();
        HashSet<StockSignal> sell3 = new HashSet<StockSignal> ();
        HashSet<StockSignal> sell4 = new HashSet<StockSignal> ();
        HashSet<StockSignal> buy1 = new HashSet<StockSignal> ();
        HashSet<StockSignal> buy2 = new HashSet<StockSignal> ();
        HashSet<StockSignal> buy3 = new HashSet<StockSignal> ();
        
        buy1.add(StockSignal.sma_mid_zone_10_200);
        buy1.add(StockSignal.break_30_day_support);
        buy1.add(StockSignal.mfi_over_sold_30);
        buy1.add(StockSignal.stochastic_over_sold);
        buy1.add(StockSignal.williams_over_sold_more);

        buy2.add(StockSignal.sma_lower_zone_10_200);
        buy2.add(StockSignal.break_10_day_resistance);
        //buy2.add(StockSignal.stochastic_over_sold);
        //buy2.add(StockSignal.williams_over_sold_more);
        //buy2.add(StockSignal.roc_over_sold);
        //buy2.add(StockSignal.rsi_over_sold_30);

        buy3.add(StockSignal.sma_upper_zone_10_200);
        buy3.add(StockSignal.break_30_day_support);
        buy3.add(StockSignal.mfi_over_sold_30);
        buy3.add(StockSignal.stochastic_over_sold);
        buy3.add(StockSignal.williams_over_sold_more);

        sell1.add(StockSignal.sma_mid_zone_10_200);
        sell1.add(StockSignal.break_10_day_resistance);
        sell1.add(StockSignal.williams_over_bought_more);
        sell1.add(StockSignal.stochastic_over_bought);

        sell2.add(StockSignal.sma_lower_zone_10_200);
        sell2.add(StockSignal.mfi_over_bought_70);
        sell2.add(StockSignal.roc_over_bought);
        sell2.add(StockSignal.stochastic_over_bought);
        sell2.add(StockSignal.williams_over_bought_more);

        sell3.add(StockSignal.sma_upper_zone_10_200);
        sell3.add(StockSignal.williams_over_bought_more);
        sell3.add(StockSignal.rsi_over_bought_70);
        sell3.add(StockSignal.mfi_over_bought_70);
        sell3.add(StockSignal.break_180_day_resistance);

        sell4.add(StockSignal.sma_enter_lower_zone);
        //sell4.add(StockSignal.sma_mid_zone_10_200);

        buyList.add(buy1);
        buyList.add(buy2);
        buyList.add(buy3);
        sellList.add(sell1);
        //sellList.add(sell2);
        sellList.add(sell3);
        sellList.add(sell4);
        
        //signals.put(StockSignal.k_hammer, 100);
        //signals.put(StockSignal.k_inverted_hammer, 100);
        //signals.put(StockSignal.k_hanging_man, -100);
        //signals.put(StockSignal.k_shooting_star, -100);
        //signals.put(StockSignal.macd_cross_signal_down, 100);
        //signals.put(StockSignal.macd_cross_signal_up, -100);
        //signals.put(StockSignal.rsi_over_bought_70, -30);
        //signals.put(StockSignal.rsi_over_bought_80, -25);
        //signals.put(StockSignal.rsi_over_sold_30, 30);
        //signals.put(StockSignal.rsi_over_sold_20, 25);
        //signals.put(StockSignal.sma_10_cross_200_up, 100);
        //signals.put(StockSignal.sma_10_cross_200_down, -100);
        //signals.put(StockSignal.break_20_day_resistance, 100);
        //signals.put(StockSignal.break_20_day_support, -100);
    }

    static public void main(String[] argu)
    {
        
        tryRandomly(10, "5");
        /*
        VirtualAccount va = new VirtualAccount(50000F, 0F, DateUtil.toDate("2004-01-01"));
        String[] date = RandomUtil.pickDate("2005-01-01", "2010-10-20", 30*72);
        trySingle(va, "DBS_D05", date[0], date[1], 0);

         
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
            String[] date = RandomUtil.pickDate("2005-01-01", "2010-10-20", 30*36);
            res.put(String.format("%s, from %s to %s", s.getCode(), date[0], date[1]),
                //trySingle(va, code, date[0], date[1],  StockSignal.macd_signal_cross_x_down, StockSignal.macd_signal_cross_x_up, 6));
                trySingle(va, s.getCode(), date[0], date[1], 3));
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
        BaseOnSignalSetList bh = new BaseOnSignalSetList();
        bh.applyTo(va, stockCode, startDate,  endDate, buyList, sellList, minDayBetweenTxn);
        AcctAnalyser aa = new AcctAnalyser(va.getAcctHist());
        Log.info("%s ~ %s, last:%.2f, Average:%.2f, Highest:%.2f, Lowest:%.2f, Mid:%.2f, Positve:%d days, Negative:%d days\n",
            startDate, endDate, aa.getLatestProfitPer(), aa.getAverProfitPer(), aa.getHighestProfitPer(), aa.getLowestProfitPer(), aa.getMidProfitPer(), aa.getPositiveDays(), aa.getNegativeDays());
        return va;
    }
}
