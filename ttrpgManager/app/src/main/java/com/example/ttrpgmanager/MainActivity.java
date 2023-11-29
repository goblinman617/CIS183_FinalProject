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

        //debugUserList();
    }

    public void logInEventHandler()
    {
        btn_j_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Currently this passes the user we created from db.initializeTables()
                gamePageIntent.putExtra("User", listOfUsers.get(0));

                startActivity(gamePageIntent);
            }
        });
    }

    private void debugUserList(){
        for (int i = 0; i < db.rowsInUsersTable(); i++){
            Log.d("User at " + i, listOfUsers.get(i).getUsername());
            Log.d("User at " + i, listOfUsers.get(i).getPassword());
        }
    }


}