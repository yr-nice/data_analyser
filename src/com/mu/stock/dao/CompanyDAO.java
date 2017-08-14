package com.mu.stock.dao;

import com.mu.db.jpa.criteria.ColumnSelector;
import com.mu.db.jpa.criteria.Filter;
import com.mu.db.jpa.dao.BaseEmbeddedDAO;
import com.mu.stock.entity.Company;
import com.mu.util.log.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Peng Mu
 */
public class CompanyDAO extends BaseEmbeddedDAO
{
    static TreeMap<String, Company> cache = new TreeMap<String, Company>();

    static
    {
        Filter f = new Filter("Company");
        //f.addOrderBy(new Sequence("shortName"));
        List<Company> l = (List<Company>) get(f);
        for(Company c : l)
            cache.put(c.getShortName()+"."+c.getQuote()+"."+c.getStockExchange(), c);
    }


    static public Company getOrCreate(String quote, String stockExchange, String shortName)
    {
        Filter f = new Filter("Company");
        f.addEqual("quote", quote);
        f.addEqual("stockExchange", stockExchange);
        Company re = cache.get(shortName+"."+quote+"."+stockExchange);
        if(re == null)
        {
            try{
            re = (Company)getFirst(f);
            }catch(Exception e){Log.error(e);}
            if(re == null)
            {
                re = new Company();
                re.setQuote(quote);
                re.setStockExchange(stockExchange);
                re.setShortName(shortName);
                insert(re);
            }
            cache.put(quote+"."+stockExchange, re);
        }
        return re;

    }
    static public List<Company> getAll()
    {
        ArrayList<Company> re = new ArrayList<Company>();
        for(String s : cache.navigableKeySet())
            re.add(cache.get(s));
        return re;
            
    }

    static public List<String> getTags()
    {
        ColumnSelector f = new ColumnSelector("Company");
        f.addColumns("distinct tags");
        List<String> list = (List<String>)get(f);
        List<String> re = new ArrayList<String>();
        re.add("All");
        TreeSet<String> set = new TreeSet<String>();
        for(String l :list)
        {
            if(l==null || l.isEmpty())
                continue;
            String[] arr = l.split(",");
            for(String s : arr)
                set.add(s.trim());
        }
        re.addAll(set);
        return re;

    }

    static public void remove(Company c)
    {
        cache.remove(c.getShortName()+"."+c.getQuote()+"."+c.getStockExchange());
        DailyPriceDAO.remove(DailyPriceDAO.getByCompany(c, null, null));
        BaseEmbeddedDAO.remove(c);
    }
}
