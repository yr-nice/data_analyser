package com.mu.stock.init;

import com.mu.db.jpa.dao.BaseEmbeddedDAO;
import com.mu.stock.cfg.CfgMgr;
import com.mu.stock.quartz.ScheduleMgr;
import com.mu.util.log.Log;


/**
 *
 * @author Peng Mu
 */
public class Initiator
{
    static private boolean inited=false;
    synchronized static public boolean init()
    {
        if(inited)
        {
            Log.info("Already init.");
            return true;
        }
        Log.info("\n*****************************************************\n" +
                   "** Start to init data analyser ...\n" +
                   "*****************************************************");
        CfgMgr.load("config.prop");
        BaseEmbeddedDAO.init("da");
        ScheduleMgr.init();

        inited=true;
        
        Log.info("\n*****************************************************\n" +
                   "** End of data analyser initialization!\n" +
                   "*****************************************************");

        return true;
    }


    static public boolean shutdown()
    {
        try{

        ScheduleMgr.shutdown();
        //Thread.sleep(5000);
        }catch(Exception e){Log.error(e);}
        return true;
    }
}
