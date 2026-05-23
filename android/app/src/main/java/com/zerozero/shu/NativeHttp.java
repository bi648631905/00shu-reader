package com.zerozero.shu;

import android.webkit.JavascriptInterface;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class NativeHttp {

    @JavascriptInterface
    public String fetch(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setInstanceFollowRedirects(true);

            InputStream is;
            if (conn.getResponseCode() >= 400) {
                is = conn.getErrorStream();
            } else {
                is = conn.getInputStream();
            }

            String encoding = conn.getContentEncoding();
            if ("gzip".equals(encoding)) {
                is = new GZIPInputStream(is);
            }

            java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
            s.close();
            conn.disconnect();
            return result;
        } catch (Exception e) {
            return "ERROR:" + e.getMessage();
        }
    }
}
