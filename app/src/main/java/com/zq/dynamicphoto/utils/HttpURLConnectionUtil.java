package com.zq.dynamicphoto.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/2/25.
 */

public class HttpURLConnectionUtil {
    public static String get(String url , int readTimeout) throws Exception
    {
        HttpURLConnection c = null;
        BufferedReader rd = null;
        InputStream in = null;
        String responeString = null;
        try
        {
            c = (HttpURLConnection) new URL(url).openConnection();
            c.setReadTimeout(readTimeout);
            c.setDoOutput(true);
            c.setDoInput(true);
            c.setUseCaches(false);
            c.setRequestMethod("GET");
            c.setRequestProperty("content-type", "text/html");
            c.setDoOutput(true);
            in = c.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = in.read(buf)) != -1)
            {
                baos.write(buf, 0, len);
            }

            //responeString = baos.toString("utf-8");

            responeString =new String(baos.toByteArray(),"utf-8");



            return responeString;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if(rd != null)
            {
                try{rd.close();}catch(Exception e){}
            }
            if(in != null)
            {
                try{in.close();}catch(Exception e){}
            }
            if(c != null)
            {
                try{c.disconnect();}catch(Exception e){}
            }
        }
    }
}
