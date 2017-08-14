package com.mu.stock.indicator.analyser;

import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.Stock;
import com.mu.stock.indicator.CandleStick;
import com.mu.stock.indicator.CandleStickType;
import com.mu.stock.indicator.PriceTrend;
import java.util.LinkedList;

/**
 *
 * @author Peng Mu
 */
public class CandlestickAnalyser
{
    static private int startIndex = 20;

    static public void detectSignal(Stock s)
    {
        detectSimplePattern(s);
        detectComplexPattern(s);
    }
    static public void detectSimplePattern(Stock s)
    {
        LinkedList<ProcessedDailyData> list = s.getPriceHistory();
        if(list.size()<=startIndex)
            return ;

        for(int i=startIndex; i<list.size(); i++)
        {
            ProcessedDailyData p = list.get(i);
            CandleStickType t = p.getCandle().getType();
            if(t == CandleStickType.hammer && p.getPriceTrend()==PriceTrend.down)
                p.addSignal(StockSignal.k_hammer);
            else if(t == CandleStickType.hammer && p.getPriceTrend() == PriceTrend.up)
                p.addSignal(StockSignal.k_hanging_man);
            else if(t == CandleStickType.inverted_hammer && p.getPriceTrend() == PriceTrend.down)
                p.addSignal(StockSignal.k_inverted_hammer);
            else if(t == CandleStickType.inverted_hammer && p.getPriceTrend() == PriceTrend.up)
                p.addSignal(StockSignal.k_shooting_star);
        }
    }

    static public void detectComplexPattern(Stock s)
    {
        LinkedList<ProcessedDailyData> list = s.getPriceHistory();
        if(list.size()<=startIndex)
            return ;

        for(int i=startIndex; i<list.size(); i++)
        {
            ProcessedDailyData p = list.get(i);
            ProcessedDailyData p1 = list.get(i-1);
            ProcessedDailyData p2 = list.get(i-2);
            
            CandleStick t = p.getCandle();
            CandleStick t1 = p1.getCandle();
            CandleStick t2 = p2.getCandle();
            if(t.getDiffCO()>0 && t1.getDiffCO()<0 && t.canEngulf(t1))
                p.addSignal(StockSignal.k_bull_engulf);
            else if(t.getDiffCO()<0 && t1.getDiffCO()>0 && t.canEngulf(t1))
                p.addSignal(StockSignal.k_bear_engulf);
            else if(t.getDiffCO()>0 && t2.getDiffCO()<0 && t1.getDiffCO()<0 && t1.canEngulf(t))
                p.addSignal(StockSignal.k_bull_harami);
            else if(t.getDiffCO()<0 && t2.getDiffCO()>0 && t1.getDiffCO()>0 && t1.canEngulf(t))
                p.addSignal(StockSignal.k_bear_harami);

            if(t.getType()==CandleStickType.doji || t.getType()==CandleStickType.dragonfly_doji || t.getType()==CandleStickType.gravestone_doji)
                p.addSignal(StockSignal.k_doji);
            if(isEveningStar(t, t1, t2))
                p.addSignal(StockSignal.k_evening_star);
            if(isMorningStar(t, t1, t2))
                p.addSignal(StockSignal.k_morning_star);
            if(isDownsideTasukiGap(t, t1, t2))
                p.addSignal(StockSignal.k_downside_tasuki_gap);
             if(isUpsideTasukiGap(t, t1, t2))
                p.addSignal(StockSignal.k_upside_tasuki_gap);
             if(isMorningStarDoji(t, t1, t2))
                p.addSignal(StockSignal.k_moring_star_doji);
             if(isEveningStarDoji(t, t1, t2))
                p.addSignal(StockSignal.k_evening_star_doji);

             if(isStickSandwich(t, t1, t2))
                p.addSignal(StockSignal.k_stick_sandwich);
             if(isDarkCloud(t, t1, t2))
                p.addSignal(StockSignal.k_dark_cloud);
             if(isPiercingPattern(t, t1, t2))
                p.addSignal(StockSignal.k_piercing);
             if(isThreeBlackCrows(t, t1, t2))
                p.addSignal(StockSignal.k_three_black_crows);
             if(isThreeWhiteSoldier(t, t1, t2))
                p.addSignal(StockSignal.k_three_white_soldiers);

       }
        
    }
    static public boolean isEveningStar(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(t2.getType()==CandleStickType.long_white && t1.getBodyBottom()>t2.getBodyTop() && t.getType()==CandleStickType.long_black && t.getBodyBottom()<t2.getBodyMid())
            return true;
        return false;
    }

