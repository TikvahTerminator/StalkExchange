package com.example.stalkexchange;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Login implements Runnable {
    private URL apiurl = new URL("https://stalk-exchange.herokuapp.com/auth/login");
    private String username;
    private String island;
    private String pass;
    private String logintoken;
    public Integer badlogin;
    public Integer Timeout;


    public Login(String un, String isd, String ps) throws MalformedURLException {
        username=un;
        island=isd;
        pass=ps;
    }

    @Override
    public void run() {
        logintoken = signin(username, island, pass);
    }


    public String signin(String username, String island, String pass) {
        JSONObject token = new JSONObject();
        try {
            token.put("name", username);
            token.put("island", island);
            token.put("password", pass);

            HttpURLConnection tc = (HttpURLConnection) apiurl.openConnection();
            tc.setRequestMethod("POST");
            tc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");
            tc.setRequestProperty("Accept", "*/*");
            tc.setRequestProperty("Host", "stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Origin", "https://stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            tc.setConnectTimeout(5000);
            tc.setDoOutput(true);
            tc.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(tc.getOutputStream());
            System.out.println("JSON Text: " + token.toString());
            wr.write(token.toString());
            wr.flush();
            wr.close();
            System.out.println("RC:" + tc.getResponseCode());
            System.out.println("Rm" + tc.getResponseMessage());
            if(tc.getResponseCode() == 401){
                badlogin = 1;
            }
            InputStreamReader isr = new InputStreamReader(tc.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            String jsontext = readall.readAll(in);
            JSONTokener tk = new JSONTokener(jsontext);
            JSONObject js = new JSONObject(tk);
            in.close();
            isr.close();
            String tok = js.getString("token");
            System.out.println(js);
            return tok;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (java.net.SocketTimeoutException e){
            Timeout = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getLogintoken(){
        return logintoken;
    }
}
