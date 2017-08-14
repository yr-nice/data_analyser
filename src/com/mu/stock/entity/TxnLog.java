package com.mu.stock.entity;

import com.mu.util.DateUtil;
import com.mu.util.MathUtil;
import com.mu.util.log.Log;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Peng Mu
 */
@Entity
public class TxnLog
{
    private static final long serialVersionUID = 1L;
    public static String Buy = "buy";
    public static String Sell = "sell";
    public static String Canceled = "canceled";
    public static String Done = "done";
    public static String Failed = "faile";
    public static String Pending = "pending";
    @Id
    @GeneratedValue
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submitDate;

    @ManyToOne(cascade={})
    private Company company;
    @ManyToOne(cascade={})
    private AccountLog acctLog;
    private Integer amount;
    private Float price;
    private String action;
    private String status=Pending;


    public TxnLog()
    {
    }

    public TxnLog(Company c, Integer amount, Float price, String action)
    {
        this.company = c;
        this.amount = amount;
        this.price = price;
        this.action = action;
    }

    public AccountLog getAcctLog()
    {
        return acctLog;
    }

    public void setAcctLog(AccountLog acctLog)
    {
        this.acctLog = acctLog;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public Integer getAmount()
    {
        return amount;
    }

    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }

    public Company getCompany()
    {
        return company;
    }

    public void setCompany(Company company)
    {
        this.company = company;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Float getPrice()
    {
        return price;
    }

    public void setPrice(Float price)
    {
        this.price = price;
    }

    public Date getSubmitDate()
    {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate)
    {
        this.submitDate = submitDate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public float getTotalValue()
    {
        return amount*price;
    }


}
