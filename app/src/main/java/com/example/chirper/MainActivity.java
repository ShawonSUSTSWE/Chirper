package com.example.chirper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chirper.databinding.ActivityMainBinding;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        getSupportActionBar().hide();

        mActivityMainBinding.ccp.registerCarrierNumberEditText(mActivityMainBinding.mobile);

        mActivityMainBinding.userphonesignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this, Manageotp.class);
                intent.putExtra("mobile", mActivityMainBinding.ccp.getFullNumberWithPlus().replace(" ",""));
                intent.putExtra("NAME", mActivityMainBinding.username.getText().toString());
                startActivity(intent);
                finish();

            }
        });
    }
}