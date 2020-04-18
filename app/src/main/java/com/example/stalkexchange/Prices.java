package com.example.stalkexchange;

import android.content.Context;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class Prices implements Runnable {
    private Context context;
    private Integer responsecode;
    private ArrayList<tPrice> Transactions = new ArrayList<tPrice>();
    private Integer usercount = 0;
    private String usersID;
    private String userDodo;

    public Prices(Context c){
        context = c;
    }

    @Override
    public void run() {
        connecttoserver();
    }

    private void connecttoserver(){
        Date today = new Date();
        Boolean Sunday = today.toString().contains("Sun");
        Boolean ispm = false;
        if(Integer.parseInt(today.toString().substring(11,13))>=12){
            ispm = true;
        }
        URL baseurl = null;
        if(!Sunday){
            try {
                baseurl = new URL("https://stalk-exchange.herokuapp.com/price?type=sell");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else if(Sunday){
            try {
                baseurl = new URL("https://stalk-exchange.herokuapp.com/price?type=buy");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        try {
            FileInputStream fis = context.openFileInput("User");
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String jstext = readall.readAll(br);
            fis.close();
            isr.close();
            br.close();
            JSONTokener Tok = new JSONTokener(jstext);
            JSONObject jo = new JSONObject(Tok);
            String usernom = jo.getString("name");
            String islnom = jo.getString("island");
            String AuthenticationToken = jo.getString("apiToken");
            HttpURLConnection tc = (HttpURLConnection) baseurl.openConnection();
            tc.setRequestMethod("GET");
            tc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");
            tc.setRequestProperty("Accept", "*/*");
            tc.setRequestProperty("Host", "stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Origin", "https://stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            tc.setRequestProperty("Authorization", AuthenticationToken);
            tc.setConnectTimeout(5000);
            responsecode = tc.getResponseCode();
            InputStreamReader inputStreamReader = new InputStreamReader(tc.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(inputStreamReader);
            String jsontext = readall.readAll(in);
            inputStreamReader.close();
            in.close();
            JSONTokener JT = new JSONTokener(jsontext);
            JSONArray JA = new JSONArray(JT);
            for(int x = 0 ; x<JA.length(); x++){
                JSONObject price = JA.getJSONObject(x);
                JSONObject user = price.getJSONObject("user");
                Date tdate = new Date (price.getLong("date"));
                int datetime = Integer.parseInt(tdate.toString().substring(11,13));
                Users ow = null;
                if(user.getString("dodoCode").equals("")){
                    ow = new Users(user.getString("_id"), user.getString("name"), user.getString("island"));
                }
                else{
                    ow = new Users(user.getString("_id"), user.getString("name"), user.getString("island"), user.getString("dodoCode"));
                }
                if(DateUtils.isToday(tdate.getTime())){
                    if(ispm){
                        if(datetime>=12){
                            if(user.getString("name").equals(usernom) && user.getString("island").equals(islnom)){
                                System.out.println("DODCODE IS (In Function): " + user.getString("dodoCode"));
                                usercount++;
                                if(user.getString("dodoCode").length()==5){
                                    userDodo = user.getString("dodoCode");
                                }
                                usersID = user.getString("_id");
                            }
                            Transactions.add(new tPrice(tdate, price.getInt("bells"),ow));
                        }
                    }
                    else if(!ispm){
                        if(datetime<12){
                            if(user.getString("name").equals(usernom) && user.getString("island").equals(islnom)){
                                usercount++;
                                System.out.println("DODCODE IS (In Function): " + user.getString("dodoCode"));
                                if(user.getString("dodoCode").length()==5){
                                    userDodo = user.getString("dodoCode");
                                }
                                usersID = user.getString("_id");
                            }
                            Transactions.add(new tPrice(tdate, price.getInt("bells"),ow));
                        }
                    }
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<tPrice> getTransactions() {
        return Transactions;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public Integer getUsercount() {
        return usercount;
    }

    public String getUsersID() {
        return usersID;
    }

    public String getUserDodo(){
        return userDodo;
    }
}
