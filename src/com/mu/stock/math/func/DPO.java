package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class DPO extends PostfixMathCommand
{
    public DPO()
    {
        numberOfParameters = 2;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        Double o = (Double)inStack.pop();
        int i = o.intValue();
        float[] c = (float[])inStack.pop();
        float[] re = process(c, i);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] list, int dateNum)
    {
        float[] re = new float[list.length];
        float[] sma = SMA.process(list, dateNum);
        sma = ArrMov.process(sma, (-1)*(dateNum/2+1));
        re = ArrSubtract.process(list,sma);
        return re;
    }
}

