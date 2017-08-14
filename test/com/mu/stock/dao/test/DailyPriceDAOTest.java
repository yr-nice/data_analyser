package com.mu.stock.dao.test;

import com.mu.db.jpa.dao.BaseEmbeddedDAO;
import com.mu.stock.dao.DailyPriceDAO;
import com.mu.stock.entity.DailyPriceLog;
import com.mu.util.DateUtil;
import com.mu.util.log.Log;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class DailyPriceDAOTest
{
    static public void main(String[] argu)
    {
        BaseEmbeddedDAO.init("stock.odb");

        DailyPriceDAOTest ins = new DailyPriceDAOTest();
        //ins.testGetByDate();
        ins.testGetSingleDay();
    }
    private void testGetByDate()
    {
        List<DailyPriceLog> arr = DailyPriceDAO.getLogByDate(DateUtil.toDate("2005-01-03"), DateUtil.toDate("2005-01-03"));
        Log.info(arr.size());
    }
    private void testGetSingleDay()
    {
        List<DailyPriceLog> arr = DailyPriceDAO.getLogByDate(DateUtil.toDate("2005-01-03"));
        Log.info(arr.size());
        arr = DailyPriceDAO.getLogByDate(DateUtil.toDate("2005-01-04"));
        Log.info(arr.size());
        arr = DailyPriceDAO.getLogByDate(DateUtil.toDate("2005-01-05"));
        Log.info(arr.size());
    }

}
