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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liamdang.englishkidslearnwithfun.model.Topic;
import com.liamdang.englishkidslearnwithfun.model.HighScores;
import com.liamdang.englishkidslearnwithfun.model.ObjectThing;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.fragment.HomeFragment;
import com.liamdang.englishkidslearnwithfun.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    Topic currentCate;
    ArrayList<ObjectThing> objThings;
    private ImageView mainPic;
    private RelativeLayout relativeLayout;
    private RadioButton choice1;
    private RadioButton choice2;
    private RadioButton choice3;
    private ObjectThing objAns;
    private TextView questionTv;
    private TextView scoreTv;
    private MediaPlayer mediaPlayer;
    DatabaseReference mRef;




    private int score = 0;
    private int questionNumber = 1;
    int position;

    User user;

    //if this value is set, block UI
    private boolean stopUserInteraction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        currentCate = HomeFragment.topicList.get(position);
        setTheme(currentCate.theme);

        user = HomeFragment.mUser;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back btn

        objThings = currentCate.getListOfThings();
        relativeLayout = (RelativeLayout) findViewById(R.id.quizLayout);
        mainPic = (ImageView) findViewById(R.id.quizImage);
        choice1 = (RadioButton) findViewById(R.id.choice1);
        choice2 = (RadioButton) findViewById(R.id.choice2);
        choice3 = (RadioButton) findViewById(R.id.choice3);
        scoreTv = (TextView) findViewById(R.id.scoreSum);
        questionTv = (TextView) findViewById(R.id.questionCount);

        choice1.setOnClickListener(this);
        choice2.setOnClickListener(this);
        choice3.setOnClickListener(this);

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

            setHighScore(score);
            //this.finish();
            HighScores.open(this);
            if (HighScores.setHighScore(currentCate.columnName, score))
                //Toast.makeText(this, "New Highscore!", Toast.LENGTH_LONG).show();
            HighScores.close();
            return;
        }
        questionTv.setText("Question: " + questionNumber);
        questionNumber++;
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();

        theme.resolveAttribute(R.attr.colorPrimaryLight, typedValue, true);

        int primaryColor = typedValue.data;
        //int primaryColor = R.color.primary_light;
        mainPic.setBackgroundColor(primaryColor);
        relativeLayout.setBackgroundColor(primaryColor);

        Random r = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 3) {
            set.add(r.nextInt(objThings.size()));
        }

        Integer[] choices = set.toArray(new Integer[set.size()]);

        ArrayList<Integer> indexes = new ArrayList<>(Arrays.asList(0,1,2));
        int index = indexes.get(r.nextInt(indexes.size()));
        objAns = objThings.get(choices[index]);

        //mainPic.setVisibility(View.INVISIBLE);
        //mainPic.setImageResource(objAns.getImage());
        //mainPic.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext())
                .load(Uri.parse(objAns.getUrlImage()))
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
                .into(mainPic);

        setRandomChoice(choice1, indexes, choices);
        setRandomChoice(choice2, indexes, choices);
        setRandomChoice(choice3, indexes, choices);

    }

    private void finishAct() {
        this.finish();
    }

    private void setHighScore(int score) {
        if(score > user.getTopicScore(position)) {
            Toast.makeText(this, "New Highscore!", Toast.LENGTH_LONG).show();

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            mRef = database.getReference();
            //mRef = database.getReference("list_users").child(user.getUid());
            updateUserScore(user, fbUser.getUid(), score);
            //user.setTopicScore(position, score);
        }




    }

    public void updateUserScore(User user, String userId, int score) {


        mRef.child("users").child(userId).child("scores").child(Integer.toString(position)).setValue(score);
    }


    private void setRandomChoice(RadioButton button, ArrayList<Integer> indexes, Integer[] answers) {
        // params:
        // e.g., indexes = [0, 1, 2]
        // e.g., answers [2, 6, 9]
        Random r = new Random();
        // random index from [0, 1, 2]. e.g., 1-Mango
        int index = indexes.get(r.nextInt(indexes.size()));
        // e.g., remove the index 1 so Mango won't appear two times as answer
        indexes.remove(Integer.valueOf(index));
        // indexes = [0, 2]
        button.setText(objThings.get(answers[index]).getText());
    }

    @Override
    public void onClick(final View view) {

        if(view instanceof RadioButton) {
            if(((RadioButton) view).getText() == objAns.getText()) {
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
                        ((RadioButton) view).setChecked(false);
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

    @Override
    public boolean onSupportNavigateUp() {
        //Close Activity when press back btn
        finish();
        return true;
    }
}
