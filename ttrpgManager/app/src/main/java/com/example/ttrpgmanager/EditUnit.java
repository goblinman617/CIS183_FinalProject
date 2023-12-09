package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditUnit extends AppCompatActivity {
    Button btn_j_update;
    Button btn_j_back;
    EditText et_j_unitName;
    EditText et_j_curHP;
    EditText et_j_maxHP;
    EditText et_j_initiative;
    Game game;
    Unit unit;
    DatabaseHelper dbHelper;
    Intent playGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_unit);

        dbHelper = new DatabaseHelper(this);

        btn_j_update = findViewById(R.id.btn_eu_update);
        btn_j_back = findViewById(R.id.btn_eu_back);
        et_j_unitName = findViewById(R.id.et_eu_unitName);
        et_j_curHP = findViewById(R.id.et_eu_curHP);
        et_j_maxHP = findViewById(R.id.et_eu_maxHP);
        et_j_initiative = findViewById(R.id.et_eu_initiative);

        playGame = new Intent(EditUnit.this, PlayGame.class);

        getPrevGame();
        Intent cameFrom = getIntent();
        unit = (Unit) cameFrom.getSerializableExtra("Unit");

        // I think we will receive a 'User' and a 'Game' to pass back

        buttonEventHandler();
        unitValues();
    }

    private void buttonEventHandler(){
        btn_j_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update the units stats in the database
                // go back to the PlayGame screen
                // pass the 'User' and the 'Game' back probably
                unit.setName(et_j_unitName.getText().toString());
                unit.setCurHealth(Integer.parseInt(et_j_curHP.getText().toString()));
                unit.setMaxHealth(Integer.parseInt(et_j_maxHP.getText().toString()));
                unit.setInitiative(Integer.parseInt(et_j_initiative.getText().toString()));
                dbHelper.updateUnit(unit);

                playGame.putExtra("Game", game);
                startActivity(playGame);
            }
        });

        btn_j_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame.putExtra("Game", game);
                startActivity(playGame);
            }
        });
    }

    public void unitValues()
    {
        et_j_unitName.setText(String.valueOf(unit.getName()));
        et_j_curHP.setText(String.valueOf(unit.getCurHealth()));
        et_j_maxHP.setText(String.valueOf(unit.getMaxHealth()));
        et_j_initiative.setText(String.valueOf(unit.getInitiative()));
    }

    private void getPrevGame(){
        Intent cameFrom2 = getIntent();

        game = (Game) cameFrom2.getSerializableExtra("Game");

        // We don't need to rebuild the game when we're on this screen
        //game = dbHelper.buildGame(game);
    }
}