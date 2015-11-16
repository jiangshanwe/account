package com.jiang.shanwe.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

import com.jiang.shanwe.Config;

public class HttpUtil {

    public static void sendHttpRequest(final String url, final HttpMethod method, final HttpCallbackListener listener,
            final String... kvs) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                StringBuffer paramsStr = new StringBuffer();
                try {
                    if (kvs != null && kvs.length > 1) {
                        for (int i = 0; i < kvs.length; i += 2) {
                            paramsStr.append(kvs[i]).append("=").append(kvs[i + 1]).append("&");
                        }
                    }
                    URLConnection uc;

                    switch (method) {
                    case POST:
                        uc = new URL(url).openConnection();
                        uc.setDoOutput(true);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(),
                                Config.CHARSET));
                        bw.write(paramsStr.toString());
                        bw.flush();
                        break;
                    default:
                        if (paramsStr != null && paramsStr.toString().length() > 0) {
                            uc = new URL(url + "?" + paramsStr.toString()).openConnection();
                        } else {
                            uc = new URL(url).openConnection();
                        }
                        break;
                    }

                    System.out.println("Request url : " + uc.getURL());
                    System.out.println("Request data : " + paramsStr);

                    BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), Config.CHARSET));
                    String line = null;
                    StringBuffer result = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    System.out.println("Result : " + result);

                    return result.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    if (listener != null) {
                        listener.onFinish(result);
                    }
                } else {
                    if (listener != null) {
                        listener.onError();
                    }
                }
                super.onPostExecute(result);
            }
        }.execute();
    }
}
