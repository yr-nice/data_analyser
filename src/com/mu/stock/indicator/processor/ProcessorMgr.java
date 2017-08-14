package com.mu.stock.indicator.processor;

import com.mu.stock.dao.DailyPriceDAO;
import com.mu.stock.entity.Company;
import com.mu.stock.entity.DailyPriceLog;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class ProcessorMgr
{
    static public void process(List<DailyPriceLog> list)
    {
        SMACalc.process(list);
        PriceChangeCalc.process(list);
        VolRatioCalc.process(list);
    }

    static public void reset(List<DailyPriceLog> list)
    {
        SMACalc.reset(list);
        PriceChangeCalc.reset(list);
        VolRatioCalc.reset(list);
    }
}
