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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class dodoCode implements Runnable {
        private int responseCode;
        Context con;
        private int Timeout;
        private String code;
        private String ddc;

        dodoCode(String dodocode, Context c) throws MalformedURLException {
            ddc = dodocode;
            code = "O";
            con = c;
        }

        dodoCode(Context c) throws MalformedURLException {
            code = "C";
            con = c;
        }
    @Override
    public void run() {
        if(code.equals("O")){
            try {
                responseCode = Connection(ddc);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else if(code.equals("C")){
            try {
                responseCode = Connection(null);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    private Integer Connection(String dodoCode) throws MalformedURLException {
        JSONObject payload = new JSONObject();
        URL apiurl = new URL("https://stalk-exchange.herokuapp.com/user");
        try{
            if(dodoCode == null){
                payload.put("dodoCode", "");
            }
            else if(dodoCode !=null){
                payload.put("dodoCode", dodoCode);
            }
            FileInputStream fis = con.openFileInput("User");
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String jstext = readall.readAll(br);
            fis.close();
            isr.close();
            br.close();
            JSONTokener Tok = new JSONTokener(jstext);
            JSONObject jo = new JSONObject(Tok);
            String AuthenticationToken = jo.getString("apiToken");
            HttpURLConnection tc = (HttpURLConnection) apiurl.openConnection();
            tc.setRequestMethod("PATCH");
            tc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");
            tc.setRequestProperty("Accept", "*/*");
            tc.setRequestProperty("Host", "stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Origin", "https://stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            tc.setRequestProperty("Authorization", AuthenticationToken);
            tc.setConnectTimeout(5000);
            tc.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(tc.getOutputStream());
            System.out.println("JSON Text: " + payload.toString());
            wr.write(payload.toString());
            wr.flush();
            wr.close();
            System.out.println("RC:" + tc.getResponseCode());
            System.out.println("Rm" + tc.getResponseMessage());
            return tc.getResponseCode();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (java.net.SocketTimeoutException e){
            Timeout = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
