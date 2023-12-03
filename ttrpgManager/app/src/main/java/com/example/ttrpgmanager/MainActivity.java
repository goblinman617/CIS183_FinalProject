package com.example.ttrpgmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    Button btn_j_logIn;
    Intent gamePageIntent;
    DatabaseHelper dbHelper;
    ArrayList<User> listOfUsers;
    EditText et_j_username;
    EditText et_j_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_j_logIn = findViewById(R.id.btn_v_logIn);
        et_j_username = findViewById(R.id.et_v_username);
        et_j_password = findViewById(R.id.et_v_password);

        //LogIn Button -> GamesPage
        gamePageIntent = new Intent(MainActivity.this, GamesPage.class);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initializeTables();
        listOfUsers = dbHelper.getAllUserRows();

        logInEventHandler();

        // Debug commands
        debugFillLoginPage();

        User newUser = new User("userTest", "example password");
        // Doesn't allow new users with the same username
        dbHelper.registerUser(newUser);
        dbHelper.registerUser(newUser);

        // Doesn't allow users with a blank field
        dbHelper.registerUser(new User("test", ""));
        dbHelper.registerUser(new User("", "test"));

        // Deletes a user by username
        dbHelper.deleteUser(newUser);

        Log.d("UserRows", dbHelper.rowsInUsersTable() + "");


        // Doesn't allow new games with the same username and gameName
        dbHelper.createNewGame(new Game("DMleo", "Tower of Sycrus"));
        // Doesn't allow blank gameName
        dbHelper.createNewGame(new Game("DMleo", ""));

        // Builds the game from the database with just username and gameName
        // This means we can pass the same 'Game' object we used to for dbHelper.createNewGame(Game)
        // To the "PlayGame" Page with no issue.
        Game myGame = new Game("testing", "Testing of Testing");
        dbHelper.createNewGame(myGame);
        myGame = dbHelper.buildGame(myGame);
        Log.d("myGame ID", myGame.getGameID() + "");

        dbHelper.deleteGame(myGame);
        Log.d("GameRows", dbHelper.rowsInGamesTable() + "");

        // This will delete the game we initialize by default
        dbHelper.deleteGame(new Game("DMleo", "Tower of Sycrus"));
        // You can see that this function also deletes the units associated with the
        dbHelper.initializeTables();
        Log.d("GameRows", dbHelper.rowsInGamesTable() + "");
        Log.d("UnitRows", dbHelper.rowsInUnitsTable() + "");

    }

    public void logInEventHandler()
    {
        btn_j_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = et_j_username.getText().toString();
                String pword = et_j_password.getText().toString();

                User curUser = new User(uname,pword);

                if (dbHelper.validateLogin(curUser)) {

                    gamePageIntent.putExtra("User", curUser);

                    Log.d("Login", curUser.getUsername() + " has logged in");
                    startActivity(gamePageIntent);
                }else{
                    // Display that the login information was wrong
                    Log.d("Login", "Incorrect login information");
                }
            }
        });
    }

    private void debugUserList(){
        for (int i = 0; i < listOfUsers.size(); i++){
            Log.d("User at " + i, listOfUsers.get(i).getUsername() + " " + listOfUsers.get(i).getPassword());
        }
    }

    private void debugFillLoginPage(){
        et_j_username.setText("DMleo");
        et_j_password.setText("pass123");
    }
}