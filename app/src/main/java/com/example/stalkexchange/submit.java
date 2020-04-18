package com.example.stalkexchange;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class submit implements Runnable {
    private String bells;
    private Integer responsecode;
    private Integer Timeout;
    private Context c;

    public submit(Integer b, Context cc){
        System.out.println("Started Sumbit thread");
        bells = Integer.toString(b);
        c=cc;
    }
    @Override
    public void run() {
        connect();
    }

    private void connect(){
        URL  baseurl = null;
        try {
            baseurl = new URL("");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Date today = new Date();
        Boolean Sunday = today.toString().contains("Sun");
        try{
            if(!Sunday){
                baseurl = new URL("https://stalk-exchange.herokuapp.com/price/sell");
            }
            else if(Sunday){
                baseurl = new URL("https://stalk-exchange.herokuapp.com/price/buy");
            }

        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        System.out.println("URL is: " + baseurl.toString());
        JSONObject token = new JSONObject();
        try {
            FileInputStream fis = c.openFileInput("User");
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String jstext = readall.readAll(br);
            fis.close();
            isr.close();
            br.close();
            JSONTokener Tok = new JSONTokener(jstext);
            JSONObject jo = new JSONObject(Tok);
            String AuthenticationToken = jo.getString("apiToken");
            token.put("bells", bells );
            HttpURLConnection tc = (HttpURLConnection) baseurl.openConnection();
            tc.setRequestMethod("POST");
            tc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");
            tc.setRequestProperty("Accept", "*/*");
            tc.setRequestProperty("Host", "stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Origin", "https://stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            tc.setRequestProperty("Authorization", AuthenticationToken);
            tc.setConnectTimeout(5000);
            tc.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(tc.getOutputStream());
            System.out.println("JSON Text: " + token.toString());
            wr.write(token.toString());
            wr.flush();
            wr.close();
            System.out.println("RC:" + tc.getResponseCode());
            System.out.println("Rm" + tc.getResponseMessage());
            responsecode = tc.getResponseCode();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (java.net.SocketTimeoutException e){
            Timeout = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public Integer getTimeout() {
        return Timeout;
    }
}
