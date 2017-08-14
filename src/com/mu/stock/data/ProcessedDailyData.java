package com.mu.stock.data;

import com.mu.stock.entity.DailyPriceLog;
import com.mu.stock.indicator.CandleStick;
import com.mu.stock.indicator.PriceTrend;
import com.mu.stock.indicator.analyser.StockSignal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Peng Mu
 */
public class ProcessedDailyData extends DailyPriceLog
{
    private CandleStick candle;
    private float priceTrendAngle;
    private float macd;
    private float macdSignal;
    private float momentum;
    private float roc;
    private long obv;
    private float mfi;
    private float stochastic;
    private float stochasticSignal;
    private float williams;
    private float rsi;
    private HashMap<String, Float> ext = new HashMap<String, Float>();
    private HashSet<StockSignal> signals = new HashSet<StockSignal>();

    public ProcessedDailyData(String[] argu)
    {
        super(argu);
        candle = new CandleStick(this.getOpen(), this.getClose(), this.getHigh(), this.getLow());
    }

    public CandleStick getCandle()
    {
        return candle;
    }

    public void setCandle(CandleStick candle)
    {
        this.candle = candle;
    }

    public HashMap<String, Float> getExt()
    {
        return ext;
    }

    public void setExt(HashMap<String, Float> ext)
    {
        this.ext = ext;
    }

    public float getMacd()
    {
        return macd;
    }

    public void setMacd(float macd)
    {
        this.macd = macd;
    }

    public float getMacdSignal()
    {
        return macdSignal;
    }

    public void setMacdSignal(float macdSignal)
    {
        this.macdSignal = macdSignal;
    }

    public float getMfi()
    {
        return mfi;
    }

    public void setMfi(float mfi)
    {
        this.mfi = mfi;
    }

    public float getMomentum()
    {
        return momentum;
    }

    public void setMomentum(float momentum)
    {
        this.momentum = momentum;
    }

    public long getObv()
    {
        return obv;
    }

    public void setObv(long obv)
    {
        this.obv = obv;
    }

    public float getPriceTrendAngle()
    {
        return priceTrendAngle;
    }

    public void setPriceTrendAngle(float priceTrendAngle)
    {
        this.priceTrendAngle = priceTrendAngle;
    }

    public float getRoc()
    {
        return roc;
    }

    public void setRoc(float roc)
    {
        this.roc = roc;
    }

    public float getStochastic()
    {
        return stochastic;
    }

    public void setStochastic(float stochastic)
    {
        this.stochastic = stochastic;
    }

    public float getWilliams()
    {
        return williams;
    }

    public void setWilliams(float williams)
    {
        this.williams = williams;
    }

    public float getRsi()
    {
        return rsi;
    }

    public void setRsi(float rsi)
    {
        this.rsi = rsi;
    }

    public float getStochasticSignal()
    {
        return stochasticSignal;
    }

    public void setStochasticSignal(float stochasticSignal)
    {
        this.stochasticSignal = stochasticSignal;
    }

    public HashSet<StockSignal> getSignals()
    {
        return signals;
    }

    public void addSignal(StockSignal s)
    {
        signals.add(s);
    }

    public boolean hasSignal(StockSignal s)
    {
        return signals.contains(s);
    }

    public boolean hasAllSignals(Collection<StockSignal> s)
    {
        return signals.containsAll(s);
    }

    public boolean hasAnySignalOf(Collection<StockSignal> s)
    {
        for(StockSignal ss : s)
            if(hasSignal(ss))
                return true;
        return false;
    }

    public HashSet<StockSignal> filterSignal(String like)
    {
        HashSet<StockSignal> re = new HashSet<StockSignal>();
        for(StockSignal ss : this.signals)
            if(ss.toString().toLowerCase().matches(".*"+like.toLowerCase()+".*"))
                re.add(ss);
        return re;
    }
    public StockSignal filterSingleSignal(String like)
    {
        for(StockSignal ss : this.signals)
            if(ss.toString().toLowerCase().matches(".*"+like.toLowerCase()+".*"))
                return ss;
        return null;
    }


    public boolean hasSignal()
    {
        return signals.size()>0;
    }

    public PriceTrend getPriceTrend()
    {
        if(priceTrendAngle>10)
            return PriceTrend.up;
        if(priceTrendAngle<-10)
            return PriceTrend.down;
        return PriceTrend.flat;
    }


}
