package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    Button btn_j_logIn;
    Button btn_j_register;
    Intent gamePageIntent;
    DatabaseHelper dbHelper;
    ArrayList<User> listOfUsers;
    EditText et_j_username;
    EditText et_j_password;
    TextView txt_j_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_j_logIn = findViewById(R.id.btn_v_logIn);
        btn_j_register = findViewById(R.id.btn_v_register);
        et_j_username = findViewById(R.id.et_v_username);
        et_j_password = findViewById(R.id.et_v_password);
        txt_j_error = findViewById(R.id.tv_errorMsg);

        //LogIn Button -> GamesPage
        gamePageIntent = new Intent(MainActivity.this, GamesPage.class);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initializeTables();
        listOfUsers = dbHelper.getAllUserRows();

        logInEventHandler();
        registerEventHandler();

        // Debug commands
        //debugFillLoginPage();
        //debugUserList();

    }

    public void logInEventHandler()
    {
        btn_j_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = et_j_username.getText().toString();
                String pword = et_j_password.getText().toString();

                User curUser = new User(uname,pword);

                if (dbHelper.validateLogin(curUser)) {
                    txt_j_error.setVisibility(View.INVISIBLE);
                    gamePageIntent.putExtra("User", curUser);

                    Log.d("Login", curUser.getUsername() + " has logged in");
                    startActivity(gamePageIntent);
                }else{
                    // Display that the login information was wrong
                    txt_j_error.setVisibility(View.VISIBLE);
                    Log.d("Login", "Incorrect login information");
                }
            }
        });
    }

    public void registerEventHandler()
    {
        btn_j_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(MainActivity.this, Register.class);
                startActivity(register);
            }
        });
    }

    private void debugUserList(){
        for (int i = 0; i < listOfUsers.size(); i++){
            Log.d("User at " + i, listOfUsers.get(i).getUsername() + " " + listOfUsers.get(i).getPassword());
        }
    }

    private void debugFillLoginPage(){
        et_j_username.setText("DMleo");
        et_j_password.setText("pass123");
    }
}