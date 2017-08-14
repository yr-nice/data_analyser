package com.mu.stock.indicator;

import com.mu.util.log.Log;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Peng Mu
 */
public class CandleStick
{
    private float diffHL;
    private float diffPerHL;
    private float diffCO;
    private float diffPerCO;
    private float upperLinePer;
    private float bodyPer;
    private float bodyLen;
    private float shadowLen;
    private float lowerLinePer;
    private CandleStickType type;
    private float open;
    private float close;
    private float high;
    private float low;
    public CandleStick(float open, float close, float high, float low)
    {
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        diffHL = high-low;
        diffCO = close-open;
        bodyLen = Math.abs(diffCO)*100/close;
        shadowLen = Math.abs(diffHL)*100/close;
        //Log.info("hl:%s, co=%s", diffHL, diffCO);
        if(Float.compare(open,0F)!=0)
        {
            
            diffPerHL = (diffHL*100)/open;
            diffPerCO = (diffCO*100)/open;
        }
        if(Float.compare(diffHL,0F)!=0)
        {
            upperLinePer = (high - Math.max(close,open))*100/diffHL;
            bodyPer = Math.abs(diffCO)*100/diffHL;
            lowerLinePer = 100 - upperLinePer - bodyPer;
        }
        //Log.info("o:%s, c:%s, h:%s, low:%s, hl:%s, hlper:%s, co=%s, coper=%s, upperLinePer=%s, bodyPer=%s", open, close, high, low, diffHL, diffPerHL, diffCO, diffPerCO, upperLinePer, bodyPer);
        checkType();
    }

    private void checkType()
    {
        if(Float.compare(diffHL,0F)==0)
            type = CandleStickType.flat;
        else if(upperLinePer <= 5 && bodyPer<=30 && bodyPer>5)
            type = CandleStickType.hammer;
        else if(lowerLinePer <= 5 && bodyPer<=30 && bodyPer>5)
            type = CandleStickType.inverted_hammer;
        else if(bodyPer<=5)
        {
            if(upperLinePer <= 5)
                type = CandleStickType.dragonfly_doji;
            else if(lowerLinePer <= 5)
                type = CandleStickType.gravestone_doji;
            else
                type = CandleStickType.doji;
        }
        else if(bodyPer>95 && diffCO>0)
            type = CandleStickType.white_marubozu;
        else if(bodyPer>95 && diffCO<0)
            type = CandleStickType.black_marubozu;
        else if(bodyLen>3 && diffCO>0 )
            type = CandleStickType.long_white;
        else if(bodyLen>3 && diffCO<0 )
            type = CandleStickType.long_black;
        else
            type = CandleStickType.spinning_top;
    }

    public float getBodyPer()
    {
        return bodyPer;
    }

    public float getDiffCO()
    {
        return diffCO;
    }

    public float getDiffHL()
    {
        return diffHL;
    }

    public float getDiffPerCO()
    {
        return diffPerCO;
    }

    public float getDiffPerHL()
    {
        return diffPerHL;
    }

    public float getUpperLinePer()
    {
        return upperLinePer;
    }

    public CandleStickType getType()
    {
        return type;
    }

    public float getLowerLinePer()
    {
        return lowerLinePer;
    }

    public boolean canEngulf(CandleStick c)
    {
        if(Math.max(close, open)> Math.max(c.getClose(), c.getOpen())
           && Math.min(close, open)< Math.min(c.getClose(), c.getOpen()))
            return true;
        return false;
    }

    public float getBodyLen()
    {
        return bodyLen;
    }

    public float getClose()
    {
        return close;
    }

    public float getHigh()
    {
        return high;
    }

    public float getLow()
    {
        return low;
    }

    public float getOpen()
    {
        return open;
    }

    public float getShadowLen()
    {
        return shadowLen;
    }

    public float getBodyBottom()
    {
        return Math.min(open, close);
    }
    public float getBodyTop()
    {
        return Math.max(open, close);
    }
    public float getBodyMid()
    {
        return (open+close)/2;
    }
    public boolean isWhite()
    {
        return diffCO>0;
    }

}
