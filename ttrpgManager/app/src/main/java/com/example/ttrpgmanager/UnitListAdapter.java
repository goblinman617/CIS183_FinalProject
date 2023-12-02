package com.example.ttrpgmanager;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UnitListAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Unit> listOfUnits;

    public UnitListAdapter(Context c, ArrayList<Unit> u)
    {
        context = c;
        listOfUnits = u;
    }

    @Override
    public int getCount() {
        return listOfUnits.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfUnits.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.unit_cell, null);
        }

        TextView tv_j_uc_charaName = view.findViewById(R.id.tv_uc_charaName);
        TextView tv_j_uc_curHP = view.findViewById(R.id.tv_uc_curHP);
        TextView tv_j_uc_int = view.findViewById(R.id.tv_uc_int);

        Unit unit = listOfUnits.get(i);

        tv_j_uc_charaName.setText(unit.getName());
        tv_j_uc_curHP.setText(unit.getCurHealth() + "");
        //will add max health visual later
        tv_j_uc_int.setText(unit.getInitiative() + "");

        return view;
    }
}
