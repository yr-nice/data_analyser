package com.mu.stock.entity;

import com.mu.stock.dao.AccountDAO;
import com.mu.stock.dao.DailyPriceDAO;
import com.mu.util.MathUtil;
import com.mu.util.TimeConstants;
import com.mu.util.log.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Peng Mu
 */
@Entity
public class Account
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    private String strategy;
    private Float priceDrift;
    private Integer dateDrift;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date currentDate;

    @OneToMany
    private List<AccountLog> acctLogs = new ArrayList<AccountLog>();;
    private List<TxnLog> pendingTxn = new ArrayList<TxnLog>();
    private List<TxnLog> settledTxn = new ArrayList<TxnLog>();
    // 0      1          2           3    4
    //amount  avgPrice   curPrice
    private TreeMap<Company, ArrayList<Object> > portfolio = new TreeMap<Company, ArrayList<Object>>();
    private Float cash;
    private Float capital;

    public List<AccountLog> getAcctLogs()
    {
        return acctLogs;
    }

    public void setAcctLogs(List<AccountLog> acctLogs)
    {
        this.acctLogs = acctLogs;
    }

    public Float getCapital()
    {
        return capital;
    }

    public void setCapital(Float capital)
    {
        this.capital = capital;
    }

    public Float getCash()
    {
        return MathUtil.round(cash,2);
    }

    public void setCash(Float cash)
    {
        this.cash = cash;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getCurrentDate()
    {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate)
    {
        this.currentDate = currentDate;
    }

    public Integer getDateDrift()
    {
        return dateDrift;
    }

    public void setDateDrift(Integer dateDrift)
    {
        this.dateDrift = dateDrift;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<TxnLog> getPendingTxn()
    {
        return pendingTxn;
    }

    public void setPendingTxn(List<TxnLog> pendingTxn)
    {
        this.pendingTxn = pendingTxn;
    }

    public TreeMap<Company, ArrayList<Object>> getPortfolio()
    {
        return portfolio;
    }

    public void setPortfolio(TreeMap<Company, ArrayList<Object>> portfolio)
    {
        this.portfolio = portfolio;
    }

    public Float getPriceDrift()
    {
        return priceDrift;
    }

    public void setPriceDrift(Float priceDrift)
    {
        this.priceDrift = priceDrift;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public String getStrategy()
    {
        return strategy;
    }

    public void setStrategy(String strategy)
    {
        this.strategy = strategy;
    }

    public List<TxnLog> getSettledTxn()
    {
        return settledTxn;
    }

    public void setSettledTxn(List<TxnLog> settledTxn)
    {
        this.settledTxn = settledTxn;
    }

    public void submitTxn(Company c, String action, int amount, float price)
    {
        TxnLog txn = new TxnLog(c, amount, price, action);
        txn.setSubmitDate(currentDate);
        pendingTxn.add(txn);
        AccountDAO.insert(txn);
        AccountDAO.update(this);

    }
    public void withdrawTxn(TxnLog txn)
    {
        for(TxnLog t : pendingTxn)
            if(t.getId()==txn.getId())
            {
                settleTxn(t, TxnLog.Canceled);
            }
        for(TxnLog t : settledTxn)
            pendingTxn.remove(t);
    }

    public void settleTxns()
    {
        long l = System.currentTimeMillis();
        for(TxnLog txn : pendingTxn)
        {
            Company c = txn.getCompany();
            DailyPriceLog d = DailyPriceDAO.getLogByDate(c, currentDate);
            if(d==null)
                continue;
            float p = txn.getPrice().floatValue();
            float ch = cash.floatValue();
            int a = txn.getAmount();
            float t = txn.getTotalValue();
            if(txn.getAction().equals(TxnLog.Buy))
            {
                if(p>=d.getAdjLow()*priceDrift)
                {
                    if(ch >= t + getBrokerFee(t))
                    {
                        cash = ch - t - getBrokerFee(t);
                        updatePortfolio(c, a, (t + getBrokerFee(t)),d.getAdjClose()*priceDrift);
                        settleTxn(txn, TxnLog.Done);
                    }
                    else
                        settleTxn(txn, TxnLog.Failed);
                }
            }
            else
            {
                if(p<=d.getAdjHigh()*priceDrift)
                {
                    if(portfolio.containsKey(c))
                    {
                        int i = (Integer)portfolio.get(c).get(0);
                        if(i>=a)
                        {
                            cash = ch + txn.getTotalValue() - getBrokerFee(txn.getTotalValue());
                            updatePortfolio(c, (-1)*a, 0, d.getAdjClose()*priceDrift);
                            settleTxn(txn, TxnLog.Done);
                        }
                        else
                            settleTxn(txn, TxnLog.Failed);
                    }
                }
            }
        }
        for(TxnLog t : settledTxn)
            pendingTxn.remove(t);
        l = Log.interval("settle txn", l);
        AccountDAO.update(this);
        l = Log.interval("update Acct", l);
    }
    private float getBrokerFee(float txnValue)
    {
        float i = txnValue * 0.0035F;
        if(i < 27)
            return 27;
        return i;
    }
    public void settleTxn(TxnLog txn, String status)
    {
        txn.setDate(currentDate);
        txn.setStatus(status);
        //pendingTxn.remove(txn);
        settledTxn.add(txn);
    }
    public void updatePortfolio(Company c, int amount, float dealValue, float curPrice)
    {
        ArrayList<Object> a = portfolio.get(c);;
        if(a==null)
        {
            a = new ArrayList<Object>();
            Collections.addAll(a, Integer.valueOf(0), Float.valueOf(0F), Float.valueOf(0F));
            portfolio.put(c, a);
        }
        Integer balAmt = amount+(Integer)a.get(0);
        if(balAmt.intValue()==0)
            portfolio.remove(c);
        else
        {
            if(amount>0)
            {
                float avgPri = ((Float)a.get(1)*(Integer)a.get(0) + dealValue)/((Integer)a.get(0)+amount);
                a.set(1, avgPri);
            }
            a.set(0, amount+(Integer)a.get(0));
            a.set(2, curPrice);
        }

    }
    public void updateDailyPrice()
    {
        for(Company c : portfolio.keySet())
        {
            DailyPriceLog d = DailyPriceDAO.getLogByDate(c, currentDate);
            if(d==null)
                continue;
            portfolio.get(c).set(2, d.getAdjClose()*priceDrift);
        }
    }
    public AccountLog getTodayLog()
    {
        AccountLog re = new AccountLog();
        re.setAcct(this);
        re.setCapital(capital);
        re.setCash(cash);
        re.setDateDrift(dateDrift);
        re.setPriceDrift(priceDrift);
        re.setPortfolio(portfolio);
        re.setTime(currentDate);
        re.getTxns().addAll(settledTxn);
        for(TxnLog t :settledTxn)
            t.setAcctLog(re);
        return re;
    }
    public void settleToday()
    {
        AccountLog re = getTodayLog();
        AccountDAO.insert(re);
        acctLogs.add(re);
        settledTxn.clear();
        AccountDAO.update(this);
    }

    public Float getPortfolioValue()
    {
        Float re = 0F;
        for(Company c : portfolio.keySet())
        {
            ArrayList<Object> a = portfolio.get(c);
            re += (Integer)a.get(0)*(Float)a.get(2);
        }
        return MathUtil.round(re,2);
    }
    public Float getTotalValue()
    {
        return MathUtil.round(cash + getPortfolioValue(),2);
    }
    public Float getProfit()
    {
        return MathUtil.round(getTotalValue()-capital,2);
    }
    public Float getProfitPerc()
    {
        return MathUtil.round((getTotalValue()-capital)*100/capital, 2);
    }
    public long getDuration()
    {
        return (currentDate.getTime() - startDate.getTime())/TimeConstants.MILLIS_IN_A_DAY;
    }
    public String toString()
    {
        return name;
    }
}
