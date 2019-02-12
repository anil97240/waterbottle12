package com.example.waterbottle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.admin_agent_side.SlidingImage_Adapter;
import com.example.waterbottle.admin_agent_side.product_model.Product;
import com.example.waterbottle.admin_agent_side.sliding_image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class customer_order extends AppCompatActivity {


    private static final String TAG = "MOHIT";
    private static final String[] IMAGESNAME = {"Bisleri", "kaveri", "Oxyri", "Mini Bisleri"};
    private static final Integer[] IMAGES = {R.drawable.bta, R.drawable.na, R.drawable.nb, R.drawable.nc};
    //bottle details
    private static TextView tvdetails, tvtotalbottle;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    EditText edtbottle;
    List<sliding_image> slid_img;

    sliding_image sliding_image;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    //database reference to get uploads data
    DatabaseReference mDatabaseReference;

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
        tvtotalbottle = (TextView) findViewById(R.id.txttotalbottle);

        slid_img=new ArrayList<>();

        //init();
        intitt();
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_backward);
//       e fab.seetOnClieckListener(this);
        //   e fab1.seteeOnCleickListeeneeer(this);
        //   efeeab2ee.seeteOneeCeelieeckLisetener(this);
        //ee   efab3.setOeeenCeleeieckLisetenere(this);
        //   feab4.setOnClickListeneer(this);


    }



    public void intitt()
    {
        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Product_data");

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product upload = postSnapshot.getValue(Product.class);

                }

                String[] uploads = new String[slid_img.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = String.valueOf(slid_img.get(i).getIMAGE());
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }















    private void init() {

        slid_img = new ArrayList<>();
        for (int i = 0; i < IMAGES.length; i++) {
            int s = IMAGES[i];
            slid_img.add(new sliding_image(s));

            mPager = (ViewPager) findViewById(R.id.pager);

            tvdetails = findViewById(R.id.tvbottledetails);


            mPager.setAdapter(new SlidingImage_Adapter(customer_order.this, slid_img));

            CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

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

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                    tvdetails.setText(IMAGESNAME[position]);
                    if (position > 0) {
                        //total();

                    }
                    switch (position) {
                        case 0:
                            tvtotalbottle.setText("dc");


                            break;
                        case 1:
                            Toast.makeText(customer_order.this, "1", Toast.LENGTH_SHORT).show();
                            tvtotalbottle.setText("1");

                            break;
                        case 2:
                            Toast.makeText(customer_order.this, "2", Toast.LENGTH_SHORT).show();
                            tvtotalbottle.setText("2");
                            break;
                        case 3:
                            Toast.makeText(customer_order.this, "3", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(customer_order.this, "000", Toast.LENGTH_SHORT).show();
                            break;

                    }


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
        int a = Integer.parseInt(edtbottle.getText().toString());
        int b = 1;
        a = b + a;

        edtbottle.setText("" + a);
        tvtotalbottle.setText("Total Bottles :" + a);

    }

    public void minbottle(View view) {
        //using button sub bottles
        int a = Integer.parseInt(edtbottle.getText().toString());
        if (a == 0) {
            Toast.makeText(this, "No Bottle In Cart", Toast.LENGTH_SHORT).show();
        } else {
            int b = 1;
            a = a - b;
            //set bottle in textbox and edittextbox


            edtbottle.setText("" + a);
            tvtotalbottle.setText("Total Bottles :" + a);
        }
    }

    public void btnok(View view) {

        Toast.makeText(this, "Order Successfully", Toast.LENGTH_SHORT).show();
    }

    private void total() {
        //set data on Text view and get data from sliding_image
        sliding_image sv = new sliding_image();
        String s = sv.getTotal();
        tvtotalbottle.setText(s);
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }


}


