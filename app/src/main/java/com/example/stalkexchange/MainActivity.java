package com.example.stalkexchange;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {
    String[] requiredPermissions = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
    boolean OK = true;
    String filename = "User";
    boolean filenull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean internetcon = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        try{
            internetcon = cm.getActiveNetworkInfo().isConnected() && cm.getActiveNetworkInfo().isAvailable();
            filenull = getApplicationContext().getFileStreamPath(filename).exists();
            System.out.println("Does it exist?: " + filenull);
        }
        catch(NullPointerException e){
            filenull = false;
        }
        if(!internetcon){
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("This application requires access to the internet. Please connect to the internet to continue!")
                    .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .show();
        }
        for(String s: requiredPermissions){
            int result = ActivityCompat.checkSelfPermission(this, s);
            if(result != PackageManager.PERMISSION_GRANTED){
                OK = false;
            }
        }
        if(!OK){
            ActivityCompat.requestPermissions(this,requiredPermissions,1);
            System.exit(0);
        }
        if(filenull){
            dash();
        }
    }

    public void dash(){
        Intent intent = new Intent(this, Dashboard.class);
        LinearLayout L = findViewById(R.id.linlay);
        if(L!=null){
            L.removeAllViews();
        }
        startActivity(intent);
    }

    public void signup(View v){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void LogI(View v) throws MalformedURLException, InterruptedException, JSONException {
        TextView usr = findViewById(R.id.usernameBox);
        TextView isl = findViewById(R.id.islandName);
        TextView pass = findViewById(R.id.passwordBox);
        Login L = new Login(usr.getText().toString(), isl.getText().toString(), pass.getText().toString());
        Thread T = new Thread(L);
        T.start();
        T.join();
        if(L.Timeout != null){
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Failed to connect to the Stalk Exchange! Try again later?")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        if(L.badlogin != null){
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Your Username, Island, or Password were incorrect!")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        else {
            JSONObject idcard = new JSONObject();
            idcard.put("apiToken", L.getLogintoken());
            idcard.put("name", usr.getText().toString());
            idcard.put("island", isl.getText().toString());
            String fileContents = idcard.toString() ;
            if(filenull){
                getFileStreamPath(filename).delete();
            }
            try (FileOutputStream fos = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
                fos.write(fileContents.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            dash();
        }

    }
}
