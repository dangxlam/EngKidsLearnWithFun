package com.liamdang.englishkidslearnwithfun.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.databinding.ActivityPronunciationBinding;
import com.liamdang.englishkidslearnwithfun.fragment.HomeFragment;
import com.liamdang.englishkidslearnwithfun.model.Topic;
import com.liamdang.englishkidslearnwithfun.model.ObjectThing;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class PronunciationActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPronunciationBinding binding;
    ObjectThing currentObject;
    Topic currentCategory;
    private MediaPlayer mediaPlayer;
    //private ImageButton btnVoice;
    protected static final int RESULT_SPEECH = 1;

    private MainActivity mainActivity;
    public final static int MY_REQUEST_RECORD = 11;

    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    AlertDialog.Builder alertSpeechDialog;
    AlertDialog alertDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent(); //return intent started this act
        int position = intent.getIntExtra("position", 0);
        currentCategory = HomeFragment.topicList.get(position);
        currentCategory.goFirstObj();
        setTheme(currentCategory.theme);
        //btnVoice = findViewById(R.id.btn_voice);

        textToSpeech = MainActivity.textToSpeech;



        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pronunciation);

        binding = ActivityPronunciationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        /*
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(PronunciationActivity.this).inflate(R.layout.alert_custom, viewGroup, false);

                alertSpeechDialog = new AlertDialog.Builder(PronunciationActivity.this);
                alertSpeechDialog.setMessage("Listening...");
                alertSpeechDialog.setView(dialogView);
                alertDialog = alertSpeechDialog.create();
                alertDialog.show();


            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> arrayList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                binding.voiceRecorded.setText(arrayList.get(0));
                if(arrayList.get(0).equals(binding.thingName.getText().toString().toLowerCase())) {
                    Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }




        });

        binding.btnVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    speechRecognizer.stopListening();
                    //alertDialog.dismiss();
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    speechRecognizer.startListening(speechIntent);
                }
                return false;
            }
        });

        */

        binding.buttonLeftThing.setOnClickListener(this);
        binding.buttonRightThing.setOnClickListener(this);
        binding.btnAudio.setOnClickListener(this);


        binding.btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                try {
                    startActivityForResult(intent1, RESULT_SPEECH);
                    //binding.thingName.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device does not support!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });





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
        releaseMediaPlayer();
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
                playTitleSound(currentObject.getText());
                break;
            case R.id.thingImage:
                if (currentObject.hasNoise()) {
                    //playSound(currentObject.getNoise());
                }
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    //ArrayList<String> arrayList = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String text = arrayList.get(0);
                    //binding.voiceRecorded.setText(text);

                    if(text.toLowerCase().equals(binding.thingName.getText().toString().toLowerCase())) {
                        Toast.makeText(getApplicationContext(), "Chính xác!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Sai rồi!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    protected void updateResources() {

        playTitleSound(currentObject.getText());


        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();

        //retrieves the color value from this theme
        //theme.resolveAttribute(R.attr.color, typedValue, true);
        int accentColor = R.color.colorAccent;




        setBtnColor(binding.buttonLeftThing, accentColor);
        setBtnColor(binding.buttonRightThing, accentColor);
        setBtnColor(binding.btnAudio, accentColor);

        theme.resolveAttribute(R.attr.colorPrimaryLight, typedValue, true);
        int primaryLightColor = typedValue.data;
        //int primaryLightColor = R.color.primary_light;

        binding.thingImage.setBackgroundColor(primaryLightColor);
        binding.getRoot().setBackgroundColor(primaryLightColor);
        binding.thingName.setBackgroundColor(primaryLightColor);

        setTitle(currentCategory.title);

        // make the picture Invisible and then Visible to add some animation
        //binding.thingImage.setVisibility(View.INVISIBLE);
        //binding.thingImage.setImageResource(currentObject.getImage());
        //binding.thingImage.setVisibility(View.VISIBLE);
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
                .into(binding.thingImage);

        binding.thingName.setText(currentObject.getText());


        binding.buttonRightThing.setVisibility(currentCategory.hasNextObj()? View.VISIBLE : View.INVISIBLE);
        binding.buttonLeftThing.setVisibility(currentCategory.hasPrevObj()? View.VISIBLE : View.INVISIBLE);



    }

    private void setBtnColor (ImageButton btn, int color) {
        GradientDrawable bgShape = (GradientDrawable) btn.getBackground();
        bgShape.setColor(color);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_REQUEST_RECORD && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}