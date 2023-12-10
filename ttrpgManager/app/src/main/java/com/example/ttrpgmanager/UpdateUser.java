package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateUser extends AppCompatActivity {
    EditText et_j_username;
    EditText et_j_password;
    TextView tv_j_error;
    TextView tv_j_updateMsg;
    Button btn_j_back;
    Button btn_j_update;
    Button btn_j_delete;
    DatabaseHelper dbHelper;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        et_j_username = findViewById(R.id.et_udm_username);
        et_j_password = findViewById(R.id.et_udm_password);
        tv_j_error = findViewById(R.id.tv_udm_errorMsg);
        tv_j_updateMsg = findViewById(R.id.tv_udm_updateMsg);
        btn_j_back = findViewById(R.id.btn_udm_back);
        btn_j_update = findViewById(R.id.btn_udm_update);
        btn_j_delete = findViewById(R.id.btn_udm_delete);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initializeTables();

        Intent cameFrom = getIntent();
        user = (User) cameFrom.getSerializableExtra("User");

        et_j_username.setText(user.getUsername());

        // I have it so they cannot change their username right now.
        // so I have it not let them edit their username
        et_j_username.setEnabled(false);

        buttonEventHandler();
    }

    public void buttonEventHandler()
    {
        btn_j_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = et_j_username.getText().toString();
                String pword = et_j_password.getText().toString();

                user = new User(uname,pword);

                if(et_j_username.getText().toString().equals("") || et_j_password.getText().toString().equals(""))
                {
                    tv_j_error.setVisibility(View.VISIBLE);
                }
                else {
                    tv_j_error.setVisibility(View.INVISIBLE);
                    dbHelper.updateUser(user);
                    tv_j_updateMsg.setVisibility(View.VISIBLE);

                    et_j_password.setText("");
                }
            }
        });

        btn_j_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = et_j_username.getText().toString();
                String pword = et_j_password.getText().toString();

                user = new User(uname,pword);

                if (dbHelper.validateLogin(user)) {
                    tv_j_error.setVisibility(View.INVISIBLE);
                    dbHelper.deleteUser(user);

                    Intent main = new Intent(UpdateUser.this, MainActivity.class);
                    startActivity(main);
                }else{
                    tv_j_error.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(UpdateUser.this, GamesPage.class);
                main.putExtra("User", user);
                startActivity(main);
            }
        });
    }
}