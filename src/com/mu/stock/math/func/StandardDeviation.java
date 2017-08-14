package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class StandardDeviation extends PostfixMathCommand
{
    public StandardDeviation()
    {
        numberOfParameters = 2;
    }
    public void run(Stack inStack) throws ParseException
    {
        checkStack(inStack);
        try{
        int o = ((Double)inStack.pop()).intValue();
        float[] arr = (float[])inStack.pop();
        float[] re = process(arr, o);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }

    static float[] process(float[] arr, int dateNum)
    {
        float[] re = new float[arr.length];
        float[] sma = SMA.process(arr, dateNum);
        float[] d = ArrSubtract.process(arr, sma);
        float[] dp = ArrPow.process(d, 2d);
        float[] smaDp = SMA.process(dp, dateNum);
        re = ArrPow.process(smaDp, 0.5d);
        return re;
    }

}

