package com.liamdang.englishkidslearnwithfun.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.liamdang.englishkidslearnwithfun.model.Topic;
import com.liamdang.englishkidslearnwithfun.model.ObjectThing;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.fragment.HomeFragment;

import java.util.Locale;
import java.util.UUID;

public class ObjectActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton rightBtn;
    private ImageButton leftBtn;
    private ImageButton quizBtn;
    private ImageView mainPicture;
    private TextView mainName;
    private TextView vietName;
    private ImageButton audioBtn;
    private RelativeLayout relativeLayout;
    private MediaPlayer mediaPlayer;
    ObjectThing currentObject;
    Topic currentCategory;

    private TextToSpeech textToSpeech;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        Intent intent = getIntent(); //return intent started this act
        int position = intent.getIntExtra("position", 0);



        textToSpeech = MainActivity.textToSpeech;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 2000);

        currentCategory = HomeFragment.topicList.get(position);
        currentCategory.goFirstObj();
        setTheme(currentCategory.theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_things);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainName = (TextView) findViewById(R.id.thingName);
        vietName = (TextView) findViewById(R.id.vietName);
        mainPicture = (ImageView) findViewById(R.id.thingImage);
        rightBtn = (ImageButton) findViewById(R.id.buttonRightThing);
        leftBtn = (ImageButton) findViewById(R.id.buttonLeftThing);
        audioBtn = (ImageButton) findViewById(R.id.btnAudio);
        relativeLayout = (RelativeLayout) findViewById(R.id.cardThingLayout);
        quizBtn = (ImageButton) findViewById(R.id.btnQuiz);

        rightBtn.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        audioBtn.setOnClickListener(this);
        mainPicture.setOnClickListener(this);
        quizBtn.setOnClickListener(this);



        currentObject = currentCategory.currentObj();
        updateResources();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

         */
        super.onPause();
        //releaseMediaPlayer();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Close Activity when press back btn
        finish();
        return true;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLeftThing:
                currentObject = currentCategory.prevObject();
                updateResources();
                break;
            case R.id.buttonRightThing:
                currentObject = currentCategory.nextObject();
                updateResources();
                break;
            case R.id.btnAudio:
                playSound(currentObject.getText());
                break;
            case R.id.thingImage:
                if (currentObject.hasNoise()) {
                    //playSound(currentObject.getNoise());
                }
                break;
            case R.id.btnQuiz:
                Intent prevIntent = getIntent();
                int position = prevIntent.getIntExtra("position", 0);
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                break;

        }
    }


    protected void updateResources() {
        if(currentObject.hasNoise()) {
            playObjectSound(currentObject.getNoise());
            //playSound(currentObject.getText());

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    player.reset();
                    playSound(currentObject.getText());
                }
            });


        }
        else {
            playSound(currentObject.getText());
        }

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();

        //retrieves the color value from this theme R.color.colorAccent
        //theme.resolveAttribute(R.attr.color, typedValue, true);
        theme.resolveAttribute(androidx.appcompat.R.attr.colorAccent, typedValue, true);
        int accentColor = typedValue.data;




        setBtnColor(leftBtn, accentColor);
        setBtnColor(rightBtn, accentColor);
        setBtnColor(audioBtn, accentColor);

        theme.resolveAttribute(R.attr.colorPrimaryLight, typedValue, true);
        int primaryLightColor = typedValue.data;
        //int primaryLightColor = R.color.primary_light;

        mainPicture.setBackgroundColor(primaryLightColor);
        relativeLayout.setBackgroundColor(primaryLightColor);
        mainName.setBackgroundColor(primaryLightColor);
        vietName.setBackgroundColor(primaryLightColor);
        setTitle(currentCategory.title);

        // make the picture Invisible and then Visible to add some animation
        //mainPicture.setVisibility(View.INVISIBLE);
        //mainPicture.setImageResource(currentObject.getImage());
        Glide.with(getApplicationContext())
                .load(Uri.parse(currentObject.getUrlImage()))
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
                .into(mainPicture);
        //mainPicture.setVisibility(View.VISIBLE);
        //quizBtn.setImageResource(quizImage);
        mainName.setText(currentObject.getText());
        vietName.setText(currentObject.getVietSub());

        rightBtn.setVisibility(currentCategory.hasNextObj()? View.VISIBLE : View.INVISIBLE);
        leftBtn.setVisibility(currentCategory.hasPrevObj()? View.VISIBLE : View.INVISIBLE);



    }

    private void setBtnColor (ImageButton btn, int color) {
        GradientDrawable bgShape = (GradientDrawable) btn.getBackground();
        bgShape.setColor(color);
    }


    private void playSound(String text) {

        String utteranceId = UUID.randomUUID().toString();

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null,utteranceId);

    }

    private void playObjectSound(int sound) {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            stopAndResetPlayer();
        }
        mediaPlayer = MediaPlayer.create(this, sound);
        mediaPlayer.start();


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                player.reset();
            }
        });

    }



    private void stopAndResetPlayer() {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    private void releaseMediaPlayer() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
