/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mu.stock.account;

import com.mu.stock.data.DataMgr;
import com.mu.stock.data.Stock;
import com.mu.util.DateUtil;
import com.mu.util.log.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author peng mu
 */
public class VirtualAccount
{
    private float capital;
    private float cash;
    private float monthlyIncome;
    private Date curDate;
    private HashMap<String, Integer> portfolio = new HashMap<String, Integer>();
    private ArrayList<AcctHistory> acctHist = new ArrayList<AcctHistory>();
    private ArrayList<StockTxn> txnList = new ArrayList<StockTxn>();
    private int bulletSize = 20000;
    

    public VirtualAccount(float capital, float monthlyIncome, Date startDate)
    {
        this.capital = capital;
        cash = capital;
        this.monthlyIncome = monthlyIncome;
        this.curDate = startDate;
    }

    public ArrayList<AcctHistory> getAcctHist() {
        return acctHist;
    }
    public AcctHistory getLatestHist()
    {
        return acctHist.get(acctHist.size()-1);
    }

    public float getCapital() {
        return capital;
    }

    public float getCash() {
        return cash;
    }

    public float getMonthlyIncome() {
        return monthlyIncome;
    }

    public HashMap<String, Integer> getPortfolio() {
        return portfolio;
    }

    public Date getCurDate() {
        return curDate;
    }
    public String getCurDateStr() {
        return DateUtil.toDateStr(curDate);
    }

    public ArrayList<StockTxn> getTxnList() {
        return txnList;
    }

    public boolean buy(String stockCode, int amount)
    {
        if(amount<=0)
            return false;
        Stock s = DataMgr.getStock(stockCode);
        if(s == null)
        {
            Log.error("Stock not exist, %s", stockCode);
            return false;
        }
        String dateStr = DateUtil.toDateStr(curDate);
        float p = s.getAdjClose(dateStr);
        if(p<0)
        {
            Log.error("No Close Price at %s", dateStr);
            return false;
        }

        float b = p*amount;
        if(b>cash)
        {
            Log.error("Insufficient cash to finish txn. Requires %s, available %s", b, cash);
            return false;
        }

        //perform txn
        cash -= b;
        int ownedAmount = 0;
        if(portfolio.containsKey(stockCode))
            ownedAmount = portfolio.get(stockCode);
        ownedAmount += amount;
        portfolio.put(stockCode, ownedAmount);
        StockTxn st = new StockTxn(dateStr, stockCode, amount, p);
        txnList.add(st);
        Log.info("%s, Bought %d %s shares at %.2f. Total cost: %.2f, available cash: %.2f", dateStr,amount, stockCode, p, b, cash);
        return true;
    }

    public boolean sell(String stockCode, int amount)
    {
        if(amount<=0)
            return false;
        Stock s = DataMgr.getStock(stockCode);
        if(s == null)
        {
            Log.error("Stock not exist, %s", stockCode);
            return false;
        }
        String dateStr = DateUtil.toDateStr(curDate);
        float p = s.getAdjClose(dateStr);
        if(p<0)
        {
            Log.error("No Close Price at %s", dateStr);
            return false;
        }
        if(!portfolio.containsKey(stockCode))
        {
            Log.error("Don't own stock %s", stockCode);
            return false;
        }


        int ownedAmount = portfolio.get(stockCode);
        float b = p*amount;
        if(ownedAmount<amount)
        {
            Log.error("Insufficient shares to sell. Requires %d, owned %d", amount, ownedAmount);
            return false;
        }

        //perform txn
        cash += b;
        ownedAmount -= amount;
        portfolio.put(stockCode, ownedAmount);
        StockTxn st = new StockTxn(dateStr, stockCode, amount*(-1), p);
        txnList.add(st);
        Log.info("%s, Sold %d %s shares at %.2f. Total gain: %.2f, available cash: %.2f", dateStr, amount, stockCode, p, b, cash);
        return true;
    }

    //create an AcctHistory base on current day price and set current day to next day.
    public void nextDay()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        //increase capital at the first day of the month
        if(c.get(Calendar.DAY_OF_MONTH)==1 && monthlyIncome>1)
        {
            capital += monthlyIncome;
            cash += monthlyIncome;
            //Log.info("After adding MonthlyIncome %.2f, cash:%.2f, capital:%.2f", monthlyIncome, cash, capital);
        }
        String dateStr = DateUtil.toDateStr(curDate);
        float paperVal = 0;
        for(String k : portfolio.keySet())
        {
            int a = portfolio.get(k);
            Stock s = DataMgr.getStock(k);
            float p = s.getAdjClose(dateStr);
            if(p>0)
                paperVal += a*p;
        }
        //if(paperVal<0.1 && acctHist.size()>0)
            //paperVal = acctHist.get(acctHist.size()-1).getPaperValue();
        AcctHistory ah = new AcctHistory(dateStr, capital, cash, paperVal);
        acctHist.add(ah);
        curDate = DateUtil.nextDay(curDate);

    }

    public void moveForwardTo(String date)
    {
        if(!DateUtil.isValidDateStr(date))
        {
            Log.error("Invalid date string, %s. Expected format is yyyy-mm-dd, like 2010-11-22", date);
            return;
        }
        String curDateStr = DateUtil.toDateStr(curDate);
        if(date.compareTo(curDateStr)<0)
        {
            Log.error("Input date %s is less than current date %s, can not move forward to", date, curDateStr);
            return;
        }
        if(date.compareTo(curDateStr)==0)
        {
            Log.error("Input date %s equals to current date %s, no need to move", date, curDateStr);
            return;
        }

        while(!date.equals(curDateStr))
        {
            nextDay();
            curDateStr = DateUtil.toDateStr(curDate);
        }

    }

    public void moveForward(int days)
    {
        for(int i=0; i<days; i++)
        {
            nextDay();
        }

    }

    public StockTxn getLastTxn(String stockCode)
    {
        for(int i=txnList.size()-1; i>=0; i--)
            if(txnList.get(i).getStockCode().equals(stockCode))
                return txnList.get(i);
        return null;

    }

    public float getProfitPerByStock(String stockCode)
    {
        StockTxn tx = getLastTxn(stockCode);
        if(tx == null || tx.getAmount()<0)
            return 0F;
        float spent = tx.getTotal();
        Stock s = DataMgr.getStock(stockCode);
        float paperVal = Math.abs(s.getAdjClose(getCurDateStr())*tx.getAmount());
        return (paperVal-spent)*100/spent;
    }

    public int getBulletSize()
    {
        return bulletSize;
    }

    public void setBulletSize(int bulletSize)
    {
        this.bulletSize = bulletSize;
    }



}
