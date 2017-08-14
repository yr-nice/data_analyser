package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class Momentum extends PostfixMathCommand
{
    public Momentum()
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
        for(int i=0; i<dateNum; i++)
        {
            re[i] = 100;
        }
        for(int i=dateNum; i<list.length; i++)
        {
            float p1 = list[i];
            float p0 = list[i-dateNum];
            if(p0==0)
                re[i]=re[i-1];
            else
                re[i] = 100*(p1/p0);
        }
        return re;
    }
}

