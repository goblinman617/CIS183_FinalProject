package com.example.ttrpgmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class GameListAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Game> listOfGames;
    ImageButton img_j_updateGame;
    Button btn_j_gameName;

    public GameListAdapter(Context c, ArrayList<Game> g)
    {
        context = c;
        listOfGames = g;
    }

    @Override
    public int getCount() { return listOfGames.size(); }

    @Override
    public Object getItem(int i) { return listOfGames.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.game_cell, null);
        }

        btn_j_gameName = view.findViewById(R.id.btn_gc_gameName);
        img_j_updateGame = view.findViewById(R.id.img_gc_updateGame);

        buttonEventHandler(i);

        Game game = listOfGames.get(i);

        btn_j_gameName.setText(game.getGameName());

        return view;
    }

    private void buttonEventHandler(int i){
        img_j_updateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // build a user to pass (for if they delete the game
                User curUser = new User();
                curUser.setUsername(listOfGames.get(i).getDMUsername());

                Intent updateGame = new Intent(context, UpdateGame.class);
                updateGame.putExtra("User", curUser);
                updateGame.putExtra("Game", listOfGames.get(i));

                context.startActivity(updateGame);
            }
        });

        btn_j_gameName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User curUser = new User();
                curUser.setUsername(listOfGames.get(i).getDMUsername());

                Intent playGame = new Intent(context, PlayGame.class);
                playGame.putExtra("User", curUser);
                playGame.putExtra("Game", listOfGames.get(i));

                context.startActivity(playGame);

            }
        });
    }
}
