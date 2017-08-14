package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class CMO extends PostfixMathCommand
{
    public CMO()
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
        float[] d = new float[list.length];
        for(int i=1; i<list.length;i++)
            d[i]=list[i]-list[i-1];
        for(int i=0; i<dateNum; i++)
        {
            re[i] = 0;
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
            re[i]=100*(p-n)/(p+n);
        }
        return re;
    }
}

