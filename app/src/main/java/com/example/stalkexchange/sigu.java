package com.example.stalkexchange;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class sigu implements Runnable {

    private URL apiurl = new URL("https://stalk-exchange.herokuapp.com/user");
    private String username;
    private String island;
    private String pass;
    private String accesscode;
    private String friendcode;
    private Integer responsecode;
    private Integer Timeout;

    public sigu(String un, String isd, String ps, String asc, String fc) throws MalformedURLException {
        username=un;
        island=isd;
        pass=ps;
        accesscode=asc;
        friendcode=fc;
    }

    @Override
    public void run() {
        responsecode = signup();
    }

    private Integer signup(){
        JSONObject token = new JSONObject();
        try {
            token.put("name", username);
            token.put("island", island);
            token.put("friendCode", friendcode);
            token.put("password", pass);
            token.put("confirmPassword", pass);
            token.put("inviteCode", accesscode);

            HttpURLConnection tc = (HttpURLConnection) apiurl.openConnection();
            tc.setRequestMethod("POST");
            tc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");
            tc.setRequestProperty("Accept", "*/*");
            tc.setRequestProperty("Host", "stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Origin", "https://stalk-exchange.herokuapp.com");
            tc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            tc.setConnectTimeout(5000);
            tc.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(tc.getOutputStream());
            System.out.println("JSON Text: " + token.toString());
            wr.write(token.toString());
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

    Integer getResponsecode(){
        return responsecode;
    }
    Integer getTimeout(){ return Timeout;}
}
