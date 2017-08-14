package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class ArrAbs extends PostfixMathCommand
{
    public ArrAbs()
    {
        numberOfParameters = 1;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        float[] c = (float[])inStack.pop();
        float[] re = process(c);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }
    static public float[] process(float[] l)
    {
        float[] re = new float[l.length];
        for(int i=0; i<l.length; i++)
        {
                re[i]=Math.abs(l[i]);
        }
        return re;
    }
}

