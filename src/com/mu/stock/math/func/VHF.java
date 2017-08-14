package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class VHF extends PostfixMathCommand
{
    public VHF()
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
    static public float[] process(float[] c, int dateNum)
    {
        float[] h = MAX.process(c, dateNum, 0);
        float[] l = MIN.process(c, dateNum, 0);
        float[] d = ArrSubtract.process(h, l);
        float[] cp = ArrMov.process(c, 1);
        float[] cd = ArrAbs.process(ArrSubtract.process(c, cp));
        float[] cdm = ArrSum.process(cd, dateNum,0);
        return ArrDiv.process(d,cdm);
    }
}

