package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class GamesPage extends AppCompatActivity {
    DatabaseHelper dbHelper;
    ArrayList<Game> usersGames = new ArrayList<>();
    Button btn_j_newGame;
    Button btn_j_logout;
    Button btn_j_update;
    User user;
    ListView lv_j_games;
    GameListAdapter adapter;
    Intent playGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_page);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initializeTables();

        btn_j_newGame = findViewById(R.id.btn_games_newGame);
        btn_j_logout = findViewById(R.id.btn_games_logout);
        btn_j_update = findViewById(R.id.btn_games_update);
        lv_j_games = findViewById(R.id.lv_games);

        user = getCurrentUser();

        usersGames = dbHelper.getUsersGames(user.getUsername());

        playGame = new Intent(GamesPage.this, PlayGame.class);

        buttonEventHandler();
        fillGamesListView();
        gameListView();
        Log.d("gameID",usersGames.get(0).getGameID()+"");
    }

    private void buttonEventHandler(){
        btn_j_newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createGame = new Intent(GamesPage.this, NewGame.class);
                createGame.putExtra("User", user);
                startActivity(createGame);
            }
        });

        btn_j_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update = new Intent(GamesPage.this, UpdateUser.class);
                update.putExtra("User", user);
                startActivity(update);
            }
        });

        btn_j_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We probably don't have to pass anything to MainActivity
                Intent mainActivity = new Intent(GamesPage.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });
    }

    private User getCurrentUser(){
        Intent cameFrom = getIntent();

        User curUser = (User) cameFrom.getSerializableExtra("User");

        if (curUser == null){
            curUser = getUserFromGame();
        }
        return curUser;
    }

    private User getUserFromGame(){
        Game lastGame = (Game) getIntent().getSerializableExtra("Game");

        if (lastGame != null){
            return dbHelper.getUser(lastGame.getDMUsername());
        }
        return null;
    }

    public void fillGamesListView()
    {
        adapter = new GameListAdapter(this, usersGames);
        lv_j_games.setAdapter(adapter);
    }

    public void gameListView()
    {
        lv_j_games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                playGame.putExtra("Game", usersGames.get(position));
                playGame.putExtra("User", user);
                startActivity(playGame);

            }
        });
    }

    private void debugLogGamesUnits(ArrayList<Unit> units){
        for (int i = 0; i < units.size(); i++){
            Log.d("UnitList at " + i, units.get(i).getName());
        }
    }
}