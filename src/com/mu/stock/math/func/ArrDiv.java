package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ArrDiv extends PostfixMathCommand
{
    public ArrDiv()
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
                if(or[i]==0&&i==0)
                    re[i]=0;
                else if(or[i]==0)
                    re[i]=re[i-1];
                else
                    re[i] = arr[i] / or[i];
            }
            else
                re[i] = arr[i] / Double.valueOf(o.toString()).floatValue();

        }
        return re;
    }

}
