package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ArrMov extends PostfixMathCommand
{
    public ArrMov()
    {
        numberOfParameters = 2;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int offset = ((Double)inStack.pop()).intValue();
        float[] c = (float[])inStack.pop();
        float[] re = process(c, offset);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] l, int offsetDays)
    {
        float[] re = new float[l.length];
        for(int i=0; i<l.length; i++)
        {
            int j=i-offsetDays;
            if(j<0||j>=l.length)
                re[i]=l[i];
            else
                re[i]=l[j];
        }
        return re;
    }
}

