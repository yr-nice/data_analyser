package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class Gravity extends PostfixMathCommand
{
    public Gravity()
    {
        numberOfParameters = 3;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int l = ((Double)inStack.pop()).intValue();
        int s = ((Double)inStack.pop()).intValue();
        float[] c = (float[])inStack.pop();
        float[] re = process(c, s, l);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] list, int shortSMA, int longSMA)
    {
        float[] s = SMA.process(list, shortSMA);
        float[] l = SMA.process(list, longSMA);
        float[] av = ArrDiv.process(ArrAdd.process(s, l),2);
        return av;
    }
}

