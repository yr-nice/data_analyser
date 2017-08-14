package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class PVT extends PostfixMathCommand
{
    public PVT()
    {
        numberOfParameters = 2;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        float[] v = (float[])inStack.pop();
        float[] c = (float[])inStack.pop();
        float[] re = process(c,v);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] list, float[] vol)
    {
        float[] re = new float[list.length];
        float[] cp = ArrMov.process(list, 1);
        float[] d = ArrSubtract.process(list, cp);
        d = ArrDiv.process(d, cp);
        d = ArrMulti.process(d, vol);
        re[0]=0;

        for(int i=1; i<d.length; i++)
        {
            re[i]=re[i-1]+d[i];
        }
        return re;
    }
}

