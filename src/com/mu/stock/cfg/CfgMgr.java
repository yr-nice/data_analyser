package com.mu.stock.cfg;

import com.mu.cfg.LinkedProperties;
import com.mu.util.log.Log;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 *
 * @author Peng Mu
 */
public class CfgMgr
{
    static private LinkedProperties prop;

    static public void load(String path)
    {
        try{
        prop = new LinkedProperties();
        prop.load(new FileInputStream(path));
        for(Entry<Object, Object> e : prop.entrySet())
        {
            String key = (String)e.getKey();
            if(key.startsWith("system."))
                System.setProperty(key, (String)e.getValue());
        }
        }catch(Exception e) {Log.fatal(e);}
    }

    static public String getStr(String key)
    {
        if(prop == null)
            return null;
        return prop.getProperty(key);

    }

    static public int getInt(String key)
    {
        return Integer.valueOf(getStr(key));
    }

    static public boolean getBool(String key)
    {
        return Boolean.valueOf(getStr(key));
    }

    public static Properties getProp()
    {
        return prop;
    }
    public static Map<String, String> getSubProp(String parent)
    {
        LinkedHashMap<String, String>  re = new LinkedHashMap<String, String> ();
        for(String k : prop.stringPropertyNames())
        {
            if(k.startsWith(parent))
                re.put(k.substring(parent.length()), prop.getProperty(k));
        }
        return re;
    }


}
