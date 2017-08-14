package com.mu.util.log;

/**
 *
 * @author Peng Mu
 */

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;


public class Log
{
    static private Logger log ;
    static final private String propCfgName = "log4j.properties";
    static final private String xmlCfgName = "log4j.xml";

    static{
        File xmlcfg = new File(xmlCfgName);
        File propcfg = new File(propCfgName);
        if(xmlcfg.exists())
        {
            System.out.printf("Load log config file from %s\n", xmlcfg.getAbsolutePath());
            DOMConfigurator.configure(xmlcfg.getAbsolutePath());
        }
        else if(propcfg.exists())
        {
            System.out.printf("Load log config file from %s\n", propcfg.getAbsolutePath());
            PropertyConfigurator.configure(propcfg.getAbsolutePath());
        }
        else
            System.out.printf("Can not locate config file from neither %s nor %s\n", xmlcfg.getAbsolutePath(), propcfg.getAbsolutePath());

        log = Logger.getLogger("hellomkt");
    }


    static public void main(String[] argu) throws Exception
    {
        //loadCfg("log4j.properties", "hellomkt");
        BigDecimal b = new BigDecimal("12.34");
        log(b);
        int x = 22;
        log(x);
        log(25);
        log("now is about %d, i have $%f", 12, 1.6);
        try{
            String [] arr =new String[10];
            arr[11]="";
        }catch(Exception e){log(e);}
    }

    static public void loadCfg(String configPath, String loggerName)
    {
        configPath = configPath.trim();
        if(configPath.endsWith(".xml"))
        {
            System.out.printf("Load log config XML file from %s\n", configPath);
            DOMConfigurator.configure(configPath);
        }
        else
        {
            System.out.printf("Load log config Property file from %s\n", configPath);
            PropertyConfigurator.configure(configPath);
        }

        log = Logger.getLogger(loggerName);
    }

    static public void log(Object o)
    {
        log.info(o);
    }

    static public void log(String o, Object ... argus)
    {
        log.info(getMsg(o, argus));
    }

    static public void log(Throwable o)
    {
        log.info(getMsg(o));
    }

    static public void debug(Object o)
    {
        log.debug(o);
    }

    static public void debug(String o, Object ... argus)
    {
        log.debug(getMsg(o, argus));
    }

    static public void debug(Throwable o)
    {
        log.debug(getMsg(o));
    }

    static public void info(Object o)
    {
        log.info(o);
    }

    static public void info(String o, Object ... argus)
    {
        log.info(getMsg(o, argus));
    }

    static public void info(Throwable o)
    {
        log.info(getMsg(o));
    }

    static public void info(Collection o)
    {
        log.info(Arrays.toString(o.toArray()));
    }

    static public void error(Object o)
    {
        log.error(o);
    }

    static public void error(String o, Object ... argus)
    {
        log.error(getMsg(o, argus));
    }

    static public void error(Throwable o)
    {
        log.error(getMsg(o));
    }

    static public void fatal(Object o)
    {
        log.fatal(o);
    }

    static public void fatal(String o, Object ... argus)
    {
        log.fatal(getMsg(o, argus));
    }

    static public void fatal(Throwable o)
    {
        log.fatal(getMsg(o));
    }

    static private String getMsg(String o, Object ... argus)
    {
        return String.format(o, argus);
    }


    static private String getMsg(Throwable o)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(o.toString());

        StringWriter 	sw = new StringWriter(5000);
        PrintWriter 	pw = new PrintWriter(sw);
        o.printStackTrace(pw);
        pw.close();

        sb.append(sw.toString());
        return sb.toString();

    }
    public static long interval(String to, long lastCheckTime)
    {
        long re = System.currentTimeMillis();
        debug("Performance Check: %s, took %d ms", to, re-lastCheckTime);
        return re;
    }

}
