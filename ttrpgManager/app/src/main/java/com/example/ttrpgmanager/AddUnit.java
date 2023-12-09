package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddUnit extends AppCompatActivity {
    Button btn_j_back;
    Button btn_j_add;
    EditText et_j_unitName;
    EditText et_j_curHP;
    EditText et_j_maxHP;
    EditText et_j_init;
    Game game;
    Unit unit;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);

        dbHelper = new DatabaseHelper(this);

        btn_j_back = findViewById(R.id.btn_au_back);
        btn_j_add = findViewById(R.id.btn_au_add);
        et_j_unitName = findViewById(R.id.et_au_unitName);
        et_j_curHP = findViewById(R.id.et_au_curHP);
        et_j_maxHP = findViewById(R.id.et_au_maxHP);
        et_j_init = findViewById(R.id.et_au_initiative);


        Intent cameFrom = getIntent();
        game = (Game) cameFrom.getSerializableExtra("Game");

        getPrevGame();
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

        btn_j_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Needs to add unit to unit table

                String name = et_j_unitName.getText().toString();
                String hp = et_j_curHP.getText().toString();
                String max = et_j_maxHP.getText().toString();
                String init = et_j_init.getText().toString();

                Unit unit = new Unit(game.getGameID(), false, name, Integer.parseInt(hp), Integer.parseInt(max), Integer.parseInt(init), false);
                dbHelper.addUnit(unit);

                Intent playGame = new Intent(AddUnit.this, PlayGame.class);
                startActivity(playGame);
            }
        });
    }


    private void getPrevGame(){
        Intent cameFrom = getIntent();

        game = (Game) cameFrom.getSerializableExtra("Game");

        // We don't need
        //game = dbHelper.buildGame(game);
    }
}