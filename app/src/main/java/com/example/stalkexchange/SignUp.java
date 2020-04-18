package com.example.stalkexchange;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.net.MalformedURLException;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
    }

    public void signmeupscotty(View v) throws MalformedURLException, InterruptedException {
        TextView user = findViewById(R.id.UserName);
        TextView island = findViewById(R.id.IslandName);
        TextView password = findViewById(R.id.Passbox);
        TextView conPass = findViewById(R.id.confirmPasswordBox);
        TextView friendCode = findViewById(R.id.friendCodeBox);
        TextView accessCode = findViewById(R.id.accessCodeBox);

        if(!password.getText().toString().equals(conPass.getText().toString())){
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Your Passwords do not match!")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        else if(password.getText().toString().length()<10){
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Your Password must be at least 10 characters")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        else {
            System.out.println("User: "+ user.getText().toString() + " | Island: " + island.getText().toString() + " | Password: " + password.getText().toString() + " | AccessCode: " + accessCode.getText().toString() + " | FriendCode:" + friendCode.getText().toString());
            sigu s = new sigu(user.getText().toString(),island.getText().toString(),password.getText().toString(),accessCode.getText().toString(),friendCode.getText().toString());
            Thread T = new Thread(s);
            T.start();
            T.join();
            if(s.getTimeout()!=null){
                new AlertDialog.Builder(this)
                        .setTitle("Error!")
                        .setMessage("Failed to connect to the Stalk Exchange! Try again later?")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
            else{
                switch(s.getResponsecode()){
                    case 200:
                        new AlertDialog.Builder(this)
                                .setTitle("Signed Up!")
                                .setMessage("You're all signed up!")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .show();
                        break;
                    case 401:
                        new AlertDialog.Builder(this)
                                .setTitle("Error!")
                                .setMessage("Incorrect access code. Try again.")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                        break;
                    case 500:
                        new AlertDialog.Builder(this)
                                .setTitle("Error!")
                                .setMessage("That friend code has already been signed up! Sorry!")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                        break;
                }
            }

        }


    }

    public void backbutton(View V){
        finish();
    }
}
