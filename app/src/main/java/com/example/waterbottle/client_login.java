package com.example.waterbottle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.waterbottle.admin_agent_side.agent_login;

public class client_login extends AppCompatActivity {

    //user singin button
    Button btSignIn;
    //user mobile number
    private EditText editTextMobile;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_client_login);

        btSignIn = findViewById(R.id.btnlogin);
        editTextMobile = findViewById(R.id.edtmob);
        tv=findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), agent_login.class);
                startActivity(i);
                finish();
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mobile = editTextMobile.getText().toString().trim();
            if(mobile.isEmpty() || mobile.length() < 10){
                editTextMobile.setError("Enter a valid mobile");
                editTextMobile.requestFocus();
                return;
            }

            Intent intent = new Intent(client_login.this, client_mobile_verification.class);
            intent.putExtra("mobile", mobile);
            startActivity(intent);
        }
    });
    }


}
