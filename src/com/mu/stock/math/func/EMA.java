package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class EMA extends PostfixMathCommand
{
    public EMA()
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
    static public float[] process(float[] list, int params)
    {
        float[] re = new float[list.length];
        float multi = 2F/(params+1);
        re[0]  = list[0];
        for(int i=1; i<re.length; i++)
        {
            re[i] = list[i]*multi + re[i-1]*(1-multi);
        }

        return re;
    }
}

