package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateGame extends AppCompatActivity {
    Button btn_j_back;
    Button btn_j_update;
    Button btn_j_delete;
    EditText et_j_gameName;
    TextView tv_j_errorMsg;
    Game game;
    User user;
    DatabaseHelper dbHelper;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_game);

        btn_j_back = findViewById(R.id.btn_ug_back);
        btn_j_update = findViewById(R.id.btn_ug_update);
        btn_j_delete = findViewById(R.id.btn_ug_delete);
        et_j_gameName = findViewById(R.id.et_ug_gameName);
        tv_j_errorMsg = findViewById(R.id.tv_ug_errorMsg);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initializeTables();

        intent = new Intent(UpdateGame.this, GamesPage.class);

        Intent cameFrom = getIntent();
        game = (Game) cameFrom.getSerializableExtra("Game");
        user = (User) cameFrom.getSerializableExtra("User");

        et_j_gameName.setText(game.getGameName());

        buttonEventHandler();
    }

    public void buttonEventHandler()
    {
        btn_j_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_j_gameName.getText().toString().equals(""))
                {
                    tv_j_errorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    tv_j_errorMsg.setVisibility(View.INVISIBLE);
                    game.setGameName(et_j_gameName.getText().toString());
                    et_j_gameName.setText(game.getGameName());
                    dbHelper.updateGame(game);

                    intent.putExtra("Game", game);
                    startActivity(intent);

                }
            }
        });

        btn_j_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper.deleteGame(game);

                Intent delete = new Intent(UpdateGame.this, GamesPage.class);
                delete.putExtra("User", user);
                startActivity(delete);
            }
        });

        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Game", game);
                startActivity(intent);
            }
        });
    }
}