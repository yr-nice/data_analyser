package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class SMA extends PostfixMathCommand
{
    public SMA()
    {
        numberOfParameters = 2;
    }
    public void run(Stack inStack) throws ParseException
    {

        // check the stack
        checkStack(inStack);

        // get the parameter from the stack
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
        re[0] = list[0];

        for(int i=1; i<list.length; i++)
        {
            float f1 = list[i];
            float r0 = re[i-1];
            try{
            if(i<params)
                re[i] = (r0*i+f1)/(i+1);
            else
                re[i] = (r0*params-list[i-params]+f1)/params;
            }catch(Exception e){Log.error(e);}
        }
        return re;
    }
}

