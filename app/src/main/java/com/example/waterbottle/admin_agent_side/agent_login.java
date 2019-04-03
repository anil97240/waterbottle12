package com.example.waterbottle.admin_agent_side;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.waterbottle.MainActivity;
import com.example.waterbottle.R;
import com.example.waterbottle.admin_agent_side.Model.UsersBean;
import com.example.waterbottle.admin_agent_side.Model.agent;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class agent_login extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;

    private static final String TAG = "Mo";
    //selected image into stream of byte
    EditText edtmail, edtpass;
    List<UsersBean> agentList;
    FirebaseAuth mAuth;
    FirebaseAuth auth;
    RelativeLayout rv;
    List<UsersBean> deliver_orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_agent_login);

        SharedPreferences prfs = getSharedPreferences("auth", MODE_PRIVATE);
        String authname = prfs.getString("authname", "");
        //admin login
        if (authname == "") {

        } else {
            Intent intent = new Intent(agent_login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        //agent login
        SharedPreferences prfs2 = getSharedPreferences("agent", MODE_PRIVATE);
        String agentname = prfs2.getString("agentname", "");

        if (agentname == "") {

        } else {
            Intent intent = new Intent(agent_login.this, agent_barcode.class);
            startActivity(intent);
            finish();
        }
        edtmail = findViewById(R.id.edtusername1);
        edtpass = findViewById(R.id.edtpass1);
        rv = findViewById(R.id.srv);
        //Get FirebetInstance();ase auth instance
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
      /*  try {
            if (auth != null) {
                FirebaseUser user = auth.getCurrentUser();
                String provider = user.getProviders().get(1);
                if (provider.contains("password")) {
                    Intent i = new Intent(getApplicationContext(), agent_barcode.class);
                    startActivity(i);
                    finish();
                }

                if (provider.contains("phone")) {
                    if (auth.getUid().contains("admin@gmail.com"))
                    {

                    } else {
                       /* Intent i = new Intent(getApplicationContext(), client_dashboard.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: "+e.getMessage() );

        }*/

    }

   /* public void cancel(View view) {
        edtpass.setText("");
        edtmail.setText("");
    }*/

    public void register(View view) {
        final String email = edtmail.getText().toString();
        final String password = edtpass.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        // String provider = user.getProviders().get(0);

        deliver_orderList = new ArrayList<>();


      /*  Firebase.setAndroidContext(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Agent_data");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        UsersBean usersBean = user.getValue(UsersBean.class);

                        if (usersBean.getAgent_email().equals(usersBean.getAgent_email().equals(edtmail.getText().toString().trim()))) {

                            if(usersBean.getAgent_password().equals(edtpass.getText().toString().trim())) {
                                Toast.makeText(getApplicationContext(), "Password is correct", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(agent_login.this, admin_dashboard.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password is wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        //authenticate user

       auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(agent_login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                edtpass.setError("Validate Password");
                            } else {
                                Snackbar.make(rv, "Re-check Id or Password", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null)
                                        .show();
                            }
                        } else {


                            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Agent_data");
                            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                        // dataSnapshot is the "issue" node with all children with id 0

                                        for (DataSnapshot user : dataSnapshot.getChildren()) {
                                            // do something with the individual "issues"
                                            UsersBean usersBean = user.getValue(UsersBean.class);

                                            if (usersBean.getAgent_email().equals(usersBean.getAgent_email().trim())) {

                                                if(usersBean.getAgent_password().equals(edtpass.getText().toString().trim())) {
                                                    if(usersBean.getType().equals("Admin"))
                                                    {
                                                        Intent intent = new Intent(agent_login.this, MainActivity.class);
                                                        SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.putString("authname", email);
                                                        editor.apply();
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        Intent intent = new Intent(agent_login.this, agent_barcode.class);
                                                        startActivity(intent);
                                                        SharedPreferences preferences = getSharedPreferences("agent", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.putString("agentname", email);
                                                        editor.apply();
                                                        finish();
                                                    }
                                                }
                                            } else {
                                               // Toast.makeText(getApplicationContext(), "Password is wrong", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



























/*


                            if (email == "admin@gmail.com" && password == "123456" ) {
                                Toast.makeText(agent_login.this, "Admin", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(agent_login.this, MainActivity.class);
                                SharedPreferences preferences = getSharedPreferences("auth", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("authname", email);
                                editor.apply();
                                startActivity(intent);
                                finish();
                            }
                            else
                                {
                                Toast.makeText(agent_login.this, "Agent", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(agent_login.this, agent_barcode.class);
                                startActivity(intent);
                                SharedPreferences preferences = getSharedPreferences("agent", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("agentname", email);
                                editor.apply();
                                finish();

                            }*/
                        }

                    }

                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

