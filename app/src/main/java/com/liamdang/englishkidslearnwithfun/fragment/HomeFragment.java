package com.liamdang.englishkidslearnwithfun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liamdang.englishkidslearnwithfun.activity.MainActivity;
import com.liamdang.englishkidslearnwithfun.adapter.TopicAdapterFireb;
import com.liamdang.englishkidslearnwithfun.activity.CategoryFunction;
import com.liamdang.englishkidslearnwithfun.model.Topic;
import com.liamdang.englishkidslearnwithfun.model.HighScores;
import com.liamdang.englishkidslearnwithfun.model.ObjectThing;
import com.liamdang.englishkidslearnwithfun.R;
import com.liamdang.englishkidslearnwithfun.model.User;
import com.liamdang.englishkidslearnwithfun.mySQLiteHelper;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static ArrayList<Topic> topicList;
    TopicAdapterFireb adapter;
    //private Button btnGetList;
    private View view;
    ListView listView;

    //User user;
    public static User mUser;
    //private MainActivity mainActivity;


    @Override
    public void onResume() {
        super.onResume();
        //getRealtimeData();
        //updateHighScores();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        getUserData();

        getRealtimeData();



        //makeData();



        super.onCreate(savedInstanceState);


    }

    private void getUserData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = database.getReference("list_users").child(user.getUid());

        //topicList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get Post object and use the values to update the UI
                mUser = snapshot.getValue(User.class);
                //System.out.println("This is User getData");

                // ..

                getRealtimeData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail get data", Toast.LENGTH_LONG).show();

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
                if (topicList != null) {
                    topicList.clear();
                }
                int i = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Topic cate = dataSnapshot.getValue(Topic.class);


                    if( mUser != null && i < 6) {
                        cate.setHighScore(mUser.getTopicScore(i));
                        //System.out.println("This is Hello TEXT Xin chào User not null" );

                    } else {
                        //System.out.println("This is Hello TEXT Xin chào HomeFrag" );
                    }
                    topicList.add(cate);
                    i++;
                }


                adapter.notifyDataSetChanged();



            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "Fail get list", Toast.LENGTH_LONG).show();

            }
        });





    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);



    /*
        btnGetList = (Button) view.findViewById(R.id.btn_get_list_obj);
        btnGetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRealtimeData();


                System.out.println("This is Hello TEXT Xin chào" );
            }
        });

     */





        adapter = new TopicAdapterFireb( getActivity(), topicList);
        //adapter.notifyDataSetChanged();

        //attach the adapter to a Listview
        listView = (ListView) view.findViewById(R.id.listViewCards);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);




        return view;
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CategoryFunction.class);
        intent.putExtra("position", position);
        startActivity(intent);

    }

    public void updateHighScores() {
        HighScores.open(getActivity());
        for (Topic c : topicList) {
            c.updateHighScore();
        }
        HighScores.close();
        //notify adapter to display latest HighScores
        adapter.notifyDataSetChanged();
    }

    private void makeData() {
        HighScores.open(getActivity());

        topicList = new ArrayList<>();



        Topic alphabetCate = new Topic("Alphabet",
                "Chữ cái",
                R.drawable.alphabet,
                HighScores.getHighScore(mySQLiteHelper.COLUMN_ALPHABET),
                ContextCompat.getColor(getContext(), R.color.teal_700),
                R.style.PurpleTheme,
                mySQLiteHelper.COLUMN_ALPHABET);



        Topic numberCate = new Topic("Numbers",
                "Số đếm",
                R.drawable.numbers,
                HighScores.getHighScore(mySQLiteHelper.COLUMN_NUMBER),
                ContextCompat.getColor(getContext(), R.color.purple_200),
                R.style.GreenTheme,
                mySQLiteHelper.COLUMN_NUMBER);
        Topic fruitCate = new Topic( "Fruits",
                "Trái cây",
                R.drawable.fruits,
                HighScores.getHighScore(mySQLiteHelper.COLUMN_FRUITS),
                ContextCompat.getColor(getContext(), R.color.primary_dark),
                R.style.GreenTheme,
                mySQLiteHelper.COLUMN_FRUITS);
        Topic animalCate = new Topic( "Animals",
                "Động vật",
                R.drawable.animals,
                HighScores.getHighScore(mySQLiteHelper.COLUMN_ANIMALS),
                ContextCompat.getColor(getContext(), R.color.blue_primary_dark),
                R.style.BlueTheme,
                mySQLiteHelper.COLUMN_ANIMALS);
        Topic foodCate = new Topic( "Food",
                "Thức ăn",
                R.drawable.food,
                HighScores.getHighScore(mySQLiteHelper.COLUMN_FOOD),
                ContextCompat.getColor(getContext(), R.color.pink_primary_dark),
                R.style.PinkTheme,
                mySQLiteHelper.COLUMN_FOOD);
        Topic colorCate = new Topic( "Color",
                "Màu sắc",
                R.drawable.colors,
                HighScores.getHighScore(mySQLiteHelper.COLUMN_COLORS),
                ContextCompat.getColor(getContext(), R.color.purple_primary_dark),
                R.style.PurpleTheme,
                mySQLiteHelper.COLUMN_COLORS);







        HighScores.close();

        fruitCate.addObject(new ObjectThing(R.drawable.apple, R.raw.apple,"Quả táo", "Apple"));
        fruitCate.addObject(new ObjectThing(R.drawable.orange, R.raw.orange,"Quả cam", "Orange"));
        fruitCate.addObject(new ObjectThing(R.drawable.banana, R.raw.banana, "Quả chuối", "Banana"));
        fruitCate.addObject(new ObjectThing(R.drawable.cherry, R.raw.cherry, "Quả Cherry", "Cherry"));
        fruitCate.addObject(new ObjectThing(R.drawable.dates, R.raw.dates, "Quả chà là", "Dates"));
        fruitCate.addObject(new ObjectThing(R.drawable.coconut, R.raw.coconut, "Quả dừa", "Coconut"));
        fruitCate.addObject(new ObjectThing(R.drawable.grape, R.raw.grape, "Quả nho", "Grape"));
        fruitCate.addObject(new ObjectThing(R.drawable.kiwi, R.raw.kiwi, "Quả kiwi", "Kiwi"));
        fruitCate.addObject(new ObjectThing(R.drawable.lemon, R.raw.lemon, "Quả chanh", "Lemon"));
        fruitCate.addObject(new ObjectThing(R.drawable.peach, R.raw.peach, "Quả đào", "Peach"));
        fruitCate.addObject(new ObjectThing(R.drawable.pear, R.raw.pear, "Quả lê", "Pear"));
        fruitCate.addObject(new ObjectThing(R.drawable.persimmon, R.raw.persimmon, "Quả hồng", "Persimmon"));
        fruitCate.addObject(new ObjectThing(R.drawable.pineapple, R.raw.pineapple, "Quả dứa", "Pineapple"));
        fruitCate.addObject(new ObjectThing(R.drawable.plum, R.raw.plum, "Quả mận", "Plum"));
        fruitCate.addObject(new ObjectThing(R.drawable.raspberry, R.raw.raspberry, "Quả mâm xôi", "Raspberry"));
        fruitCate.addObject(new ObjectThing(R.drawable.strawberry, R.raw.strawberry, "Quả dâu tây", "Strawberry"));
        fruitCate.addObject(new ObjectThing(R.drawable.watermelon, R.raw.watermelon, "Quả dưa hấu", "Watermelon"));
        fruitCate.addObject(new ObjectThing(R.drawable.mango, R.raw.mango, "Quả xoài", "Mango"));

        animalCate.addObject(new ObjectThing( R.drawable.dog, R.raw.dog, "Con chó", "Dog", R.raw.dognoise));
        animalCate.addObject(new ObjectThing( R.drawable.bear, R.raw.bear, "Con gấu", "Bear", R.raw.bearnoise));
        animalCate.addObject(new ObjectThing( R.drawable.wolf, R.raw.wolf, "Con chó sói", "Wolf", R.raw.wolfnoise ));
        animalCate.addObject(new ObjectThing( R.drawable.dolphin, R.raw.dolphin, "Con cá heo", "Dolphin", R.raw.dolphinnoise));
        animalCate.addObject(new ObjectThing( R.drawable.duck, R.raw.duck, "Con vịt", "Duck", R.raw.ducknoise));
        animalCate.addObject(new ObjectThing( R.drawable.leopard, R.raw.leopard, "Con báo", "Leopard", R.raw.leopardnoise));
        animalCate.addObject(new ObjectThing( R.drawable.lion, R.raw.lion, "Con sư tử", "Lion", R.raw.lionnoise));
        animalCate.addObject(new ObjectThing( R.drawable.monkey, R.raw.monkey, "Con khỉ", "Monkey", R.raw.monkeynoise));
        animalCate.addObject(new ObjectThing( R.drawable.penguin, R.raw.penguin, "Con chim cánh cụt", "Penguin", R.raw.penguinnoise));
        animalCate.addObject(new ObjectThing( R.drawable.rooster, R.raw.rooster, "Con gà trống", "Rooster", R.raw.roosternoise));
        animalCate.addObject(new ObjectThing( R.drawable.sheep, R.raw.sheep, "Con cừu", "Sheep", R.raw.sheepnoise));
        animalCate.addObject(new ObjectThing( R.drawable.snake, R.raw.snake, "Con rắn", "Snake", R.raw.snakenoise));
        animalCate.addObject(new ObjectThing( R.drawable.tiger, R.raw.tiger, "Con hổ", "Tiger", R.raw.tigernoise));
        animalCate.addObject(new ObjectThing( R.drawable.fox, R.raw.fox, "Con cáo", "Fox", R.raw.foxnoise));
        animalCate.addObject(new ObjectThing( R.drawable.camel, R.raw.camel, "Con lạc đà", "Camel", R.raw.camelnoise));

        foodCate.addObject(new ObjectThing(R.drawable.bread, R.raw.bread,"Bánh mì", "Bread"));
        foodCate.addObject(new ObjectThing(R.drawable.burger, R.raw.burger, "Burger", "Burger"));
        foodCate.addObject(new ObjectThing(R.drawable.cheese, R.raw.cheese, "Phô mai", "Cheese"));
        foodCate.addObject(new ObjectThing(R.drawable.chocolate, R.raw.chocolate, "Sô-cô-la", "Chocolate"));
        foodCate.addObject(new ObjectThing(R.drawable.coffee, R.raw.coffee, "Cafe", "Coffee"));
        foodCate.addObject(new ObjectThing(R.drawable.egg, R.raw.egg, "Trứng", "Egg"));
        foodCate.addObject(new ObjectThing(R.drawable.honey, R.raw.honey, "Mật ong", "Honey"));
        foodCate.addObject(new ObjectThing(R.drawable.hotdog, R.raw.hotdog, "Xúc xích", "Hot Dog"));
        foodCate.addObject(new ObjectThing(R.drawable.icecream, R.raw.icecream, "Kem", "Ice Cream"));
        foodCate.addObject(new ObjectThing(R.drawable.meat, R.raw.meat, "Thịt", "Meat"));
        foodCate.addObject(new ObjectThing(R.drawable.pizza, R.raw.pizza, "Pizza", "Pizza"));
        foodCate.addObject(new ObjectThing(R.drawable.sandwich, R.raw.sandwich, "Sandwich", "Sandwich"));
        foodCate.addObject(new ObjectThing(R.drawable.sausage, R.raw.sausage, "Lạp xườn", "Sausage"));
        foodCate.addObject(new ObjectThing(R.drawable.water, R.raw.water, "Nước", "Water"));

        colorCate.addObject(new ObjectThing(R.drawable.blue, R.raw.blue, "Màu xanh dương", "Blue"));
        colorCate.addObject(new ObjectThing(R.drawable.pink, R.raw.pink, "Màu hồng", "Pink"));
        colorCate.addObject(new ObjectThing(R.drawable.green, R.raw.green, "Màu xanh lá cây", "Green"));
        colorCate.addObject(new ObjectThing(R.drawable.orange_color, R.raw.orange_color, "Màu cam", "Orange"));
        colorCate.addObject(new ObjectThing(R.drawable.purple, R.raw.purple, "Màu tím", "Purple"));
        colorCate.addObject(new ObjectThing(R.drawable.red, R.raw.red, "Màu đỏ", "Red"));
        colorCate.addObject(new ObjectThing(R.drawable.yellow, R.raw.yellow, "Màu vàng", "Yellow"));
        colorCate.addObject(new ObjectThing(R.drawable.brown, R.raw.brown, "Màu nâu", "Brown"));
        colorCate.addObject(new ObjectThing(R.drawable.gray, R.raw.gray, "Màu xám", "Gray"));
        colorCate.addObject(new ObjectThing(R.drawable.white, R.raw.white, "Màu trắng", "White"));
        colorCate.addObject(new ObjectThing(R.drawable.black, R.raw.black, "Màu đen", "Black"));



        alphabetCate.addObject(new ObjectThing(R.drawable.a, R.raw.a_sound, "", "A"));
        alphabetCate.addObject(new ObjectThing(R.drawable.b, R.raw.b_sound,"",  "B"));
        alphabetCate.addObject(new ObjectThing(R.drawable.c, R.raw.c_sound,"",  "C"));
        alphabetCate.addObject(new ObjectThing(R.drawable.d, R.raw.d_sound,"", "D"));
        alphabetCate.addObject(new ObjectThing(R.drawable.e, R.raw.e_sound,"", "E"));
        alphabetCate.addObject(new ObjectThing(R.drawable.f, R.raw.f_sound,"", "F"));
        alphabetCate.addObject(new ObjectThing(R.drawable.g, R.raw.g_sound,"", "G"));
        alphabetCate.addObject(new ObjectThing(R.drawable.h, R.raw.h_sound,"", "H"));
        alphabetCate.addObject(new ObjectThing(R.drawable.i, R.raw.i_sound,"", "I"));
        alphabetCate.addObject(new ObjectThing(R.drawable.j, R.raw.j_sound,"", "J"));
        alphabetCate.addObject(new ObjectThing(R.drawable.k, R.raw.k_sound,"", "K"));
        alphabetCate.addObject(new ObjectThing(R.drawable.l, R.raw.l_sound,"", "L"));
        alphabetCate.addObject(new ObjectThing(R.drawable.m, R.raw.m_sound,"", "M"));
        alphabetCate.addObject(new ObjectThing(R.drawable.n, R.raw.n_sound,"", "N"));
        alphabetCate.addObject(new ObjectThing(R.drawable.o, R.raw.o_sound,"", "O"));
        alphabetCate.addObject(new ObjectThing(R.drawable.p, R.raw.p_sound,"", "P"));
        alphabetCate.addObject(new ObjectThing(R.drawable.q, R.raw.q_sound,"", "Q"));
        alphabetCate.addObject(new ObjectThing(R.drawable.r, R.raw.r_sound,"", "R"));
        alphabetCate.addObject(new ObjectThing(R.drawable.s, R.raw.s_sound,"", "S"));
        alphabetCate.addObject(new ObjectThing(R.drawable.t, R.raw.t_sound,"", "T"));
        alphabetCate.addObject(new ObjectThing(R.drawable.u, R.raw.u_sound,"", "U"));
        alphabetCate.addObject(new ObjectThing(R.drawable.v, R.raw.v_sound,"", "V"));
        alphabetCate.addObject(new ObjectThing(R.drawable.w, R.raw.w_sound,"", "W"));
        alphabetCate.addObject(new ObjectThing(R.drawable.x, R.raw.x_sound,"", "X"));
        alphabetCate.addObject(new ObjectThing(R.drawable.y, R.raw.y_sound,"", "Y"));
        alphabetCate.addObject(new ObjectThing(R.drawable.z, R.raw.z_sound,"", "Z"));

        numberCate.addObject(new ObjectThing(R.drawable.num0, R.raw.num0_sound, "Không", "0"));
        numberCate.addObject(new ObjectThing(R.drawable.num1, R.raw.num1_sound, "Một", "1"));
        numberCate.addObject(new ObjectThing(R.drawable.num2, R.raw.num2_sound, "Hai", "2"));
        numberCate.addObject(new ObjectThing(R.drawable.num3, R.raw.num3_sound, "Ba", "3"));
        numberCate.addObject(new ObjectThing(R.drawable.num4, R.raw.num4_sound, "Bốn", "4"));
        numberCate.addObject(new ObjectThing(R.drawable.num5, R.raw.num5_sound, "Năm", "5"));
        numberCate.addObject(new ObjectThing(R.drawable.num6, R.raw.num6_sound, "Sáu", "6"));
        numberCate.addObject(new ObjectThing(R.drawable.num7, R.raw.num7_sound, "Bảy", "7"));
        numberCate.addObject(new ObjectThing(R.drawable.num8, R.raw.num8_sound, "Tám", "8"));
        numberCate.addObject(new ObjectThing(R.drawable.num9, R.raw.num9_sound, "Chín", "9"));




        topicList.add(alphabetCate);
        topicList.add(numberCate);
        topicList.add(fruitCate);
        topicList.add(animalCate);
        topicList.add(foodCate);
        topicList.add(colorCate);
        //categories.add(TestCate);

    }




}
