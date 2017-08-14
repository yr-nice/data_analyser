package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import com.mu.util.log.Log;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class PriceTrendAnglePrc
{
    static public void process(List<ProcessedDailyData> list, int averDateNum, int emaNum)
    {
        float[] ema = getEMAPri(list, emaNum);
        for(int i=averDateNum+emaNum; i<list.size(); i++)
        {
            ProcessedDailyData p1 = list.get(i);
            //Log.info("processing %s", p1.getDate());
            ProcessedDailyData p2 = list.get(i-averDateNum);
            float diff = ema[i]-ema[i-averDateNum];
            float diffPer = diff/ema[i-averDateNum];
            float tan = diffPer/averDateNum;
            p1.setPriceTrendAngle(Float.valueOf(Math.atan(tan*100)/Math.PI*180 +""));
            //p1.setPriceTrendAngle(tan*100);
        }
    }

    static private float[] getEMAPri(List<ProcessedDailyData> list, int dateNum)
    {
        float[] re = new float[list.size()];
        float multi = 2F/(dateNum+1);
        re[0]  = list.get(0).getAdjClose();
        for(int i=1; i<re.length; i++)
        {
            re[i] = list.get(i).getAdjClose()*multi + re[i-1]*(1-multi);
        }

        return re;
    }
}
