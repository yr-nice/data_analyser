package com.mu.stock.account.analyser;

import com.mu.stock.account.AcctHistory;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class AcctAnalyser
{
    private LinkedList<AcctHistory> acctHist = new LinkedList<AcctHistory>();
    private float[] profitPercent;
    public AcctAnalyser(List<AcctHistory> acctHist)
    {
        this.acctHist.addAll(acctHist);
        profitPercent = new float[acctHist.size()] ;
        int i=0;
        for(AcctHistory a : acctHist)
        {
            profitPercent[i] = a.getProfitPercent();
            i++;
        }
        Arrays.sort(profitPercent);
    }

    public float getLatestProfitPer()
    {
        if(acctHist.size()==0)
            return 0;
        return acctHist.getLast().getProfitPercent();
    }

    public float getAverProfitPer()
    {
        if(profitPercent.length==0)
            return 0;
        float sum=0;
        for(int i=0; i<profitPercent.length; i++)
            sum += profitPercent[i];

        return sum/profitPercent.length;
    }

    public float getLowestProfitPer()
    {
        if(profitPercent.length==0)
            return 0;
        return profitPercent[0];
    }

    public float getHighestProfitPer()
    {
        if(profitPercent.length==0)
            return 0;
        return profitPercent[profitPercent.length-1];
    }

    public float getMidProfitPer()
    {
        if(profitPercent.length==0)
            return 0;
        return profitPercent[profitPercent.length/2];
    }

    public int getPositiveDays()
    {
        int re=0;
        for(int i=0; i<profitPercent.length; i++)
            if(profitPercent[i]>0)
                re++;
        return re;
    }

    public int getNegativeDays()
    {
        int re=0;
        for(int i=0; i<profitPercent.length; i++)
            if(profitPercent[i]<0)
                re++;
        return re;
    }

}
