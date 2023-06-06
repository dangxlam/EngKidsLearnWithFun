package com.liamdang.englishkidslearnwithfun.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.activity.RealtimeDb;
import com.liamdang.englishkidslearnwithfun.model.Topic;

import java.util.ArrayList;


public class TopicAdapter extends ArrayAdapter<Topic> {
    private Context mContext;

    public TopicAdapter(Context context, ArrayList<Topic> categories) {
        super(context, 0, categories);
    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        //Get data item for this posit
        Topic category = getItem(position);

        //Check if existing view is being reused, or inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_template, parent, false);
        }

        //Lookup view for data
        TextView categoryTitle = (TextView) convertView.findViewById(R.id.card_title);
        TextView cateVietTitle = (TextView) convertView.findViewById(R.id.card_viet_title);
        TextView categoryHighScore = (TextView) convertView.findViewById(R.id.card_high_score);
        ImageView categoryImage = (ImageView) convertView.findViewById(R.id.card_image);
        RelativeLayout categoryLayout = (RelativeLayout) convertView.findViewById(R.id.cardLayout);

        //Populate the data into template view using data object
        categoryTitle.setText(category.title);
        cateVietTitle.setText(category.viet_title);
        categoryHighScore.setText("Highscore: " + category.highScore);
        //Glide.with(getContext()).load(Uri.parse(category.getImageUrl())).error(R.drawable.avatar_default).into(categoryImage);
        categoryImage.setImageResource(category.image);
        categoryLayout.setBackgroundColor(category.color);





        return convertView;
    }
}
