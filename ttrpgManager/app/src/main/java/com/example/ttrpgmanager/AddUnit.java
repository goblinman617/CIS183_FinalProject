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
    Game game;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);

        dbHelper = new DatabaseHelper(this);

        btn_j_back = findViewById(R.id.btn_au_back);
        btn_j_submit = findViewById(R.id.btn_au_submit);

        buildCurrentGame();

        buttonEventHandler();
    }

    private void buttonEventHandler(){
        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // just goes back to playGame intent

                Intent playGame = new Intent(AddUnit.this, PlayGame.class);
                playGame.putExtra("Game", game);
                startActivity(playGame);
            }
        });

        btn_j_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Needs to add unit to unit table

                Intent playGame = new Intent(AddUnit.this, PlayGame.class);
                playGame.putExtra("Game", game);
                startActivity(playGame);
            }
        });
    }


    private void buildCurrentGame(){
        Intent cameFrom = getIntent();

        //It's fine that we store this in curGame
        game = (Game) cameFrom.getSerializableExtra("Game");
        game = dbHelper.buildGame(game);
    }
}