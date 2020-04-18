package com.example.stalkexchange;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Dashboard extends AppCompatActivity {
    public static ArrayList<tPrice> priceArrayList = new ArrayList<tPrice>();
    private ViewPager vp;
    public PagerAdapter pagerAdapter;
    public static String DDCODE = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Date today = new Date();
        Boolean Sunday = today.toString().contains("Sun");
        Prices p = new Prices(getApplicationContext());
        Thread t = new Thread(p);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (p.getResponsecode() == 401){
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Login Session Expired. Please login again!")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
        for(tPrice copy : p.getTransactions()){
            priceArrayList.add(copy);
        }
        if(p.getUserDodo()!=null){
            DDCODE = p.getUserDodo();
        }
        TextView dashtitlesign = findViewById(R.id.dashTitle);
        if(p.getUsercount()>=1){
            dashtitlesign.setTextColor(Color.RED);
            dashtitlesign.setText(R.string.maxentry);
            ConstraintLayout c = findViewById(R.id.CL);
            c.removeAllViews();
        }
        else if (p.getUsercount()<1){
            dashtitlesign.setText(R.string.Sumbittitle);
        }
        TextView ListTitle = findViewById(R.id.timetitle);
        if(!Sunday){
            ListTitle.setText(getString(R.string.Weekdaydate,today.toString().substring(0,16)));
        }
        if(Sunday){
            ListTitle.setText(getString(R.string.Sundaydate,today.toString().substring(0,16)));
        }
        try{
            FileInputStream fis = getApplicationContext().openFileInput("User");
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String jstext = readall.readAll(br);
            fis.close();
            isr.close();
            br.close();
            JSONTokener Tok = new JSONTokener(jstext);
            JSONObject jo = new JSONObject(Tok);
            jo.put("_id", p.getUsersID());
            String fileContents = jo.toString();
            try (FileOutputStream fos = getApplicationContext().openFileOutput("User", Context.MODE_PRIVATE)) {
                fos.write(fileContents.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
        }
        TabLayout tabbyboys = findViewById(R.id.tabLayout);
        vp = findViewById(R.id.Vice);
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabbyboys.getTabCount());
        vp.setAdapter(pagerAdapter);

        tabbyboys.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /**
             * If a tab is selected, the ViewPager changes to that tab. The pagerAdapter is also notified to ensure the fragment is spawned.
             * @param tab Passed by the TabLayout object. is the tab that the function is referring to in context.
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    pagerAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 1){
                    pagerAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 2){
                    pagerAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { } //unused

            @Override
            public void onTabReselected(TabLayout.Tab tab) { } //unused
        });

        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabbyboys)); //ensures that the viewpager updates the page.

    }

    public void submitprice(View v) {
        TextView bells = findViewById(R.id.BellCount);
        String numbells = bells.getText().toString();
        bells.setText("");
        try{
            priceArrayList.clear();
            submit s = new submit(Integer.valueOf(numbells), getApplicationContext());
            Thread t = new Thread(s);
            t.start();
            t.join();
            Prices p = new Prices(getApplicationContext());
            Thread tt = new Thread(p);
            tt.start();
            tt.join();
            System.out.println("My Dodo is: " + p.getUserDodo());
            if(p.getUserDodo()!=null){
                DDCODE = p.getUserDodo();
            }
            TextView dashtitlesign = findViewById(R.id.dashTitle);
            if(p.getUsercount()>=1){
                dashtitlesign.setTextColor(Color.RED);
                dashtitlesign.setText(R.string.maxentry);
                ConstraintLayout c = findViewById(R.id.CL);
                c.removeAllViews();
            }
            else if (p.getUsercount()<1){
                dashtitlesign.setText(R.string.Sumbittitle);
            }
            if(!priceArrayList.isEmpty()){
                priceArrayList.addAll(p.getTransactions());
                LinearLayout lin = v.findViewById(R.id.linlay);
                lin.removeAllViews();
                int best = 0;
                String bestname = "";
                String bestisl = "";
                String dodoCode = null;
                for(tPrice i : priceArrayList){
                    if(i.getPrice()>best){
                        bestname = i.getOwner().getName();
                        bestisl = i.getOwner().getIsland();
                        best = i.getPrice();
                        if(i.getOwner().getDodoCode() != null){
                            dodoCode = i.getOwner().getDodoCode();
                        }
                        else{
                            dodoCode = null;
                        }
                    }
                }
                TextView busern = new TextView(v.getContext());
                TextView bislandn = new TextView(v.getContext());
                TextView bpricen = new TextView(v.getContext());
                TextView bspaceone = new TextView(v.getContext());
                TextView bspacetwo = new TextView(v.getContext());
                TextView blinesone = new TextView(v.getContext());
                TextView blinestwo = new TextView(v.getContext());
                TextView bDodoIsland = new TextView(v.getContext());
                TextView bddc = null;
                busern.setText(getString(R.string.best_price, bestname));
                bislandn.setText(getString(R.string.listIsland,bestisl));
                bpricen.setText(getString(R.string.listBells,best));
                if(dodoCode == null){
                    bDodoIsland.setText(getString(R.string.closedodo));
                    bDodoIsland.setTextColor(Color.RED);
                }
                else if(dodoCode !=null){
                    bDodoIsland.setText(getString(R.string.opendodo));
                    bddc = new TextView(v.getContext());
                    bddc.setGravity(Gravity.CENTER);
                    bDodoIsland.setTextColor(Color.GREEN);
                    bddc.setText(dodoCode);
                    bddc.setTextColor(Color.CYAN);

                }
                busern.setText(getString(R.string.best_price, bestname));
                bislandn.setText(getString(R.string.listIsland,bestisl));
                bpricen.setText(getString(R.string.listBells,best));
                busern.setGravity(Gravity.CENTER);
                bislandn.setGravity(Gravity.CENTER);
                bDodoIsland.setGravity(Gravity.CENTER);
                bpricen.setGravity(Gravity.CENTER);
                bspaceone.setGravity(Gravity.CENTER);
                bspacetwo.setGravity(Gravity.CENTER);
                blinesone.setGravity(Gravity.CENTER);
                blinestwo.setGravity(Gravity.CENTER);
                bspaceone.setText(" ");
                bspacetwo.setText(" ");
                blinesone.setTextColor(Color.BLUE);
                busern.setTextColor(Color.MAGENTA);
                blinestwo.setTextColor(Color.BLUE);
                blinesone.setText("===========================");
                blinestwo.setText("===========================");
                lin.addView(bspaceone);
                lin.addView(blinesone);
                lin.addView(busern);
                lin.addView(bislandn);
                lin.addView(bpricen);
                lin.addView(blinestwo);
                lin.addView(bspacetwo);
                for(tPrice i : priceArrayList){
                    TextView usern = new TextView(v.getContext());
                    TextView islandn = new TextView(v.getContext());
                    TextView pricen = new TextView(v.getContext());
                    TextView spaceone = new TextView(v.getContext());
                    TextView spacetwo = new TextView(v.getContext());
                    TextView linesone = new TextView(v.getContext());
                    TextView linestwo = new TextView(v.getContext());
                    TextView dodoIsland = new TextView(v.getContext());
                    TextView ddc = null;
                    if(i.getOwner().getDodoCode() == null){
                        dodoIsland.setText(getString(R.string.closedodo));
                        dodoIsland.setTextColor(Color.RED);
                    }
                    else if(i.getOwner().getDodoCode() !=null){
                        dodoIsland.setText(getString(R.string.opendodo));
                        ddc = new TextView(v.getContext());
                        ddc.setGravity(Gravity.CENTER);
                        dodoIsland.setTextColor(Color.GREEN);
                        ddc.setText(i.getOwner().getDodoCode());
                        ddc.setTextColor(Color.CYAN);

                    }
                    usern.setText(getString(R.string.listName,i.getOwner().getName()));
                    islandn.setText(getString(R.string.listIsland,i.getOwner().getIsland()));
                    pricen.setText(getString(R.string.listBells,i.getPrice()));
                    dodoIsland.setGravity(Gravity.CENTER);
                    usern.setGravity(Gravity.CENTER);
                    islandn.setGravity(Gravity.CENTER);
                    pricen.setGravity(Gravity.CENTER);
                    spaceone.setGravity(Gravity.CENTER);
                    spacetwo.setGravity(Gravity.CENTER);
                    linesone.setGravity(Gravity.CENTER);
                    linestwo.setGravity(Gravity.CENTER);
                    spaceone.setText(" ");
                    spacetwo.setText(" ");
                    linesone.setTextColor(Color.YELLOW);
                    linestwo.setTextColor(Color.YELLOW);
                    linesone.setText("===========================");
                    linestwo.setText("===========================");
                    lin.addView(spaceone);
                    lin.addView(linesone);
                    lin.addView(usern);
                    lin.addView(islandn);
                    lin.addView(pricen);
                    lin.addView(dodoIsland);
                    if(ddc!=null){
                        lin.addView(ddc);
                    }
                    lin.addView(linestwo);
                    lin.addView(spacetwo);
                    lin.invalidate();
                }

            }
            else {
                LinearLayout lin = v.findViewById(R.id.linlay);
                TextView busern = new TextView(v.getContext());
                TextView bspaceone = new TextView(v.getContext());
                TextView bspacetwo = new TextView(v.getContext());
                TextView blinesone = new TextView(v.getContext());
                TextView blinestwo = new TextView(v.getContext());
                busern.setGravity(Gravity.CENTER);
                bspaceone.setGravity(Gravity.CENTER);
                bspacetwo.setGravity(Gravity.CENTER);
                blinesone.setGravity(Gravity.CENTER);
                blinestwo.setGravity(Gravity.CENTER);
                bspaceone.setText(" ");
                bspacetwo.setText(" ");
                blinesone.setTextColor(Color.RED);
                busern.setTextColor(Color.RED);
                blinestwo.setTextColor(Color.RED);
                blinesone.setText("===========================");
                blinestwo.setText("===========================");
                lin.addView(bspaceone);
                lin.addView(blinesone);
                lin.addView(busern);
                lin.addView(blinestwo);
                lin.addView(bspacetwo);
                lin.invalidate();
            }
        }
        catch(NumberFormatException e){
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Please enter a valid number! Do not include a-z or symbols.")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
            System.out.println("TestyBoys");
        }
    }

    public void refreshprice(View v) {
        try{
            priceArrayList.clear();
            Prices p = new Prices(getApplicationContext());
            Thread tt = new Thread(p);
            tt.start();
            tt.join();
            System.out.println("My Dodo is: " + p.getUserDodo());
            if(p.getUserDodo()!=null){
                DDCODE = p.getUserDodo();
            }
            TextView dashtitlesign = findViewById(R.id.dashTitle);
            if(!priceArrayList.isEmpty()){
                priceArrayList.addAll(p.getTransactions());
                LinearLayout lin = v.findViewById(R.id.linlay);
                lin.removeAllViews();
                int best = 0;
                String bestname = "";
                String bestisl = "";
                String dodoCode = null;
                for(tPrice i : priceArrayList){
                    if(i.getPrice()>best){
                        bestname = i.getOwner().getName();
                        bestisl = i.getOwner().getIsland();
                        best = i.getPrice();
                        if(i.getOwner().getDodoCode() != null){
                            dodoCode = i.getOwner().getDodoCode();
                        }
                    }
                }
                TextView busern = new TextView(v.getContext());
                TextView bislandn = new TextView(v.getContext());
                TextView bpricen = new TextView(v.getContext());
                TextView bspaceone = new TextView(v.getContext());
                TextView bspacetwo = new TextView(v.getContext());
                TextView blinesone = new TextView(v.getContext());
                TextView blinestwo = new TextView(v.getContext());
                TextView bDodoIsland = new TextView(v.getContext());
                TextView bddc = null;
                busern.setText(getString(R.string.best_price, bestname));
                bislandn.setText(getString(R.string.listIsland,bestisl));
                bpricen.setText(getString(R.string.listBells,best));
                if(dodoCode == null){
                    bDodoIsland.setText(getString(R.string.closedodo));
                    bDodoIsland.setTextColor(Color.RED);
                }
                else if(dodoCode !=null){
                    bDodoIsland.setText(getString(R.string.opendodo));
                    bddc = new TextView(v.getContext());
                    bddc.setGravity(Gravity.CENTER);
                    bDodoIsland.setTextColor(Color.GREEN);
                    bddc.setText(dodoCode);
                    bddc.setTextColor(Color.CYAN);

                }
                busern.setText(getString(R.string.best_price, bestname));
                bislandn.setText(getString(R.string.listIsland,bestisl));
                bpricen.setText(getString(R.string.listBells,best));
                busern.setGravity(Gravity.CENTER);
                bislandn.setGravity(Gravity.CENTER);
                bDodoIsland.setGravity(Gravity.CENTER);
                bpricen.setGravity(Gravity.CENTER);
                bspaceone.setGravity(Gravity.CENTER);
                bspacetwo.setGravity(Gravity.CENTER);
                blinesone.setGravity(Gravity.CENTER);
                blinestwo.setGravity(Gravity.CENTER);
                bspaceone.setText(" ");
                bspacetwo.setText(" ");
                blinesone.setTextColor(Color.BLUE);
                busern.setTextColor(Color.MAGENTA);
                blinestwo.setTextColor(Color.BLUE);
                blinesone.setText("===========================");
                blinestwo.setText("===========================");
                lin.addView(bspaceone);
                lin.addView(blinesone);
                lin.addView(busern);
                lin.addView(bislandn);
                lin.addView(bpricen);
                lin.addView(blinestwo);
                lin.addView(bspacetwo);
                for(tPrice i : priceArrayList){
                    TextView usern = new TextView(v.getContext());
                    TextView islandn = new TextView(v.getContext());
                    TextView pricen = new TextView(v.getContext());
                    TextView spaceone = new TextView(v.getContext());
                    TextView spacetwo = new TextView(v.getContext());
                    TextView linesone = new TextView(v.getContext());
                    TextView linestwo = new TextView(v.getContext());
                    TextView dodoIsland = new TextView(v.getContext());
                    TextView ddc = null;
                    if(i.getOwner().getDodoCode() == null){
                        dodoIsland.setText(getString(R.string.closedodo));
                        dodoIsland.setTextColor(Color.RED);
                    }
                    else if(i.getOwner().getDodoCode() !=null){
                        dodoIsland.setText(getString(R.string.opendodo));
                        ddc = new TextView(v.getContext());
                        ddc.setGravity(Gravity.CENTER);
                        dodoIsland.setTextColor(Color.GREEN);
                        ddc.setText(i.getOwner().getDodoCode());
                        ddc.setTextColor(Color.CYAN);

                    }
                    usern.setText(getString(R.string.listName,i.getOwner().getName()));
                    islandn.setText(getString(R.string.listIsland,i.getOwner().getIsland()));
                    pricen.setText(getString(R.string.listBells,i.getPrice()));
                    dodoIsland.setGravity(Gravity.CENTER);
                    usern.setGravity(Gravity.CENTER);
                    islandn.setGravity(Gravity.CENTER);
                    pricen.setGravity(Gravity.CENTER);
                    spaceone.setGravity(Gravity.CENTER);
                    spacetwo.setGravity(Gravity.CENTER);
                    linesone.setGravity(Gravity.CENTER);
                    linestwo.setGravity(Gravity.CENTER);
                    spaceone.setText(" ");
                    spacetwo.setText(" ");
                    linesone.setTextColor(Color.YELLOW);
                    linestwo.setTextColor(Color.YELLOW);
                    linesone.setText("===========================");
                    linestwo.setText("===========================");
                    lin.addView(spaceone);
                    lin.addView(linesone);
                    lin.addView(usern);
                    lin.addView(islandn);
                    lin.addView(pricen);
                    lin.addView(dodoIsland);
                    if(ddc!=null){
                        lin.addView(ddc);
                    }
                    lin.addView(linestwo);
                    lin.addView(spacetwo);
                    lin.invalidate();
                }

            }
            else {
                LinearLayout lin = v.findViewById(R.id.linlay);
                TextView busern = new TextView(v.getContext());
                TextView bspaceone = new TextView(v.getContext());
                TextView bspacetwo = new TextView(v.getContext());
                TextView blinesone = new TextView(v.getContext());
                TextView blinestwo = new TextView(v.getContext());
                busern.setGravity(Gravity.CENTER);
                bspaceone.setGravity(Gravity.CENTER);
                bspacetwo.setGravity(Gravity.CENTER);
                blinesone.setGravity(Gravity.CENTER);
                blinestwo.setGravity(Gravity.CENTER);
                bspaceone.setText(" ");
                bspacetwo.setText(" ");
                blinesone.setTextColor(Color.RED);
                busern.setTextColor(Color.RED);
                blinestwo.setTextColor(Color.RED);
                blinesone.setText("===========================");
                blinestwo.setText("===========================");
                lin.addView(bspaceone);
                lin.addView(blinesone);
                lin.addView(busern);
                lin.addView(blinestwo);
                lin.addView(bspacetwo);
                lin.invalidate();
            }
        }
         catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
        }
    }

}
