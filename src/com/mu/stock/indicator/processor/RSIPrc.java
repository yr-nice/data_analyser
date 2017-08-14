package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class RSIPrc
{
    static public void process(List<ProcessedDailyData> list,  int dateNum)
    {
        float[] rsi = getRSI(list, dateNum);

        for(int i=0; i<list.size(); i++)
        {
            ProcessedDailyData p = list.get(i);
            p.setRsi(rsi[i]);
        }

    }

    static private float[] getRSI(List<ProcessedDailyData> list, int dateNum)
    {
        float[] re = new float[list.size()];
        float[] up = new float[list.size()];
        float[] down = new float[list.size()];
        float multi = 2F/(dateNum+1);
        re[0]  = 50;
        up[0]  = 0;
        down[0]  = 0;
        for(int i=1; i<re.length; i++)
        {
            float t = list.get(i).getAdjClose()- list.get(i-1).getAdjClose();
            if(t>0)
            {
                up[i]=t;
                down[i]=0;
            }
            else
            {
                down[i]=Math.abs(t);
                up[i]=0;
            }
        }

        float emaUp[] =  new float[list.size()];
        float emaDown[] =  new float[list.size()];
        emaUp[0] = 0;
        emaDown[0] =0;
        for(int i=1; i<re.length; i++)
        {
            //emaUp[i] = up[i]*multi + emaUp[i-1]*(1-multi);
            //emaDown[i] = down[i]*multi + emaDown[i-1]*(1-multi);
            emaUp[i] = (up[i] + emaUp[i-1]*(dateNum-1))/dateNum;
            emaDown[i] = (down[i] + emaDown[i-1]*(dateNum-1))/dateNum;
            float rs = emaUp[i]/emaDown[i];
            re[i] = 100 - 100/(1+rs);
        }

        return re;
    }

}
