package com.example.stalkexchange;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.MalformedURLException;


public class dodoTab extends Fragment {
    private int mode =0;
    View theview;
    public dodoTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dodo_tab, container, false);
        theview = v;
        TextView isltabtitle = v.findViewById(R.id.IslandTabPrompt);
        TextView isltabdodocode = v.findViewById(R.id.dodoCodeTitle);
        Button function = v.findViewById(R.id.DcodeBut);
        function.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                connecttododo(v);
            }

        });
        TextView lintitle = v.findViewById(R.id.dodoCLOP);
        EditText edi = v.findViewById(R.id.DodoEdit);
        if(Dashboard.DDCODE!=null){
            mode = 2;
            lintitle.setText(getString(R.string.CLTITLE));
            isltabtitle.setText(getString(R.string.opendodo));
            isltabdodocode.setText(getString(R.string.dodocodeactual, Dashboard.DDCODE));
            function.setText(getString(R.string.CLISL));
            isltabdodocode.setTextColor(Color.CYAN);
            isltabtitle.setTextColor(Color.GREEN);
            edi.setEnabled(false);
        }
        else{
            mode = 1;
            edi.setEnabled(true);
            lintitle.setText(getString(R.string.OPTITLE));
            function.setText(R.string.OPISL);
            isltabtitle.setText(R.string.closedodo);
            isltabtitle.setTextColor(Color.RED);
        }

        return v;
    }

    public void connecttododo(View v) {
        TextView isltabtitle = theview.findViewById(R.id.IslandTabPrompt);
        ConstraintLayout cl = theview.findViewById(R.id.DodoConst);
        TextView isltabdodocode = theview.findViewById(R.id.dodoCodeTitle);
        TextView lintitle = theview.findViewById(R.id.dodoCLOP);
        EditText edi = theview.findViewById(R.id.DodoEdit);
        Button dbutton = theview.findViewById(R.id.DcodeBut);
        System.out.println("is edi null? After: " + edi == null );
        dodoCode d = null;
        if(edi.getText().toString().length()>5 || edi.getText() == null ){
            new AlertDialog.Builder(theview.getContext())
                    .setTitle("Error!")
                    .setMessage("Dodo codes are 5 characters long! Please enter a valid Dodo Code!")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        else{
            if(mode == 1){
                d = null;
                try {
                    d = new dodoCode(edi.getText().toString(), getContext());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Thread t = new Thread(d);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                edi.setEnabled(false);
                Dashboard.DDCODE = edi.getText().toString();
                lintitle.setText(getString(R.string.CLTITLE));
                dbutton.setText(getString(R.string.CLISL));
                isltabtitle.setText(getString(R.string.opendodo));
                isltabdodocode.setText(getString(R.string.dodocodeactual, Dashboard.DDCODE));
                isltabdodocode.setTextColor(Color.CYAN);
                isltabtitle.setTextColor(Color.GREEN);
                mode = 2;
            }
            else if(mode == 2){
                try {
                    d = new dodoCode(getContext());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Thread t = new Thread(d);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                edi.setEnabled(true);
                lintitle.setText(getString(R.string.OPTITLE));
                dbutton.setText(R.string.OPISL);
                edi.setText("");
                isltabdodocode.setText("");
                isltabtitle.setText(R.string.closedodo);
                isltabtitle.setTextColor(Color.RED);
                mode = 1;
            }
        }

        cl.invalidate();
    }
}

