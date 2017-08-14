package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ChaikinV extends PostfixMathCommand
{
    public ChaikinV()
    {
        numberOfParameters = 4;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int c = ((Double)inStack.pop()).intValue();
        int s = ((Double)inStack.pop()).intValue();
        float[] l = (float[])inStack.pop();
        float[] h = (float[])inStack.pop();
        float[] re = process(h, l, s, c);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] high, float[] low, int smoothDate, int compareTo)
    {
        float[] re = new float[high.length];
        float[] hl = ArrSubtract.process(high, low);
        float[] ema = EMA.process(hl, smoothDate);
        for(int i=compareTo; i<high.length; i++)
        {
            float c = ema[i] - ema[i-compareTo];
            float l = ema[i-compareTo];
            if(l==0)
                re[i] = re[i-1];
            else
                re[i] = 100*c/l;
        }

        return SMA.process(re, smoothDate);
    }
}

