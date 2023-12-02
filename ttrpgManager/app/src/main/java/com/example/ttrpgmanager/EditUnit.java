package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditUnit extends AppCompatActivity {
    Button btn_j_submit;
    Game game;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_unit);

        dbHelper = new DatabaseHelper(this);

        btn_j_submit = findViewById(R.id.btn_eu_submit);

        buildCurrentGame();

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
                playGame.putExtra("Game", game);
                startActivity(playGame);
            }
        });
    }
    private void buildCurrentGame(){
        Intent cameFrom = getIntent();

        game = (Game) cameFrom.getSerializableExtra("Game");

        // We don't need to rebuild the game when we're on this screen
        //game = dbHelper.buildGame(game);
    }
}