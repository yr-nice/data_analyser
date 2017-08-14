package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class Trix extends PostfixMathCommand
{
    public Trix()
    {
        numberOfParameters = 2;
    }
    public void run(Stack inStack) throws ParseException
    {
        checkStack(inStack);
        try{
        Double o = (Double)inStack.pop();
        int i = o.intValue();
        float[] arr = (float[])inStack.pop();
        float[] re = process((float[])arr, i);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] list, int params)
    {
        float[] re = new float[list.length];
        float[] ema1 = EMA.process(list, params);
        float[] ema2 = EMA.process(ema1, params);
        float[] ema3 = EMA.process(ema2, params);

        float multi = 2F/(params+1);
        re[0]  = 0;
        for(int i=1; i<re.length; i++)
        {
            if(ema3[i]==0)
                re[i]= re[i-1];
            else
                re[i] = 100*(ema3[i]-ema3[i-1])/ema3[i];
        }

        return re;
    }
}

