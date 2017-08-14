package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ATR extends PostfixMathCommand
{
    public ATR()
    {
        numberOfParameters = 4;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int d = ((Double)inStack.pop()).intValue();
        float[] c = (float[])inStack.pop();
        float[] l = (float[])inStack.pop();
        float[] h = (float[])inStack.pop();
        float[] re = process(h, l, c, d);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] high, float[] low, float[] close, int dateNum)
    {
        float[] re = new float[close.length];
        re[0] = high[0] - low[0];
        for(int i=1; i<close.length; i++)
        {
            float tr0 = high[i] - low[i];
            float tr1 = Math.abs(high[i] - close[i-1]);
            float tr2 = Math.abs(low[i] - close[i-1]);
            float tr = Math.max(Math.max(tr0, tr1), tr2);
            re[i] = (re[i-1]*(dateNum - 1) + tr)/dateNum;
        }
        return re;
    }
}

