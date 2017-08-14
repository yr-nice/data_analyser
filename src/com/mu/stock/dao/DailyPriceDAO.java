package com.mu.stock.dao;

import com.mu.collection.AutoDiscardingMap;
import com.mu.db.jpa.criteria.Filter;
import com.mu.db.jpa.criteria.Sequence;
import com.mu.db.jpa.dao.BaseEmbeddedDAO;
import com.mu.stock.entity.Company;
import com.mu.stock.entity.DailyPriceLog;
import com.mu.util.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peng Mu
 */
public class DailyPriceDAO extends BaseEmbeddedDAO
{
    static private AutoDiscardingMap<String, ArrayList<DailyPriceLog>> cache = new AutoDiscardingMap<String, ArrayList<DailyPriceLog> >(100);
    static public DailyPriceLog getLatestLog(Company c)
    {
        Filter f = new Filter("DailyPriceLog");
        f.addEqual("company", c);
        f.addOrderBy(new Sequence("date", false));
        f.setNumOfRow(1);
        DailyPriceLog re = (DailyPriceLog)getFirst(f);
        return re;
    }

    static public List<DailyPriceLog> getLastNLog(Company c, int num)
    {
        Filter f = new Filter("DailyPriceLog");
        f.addEqual("company", c);
        f.addOrderBy(new Sequence("date", false));
        f.setNumOfRow(num);
        return (List<DailyPriceLog>)get(f);
    }


    static public List<DailyPriceLog> getByCompany(Company c, Date from, Date to)
    {
        Filter f = new Filter("DailyPriceLog");
        f.addEqual("company", c);
        if(from != null)
            f.addMoreEqualThan("date", from);
        if(to != null)
            f.addLessEqualThan("date", to);

        f.addOrderBy(new Sequence("date", true));
        List<DailyPriceLog> re = (List<DailyPriceLog>)get(f);
        return re;
    }
    
    static public DailyPriceLog getLogByDate(Company c, Date date)
    {
        /*List<DailyPriceLog> re = getByCompany(c, DateUtil.getDayStart(date),DateUtil.getDayEnd(date));
        if(re==null || re.isEmpty())
            return null;
        return re.get(0);
         *
         */
        String k = DateUtil.toDateStr(date);
        if(!cache.containsKey(k))
        {
            getLogByDate(date);
        }
        List<DailyPriceLog> dList = cache.get(k);
        for(DailyPriceLog dp : dList)
            if(dp.getCompany()== c)
                return dp;
        return null;
    }

    static public List<DailyPriceLog> getLogByDate(Date from, Date to)
    {
        Filter f = new Filter("DailyPriceLog");
        f.addMoreEqualThan("dateStr", DateUtil.toDateStr(from));
        f.addLessEqualThan("dateStr", DateUtil.toDateStr(to));
        f.addOrderBy(new Sequence("dateStr"));
        return (List<DailyPriceLog>)get(f);
    }

    static public List<DailyPriceLog> getLogByDate(Date date)
    {
        String k = DateUtil.toDateStr(date);
        if(!cache.containsKey(k))
        {
            Filter f = new Filter("DailyPriceLog");
            f.addMoreEqualThan("dateStr", DateUtil.toDateStr(date));
            f.addLessEqualThan("dateStr", DateUtil.toDateStr(DateUtil.nextNDay(date, 12)));
            f.addOrderBy(new Sequence("dateStr"));
            List<DailyPriceLog> rlt = (List<DailyPriceLog>)get(f);
            String dk=null;
            ArrayList<DailyPriceLog> dlist = new ArrayList<DailyPriceLog>();
            for(DailyPriceLog dp : rlt)
            {
                if(dk==null)
                    dk = dp.getDateStr();
                if(!dk.equals(dp.getDateStr()))
                {
                    cache.push(dk, dlist);
                    dk = dp.getDateStr();
                    dlist = new ArrayList<DailyPriceLog>();
                }
                dlist.add(dp);
            }
            if(dk != null)
                cache.push(dk, dlist);
        }
        return cache.get(k);
    }

}
