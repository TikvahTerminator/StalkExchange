package com.example.stalkexchange;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


public class listTab extends Fragment {
    public listTab() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Date today = new Date();
        Boolean Sunday = today.toString().contains("Sun");
        View v = inflater.inflate(R.layout.fragment_list_tab, container, false);
        int best = 0;
        String bestname = "";
        String bestisl = "";
        String dodoCode = null;
        Boolean b = Dashboard.priceArrayList.isEmpty();
        if(!Dashboard.priceArrayList.isEmpty()){
            for(tPrice i : Dashboard.priceArrayList){
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
            LinearLayout lin = v.findViewById(R.id.linlay);
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
            busern.setGravity(Gravity.CENTER);
            bislandn.setGravity(Gravity.CENTER);
            bpricen.setGravity(Gravity.CENTER);
            bDodoIsland.setGravity(Gravity.CENTER);
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
            lin.addView(bDodoIsland);
            if(bddc !=null){
                lin.addView(bddc);
            }
            lin.addView(blinestwo);
            lin.addView(bspacetwo);
            for(tPrice i : Dashboard.priceArrayList){
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
                usern.setGravity(Gravity.CENTER);
                islandn.setGravity(Gravity.CENTER);
                pricen.setGravity(Gravity.CENTER);
                spaceone.setGravity(Gravity.CENTER);
                spacetwo.setGravity(Gravity.CENTER);
                dodoIsland.setGravity(Gravity.CENTER);
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
        else{
            LinearLayout lin = v.findViewById(R.id.linlay);
            TextView busern = new TextView(v.getContext());
            TextView bspaceone = new TextView(v.getContext());
            TextView bspacetwo = new TextView(v.getContext());
            TextView blinesone = new TextView(v.getContext());
            TextView blinestwo = new TextView(v.getContext());
            busern.setText(getString(R.string.reporterror));
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

        return v;
    }


}
