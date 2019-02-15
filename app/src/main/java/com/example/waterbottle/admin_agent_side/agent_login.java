package com.example.waterbottle.admin_agent_side;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.waterbottle.R;
import com.example.waterbottle.client_dashboard;
import com.example.waterbottle.client_login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class agent_login extends AppCompatActivity {

    private static final String TAG ="Mo" ;
    //selected image into stream of byte
    EditText edtmail, edtpass;

    private FirebaseAuth auth;
     RelativeLayout rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_agent_login);
        edtmail = findViewById(R.id.edtusername1);
        edtpass = findViewById(R.id.edtpass1);
       rv=findViewById(R.id.srv);
        //Get FirebetInstance();ase auth instance

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();


        try {
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
                        Toast.makeText(this, "haa", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent i = new Intent(getApplicationContext(), client_dashboard.class);
                        startActivity(i);
                        finish();
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "onCreate: "+e.getMessage() );

        }


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

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
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
                                Snackbar.make(rv, "Recheck Id or Password", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null)
                                        .show();

                            }
                        } else {
                            if (email.equals("admin@gmail.com") && password.equals("123456")) {
                                Intent intent = new Intent(agent_login.this, admin_dashboard.class);
                                intent.putExtra("id", email);
                                startActivity(intent);
                                finish();

                            } else {
                                Intent intent = new Intent(agent_login.this, agent_barcode.class);
                                startActivity(intent);

                                finish();
                            }

                        }
                    }
                });
    }

    public void godashboard(View view) {
        Intent intent = new Intent(getApplicationContext(), client_login.class);
        startActivity(intent);
        finish();
    }


}
