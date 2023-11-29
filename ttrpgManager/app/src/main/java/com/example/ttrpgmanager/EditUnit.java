package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class EditUnit extends AppCompatActivity {
    Button btn_j_submit;
    User curUser;
    Game curGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_unit);

        btn_j_submit = findViewById(R.id.btn_eu_submit);

        curUser = getCurrentUser();

        // I think we will receive a 'User' and a 'Game' to pass back

        submitEventHandler();
    }

    private void submitEventHandler(){
        btn_j_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update the units stats in the database
                // go back to the PlayGame screen
                // pass the 'User' and the 'Game' back probably

                Intent playGame = new Intent(EditUnit.this, PlayGame.class);
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