/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mu.util;

import com.mu.util.log.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author Peng mu
 */
public class WebUtil
{
    static public void main(String[] argu) throws Exception
    {
        //Log.log(getUrlFileSize("http://203.208.206.19/v.cctv.com/flash/media/qgds/2009/05/qgds_null_20090522_295.mp4"));
        //isResumable("http://203.208.206.19/v.cctv.com/flash/media/qgds/2009/05/qgds_null_20090522_295.mp4");

        //Log.log(getWebPage("http://space.tv.cctv.com/playcfg/CCTVvideoplayer.swf"));
        //downloadFile("http://space.tv.cctv.com/playcfg/CCTVvideoplayer.swf", "c:\\temp\\CCTVvideoplayer.swf");
        HashMap<String,String> prop = new HashMap<String,String>();
        //prop.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6 GTB6 (.NET CLR 3.5.30729)");
        //prop.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; SV1; GTB6.3; SLCC1; .NET CLR 2.0.50727; CIBA; .NET CLR 3.5.30729; .NET CLR 3.0.30618)");
        //prop.put("Referer", "http://tv.sohu.com/upload/swf/20091230/Player.swf");
        prop.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15");

        //prop.put("ownerPodcastId", "PODC1197969283610142");
        //prop.put("Referer", "http://space.dianshiju.cctv.com/podcast/shibingtuji");
        //prop.put("currpage", "2");

        /*String episodePattern = "<div class=\"text\">\\s*<a href=\"/video/(VIDE[0-9]+)\"" +                   // video id
                                         " +target=\"_blank\" +title *= *\"(.*?)\">" + "";            // title

        String s = getWebPage("http://space.dianshiju.cctv.com/act/platform/view/page/showElement.jsp?para_for_refresh=1260848314499", "GB2312", prop);
        Log.log(s);
        ArrayList<String> arr = RegexUtil.getAllMatch(s, episodePattern);
        Log.log(arr.size());
        Log.logCollection(arr);
        String s = getWebPage("http://tv.sohu.com/20090116/n261794870.shtml", "GB2312");
        Log.log(s);
        String ptn = "var vid *= *\"([0-9]+)\".*" +
            "var pid *= *\"([0-9]+)\"";
        ArrayList<String> arr = RegexUtil.getAllUniqueMatch(s, ptn);
        Log.logCollection(arr);*/

        //String epiStr = WebUtil.getWebPage(String.format("http://hot.vrs.sohu.com/vrs_videolist.action?vid=%s&pid=%s", "1110", "261792495"), "GB2312");
        //Log.log(epiStr);
        //String ptn = "\"videoId\":([0-9]+).+?" +
        //    "\"videoName\":\"(.+?)\"";
        //ArrayList<String> arr = RegexUtil.getAllUniqueMatch(epiStr, ptn);
        //Log.info(arr);

        downloadFile("http://114.80.235.81/f4v/15/74711415.h264_2.f4v?key=1c79e05030b9ead705ffd54dbf97f5cfce8b5e", "c:/temp/output.f4v", prop);

    }
    static public String getWebPage(String strUrl) throws Exception
    {
        URL url = new URL(strUrl);
        BufferedReader br = new BufferedReader( new InputStreamReader(url.openStream()));
        StringBuffer sb = new StringBuffer();
        while(true)
        {
            String tmp = br.readLine();
            if(tmp==null)
                    break;
            sb.append(tmp);
            sb.append('\n');
        }
        br.close();
        return sb.toString();

    }

    static public String getWebPage (String strUrl, String charset)throws Exception
	{
        URL url = new URL(strUrl);
        InputStream is = url.openStream();
        ByteArrayOutputStream o = new ByteArrayOutputStream();

        FileOutputStream fo = new FileOutputStream("c:/temp/out-str-2.txt", true);
        byte[] buff = new byte[1024];
        while(true)
        {
            int i = is.read(buff);
            if(i == -1) break;
            o.write(buff, 0, i);
            fo.write(buff, 0, i);
            //Thread.sleep(10);

        }
        fo.close();
        o.close();
        is.close();
        return o.toString(charset);

    }

    static public String getWebPage (String strUrl, String charset, HashMap<String,String> header)throws Exception
	{
        URL url = new URL(strUrl);
        HttpURLConnection hurl = (HttpURLConnection)url.openConnection();
        for(String key: header.keySet())
            hurl.setRequestProperty(key, header.get(key));
        System.out.printf("length=%d, type=%s, encoding=%s", hurl.getContentLength(),hurl.getContentType(), hurl.getContentEncoding());
        InputStream is = hurl.getInputStream();
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        while(true)
        {
            int i = is.read(buff);
            if(i == -1) break;
            o.write(buff, 0, i);
            //System.out.print(o.toString(charset));
        }
        o.close();
        is.close();
        return o.toString(charset);

    }

    static public String getUnzippedWebPage (String strUrl, String charset, HashMap<String,String> header)throws Exception
	{
        URL url = new URL(strUrl);
        HttpURLConnection hurl = (HttpURLConnection)url.openConnection();
        if(header != null)
            for(String key: header.keySet())
                hurl.setRequestProperty(key, header.get(key));
        System.out.printf("length=%d, type=%s, encoding=%s", hurl.getContentLength(),hurl.getContentType(), hurl.getContentEncoding());
        InputStream is = hurl.getInputStream();
        if( hurl.getContentEncoding() !=null &&  hurl.getContentEncoding().toLowerCase().indexOf("gzip")!=-1)
        {
            is = new GZIPInputStream(is);
        }
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        while(true)
        {
            int i = is.read(buff, 0, buff.length);
            if(i == -1) break;
            o.write(buff, 0, i);
            //System.out.print(o.toString(charset));
        }
        o.close();
        is.close();
        return o.toString(charset);

    }

	static public void downloadFile (String strUrl, String dest)throws Exception
	{
        URL url = new URL(strUrl);
        InputStream is = url.openStream();
        //ByteArrayInputStream ba = new ByteArrayInputStream(is);
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

    static public void downloadFile (String strUrl, String dest, HashMap<String,String> header)throws Exception
	{
        URL url = new URL(strUrl);
        HttpURLConnection hurl = (HttpURLConnection)url.openConnection();
        for(String key: header.keySet())
            hurl.setRequestProperty(key, header.get(key));
        System.out.printf("length=%d, type=%s, encoding=%s", hurl.getContentLength(),hurl.getContentType(), hurl.getContentEncoding());
        InputStream is = hurl.getInputStream();
        FileOutputStream o = new FileOutputStream(dest, true);
        byte[] buff = new byte[1024];
        while(true)
        {
            int i = is.read(buff);
            if(i == -1) break;
            o.write(buff, 0, i);
            //System.out.print(o.toString(charset));
        }
        o.close();
        is.close();
    }

    static public int getUrlFileSize(String url)
    {
        int re = 0;
        try{
        URL u = new URL(url);
        HttpURLConnection hu = (HttpURLConnection)u.openConnection();
        re = hu.getContentLength();
        }catch(Exception e){Log.log(e);}
        return re;
    }

    static public boolean isResumable(String url)
    {
        boolean re = false;
        try{
        URL u = new URL(url);
        HttpURLConnection hu = (HttpURLConnection)u.openConnection();
        Map m = hu.getHeaderFields();
        for(Object k : m.keySet())
            Log.log(k+"="+m.get(k));
        re = hu.getHeaderFields().containsKey("Accept-Ranges");
        }catch(Exception e){Log.log(e);}
        return re;
    }

}
