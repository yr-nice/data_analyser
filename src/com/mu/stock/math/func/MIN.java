package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class MIN extends PostfixMathCommand
{
    public MIN()
    {
        numberOfParameters = 3;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int offset = ((Double)inStack.pop()).intValue();
        int d = ((Double)inStack.pop()).intValue();
        float[] c = (float[])inStack.pop();
        float[] re = process(c, d, offset);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] l, int dateNum, int offsetDays)
    {
        float[] re = new float[l.length];
        for(int i=0; i<dateNum-offsetDays; i++)
        {
            re[i]=l[i];
        }
        for(int i=dateNum-offsetDays; i<l.length; i++)
        {
            float h = Float.MAX_VALUE;
            for(int j=i-dateNum+1+offsetDays; j<=i+offsetDays; j++)
            {
                float hn = l[j];
                h = hn < h ? hn : h;
            }
            re[i]=h;
        }
        return re;
    }
}

