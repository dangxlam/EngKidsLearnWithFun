package com.liamdang.englishkidslearnwithfun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.liamdang.englishkidslearnwithfun.R;

import java.util.ArrayList;

public class CharacterAdapter extends ArrayAdapter<Character> {
    public CharacterAdapter (Context context, ArrayList<Character> characters_list) {
        super(context, 0, characters_list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get data item for this posit
        Character character = getItem(position);

        //Check if existing view is being reused, or inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.character_template, parent, false);
        }

        //Lookup view for data
        TextView characterTitle = (TextView) convertView.findViewById(R.id.character_title);


        //Populate the data into template view using data object
        characterTitle.setText(character.toString());


        return convertView;
    }
}
