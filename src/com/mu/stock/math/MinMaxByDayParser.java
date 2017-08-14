package com.mu.stock.math;

import java.lang.String;
import java.util.TreeMap;

/**
 *
 * @author Peng Mu
 */
public class MinMaxByDayParser
{
    private TreeMap<String, Float> data;
    private TreeMap<String, Float> localMin = new TreeMap<String, Float>();
    private TreeMap<String, Float> localMax = new TreeMap<String, Float>();
    public MinMaxByDayParser(TreeMap<String, Float> data)
    {
        this.data = data;
        if(data.size()<3)
            return;

        for(String k : data.navigableKeySet())
        {
            if(data.lowerEntry(k)==null || data.higherEntry(k)==null)
                continue;

            float left = data.lowerEntry(k).getValue();
            float right = data.higherEntry(k).getValue();
            float mid = data.get(k);
            if(mid<=left && mid<=right)
                localMin.put(k, mid);
            if(mid>=left && mid>=right)
                localMax.put(k, mid);
        }
    }

    public TreeMap<String, Float> getLocalMax()
    {
        return localMax;
    }

    public TreeMap<String, Float> getLocalMin()
    {
        return localMin;
    }

    public TreeMap<String, Float> getMaxTill(String date)
    {
        return localMax;
    }


}
