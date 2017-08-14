package com.mu.stock.indicator.processor;

import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.entity.DailyPriceLog;
import com.mu.util.ReflectionUtil;
import com.mu.util.log.Log;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class SMACalc
{

    static public void reset(List<DailyPriceLog> list)
    {
        List<Method> setters = ReflectionUtil.getSetters(DailyPriceLog.class, "sma");
        for(int i=0; i<list.size(); i++)
        {
            for(Method g : setters)
            {
                try{
                g.invoke(list.get(i), 0F);
                }catch(Exception e){Log.error(e);}
            }
        }

    }
    static public void process(List<DailyPriceLog> list)
    {
        if(list==null || list.isEmpty())
            return;
        List<Method> getters = ReflectionUtil.getGetters(DailyPriceLog.class, "sma");
        HashMap<Method, Method> mtdMap = new HashMap<Method, Method>();
        HashMap<Method, Integer> paramMap = new HashMap<Method, Integer>();
        for(Method g : getters)
        {
            mtdMap.put(g, ReflectionUtil.getSetter(DailyPriceLog.class, g.getName()));
            paramMap.put(g, Integer.parseInt(g.getName().replaceAll("\\D", "")));
        }

        DailyPriceLog fd = list.get(0);
        for(Method g : getters)
        {
            try{
            Float f = (Float)g.invoke(fd);
            if(f == null || f.floatValue()<=0F)
            {
                mtdMap.get(g).invoke(fd, fd.getAdjClose());
            }
            }catch(Exception e){Log.error(e);}
        }

        for(int i=1; i<list.size(); i++)
        {
            DailyPriceLog d1 = list.get(i);
            DailyPriceLog d0 = list.get(i-1);
            for(Method g : getters)
            {
                try{
                Float f1 = (Float)g.invoke(d1);
                if(f1 == null || f1.floatValue()<=0F)
                {
                    Float f0 = (Float)g.invoke(d0);
                    int param = paramMap.get(g);
                    if(i<param)
                        f1 = (f0*i+d1.getAdjClose())/(i+1);
                    else
                        f1 = (f0*param-list.get(i-param).getAdjClose()+d1.getAdjClose())/param;
                    //Log.info("%d, %f", i, f1);
                    mtdMap.get(g).invoke(d1, f1);
                }
                }catch(Exception e){Log.error(e);}
            }
        }



    }
    static public float[] process(List<float[]> data, String params)
    {
        float[] list = data.get(0);
        float[] re = new float[list.length];
        re[0] = list[0];
        int param = Integer.valueOf(params);

        for(int i=1; i<list.length; i++)
        {
            float f1 = list[i];
            float r0 = re[i-1];
            try{
            if(i<param)
                re[i] = (r0*i+f1)/(i+1);
            else
                re[i] = (r0*param-list[i-param]+f1)/param;
            }catch(Exception e){Log.error(e);}
        }
        return re;

    }
}
