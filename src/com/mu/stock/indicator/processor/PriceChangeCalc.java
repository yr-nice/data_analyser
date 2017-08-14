package com.mu.stock.indicator.processor;

import com.mu.stock.entity.DailyPriceLog;
import com.mu.util.MathUtil;
import com.mu.util.log.Log;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class PriceChangeCalc
{

    static public void reset(List<DailyPriceLog> list)
    {
        for(int i=0; i<list.size(); i++)
        {
            list.get(i).setChangePer(null);
        }

    }
    static public void process(List<DailyPriceLog> list)
    {
        if(list==null || list.isEmpty())
            return;

        DailyPriceLog fd = list.get(0);
        if(fd.getChangePer() != null)
            fd.setChangePer(0F);

        for(int i=1; i<list.size(); i++)
        {
            DailyPriceLog d1 = list.get(i);
            if(d1.getChangePer() != null)
                continue;
            DailyPriceLog d0 = list.get(i-1);

            try{
                float close1 = d1.getAdjClose();
                float close0 = d0.getAdjClose();
                float delta = close1 - close0;
                list.get(i).setChangePer(MathUtil.round(delta*100/close0, 1));
                }catch(Exception e){Log.error(e);}
            }
        }
}
