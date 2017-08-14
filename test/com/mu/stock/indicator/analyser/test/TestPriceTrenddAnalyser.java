package com.mu.stock.indicator.analyser.test;

import com.mu.stock.data.DataMgr;
import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.Stock;
import com.mu.stock.indicator.analyser.MacdAnalyser;
import com.mu.util.log.Log;
import java.util.Arrays;

/**
 *
 * @author Peng Mu
 */
public class TestPriceTrenddAnalyser
{

    static public void main(String[] argu)
    {
        Stock s = DataMgr.getStock("EcoWise_5CT");
        for(ProcessedDailyData p : s.getPriceHistory())
        {
            Log.info("%s, %s", p.getDate(), p.getPriceTrendAngle());
        }
    }

}
