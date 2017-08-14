package com.mu.stock.chart;

import com.mu.util.DateUtil;
import java.util.Date;

/**
 *
 * @author Peng Mu
 */
public class PointDiff
{
    private double p1;
    private double p2;
    private Date d1;
    private Date d2;
    private int txnDays;
    private PointDiff last;
    public PointDiff(double p1, Date d1, double p2, Date d2, int txnDays, PointDiff last)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.d1 = d1;
        this.d2 = d2;
        this.txnDays = txnDays;
        this.last = last;
    }
    public double getChange()
    {
        return p2-p1;
    }
    public double getChangeRate()
    {
        return (p2-p1)/p1*100;
    }
    public double getChangeRateSpeed()
    {
        return getChangeRate()/txnDays;
    }
    public double getChangeRateToLast()
    {
        if(last == null)
            return 0;
        return Math.abs((p2-p1)/last.getChange()*100);
    }

    public String toString()
    {
        String re = String.format("%s to %s\n$%.2f to $%.2f\nTxn days: %d\nPROC: %.2f%%, $%.3f\nROC Speed: %.3f\nPROC/Last: %.2f%%",
            DateUtil.toDateStr(d1),DateUtil.toDateStr(d2),p1,p2,txnDays,getChangeRate(),getChange(),getChangeRateSpeed(),getChangeRateToLast());
        return re;
    }

    public Date getD1()
    {
        return d1;
    }

    public void setD1(Date d1)
    {
        this.d1 = d1;
    }

    public Date getD2()
    {
        return d2;
    }

    public void setD2(Date d2)
    {
        this.d2 = d2;
    }

    public double getP1()
    {
        return p1;
    }

    public void setP1(double p1)
    {
        this.p1 = p1;
    }

    public double getP2()
    {
        return p2;
    }

    public void setP2(double p2)
    {
        this.p2 = p2;
    }

    public int getTxnDays()
    {
        return txnDays;
    }

    public void setTxnDays(int txnDays)
    {
        this.txnDays = txnDays;
    }


}
