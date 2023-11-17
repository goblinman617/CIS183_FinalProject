package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    Button btn_j_logIn;
    Intent gamePageIntent;
    DatabaseHelper db;
    ArrayList<User> listOfUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_j_logIn = findViewById(R.id.btn_v_logIn);

        //LogIn Button -> GamesPage
        gamePageIntent = new Intent(MainActivity.this, GamesPage.class);

        db = new DatabaseHelper(this);
        db.initializeTables();
        listOfUsers = db.getAllUserRows();

        logInEventHandler();
    }

    public void logInEventHandler()
    {
        btn_j_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(gamePageIntent);
            }
        });
    }
}