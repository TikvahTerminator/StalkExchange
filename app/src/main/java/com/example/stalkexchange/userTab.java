package com.example.stalkexchange;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;


public class userTab extends Fragment {

    public userTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_tab, container, false);
        TextView Username = v.findViewById(R.id.UserpageUser);
        TextView UserIsland = v.findViewById(R.id.UserpageIsland);
        FileInputStream fis = null;
        try {
            fis = v.getContext().openFileInput("User");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String jstext = null;
        try {
            jstext = readall.readAll(br);
            fis.close();
            isr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONTokener Tok = new JSONTokener(jstext);
        JSONObject jo = null;
        try {
            jo = new JSONObject(Tok);
            Username.setText(getString(R.string.Usersname, jo.getString("name")));
            UserIsland.setText(getString(R.string.UsersIsland, jo.getString("island")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}
