package com.mu.stock.indicator.processor;

import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class EMACalc
{
    static public float[] process(List<float[]> data, String params)
    {
        float[] list = data.get(0);
        float[] re = new float[list.length];
        int param = Integer.valueOf(params);
        float multi = 2F/(param+1);
        re[0]  = list[0];
        for(int i=1; i<re.length; i++)
        {
            re[i] = list[i]*multi + re[i-1]*(1-multi);
        }

        return re;
    }

}
