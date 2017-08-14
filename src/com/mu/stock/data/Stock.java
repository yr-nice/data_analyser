/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mu.stock.data;

import com.mu.stock.indicator.analyser.CandlestickAnalyser;
import com.mu.stock.indicator.analyser.MFIAnalyser;
import com.mu.stock.indicator.analyser.MacdAnalyser;
import com.mu.stock.indicator.analyser.PeakBottomAnalyser;
import com.mu.stock.indicator.analyser.RSIAnalyser;
import com.mu.stock.indicator.analyser.RocAnalyser;
import com.mu.stock.indicator.analyser.SMAAnalyser;
import com.mu.stock.indicator.analyser.StochasticAnalyser;
import com.mu.stock.indicator.analyser.SupportResistanceAnalyser;
import com.mu.stock.indicator.analyser.WilliamsRAnalyser;
import com.mu.stock.indicator.processor.LocalMinMaxPrc;
import com.mu.stock.indicator.processor.MACDPrc;
import com.mu.stock.indicator.processor.MFIPrc;
import com.mu.stock.indicator.processor.MomentumPrc;
import com.mu.stock.indicator.processor.OBVPrc;
import com.mu.stock.indicator.processor.PriceROCPrc;
import com.mu.stock.indicator.processor.PriceTrendAnglePrc;
import com.mu.stock.indicator.processor.RSIPrc;
import com.mu.stock.indicator.processor.SMAPrc;
import com.mu.stock.indicator.processor.StochasticRPrc;
import com.mu.stock.indicator.processor.WilliamsRPrc;
import com.mu.util.DateUtil;
import com.mu.util.FileUtil;
import com.mu.util.log.Log;
import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 *
 * @author peng mu
 */
public class Stock
{
    private String code;
    private LinkedList<ProcessedDailyData> priceHistory;
    private TreeMap<String, ProcessedDailyData> priceHistoryMap;
    private TreeMap<String, ProcessedDailyData> localMin = new TreeMap<String, ProcessedDailyData>();
    private TreeMap<String, ProcessedDailyData> localMax = new TreeMap<String, ProcessedDailyData>();

    public Stock(String code)
    {
        this.code = code;
        load(code);
    }

    private void load(String code)
    {
        Log.info("Loading %s", code);
        //LinkedList<ProcessedDailyData> list = DataReader.read("historical_data/"+code+".csv");
        LinkedList<ProcessedDailyData> list = DataReader.read(FileUtil.locateFirstFile(new File("historical_data"), code));
        if(list.size()==0)
            return;

        LocalMinMaxPrc.process(list, localMin, localMax);
        PriceTrendAnglePrc.process(list, 1, 17);
        MACDPrc.process(list, 12, 26, 9);
        RSIPrc.process(list, 14);
        OBVPrc.process(list);
        PriceROCPrc.process(list, 12);
        MomentumPrc.process(list, 12);
        MFIPrc.process(list, 14);
        WilliamsRPrc.process(list, 14);
        StochasticRPrc.process(list, 14, 3, 3);
        SMAPrc.process(list);
        priceHistory = list;
        priceHistoryMap = new TreeMap<String, ProcessedDailyData>();
        for(ProcessedDailyData p : priceHistory)
            priceHistoryMap.put(p.getDateStr(), p);

        MacdAnalyser.detectSignal(this);
        RSIAnalyser.detectSignal(this);
        CandlestickAnalyser.detectSignal(this);
        PeakBottomAnalyser.detectSignal(this);
        MFIAnalyser.detectSignal(this);
        WilliamsRAnalyser.detectSignal(this);
        StochasticAnalyser.detectSignal(this);
        RocAnalyser.detectSignal(this);
        SupportResistanceAnalyser.detectSignal(this);
        SMAAnalyser.detectSignal(this);
    }

    public String getCode()
    {
        return code;
    }

    public LinkedList<ProcessedDailyData> getPriceHistory()
    {
        return priceHistory;
    }

    public float getAdjClose(String date)
    {
        while(!isTxnDay(date)&&date.compareTo(priceHistory.get(0).getDateStr())>0)
        {
            date = DateUtil.previousDayStr(date);
        }
        ProcessedDailyData p =  priceHistoryMap.get(date);
        if(p==null)
            return -1F;

        return p.getAdjClose();
        /*
        for(ProcessedDailyData p : priceHistory)
        {
            if(p.getDate().compareTo(date)>0)
                return -1F;
            if(p.getDate().compareTo(date)==0)
                return p.getAdjClose();
        }
        return -1F;*/
    }
    
    public int numOfSharesCanBuy(String date, float money)
    {
        float p = getAdjClose(date);
        if(p<0)
            return -1;
        float max = money/p;
        int i = (int) (max / 1000);
        return i*1000;
    }

    public boolean isTxnDay(String date)
    {
        return priceHistoryMap.containsKey(date);
    }
    public String getTxnDay(String date)
    {
        while(!isTxnDay(date) && date.compareTo(priceHistory.getLast().getDateStr())<=0)
        {
            date = DateUtil.nextDayStr(date);
        }
        return date;
    }

    public ProcessedDailyData getProcessedDataByDay(String date)
    {
        return priceHistoryMap.get(date);
    }

    public TreeMap<String, ProcessedDailyData> getLocalMax()
    {
        return localMax;
    }

    public TreeMap<String, ProcessedDailyData> getLocalMin()
    {
        return localMin;
    }

    public float getSupportPrice(String date, int withinLastDays)
    {

        TreeMap<String, ProcessedDailyData> sub = getSubMap(date, withinLastDays, localMin);
        float re = 99999;
        if(sub.isEmpty())
            return -1;
        else
            for(ProcessedDailyData d : sub.values())
                if(re>d.getAdjClose())
                    re = d.getAdjClose();
        return re*0.985F;
    }

    public float getResistancePrice(String date, int withinLastDays)
    {

        TreeMap<String, ProcessedDailyData> sub = getSubMap(date, withinLastDays, localMax);
        float re = -99999;
        if(sub.isEmpty())
            return -1;
        else
            for(ProcessedDailyData d : sub.values())
                if(re<d.getAdjClose())
                    re = d.getAdjClose();
        return re*1.03F;
    }

    private TreeMap<String, ProcessedDailyData> getSubMap(String date, int withinLastDays, TreeMap<String, ProcessedDailyData> orig)
    {
        ProcessedDailyData p = priceHistoryMap.get(date);
        int i = priceHistory.indexOf(p);
        ProcessedDailyData left = null;
        if(i>=withinLastDays)
            left = priceHistory.get(i-withinLastDays);
        else
            left = priceHistory.get(0);

        TreeMap<String, ProcessedDailyData> sub = new TreeMap<String, ProcessedDailyData>(orig.subMap(left.getDateStr(), date));
        return sub;
    }

    public float getAverPriceByVol(String date, int days)
    {
        float re = 0;
        int sumVol=0;
        date = getTxnDay(date);
        ProcessedDailyData p = priceHistoryMap.get(date);
        int index = priceHistory.indexOf(p);
        if(index<days+1)
            return 0;
        for(int i=index-days-1; i<index; i++)
        {
            re += priceHistory.get(i).getAdjClose()*priceHistory.get(i).getVolume();
            sumVol +=priceHistory.get(i).getVolume();
        }
        return re/sumVol;
    }
}