    static public boolean isMorningStar(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(t2.getType()==CandleStickType.long_black && t1.getBodyTop()<t2.getBodyBottom() && t.getType()==CandleStickType.long_white && t.getBodyTop()>t2.getBodyMid())
            return true;
        return false;
    }

    static public boolean isDownsideTasukiGap(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(t2.getType()==CandleStickType.long_black && !t1.isWhite() && t1.getBodyTop()<t2.getBodyBottom() && t.isWhite() && t.getBodyTop()>t1.getBodyTop()&& t.getBodyTop()<t2.getBodyBottom())
            return true;
        return false;
    }

    static public boolean isUpsideTasukiGap(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(t2.getType()==CandleStickType.long_white && t1.isWhite() && t1.getBodyBottom()>t2.getBodyTop() && !t.isWhite()
            && t.getBodyBottom()<t1.getBodyBottom()&& t.getBodyBottom()>t2.getBodyTop())
            return true;
        return false;
    }

    static public boolean isMorningStarDoji(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(isMorningStar(t, t1, t2) && t1.getType()==CandleStickType.doji)
            return true;
        return false;
    }
    static public boolean isEveningStarDoji(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(isEveningStar(t, t1, t2) && t1.getType()==CandleStickType.doji)
            return true;
        return false;
    }

    static public boolean isStickSandwich(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(!t2.isWhite() && t1.isWhite() && !t.isWhite() && t.getBodyBottom()==t2.getBodyBottom())
            return true;
        return false;
    }



    static public boolean isDarkCloud(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(t2.getDiffCO()>0 && t1.getDiffCO()>0 && t.getDiffCO()<0 && t.getBodyTop()>t1.getBodyTop() && t.getBodyBottom()<t1.getBodyMid())
            return true;
        return false;
    }

    static public boolean isPiercingPattern(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(t2.getDiffCO()<0 && t1.getDiffCO()<0 && t.getDiffCO()>0 && t.getBodyBottom()<t1.getBodyBottom() && t.getBodyTop()>t1.getBodyMid())
            return true;
        return false;
    }

    static public boolean isThreeBlackCrows(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(!t.isWhite() && !t1.isWhite() && !t2.isWhite() 
            && t.getBodyTop()>t1.getBodyBottom() && t.getBodyTop()<t1.getBodyTop()&& t1.getBodyBottom()>t.getBodyBottom()
            && t1.getBodyTop()>t2.getBodyBottom() && t1.getBodyTop()<t2.getBodyTop()&& t2.getBodyBottom()>t1.getBodyBottom())
            return true;
        return false;
    }

    static public boolean isThreeWhiteSoldier(CandleStick t, CandleStick t1, CandleStick t2)
    {
        if(t.isWhite() && t1.isWhite() && t2.isWhite() 
            && t.getBodyBottom()<t1.getBodyTop() && t.getBodyBottom()> t1.getBodyBottom() && t.getBodyTop()>t1.getBodyTop()
            && t1.getBodyBottom()<t2.getBodyTop() && t1.getBodyBottom()> t2.getBodyBottom() && t1.getBodyTop()>t2.getBodyTop())
            return true;
        return false;
    }
}
