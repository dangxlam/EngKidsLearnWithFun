package com.liamdang.englishkidslearnwithfun.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liamdang.englishkidslearnwithfun.adapter.TopicAdapterFireb;
import com.liamdang.englishkidslearnwithfun.model.Topic;
import com.liamdang.englishkidslearnwithfun.adapter.TopicAdapter;
import com.liamdang.englishkidslearnwithfun.model.HighScores;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.fragment.ChangePassFragment;
import com.liamdang.englishkidslearnwithfun.fragment.HomeFragment;
import com.liamdang.englishkidslearnwithfun.fragment.MyProfileFragment;
import com.liamdang.englishkidslearnwithfun.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    public static ArrayList<Topic> categories;
    //public static TopicAdapterFireb adapter;
    ListView listView;
    private DrawerLayout drawerLayout;
    public static ArrayList<Topic> topicList;
    public static User mUser;

    //ImageButton imageButton;


    private NavigationView navigationView;
    private ImageView imgAvatar;
    private TextView tvName;
    private TextView tvEmail;
    final private MyProfileFragment myProfileFragment = new MyProfileFragment();
    //private HomeFragment homeFragment;

    public final static int MY_REQUEST_CODE = 10;
    public final static int MY_REQUEST_RECORD = 11;

    public static TextToSpeech textToSpeech;


    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_MY_PROFILE = 1;
    private static final int FRAGMENT_CHANGE_PASS = 2;

    private int currentFragment = 0;

    final private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if(intent == null) return;
                Uri uri = intent.getData();
                myProfileFragment.setUri(uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    myProfileFragment.setBitmapImageView(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    });


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        categories = new ArrayList<>();


        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        //homeFragment = (HomeFragment) HomeFragment;

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);

                }
            }
        });

        //getUserData();




        drawerLayout = findViewById(R.id.cardListLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navi_navi_open, R.string.navi_navi_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        initUI();
        setMyRequestRecord();

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);




        showUserInf();






    }



    public ArrayList<Topic> getRealtimeData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_topic");



        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (topicList != null) {
                    topicList.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Topic cate = dataSnapshot.getValue(Topic.class);
                    cate.setHighScore(mUser.getTopicScore(topicList.size()));
                    topicList.add(cate);
                }


                //adapter.notifyDataSetChanged();



            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Fail get list", Toast.LENGTH_LONG).show();

            }
        });

        return topicList;



    }

    private void initUI() {
        navigationView = findViewById(R.id.navigation_view);
        //imageButton = (ImageButton) findViewById(R.id.btnGameChooseImg);

        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateHighScores();
    }







    public void updateHighScores() {
        HighScores.open(this);
        for (Topic c : categories) {
            c.updateHighScore();
        }
        HighScores.close();
        //notify adapter to display latest HighScores
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ObjectActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_home) {
            if(currentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                currentFragment = FRAGMENT_HOME;
                navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                navigationView.getMenu().findItem(R.id.nav_my_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_change_pass).setChecked(false);

            }

        } else if(id == R.id.nav_my_profile) {
            if(currentFragment != FRAGMENT_MY_PROFILE) {
                replaceFragment(myProfileFragment);
                currentFragment = FRAGMENT_MY_PROFILE;
                navigationView.getMenu().findItem(R.id.nav_my_profile).setChecked(true);
                navigationView.getMenu().findItem(R.id.nav_home).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_change_pass).setChecked(false);
            }

        } else if(id == R.id.nav_change_pass) {
            if(currentFragment != FRAGMENT_CHANGE_PASS) {
                replaceFragment(new ChangePassFragment());
                currentFragment = FRAGMENT_CHANGE_PASS;
                navigationView.getMenu().findItem(R.id.nav_change_pass).setChecked(true);
                navigationView.getMenu().findItem(R.id.nav_my_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.nav_home).setChecked(false);
            }

        } else if(id == R.id.nav_log_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Đăng Xuất");
            builder.setMessage("Bạn muốn đăng xuất?");

            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog dialog = builder.show();


        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_layout, fragment);
        transaction.commit();
    }

    public void showUserInf() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if( user == null) return;

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name == null) {
            name = "Hello";
        }
        else {
            name = "Hello " + name;
        }

        tvName.setText(name);
        tvEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.avatar_default2).into(imgAvatar);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE) {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
            else {
                return;

            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "Chọn hình ảnh"));


    }

    public void setMyRequestRecord() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }

    private void requestPermission () {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.RECORD_AUDIO}, MY_REQUEST_RECORD);
        }

    }
}
