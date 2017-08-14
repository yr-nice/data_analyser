/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mu.stock.account;

/**
 *
 * @author peng mu
 */
public class StockTxn
{

    private String date;
    private String stockCode;
    private int amount;
    private float price;

    public StockTxn(String date, String stockCode, int amount, float price)
    {
        this.date = date;
        this.stockCode = stockCode;
        this.amount = amount;
        this.price = price;
    }

    public int getAmount()
    {
        return amount;
    }

    public String getDate()
    {
        return date;
    }

    public float getPrice()
    {
        return price;
    }

    public String getStockCode()
    {
        return stockCode;
    }

    public String getAction()
    {
        return amount>0?"buy":"sell";
    }
    public float getTotal()
    {
        return Math.abs(amount)*price;
    }

    public String toString()
    {
        return String.format("%s, %s %s %d shares at %.2f, total %.2f", date, getAction(), stockCode, Math.abs(amount), price, getTotal());
    }
}
