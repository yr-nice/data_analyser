package com.mu.stock.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author Peng Mu
 */
@Entity
public class Company implements Serializable, Comparable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    private String shortName;
    private String engName;
    private String chsName;
    private String quote;
    private String stockExchange;
    private String yahooQuote;
    private String url;
    private String tags;

    private List<DailyPriceLog> dPrice;
    private List<RealtimeDataLog> rtPrice;

    public String getChsName()
    {
        return chsName;
    }

    public void setChsName(String chsName)
    {
        this.chsName = chsName;
    }

    public List<DailyPriceLog> getdPrice()
    {
        return dPrice;
    }

    public void setdPrice(List<DailyPriceLog> dPrice)
    {
        this.dPrice = dPrice;
    }

    public String getEngName()
    {
        return engName;
    }

    public void setEngName(String engName)
    {
        this.engName = engName;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getQuote()
    {
        return quote;
    }

    public void setQuote(String quote)
    {
        this.quote = quote;
    }

    public List<RealtimeDataLog> getRtPrice()
    {
        return rtPrice;
    }

    public void setRtPrice(List<RealtimeDataLog> rtPrice)
    {
        this.rtPrice = rtPrice;
    }

    public String getStockExchange()
    {
        return stockExchange;
    }

    public void setStockExchange(String stockExchange)
    {
        this.stockExchange = stockExchange;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getYahooQuote()
    {
        return yahooQuote;
    }

    public void setYahooQuote(String yahooQuote)
    {
        this.yahooQuote = yahooQuote;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String toString()
    {
        if(engName!=null && !engName.isEmpty())
            return engName;
        return shortName;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }
    public int compareTo(Object c)
    {
        return this.getShortName().compareTo(c.toString());
    }

}
