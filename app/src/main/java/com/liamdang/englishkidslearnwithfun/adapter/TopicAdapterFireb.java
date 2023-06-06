package com.liamdang.englishkidslearnwithfun.adapter;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.model.Topic;

import java.util.ArrayList;

public class TopicAdapterFireb extends ArrayAdapter<Topic> {
    private Context mContext;
    Intent intent;



    public TopicAdapterFireb(Context context, ArrayList<Topic> categories) {
        super(context, 0, categories);

        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT).addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                .addFlags(FLAG_GRANT_READ_URI_PERMISSION);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        //getContext().grantUriPermission(p, imageFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Glide.with(getContext())
                .load(Uri.parse(category.getUrl_Image()))
                .error(R.drawable.avatar_default)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception

                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(categoryImage);
        //categoryImage.setImageResource(category.image);
        categoryLayout.setBackgroundColor(category.color);






        return convertView;
    }
}
