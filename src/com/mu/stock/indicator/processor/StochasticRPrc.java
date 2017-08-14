package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import com.mu.util.log.Log;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class StochasticRPrc
{
    static public void process(List<ProcessedDailyData> list,  int dateNum, int smoothDate, int signalDateNum)
    {
        setStochastic(list, dateNum, smoothDate, signalDateNum);
    }

    static private void setStochastic(List<ProcessedDailyData> list, int dateNum, int smoothDate, int signalDateNum)
    {
        float[] arr = new float[list.size()];
        for(int i=dateNum; i<list.size(); i++)
        {
            float h = 0;
            float l = 100000;
            for(int j=i-dateNum+1; j<=i; j++)
            {
                float hn = list.get(j).getHigh();
                float ln = list.get(j).getLow();
                h = hn > h ? hn : h;
                l = ln < l ? ln :l;
            }
            //float c = list.get(i).getClose();
            float c = list.get(i).getAdjClose();
            float f = 100*(c-l)/(h-l);
            arr[i] = f;
            list.get(i).setStochastic(f);
        }

        for(int i=dateNum+smoothDate; i<list.size(); i++)
        {
            float sum = 0;
            for(int j=i-smoothDate+1; j<=i; j++)
            {
                sum += arr[j];
            }
            list.get(i).setStochastic(sum/smoothDate);
        }

        for(int i=dateNum+smoothDate+signalDateNum; i<list.size(); i++)
        {
            float sum = 0;
            for(int j=i-signalDateNum+1; j<=i; j++)
            {
                sum += list.get(j).getStochastic();
            }
            list.get(i).setStochasticSignal(sum/signalDateNum);
        }
    }

}
