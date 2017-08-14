package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ArrPow extends PostfixMathCommand
{
    public ArrPow()
    {
        numberOfParameters = 2;
    }
    public void run(Stack inStack) throws ParseException
    {
        checkStack(inStack);
        try{
        Object o = inStack.pop();
        float[] arr = (float[])inStack.pop();
        float[] re = process(arr, o);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }

    static float[] process(float[] arr, Object o)
    {
        float[] re = new float[arr.length];
        for(int i=0; i<re.length; i++)
        {
            if(o.getClass().isArray())
            {
                float[] or = (float[])o;
                Double db = Math.pow(arr[i], or[i]);
                re[i] = db.floatValue();
            }
            else
            {
                Double db = Math.pow(arr[i], Double.valueOf(o.toString()).floatValue());
                re[i] = db.floatValue();
            }

        }
        return re;

    }

}

