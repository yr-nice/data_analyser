package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class DEMA extends PostfixMathCommand
{
    public DEMA()
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
    static public float[] process(float[] list, int dayNum)
    {
        float[] ema = EMA.process(list, dayNum);
        float[] ee = EMA.process(ema, dayNum);
        float[] e2 = ArrMulti.process(ema, 2d);
        float[] re = ArrSubtract.process(e2, ee);
        return re;
    }
}

