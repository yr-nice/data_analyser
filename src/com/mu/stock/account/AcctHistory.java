/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mu.stock.account;

/**
 *
 * @author peng mu
 */
public class AcctHistory
{

    private String date;
    private float capital;
    private float cash;
    private float paperValue;

    public AcctHistory(String date, float capital, float cash, float paperValue)
    {
        this.date = date;
        this.capital = capital;
        this.cash = cash;
        this.paperValue = paperValue;
    }

    public float getCapital()
    {
        return capital;
    }

    public float getCash()
    {
        return cash;
    }

    public String getDate()
    {
        return date;
    }

    public float getPaperValue()
    {
        return paperValue;
    }

    public float getTotalValue()
    {
        return paperValue+cash;
    }

    public float getProfit()
    {
        return getTotalValue()-capital;
    }

    public float getProfitPercent()
    {
        return getProfit()*100/capital;
    }

    public String toString()
    {
        return String.format("%s, cash:%.2f, paperValue:%.2f, total:%.2f, capital:%.2f, profit:%.2f, percent:%.2f%%", date, cash, paperValue, getTotalValue(), capital, getProfit(), getProfitPercent());
    }
}
