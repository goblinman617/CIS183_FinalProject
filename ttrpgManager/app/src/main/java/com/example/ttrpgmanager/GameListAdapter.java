package com.example.ttrpgmanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GameListAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Game> listOfGames;

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

        TextView tv_j_gc_gameName = view.findViewById(R.id.tv_gc_gameName);

        Game game = listOfGames.get(i);

        tv_j_gc_gameName.setText(game.getGameName());

        return view;
    }
}
