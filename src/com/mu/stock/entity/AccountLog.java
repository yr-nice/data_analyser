package com.mu.stock.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Peng Mu
 */
@Entity
public class AccountLog
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade={})
    Account acct;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    private Float priceDrift;
    private Integer dateDrift;
    private Float capital;
    private Float cash;

    // 0      1          2           3    4
    //amount  avgPrice   curPrice
    private TreeMap<Company, ArrayList<Object> > portfolio = new TreeMap<Company, ArrayList<Object>>();
    @OneToMany()
    private List<TxnLog> txns = new ArrayList<TxnLog>();

    public Account getAcct()
    {
        return acct;
    }

    public void setAcct(Account acct)
    {
        this.acct = acct;
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
        return cash;
    }

    public void setCash(Float cash)
    {
        this.cash = cash;
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

    public Float getPriceDrift()
    {
        return priceDrift;
    }

    public TreeMap<Company, ArrayList<Object>> getPortfolio()
    {
        return portfolio;
    }

    public void setPortfolio(TreeMap<Company, ArrayList<Object>> portfolio)
    {
        this.portfolio.clear();
        this.portfolio.putAll(portfolio);
    }

    public void setPriceDrift(Float priceDrift)
    {
        this.priceDrift = priceDrift;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    public List<TxnLog> getTxns()
    {
        return txns;
    }

    public void setTxns(List<TxnLog> txns)
    {
        this.txns = txns;
    }

    public Float getPortfolioValue()
    {
        Float re = 0F;
        for(Company c : portfolio.keySet())
        {
            ArrayList<Object> a = portfolio.get(c);
            re += (Integer)a.get(0)*(Float)a.get(2);
        }
        return re;
    }
    public Float getTotalValue()
    {
        return cash + getPortfolioValue();
    }
    public Float getProfit()
    {
        return getTotalValue()-capital;
    }
    public Float getProfitPerc()
    {
        return (getTotalValue()-capital)*100/capital;
    }

}
