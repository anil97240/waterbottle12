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
import android.text.Html;
import android.text.TextUtils;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.loaderspack.loaders.CurvesLoader;
import com.example.waterbottle.R;
import com.example.waterbottle.client_side.client_model.Client;
import com.example.waterbottle.client_side.client_model.clientlistAdepter;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Admin_view_all_client extends AppCompatActivity implements View.OnClickListener {


    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS1 = "Product_data";
    private static final String TAG = "Mohit";
    private static final int PICK_IMAGE_REQUEST = 234, PICK_IMAGE_REQUEST1 = 234;
    Bitmap bitmap;
    EditText edtnm;
    EditText edtmob;
    EditText edtadd;
    EditText edtadd2;
   EditText edtbarcode;
   ImageView imgview;
    Client client;
    EditText edtpnm;
    EditText edtprice;
    EditText edtdetail;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    List<Client> clientList;
    int p = 0;
    Spinner spd;
    String filedownloadpath;
    CurvesLoader curvesLoader;
    EditText edtemail, edtpass;
    Bitmap bitmap2;
    ImageView img_pro;
    Client c;
    StorageReference photoRef;
    //the listview
    clientlistAdepter adapter;
    ListView listView;
    ArrayList arrayList = new ArrayList<String>();
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    private StorageReference storageReference;
    private Uri filePath;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView tvhide, tvagenthide, tvproducthide, tvlogouthide;
    private DatabaseReference mDatabaseReference;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_admin_view_all_client);

        curvesLoader = findViewById(R.id.curvesLoader);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        clientList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.Adminviewallclient);

        //add Client list in listview
        tvhide = findViewById(R.id.tvhide);
        tvagenthide = findViewById(R.id.tvagenthide);
        tvproducthide = findViewById(R.id.tvhideproduct);
        tvlogouthide = findViewById(R.id.tvhidelogout);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Customer_data");
        //retrieving upload data from firebase database

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        client = postSnapshot.getValue(Client.class);
                        clientList.add(client);
                        arrayList.add(postSnapshot.getKey());
                    }

                    String[] uploads = new String[clientList.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = clientList.get(i).getMobile_number();
                        Log.e(TAG, "onDataChange: " + uploads[i]);
                    }


                    curvesLoader.setVisibility(View.GONE);
                    //displaying it to list

                    adapter = new clientlistAdepter(getApplicationContext(), R.layout.my_custom_listview_customer, clientList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                            p = position;

                            final AlertDialog.Builder builder = new AlertDialog.Builder(Admin_view_all_client.this, R.style.Theme_AppCompat_DayNight_Dialog);
                            builder.setMessage("Are You Sure ?");

                            builder.setPositiveButton(Html.fromHtml("<font color='#FF0000'>Delete</font></font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mDatabaseReference.child(String.valueOf(arrayList.get(p))).removeValue();
                                    //  Toast.makeText(Admin_view_all_client.this, "data remove", Toast.LENGTH_SHORT).show();
                                    clientList.remove(p);
                                    clientList.clear();
                                    arrayList.clear();
                                    adapter.notifyDataSetChanged();
                                    //do things
                                    //for image delete in firebase

                                }
                            });
                            builder.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>EDIT</font></font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    upadteAgentDialog();
                                    //do things
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "onDataChange: " + e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Admin_view_all_client.this, "" + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        clientlistAdepter adapter = new clientlistAdepter(this, R.layout.my_custom_listview_customer, clientList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                c = clientList.get(position);

            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Firebase.setAndroidContext(this);
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();

                break;
            case R.id.fab1:

                showCustomDialog();
                break;

            case R.id.fab2:
                //Toast.makeText(this, "log1", Toast.LENGTH_SHORT).show();

                showAgentDialog();
                break;

            case R.id.fab3:

                showProductDialog();
                break;

            case R.id.fab4:
                //Logout From cURRENT User
                Intent i = new Intent(getApplicationContext(), agent_login.class);
                SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                startActivity(i);
                finish();
                break;
        }
    }
//for product add dialong method

    public void showProductDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        View view = getLayoutInflater().inflate(R.layout.dialong_product_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();

        //get all edittext in dialog_customer_add
       edtpnm = view.findViewById(R.id.edtnm);
         edtprice = view.findViewById(R.id.edtprice);
         edtdetail = view.findViewById(R.id.edtdetail);
        img_pro = view.findViewById(R.id.imgview);
         FloatingActionButton uploadproimage = view.findViewById(R.id.btnuploadimg);
        edtpnm.requestFocus();

        view.findViewById(R.id.btnallproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all client display activity
                //display all product page
                Intent i = new Intent(getApplicationContext(), admin_view_product.class);
                startActivity(i);
                dialog.dismiss();
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

                            //creating the upload object to store uploaded image details
                            Client cn = new Client();
                            //   Client upload = new Client(cn.getCustomer_name(),cn.getMobile_number(),cn.getAddress(),taskSnapshot.getDownloadUrl().toString());

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
            //display an error if no file is selected
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();

        }
    }
