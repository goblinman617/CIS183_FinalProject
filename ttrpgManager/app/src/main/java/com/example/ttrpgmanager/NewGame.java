package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewGame extends AppCompatActivity {
    EditText et_j_gameName;
    TextView tv_j_nameTaken;
    Button btn_j_createGame;
    Button btn_j_back;
    Game game;
    User user;
    DatabaseHelper dbHelper;
    Intent gamesPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        et_j_gameName = findViewById(R.id.et_ng_gameName);
        tv_j_nameTaken = findViewById(R.id.tv_ng_nameTaken);
        btn_j_createGame = findViewById(R.id.btn_ng_create);
        btn_j_back = findViewById(R.id.btn_ng_back);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initializeTables();

        Intent cameFrom = getIntent();
        user = (User) cameFrom.getSerializableExtra("User");

        gamesPage = new Intent(NewGame.this, GamesPage.class);

        buttonEventHandler();
    }

    public void buttonEventHandler()
    {
        btn_j_createGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = et_j_gameName.getText().toString();
                Game newGame = new Game(user.getUsername(), name);

                if(dbHelper.createNewGame(newGame))
                {
                    tv_j_nameTaken.setVisibility(View.INVISIBLE);
                    gamesPage.putExtra("User", user);
                    startActivity(gamesPage);
                }
                else {
                    tv_j_nameTaken.setVisibility(View.VISIBLE);
                }

            }
        });

        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamesPage.putExtra("User", user);
                startActivity(gamesPage);
            }
        });
    }
}