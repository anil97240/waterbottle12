package com.example.waterbottle.admin_agent_side;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class agent_barcode extends AppCompatActivity implements  View.OnClickListener {

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2,fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private IntentIntegrator qrScan;
    Firebase ref;
    String a;

    List userlist;

    ArrayList arrayList = new ArrayList<String>();

    ArrayList arrayList1 = new ArrayList<String>();
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//for firebase data retrive
        Firebase.setAndroidContext(this);
        String url="https://waterbottle12-e6aa9.firebaseio.com/QR_CODE";
        ref = new Firebase(url);
        //DatabaseReference myRef = database.getReference("market");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    //userlist.datas(String.valueOf(dsp.getKey())); //add result into array list
                    String key1= dsp.getKey();
                    //dsp.child("https://waterbottle12-e6aa9.firebaseio.com/QR_CODE");
                    //condition code
                        arrayList.add(dsp.getValue().toString());
                        arrayList.add(key1);
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_agent_barcode);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;

            case R.id.fab1:
                Log.d("Raj", "Fab 1");
                Intent i1=new Intent(this,agent_add_bottle.class);
               startActivity(i1);
                break;

            case R.id.fab2:


                //Logout From cURRENT User

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fAuth.signOut();
                if (fAuth != null)
                {
                    Intent i=new Intent(getApplicationContext(),agent_login.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(this, "Cant Logout", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

        private void animateFAB(){
            if (isFabOpen) {

                fab.startAnimation(rotate_backward);
                fab1.startAnimation(fab_close);
                fab2.startAnimation(fab_close);

                fab1.setClickable(false);
                fab2.setClickable(false);

                isFabOpen = false;
                Log.d("Raj", "close");

            } else {

                fab.startAnimation(rotate_forward);
                fab1.startAnimation(fab_open);
                fab2.startAnimation(fab_open);

                fab1.setClickable(true);
                fab2.setClickable(true);

                isFabOpen = true;
                Log.d("Raj", "open");

            }
        }


        public void goback(View view)
        {
       /* Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);*/
       finish();
        }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    // textViewName.setText(obj.getString("name"));
                    // textViewAddress.setText(obj.getString("address"));



                } catch (JSONException e) {
                    e.printStackTrace();

                    String toTest = result.getContents();


                     //   Toast.makeText(this, "data:"+datarr, Toast.LENGTH_SHORT).show();
                        if (arrayList.contains(toTest)) {

                         //   Toast.makeText(this, "1:" + arrayList.get(i) + "2:" + toTest, Toast.LENGTH_SHORT).show();
                            Intent i1 = new Intent(getApplicationContext(), agent_add_bottle.class);
                            i1.putExtra("qrcode", result.getContents());
                            startActivity(i1);
                        }
                        else {
                            Toast.makeText(this, "QR Not found", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void btnscanner(View view) {
        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();

    }
}

