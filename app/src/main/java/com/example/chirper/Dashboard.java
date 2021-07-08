package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chirper.databinding.ActivityDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity
{

    FirebaseAuth mFirebaseAuth;
    private ActivityDashboardBinding mActivityDashboardBinding;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(mActivityDashboardBinding.getRoot());

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //Lets try to commit this change
        if(!mFirebaseUser.isEmailVerified()) {

            mActivityDashboardBinding.notverified.setVisibility(View.VISIBLE);

        }

        mActivityDashboardBinding.notverified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirebaseAuth.signOut();
                Intent intent = new Intent(Dashboard.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item ) {

        switch (item.getItemId()) {

            case R.id.settings:

                Toast.makeText(Dashboard.this,"Settings selected",Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:

                mFirebaseAuth.signOut();
                Intent intent_log = new Intent(Dashboard.this, SignInActivity.class);
                startActivity(intent_log);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);

    }


}