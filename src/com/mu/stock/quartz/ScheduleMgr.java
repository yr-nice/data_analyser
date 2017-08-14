package com.mu.stock.quartz;

import com.mu.stock.cfg.CfgMgr;
import com.mu.util.log.Log;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class ScheduleMgr
{

    private static Scheduler scheduler = null;

    public static Scheduler getScheduler()
    {
        if (scheduler == null)
        {
            init();
        }

        return scheduler;
    }

    public static void init()
    {

        try
        {

            Log.info("starting quartz scheduler .....");

            StdSchedulerFactory factory = new StdSchedulerFactory();

            factory.initialize();

            scheduler = factory.getScheduler();

            scheduler.start();
            //createSystemScheduledJob();

            Log.info("quartz scheduler started.");

        } catch (SchedulerException e)
        {
            Log.error("unable to initialize quartz scheduler", e);
        }
    }

    public static void shutdown()
    {
        try
        {
            if (scheduler != null)
            {
                Log.info("Shutting down quartz scheduler");
                scheduler.shutdown(true);
                Log.info("quartz scheduler shutdown complete");
            }
        } catch (SchedulerException e)
        {
            Log.error("Unable to properly shutdown the quartz scheduler", e);
        }
    }

    public static List<JobDetail> getScheduledJobList(String groupName)
    {
        ArrayList<JobDetail> re = new ArrayList();
        try
        {
            String[] names = scheduler.getJobNames(groupName);
            if (names == null)
            {
                return re;
            }
            for (String s : names)
            {
                re.add(scheduler.getJobDetail(s, groupName));
            }
        } catch (Exception e)
        {
            Log.error(e);
        }
        return re;
    }

    public static void createSystemScheduledJob()
    {
        //broadcast monitor
        try
        {
            String s = CfgMgr.getStr("quartz.jobs");
            String[] arr = s.split(",");
            for(String ss :arr)
            {
                removeJob(ss, "system");
                JobDetail j = new JobDetail(ss, "system", Class.forName(CfgMgr.getStr("quartz."+ss+".class")), true, false, false);
                j.getJobDataMap().putAll(CfgMgr.getSubProp("quartz."+ss+"."));
                scheduler.addJob(j, true);
                int i=1;
                while(true)
                {
                    String k = "quartz."+ss+".cron"+i;
                    if(!CfgMgr.getProp().containsKey(k))
                        break;
                    CronTrigger t = new CronTrigger(ss+i, "system", ss, "system", CfgMgr.getStr(k));
                    scheduler.scheduleJob(t);
                    i++;
                }
            }
        } catch (Exception e)
        {
            Log.error(e);
        }

    }

    public static void removeJob(String name, String group)
    {
        try
        {
            scheduler.unscheduleJob(name, group);
            scheduler.deleteJob(name, group);

        } catch (Exception e)
        {
            Log.error(e);
        }
    }

    public static void triggerJob(String name, String group)
    {
        try
        {
            scheduler.triggerJob(name, group);
        } catch (Exception e)
        {
            Log.error(e);
        }
    }
}
