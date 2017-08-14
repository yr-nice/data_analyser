package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class NVI extends PostfixMathCommand
{
    public NVI()
    {
        numberOfParameters = 2;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        float[] vol = (float[])inStack.pop();
        float[] c = (float[])inStack.pop();
        float[] re = process(c, vol);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] pList, float[] vList)
    {
        float[] re = new float[vList.length];
        re[0]=1000;
        for(int i=1; i<vList.length; i++)
        {
            float p1 = pList[i];
            float p0 = pList[i-1];
            float v1 = vList[i];
            float v0 = vList[i-1];
            if(v1<=v0)
                re[i]=re[i-1];
            else
                re[i]=re[i-1]+(p1-p0)/p0*re[i-1];
        }
        return re;
    }
}

