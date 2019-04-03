package com.example.waterbottle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.agrawalsuneet.loaderspack.loaders.CurvesLoader;
import com.example.waterbottle.admin_agent_side.admin_dashboard;
import com.example.waterbottle.admin_agent_side.admin_view_order;
import com.example.waterbottle.admin_agent_side.agent_login;
import com.example.waterbottle.admin_agent_side.order_delivered;
import com.example.waterbottle.client_side.client_model.Bottle_delivered;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CurvesLoader curvesLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);

        SharedPreferences prfs = getSharedPreferences("auth",MODE_PRIVATE);
        String authname = prfs.getString("authname", "");

        if(authname=="")
        {
            Intent intent = new Intent(MainActivity.this, agent_login.class);
            startActivity(intent);
            finish();
        }
        else
        {


        }



    }

    public void order(View view) {

        Intent i=new Intent(MainActivity.this, admin_view_order.class);
        startActivity(i);
    }

    public void otherdata(View view) {
        Intent i=new Intent(MainActivity.this, admin_dashboard.class);
        startActivity(i);

    }

    public void ordercomplete(View view) {
        Intent i=new Intent(MainActivity.this, order_delivered.class);
        startActivity(i);

    }
}



