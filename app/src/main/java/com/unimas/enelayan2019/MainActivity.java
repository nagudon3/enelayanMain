package com.unimas.enelayan2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goLogin = (Button) findViewById(R.id.goToLoginBtn);
        Button goReg = (Button) findViewById(R.id.goToRegBtn);

        mAuth = FirebaseAuth.getInstance();

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                System.out.println("Login Button Clicked!"); //Test

                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        goReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                System.out.println("Register button clicked!"); //Test
                Intent regIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(regIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