//add product
    private void addproductdata() {

        Firebase ref;

        ref = new Firebase(url);

        String pnm = edtpnm.getText().toString();
        String price = edtprice.getText().toString();
        String detail = edtdetail.getText().toString();
        Validation_text valid = new Validation_text();

        edtnm.requestFocus();

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
            users.put("image",filedownloadpath);
            ref.child("Product_data").push().setValue(users);

            Toast.makeText(getApplicationContext(), "Product add", Toast.LENGTH_SHORT).show();


        }
    }
    private void showFileChooser1() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);


    }

    //Agent Dialoge BOX For Insert An record
    public void showAgentDialog() {
        final String[] type = {"Agent"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        View view = getLayoutInflater().inflate(R.layout.dialong_agent_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();

        edtemail = view.findViewById(R.id.edtemail);
        edtpass = view.findViewById(R.id.edtpass);
        final EditText edtname = view.findViewById(R.id.edtname);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        spd = view.findViewById(R.id.spinnerid);
        edtname.requestFocus();
        spd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type[0] = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        view.findViewById(R.id.btnallagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admin_view_all_client.this, admin_dashboard.class);
                startActivity(i);
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
                    Map<String, String> users = new HashMap<>();
                    users.put("Agent_email", email);
                    users.put("Agent_password", pass);
                    users.put("Agent_name", name);
                    users.put("Agent_mobile", mob);
                    users.put("Type", type[0]);
                    ref.child("Agent_data").child("+91" + mob.toString()).setValue(users);
                    Toast.makeText(getApplicationContext(), "Agent add", Toast.LENGTH_SHORT).show();
                    registerUser();
                    dialog.dismiss();
                }
            }
        });
    }

    private void registerUser() {
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

    ///Update an Selected REcord From List View Agent Dialoge
    private void upadteAgentDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_view_all_client.this);
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
       imgview = view.findViewById(R.id.imgview);
        FloatingActionButton btn = view.findViewById(R.id.btnuploadimg);

        edtnm.requestFocus();
        c = clientList.get(p);
        edtnm.setText(c.getCustomer_name());
        edtmob.setText(c.getMobile_number());
        edtadd.setText(c.getLocal_address());
        edtadd2.setText(c.getAddress());
        edtbarcode.setText(c.getCustomer_qrcode());

        try {
            Picasso.with(getApplicationContext())
                    .load(c.getImage())
                    .into(imgview);
        }
        catch (Exception e)
        {

        }

        view.findViewById(R.id.btnallclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all Client display activity
             /*   Intent i = new Intent(getApplicationContext(), Admin_view_all_client.class);
                startActivity(i);*/
            }
        });

        view.findViewById(R.id.btnscannbarcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add barcode scann code herer
                //Toast.makeText(Admin_view_all_client.this, "barcode scanner", Toast.LENGTH_SHORT).show();
            }
        });
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });
        view.findViewById(R.id.btnaddclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();

              /*  String nm = edtnm.getText().toString();
                String mob = edtmob.getText().toString();
                String add = edtadd.getText().toString();
                String add2 = edtadd2.getText().toString();
                String qrcode = edtbarcode.getText().toString();

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


                    Map<String, String> users = new HashMap<>();
                    users.put("Customer_name", nm);
                    users.put("Mobile_number", mob);
                    users.put("Address", add);
                    users.put("Local_address", add2);
                    users.put("Customer_qrcode", qrcode);
                    if (filedownloadpath == null) {
                        users.put("image", client.getImage());
                    } else {
                        users.put("image", filedownloadpath);
                    }
                    mDatabaseReference.child(String.valueOf(arrayList.get(p))).setValue(users);
                    Toast.makeText(getApplicationContext(), "Client Updated.", Toast.LENGTH_SHORT).show();
                    clientList.clear();
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();*/
                //}
            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_view_all_client.this);
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
        imgview = view.findViewById(R.id.imgview);
        edtnm.requestFocus();

        view.findViewById(R.id.btnallclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.btnscannbarcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.findViewById(R.id.btnuploadimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        view.findViewById(R.id.btnaddclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });


    }

    private void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);

            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);

            tvhide.startAnimation(fab_close);
            tvagenthide.startAnimation(fab_close);
            tvproducthide.startAnimation(fab_close);
            tvlogouthide.startAnimation(fab_close);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {
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

        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        final String STORAGE_PATH_UPLOADS = "uploads/";
        final String DATABASE_PATH_UPLOADS = "Customer_data";
        DatabaseReference mDatabase;
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
                                if(filedownloadpath=="")
                                {
                                    Toast.makeText(getApplicationContext(), "No image Selected", Toast.LENGTH_SHORT).show();
                                }
                                else {
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
            try {
                users.put("image",c.getImage());
            }
            catch (Exception e)
            {
                //Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
                users.put("image", "");
            }
            // users.put("upload",)
            // mDatabase.child(uploadId).setValue(upload);
            users.put("Customer_qrcode", qrcode);
            ref.child("Customer_data").child("+91" + mob.toString()).setValue(users);
            // ref.child()
            //   ref.setValue(users);
            Toast.makeText(getApplicationContext(), "Customer added", Toast.LENGTH_SHORT).show();
            clientList.clear();
            arrayList.clear();
            adapter.notifyDataSetChanged();
        }
    }

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
            users.put("Local_address",add2);

            users.put("image", filedownloadpath);

            // users.put("upload",)
            // mDatabase.child(uploadId).setValue(upload);
            users.put("Customer_qrcode", qrcode);
            ref.child("Customer_data").child("+91" + mob.toString()).setValue(users);

            // ref.child()
            //   ref.setValue(users);

            Toast.makeText(getApplicationContext(), "Customer added", Toast.LENGTH_SHORT).show();
            clientList.clear();
            arrayList.clear();
            adapter.notifyDataSetChanged();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                datacall();
                datacall2();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void datacall2() {
        try {
            imgview.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            Log.e(TAG, "datacall2: "+e);
        }
        }

    private void datacall() {
        try {
            img_pro.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "datacall: " + e);
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public void goback(View view) {
        super.onBackPressed();
    }
}