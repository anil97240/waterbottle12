package com.example.waterbottle.admin_agent_side;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.product_model.Product;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class agent_add_bottle extends AppCompatActivity {
    public static final Integer[] IMAGES = {R.drawable.bta, R.drawable.na, R.drawable.nb, R.drawable.nc};
    private static final String[] IMAGESNAME = {"Bisleri", "kaveri", "Oxyri", "Mini Bisleri"};
    //bottle details
    private static TextView tvdetails, tvtotalbottle;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public String date_of_birth;
    public String full_name;
    public String nickname;
    int bottleprice = 0;
    EditText edtbottle;
    //save in firebase
    String userName;
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    String s;
    Product pro;

    List<sliding_image> slid_img;
    List<Product> productList;
    ArrayList arrayList = new ArrayList<String>();
    ArrayList arrayList1 = new ArrayList<String>();
    ArrayList arrprice = new ArrayList<String>();
    String[] uploads;
    String[] adddata;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_agent_add_bottle);

        tvtotalbottle = findViewById(R.id.txttotalbottle2);

        FirebaseApp.initializeApp(this);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Product_data");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    slid_img = new ArrayList<>();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        sliding_image list = postSnapshot.getValue(sliding_image.class);
                        slid_img.add(list);
                        arrayList1.add(postSnapshot.getKey());
                    }

                    String[] uploads = new String[slid_img.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = slid_img.get(i).getImage();

                    }

                    for (int i = 0; i < uploads.length; i++) {
                        String s = (uploads[i]);
                        mPager = (ViewPager) findViewById(R.id.pager);

                        tvdetails = findViewById(R.id.tvbottledetails);

                        mPager.setAdapter(new SlidingImage_Adapter(agent_add_bottle.this, slid_img));

                        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

                        indicator.setViewPager(mPager);

                        final float density = getResources().getDisplayMetrics().density;

                        //Set circle indicator radius
                        indicator.setRadius(8 * density);

                        NUM_PAGES = uploads.length;
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

                                sliding_image slid = new sliding_image();
                                String a = slid.getQry();

                                tvdetails.setText(slid_img.get(position).getProduct_name());
                                tvtotalbottle.setText(slid_img.get(position).getProduct_Price());
                                //total();

                            }

                            @Override
                            public void onPageScrolled(int pos, float arg1, int arg2) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int pos) {

                            }
                        });

                    }

                } catch (Exception e) {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        pro = new Product();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            userName = extras.getString("qrcode");

        }


    }

    private void total() {

        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }
//get all product from firebase

    //add dialog
    public void showpaymentamount() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        View view = getLayoutInflater().inflate(R.layout.paymet_agent_dialog, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);

        dialog.show();

        final EditText edtamount = view.findViewById(R.id.edtamount);
        final EditText edtpaddingamount = view.findViewById(R.id.edtpaddingamount);
        final TextView tvtotal = view.findViewById(R.id.tvtotalamount);


        view.findViewById(R.id.btnclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.findViewById(R.id.btnaddorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {
                    int a = 0, t = 0, p = 0;

                    String s,s1;
                    s = edtamount.getText().toString();
                    s1 = edtpaddingamount.getText().toString();

                    if(!s.equals("")) {
                        a = Integer.parseInt(s);

                    }
                    else if(!s1.equals(""))
                    {
                        p = Integer.parseInt(s1);
                    }

                    t = a + p;





                    Firebase ref;

                    ref = new Firebase(url);
                    String date;
                    date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    Map<String, String> users = new HashMap<>();

                    users.put("Botttle_total", tvtotalbottle.getText().toString());
                    users.put("Amount_collected", String.valueOf(a));
                    users.put("Padding_amount", String.valueOf(p));
                    users.put("Delivry_date", date);
                    users.put("QR_code", userName);
                    users.put("Total_amount", String.valueOf(t));
                    users.put("Bottle_type", tvdetails.getText().toString());
                    ref.child("Bottle_delivered").push().setValue(users);

                    Toast.makeText(getApplicationContext(), "Order Successfully Delivered", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        });
    }

    public void btnok(View view) {
        showpaymentamount();

    }

    public void goback(View view) {
        Intent intent = new Intent(this, agent_barcode.class);
        startActivity(intent);
        finish();
    }
}

