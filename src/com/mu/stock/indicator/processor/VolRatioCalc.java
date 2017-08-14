package com.mu.stock.indicator.processor;

import com.mu.stock.entity.DailyPriceLog;
import com.mu.util.MathUtil;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class VolRatioCalc
{

    static public void reset(List<DailyPriceLog> list)
    {
        for(DailyPriceLog d : list)
        {
            d.setVolRatio(null);
        }

    }
    static public void process(List<DailyPriceLog> list)
    {
        if(list==null || list.isEmpty())
            return;
        DailyPriceLog fd = list.get(0);
        if(fd.getChangePer() != null)
            fd.setChangePer(0F);

        for(int i=5; i<list.size(); i++)
        {
            DailyPriceLog d1 = list.get(i);
            if(d1.getVolRatio()!=null)
                continue;

            float smaLast5 = 0;
            for(int j=1; j<=5; j++)
                smaLast5 += list.get(i-j).getVolume();
            smaLast5 /=5;
            d1.setVolRatio(MathUtil.round(d1.getVolume()/smaLast5, 2));

        }
    }

}
