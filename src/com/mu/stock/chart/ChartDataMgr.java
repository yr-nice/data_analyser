package com.mu.stock.chart;

import com.mu.stock.dao.DailyPriceDAO;
import com.mu.stock.entity.Company;
import com.mu.stock.entity.DailyPriceLog;
import com.mu.stock.math.func.ADL;
import com.mu.stock.math.func.ATR;
import com.mu.stock.math.func.Aroon;
import com.mu.stock.math.func.ArrAbs;
import com.mu.stock.math.func.ArrAdd;
import com.mu.stock.math.func.ArrDiv;
import com.mu.stock.math.func.ArrItem;
import com.mu.stock.math.func.ArrMulti;
import com.mu.stock.math.func.ArrPow;
import com.mu.stock.math.func.ArrSubtract;
import com.mu.stock.math.func.CMO;
import com.mu.stock.math.func.ChaikinMF;
import com.mu.stock.math.func.ChaikinO;
import com.mu.stock.math.func.ChaikinV;
import com.mu.stock.math.func.DEMA;
import com.mu.stock.math.func.DPO;
import com.mu.stock.math.func.EMA;
import com.mu.stock.math.func.Gravity;
import com.mu.stock.math.func.IMI;
import com.mu.stock.math.func.MAX;
import com.mu.stock.math.func.MFI;
import com.mu.stock.math.func.MIN;
import com.mu.stock.math.func.MassIndex;
import com.mu.stock.math.func.Momentum;
import com.mu.stock.math.func.OBV;
import com.mu.stock.math.func.PVT;
import com.mu.stock.math.func.PriceROC;
import com.mu.stock.math.func.RMI;
import com.mu.stock.math.func.SMA;
import com.mu.stock.math.func.StandardDeviation;
import com.mu.stock.math.func.StochasticR;
import com.mu.stock.math.func.Trix;
import com.mu.stock.math.func.VHF;
import com.mu.stock.math.func.WilliamsR;
import com.mu.util.DateUtil;
import com.mu.util.ui.ColorUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.nfunk.jep.JEP;

/**
 *
 * @author Peng Mu
 */
public class ChartDataMgr
{
    JEP parser = new JEP();
    private Company cpy;
    private int dateDrift;
    private float priceDrift;
    private List<DailyPriceLog> all;
    private Date[] arrDate;
    private float[] arrClose;
    private float[] arrHigh;
    private float[] arrLow;
    private float[] arrOpen;
    private float[] arrVol;
    private float[] arrDayCount;
    private HashMap<Date, Integer> d2i = new HashMap<Date, Integer>();
    private HashMap<String, float[]> cache = new HashMap<String, float[]>();
    private HashMap<String, XYDataset> handDraws = new HashMap<String, XYDataset>();
    private ArrayList<Date> excludeDates = new ArrayList<Date>();

