package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ArrItem extends PostfixMathCommand
{
    public ArrItem()
    {
        numberOfParameters = 2;
    }
    public void run(Stack inStack) throws ParseException
    {
        checkStack(inStack);
        try{
        int i  = ((Double)inStack.pop()).intValue();
        float[] arr = (float[])inStack.pop();
        float re = process(arr, i);
        inStack.push(Double.valueOf(re));
        }catch(Exception e){Log.error(e);}
    }

    static float process(float[] arr, int i)
    {
        return arr[i];

    }

}

