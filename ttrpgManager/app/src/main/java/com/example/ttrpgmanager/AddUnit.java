package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AddUnit extends AppCompatActivity {
    Button btn_j_back;
    Button btn_j_submit;
    User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);

        btn_j_back = findViewById(R.id.btn_au_back);
        btn_j_submit = findViewById(R.id.btn_au_submit);

        curUser = getCurrentUser();

        buttonEventHandler();
    }

    private void buttonEventHandler(){
        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // just goes back to playGame intent

                Intent playGame = new Intent(AddUnit.this, PlayGame.class);
                playGame.putExtra("User", curUser);
                startActivity(playGame);
            }
        });

        btn_j_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // adds to database
                // adds unit to the current game

                Intent playGame = new Intent(AddUnit.this, PlayGame.class);
                playGame.putExtra("User", curUser);
                startActivity(playGame);
            }
        });
    }

    private User getCurrentUser(){
        Intent cameFrom = getIntent();

        User curUser;
        curUser = (User) cameFrom.getSerializableExtra("User");

        if (curUser == null){
            Log.d("Error", "No current user received");
            return null;
        }
        return curUser;
    }
}