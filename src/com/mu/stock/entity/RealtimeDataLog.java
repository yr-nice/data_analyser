package com.mu.stock.entity;

import com.mu.util.DateUtil;
import com.mu.util.log.Log;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Peng Mu
 */
//{ID:0,N:'2ndChance',NC:'528',R:'NONE',I:'NONE',M:'-',LT:0.400,C:0.005,VL:197.000,BV:3.000,
// B:0.400,S:0.405,SV:293.000,O:0.395,H:0.400,L:0.395,V:78795.000,SC:'4',PV:0.395,P:1.26582275390625,P_:'X',V_:''}
@Entity
public class RealtimeDataLog
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String code;
    private float last;
    private float change;
    private float open;
    private float high;
    private float low;
    private float vol;
    private float buyVol;
    private float sellVol;
    private float buyPri;
    private float sellPri;
    private float value;
    private float prevClose;
    private String sector;
    private String origStr="";
    private String remarks;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    public RealtimeDataLog()
    {

    }

    public RealtimeDataLog(String str)
    {
        str = str.replaceAll("'NONE'", "");
        origStr = str.replaceAll("[\\{\\}']", "");
        Log.info(origStr);
        String[] arr = origStr.split(",");
        for(String s : arr)
        {
            String [] kv = s.split(":");
            if(kv.length<2) continue;
            String k = kv[0];
            String v = kv[1];
            if(k.equals("N")) name = v;
            else if(k.equals("NC")) code = v;
            else if(k.equals("R")) remarks = v;
            else if(k.equals("LT")) last = Float.parseFloat(v);
            else if(k.equals("C")) change = Float.parseFloat(v);
            else if(k.equals("VL")) vol = Float.parseFloat(v);
            else if(k.equals("BV")) buyVol = Float.parseFloat(v);
            else if(k.equals("B")) buyPri = Float.parseFloat(v);
            else if(k.equals("S")) sellPri = Float.parseFloat(v);
            else if(k.equals("SV")) sellVol  = Float.parseFloat(v);
            else if(k.equals("O")) open = Float.parseFloat(v);
            else if(k.equals("H")) high = Float.parseFloat(v);
            else if(k.equals("L")) low = Float.parseFloat(v);
            else if(k.equals("V")) value = Float.parseFloat(v);
            else if(k.equals("PV")) prevClose = Float.parseFloat(v);
            else if(k.equals("SC")) sector = v;

        }
    }


    public float getBuyPri()
    {
        return buyPri;
    }

    public void setBuyPri(float buyPri)
    {
        this.buyPri = buyPri;
    }

    public float getBuyVol()
    {
        return buyVol;
    }

    public void setBuyVol(float buyVol)
    {
        this.buyVol = buyVol;
    }

    public float getChange()
    {
        return change;
    }

    public void setChange(float change)
    {
        this.change = change;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public float getHigh()
    {
        return high;
    }

    public void setHigh(float high)
    {
        this.high = high;
    }

    public float getLow()
    {
        return low;
    }

    public void setLow(float low)
    {
        this.low = low;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public float getLast()
    {
        return last;
    }

    public void setLast(float now)
    {
        this.last = now;
    }

    public float getOpen()
    {
        return open;
    }

    public void setOpen(float open)
    {
        this.open = open;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getSector()
    {
        return sector;
    }

    public void setSector(String sector)
    {
        this.sector = sector;
    }

    public float getSellPri()
    {
        return sellPri;
    }

    public void setSellPri(float sellPri)
    {
        this.sellPri = sellPri;
    }

    public float getSellVol()
    {
        return sellVol;
    }

    public void setSellVol(float sellVol)
    {
        this.sellVol = sellVol;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public float getValue()
    {
        return value;
    }

    public void setValue(float value)
    {
        this.value = value;
    }

    public float getVol()
    {
        return vol;
    }

    public void setVol(float vol)
    {
        this.vol = vol;
    }

    public String getOrigStr()
    {
        return origStr;
    }

    public void setOrigStr(String origStr)
    {
        this.origStr = origStr;
    }

    public float getChangePerc()
    {
        if(last==0F)
            return 0F;
        return change*100/last;
    }

    public float getPrevClose()
    {
        return prevClose;
    }

    public void setPrevClose(float prevClose)
    {
        this.prevClose = prevClose;
    }


    public boolean equals(RealtimeDataLog o )
    {
        return origStr.equals(o.getOrigStr());
    }


    public String[] toArray()
    {
        String[] re = new String[19];
        re[0] = name;
        re[1] = code;
        re[2] = last+"";
        re[3] = change+"";
        re[4] = getChangePerc() + "";
        re[5] = vol+"";
        re[6] = buyVol +"";
        re[7] = buyPri +"";
        re[8] = sellPri +"";
        re[9] = sellVol +"";
        re[10] = open +"";
        re[11] = high +"";
        re[12] = low +"";
        re[13] = prevClose +"";
        re[14] = value +"";
        re[15] = sector;
        re[16] = remarks;
        re[17] = DateUtil.dateToTimestamp(timestamp);

        return re;
    }

}
