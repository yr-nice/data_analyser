package com.mu.math.test;

import com.mu.stock.math.MinMaxByDayParser;
import com.mu.stock.data.DataMgr;
import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.Stock;
import com.mu.util.log.Log;
import java.util.TreeMap;

/**
 *
 * @author Peng Mu
 */
public class TestMinMaxByDayParser
{
    static public void main(String[] argu)
    {
        Stock s = DataMgr.getStock("DBS_D05");
        TreeMap<String, Float> data = new TreeMap<String, Float>();
        for(ProcessedDailyData p : s.getPriceHistory())
        {
            if(p.getSma200()>0)
                data.put(p.getDateStr(), p.getSma10()-p.getSma200());
        }
        MinMaxByDayParser mmp = new MinMaxByDayParser(data);

        TreeMap<String, Float> minMax = new TreeMap<String, Float>(mmp.getLocalMax());
        minMax.putAll(mmp.getLocalMin());
        for(String d : minMax.navigableKeySet())
        {
            if(mmp.getLocalMax().containsKey(d))
                Log.info("%s, %.4f, local Max", d, minMax.get(d));
            if(mmp.getLocalMin().containsKey(d))
                Log.info("%s, %.4f, local Min", d, minMax.get(d));
        }
        TreeMap<String, Float> localMax = mmp.getLocalMax();
        for(String d : localMax.navigableKeySet())
        {
            if(localMax.get(d)<0||localMax.lowerKey(d)==null)
                continue;
            if(localMax.get(d)<=localMax.lowerEntry(d).getValue())
                Log.info("%s, Second Max:%.4f, Max:%.4f", d, localMax.get(d), localMax.lowerEntry(d).getValue());
        }

        for(String d : localMax.navigableKeySet())
        {
            if(localMax.get(d)>0||localMax.lowerKey(d)==null)
                continue;
            if(localMax.get(d)>=localMax.lowerEntry(d).getValue())
                Log.info("%s, Second Min:%.4f, Min:%.4f", d, localMax.get(d), localMax.lowerEntry(d).getValue());
        }

    }
}
