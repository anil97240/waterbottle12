package com.example.waterbottle.admin_agent_side;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class agent_add_bottle extends AppCompatActivity {

    //bottle details
    private static TextView tvdetails,tvtotalbottle;
    EditText edtbottle;
//save in firebase
String userName;
    public String date_of_birth;
    public String full_name;
    public String nickname;
    String url="https://waterbottle12-e6aa9.firebaseio.com/";
    String s;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String[] IMAGESNAME= {"Bisleri","kaveri","Oxyri","Mini Bisleri"};
    private static final Integer[] IMAGES= {R.drawable.bta,R.drawable.na,R.drawable.nb,R.drawable.nc};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ArrayList<String> Imagesname = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_agent_add_bottle);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            userName = extras.getString("qrcode");
            // and get whatever type user account id is
        }
        init();
    }

    private void init() {
        for (int i = 0; i < IMAGES.length; i++) {

//            Imagesname.add((IMAGESNAME[i]));
            ImagesArray.add(IMAGES[i]);
//

            mPager = (ViewPager) findViewById(R.id.pager);
            tvdetails = findViewById(R.id.tvbottledetails);
            edtbottle=findViewById(R.id.edtadd);
            tvtotalbottle=findViewById(R.id.txttotalbottle);
            tvtotalbottle.setText(""+0);
            edtbottle.setText(""+0);
           // tvdetails.setText((CharSequence) Imagesname);
            mPager.setAdapter(new SlidingImage_Adapter(agent_add_bottle.this, ImagesArray));


            CirclePageIndicator indicator = (CirclePageIndicator)
                    findViewById(R.id.indicator);

            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;





        //Set circle indicator radius
            indicator.setRadius(8 * density);

            NUM_PAGES = IMAGES.length;


            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
          /*  Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 300000, 300000);*/

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                    tvdetails.setText(IMAGESNAME[position]);


                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {


                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });

        }
    }

    public void addbottle(View view) {
        int a= Integer.parseInt(edtbottle.getText().toString());
        int b=1;
        a=b+a;

        edtbottle.setText(""+a);
        tvtotalbottle.setText("Total Bottles :"+a);

    }

    public void minbottle(View view) {
        //using button sub bottles
        int a= Integer.parseInt(edtbottle.getText().toString());
        if(a==0)
        {
            Toast.makeText(this, "No Bottle In Cart", Toast.LENGTH_SHORT).show();
        }
        else {
            int b = 1;
            a = a-b;
        //set bottle in textbox and edittextbox
            edtbottle.setText("" + a);
            tvtotalbottle.setText("Total Bottles :"+a);
        }
        }

    public void btnok(View view) {

        Firebase ref;

        ref = new Firebase(url);

      //  Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show();
       // DatabaseReference usersRef = ref.child("users");

        Map<String, String> users = new HashMap<>();
        users.put("Botttle",edtbottle.getText().toString() );
        users.put("QR_code",userName.toString());
        users.put("Bottle_type",tvdetails.getText().toString());
        ref.child("Bottle_delivered").push().setValue(users);

       // ref.child()

     //   ref.setValue(users);
        Toast.makeText(this, "Order Successfully Delivered", Toast.LENGTH_SHORT).show();

    }

    public void goback(View view) {
        Intent intent = new Intent(this,agent_barcode.class);
        startActivity(intent);
        finish();
    }
}