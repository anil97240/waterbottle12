package com.example.waterbottle.admin_agent_side;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.waterbottle.client_side.client_model.Client;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
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

public class agent_barcode extends AppCompatActivity implements View.OnClickListener {

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "Customer_data";
    public static final String DATABASE_PATH_UPLOADS1 = "Product_data";
    private static final String TAG = "Mohit";
    private static final int PICK_IMAGE_REQUEST = 234, PICK_IMAGE_REQUEST1 = 234;
    String[] uploads;
    ArrayList arrayList = new ArrayList<String>();
    ArrayList arrbaarcode = new ArrayList<String>();
    List<Client> clientList;
    ListView listView;
    String filedownloadpath;
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    //search
    //qr Scanner
    EditText edtbarcode, edtemail, edtpass;
    TextView tvhide, tvloguot;
    Bitmap bitmap, bitmap2;
    ImageView imgview;
    ImageView img_pro;
    String toTest;
    EditText inputSearch;
    ArrayList arrayList1 = new ArrayList<String>();
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private IntentIntegrator qrScan;
    private DatabaseReference mDatabase;
    private Firebase mRef;
    private StorageReference storageRef;
    private DatabaseReference mDatabaseReference;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private Uri filePath;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_agent_barcode);
        Firebase.setAndroidContext(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        tvloguot = findViewById(R.id.tvhidelogout);
        tvhide = findViewById(R.id.tvhide);


        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);







        Firebase.setAndroidContext(this);

        FirebaseApp.initializeApp(this);

        clientList = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Customer_data");
        //retrieving upload data from firebase database

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Client client = postSnapshot.getValue(Client.class);
                        clientList.add(client);

                    }

                    String[] uploads = new String[clientList.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i]=clientList.get(i).getCustomer_qrcode();
                        arrayList1.add(uploads[i]);

                    }

                } catch (Exception e) {

                }

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
                Log.d("Raj", "Fab 1");
               /* Intent i1 = new Intent(this, agent_add_bottle.class);
                startActivity(i1);*/
                showCustomDialog();
                break;

            case R.id.fab2:
                //Logout From cURRENT User

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fAuth.signOut();
                if (fAuth != null) {
                    Intent i = new Intent(getApplicationContext(), agent_login.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, "Cant Logout", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.fab3:
                Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void animateFAB() {
        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            tvhide.startAnimation(fab_close);
            tvloguot.startAnimation(fab_close);

            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;

            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            tvhide.startAnimation(fab_open);
            tvloguot.startAnimation(fab_open);


            fab1.setClickable(true);
            fab2.setClickable(true);


            isFabOpen = true;
            Log.d("Raj", "open");

        }
    }

    public void goback(View view) {
        finish();
    }

    public void btnscanner(View view) {
        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
    }

    public void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        View view = getLayoutInflater().inflate(R.layout.agent_add_client, null);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userName = extras.getString("qrcode");
            // and get whatever type user account id is
            // edtbarcode.setText(userName.toString());
        }


        view.findViewById(R.id.btnscannbarcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan = new IntentIntegrator(agent_barcode.this);
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
                users.put("Customer_qrcode", qrcode);
                ref.child("Customer_data").push().setValue(users);

                // ref.child()

                //   ref.setValue(users);
                Toast.makeText(getApplicationContext(), "Customer added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });


    }


    //image chooser
    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //upload image extension
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //for upload images
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

                            //   Client upload = new Client(cn.getCustomer_name(),cn.getMobile_number(),cn.getAddress(),taskSnapshot.getDownloadUrl().toString());
                            try {
                                Client cn = new Client();
                                filedownloadpath = taskSnapshot.getDownloadUrl().toString();
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
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Cant read Code", Toast.LENGTH_SHORT).show();
                    } else {


                      //  edtbarcode.setText(toTest);


                    }

                    String toTest = result.getContents();
                    Toast.makeText(this, "qrcode:"+arrayList1, Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(this, "data:"+datarr, Toast.LENGTH_SHORT).show();
                    if (arrayList1.contains(toTest)) {
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

        //firebase add new Agent Accont


    }
}

