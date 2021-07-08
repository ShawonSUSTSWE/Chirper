package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        getSupportActionBar().hide();

        Thread thread = new Thread() {

            public void run() {

                try {

                    sleep(4000);

                } catch (Exception e) {

                    e.printStackTrace();

                }
                finally {
                    Intent intent = new Intent(Splashscreen.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }


            }


        };
        thread.start();
    }
}