package com.example.waterbottle.admin_agent_side;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.Model.MyListAdapter;
import com.example.waterbottle.admin_agent_side.Model.agent;
import com.example.waterbottle.client_side.client_model.Client;
import com.firebase.client.Firebase;
import com.firebase.client.authentication.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class admin_dashboard extends AppCompatActivity implements View.OnClickListener {

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private TextView tvhide,tvagenthide,tvproducthide,tvlogouthide;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    private Firebase mRef;
    private StorageReference storageRef;


    private FirebaseAuth mAuth;



    List<agent> agentList;
    MyListAdapter adapter;
    //the listview
    ListView listView;
    String filedownloadpath;
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    //search
    //qr Scanner
    private IntentIntegrator qrScan;
    EditText edtbarcode,edtemail,edtpass;
    private Uri filePath;

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "Customer_data";
    public static final String DATABASE_PATH_UPLOADS1 = "Product_data";

    private static final int PICK_IMAGE_REQUEST = 234, PICK_IMAGE_REQUEST1 = 234;
    Bitmap bitmap, bitmap2;
    ImageView imgview;
    ImageView img_pro;
    String toTest;
    EditText inputSearch;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();



        agentList = new ArrayList<>();

        Firebase.setAndroidContext(this);


        listView = (ListView) findViewById(R.id.listView);
        //listview add values using arraaylist
        agentList.add(new agent("abc", "5", "500"));
        agentList.add(new agent("mohit", "1", "200"));
        agentList.add(new agent("anil", "2", "100"));
        agentList.add(new agent("ajaz", "3", "10"));
        agentList.add(new agent("sagar", "5", "20"));
        agentList.add(new agent("abc", "5", "500"));
        agentList.add(new agent("abc", "5", "500"));


        inputSearch = findViewById(R.id.inputSearch);


        //initializing objects
        tvhide=findViewById(R.id.tvhide);
        tvagenthide=findViewById(R.id.tvagenthide);
        tvproducthide=findViewById(R.id.tvhideproduct);
        tvlogouthide=findViewById(R.id.tvhidelogout);

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
                //admin_dashboard.this.adapter.getFilter().filter(s);


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // listView.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {
                // listView.setAdapter(adapter);
                //String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                admin_dashboard.this.adapter.getFilter().filter(s.toString());

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

                Log.d("Raj", "Fab 1");

                showCustomDialog();
                break;
            case R.id.fab2:
                Log.d("Raj", "Fab 2");
                     showAgentDialog();
                break;

            case R.id.fab3:
                Log.d("Raj", "Fab 3");
                //     Toast.makeText(this, "fab 3", Toast.LENGTH_SHORT).show();
                showProductDialog();
                break;

            case R.id.fab4:


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

    public void showProductDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        View view = getLayoutInflater().inflate(R.layout.dialong_product_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();


        //get all edittext in dialog_customer_add
        final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtprice = view.findViewById(R.id.edtprice);
        final EditText edtdetail = view.findViewById(R.id.edtdetail);
        img_pro = view.findViewById(R.id.pro_image);
        final Button uploadproimage = view.findViewById(R.id.btnuploadimage);


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

        img_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser1();


            }
        });
        uploadproimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile1();
                img_pro.setImageBitmap(bitmap2);
            }
        });
        view.findViewById(R.id.btnaddproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Firebase ref;
                ref = new Firebase(url);

                String pnm = edtnm.getText().toString();
                String price = edtprice.getText().toString();
                String detail = edtdetail.getText().toString();
                //insert product in firebase
                Map<String, String> users = new HashMap<>();
                users.put("Product_name", pnm);
                users.put("Product_Price", price);
                users.put("Product_detail", detail);
                users.put("image", filedownloadpath);
                ref.child("Product_data").push().setValue(users);


                Toast.makeText(getApplicationContext(), "Product add", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

    }

    public void showAgentDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        View view = getLayoutInflater().inflate(R.layout.dialong_agent_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();

       edtemail=view.findViewById(R.id.edtemail);
         edtpass=view.findViewById(R.id.edtpass);
        final EditText edtname=view.findViewById(R.id.edtname);
        final EditText edtmob=view.findViewById(R.id.edtmob);





        view.findViewById(R.id.btnallagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        view.findViewById(R.id.btnaddagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                Firebase ref;
                ref = new Firebase(url);


                String email=edtemail.getText().toString();
                String pass=edtpass.getText().toString();
                String name=edtname.getText().toString();
                String mob=edtmob.getText().toString();

                //insert Agent in firebase
                Map<String, String> users = new HashMap<>();
                users.put("Agent_email",email);
                users.put("Agent_password", pass);
                users.put("Agent_name", name);
                users.put("Agent_mobile", mob);
                ref.child("Agent_data").push().setValue(users);

                Toast.makeText(getApplicationContext(), "Agent add", Toast.LENGTH_SHORT).show();
                registerUser();
                dialog.dismiss();

            }
        });
    }

    private void registerUser() {

        String email = edtemail.getText().toString();
        String password  = edtpass.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }



        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        } else {

                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

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
        final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        final EditText edtadd = view.findViewById(R.id.edtname);
        final EditText edtadd2 = view.findViewById(R.id.edtaddresstwo);
        edtbarcode = view.findViewById(R.id.edtbarcode);
        final Button btnadd = view.findViewById(R.id.btnaddclient);
        imgview = view.findViewById(R.id.imgview);
        final  Button btnuploadimg=view.findViewById(R.id.btnuploadimg);

    //image chooser
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        //image upload
        btnuploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
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
        view.findViewById(R.id.imgview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();



            }
        });
        view.findViewById(R.id.btnuploadimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();
                imgview.setImageBitmap(bitmap);
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //  Toast.makeText(admin_dashboard.this, ""+edtnm.getText().toString(), Toast.LENGTH_SHORT).show();
                Firebase ref;
                ref = new Firebase(url);

                //  Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show();
                // DatabaseReference usersRef = ref.child("users");

                String nm = edtnm.getText().toString();
                String mob = edtmob.getText().toString();
                String add = edtadd.getText().toString();
                String add2 = edtadd2.getText().toString();
                String qrcode = edtbarcode.getText().toString();

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
                //  mDatabase.child(uploadId).setValue(upload);
                users.put("Customer_qrcode", qrcode);
                ref.child("Customer_data").push().setValue(users);

                // ref.child()

                //   ref.setValue(users);
                Toast.makeText(getApplicationContext(), "Customer added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });


    }


    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showFileChooser1() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
    }


    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void animateFAB() {

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

                            //creating the upload object to store uploaded image details
                            Client cn = new Client();
                            //   Client upload = new Client(cn.getCustomer_name(),cn.getMobile_number(),cn.getAddress(),taskSnapshot.getDownloadUrl().toString());

                            filedownloadpath = taskSnapshot.getDownloadUrl().toString();

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
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

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

                    toTest = result.getContents();
                    if (toTest.equals("")) {

                    } else {
                        edtbarcode.setText(toTest.toString());
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        //firebase add new Agent Accont



    }
}

