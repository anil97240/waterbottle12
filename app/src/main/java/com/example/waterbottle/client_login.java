package com.example.waterbottle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waterbottle.admin_agent_side.admin_dashboard;
import com.example.waterbottle.admin_agent_side.agent_barcode;
import com.example.waterbottle.admin_agent_side.agent_login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class client_login extends AppCompatActivity {

    private static final String TAG = "Mohit";
    //user singin button
    Button btSignIn;
    TextView tv;
    //user mobile number
    private EditText editTextMobile;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_client_login);
        mAuth = FirebaseAuth.getInstance();


        try {
            if (mAuth != null) {
                FirebaseUser user = mAuth.getCurrentUser();
                String provider = user.getProviders().get(0);
                if (provider.contains("password")) {
                    Intent i=new Intent(getApplicationContext(), agent_barcode.class);
                    startActivity(i);
                    finish();
                }
                if (provider.contains("phone"))
                {
                    if (provider.contains("phone")) {
                        if (mAuth.getCurrentUser().getEmail() == "admin@gmail.com") {
                            Intent i = new Intent(getApplicationContext(), admin_dashboard.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(getApplicationContext(), client_dashboard.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "onCreate: "+e.getMessage() );

        }










        btSignIn = findViewById(R.id.btnlogin);
        editTextMobile = findViewById(R.id.edtmob);
        tv = findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), agent_login.class);
                startActivity(i);
                finish();
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = editTextMobile.getText().toString().trim();
                if (mobile.isEmpty() || mobile.length() < 10) {
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(client_login.this, client_mobile_verification.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
                finish();
            }
        });
    }


}
