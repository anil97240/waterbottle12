package com.example.waterbottle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.admin_agent_side.SlidingImage_Adapter;
import com.example.waterbottle.admin_agent_side.agent_add_bottle;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class customer_order extends AppCompatActivity{


    //bottle details
    private static TextView tvdetails,tvtotalbottle;
    EditText edtbottle;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final String[] IMAGESNAME= {"Bisleri","kaveri","Oxyri","Mini Bisleri"};
    private static final Integer[] IMAGES= {R.drawable.bta,R.drawable.na,R.drawable.nb,R.drawable.nc};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_customer_order);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        init();
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_backward);
//        fab.setOnClickListener(this);
    //    fab1.setOnClickListener(this);
     //   fab2.setOnClickListener(this);
     //   fab3.setOnClickListener(this);
     //   fab4.setOnClickListener(this);


    }
    private void init() {
/*        for (int i = 0; i < IMAGES.length; i++) {

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
            mPager.setAdapter(new SlidingImage_Adapter(customer_order.this, ImagesArray));


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
         /*   indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                    tvdetails.setText(IMAGESNAME[position]);
                    edtbottle.setText(""+0);


                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {


                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });

        }*/
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

        Toast.makeText(this, "Order Successfully", Toast.LENGTH_SHORT).show();
    }

}


