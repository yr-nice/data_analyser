package com.mu.stock.data;

import com.mu.util.log.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

/**
 *
 * @author Peng Mu
 */
public class DataReader
{
    static public LinkedList<ProcessedDailyData> read(File f)
    {
        LinkedList<ProcessedDailyData> re = null;
        if(!f.exists())
            return re;
        try{
        BufferedReader bf = new BufferedReader(new FileReader(f));
        re = new LinkedList<ProcessedDailyData>();
        while(true)
        {
            String s = bf.readLine();
            if(s == null)
                break;
            if(s.startsWith("Date"))
                continue;
            String[] arr = s.split(",");
            ProcessedDailyData p = new ProcessedDailyData(arr);
            if(p.getVolume()==0)
                continue;

            re.addFirst(p);
        }
        bf.close();
        }catch(Exception e){Log.error(e);}
        return re;

    }
    static public LinkedList<ProcessedDailyData> read(String path)
    {
        return read(new File(path));
    }
}
