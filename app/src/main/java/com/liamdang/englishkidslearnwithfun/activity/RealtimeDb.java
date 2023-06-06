package com.liamdang.englishkidslearnwithfun.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.adapter.TopicAdapterFireb;
import com.liamdang.englishkidslearnwithfun.model.Topic;
import com.liamdang.englishkidslearnwithfun.model.HighScores;
import com.liamdang.englishkidslearnwithfun.model.ObjectThing;
import com.liamdang.englishkidslearnwithfun.mySQLiteHelper;

import java.util.ArrayList;

public class RealtimeDb extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Button btnPutList;
    private Button btnGetList;
    TopicAdapterFireb adapter;
    FirebaseStorage storage;
    ListView listView;
    //ArrayList<TestObject> testObjects;

    public static ArrayList<Topic> topicList;
    //public static ArrayList<Topic> cateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_db);

        initUI();


        btnPutList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPutList();
            }
        });

        btnGetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRealtimeData();


                System.out.println("This is Hello TEXT Xin chào" );
            }
        });




    }

    private void initUI() {
        btnPutList = (Button) findViewById(R.id.btn_put_list_obj);
        btnGetList = (Button) findViewById(R.id.btn_get_list_obj);
    }

    private void getImageUrl() {
        StorageReference listRef = storage.getReference().child("Alphabet");

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {


                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            item.getDownloadUrl();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });



    }






    private void onClickPutList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_topic");

        makeTopicList();



        myRef.setValue(topicList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(RealtimeDb.this, "Add success", Toast.LENGTH_LONG).show();

            }
        });

    }


    private void getRealtimeData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_topic");

        topicList = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Topic cate = dataSnapshot.getValue(Topic.class);
                    topicList.add(cate);
                }

                adapter = new TopicAdapterFireb( getApplicationContext(), topicList);

                //attach the adapter to a Listview
                listView = (ListView) findViewById(R.id.listViewCardsFire);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(RealtimeDb.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(RealtimeDb.this, "Fail get list", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CategoryFunction.class);
        intent.putExtra("position", position);
        startActivity(intent);

    }

    private void makeTopicList() {
        /*
        topicList = new ArrayList<>();

        HighScores.open(this);
        TestTopic alphabetCate = new TestTopic("Alphabet",
                "Chữ cái",
                "https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2Falphabet.png?alt=media&token=dd6107b2-272c-4dab-904f-c03311140fcd",
                HighScores.getHighScore(mySQLiteHelper.COLUMN_ALPHABET),
                ContextCompat.getColor(this, R.color.teal_700),
                R.style.PurpleTheme,
                mySQLiteHelper.COLUMN_ALPHABET);


        TestTopic numberCate = new TestTopic("Numbers",
                "Số đếm",
                "https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2Fnumbers.png?alt=media&token=bdf7df06-6eca-42ed-a637-4dd16ea73d6d",
                HighScores.getHighScore(mySQLiteHelper.COLUMN_NUMBER),
                ContextCompat.getColor(this, R.color.purple_200),
                R.style.GreenTheme,
                mySQLiteHelper.COLUMN_NUMBER);


        HighScores.close();

        testObjects = new ArrayList<>();



        testObjects.add(new TestObject("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fa.png?alt=media&token=7533a594-53de-4d2b-ad73-e74aa1be5653"
                ,"", "C"));
        testObjects.add(new TestObject("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fb.png?alt=media&token=d101470d-1d2f-42bb-ab3c-8ffd5adb45f2"
                ,"","D"));



        testObjects.add(new TestObject("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum0.png?alt=media&token=f84d5abb-4327-4ced-a586-49514cee5233"
                ,"Không","2"));
        testObjects.add(new TestObject("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum1.png?alt=media&token=cc6a0f6a-6605-4624-8eae-e9e3c11f7a07"
                ,"Một","3"));

        topicList.add(alphabetCate);
        topicList.add(numberCate);

         */





        topicList = new ArrayList<>();

        HighScores.open(this);

        Topic alphabetCate = new Topic("Alphabet",
                "Chữ cái",
                "https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2Falphabet.png?alt=media&token=dd6107b2-272c-4dab-904f-c03311140fcd",
                HighScores.getHighScore(mySQLiteHelper.COLUMN_ALPHABET),
                ContextCompat.getColor(this, R.color.teal_700),
                R.style.PurpleTheme,
                mySQLiteHelper.COLUMN_ALPHABET);


        Topic numberCate = new Topic("Numbers",
                "Số đếm",
                "https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2Fnumbers.png?alt=media&token=bdf7df06-6eca-42ed-a637-4dd16ea73d6d",
                HighScores.getHighScore(mySQLiteHelper.COLUMN_NUMBER),
                ContextCompat.getColor(this, R.color.purple_200),
                R.style.GreenTheme,
                mySQLiteHelper.COLUMN_NUMBER);

        Topic fruitCate = new Topic( "Fruits",
                "Trái cây",
                "https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2Ffruits.png?alt=media&token=c1d6d17e-b30a-4ca3-9eb3-b7ff1835cb66",
                HighScores.getHighScore(mySQLiteHelper.COLUMN_FRUITS),
                ContextCompat.getColor(this, R.color.primary_dark),
                R.style.GreenTheme,
                mySQLiteHelper.COLUMN_FRUITS);
        Topic animalCate = new Topic( "Animals",
                "Động vật",
                "https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2Fanimals.png?alt=media&token=1882f3c1-4443-481e-9e49-9d01ac53d8bf",
                HighScores.getHighScore(mySQLiteHelper.COLUMN_ANIMALS),
                ContextCompat.getColor(this, R.color.blue_primary_dark),
                R.style.BlueTheme,
                mySQLiteHelper.COLUMN_ANIMALS);
        Topic foodCate = new Topic( "Food",
                "Thức ăn",
                "https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2Ffood.png?alt=media&token=3b42d3e3-a9e8-4c73-bb73-c82d82f96b8f",
                HighScores.getHighScore(mySQLiteHelper.COLUMN_FOOD),
                ContextCompat.getColor(this, R.color.pink_primary_dark),
                R.style.PinkTheme,
                mySQLiteHelper.COLUMN_FOOD);


        Topic colorCate = new Topic( "Color",
                "Màu sắc",
                "https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2Fcolors.png?alt=media&token=5b7efc16-b823-437c-a81e-28ac953ee2d1",
                HighScores.getHighScore(mySQLiteHelper.COLUMN_COLORS),
                ContextCompat.getColor(this, R.color.purple_primary_dark),
                R.style.PurpleTheme,
                mySQLiteHelper.COLUMN_COLORS);

        HighScores.close();

        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fa.png?alt=media&token=7533a594-53de-4d2b-ad73-e74aa1be5653"
                 ,"", "A"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fb.png?alt=media&token=d101470d-1d2f-42bb-ab3c-8ffd5adb45f2"
                ,"","B"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fc.png?alt=media&token=d98bc112-577f-4385-8af4-7c144983237c"
                ,"","C"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fd.png?alt=media&token=2f80c202-0f38-4ece-9956-300876915ded"
                ,"","D"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fe.png?alt=media&token=94262dc4-68f9-4004-a2b8-3b9a93c792eb"
                ,"","E"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Ff.png?alt=media&token=d11e71c0-0a35-4438-aa2f-0e4239d5b737"
                ,"","F"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fg.png?alt=media&token=a4d795b0-3d24-4739-9b38-b2aa9d709117"
                ,"","J"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fh.png?alt=media&token=a39d02ea-1ebb-4447-8da2-4dbbb99eced6"
                ,"","H"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fi.png?alt=media&token=13534200-6153-45e3-a65a-724997789176"
                ,"","I"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fj.png?alt=media&token=36575655-5a76-4add-9156-54c5595f91a3"
                ,"","J"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fk.png?alt=media&token=27f4e637-4b35-4572-98da-41e36984aab5"
                ,"","K"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fk.png?alt=media&token=27f4e637-4b35-4572-98da-41e36984aab55"
                ,"","L"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fm.png?alt=media&token=c8d85f22-ce08-4243-8350-44e178023cfd"
                ,"","M"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fn.png?alt=media&token=a5012d7c-a59a-4c63-bc4e-4575418da1ef"
                ,"","N"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fo.png?alt=media&token=df120818-df91-43ef-b812-dd7e45ef3c94"
                ,"","O"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fp.png?alt=media&token=bf233a1b-1167-43b2-a309-6a37caf12181"
                ,"","P"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fq.png?alt=media&token=440755e8-de02-41f3-97b9-576f446c62e5"
                ,"","Q"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fr.png?alt=media&token=af5f25c7-224a-454d-912a-fd511260adde"
                ,"","R"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fs.png?alt=media&token=93225cd2-38a2-4a78-85cd-dadbd82362e6"
                ,"","S"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Ft.png?alt=media&token=1e227080-7d5d-4671-9e07-0f7076a333b2"
                ,"","T"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Ft.png?alt=media&token=1e227080-7d5d-4671-9e07-0f7076a333b2"
                ,"","U"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fv.png?alt=media&token=8e8de408-fe47-40ca-8dc1-c35d8b57bd66"
                ,"","V"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fw.png?alt=media&token=5a04525e-b8dd-438b-9c06-24b2922aa567"
                ,"","W"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fx.png?alt=media&token=8e592a1c-c319-4f89-977f-0d4c2d3210ba"
                ,"","X"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fy.png?alt=media&token=bd3eb88b-c90a-4b5e-a302-ab6acf022a44"
                ,"","Y"));
        alphabetCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Alphabet%2FImage%2Fz.png?alt=media&token=302414be-4858-4c5f-a6f5-051866ceeb3a"
        ,"",""));


        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum0.png?alt=media&token=f84d5abb-4327-4ced-a586-49514cee5233"
                ,"Không","0"));
        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum1.png?alt=media&token=cc6a0f6a-6605-4624-8eae-e9e3c11f7a07"
                ,"Một","1"));
        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum2.png?alt=media&token=fed65c4c-0a78-401b-8ab5-64252b9859ea"
                ,"Hai","2"));
        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum3.png?alt=media&token=cbc322a8-7758-4719-9fed-27f5315af091"
                ,"Ba","3"));
        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum4.png?alt=media&token=c2beff0c-804f-48be-be69-527576dd287b"
                ,"Bốn","4"));
        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum5.png?alt=media&token=d6e09fa8-0f85-4edd-a9eb-f2600821df1d"
                ,"Năm","5"));
        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum6.png?alt=media&token=64f3383e-a46c-4864-b8a7-c3af9c72d8f2"
                ,"Sáu","6"));
        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum7.png?alt=media&token=b2c3d193-465f-45b4-bd31-4df0c6e922d7"
                ,"Bảy","7"));
        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum8.png?alt=media&token=991ed5f1-eed3-4d83-bc9c-62db85945c8f"
                ,"Tám","8"));
        numberCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Numbers%2FImage%2Fnum9.png?alt=media&token=8333b90a-97ec-44fd-b62d-8464f78f9d5f"
                ,"Chín","9"));



        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fdog.png?alt=media&token=9c8b9259-00fd-4e21-a262-377825f3e3f2"
                ,"Con chó", "Dog"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fcat.png?alt=media&token=ae41f7f3-99ff-41a9-84b2-a3820d54de14"
                ,"Con mèo", "Cat"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fbear.png?alt=media&token=08e1a04e-2a1d-4c8c-8c60-082176f08717"
                ,"Con gấu", "Bear"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fwolf.png?alt=media&token=4d766d7b-0a06-442e-9c5c-1d4d69b565dd"
                ,"Con chó sói", "Wolf"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fdolphin.png?alt=media&token=96fe32a5-f444-4a97-98f7-9092884f59ea"
                ,"Con cá heo", "Dolphin"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fduck.png?alt=media&token=d4740d1b-7bde-4bfb-bcb8-edef25c183a4"
                ,"Con vịt", "Duck"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fleopard.png?alt=media&token=c1245c41-42a6-4ca9-988e-40333f077625"
                ,"Con báo", "Leopard"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Flion.png?alt=media&token=e4a22d21-b23e-4f09-8e7a-b43071683c6d"
                ,"Con sư tử", "Lion"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Flion.png?alt=media&token=e4a22d21-b23e-4f09-8e7a-b43071683c6d"
                ,"Con khỉ", "Monkey"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fpenguin.png?alt=media&token=d7e17a15-33dc-4796-9832-05a0058497b2"
                ,"Con chim cánh cụt", "Penguin"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Frooster.png?alt=media&token=a65b5079-e119-4deb-9b4c-ea8fdae5fab6"
                ,"Con gà", "Chicken"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fsheep.png?alt=media&token=1e3a475c-245f-4e8e-a0ef-704add3395c6"
                ,"Con cừu", "Sheep"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fsnake.png?alt=media&token=f381b4ef-2fee-413f-a573-401fb13f5d9d"
                ,"Con rắn", "Snake"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Ftiger.png?alt=media&token=2ccb754a-b9b3-49fb-8c2b-f730224b885a"
                ,"Con hổ", "Tiger"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Ffox.png?alt=media&token=0d644377-6e43-4309-a3a8-0f0c420040ac"
                ,"Con cáo", "Fox"));
        animalCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Animals%2FImage%2Fcamel.png?alt=media&token=242f6012-3cac-4d26-81cf-7c940dc419a3"
                ,"Con lạc đà", "Camel"));



        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fapple.png?alt=media&token=09354d1e-6259-403f-bcc1-76e99dc4dd40"
                ,"Quả táo", "Apple"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Forange.png?alt=media&token=2c6738f3-9f1a-4f12-a122-c21882f829c4"
                ,"Quả cam", "Orange"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fbanana.png?alt=media&token=7aed039a-1df0-4597-9a93-fdbd0193e657"
                ,"Quả chuối", "Banana"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fcherry.png?alt=media&token=0a2f7fe0-f6b2-42f5-8a65-e7fcd0e41a11"
                ,"Quả Cherry", "Cherry"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fcoconut.png?alt=media&token=1018eb89-354e-419a-ab62-e6b42316ba01"
                ,"Quả dừa", "Coconut"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fgrape.png?alt=media&token=ee9c8cc2-f582-454a-84e7-aa97360ba557"
                ,"Quả nho", "Grape"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fkiwi.png?alt=media&token=7bd41fe7-97e5-48dc-8631-39e8aaa70355"
                ,"Quả kiwi", "Kiwi"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Flemon.png?alt=media&token=98072c46-a820-40a4-b785-c9574b55a21b"
                ,"Quả chanh", "Lemon"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fpeach.png?alt=media&token=15091d0f-d259-4c51-b127-288a39256cf3"
                ,"Quả đào", "Peach"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fpear.png?alt=media&token=951f4da9-70fa-4a77-8e19-4cd4f85b00fd"
                ,"Quả lê", "Pear"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fpersimmon.png?alt=media&token=d612bdfd-0d5c-49d6-ad53-09321b0f5eea"
                ,"Quả hồng", "Persimmon"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fpineapple.png?alt=media&token=9a7d2edf-aa13-4f06-9a2c-247f1106574c"
                ,"Quả dứa", "Pineapple"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fplum.png?alt=media&token=6569f9d2-4017-4c38-bf4e-56579335b5ba"
                ,"Quả mận", "Plum"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fstrawberry.png?alt=media&token=5ebfa476-2950-4334-add3-9310351b8b2a"
                ,"Quả dâu tây", "Strawberry"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fwatermelon.png?alt=media&token=1dc5b91b-4fd0-43a4-b377-3a083d335d58"
                ,"Quả dưa hấu", "Watermelon"));
        fruitCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Fruits%2FImage%2Fmango.png?alt=media&token=655ee29b-5415-4cf3-9f98-c93e98e8d451"
                ,"Quả xoài", "Mango"));




        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fbread.png?alt=media&token=663cc766-97f1-4208-92d6-b686e06b1a6b"
                ,"Bánh mì", "Bread"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fburger.png?alt=media&token=f70a305c-69c6-4485-b888-05c9e5ac7b4c"
                ,"Burger", "Burger"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fcheese.png?alt=media&token=a44a3f44-b542-445c-bc74-de94ae439c62"
                ,"Phô mai", "Cheese"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fchocolate.png?alt=media&token=223a3ddf-0344-4944-bd2a-4c025a63374c"
                ,"Sô-cô-la", "Chocolate"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fcoffee.png?alt=media&token=f4f15f45-00b1-4547-9e06-7848f7192c5e"
                ,"Cafe", "Coffee"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fegg.png?alt=media&token=8961ca29-178a-45e3-abb2-f947abae6fba"
                ,"Trứng", "Egg"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fhoney.png?alt=media&token=12b21ee8-7958-45b7-acaa-d6c29a75b828"
                ,"Mật ong", "Honey"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fhotdog.png?alt=media&token=3c18f3d9-40ab-4077-957f-b4d6afc6635a"
                ,"Xúc xích", "Hot Dog"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Ficecream.png?alt=media&token=038d8e26-11e3-466c-ad5d-13bba7e050f1"
                ,"Kem", "Ice Cream"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fmeat.png?alt=media&token=ea70c44e-c263-4a0e-a642-09da7c0b11ec"
                ,"Thịt", "Meat"));
        foodCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Food%2FImage%2Fwater.png?alt=media&token=4048365f-b5d3-4eff-a997-bfdcda0ce578"
                ,"Nước", "Water"));



        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fblack.png?alt=media&token=bc02305a-a8f0-414f-a059-2de16b9f2599"
                ,"Màu xanh dương", "Blue"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fpink.png?alt=media&token=c3a944e7-137e-47a5-9066-6cf7f8580537"
                ,"Màu hồng", "Pink"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fgreen.png?alt=media&token=515e449d-cb03-4ab3-a1c0-a711c8785ca3"
                ,"Màu xanh lá cây", "Green"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Forange_color.png?alt=media&token=bd66c3f6-b39e-4fb0-abb8-d3982ec0cf02"
                ,"Màu cam", "Orange"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fpurple.png?alt=media&token=ce695be5-5847-4e22-aa76-b037f8557d67"
                ,"Màu tím", "Purple"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fred.png?alt=media&token=9dd9d529-89e1-41ad-b7b8-fc5c72c27e4f"
                ,"Màu đỏ", "Red"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fyellow.png?alt=media&token=7b5a63f5-4d47-4acf-9a32-fd09712355d3"
                ,"Màu vàng", "Yellow"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fbrown.png?alt=media&token=466dc200-66c5-487d-bc73-b695872f49a0"
                ,"Màu nâu", "Brown"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fgray.png?alt=media&token=2a6b1ae2-98cb-4589-b2b3-91559780fe41"
                ,"Màu xám", "Gray"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fwhite.png?alt=media&token=8355e9d1-170c-49af-81d6-495c35105d86"
                ,"Màu trắng", "White"));
        colorCate.addObject(new ObjectThing("https://firebasestorage.googleapis.com/v0/b/engkidlearnwithfun.appspot.com/o/Colors%2FImage%2Fblack.png?alt=media&token=bc02305a-a8f0-414f-a059-2de16b9f2599"
                ,"Màu đen", "Black"));






        topicList.add(alphabetCate);
        topicList.add(numberCate);
        topicList.add(fruitCate);
        topicList.add(animalCate);
        topicList.add(foodCate);
        topicList.add(colorCate);




    }




}