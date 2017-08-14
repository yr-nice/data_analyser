/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mu.stock.data;

import com.mu.util.RandomUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author peng mu
 */
public class DataMgr
{
    private static HashMap<String, Stock> cache = new HashMap<String, Stock>();

    static public Stock getStock(File stockFile)
    {
        String stockCode = stockFile.getName().replace(".csv", "");
        return getStock(stockCode);
    }

    static public Stock getStock(String stockCode)
    {
        if(cache.containsKey(stockCode))
            return cache.get(stockCode);

        Stock re = new Stock(stockCode);
        if(re.getPriceHistory() == null)
            return null;
        cache.put(stockCode, re);
        return re;
    }

    static public ArrayList<Stock> getRandomStocks(int num, String subFolderName)
    {
        ArrayList<Stock> re = new ArrayList<Stock>();
        File folder = new File("historical_data");
        if(subFolderName!=null || !subFolderName.isEmpty())
            folder = new File(folder, subFolderName);

        num = num<folder.listFiles().length? num : folder.listFiles().length;
        while(re.size()<num)
        {
            Stock s = getStock(RandomUtil.pickFile(folder));
            if(!re.contains(s))
                re.add(s);
        }
        return re;
    }


}
