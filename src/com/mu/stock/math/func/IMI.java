package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class IMI extends PostfixMathCommand
{
    public IMI()
    {
        numberOfParameters = 3;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        Double o = (Double)inStack.pop();
        int i = o.intValue();
        float[] c = (float[])inStack.pop();
        float[] op = (float[])inStack.pop();
        float[] re = process(op, c, i);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] open, float[] close, int dateNum)
    {
        float[] re = new float[open.length];
        float[] d = ArrSubtract.process(close, open);
        for(int i=0; i<dateNum; i++)
        {
            re[i] = 50;
        }
        for(int i=dateNum; i<d.length; i++)
        {
            float p=0;
            float n=0;
            for(int j=i-dateNum+1; j<=i; j++)
            {
                if (d[j] > 0)
                    p+=d[j];
                else
                    n+=Math.abs(d[j]);
            }
            re[i]=100*p/(p+n);
        }
        return re;
    }
}

