package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText et_j_username;
    EditText et_j_password;
    Button btn_j_register;
    Button btn_j_back;
    DatabaseHelper dbHelper;
    Intent main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_j_username = findViewById(R.id.et_r_username);
        et_j_password = findViewById(R.id.et_r_password);
        btn_j_register = findViewById(R.id.btn_r_register);
        btn_j_back = findViewById(R.id.btn_r_back);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initializeTables();

        main = new Intent(Register.this, MainActivity.class);

        buttonEventHandler();
    }

    public void buttonEventHandler()
    {
        btn_j_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(main);
            }
        });

        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(main);
            }
        });
    }
}