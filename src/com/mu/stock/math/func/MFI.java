package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class MFI extends PostfixMathCommand
{
    public MFI()
    {
        numberOfParameters = 5;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int d = ((Double)inStack.pop()).intValue();
        float[] v = (float[])inStack.pop();
        float[] c = (float[])inStack.pop();
        float[] l = (float[])inStack.pop();
        float[] h = (float[])inStack.pop();
        float[] re = process(h,l,c, v, d);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] high, float[] low, float[] close, float[] vol, int dateNum)
    {
        float[] re = new float[high.length];
        for(int i=dateNum; i<high.length; i++)
        {
            float sumUp =0;
            float sumDown =0;
            for(int j=i-dateNum+1; j<=i; j++)
            {
                float tp1 = (high[j] + low[j] + close[j])/3;
                float tp0 = (high[j-1] + low[j-1] + close[j-1])/3;;
                float mf = tp1 * vol[j];
                if(tp1 > tp0)
                    sumUp += mf;
                else
                    sumDown += mf;
            }
            if(sumDown==0)
                re[i] = re[i-1];
            else
                re[i] = 100 - 100/(1+sumUp/sumDown);
        }
        return re;
    }
}

