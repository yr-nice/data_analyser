package com.mu.stock.entity;

import com.mu.util.DateUtil;
import com.mu.util.MathUtil;
import com.mu.util.log.Log;
import java.util.Date;
import javax.jdo.annotations.Index;
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
public class DailyPriceLog
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private long id;

    @Index
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private String dateStr;
    private Float open;
    private Float close;
    private Float adjClose;
    private Float high;
    private Float low;
    private Long volume;
    private Float sma30;
    private Float sma72;
    private Float sma5;
    private Float sma10;
    private Float sma50;
    private Float sma200;
    private Float changePer;
    private Float volRatio;

    @ManyToOne
    @Index
    private Company company;

    public DailyPriceLog()
    {
    }
    public DailyPriceLog(String[] arr)
    {
        try{
        dateStr = arr[0];
        date = DateUtil.timestampToDate(dateStr + " 17:20:00");
        open = Float.parseFloat(arr[1]);
        high = Float.parseFloat(arr[2]);
        low = Float.parseFloat(arr[3]);
        close = Float.parseFloat(arr[4]);
        volume = Long.parseLong(arr[5]);
        adjClose = Float.parseFloat(arr[6]);
        }catch(Exception e){Log.error(e);}
    }

    public Company getCompany()
    {
        return company;
    }

    public void setCompany(Company company)
    {
        this.company = company;
    }

    public Float getAdjClose()
    {
        return adjClose;
    }

    public void setAdjClose(Float adjClose)
    {
        this.adjClose = adjClose;
    }

    public Float getAdjHigh()
    {
        float r = close==0?1:(adjClose/close);
        return MathUtil.round(high*r, 3);
    }

    public Float getAdjLow()
    {
        float r = close==0?1:(adjClose/close);
        return MathUtil.round(low*r, 3);
    }

    public Float getAdjOpen()
    {
        float r = close==0?1:(adjClose/close);
        return MathUtil.round(open*r, 3);
    }

    public Float getClose()
    {
        return close;
    }

    public void setClose(Float close)
    {
        this.close = close;
    }

    public String getDateStr()
    {
        return dateStr;
    }

    public void setDateStr(String dateStr)
    {
        this.dateStr = dateStr;
    }

    public Float getHigh()
    {
        return high;
    }

    public void setHigh(Float high)
    {
        this.high = high;
    }

    public Float getLow()
    {
        return low;
    }

    public void setLow(Float low)
    {
        this.low = low;
    }

    public Float getOpen()
    {
        return open;
    }

    public void setOpen(Float open)
    {
        this.open = open;
    }

    public Long getVolume()
    {
        return volume;
    }

    public void setVolume(Long volume)
    {
        this.volume = volume;
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

    public Float getSma30()
    {
        return sma30;
    }

    public void setSma30(Float sma30)
    {
        this.sma30 = sma30;
    }

    public Float getSma72()
    {
        return sma72;
    }

    public void setSma72(Float sma72)
    {
        this.sma72 = sma72;
    }

    public Float getSma10()
    {
        return sma10;
    }

    public void setSma10(Float sma10)
    {
        this.sma10 = sma10;
    }

    public Float getSma200()
    {
        return sma200;
    }

    public void setSma200(Float sma200)
    {
        this.sma200 = sma200;
    }

    public Float getSma5()
    {
        return sma5;
    }

    public void setSma5(Float sma5)
    {
        this.sma5 = sma5;
    }

    public Float getSma50()
    {
        return sma50;
    }

    public void setSma50(Float sma50)
    {
        this.sma50 = sma50;
    }

    public Float getHLYGravity()
    {
        if(sma30==null || sma72==null)
            return null;
        return MathUtil.round((sma30 + sma72)/2, 3);
    }

    public Float getChangePer()
    {
        return changePer;
    }

    public void setChangePer(Float changePer)
    {
        this.changePer = changePer;
    }

    public String toString()
    {
        return dateStr;
    }

    public Float getVolRatio()
    {
        return volRatio;
    }

    public void setVolRatio(Float volRatio)
    {
        this.volRatio = volRatio;
    }



}