    public ChartDataMgr(Company cpy, int dateDrift, float priceDrift)
    {
        this.cpy = cpy;
        this.dateDrift = dateDrift;
        this.priceDrift = priceDrift;
        all = DailyPriceDAO.getByCompany(cpy, null, null);
        int s = all.size();
        arrDate = new Date[s];
        arrClose = new float[s];
        arrHigh = new float[s];
        arrLow = new float[s];
        arrOpen = new float[s];
        arrVol = new float[s];
        arrDayCount = new float[s];
        //HashSet<String> days = new HashSet<String>();
        int i=0;
        for(DailyPriceLog d : all)
        {
            arrDate[i] = DateUtil.nextNDay(d.getDate(),dateDrift);
            arrHigh[i] = d.getAdjHigh()*priceDrift;
            arrLow[i] = d.getAdjLow()*priceDrift;
            arrOpen[i] = d.getAdjOpen()*priceDrift;
            arrClose[i] = d.getAdjClose()*priceDrift;
            arrVol[i] = d.getVolume();
            arrDayCount[i] = i+1;
            d2i.put(arrDate[i], Integer.valueOf(i));
            //days.add(DateUtil.toDateStr(arrDate[i]));
            i++;
        }
        /*Date d = arrDate[0];
        while(d.before(arrDate[arrDate.length-1]))
        {
            if(!days.contains(DateUtil.toDateStr(d)))
                excludeDates.add(d);
            d = DateUtil.nextDay(d);
        }*/

        cache.put("h", arrHigh);
        cache.put("l", arrLow);
        cache.put("o", arrOpen);
        cache.put("c", arrClose);
        cache.put("v", arrVol);
        cache.put("x", arrDayCount);

        parser.addFunction("ai", new ArrItem());
        parser.addFunction("ap", new ArrPow());
        parser.addFunction("aa", new ArrAdd());
        parser.addFunction("as", new ArrSubtract());
        parser.addFunction("am", new ArrMulti());
        parser.addFunction("ad", new ArrDiv());
        parser.addFunction("abs", new ArrAbs());
        parser.addFunction("sma", new SMA());
        parser.addFunction("ema", new EMA());
        parser.addFunction("sp", new StandardDeviation());
        parser.addFunction("obv", new OBV());
        parser.addFunction("mfi", new MFI());
        parser.addFunction("adl", new ADL());
        parser.addFunction("atr", new ATR());
        parser.addFunction("aroon", new Aroon());
        parser.addFunction("cko", new ChaikinO());
        parser.addFunction("ckv", new ChaikinV());
        parser.addFunction("ckm", new ChaikinMF());
        parser.addFunction("cmo", new CMO());
        parser.addFunction("dpo", new DPO());
        parser.addFunction("dema", new DEMA());
        parser.addFunction("mtm", new Momentum());
        parser.addFunction("imi", new IMI());
        parser.addFunction("mi", new MassIndex());
        parser.addFunction("pvt", new PVT());
        parser.addFunction("roc", new PriceROC());
        parser.addFunction("rmi", new RMI());
        parser.addFunction("stor", new StochasticR());
        parser.addFunction("trix", new Trix());
        parser.addFunction("wlmr", new WilliamsR());
        parser.addFunction("max", new MAX());
        parser.addFunction("min", new MIN());
        parser.addFunction("vhf", new VHF());
        parser.addFunction("gvt", new Gravity());
        parser.addVariable("h", arrHigh);
        parser.addVariable("l", arrLow);
        parser.addVariable("o", arrOpen);
        parser.addVariable("c", arrClose);
        parser.addVariable("v", arrVol);
        parser.addVariable("x", arrDayCount);
    }

