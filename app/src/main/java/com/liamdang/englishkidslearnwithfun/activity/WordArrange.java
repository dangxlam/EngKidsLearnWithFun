package com.liamdang.englishkidslearnwithfun.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.liamdang.englishkidslearnwithfun.adapter.CharacterAdapter;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.fragment.HomeFragment;
import com.liamdang.englishkidslearnwithfun.model.Topic;
import com.liamdang.englishkidslearnwithfun.model.HighScores;
import com.liamdang.englishkidslearnwithfun.model.ObjectThing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class WordArrange extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Topic currentCate;
    ArrayList<ObjectThing> objThings;
    ArrayList<Character> character_list;

    private ObjectThing objAns;
    private TextView questionTv;
    private TextView tvWord;
    private TextView scoreTv;
    private ImageView mainPic;
    private ImageButton btnAudio;
    private ImageButton btnBackspace;
    private Button btnSkip;


    private MediaPlayer mediaPlayer;
    private TextToSpeech textToSpeech;


    CharacterAdapter adapter;
    GridView gridView;

    private int score = 0;
    private int questionNumber = 1;

    //if this value is set, block UI
    private boolean stopUserInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        currentCate = HomeFragment.topicList.get(position);
        setTheme(currentCate.theme);

        textToSpeech = MainActivity.textToSpeech;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_arrange);
        tvWord = findViewById(R.id.tvWord);
        scoreTv = (TextView) findViewById(R.id.scoreSum);
        questionTv = (TextView) findViewById(R.id.questionCount);
        mainPic = (ImageView) findViewById(R.id.thingImage);
        btnAudio = (ImageButton) findViewById(R.id.btnAudio);
        btnBackspace = (ImageButton) findViewById(R.id.btnBackspace);
        btnSkip = (Button) findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateResources();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back btn

        objThings = currentCate.getListOfThings();
        updateResources();

        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTitleSound(objAns.getText());
            }
        });

        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = tvWord.getText().toString();
                //word.substring(0, word.length() - 2);
                //char[] w = word.toCharArray();

                tvWord.setText("");
            }
        });


    }

    private void updateResources() {
        //Character[] character_list;
        tvWord.setText("");

        character_list = new ArrayList<>();

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
            HighScores.open(this);
            if (HighScores.setHighScore(currentCate.columnName, score))
                Toast.makeText(this, "New Highscore!", Toast.LENGTH_LONG).show();
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

        //relativeLayout.setBackgroundColor(primaryColor);




        objAns = randomObject();
        char[] ans_char = objAns.getText().toUpperCase().toCharArray();
        for(int i = 0; i < ans_char.length; i++) {
            Character ch = new Character(ans_char[i]);
            character_list.add(ch);
        }

        Random r = new Random();
        char[] char_alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L'
                , 'M', 'O', 'P', 'Q', 'R','S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        for(int i = 0; i < 12 - ans_char.length; i++) {
            int j = r.nextInt(char_alphabet.length);
            character_list.add(char_alphabet[j]);
        }

        for(int i = 0; i < ans_char.length; i++) {
            int j = r.nextInt(character_list.size());
            Character ch = character_list.get(i);
            character_list.set(i, character_list.get(j));
            character_list.set(j, ch);

        }


        //mainPic.setVisibility(View.INVISIBLE);
        // mainPic.setImageResource(objAns.getImage());
       // mainPic.setVisibility(View.VISIBLE);
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
        playTitleSound(objAns.getText());

        adapter = new CharacterAdapter( WordArrange.this, character_list);

        //attach the adapter to a Listview
        gridView = (GridView) findViewById(R.id.gridViewWord);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);


    }

    private ObjectThing randomObject() {
        Random r = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 3) {
            set.add(r.nextInt(objThings.size()));
        }

        Integer[] choices = set.toArray(new Integer[set.size()]);

        ArrayList<Integer> indexes = new ArrayList<>(Arrays.asList(0,1,2));
        int index = indexes.get(r.nextInt(indexes.size()));

        return objThings.get(choices[index]);

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String word = tvWord.getText().toString();
        word += character_list.get(position).toString();

        tvWord.setText(word);

        isCorrect(word);




    }

    private void isCorrect(String s) {
        if(s.toLowerCase().equals(objAns.getText().toLowerCase())) {
            score++;
            scoreTv.setText("Score: " + score);
            playSound(true);
            //block UI till new ques
            stopUserInteraction = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateResources();

                    stopUserInteraction = false; //enable UI
                }
            }, 2000);
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