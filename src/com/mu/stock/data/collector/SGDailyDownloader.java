package com.mu.stock.data.collector;

import com.mu.stock.dao.DailyPriceDAO;
import com.mu.stock.entity.Company;
import com.mu.stock.entity.DailyPriceLog;
import com.mu.util.StringUtil;
import com.mu.util.log.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Properties;

/**
 *
 * @author Peng Mu
 */
public class SGDailyDownloader
{
    static public void downloadFromYahoo()
    {
        Properties p = new Properties();
        try{
        p.load(new FileReader("conf/symbols.properties"));
        File folder = new File("historical_data");
        if(!folder.exists())
            folder.mkdirs();
        for(String code : p.stringPropertyNames())
        {
            /*if(name.contains(".ES."))
            {
                Log.info("skip %s", name);
                continue;
            }*/
            String name = p.getProperty(code);
            Log.info("name:%s, code:%s", name, code);
            
            File f = new File(folder, code+"__"+name+".csv");
            if(f.exists())
            {
                Log.info("%s exists, skip", f.getName());
                continue;
            }

            try{
            downloadFile("http://ichart.finance.yahoo.com/table.csv?s="+code+".SI", f);
            Log.info("Downloaded %s", f.getName());
            }catch(Exception e){Log.error(e);}
        }
        }catch(Exception e){Log.error(e);}
    }

    static public File downloadUpdates(Company c)
    {
        File f = null;
        try{
        File folder = new File("historical_data");
        if(!folder.exists())
            folder.mkdirs();

        f = new File(folder, c.getQuote()+"__"+c.getShortName()+"__"+c.getStockExchange()+".csv");
        if(f.exists())
        {
            f.delete();
        }
        DailyPriceLog dp = DailyPriceDAO.getLatestLog(c);
        String url = "http://ichart.finance.yahoo.com/table.csv?s="+c.getQuote()+".SI";
        if(!StringUtil.isEmpty(c.getYahooQuote()))
            url = "http://ichart.finance.yahoo.com/table.csv?s="+c.getYahooQuote();
        if(dp != null)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dp.getDate());
            url += String.format("&a=%d&b=%d&c=%d", cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
        }

        downloadFile(url, f);
        Log.info("Downloaded %s", f.getName());
        }catch(Exception e){Log.error(e);}
        return f;
    }

    static private void downloadFile (String strUrl, File dest)throws Exception
	{
        URL url = new URL(strUrl);
        InputStream is = url.openStream();
        FileOutputStream fo = new FileOutputStream(dest, true);
        byte[] buff = new byte[1024];
        while(true)
        {
            int i = is.read(buff);
            if(i == -1) break;
            fo.write(buff, 0, i);
        }
        fo.close();
        is.close();
	}
}
