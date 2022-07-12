package com.example.chat_bknj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
               if (firebaseUser !=null){
                   Intent in=new Intent(getApplicationContext(),Dashboard.class);
                   startActivity(in);
                   finish();
           }else {
                   Intent i=new Intent(getApplicationContext(),Authentification.class);
                   startActivity(i);
                   finish();
               }
       }

        },6000);
    }
}
