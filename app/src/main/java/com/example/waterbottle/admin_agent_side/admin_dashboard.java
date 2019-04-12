package com.example.waterbottle.admin_agent_side;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.loaderspack.loaders.CurvesLoader;
import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.Model.CustomAdepterAgent;
import com.example.waterbottle.admin_agent_side.Model.MyListAdapter;
import com.example.waterbottle.admin_agent_side.Model.agent;
import com.example.waterbottle.admin_agent_side.Model.deliver_order;
import com.example.waterbottle.client_side.client_model.Client;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class admin_dashboard extends AppCompatActivity implements View.OnClickListener {
    CurvesLoader curvesLoader;
    Spinner spd;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "Customer_data";
    public static final String DATABASE_PATH_UPLOADS1 = "Product_data";
    private static final String TAG = "Mohit";
    private static final int PICK_IMAGE_REQUEST = 234, PICK_IMAGE_REQUEST1 = 234;
    List<agent> agentList;
    MyListAdapter adapter;
    //the listview
    EditText edtnm;
    EditText edtmob;
    EditText edtadd;
    EditText edtadd2;
    EditText edtpnm;
    EditText edtprice;
    EditText edtdetail;

    ListView listView;
    String filedownloadpath;
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    EditText edtbarcode, edtemail, edtpass;
    Bitmap bitmap;
    ImageView imgview;
    ImageView img_pro;
    String toTest;
    EditText inputSearch;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private TextView tvhide, tvagenthide, tvproducthide, tvlogouthide;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private Firebase mRef;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    ArrayList arrayList = new ArrayList<String>();
    //the list values in the List of type hero
    //list to store uploads data
    List<agent> deliver_orderList;
    List<deliver_order> deliver_order_data;
    //search
    private DatabaseReference mDatabaseReference;
    //qr Scanner
    private IntentIntegrator qrScan;
    private Uri filePath;
    int p;

    ExpandableListView expandableListView;
    CustomAdepterAgent customExpandableListViewAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> childItem;
    int counter = 0;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_admin_dashboard);

        SharedPreferences prfs = getSharedPreferences("auth", MODE_PRIVATE);
        String authname = prfs.getString("authname", "");
        expandableListView = findViewById(R.id.expandableListView);
        curvesLoader = findViewById(R.id.curvesLoader);
        if (authname == "") {
            Intent intent = new Intent(admin_dashboard.this, agent_login.class);
            startActivity(intent);
            finish();
        } else {
        }
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

     /*   FirebaseUser user =mAuth.getCurrentUser();
        String provider = user.getProviders().get(0);*/


        SetStandardGroups();
        customExpandableListViewAdapter = new CustomAdepterAgent(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(customExpandableListViewAdapter);
        deliver_order_data = new ArrayList<>();

        deliver_orderList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        Firebase.setAndroidContext(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Agent_data");

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        agent upload = postSnapshot.getValue(agent.class);
                        deliver_orderList.add(upload);
                        arrayList.add(postSnapshot.getKey());
                    }
                    curvesLoader.setVisibility(View.GONE);

                    String[] uploads = new String[deliver_orderList.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = deliver_orderList.get(i).getAgent_mobile();
                        /*uploads[i] = deliver_orderList.get(i).getAgent_email();
                        uploads[i] = deliver_orderList.get(i).getBotttle_total();*/
                        Log.e(TAG, "onDataChange: " + uploads[i].toString());
                    }
                    //displaying it to list
                    final MyListAdapter adapter = new MyListAdapter(getApplicationContext(), R.layout.my_custom_list, deliver_orderList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            p = position;
                            String alert1 = "Email  :  " + deliver_orderList.get(p).getAgent_email();
                            String alert2 = "Mobile No  :  " + deliver_orderList.get(p).getAgent_mobile();
                            String alert3 = "Name           :  " + deliver_orderList.get(p).getAgent_name();
                            String alert4 = "Password    :  " + deliver_orderList.get(p).getAgent_password();
                            String alert5 = "Type           :  " + deliver_orderList.get(p).getType();

                            final AlertDialog.Builder builder = new AlertDialog.Builder(admin_dashboard.this, R.style.Theme_AppCompat_DayNight_Dialog);

                            builder.setTitle("Details");
                            builder.setMessage(alert1 + "\n" + alert2 + "\n" + alert3 + "\n" + alert4 + "\n" + alert5);
                            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Delete</font></font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mDatabaseReference.child(String.valueOf(arrayList.get(p))).removeValue();
                                    //  Toast.makeText(Admin_view_all_client.this, "data remove", Toast.LENGTH_SHORT).show();
                                    deliver_orderList.remove(p);
                                    deliver_orderList.clear();
                                    arrayList.clear();
                                    adapter.notifyDataSetChanged();
                                    //do things
                                }
                            });
                            builder.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font></font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                    //select product for delete
                } catch (Exception e) {
                    Log.e(TAG, "New: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled:" + databaseError.getMessage());
            }
        });
        agentList = new ArrayList<>();

        inputSearch = findViewById(R.id.inputSearch1);
        //initializing objects
        tvhide = findViewById(R.id.tvhide);
        tvagenthide = findViewById(R.id.tvagenthide);
        tvproducthide = findViewById(R.id.tvhideproduct);
        tvlogouthide = findViewById(R.id.tvhidelogout);
        adapter = new MyListAdapter(this, R.layout.my_custom_list, agentList);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(adapter);
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab4 = findViewById(R.id.fab4);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_backward);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);


        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // listView.setAdapter(adapter);
                adapter.getFilter().filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void SetStandardGroups() {

        agentList = new ArrayList<>();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Agent_data");
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> childItem;
                int counter = 0;
                // final String headerTitle = dataSnapshot.getKey();
                //  Toast.makeText(admin_view_order.this, "" + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                //    Log.e("TAG", headerTitle);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //   String child = (String) ds.getValue();
                    agent child = ds.getValue(agent.class);
                    agentList.add(child);
                    listDataHeader.add("     " + child.getAgent_name() + "                           " + child.getAgent_mobile() + "                      " + child.getType());
                   /* childItem.add("Price :" + String.valueOf(child.getBottle_price()));
                    childItem.add("Mobile No :" + String.valueOf(child.getCustomer_id()));
                    childItem.add("Quantity :" + String.valueOf(child.getBotttle_qty()));
                    childItem.add("Type :" + String.valueOf(child.getBottle_type()));
                    childItem.add("Order Date :" + String.valueOf(child.getOrder_date()));
                    childItem.add("total cost :" + String.valueOf(child.getTotal_cost()));*/
                }
                //customer data display order delived

                mDatabaseReference = FirebaseDatabase.getInstance().getReference("collected_amount");
                //retrieving upload data from firebase database
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> childItem;
                        int counter = 0;
                        // final String headerTitle = dataSnapshot.getKey();
                        //  Toast.makeText(admin_view_order.this, "" + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                        //    Log.e("TAG", headerTitle);
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //   String child = (String) ds.getValue();
                            deliver_order child = ds.getValue(deliver_order.class);
                            deliver_order_data.add(child);
                   /* childItem.add("Price :" + String.valueOf(child.getBottle_price()));
                    childItem.add("Mobile No :" + String.valueOf(child.getCustomer_id()));
                    childItem.add("Quantity :" + String.valueOf(child.getBotttle_qty()));
                    childItem.add("Type :" + String.valueOf(child.getBottle_type()));
                    childItem.add("Order Date :" + String.valueOf(child.getOrder_date()));
                    childItem.add("total cost :" + String.valueOf(child.getTotal_cost()));*/
                        }
                        childItem = new ArrayList<>();
                        childItem.add("   " + deliver_order_data.get(0).getCollected_Date());

                        for (int i = 0; i < agentList.size(); i++) {
                            try {
                                childItem = new ArrayList<>();

                                childItem.add("      Date                     C.Name                   C.Amt");

                                String orderdata = agentList.get(i).getAgent_email();
                                //  Toast.makeText(admin_view_order.this, ""+orderdata, Toast.LENGTH_SHORT).show();
                                for (int j = 0; j < deliver_order_data.size(); j++) {
                                    if (deliver_order_data.get(j).getAgent_email().equals(orderdata)) {
                                        //  childItem.add("Price :" + String.valueOf(orderList.get(i).getOrder_id() + "        " + "Mobile No :" + String.valueOf(orderList.get(i).getCustomer_id())));
                                        //  childItem.add("Quantity :" + String.valueOf(orderList.get(i).getBotttle_qty() + "        " + "Status :" + String.valueOf(orderList.get(i).getStatus())));
                                        childItem.add("   "+deliver_order_data.get(j).getCollected_Date() + "           " + deliver_order_data.get(j).getCustomer_name() + "                         " + deliver_order_data.get(j).getAmount_collected());
                                        // childItem.add();
                                        //  childItem.add("Quantity  : "+orderdetailList.get(j).getBotttle_qty()+""+"Price  : "+orderdetailList.get(j).getBottle_price());
                                    }
                                    listDataChild.put(listDataHeader.get(i),childItem);
                                    counter++;
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "onDataChange" + e);
                            }
                        }
                        customExpandableListViewAdapter.notifyDataSetChanged();
                        curvesLoader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                showCustomDialog();
                break;
            case R.id.fab2:
                showAgentDialog();
                break;
            case R.id.fab3:
                showProductDialog();
                break;
            case R.id.fab4:
                logout();
                break;
        }
    }

    public void showProductDialog() {

        View view = getLayoutInflater().inflate(R.layout.dialong_product_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(admin_dashboard.this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);

        //get all edittext in dialog_customer_add
        edtpnm = view.findViewById(R.id.edtnm);
        edtprice = view.findViewById(R.id.edtprice);
        edtdetail = view.findViewById(R.id.edtdetail);
        img_pro = view.findViewById(R.id.imgview);

        FloatingActionButton uploadproimage = view.findViewById(R.id.btnuploadimg);
        edtpnm.requestFocus();
        view.findViewById(R.id.btnproclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        view.findViewById(R.id.btnallproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), admin_view_product.class);
                startActivity(i);
                dialog.dismiss();
            }
        });

        img_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        uploadproimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser1();


            }
        });
        view.findViewById(R.id.btnaddproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile1();
            }
        });
        dialog.show();
    }

    public void showAgentDialog() {
        final String[] type = {"Agent"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        View view = getLayoutInflater().inflate(R.layout.dialong_agent_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();
        spd = view.findViewById(R.id.spinnerid);
        edtemail = view.findViewById(R.id.edtemail);
        edtpass = view.findViewById(R.id.edtpass);

        final EditText edtname = view.findViewById(R.id.edtname);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        edtname.requestFocus();

        view.findViewById(R.id.btnviewallagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(getApplicationContext(), admin_dashboard.class);
                startActivity(i);*/
                dialog.dismiss();
            }
        });

        spd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type[0] = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        view.findViewById(R.id.btnclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        view.findViewById(R.id.btnaddagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Firebase ref;
                ref = new Firebase(url);
                String email = edtemail.getText().toString();
                String pass = edtpass.getText().toString();
                String name = edtname.getText().toString();
                String mob = edtmob.getText().toString();
                Validation_text valid = new Validation_text();
                if (!valid.isValidName(name)) {
                    edtname.setError("Invalid Name");
                } else if (!valid.isValidEmail(email)) {
                    edtemail.setError("Invalid Email");
                } else if (!valid.isValidPassword(pass)) {
                    edtpass.setError("Invalid Password");
                } else if (!valid.isValidMobile(mob)) {
                    edtmob.setError("Invalid Mobile No");
                } else {
                    //insert Agent in firebase
                    Map<String, String> users = new HashMap<>();
                    users.put("Agent_email", email);
                    users.put("Agent_password", pass);
                    users.put("Agent_name", name);
                    users.put("Agent_mobile", mob);
                    users.put("Type", type[0]);
                    ref.child("Agent_data").child("+91" + mob).setValue(users);
                    Toast.makeText(getApplicationContext(), "Agent add", Toast.LENGTH_SHORT).show();
                    registerUser();

                    edtemail.setText("");
                    edtpass.setText("");
                    edtname.setText("");
                    edtmob.setText("");

                    deliver_orderList.remove(p);
                    deliver_orderList.clear();
                    arrayList.clear();
                }
            }
        });
    }

    public void registerUser()
    {
        String email = edtemail.getText().toString();
        String password = edtpass.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_customer_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();
        //get all edittext in dialog_customer_add
        edtnm = view.findViewById(R.id.edtnm);
        edtmob = view.findViewById(R.id.edtmob);
        edtadd = view.findViewById(R.id.edtname);
        edtadd2 = view.findViewById(R.id.edtaddresstwo);
        edtbarcode = view.findViewById(R.id.edtbarcode);
        final Button btnadd = view.findViewById(R.id.btnaddclient);
        imgview = view.findViewById(R.id.imgview);
        final FloatingActionButton btnuploadimg = view.findViewById(R.id.btnuploadimg);
        edtnm.requestFocus();
        //image chooser

        view.findViewById(R.id.btncloseclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //image upload
        btnuploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userName = extras.getString("qrcode");
            // and get whatever type user account id is
            // edtbarcode.setText(userName.toString());
        }


        view.findViewById(R.id.btnallclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all client display activity
                Intent i = new Intent(getApplicationContext(), Admin_view_all_client.class);
                startActivity(i);
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.btnscannbarcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan = new IntentIntegrator(admin_dashboard.this);
                qrScan.initiateScan();
            }
        });

        //upload image
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                //  Toast.makeText(admin_dashboard.this, ""+edtnm.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showFileChooser() {
        //for image select
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    private void showFileChooser1()
    {
        //for image select
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
    }

    public String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void animateFAB()
    {
        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            tvhide.startAnimation(fab_close);
            tvagenthide.startAnimation(fab_close);
            tvproducthide.startAnimation(fab_close);
            tvlogouthide.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");
        }
        else
            {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            tvhide.startAnimation(fab_open);
            tvagenthide.startAnimation(fab_open);
            tvproducthide.startAnimation(fab_open);
            tvlogouthide.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");
        }
    }

    private void uploadFile1() {
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS1);
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();//displays the progress bar
            //getting the storage reference
            StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            filedownloadpath = taskSnapshot.getDownloadUrl().toString();
                            addproductdata();
                            //adding an upload to firebase database
                          /*  String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);*/
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {

            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void addproductdata() {
        Firebase ref;
        ref = new Firebase(url);
        String pnm = edtpnm.getText().toString();
        String price = edtprice.getText().toString();
        String detail = edtdetail.getText().toString();
        Validation_text valid = new Validation_text();

        if (!valid.isValidName(pnm)) {
            edtpnm.setError("Invalid Product Name");
        } else if (!valid.isValidPrice(price)) {
            edtprice.setError("Invalid Price");
        } else if (!valid.isvaliddetails(detail)) {
            edtdetail.setError("Invalid Product Detail");
        } else {
            //insert product in firebase
            Map<String, String> users = new HashMap<>();
            users.put("Product_name", pnm);
            users.put("Product_Price", price);
            users.put("Product_detail", detail);
            users.put("image", filedownloadpath);
            ref.child("Product_data").push().setValue(users);
            Toast.makeText(getApplicationContext(), "Product add", Toast.LENGTH_SHORT).show();

            edtpnm.setText("");
            edtprice.setText("");
            edtdetail.setText("");
            img_pro.setImageBitmap(null);
            img_pro.setImageResource(R.drawable.cus);

        }
    }

    private void uploadFile() {
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            //getting the storage reference
            StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            //creating the upload object to store uploaded image details
                            Client cn = new Client();
                            //   Client upload = new Client(cn.getCustomer_name(),cn.getMobile_number(),cn.getAddress(),taskSnapshot.getDownloadUrl().toString());
                            filedownloadpath = taskSnapshot.getDownloadUrl().toString();
                            try {

                                filedownloadpath = taskSnapshot.getDownloadUrl().toString();
                                if (filedownloadpath == "") {
                                    Toast.makeText(admin_dashboard.this, "No image Selected", Toast.LENGTH_SHORT).show();
                                } else {
                                    addclientdata();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Uri Not found: " + e.getMessage());
                            }
                            //adding an upload to firebase database
                          /*  String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);*/
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
            noimageaddclientdata();
        }
    }

    private void noimageaddclientdata() {
        Firebase ref;
        ref = new Firebase(url);
        //  Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show();
        // DatabaseReference usersRef = ref.child("users");
        final String nm = edtnm.getText().toString();
        final String mob = edtmob.getText().toString();
        final String add = edtadd.getText().toString();
        final String add2 = edtadd2.getText().toString();
        final String qrcode = edtbarcode.getText().toString();
        Validation_text valid = new Validation_text();
        if (!valid.isValidName(nm)) {
            edtnm.setError("Invalid Name");
        } else if (!valid.isValidMobile(mob)) {
            edtmob.setError("Invalid Mobile Number");
        } else if (!valid.isValidName(add)) {
            edtadd.setError("Invalid Address");
        } else if (!valid.isValidName(add2)) {
            edtadd2.setError("Invalid Address");
        } else if (!valid.isValidName(qrcode)) {
            edtbarcode.setError("Invalid Qrcode");
        } else {
            //for data get using client
            Client client = new Client();
            client.setCustomer_name(nm);
            client.setMobile_number(mob);
            client.setAddress(add);
            client.setCustomer_qrcode(qrcode);
            Map<String, String> users = new HashMap<>();
            users.put("Customer_name", nm);
            users.put("Mobile_number", mob);
            users.put("Address", add);
            users.put("Local_address", add2);
            users.put("image", "");
            // users.put("upload",)
            // mDatabase.child(uploadId).setValue(upload);
            users.put("Customer_qrcode", qrcode);
            ref.child("Customer_data").child("+91" + mob.toString()).setValue(users);
            // ref.child()
            //   ref.setValue(users);
            Toast.makeText(getApplicationContext(), "Customer added", Toast.LENGTH_SHORT).show();
            edtnm.setText("");
            edtadd2.setText("");
            edtadd.setText("");
            edtmob.setText("");
            edtbarcode.setText("");
            imgview.setImageResource(R.drawable.cus);

        }

    }

    //no image
    //with image
    private void addclientdata() {
        Firebase ref;
        ref = new Firebase(url);
        //  Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show();
        // DatabaseReference usersRef = ref.child("users");
        final String nm = edtnm.getText().toString();
        final String mob = edtmob.getText().toString();
        final String add = edtadd.getText().toString();
        final String add2 = edtadd2.getText().toString();
        final String qrcode = edtbarcode.getText().toString();
        Validation_text valid = new Validation_text();
        if (!valid.isValidName(nm)) {
            edtnm.setError("Invalid Name");
        } else if (!valid.isValidMobile(mob)) {
            edtmob.setError("Invalid Mobile Number");
        } else if (!valid.isValidName(add)) {
            edtadd.setError("Invalid Address");
        } else if (!valid.isValidName(add2)) {
            edtadd2.setError("Invalid Address");
        } else if (!valid.isValidName(qrcode)) {
            edtbarcode.setError("Invalid Qrcode");
        } else {
            //for data get using client
            Client client = new Client();
            client.setCustomer_name(nm);
            client.setMobile_number(mob);
            client.setAddress(add);
            client.setCustomer_qrcode(qrcode);
            Map<String, String> users = new HashMap<>();
            users.put("Customer_name", nm);
            users.put("Mobile_number", mob);
            users.put("Address", add);
            users.put("Local_address", add2);
            users.put("image", filedownloadpath);
            // users.put("upload",)
            // mDatabase.child(uploadId).setValue(upload);
            users.put("Customer_qrcode", qrcode);
            ref.child("Customer_data").child("+91" + mob.toString()).setValue(users);
            // ref.child()
            // ref.setValue(users);
            Toast.makeText(getApplicationContext(), "Customer added", Toast.LENGTH_SHORT).show();
            edtnm.setText("");
            edtadd2.setText("");
            edtadd.setText("");
            edtmob.setText("");
            edtbarcode.setText("");
            imgview.setImageResource(R.drawable.cus);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//image select result

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                datacall2();
                datacall();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                try {
                    JSONObject obj = new JSONObject(result.getContents());

                } catch (JSONException e) {

                    e.printStackTrace();
                    //get qr result data in toTest var
                    toTest = result.getContents();
                    if (toTest.equals("")) {

                    } else {
                        //qr code set text in edittext
                        edtbarcode.setText(toTest.toString());
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        //firebase add new Agent Accont
    }

    private void datacall2() {
        try {
            imgview.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "datacall2: " + e);
        }
    }

    private void datacall() {
        try {
            img_pro.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "datacall: " + e);
        }
    }

    public void logout() {
        //Logout From Current User
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), agent_login.class);
        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        startActivity(i);
        finish();
    }

    public void goback1(View view) {
        super.onBackPressed();
    }
}

