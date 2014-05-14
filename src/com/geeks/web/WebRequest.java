package com.geeks.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebRequest {
	private static final String POST = "POST";
    public static final String K0 = "cookie";
    public static final String DATA = "data";
    protected static final String K1 = "accept-charset";
    protected static final String V1 = "ISO-8859-1,utf-8;q=0.7,*;q=0.7";
    public static final String K2 = "content-type";
    protected static final String V2 = "application/json; charset=utf-8";
    protected static final String K3 = "accept-encoding";
    protected static final String V3 = "gzip,deflate";
    protected static final String K4 = "User-Agent";
    protected static final String V4 = "Android";
    protected static final String HOST = "http://173.198.254.66:8000/pbx/";

    static protected String cookies = null;
    protected WebResponse wResponse = null;
    protected HttpURLConnection connection = null;
    protected JSONObject jsonret;
    
    /**
     * When request failed, we clear cookies.
     */
    public static void clearCookies() {
    	if (cookies != null) {
    		cookies = null;
    	}
    }

    public static String getCookies() {
    	return cookies;
    }

    protected WebRequest() {
    	jsonret = null;
    }

    public WebRequest(WebResponse webRes) {
    	this();
    	wResponse = webRes;
    }
    
    protected int handleRequest(final String urlString,
    		final String keyvaluestring) {
    	URL url = null;
    	try {
            url = new URL(urlString);
    	} catch (MalformedURLException e) {
            return -3;
    	}
    	StringBuilder builder = null;
    	try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(60000);
            // For each WebRequest, we use separated connection,
            // so we always set cookies for new connection.
            if (cookies != null) {
                    connection.setRequestProperty(K0, cookies.split(";", 2)[0]);
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            try {
            	connection.setRequestMethod(POST);
            } catch (ProtocolException e) {
            	return -5;
            }
            connection.setRequestProperty(K1, V1);
            connection.setRequestProperty(K2, V2);
            connection.setRequestProperty(K3, V3);
            connection.setRequestProperty(K4, V4);
            OutputStream os = connection.getOutputStream();
            os.write(keyvaluestring.getBytes());
            os.flush();
            os.close();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK &&
                    connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            	Log.e("WebRequest", "http code = " + connection.getResponseCode());
            	return -7;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream()), "utf-8"));
            builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) {
            	builder.append(line).append("\n");
            }
            // init cookies for using in next http request
            if (cookies == null) {
            	if (connection != null) {
            		List<String> tmp = connection.getHeaderFields().get("set-cookie");
            		if (tmp != null) {
            			for(String ck: tmp) {
            				if (ck.contains("sessionid=")) {
            					System.setProperty("http.maxRedirects", "50");
            					cookies = new String(ck);
            					break;
            				}
            			}
            		}
            	}
            }
            reader.close();
    	} catch (IOException e) {
            e.printStackTrace();
            return -4;
    	} catch (NullPointerException e) {
            e.printStackTrace();
            return -10;
    	} finally {
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
    	}
    	String result = builder.toString();
    	try {
    		jsonret = new JSONObject(result);
    	} catch (JSONException e) {
    		return -9;
    	}

    	return 0;
    }
    
    protected void handleJSON() {
        int code;
        try {
        	code = jsonret.getInt("code");
        } catch (JSONException e) {
        	wResponse.failHandle(-1);
        	return;
        }
        if (code == 0) {
        	wResponse.successHandle(jsonret);
        } else {
        	Log.e("WebRequest", "json error " + jsonret.toString());
        	try {
				String errorstr = jsonret.getString("data");
				Log.e("WebRequest", "code." + code + ": " + errorstr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
        	wResponse.failHandle(code);
        }
    }
    
}
