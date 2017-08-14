package com.mu.stock.chart;

import com.mu.stock.ui.ChartView;
import com.mu.util.DateUtil;
import com.mu.util.StringUtil;
import com.mu.util.ui.ChartUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.MouseInputAdapter;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.DefaultXYDataset;

/**
 *
 * @author Peng Mu
 */
public class GroupDrawer extends MouseInputAdapter
{

    private boolean isActive;
    private DrawType type;
    private ChartView cv;
    Point lastPoint;
    DefaultXYDataset hdDS;
    static public XYSplineRenderer hdRenderer = new XYSplineRenderer();

    List<Point> points = new ArrayList<Point>();
    XYLineAnnotation tmpAtn;

    public GroupDrawer(ChartView cv)
    {
        this.cv = cv;
        hdRenderer.setBaseShapesVisible(false);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        cv.getPanel().requestFocusInWindow();
        if (!isActive)
        {
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            points.add(e.getPoint());
            processPressed();
        }
    }
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (!isActive)
        {
            return;
        }
        cv.getPanel().setDomainZoomable(false);
        points.add(e.getPoint());
        processDragged();
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {

        if(e.isPopupTrigger())
        {
            cv.getPanel().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
            lastPoint = e.getPoint();
        }
        else if(isActive && e.getButton() == MouseEvent.BUTTON1)
        {
            //points.add(e.getPoint());
            processReleased();
        }
    }
    
