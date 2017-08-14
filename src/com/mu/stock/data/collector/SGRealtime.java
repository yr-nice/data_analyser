package com.mu.stock.data.collector;

import au.com.bytecode.opencsv.CSVWriter;
import com.mu.stock.entity.RealtimeDataLog;
import com.mu.util.DateUtil;
import com.mu.util.WebUtil;
import com.mu.util.log.Log;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Peng Mu
 */
public class SGRealtime implements Job
{
    //private boolean shouldStop = false;
    //private boolean stopped = false;
    static private ConcurrentHashMap<String, RealtimeDataLog> latest = new ConcurrentHashMap<String, RealtimeDataLog>();
    //private

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
    {
        try{
        JobDataMap dataMap = context.getMergedJobDataMap();;
        String stockUrl = dataMap.getString("url");
        String s = WebUtil.getWebPage(stockUrl);
        s = s.substring(s.indexOf("["));
        Log.info(s);
        String[] arr = s.split("\\},\\{");
        Log.info(arr.length);
        File f = new File("realtime", "sgx_"+DateUtil.toDateStr(new Date())+".csv");
        boolean needHeader = false;
        if(!f.exists() || f.length()==0)
        {
            if(f.getParentFile()!=null && !f.getParentFile().exists())
                f.getParentFile().mkdirs();
            needHeader = true;
            latest.clear();
        }
        CSVWriter csv = new CSVWriter(new FileWriter(f,true));
        if(needHeader)
        {
            String[] header = {"Name", "Code", "Last", "Change", "%", "Vol", "Buy Vol", "Buy Price", "Sell Price", "Sell Vol", "Open", "High", "Low", "Previous Close", "Value", "Sector", "Remarks", "Timestamp"};
            csv.writeNext(header);
        }

        for(String st : arr)
        {
            RealtimeDataLog r = new RealtimeDataLog(st);
            RealtimeDataLog last = latest.get(r.getCode());
            if(last != null && last.equals(r))
                continue;
            csv.writeNext(r.toArray());
            latest.put(r.getCode(), r);
        }
        csv.close();
        }catch(Exception e){Log.error(e);}
    }
}

