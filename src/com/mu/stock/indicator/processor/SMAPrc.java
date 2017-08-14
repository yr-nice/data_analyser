package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class SMAPrc
{
    static public void process(List<ProcessedDailyData> list)
    {

        if(list.size()<=200)
            return ;
        float sum200 = 0;
        for(int i=0; i<200; i++)
            sum200 += list.get(i).getAdjClose();
        ProcessedDailyData p = list.get(199);
        p.setSma200(sum200/200);

        float sum10 = 0;
        for(int i=190; i<200; i++)
            sum10 += list.get(i).getAdjClose();
        p.setSma10(sum10/10);

        float sum50 = 0;
        for(int i=150; i<200; i++)
            sum50 += list.get(i).getAdjClose();
        p.setSma50(sum50/50);


        for(int i=200; i<list.size(); i++)
        {
            p = list.get(i);
            ProcessedDailyData p0 = list.get(i-1);
            
            /*p.setSma10(p0.getSma10()*0.9F + p.getAdjClose()*0.1F);
            p.setSma50(p0.getSma50()*0.98F + p.getAdjClose()*0.02F);
            p.setSma200(p0.getSma200()*0.995F + p.getAdjClose()*0.005F);
            */
            p.setSma10((p0.getSma10()*10 + p.getAdjClose() - list.get(i-10).getAdjClose())/10);
            p.setSma50((p0.getSma50()*50 + p.getAdjClose() - list.get(i-50).getAdjClose())/50F);
            p.setSma200((p0.getSma200()*200 + p.getAdjClose() - list.get(i-200).getAdjClose())/200F);
            /*
            p.setSma10(getSMA(list, i, 10));
            p.setSma50(getSMA(list, i, 50));
            p.setSma200(getSMA(list, i, 200));
            */
        }
    }

}
