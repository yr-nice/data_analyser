package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ChaikinO extends PostfixMathCommand
{
    public ChaikinO()
    {
        numberOfParameters = 6;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int ld = ((Double)inStack.pop()).intValue();
        int sd = ((Double)inStack.pop()).intValue();
        float[] v = (float[])inStack.pop();
        float[] c = (float[])inStack.pop();
        float[] l = (float[])inStack.pop();
        float[] h = (float[])inStack.pop();
        float[] re = process(h,l,c, v, sd, ld);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] high, float[] low, float[] close, float[] vol, int sDay, int lDay)
    {
        float[] re = new float[high.length];
        float[] adl = ADL.process(high, low, close, vol);
        re = ArrSubtract.process(EMA.process(adl, sDay), EMA.process(adl, lDay));
        return re;
    }
}

