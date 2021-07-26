package com.example.chirper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.chirper.Adapters.FragmentsAdapter;
import com.example.chirper.databinding.ActivityDashboardBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class Dashboard extends AppCompatActivity
{

    private FirebaseAuth mFirebaseAuth;
    private ActivityDashboardBinding mActivityDashboardBinding;
    FirebaseUser mFirebaseUser;
    private FirebaseDatabase mDashboardDatabase;
    private DatabaseReference mDatabaseReferenceUsers;

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReferenceUsers.child("lastseen").setValue("true");
        mDatabaseReferenceUsers.child("online_status").setValue(true);


    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabaseReferenceUsers.child("lastseen").onDisconnect().setValue(ServerValue.TIMESTAMP);
        mDatabaseReferenceUsers.child("online_status").onDisconnect().setValue(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(mActivityDashboardBinding.getRoot());

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDashboardDatabase = FirebaseDatabase.getInstance("https://chirper-f0c29-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDashboardDatabase.getReference().keepSynced(true);
        mDatabaseReferenceUsers = mDashboardDatabase.getReference().child("Users").child(mFirebaseUser.getUid());

        mActivityDashboardBinding.viewpager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        mActivityDashboardBinding.tablayout.setupWithViewPager(mActivityDashboardBinding.viewpager);



        /*
        <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">


        <androidx.appcompat.widget.Toolbar
            android:id="@+id/dashboard_toolbar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="?attr/colorPrimary"
            android:minHeight="?attr/actionBarSize"
            app:titleTextColor="@color/white"
            app:title="Chirper"/>


    </com.google.android.material.appbar.AppBarLayout>
         */


        //Lets try to commit this change



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

            case R.id.user_profile:

                Intent profileintent = new Intent(Dashboard.this, ProfileActivity.class);
                startActivity(profileintent);
                break;

            case R.id.settings:

                Toast.makeText(Dashboard.this,"Settings selected",Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:

                String idpass = mFirebaseUser.getUid();
                mDatabaseReferenceUsers.child("lastseen").setValue(ServerValue.TIMESTAMP);
                mFirebaseAuth.signOut();
                Intent intent_log = new Intent(Dashboard.this, SignInActivity.class);
                intent_log.putExtra("Previous intent", "Dashboard");
                intent_log.putExtra("online_status_checker", idpass);
                startActivity(intent_log);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);

    }


}