package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ChaikinMF extends PostfixMathCommand
{
    public ChaikinMF()
    {
        numberOfParameters = 5;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int d = ((Double)inStack.pop()).intValue();
        float[] v = (float[])inStack.pop();
        float[] c = (float[])inStack.pop();
        float[] l = (float[])inStack.pop();
        float[] h = (float[])inStack.pop();
        float[] re = process(h,l,c, v,d);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] high, float[] low, float[] close, float[] vol, int dayNum)
    {
        float[] c2 = ArrMulti.process(close, 2d);
        float[] a = ArrAdd.process(high, low);
        float[] s = ArrSubtract.process(high, low);
        float[] re = ArrMulti.process(ArrDiv.process(ArrSubtract.process(c2,a),s),vol);
        re = ArrSum.process(re, dayNum,0);
        float[] sv = ArrSum.process(vol, dayNum,0);
        re = ArrDiv.process(re, sv);
        return re;
    }
}

