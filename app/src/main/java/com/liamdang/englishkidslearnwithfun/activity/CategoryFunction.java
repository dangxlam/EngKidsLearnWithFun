package com.liamdang.englishkidslearnwithfun.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.fragment.HomeFragment;
import com.liamdang.englishkidslearnwithfun.model.Topic;

public class CategoryFunction extends AppCompatActivity {

    private TextView cateTitle;
    private CardView cateWord;
    private CardView chooseImg;
    private CardView arrangeWord;
    private CardView pronunciationCv;
    private CardView testCate;
    private ImageView imgCategory;

    //public TextToSpeech textToSpeech;

    Topic currentCate;
    int position;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        currentCate = HomeFragment.topicList.get(position);
        setTheme(currentCate.theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_function);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back btn
        initUI();
        initListener();

        cateTitle.setText(currentCate.title);
        //imgCategory.setImageResource(currentCate.image);
        Glide.with(getApplicationContext())
                .load(Uri.parse(currentCate.getUrl_Image()))
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
                .into(imgCategory);

    }



    private void initUI() {
        cateTitle = (TextView) findViewById(R.id.tvCateTitle);
        cateWord = (CardView) findViewById(R.id.cateNewWord);
        chooseImg = (CardView) findViewById(R.id.chooseImage);
        arrangeWord = (CardView) findViewById(R.id.arrangeWord);
        pronunciationCv = (CardView) findViewById(R.id.pronunciationFunc);
        testCate = (CardView) findViewById(R.id.testCate);
        imgCategory = (ImageView) findViewById(R.id.imgCategory);
    }

    private void initListener() {
        cateWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( CategoryFunction.this, ObjectActivity.class);
                intent.putExtra("position", position);

                startActivity(intent);
            }
        });

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( CategoryFunction.this, ChooseImgActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        arrangeWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( CategoryFunction.this, WordArrange.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        pronunciationCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( CategoryFunction.this, PronunciationActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        testCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( CategoryFunction.this, QuizActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Close Activity when press back btn
        finish();
        return true;
    }
}