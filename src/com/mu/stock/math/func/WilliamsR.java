package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class WilliamsR extends PostfixMathCommand
{
    public WilliamsR()
    {
        numberOfParameters = 4;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int d = ((Double)inStack.pop()).intValue();
        float[] c = (float[])inStack.pop();
        float[] l = (float[])inStack.pop();
        float[] h = (float[])inStack.pop();
        float[] re = process(h, l, c, d);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] high, float[] low, float[] close, int dateNum)
    {
        float[] re = new float[close.length];
        for(int i=dateNum; i<close.length; i++)
        {
            float h = 0;
            float l = 100000;
            for(int j=i-dateNum+1; j<=i; j++)
            {
                float hn = high[j];
                float ln = low[j];
                h = hn > h ? hn : h;
                l = ln < l ? ln :l;
            }
            float c = close[i];
            if(h==l)
                re[i] = re[i-1];
            else
                re[i] = 100*(c-h)/(h-l);
        }

        return re;
    }
}

