package com.mu.stock.math.func;

import com.mu.util.log.Log;
import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 *
 * @author Peng Mu
 */
public class RMI extends PostfixMathCommand
{
    public RMI()
    {
        numberOfParameters = 3;
    }
    public void run(Stack inStack) throws ParseException
    {

        checkStack(inStack);

        try{
        int o = ((Double)inStack.pop()).intValue();
        int d = ((Double)inStack.pop()).intValue();
        float[] c = (float[])inStack.pop();
        float[] re = process(c,d,o);
        inStack.push(re);
        }catch(Exception e){Log.error(e);}
    }

    static public float[] process(float[] list, int dateNum, int compareTo)
    {
        float[] re = new float[list.length];
        float[] up = new float[list.length];
        float[] down = new float[list.length];
        up[0]  = 0;
        down[0]  = 0;
        for(int i=0; i<compareTo; i++)
        {
            re[i]=50;
        }
        for(int i=compareTo; i<re.length; i++)
        {
            float t = list[i]- list[i-compareTo];
            if(t>0)
            {
                up[i]=t;
                down[i]=0;
            }
            else
            {
                down[i]=Math.abs(t);
                up[i]=0;
            }
        }

        float emaUp[] =  EMA.process(up, dateNum);
        float emaDown[] =  EMA.process(down, dateNum);
        //emaUp[0] = 0;
        //emaDown[0] =0;
        for(int i=1; i<re.length; i++)
        {
            //emaUp[i] = (up[i] + emaUp[i-1]*(dateNum-1))/dateNum;
            //emaDown[i] = (down[i] + emaDown[i-1]*(dateNum-1))/dateNum;
            if(emaDown[i]==0)
                re[i] = re[i-1];
            else
            {
                float rs = emaUp[i]/emaDown[i];
                re[i] = 100 - 100/(1+rs);
            }
        }

        return re;
    }

}

