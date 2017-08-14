package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class OBVCalc
{
    static public float[] process(List<float[]> data, String params)
    {
        float[] pList = data.get(0);
        float[] vList = data.get(1);
        float[] re = new float[vList.length];
        re[0]=0;
        for(int i=1; i<vList.length; i++)
        {
            float p1 = pList[i];
            float p0 = pList[i-1];
            int a = p1>p0? 1:-1;
            re[i] = re[i-1]+ a*vList[i];
        }
        return re;
    }

}