    private void processReleased()
    {
        XYPlot p = ChartUtil.getPlotByPoint(cv.getPanel(), points.get(points.size() - 1));
        String plotName = cv.getPlotName(p);
        Indicator ih=null;
        if (type == DrawType.Line && points.size() >= 2)
        {
            ih = new Indicator("LineDraw", getLineDrawParam(points.get(0), points.get(points.size()-1)), 1, plotName, null, IDType.LineDraw);
        }
        else if(type == DrawType.HandDraw && points.size() >= 1)
        {
            String k = System.currentTimeMillis()+"";
            cv.getDataMgr().addHandDraw(k, hdDS);
            ih = new Indicator("HandDraw", k, 1, plotName,hdRenderer.getClass(),IDType.HandDraw);
        }



        if(ih!=null)
        {
            cv.addToIndicatorList(ih);
            cv.updateChart();
            reset();
        }
    }
    private void processDragged()
    {
        if(points.isEmpty())
            return;
        XYPlot p = ChartUtil.getPlotByPoint(cv.getPanel(), points.get(points.size() - 1));
        if (type == DrawType.Line && points.size() >= 1)
        {
            Point p1 = points.get(0);
            Point p2 = points.get(points.size()-1);
            if (tmpAtn != null)
            {
                p.removeAnnotation(tmpAtn);
            }
            tmpAtn = getAnnotation(p1, p2);
            p.addAnnotation(tmpAtn);
        }
        else if(type == DrawType.HandDraw && points.size() >= 1)
        {
            double[][] tmp = new double[2][points.size()];
            for(int j=0; j<points.size(); j++)
            {
                tmp[0][j]=ChartUtil.x2val(cv.getPanel(), points.get(j));
                tmp[1][j]=ChartUtil.y2val(cv.getPanel(), points.get(j));
            }
            if(hdDS==null)
            {
                hdDS = new DefaultXYDataset();
                int c = p.getDatasetCount();
                p.setDataset(c, hdDS);
                p.setRenderer(c, hdRenderer);
                hdRenderer.setSeriesPaint(c, Color.blue);
                hdRenderer.setSeriesStroke(c, new BasicStroke());
            }
            hdDS.removeSeries(0);
            hdDS.addSeries(0, tmp);

        }
    }
    private void processPressed()
    {
        if(points.isEmpty())
            return;
        Indicator ih=null;
        XYPlot p = ChartUtil.getPlotByPoint(cv.getPanel(), points.get(points.size() - 1));
        String plotName = cv.getPlotName(p);
        if(type == DrawType.TrisH && points.size()>=2)
        {
            String param = getHSplitParam(points.get(0), points.get(1), 3);
            ih = new Indicator("TrisH", param, 1, plotName, null, IDType.HMark);
        }
        else if(type == DrawType.QuartH && points.size()>=2)
        {
            String param = getHSplitParam(points.get(0), points.get(1), 4);
            ih = new Indicator("QuartH", param, 1, plotName, null, IDType.HMark);
        }
        else if(type == DrawType.MidH && points.size()>=2)
        {
            String param = getHSplitParam(points.get(0), points.get(1), 2);
            ih = new Indicator("MidH", param, 1, plotName, null, IDType.HMark);
        }
        else if(type == DrawType.TrisS && points.size()>=2)
        {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            double[] arr = split(p1.y, p2.y, 3);
            String param = getLinearParam(p1, p2);
            param += "|" + getLinearParam(p1, new Point(p2.x, Double.valueOf(arr[1]).intValue()));
            param += "|" + getLinearParam(p1, new Point(p2.x, Double.valueOf(arr[2]).intValue()));
            ih = new Indicator("LineEx", param, 1, plotName, StandardXYItemRenderer.class, IDType.Indicator);

        }
        else if(type == DrawType.FibonacciFans && points.size()>=2)
        {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            double[] arr = splitFibonacci(p2.y-p1.y);
            String param = getLinearParam(p1, p2);
            param += "|" + getLinearParam(p1, new Point(p2.x, p2.y - Double.valueOf(arr[1]).intValue()));
            param += "|" + getLinearParam(p1, new Point(p2.x, p2.y - Double.valueOf(arr[2]).intValue()));
            param += "|" + getLinearParam(p1, new Point(p2.x, p2.y - Double.valueOf(arr[3]).intValue()));
            ih = new Indicator("FbnFan", param, 1, plotName, StandardXYItemRenderer.class, IDType.Indicator);
            
        }
        else if(type == DrawType.FibonacciRetreatment && points.size()>=2)
        {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            double x1 = ChartUtil.x2val(cv.getPanel(), p1);
            double y1 = ChartUtil.y2val(cv.getPanel(), p1);
            double y2 = ChartUtil.y2val(cv.getPanel(), p2);
            double[] arr = splitFibonacci(y2-y1);
            String param = getFbnRetParam(x1, y2, "0%");
            for(int i=0; i<6; i++)
                param += "|" + getFbnRetParam(x1, y2-arr[i], "");
            ih = new Indicator("FbnRet", param, 1, plotName, StandardXYItemRenderer.class, IDType.Indicator);

        }
        else if(type == DrawType.FibonacciProjection && points.size()>=3)
        {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            Point p3 = points.get(2);
            double x1 = ChartUtil.x2val(cv.getPanel(), p1);
            double y1 = ChartUtil.y2val(cv.getPanel(), p1);
            double y2 = ChartUtil.y2val(cv.getPanel(), p2);
            double y3 = ChartUtil.y2val(cv.getPanel(), p3);
            double[] arr = splitFibonacci(y2-y1);
            String param = getFbnRetParam(x1, y3, "0%");
            for(int i=0; i<9; i++)
                param += "|" + getFbnRetParam(x1, y3+arr[i], "");
            ih = new Indicator("FbnProj", param, 1, plotName, StandardXYItemRenderer.class, IDType.Indicator);

        }
        else if(type == DrawType.FibonacciTimeZone && points.size()>=1)
        {
            Point p1 = points.get(0);
            double x1 = ChartUtil.x2val(cv.getPanel(), p1);
            int start = cv.getDataMgr().getDateIndex(new Date((long)x1));
            int[] arr = getFibonacciArr(15);
            String param = "";
            for(int i=0; i<arr.length; i++)
                param += getFbnTZParam(start, arr[i])+"|";
            param = StringUtil.trimRight(param, "|");
            ih = new Indicator("FbnTZ", param, 1, plotName, StandardXYItemRenderer.class, IDType.VMark);

        }
        else if(type == DrawType.FibonacciCircle && points.size()>=2)
        {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            Point[] arr = getFibonacciCirclePoint(p1, p2);
            String param="";
            for(int i=0; i<arr.length; i++)
                param += getArcParam(p2, arr[i], 0,360)+"|";
            param = StringUtil.trimRight(param, "|");
            ih = new Indicator("FbnCc", param, 1, plotName, StandardXYItemRenderer.class, IDType.Arc);

        }
        else if(type == DrawType.LineEx && points.size()>=2)
        {
            String param = getLinearParam(points.get(0), points.get(1));
            ih = new Indicator("LineEx", param, 1, plotName, StandardXYItemRenderer.class, IDType.Indicator);
        }
        else if(type == DrawType.Pitchfork && points.size()>=3)
        {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            Point p3 = points.get(2);
            Point p4 = new Point((p2.x+p3.x)/2, (p2.y+p3.y)/2);
            double slope = getLinearSlope(p1, p4);

            String param=getLinearParam(p1, p4)+"|";
            param += getLinearParam(p2, slope)+"|";
            param += getLinearParam(p3, slope);
            ih = new Indicator("Pitchfork", param, 1, plotName, StandardXYItemRenderer.class, IDType.Indicator);

        }

        if(ih!=null)
        {
            cv.addToIndicatorList(ih);
            cv.updateChart();
            reset();
        }

    }
    private String getFbnTZParam(int start, int interval)
    {
        Date d = cv.getDataMgr().getDateByIndex(start+interval);
        if(d==null)
            return "";
        return String.format("v=%d&color=blue", d.getTime());
    }

