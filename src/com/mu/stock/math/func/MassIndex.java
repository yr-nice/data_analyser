package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class MassIndex extends PostfixMathCommand
{
    public MassIndex()
    {
        numberOfParameters = 3;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int d = ((Double)inStack.pop()).intValue();
        float[] l = (float[])inStack.pop();
        float[] h = (float[])inStack.pop();
        float[] re = process(h,l,d);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] high, float[] low, int dayNum)
    {
        float[] s = ArrSubtract.process(high, low);
        float[] ema = EMA.process(s, 9);
        float[] ee = EMA.process(ema, 9);
        float[] d = ArrDiv.process(ema, ee);
        float[] re = ArrSum.process(d, dayNum,0);
        return re;
    }
}

