package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ADL extends PostfixMathCommand
{
    public ADL()
    {
        numberOfParameters = 4;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        float[] v = (float[])inStack.pop();
        float[] c = (float[])inStack.pop();
        float[] l = (float[])inStack.pop();
        float[] h = (float[])inStack.pop();
        float[] re = process(h,l,c, v);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] high, float[] low, float[] close, float[] vol)
    {
        float[] adl = new float[high.length];
        if(high[0]==low[0])
            adl[0]=0;
        else
            adl[0]=vol[0]*(2*close[0]-high[0]-low[0])/(high[0]-low[0]);
        for(int i=1; i<high.length; i++)
        {
            float multi = 0;
            if(high[i]!=low[i])
                multi = (2*close[i]-high[i]-low[i])/(high[i]-low[i]);
            adl[i] = adl[i-1] + vol[i]*multi;
        }
        return adl;
    }
}