    private String getFbnRetParam(double x, double y, String label)
    {
        return String.format("y=%f&xs=%s&color=blue", y, DateUtil.toDateStr(new Date(Double.valueOf(x).longValue())));
    }
    private String getLineDrawParam(Point p1, Point p2)
    {
        double x0 = ChartUtil.x2val(cv.getPanel(), p1);
        double y0 = ChartUtil.y2val(cv.getPanel(), p1);
        double x1 = ChartUtil.x2val(cv.getPanel(), p2);
        double y1 = ChartUtil.y2val(cv.getPanel(), p2);
        return String.format("v=%f,%f,%f,%f&color=blue", x0,y0,x1,y1);
    }

    private XYLineAnnotation getAnnotation(Point p1, Point p2)
    {
        double x0 = ChartUtil.x2val(cv.getPanel(), p1);
        double y0 = ChartUtil.y2val(cv.getPanel(), p1);
        double x1 = ChartUtil.x2val(cv.getPanel(), p2);
        double y1 = ChartUtil.y2val(cv.getPanel(), p2);
        return new XYLineAnnotation(x0, y0, x1, y1, new BasicStroke(), Color.blue);
     }
    private String getLinearParam(Point p1, Point p2)
    {
        double x0 = ChartUtil.x2val(cv.getPanel(), p1);
        double y0 = ChartUtil.y2val(cv.getPanel(), p1);
        double x1 = ChartUtil.x2val(cv.getPanel(), p2);
        double y1 = ChartUtil.y2val(cv.getPanel(), p2);
        int d0 = cv.getDataMgr().getDateIndex(new Date(Double.valueOf(x0).longValue()));
        int d1 = cv.getDataMgr().getDateIndex(new Date(Double.valueOf(x1).longValue()));
        String param = cv.getDataMgr().getLinearEquation(d0, y0, d1, y1)+"&color=blue&xs="+DateUtil.toDateStr(new Date(Double.valueOf(Math.min(x0, x1)).longValue()));
        return param;
     }
    private String getLinearParam(Point p1, double slope)
    {
        double x0 = ChartUtil.x2val(cv.getPanel(), p1);
        double y0 = ChartUtil.y2val(cv.getPanel(), p1);
        int d0 = cv.getDataMgr().getDateIndex(new Date(Double.valueOf(x0).longValue()));
        String param = cv.getDataMgr().getLinearEquation(d0, y0, slope)+"&color=blue&xs="+DateUtil.toDateStr(new Date(Double.valueOf(x0).longValue()));
        return param;
     }
    private double getLinearSlope(Point p1, Point p2)
    {
        double x0 = ChartUtil.x2val(cv.getPanel(), p1);
        double y0 = ChartUtil.y2val(cv.getPanel(), p1);
        double x1 = ChartUtil.x2val(cv.getPanel(), p2);
        double y1 = ChartUtil.y2val(cv.getPanel(), p2);
        int d0 = cv.getDataMgr().getDateIndex(new Date(Double.valueOf(x0).longValue()));
        int d1 = cv.getDataMgr().getDateIndex(new Date(Double.valueOf(x1).longValue()));
        return cv.getDataMgr().getLinearSlope(d0, y0, d1, y1);
     }
    private String getHSplitParam(Point p1, Point p2, int divideBy)
    {
        double[] arr = split(p1, p2, divideBy);
        String param = "";
        for(double d: arr)
            param += String.format("v=%.3f&color=blue|", d);
        param = StringUtil.trimRight(param, "|");
        return param;
    }
    private double[] split(Point p1, Point p2, int divideBy)
    {
        double y1 = ChartUtil.y2val(cv.getPanel(), p1);
        double y2 = ChartUtil.y2val(cv.getPanel(), p2);
        return split(y1, y2, divideBy);
    }
    private double[] split(double y1, double y2, int divideBy)
    {
        double re[] = new double[1+divideBy];
        double delta = Math.abs(y1-y2)/divideBy;
        double h = Math.max(y1, y2);
        for(int i=0; i<re.length; i++)
            re[i]=h-delta*i;

        return re;
    }
    private double[] splitFibonacci(double y)
    {
        double re[] = new double[9];
        int i =0;
        re[i++] = y*0.236;
        re[i++] = y*0.382;
        re[i++] = y*0.5;
        re[i++] = y*0.618;
        re[i++] = y*0.786;
        re[i++] = y;
        re[i++] = y*1.618;
        re[i++] = y*2.618;
        re[i++] = y*4.2368;
        return re;
    }
    private int[] getFibonacciArr(int size)
    {
        if(size<2)
            return new int[]{1};
        int re[] = new int[size];
        re[0]=1;
        re[1]=1;
        for(int i=2; i<size; i++)
            re[i]=re[i-1]+re[i-2];
        return re;
    }
    private Point[] getFibonacciCirclePoint(Point p1, Point p2)
    {
        int xd = Math.abs(p1.x - p2.x);
        int yd = Math.abs(p1.y - p2.y);
        int m = p2.x > p1.x? -1:1;
        int n = p2.y > p1.y? -1:1;
        Point[] re = new Point[6];
        re[0] = new Point((int)(p2.x+m*xd*0.382), (int)(p2.y+n*yd*0.382));
        re[1] = new Point((int)(p2.x+m*xd*0.5), (int)(p2.y+n*yd*0.5));
        re[2] = new Point((int)(p2.x+m*xd*0.618), (int)(p2.y+n*yd*0.618));
        re[3] = new Point((int)(p2.x+m*xd*1), (int)(p2.y+n*yd*1));
        re[4] = new Point((int)(p2.x+m*xd*1.618), (int)(p2.y+n*yd*1.618));
        re[5] = new Point((int)(p2.x+m*xd*2.618), (int)(p2.y+n*yd*2.618));
        return re;
    }
    private String getArcParam(Point center, Point pointOnCircle, double startDegree, double extendDegree)
    {

        double r = Math.sqrt(Math.pow((center.x-pointOnCircle.x),2) + Math.pow((center.y-pointOnCircle.y),2));
        double x0 = ChartUtil.x2val(cv.getPanel(), center);
        double y0 = ChartUtil.y2val(cv.getPanel(), center);
        double xu = ChartUtil.len2x(cv.getPanel(), center, 1);
        double yu = ChartUtil.len2y(cv.getPanel(), center, 1);
        double xd = 1.4;
        double x = x0-xd*r/xu;
        double y = y0-r/yu;
        double w =  2*xd*r/xu;
        double h =  2*r/yu;


        /*
        Point tl = new Point(center.x-(int)r, center.y+(int)r);
        Point br = new Point(center.x+(int)r, center.y-(int)r);
        ChartPanel chartPanel = cv.getPanel();
        double x = ChartUtil.x2val(chartPanel, tl);
        double y = ChartUtil.y2val(chartPanel, tl);
        double xb = ChartUtil.x2val(chartPanel, br);
        double yb = ChartUtil.y2val(chartPanel, br);
        double w = Math.abs(xb-x);
        double h =  Math.abs(y-yb) ;
        //Arc2D.Double arc = new Arc2D.Double(x, y, w, h, startDegree, extendDegree, Arc2D.OPEN);
         *
         */

        return String.format("v=%f,%f,%f,%f,%f,%f,%d", x, y, w, h, startDegree, extendDegree, Arc2D.OPEN);
    }

    public ChartView getCv()
    {
        return cv;
    }

    public void setCv(ChartView cv)
    {
        this.cv = cv;
    }

    public boolean isIsActive()
    {
        return isActive;
    }

    public void setIsActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public List<Point> getPoints()
    {
        return points;
    }

    public void setPoints(List<Point> points)
    {
        this.points = points;
    }

    public DrawType getType()
    {
        return type;
    }

    public void setType(DrawType type)
    {
        this.type = type;
        isActive = true;
        points.clear();
    }

    private void reset()
    {
        cv.getPanel().setDomainZoomable(true);
        isActive = false;
        points.clear();
        cv.resetButtonGroup();
        tmpAtn=null;
        hdDS=null;

    }

    public Point getLastPoint()
    {
        return lastPoint;
    }

    public void setLastPoint(Point lastPoint)
    {
        this.lastPoint = lastPoint;
    }

}
