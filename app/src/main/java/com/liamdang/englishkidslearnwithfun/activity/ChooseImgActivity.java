package com.liamdang.englishkidslearnwithfun.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.fragment.HomeFragment;
import com.liamdang.englishkidslearnwithfun.model.Topic;
import com.liamdang.englishkidslearnwithfun.model.ObjectThing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class ChooseImgActivity extends AppCompatActivity implements View.OnClickListener {

    Topic currentCate;
    ArrayList<ObjectThing> objThings;
    private ImageView mainPic;
    private TextView quesTitle;
    private LinearLayout relativeLayout;
    private ImageView choice1;
    private ImageView choice2;
    private ImageView choice3;
    private ImageView choice4;
    private ObjectThing objAns;
    private TextView questionTv;
    private TextView scoreTv;
    private ImageButton btnAudio;

    private TextToSpeech textToSpeech;


    private MediaPlayer mediaPlayer;

    private int score = 0;
    private int questionNumber = 1;
    private int ansPos = 0;

    //if this value is set, block UI
    private boolean stopUserInteraction;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 3);
        //position = 0;
        currentCate = HomeFragment.topicList.get(position);
        setTheme(currentCate.theme);

        textToSpeech = MainActivity.textToSpeech;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_choose_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back btn

        objThings = currentCate.getListOfThings();
        relativeLayout = (LinearLayout) findViewById(R.id.gameChooseImgLayout);
        //mainPic = (ImageView) findViewById(R.id.quizImage);
        quesTitle = (TextView) findViewById(R.id.tvQuesTitle);
        choice1 = (ImageView) findViewById(R.id.ivChoice1);
        choice2 = (ImageView) findViewById(R.id.ivChoice2);
        choice3 = (ImageView) findViewById(R.id.ivChoice3);
        choice4 = (ImageView) findViewById(R.id.ivChoice4);
        scoreTv = (TextView) findViewById(R.id.scoreSum);
        questionTv = (TextView) findViewById(R.id.questionCount);
        btnAudio = (ImageButton) findViewById(R.id.btnAudio);


        choice1.setOnClickListener(this);
        choice2.setOnClickListener(this);
        choice3.setOnClickListener(this);
        choice4.setOnClickListener(this);


        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTitleSound(objAns.getText());
            }
        });

        updateResources();

    }

    public void updateResources() {

        if (questionNumber == 1) {
            scoreTv.setText("Score: " + 0);
        } else if (questionNumber > 10) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Hoàn thành");
            builder.setMessage("Bạn đạt được " + score + " điểm!");

            builder.setPositiveButton("Xong", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishAct();

                }
            });

            AlertDialog dialog = builder.show();
            //this.finish();
            /*
            HighScores.open(this);
            if (HighScores.setHighScore(currentCate.columnName, score))
                Toast.makeText(this, "New Highscore!", Toast.LENGTH_LONG).show();
            HighScores.close();

             */
            return;
        }
        questionTv.setText("Question: " + questionNumber);
        questionNumber++;
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();

        theme.resolveAttribute(R.attr.colorPrimaryLight, typedValue, true);

        int primaryColor = typedValue.data;
        //int primaryColor = R.color.primary_light;
        //mainPic.setBackgroundColor(primaryColor);
        relativeLayout.setBackgroundColor(primaryColor);

        Random r = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            set.add(r.nextInt(objThings.size()));
        }

        Integer[] choices = set.toArray(new Integer[set.size()]);

        ArrayList<Integer> indexes = new ArrayList<>(Arrays.asList(0,1,2,3));
        ansPos = indexes.get(r.nextInt(indexes.size()));

        objAns = objThings.get(choices[ansPos]);

        playTitleSound(objAns.getText());

        quesTitle.setVisibility(View.INVISIBLE);
        quesTitle.setText(objAns.getText());
        quesTitle.setVisibility(View.VISIBLE);

        setRandomChoice(choice1, indexes, choices);
        setRandomChoice(choice2, indexes, choices);
        setRandomChoice(choice3, indexes, choices);
        setRandomChoice(choice4, indexes, choices);

    }

    private void setRandomChoice(ImageView button, ArrayList<Integer> indexes, Integer[] answers) {
        // params:
        // e.g., indexes = [0, 1, 2]
        // e.g., answers [2, 6, 9]
        Random r = new Random();
        // random index from [0, 1, 2]. e.g., 1-Mango
        int index = indexes.get(r.nextInt(indexes.size()));
        // e.g., remove the index 1 so Mango won't appear two times as answer
        indexes.remove(Integer.valueOf(index));
        // indexes = [0, 2]
        //button.setImageResource(objThings.get(answers[index]).getImage());
        Glide.with(getApplicationContext())
                .load(Uri.parse(objThings.get(answers[index]).getUrlImage()))
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
                .into(button);
        if(index == ansPos) {
            button.setImageAlpha(254);
        } else {
            button.setImageAlpha(255);
        }

    }

    @Override
    public void onClick(View view) {
        if(view instanceof ImageView) {
            if(((ImageView) view).getImageAlpha() == 254) {
                score++;
                scoreTv.setText("Score: " + score);
                playSound(true);
            } else {
                playSound(false);
            }
            //block UI till new ques
            stopUserInteraction = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateResources();
                    if (view instanceof RadioButton)
                        //((RadioButton) view).setChecked(false);
                    stopUserInteraction = false; //enable UI
                }
            }, 2000);
        }

    }

    private void playSound(boolean isCorrect) {
        mediaPlayer = MediaPlayer.create(this,
                isCorrect ? randomCorrectSound() : randomWrongSound());
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                player.reset();
            }
        });
    }

    private int randomCorrectSound() {
        List<Integer> correctSounds = new ArrayList<>();
        correctSounds.add(R.raw.correct1_good_job);
        correctSounds.add(R.raw.correct2_well_done);
        correctSounds.add(R.raw.correct3_perfect);
        correctSounds.add(R.raw.correct4_amazing);
        correctSounds.add(R.raw.correct5_great);
        Random rand = new Random();

        return correctSounds.get(rand.nextInt(correctSounds.size()));
    }

    private int randomWrongSound() {
        List<Integer> wrongSounds = new ArrayList<>();
        wrongSounds.add(R.raw.wrong1_oh_no);
        wrongSounds.add(R.raw.wrong2_try_again);
        wrongSounds.add(R.raw.wrong3_wrong);
        wrongSounds.add(R.raw.wrong4_you_need_some_practice);
        Random rand = new Random();

        return wrongSounds.get(rand.nextInt(wrongSounds.size()));

    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void finishAct() {
        this.finish();
    }

    private void playTitleSound(String text) {
        /*
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

         */

        String utteranceId = UUID.randomUUID().toString();

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null,utteranceId);
    }

    private void stopAndResetPlayer() {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Close Activity when press back btn
        finish();
        return true;
    }
}
