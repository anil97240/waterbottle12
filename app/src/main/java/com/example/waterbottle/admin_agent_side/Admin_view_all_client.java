package com.example.waterbottle.admin_agent_side;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.text.Html;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.example.waterbottle.client_side.client_model.Client;
import com.example.waterbottle.client_side.client_model.clientlistAdepter;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Admin_view_all_client extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MOHIT";
    private static final int PICK_IMAGE_REQUEST = 545;
    List<Client> clientList;
    int p = 0;
    String filedownloadpath;
    //the listview
    ListView listView;
    ArrayList arrayList = new ArrayList<String>();
    String url = "https://waterbottle12-e6aa9.firebaseio.com/";
    private StorageReference storageReference;
    private Uri filePath;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private TextView tvhide,tvagenthide,tvproducthide,tvlogouthide;
    private DatabaseReference mDatabaseReference;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_admin_view_all_client);


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();//displays the progress bar

        FirebaseApp.initializeApp(this);

        clientList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.Adminviewallclient);

        //add Client list in listview
        tvhide=findViewById(R.id.tvhide);
        tvagenthide=findViewById(R.id.tvagenthide);
        tvproducthide=findViewById(R.id.tvhideproduct);
        tvlogouthide=findViewById(R.id.tvhidelogout);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Customer_data");
        //retrieving upload data from firebase database

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Client client = postSnapshot.getValue(Client.class);
                        clientList.add(client);
                        arrayList.add(postSnapshot.getKey());
                    }

                    String[] uploads = new String[clientList.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = clientList.get(i).getMobile_number();
                        Log.e(TAG, "onDataChange: " + uploads[i]);
                    }
                    progressDialog.dismiss();
                    //displaying it to list

                    final clientlistAdepter adapter = new clientlistAdepter(getApplicationContext(), R.layout.my_custom_listview_customer, clientList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                            p = position;


                            final AlertDialog.Builder builder = new AlertDialog.Builder(Admin_view_all_client.this);
                            builder.setMessage("Are You Sure ?");

                            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Delete</font>YES</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mDatabaseReference.child(String.valueOf(arrayList.get(p))).removeValue();
                                    Toast.makeText(Admin_view_all_client.this, "" + arrayList.get(position), Toast.LENGTH_SHORT).show();
                                    //  Toast.makeText(Admin_view_all_client.this, "data remove", Toast.LENGTH_SHORT).show();
                                    clientList.remove(p);
                                    adapter.notifyDataSetChanged();

                                    //do things

                                }
                            });

                            builder.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>EDIT</font>YES</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    upadteAgentDialog();
                                    //do things
                                }
                            });

                            builder.setNeutralButton("EDIT", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

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

            }
        });


        clientlistAdepter adapter = new clientlistAdepter(this, R.layout.my_custom_listview_customer, clientList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Client c = clientList.get(position);

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
                Log.d("Raj", "Fab 1");
                showCustomDialog();

                break;

            case R.id.fab2:
                //Toast.makeText(this, "log1", Toast.LENGTH_SHORT).show();
                Log.d("a", "Fab 2");
                showAgentDialog();
                break;

            case R.id.fab3:

                Log.d("a", "Fab 3");
                showProductDialog();
                break;
            case R.id.fab4:
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

        }
    }
//for product add dialong method

    private void showProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_view_all_client.this);

        View view = getLayoutInflater().inflate(R.layout.dialong_product_add, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();

        final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtprice = view.findViewById(R.id.edtprice);
        final EditText edtdetail = view.findViewById(R.id.edtdetail);


        view.findViewById(R.id.btnallproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all Client display activity
                //display all product page

                Intent i = new Intent(getApplicationContext(), admin_view_product.class);
                startActivity(i);

            }
        });

        view.findViewById(R.id.btnaddproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Firebase ref;

                ref = new Firebase(url);

                //  Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show();
                // DatabaseReference usersRef = ref.child("users");

                String pnm = edtnm.getText().toString();
                String price = edtprice.getText().toString();
                String detail = edtdetail.getText().toString();


                Map<String, String> users = new HashMap<>();
                users.put("Product_name", pnm);
                users.put("Product_Price", price);
                users.put("Product_detail", detail);
                ref.child("Product_data").push().setValue(users);

                // ref.child()

                //   ref.setValue(users);
                Toast.makeText(getApplicationContext(), "Product add", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });


    }

    //Agent Dialoge BOX For INsert An record
    private void showAgentDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_view_all_client.this);

        View view = getLayoutInflater().inflate(R.layout.dialong_agent_add, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.Theme_Design_BottomSheetDialog); // Style here
        dialog.setContentView(view);
        dialog.show();
        view.findViewById(R.id.btnallagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all Client display activity
                Intent i = new Intent(getApplicationContext(), admin_dashboard.class);
                startActivity(i);
            }
        });
        view.findViewById(R.id.btnaddagent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        final EditText edtadd = view.findViewById(R.id.edtname);
        final EditText edtadd2 = view.findViewById(R.id.edtaddresstwo);
        final EditText edtbarcode = view.findViewById(R.id.edtbarcode);
        final ImageView imgview = view.findViewById(R.id.imgview);
        final Button btn = view.findViewById(R.id.btnuploadimg);


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
                showFileChooser();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });


        view.findViewById(R.id.btnaddclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nm = edtnm.getText().toString();
                String mob = edtmob.getText().toString();
                String add = edtadd.getText().toString();
                String add2 = edtadd2.getText().toString();
                String qrcode = edtbarcode.getText().toString();
                Map<String, String> users = new HashMap<>();
                users.put("Customer_name", nm);
                users.put("Mobile_number", mob);
                users.put("Address ", add);
                users.put("Local_address ", add2);
                users.put("Customer_qrcode", qrcode);
                users.put("image", filedownloadpath);

                mDatabaseReference.child(String.valueOf(arrayList.get(p))).setValue(users);
                Toast.makeText(getApplicationContext(), "Client Updated.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
        final EditText edtnm = view.findViewById(R.id.edtnm);
        final EditText edtmob = view.findViewById(R.id.edtmob);
        final EditText edtadd = view.findViewById(R.id.edtname);
        final EditText edtadd2 = view.findViewById(R.id.edtaddresstwo);
        final EditText edtbarcode = view.findViewById(R.id.edtbarcode);
        final ImageView imgview=view.findViewById(R.id.imgview);



        view.findViewById(R.id.btnallclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.findViewById(R.id.btnscannbarcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        view.findViewById(R.id.btnuploadimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        view.findViewById(R.id.btnaddclient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Firebase ref;

                ref = new Firebase(url);

                //  Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show();
                // DatabaseReference usersRef = ref.child("users");
                String nm = edtnm.getText().toString();
                String mob = edtmob.getText().toString();
                String add = edtadd.getText().toString();
                String add2 = edtadd2.getText().toString();
                String qrcode = edtbarcode.getText().toString();


                Map<String, String> users = new HashMap<>();
                users.put("Customer_name", nm);
                users.put("Mobile_number", mob);
                users.put("Address ", add);
                users.put("Local_address ", add2);
                users.put("Customer_qrcode", qrcode);
                ref.child("Customer_data").push().setValue(users);

                // ref.child()

                //   ref.setValue(users);
                Toast.makeText(getApplicationContext(), "Customer added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
            Log.d("Raj", "open");

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
                Bitmap bitmap, bitmap2;
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