    public void addCpyData(Company cp)
    {
        List<DailyPriceLog> a = DailyPriceDAO.getByCompany(cp, all.get(0).getDate(), all.get(all.size()-1).getDate());
        int s = all.size();
        float[] c = new float[s];
        float[] h  = new float[s];
        float[] l  = new float[s];
        float[] o  = new float[s];
        float[] v  = new float[s];
        for(DailyPriceLog d : a)
        {
            Date dt = DateUtil.nextNDay(d.getDate(),dateDrift);
            if(!d2i.containsKey(dt))
                continue;
            int i=d2i.get(dt);
            h[i] = d.getAdjHigh()*priceDrift;
            l[i] = d.getAdjLow()*priceDrift;
            o[i] = d.getAdjOpen()*priceDrift;
            c[i] = d.getAdjClose()*priceDrift;
            v[i] = d.getVolume();
        }
        String suf = "."+cp.getShortName();
        cache.put("h"+suf, h);
        cache.put("l"+suf, l);
        cache.put("o"+suf, o);
        cache.put("c"+suf, c);
        cache.put("v"+suf, v);
        parser.addVariable("h"+suf, h);
        parser.addVariable("l"+suf, l);
        parser.addVariable("o"+suf, o);
        parser.addVariable("c"+suf, c);
        parser.addVariable("v"+suf, v);
    }
    public OHLCDataset getHL(String params, Collection<Date> list)
    {

        Map cfg = paramStr2Map(params).get(0);
        String[] v = cfg.get("y").toString().split(",");
        float[] h = cache.get(v[0].trim());
        float[] l = cache.get(v[1].trim());
        float[] o = cache.get(v[2].trim());
        float[] c = cache.get(v[3].trim());
        float[] vl = cache.get(v[4].trim());

        int len = 0;
        for(Date d : list)
        {
            int j = d2i.get(d);
            if(c[j]>0)
                len++;
        }
        Date[] date = new Date[len];
        double[] high = new double[len];
        double[] low = new double[len];
        double[] open = new double[len];
        double[] close = new double[len];
        double[] volume = new double[len];

        int i=0;
        for(Date d : list)
        {
            //d = DateUtil.nextNDay(d, dateDrift);
            int j = d2i.get(d);
            if(c[j]<=0)
                continue;
            date[i] = d;
            high[i] = h[j];
            low[i] = l[j];
            open[i] = o[j];
            close[i] = c[j];
            volume[i] = vl[j];
            i++;
        }
        return new DefaultHighLowDataset("cs", date, high, low, open, close, volume);
    }
    public XYDataset getDateset(Collection<Date> list, String params, XYItemRenderer renderer)
    {

        if(renderer.getClass() == CandlestickRenderer.class)
        {
            CandlestickRenderer cRender = (CandlestickRenderer)renderer;
            cRender.setUpPaint(Color.WHITE);
            cRender.setDownPaint(Color.BLACK);
            cRender.setUseOutlinePaint(true);
            //cRender.setMaxCandleWidthInMilliseconds(12*3600*1000);
            cRender.setBaseOutlinePaint(Color.BLACK);


            return getHL(params, list);
        }
        else
        {
            List<Map> seriesCfg = paramStr2Map(params);
            DefaultXYDataset ds = new DefaultXYDataset();;

            ArrayList<double[][]> series = new ArrayList<double[][]>();
            int i=0;
            for(Map m : seriesCfg)
            {
                double[][] arr = get(list, m);
                series.add(arr);
                if(m.containsKey("color"))
                    renderer.setSeriesPaint(i, ColorUtil.s2c(m.get("color").toString()));
                ds.addSeries(i++, arr);
            }

            XYDataset re = ds;
            if(renderer.getClass() == XYBarRenderer.class)
            {
                ((XYBarRenderer)renderer).setShadowVisible(false);
                re = new XYBarDataset(ds, 20 * 60 * 60 * 1000);
            }
            return re;
        }

    }
    public List<Map> paramStr2Map(String params)
    {
        String[] serArr = params.split("\\|");
        ArrayList<Map> seriesCfg = new ArrayList<Map>();
        for(String s : serArr)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            String[] kv = s.split("&");
            for(String ss : kv)
            {
                String[] rr = ss.split("=");
                map.put(rr[0], rr[1]);
            }
            seriesCfg.add(map);
        }
        return seriesCfg;

    }
    public double[][] get(Collection<Date> list, Map cfg)
    {
        List<Date> ll = new ArrayList<Date>();
        ll.addAll(list);
        Date start = DateUtil.toDate((String)cfg.get("xs"));
        Date end = DateUtil.toDate((String)cfg.get("xe"));
        for(Date d : list)
        {
            if(start!=null&&d.before(start))
                ll.remove(d);
            if(end!=null&&d.after(end))
                ll.remove(d);
        }

        double[][] re = new double[2][ll.size()];
        String exp = cfg.get("y").toString();
        Object f=null;
        if(cache.containsKey(exp))
        {
            f = cache.get(exp);
        }
        else
        {
            parser.parseExpression(cfg.get("y").toString());
            f = parser.getValueAsObject();
        }
        int dateDelta =0;
        if(cfg.containsKey("xd"))
            dateDelta += Integer.valueOf(cfg.get("xd").toString());

        int i=0;
        for(Date d : ll)
        {
            //d = DateUtil.nextNDay(d, dateDrift);
            int j = d2i.get(d);
            re[0][i] = DateUtil.nextNDay(d, dateDelta).getTime();
            if(f instanceof Double)
                re[1][i] = (Double)f;
            else
                re[1][i] = ((float[])f)[j];
            i++;
        }
        return re;
    }

    public List<ValueMarker> getMarker(String params)
    {
        List<ValueMarker> re = new ArrayList<ValueMarker>();
        List<Map> cfgs = paramStr2Map(params);
        for(Map cfg : cfgs)
        {
            double v = Double.valueOf(cfg.get("v").toString());
            Color cl = Color.BLACK;
            if(cfg.containsKey("color"))
                cl = ColorUtil.s2c(cfg.get("color").toString());
            ValueMarker m = new ValueMarker(v, cl, new BasicStroke());
            re.add(m);

        }
        return re;
    }
    public List<XYLineAnnotation> getAnnotation(String params)
    {
        List<XYLineAnnotation> re = new ArrayList<XYLineAnnotation>();
        List<Map> cfgs = paramStr2Map(params);
        for(Map cfg : cfgs)
        {
            String[] v = cfg.get("v").toString().split(",");
            double x1 = Double.valueOf(v[0].trim());
            double y1 = Double.valueOf(v[1].trim());
            double x2 = Double.valueOf(v[2].trim());
            double y2 = Double.valueOf(v[3].trim());

            Color cl = Color.BLUE;
            if(cfg.containsKey("color"))
                cl = ColorUtil.s2c(cfg.get("color").toString());
            XYLineAnnotation m = new XYLineAnnotation(x1, y1, x2, y2, new BasicStroke(), cl);
            re.add(m);
        }
        return re;
    }
    public List<XYShapeAnnotation> getArcAnnotation(String params)
    {
        List<XYShapeAnnotation> re = new ArrayList<XYShapeAnnotation>();
        List<Map> cfgs = paramStr2Map(params);
        for(Map cfg : cfgs)
        {
            String[] v = cfg.get("v").toString().split(",");
            double x = Double.valueOf(v[0].trim());
            double y = Double.valueOf(v[1].trim());
            double w = Double.valueOf(v[2].trim());
            double h = Double.valueOf(v[3].trim());
            double s = Double.valueOf(v[4].trim());
            double e = Double.valueOf(v[5].trim());
            int t = Integer.valueOf(v[6].trim());

            Color cl = Color.BLUE;
            if(cfg.containsKey("color"))
                cl = ColorUtil.s2c(cfg.get("color").toString());
            XYShapeAnnotation m = new XYShapeAnnotation(new Arc2D.Double(x,y,w,h,s,e,t), new BasicStroke(), cl);
            re.add(m);
        }
        return re;
    }
    public void addHandDraw(String key, XYDataset ds)
    {
        handDraws.put(key, ds);
    }

    public XYDataset getHandDraw(String key)
    {
        return handDraws.get(key);
    }

    public ArrayList<Date> getExcludeDates()
    {
        return excludeDates;
    }

    public int getWeekDayDrift()
    {
        return dateDrift%7;
    }
    public List<Date> getDaysUpto(Date from, Date upto)
    {
        List<Date> re = new ArrayList<Date>();
        for(Date d : arrDate)
        {
            if(d.getTime()>=from.getTime()&&d.getTime()<=upto.getTime())
                re.add(d);
            else if(d.getTime()>upto.getTime())
                return re;
        }
        return re;
    }
    public int getDateIndex(Date date)
    {
        int i = 1;
        for(Date d : arrDate)
        {
            if(d.before(date))
                i++;
            else
                return i;
        }
        return i;
    }
    public Date getDateByIndex(int i)
    {
        if(i>arrDate.length||i<1)
            return null;
        return arrDate[i-1];
    }

    static public String getLinearEquation(double x1, double y1, double x2, double y2)
    {
        double a = (y1-y2)/(x1-x2);
        double b = y1-a*x1;
        return String.format("y=aa(am(x,%.8f),%.8f)", a, b);
    }

    static public double getLinearSlope(double x1, double y1, double x2, double y2)
    {
        double a = (y1-y2)/(x1-x2);
        return a;
    }

    static public String getLinearEquation(double x1, double y1, double slope)
    {
        double b = y1-slope*x1;
        return String.format("y=aa(am(x,%.8f),%.8f)", slope, b);
    }
}
