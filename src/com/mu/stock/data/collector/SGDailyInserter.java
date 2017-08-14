package com.mu.stock.data.collector;

import com.mu.db.jpa.dao.BaseEmbeddedDAO;
import com.mu.stock.dao.CompanyDAO;
import com.mu.stock.dao.DailyPriceDAO;
import com.mu.stock.entity.Company;
import com.mu.stock.entity.DailyPriceLog;
import com.mu.util.log.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

/**
 *
 * @author Peng Mu
 */
public class SGDailyInserter
{
    static public void proccess()
    {

        File folder = new File("historical_data");
        File proccessed = new File(folder, "proccessed");
        if(!proccessed.exists())
        {
            proccessed.mkdirs();
        }

        for(File f : folder.listFiles())
        {
            if(proccessFile(f)!=null)
                f.renameTo(new File(proccessed, f.getName()));
        }
    }
    static public LinkedList<DailyPriceLog> proccessFile(File f)
    {
        LinkedList<DailyPriceLog> list = new LinkedList<DailyPriceLog>();
        String[] a = f.getName().replaceAll(".csv", "").split("__");
        if(a.length!=3)
            return null;
        Company c = CompanyDAO.getOrCreate(a[0], a[2], a[1]);
        DailyPriceLog latest = DailyPriceDAO.getLatestLog(c);
        try{
        BufferedReader bf = new BufferedReader(new FileReader(f));
        while(true)
        {
            String s = bf.readLine();
            if(s == null)
                break;
            if(s.startsWith("Date"))
                continue;
            String[] arr = s.split(",");
            DailyPriceLog p = new DailyPriceLog(arr);
            if(p.getVolume()==0)p.setVolume(1L);
            if(latest!=null && !latest.getDate().before(p.getDate()))continue;
            p.setCompany(c);
            list.addFirst(p);
        }
        BaseEmbeddedDAO.insert(list);
        Log.info("%s, %d records inserted", f.getName(), list.size());
        bf.close();
        }catch(Exception e){Log.error(e);}
        return list;
    }
    static public void main(String[] argu)
    {
        BaseEmbeddedDAO.init("stock.odb");
        proccess();
    }
}
