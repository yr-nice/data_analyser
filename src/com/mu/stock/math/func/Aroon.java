package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class Aroon extends PostfixMathCommand
{
    public Aroon()
    {
        numberOfParameters = 3;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int up = ((Double)inStack.pop()).intValue();
        int d = ((Double)inStack.pop()).intValue();
        float[] c = (float[])inStack.pop();
        float[] re = process(c, d, up);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] close, int dateNum, int up)
    {
        float[] re = new float[close.length];
        for(int i=dateNum; i<close.length; i++)
        {
            int a=i-dateNum;
            float f = close[i-dateNum];
            for(int j=i-dateNum+1; j<=i; j++)
            {
                float n = close[j];
                if((up > 0 && n>=f) ||
                    (up <= 0 && n<=f))
                {
                    f=n;
                    a=j;
                }
                int toNow = i-a;
                re[i] = 100*(dateNum - toNow)/dateNum ;

            }
        }

        return re;
    }
}

