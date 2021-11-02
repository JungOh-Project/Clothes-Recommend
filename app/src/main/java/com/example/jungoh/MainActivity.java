package com.example.jungoh;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.jung_oh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    private GPSTracker gpsTracker;
    private FirebaseAuth firebaseAuth;


    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    TextView textview_feels_like, textview_min, textview_max, textview_symbol;
    TextView textview_date;
    TextView weather1_time, weather2_time, weather3_time, weather4_time;
    TextView temp1, temp2, temp3, temp4;
    ImageView imageView1, imageView2, imageView3, imageView4;

    ImageView img_outer, img_top, img_bottom, img_accessary;
    String feels_like[]=new String[4];
    String min[]=new String[4];
    String max[]=new String[4];
    String symbol_name[]=new String[4];
    String symbol[]= new String[4];
    String time[]= new String[4];
    String temperature[] = new String[4];
    int data[] = new int[4];
    int n;

    int set_img_btn=0;

    int show_check_tmp=0;
    int cur_temp=0;

    int symbol_num;//weather 알기 위한 xml 속 symbol의 number값 ex 800 구름
    int month;
    double _temperature;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle barDrawerToggle;
    private BackPressHandler backPressHandler = new BackPressHandler(this);
    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    //옷 종류별 리스트
    String[] spring_t_0 = new String[]{"mentomen","hoodT","anorak"};
    String[] spring_t_5 = new String[]{"mentomen","hoodT","anorak"};
    String[] spring_t_10 = new String[]{"longSleve","mentomen","onePiece", "hoodT","anorak"};
    String[] spring_t_15 = new String[]{"longSleve","onePiece","hoodT","anorak"};

    String[] summer_t_20 = new String[]{"bluse","shirt"};
    String[] summer_t_25 = new String[]{"Tshirt","bluse","onePiece"};
    String[] summer_t_30 = new String[]{"sleeveless","Tshirt","bluse","onePiece"};
    String[] summer_t_35 = new String[]{"sleeveless","Tshirt","bluse","onePiece"};

    String[] autumn_t_5 = new String[]{"hoodT","mentomen", "anorak"};
    String[] autumn_t_10 = new String[]{"hoodT","longSleve","mentomen","anorak","longSleve"};
    String[] autumn_t_15 = new String[]{"anorak","longSleve","hoodT","onePiece" };
    String[] autumn_t_20 = new String[]{"bluse","shirt11","shirt4","shirt5","shirt6","shirt7","shirt8", "shirt","shirt1","shirt10","shirt2","shirt3","shirt9"};

    String[] winter_t_m15 = new String[]{"hoodT","hoodT1","hoodT2","hoodT3","mentomen","mentomen1","mentomen2","mentomen3", "mentomen4","mentomen5","mentomen6"};
    String[] winter_t_m5 = new String[]{"mentomen4","mentomen5","mentomen6","hoodT","hoodT1","hoodT2","hoodT3","mentomen","mentomen1","mentomen2","mentomen3" };
    String[] winter_t_5 = new String[]{ "anorak","mentomen4","mentomen5","mentomen6","hoodT","hoodT1","hoodT2","hoodT3","mentomen","mentomen1","mentomen2","mentomen3" };
    String[] winter_t_10 = new String[]{"anorak","longSleve","longSleve1","longSleve2","longSleve4","longSleve6","hoodT" ,"hoodT1","hoodT2","hoodT3", "longSleve3","longSleve7","longSleve8","longSleve9","mentomen","onePiece5"};

    String[] spring_b_0 = new String[]{"jeans4", "jeans","jeans1","jeans3","jeans2"};
    String[] spring_b_5 = new String[]{"jeans2","jeans","jeans1" ,"jeans3","jeans4"};
    String[] spring_b_10 = new String[]{"jeans4","jeans","jeans1","jeans3","longSkirt","longSkirt1","longSkirt2","longSkirt5","skirt","skirt1","skirt2","jeans2","trainingSuit"};
    String[] spring_b_15 = new String[]{"jeans2","jogerPants","slacks2","slacks3","slacks5","slacks6","trainingSuit","widePants","jeans","jeans1","jeans3","longSkirt","longSkirt1","longSkirt2","longSkirt5","skirt","skirt1","skirt2","slacks","slacks1","slacks4","widePants1", "widePants2", "jeans2","jogerPants","slacks2","slacks3","slacks5","slacks6","trainingSuit","widePants"};

    String[] summer_b_20 = new String[]{"jeans4","trainingPants","jeans","jeans1","jeans3","jogerPants","skirt","skirt2","skirt3","widePants2","jeans2"};
    String[] summer_b_25 = new String[]{"pants11","pants3","pants4","pants9", "longSkirt3","longSkirt4","pants","pants10","pants2","pants5","pants6", "pants7","pants8","skirt","skirt3","trouser","trouser1","trouser2","trouser3","trouser4", "pants1","trainingPants"};
    String[] summer_b_30 = new String[]{"pants1","trainingPants","pants","pants2","pants5","pants6","pants7","pants8","skirt","skirt2","skirt3","trouser3","trouser4", "pants11","pants3","pants4","pants9"};
    String[] summer_b_35 = new String[]{ "pants1","trainingPants","pants","pants10","pants2","pants5","pants6","pants7","pants8","skirt","skirt2","skirt3","trouser3","trouser4", "pants11","pants3","pants4","pants9"};

    String[] autumn_b_5 = new String[]{"jeans4","jeans","jeans1","jeans3","jeans2"};
    String[] autumn_b_10 = new String[]{"jeans2","jeans","jeans1","jeans3","longSkirt","longSkirt1","longSkirt2","longSkirt5","skirt","jeans4"};
    String[] autumn_b_15 = new String[]{"jeans4","jeans5","jeans","jeans1","jeans3","longSkirt","longSkirt1","longSkirt2","longSkirt5","skirt","slacks", "slacks1","slacks4","widePants1","widePants2","jeans2","jogerPants","slacks2","slacks3","slacks5","slacks6","trainingSuit","widePants"};
    String[] autumn_b_20 = new String[]{"jeans2","jeans","jeans3","jogerPants1","skirt","jeans4"};

    String[] winter_b_m15 = new String[]{"jeans4","jeans","jeans1","jeans3","jeans2"};
    String[] winter_b_m5 = new String[]{"jeans2", "jeans","jeans1" ,"jeans3","jeans4"};
    String[] winter_b_5 = new String[]{"jeans4", "jeans","jeans1","jeans3","jeans2"};
    String[] winter_b_10 = new String[]{"jeans2","trainingSuit","jeans","jeans1","jeans3","longSkirt","longSkirt1","longSkirt2","longSkirt5","skirt1","skirt2","jeans4"};

    String[] spring_a_0 = new String[]{null};
    String[] spring_a_5 = new String[]{null};
    String[] spring_a_10 = new String[]{null};
    String[] spring_a_15 = new String[]{null};

    String[] summer_a_20 = new String[]{"cap","glasses","parasol","scarf","strawHat","sunglasses"};
    String[] summer_a_25 = new String[]{"cap","glasses","parasol","scarf","strawHat","sunglasses"};
    String[] summer_a_30 = new String[]{"cap","glasses","parasol","scarf","strawHat","sunglasses"};
    String[] summer_a_35 = new String[]{"cap","glasses","parasol","scarf","strawHat","sunglasses"};

    String[] autumn_a_5 = new String[]{"beanie","beret","bucketHat","cap","glasses","muffler","scarf","sunglasses"};
    String[] autumn_a_10 = new String[]{"beanie","beret","bucketHat","cap","glasses","muffler","scarf","sunglasses"};
    String[] autumn_a_15 = new String[]{"beanie","beret","bucketHat","cap","glasses","muffler","scarf","sunglasses"};
    String[] autumn_a_20 = new String[]{"beanie","beret","bucketHat","cap","glasses","muffler","scarf","sunglasses"};

    String[] winter_a_m15 = new String[]{"beanie","beret","bucketHat","cap" ,"glasses","muffler","scarf","sunglasses" };
    String[] winter_a_m5 = new String[]{"beanie","beret","bucketHat","cap","muffler","scarf","sunglasses" };
    String[] winter_a_5 = new String[]{"beanie","beret","bucketHat","cap","glasses","scarf","sunglasses"};
    String[] winter_a_10 = new String[]{"beanie","beret","bucketHat","cap","glasses","scarf","sunglasses"};

    String[] spring_o_0 = new String[]{"bbogul","coat","denimJacket","flightJacket","leatherJacket","lightweightPadding","mustang","peddedCoat","yasang", "cardigan","denimCoat","paddingCoat", "woolCoat","paddedJacket" };
    String[] spring_o_5 = new String[]{"bbogul","cardigan","coat","denimJacket","flightJacket","leatherJacket","paddingVest","shirt","trenchCoat","windBreaker"};
    String[] spring_o_10 = new String[]{"denimJacket","jacket","jersey","shirt","vest","bbogulVest","windBreaker","denimJean","hoodZipup","windBreaker"};
    String[] spring_o_15 = new String[]{"hoodZipup1","shirt","vest" };

    String[] summer_o_20 = new String[]{null};
    String[] summer_o_25 = new String[]{null};
    String[] summer_o_30 = new String[]{null};
    String[] summer_o_35 = new String[]{null};

    String[] autumn_o_5 = new String[]{"bbogul1","coat","denimJacket","flightJacket","jacket","jersey","leatherJacket","lightweightPadding","paddingVest","trenchCoat","windBreaker2","yasang"};
    String[] autumn_o_10 = new String[]{"bbogul","bbogulVest","cardigan","coat","denimJacket","jacket","paddingCoat","paddingVest","trenchCoat","windBreaker","woolenCoat","yasang","flightJacket","hoodZipup","shirt"};
    String[] autumn_o_15 = new String[]{"hoodZipup","shirt","vest"};
    String[] autumn_o_20 = new String[]{null};

    String[] winter_o_m15 = new String[]{"coat","longPadding","mustang","padding","yasang","bbogul","paddingCoat","woolenCoat"};
    String[] winter_o_m5 = new String[]{"bbogul","coat","flightJacket","longPadding","padding","yasang","mustang","paddingCoat","woolenCoat"};
    String[] winter_o_5 = new String[]{"bbogul","coat","flightJacket","hoodZipup","jacket","jersey","leaderJacket","lightweightPadding","paddingVest","trenchCoat","windBreaker","bbogulVest","cardigan","denimJacket","paddingCoat"};
    String[] winter_o_10 = new String[]{"denimJacket","hoodZipup","paddingVest","shirt","trenchCoat","windBreaker","bbogulVest","jacket","jersey"};

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();

    //최종으로 옷이 저장된 리스트
    static List<String> list_a_spring_0= new ArrayList<String>(), list_a_spring_5= new ArrayList<String>(), list_a_spring_10= new ArrayList<String>(), list_a_spring_15= new ArrayList<String>();
    static List<String> list_a_summer_20= new ArrayList<String>(), list_a_summer_25= new ArrayList<String>(), list_a_summer_30= new ArrayList<String>(), list_a_summer_35= new ArrayList<String>();
    static List<String> list_a_autumn_5= new ArrayList<String>(), list_a_autumn_10= new ArrayList<String>(), list_a_autumn_15= new ArrayList<String>(), list_a_autumn_20= new ArrayList<String>();
    static List<String> list_a_winter_m15= new ArrayList<String>(), list_a_winter_m5= new ArrayList<String>(), list_a_winter_5= new ArrayList<String>(), list_a_winter_10= new ArrayList<String>();

    static List<String> list_o_spring_0= new ArrayList<String>(), list_o_spring_5= new ArrayList<String>(), list_o_spring_10= new ArrayList<String>(), list_o_spring_15= new ArrayList<String>();
    static List<String> list_o_summer_20= new ArrayList<String>(), list_o_summer_25= new ArrayList<String>(), list_o_summer_30= new ArrayList<String>(), list_o_summer_35= new ArrayList<String>();
    static List<String> list_o_autumn_5= new ArrayList<String>(), list_o_autumn_10= new ArrayList<String>(), list_o_autumn_15= new ArrayList<String>(), list_o_autumn_20= new ArrayList<String>();
    static List<String> list_o_winter_m15= new ArrayList<String>(), list_o_winter_m5= new ArrayList<String>(), list_o_winter_5= new ArrayList<String>(), list_o_winter_10= new ArrayList<String>();

    static List<String> list_t_spring_0 = new ArrayList<String>(), list_t_spring_5 = new ArrayList<String>(), list_t_spring_10 = new ArrayList<String>(), list_t_spring_15 = new ArrayList<String>();
    static List<String> list_t_summer_20 = new ArrayList<String>(), list_t_summer_25 = new ArrayList<String>(), list_t_summer_30 = new ArrayList<String>(), list_t_summer_35 = new ArrayList<String>();
    static List<String> list_t_autumn_5 = new ArrayList<String>(), list_t_autumn_10 = new ArrayList<String>(), list_t_autumn_15 = new ArrayList<String>(), list_t_autumn_20 = new ArrayList<String>();
    static List<String> list_t_winter_m15 = new ArrayList<String>(), list_t_winter_m5 = new ArrayList<String>(), list_t_winter_5 = new ArrayList<String>(), list_t_winter_10 = new ArrayList<String>();

    static List<String> list_b_spring_0 = new ArrayList<String>(), list_b_spring_5 = new ArrayList<String>(), list_b_spring_10 = new ArrayList<String>(), list_b_spring_15 = new ArrayList<String>();
    static List<String> list_b_summer_20 = new ArrayList<String>(), list_b_summer_25 = new ArrayList<String>(), list_b_summer_30 = new ArrayList<String>(), list_b_summer_35 = new ArrayList<String>();
    static List<String> list_b_autumn_5 = new ArrayList<String>(), list_b_autumn_10 = new ArrayList<String>(), list_b_autumn_15 = new ArrayList<String>(), list_b_autumn_20 = new ArrayList<String>();
    static List<String> list_b_winter_m15 = new ArrayList<String>(), list_b_winter_m5 = new ArrayList<String>(), list_b_winter_5 = new ArrayList<String>(), list_b_winter_10 = new ArrayList<String>();

    static List<String> list_rain = new ArrayList<String>();

    //사용자 옷 넣은 리스트
    public static final List<String> recommend_data4 = new ArrayList<>();
    public static final  List<String> recommend_data1 = new ArrayList<>();
    public static final  List<String> recommend_data2 = new ArrayList<>();
    public static final  List<String> recommend_data3 = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {  checkRunTimePermission();   }


        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B9%84%EB%8B%88.jpeg?alt=media&token=d7a16cd2-6039-4b91-b26a-3992c19e4861");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%9E%A5%EA%B0%91.jpeg?alt=media&token=932b4640-eeff-4569-8c55-63ea63394a41");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%9E%A5%EA%B0%911.jpeg?alt=media&token=e7c0d1c0-dadc-4c0e-9014-d09065cd7c17");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%AA%A9%EB%8F%84%EB%A6%AC.jpeg?alt=media&token=e019811d-86f9-46ac-8059-8203209b6770");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%AA%A9%EB%8F%84%EB%A6%AC1.jpeg?alt=media&token=bcbd7bf8-4a1b-4a17-b5ce-c01947c61029");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A42.jpeg?alt=media&token=7229033d-107c-4a5d-8326-b46cb4aee8ba");
        list_a_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%ED%84%B8%EB%AA%A8%EC%9E%90.jpeg?alt=media&token=99e32185-c456-4bf7-a325-4f82313ec145");
        list_a_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%8A%A4%EC%B9%B4%ED%94%84.jpeg?alt=media&token=4a64caac-cbeb-4d93-a6f3-25d1b4b4d1a0");
        list_a_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%8A%A4%EC%B9%B4%ED%94%84.jpeg?alt=media&token=4a64caac-cbeb-4d93-a6f3-25d1b4b4d1a0");
        list_a_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%8A%A4%EC%B9%B4%ED%94%84.jpeg?alt=media&token=4a64caac-cbeb-4d93-a6f3-25d1b4b4d1a0");
        list_a_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");

        list_a_summer_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_summer_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a"  );
        list_a_summer_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%96%91%EC%82%B0.jpeg?alt=media&token=6b324f14-d9b5-42dd-8e43-a41e8b68e911");
        list_a_summer_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_summer_25.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_summer_25.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a"     );
        list_a_summer_25.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%96%91%EC%82%B0.jpeg?alt=media&token=6b324f14-d9b5-42dd-8e43-a41e8b68e911");
        list_a_summer_25.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a" );
        list_a_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%96%91%EC%82%B0.jpeg?alt=media&token=6b324f14-d9b5-42dd-8e43-a41e8b68e911");
        list_a_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_summer_35.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_summer_35.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a"   );
        list_a_summer_35.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%96%91%EC%82%B0.jpeg?alt=media&token=6b324f14-d9b5-42dd-8e43-a41e8b68e911");
        list_a_summer_35.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");

        list_a_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B9%84%EB%8B%88.jpeg?alt=media&token=d7a16cd2-6039-4b91-b26a-3992c19e4861");
        list_a_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%AA%A9%EB%8F%84%EB%A6%AC.jpeg?alt=media&token=e019811d-86f9-46ac-8059-8203209b6770");
        list_a_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B9%84%EB%8B%88.jpeg?alt=media&token=d7a16cd2-6039-4b91-b26a-3992c19e4861");
        list_a_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%AA%A9%EB%8F%84%EB%A6%AC.jpeg?alt=media&token=e019811d-86f9-46ac-8059-8203209b6770");
        list_a_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B9%84%EB%8B%88.jpeg?alt=media&token=d7a16cd2-6039-4b91-b26a-3992c19e4861");
        list_a_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%AA%A9%EB%8F%84%EB%A6%AC.jpeg?alt=media&token=e019811d-86f9-46ac-8059-8203209b6770");
        list_a_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_autumn_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B9%84%EB%8B%88.jpeg?alt=media&token=d7a16cd2-6039-4b91-b26a-3992c19e4861");
        list_a_autumn_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_autumn_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_autumn_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_autumn_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_autumn_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%AA%A9%EB%8F%84%EB%A6%AC.jpeg?alt=media&token=e019811d-86f9-46ac-8059-8203209b6770");
        list_a_autumn_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");

        list_a_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B9%84%EB%8B%88.jpeg?alt=media&token=d7a16cd2-6039-4b91-b26a-3992c19e4861");
        list_a_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%AA%A9%EB%8F%84%EB%A6%AC.jpeg?alt=media&token=e019811d-86f9-46ac-8059-8203209b6770");
        list_a_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B9%84%EB%8B%88.jpeg?alt=media&token=d7a16cd2-6039-4b91-b26a-3992c19e4861");
        list_a_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%AA%A9%EB%8F%84%EB%A6%AC.jpeg?alt=media&token=e019811d-86f9-46ac-8059-8203209b6770");
        list_a_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B9%84%EB%8B%88.jpeg?alt=media&token=d7a16cd2-6039-4b91-b26a-3992c19e4861");
        list_a_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%AA%A9%EB%8F%84%EB%A6%AC.jpeg?alt=media&token=e019811d-86f9-46ac-8059-8203209b6770");
        list_a_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");
        list_a_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B9%84%EB%8B%88.jpeg?alt=media&token=d7a16cd2-6039-4b91-b26a-3992c19e4861");
        list_a_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%A0%EB%A0%88%EB%AA%A8.jpeg?alt=media&token=efc5149c-8589-4aa9-aead-e25d31b7a39d");
        list_a_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B2%84%ED%82%B7%ED%96%87.jpeg?alt=media&token=58baa9cf-a736-4c82-9f69-bd1c562e4fb7");
        list_a_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EB%B3%BC%EC%BA%A1.jpeg?alt=media&token=ff55a112-6742-4639-867e-f71d9b389abf");
        list_a_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%95%88%EA%B2%BD.jpeg?alt=media&token=ef143129-38bd-42e4-84df-64040c1ecd7a");
        list_a_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%84%A0%EA%B8%80%EB%9D%BC%EC%8A%A4.jpeg?alt=media&token=22360316-4907-4095-bd61-967b34e64697");

        list_b_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%80.jpg?alt=media&token=e4c3d0c6-097c-4495-8d77-82b1adc95030");
        list_b_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%803.jpg?alt=media&token=e65d43f7-27c6-4269-a61e-c2ed37b0cbec");
        list_b_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%80.jpg?alt=media&token=e4c3d0c6-097c-4495-8d77-82b1adc95030");
        list_b_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%803.jpg?alt=media&token=e65d43f7-27c6-4269-a61e-c2ed37b0cbec");
        list_b_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%80.jpg?alt=media&token=e4c3d0c6-097c-4495-8d77-82b1adc95030");
        list_b_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%803.jpg?alt=media&token=e65d43f7-27c6-4269-a61e-c2ed37b0cbec");
        list_b_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EA%B8%B4%EC%B9%98%EB%A7%88.jpeg?alt=media&token=21a45ef9-9bcd-49f8-ac1f-1f9169581993");
        list_b_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B9%98%EB%A7%88.jpeg?alt=media&token=4ceb3503-31c3-4835-bdef-7b178e460a3e");
        list_b_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%805.jpeg?alt=media&token=026244af-029a-42a9-9d2e-c3ab631d41c1");
        list_b_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%99%80%EC%9D%B4%EB%93%9C%ED%8C%AC%EC%B8%A0.jpeg?alt=media&token=8fdf67f8-2528-4c0c-ae8d-74657707d230");
        list_b_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%ED%8A%B8%EB%A0%88%EC%9D%B4%EB%8B%9D%EB%B3%B5.jpeg?alt=media&token=bb2a43c7-aa59-48ad-9212-7df09d8ffb19");
        list_b_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%A1%B0%EA%B1%B0%ED%8C%AC%EC%B8%A0.jpeg?alt=media&token=c47a4709-7089-491a-a776-1c9e29adc08f");

        list_b_summer_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B9%98%EB%A7%88.jpeg?alt=media&token=4ceb3503-31c3-4835-bdef-7b178e460a3e");
        list_b_summer_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%99%80%EC%9D%B4%EB%93%9C%ED%8C%AC%EC%B8%A02.jpeg?alt=media&token=d9d5becb-794b-41f3-9ee2-abab2a4a78be");
        list_b_summer_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_summer_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%ED%8A%B8%EB%A0%88%EC%9D%B4%EB%8B%9D%EB%B0%98%EB%B0%94%EC%A7%80.jpeg?alt=media&token=f26126dc-0daa-4d34-8738-49e86ed0ad96");
        list_b_summer_25.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_summer_25.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%ED%8A%B8%EB%A0%88%EC%9D%B4%EB%8B%9D%EB%B0%98%EB%B0%94%EC%A7%80.jpeg?alt=media&token=f26126dc-0daa-4d34-8738-49e86ed0ad96");
        list_b_summer_25.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F5%EB%B6%80%EB%B0%94%EC%A7%80.jpeg?alt=media&token=1f618daa-6777-4841-833b-6745b0a1302d");
        list_b_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%ED%8A%B8%EB%A0%88%EC%9D%B4%EB%8B%9D%EB%B0%98%EB%B0%94%EC%A7%80.jpeg?alt=media&token=f26126dc-0daa-4d34-8738-49e86ed0ad96");
        list_b_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F5%EB%B6%80%EB%B0%94%EC%A7%801.jpeg?alt=media&token=c2468aa2-3fd0-45f6-9ed2-62afec14a0a7");
        list_b_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F5%EB%B6%80%EB%B0%94%EC%A7%803.jpeg?alt=media&token=b4fd4246-a4a4-49bd-8361-d5aca01405ab");
        list_b_summer_35.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_summer_35.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%ED%8A%B8%EB%A0%88%EC%9D%B4%EB%8B%9D%EB%B0%98%EB%B0%94%EC%A7%80.jpeg?alt=media&token=f26126dc-0daa-4d34-8738-49e86ed0ad96");

        list_b_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%805.jpeg?alt=media&token=026244af-029a-42a9-9d2e-c3ab631d41c1");
        list_b_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%ED%8A%B8%EB%A0%88%EC%9D%B4%EB%8B%9D%EB%B3%B5.jpeg?alt=media&token=bb2a43c7-aa59-48ad-9212-7df09d8ffb19");
        list_b_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%8A%AC%EB%9E%99%EC%8A%A42.jpeg?alt=media&token=cb321f47-89a1-4b89-8939-a058fc9e157e");
        list_b_autumn_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");

        list_b_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%801.jpeg?alt=media&token=81aa3f8e-da54-4c7c-89c1-01e2532bfa30");
        list_b_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%802.jpeg?alt=media&token=0efa27bf-1269-43e8-a769-928d7985d672");
        list_b_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%801.jpeg?alt=media&token=81aa3f8e-da54-4c7c-89c1-01e2532bfa30");
        list_b_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%802.jpeg?alt=media&token=0efa27bf-1269-43e8-a769-928d7985d672");
        list_b_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%803.jpg?alt=media&token=e65d43f7-27c6-4269-a61e-c2ed37b0cbec");
        list_b_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%802.jpeg?alt=media&token=0efa27bf-1269-43e8-a769-928d7985d672");
        list_b_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%804.jpeg?alt=media&token=b15bff52-e4a2-4325-91a5-23d6eb376ca9");
        list_b_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%ED%8A%B8%EB%A0%88%EC%9D%B4%EB%8B%9D%EB%B3%B5.jpeg?alt=media&token=bb2a43c7-aa59-48ad-9212-7df09d8ffb19");
        list_b_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EA%B8%B4%EC%B9%98%EB%A7%881.jpeg?alt=media&token=03029dea-83d6-4f03-b9a6-1092633806da");
        list_b_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%ED%95%98%EC%9D%98%2F%EC%B2%AD%EB%B0%94%EC%A7%80.jpg?alt=media&token=e4c3d0c6-097c-4495-8d77-82b1adc95030");

        list_o_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%BD%80%EA%B8%80%EC%9D%B41.jpeg?alt=media&token=fc3db186-3003-4ca8-97a2-8748298e328e");
        list_o_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%BD%94%ED%8A%B8.jpeg?alt=media&token=d2ef6f58-337a-4892-899d-abb20140ba5d");
        list_o_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%95%AD%EA%B3%B5%EC%A0%90%ED%8D%BC.jpeg?alt=media&token=05793dff-4df3-45ea-85ab-11fe5549b427");
        list_o_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%A1%B1%ED%8C%A8%EB%94%A9.jpeg?alt=media&token=3d800f24-7a84-4bed-a5c7-ab50f9e9e9c8");
        list_o_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%AC%B4%EC%8A%A4%ED%83%952.jpeg?alt=media&token=2b7ec4ff-55b2-4c92-b740-c8a4b0dff462");
        list_o_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%8C%A8%EB%94%A9.jpeg?alt=media&token=7d300b54-fde3-4b39-905d-2a7e6210762c");
        list_o_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%95%BC%EC%83%81.jpeg?alt=media&token=1bc08933-3221-488a-a99a-80b8c818a20e");
        list_o_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%BD%80%EA%B8%80%EC%9D%B41.jpeg?alt=media&token=fc3db186-3003-4ca8-97a2-8748298e328e");
        list_o_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%BD%94%ED%8A%B8.jpeg?alt=media&token=d2ef6f58-337a-4892-899d-abb20140ba5d");
        list_o_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%A1%B1%ED%8C%A8%EB%94%A9.jpeg?alt=media&token=3d800f24-7a84-4bed-a5c7-ab50f9e9e9c8");
        list_o_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%AC%B4%EC%8A%A4%ED%83%952.jpeg?alt=media&token=2b7ec4ff-55b2-4c92-b740-c8a4b0dff462");
        list_o_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%95%BC%EC%83%81.jpeg?alt=media&token=1bc08933-3221-488a-a99a-80b8c818a20e");
        list_o_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%B2%AD%EC%9E%90%EC%BC%93.jpeg?alt=media&token=ac8bafc7-e519-4782-8ab1-00f4da9a991f");
        list_o_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%9B%84%EB%93%9C%EC%A7%91%EC%97%851.jpeg?alt=media&token=6f8b2422-6b3a-4cca-8f96-4b2cf2ff211c");
        list_o_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%9E%90%EC%BC%93.jpeg?alt=media&token=a57099c7-9639-4541-89ec-7ae3c3db2b45");
        list_o_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%A0%B8%EC%A7%80.jpeg?alt=media&token=2608d842-0974-4ffc-aba3-1ee0e0d857f3");
        list_o_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%8C%A8%EB%94%A9%EC%A1%B0%EB%81%BC3.jpeg?alt=media&token=ee4a9e60-8a6f-4d65-99e6-322b361bb572");
        list_o_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%85%94%EC%B8%A02.jpeg?alt=media&token=7cff683f-c1b7-4ff7-9f8c-4172de5e4b57");
        list_o_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%8A%B8%EB%A0%8C%EC%B9%98%EC%BD%94%ED%8A%B81.jpeg?alt=media&token=18722772-1fbe-461e-95fb-f8cc6cb21006");
        list_o_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%B0%94%EB%9E%8C%EB%A7%89%EC%9D%B42.jpeg?alt=media&token=268e8cd1-a7bb-4d70-97e7-2d58a48e873b");
        list_o_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%BD%80%EA%B8%80%EC%9D%B41.jpeg?alt=media&token=fc3db186-3003-4ca8-97a2-8748298e328e");
        list_o_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%BD%94%ED%8A%B8.jpeg?alt=media&token=d2ef6f58-337a-4892-899d-abb20140ba5d");
        list_o_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%B2%AD%EC%9E%90%EC%BC%93.jpeg?alt=media&token=ac8bafc7-e519-4782-8ab1-00f4da9a991f");
        list_o_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%95%AD%EA%B3%B5%EC%A0%90%ED%8D%BC.jpeg?alt=media&token=05793dff-4df3-45ea-85ab-11fe5549b427");
        list_o_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%95%AD%EA%B3%B5%EC%A0%90%ED%8D%BC.jpeg?alt=media&token=05793dff-4df3-45ea-85ab-11fe5549b427");
        list_o_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EA%B0%80%EC%A3%BD%EC%9E%90%EC%BC%931.jpeg?alt=media&token=dadfa398-3030-4c22-8243-e5b5cbea9dfd");
        list_o_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%8A%B8%EB%A0%8C%EC%B9%98%EC%BD%94%ED%8A%B8.jpeg?alt=media&token=83b63054-d2a9-43b0-9ffd-f7e886c3ab97");
        list_o_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%B0%94%EB%9E%8C%EB%A7%89%EC%9D%B42.jpeg?alt=media&token=268e8cd1-a7bb-4d70-97e7-2d58a48e873b");

        list_o_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%BD%80%EA%B8%80%EC%9D%B41.jpeg?alt=media&token=fc3db186-3003-4ca8-97a2-8748298e328e");
        list_o_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%BD%94%ED%8A%B8.jpeg?alt=media&token=d2ef6f58-337a-4892-899d-abb20140ba5d");
        list_o_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%B2%AD%EC%9E%90%EC%BC%93.jpeg?alt=media&token=ac8bafc7-e519-4782-8ab1-00f4da9a991f");
        list_o_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%95%AD%EA%B3%B5%EC%A0%90%ED%8D%BC3.jpeg?alt=media&token=50cac069-d56a-4b89-a0e3-f70c144986bc");
        list_o_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%AC%B4%EC%8A%A4%ED%83%954.jpeg?alt=media&token=ca9c309d-7b90-4b33-9827-d9b2c4f639e5");
        list_o_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%8C%A8%EB%94%A9.jpeg?alt=media&token=7d300b54-fde3-4b39-905d-2a7e6210762c");
        list_o_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%B2%AD%EC%9E%90%EC%BC%93.jpeg?alt=media&token=ac8bafc7-e519-4782-8ab1-00f4da9a991f");
        list_o_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%9B%84%EB%93%9C%EC%A7%91%EC%97%851.jpeg?alt=media&token=6f8b2422-6b3a-4cca-8f96-4b2cf2ff211c");
        list_o_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%9E%90%EC%BC%931.jpeg?alt=media&token=8e5e720b-1c1d-4e38-a8f2-a4974577ffd4");
        list_o_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%85%94%EC%B8%A0.jpeg?alt=media&token=f0f255a1-1c49-49d6-a1a5-e58178828299");
        list_o_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%B0%94%EB%9E%8C%EB%A7%89%EC%9D%B42.jpeg?alt=media&token=268e8cd1-a7bb-4d70-97e7-2d58a48e873b");
        list_o_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%9B%84%EB%93%9C%EC%A7%91%EC%97%851.jpeg?alt=media&token=6f8b2422-6b3a-4cca-8f96-4b2cf2ff211c");
        list_o_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%85%94%EC%B8%A0.jpeg?alt=media&token=f0f255a1-1c49-49d6-a1a5-e58178828299");
        list_o_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%85%94%EC%B8%A01.jpeg?alt=media&token=94b99662-83fe-46cc-b72e-8811c006a3e4");
        list_o_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%A1%B0%EB%81%BC.jpeg?alt=media&token=428af2ad-fc96-466a-bb2c-5e7c08e4b0e2");
        list_o_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%BD%80%EA%B8%80%EC%9D%B41.jpeg?alt=media&token=fc3db186-3003-4ca8-97a2-8748298e328e");
        list_o_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%BD%94%ED%8A%B81.jpeg?alt=media&token=35f30d66-7144-44af-be23-8104dd0d0aeb");
        list_o_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%95%AD%EA%B3%B5%EC%A0%90%ED%8D%BC.jpeg?alt=media&token=05793dff-4df3-45ea-85ab-11fe5549b427");
        list_o_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EA%B0%80%EC%A3%BD%EC%9E%90%EC%BC%931.jpeg?alt=media&token=dadfa398-3030-4c22-8243-e5b5cbea9dfd");
        list_o_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EA%B2%BD%EB%9F%89%ED%8C%A8%EB%94%A9.jpeg?alt=media&token=4ab3a2e7-4bd9-4d2c-a8dd-9f9bd9d83682");
        list_o_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%B0%94%EB%9E%8C%EB%A7%89%EC%9D%B42.jpeg?alt=media&token=268e8cd1-a7bb-4d70-97e7-2d58a48e873b");

        list_o_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EB%BD%80%EA%B8%80%EC%9D%B41.jpeg?alt=media&token=fc3db186-3003-4ca8-97a2-8748298e328e");
        list_o_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%BD%94%ED%8A%B81.jpeg?alt=media&token=35f30d66-7144-44af-be23-8104dd0d0aeb");
        list_o_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%B2%AD%EC%9E%90%EC%BC%93.jpeg?alt=media&token=ac8bafc7-e519-4782-8ab1-00f4da9a991f");
        list_o_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EA%B0%80%EC%A3%BD%EC%9E%90%EC%BC%931.jpeg?alt=media&token=dadfa398-3030-4c22-8243-e5b5cbea9dfd");
        list_o_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%8A%B8%EB%A0%8C%EC%B9%98%EC%BD%94%ED%8A%B82.jpeg?alt=media&token=ade7e2d2-e59b-451f-bcea-6a03a6ba8b01");
        list_o_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%95%BC%EC%83%81.jpeg?alt=media&token=1bc08933-3221-488a-a99a-80b8c818a20e");
        list_o_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%B2%AD%EC%9E%90%EC%BC%93.jpeg?alt=media&token=ac8bafc7-e519-4782-8ab1-00f4da9a991f");
        list_o_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%9B%84%EB%93%9C%EC%A7%91%EC%97%852.jpeg?alt=media&token=05a2740a-cfca-459e-bd59-4e4732b57786");
        list_o_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%8C%A8%EB%94%A9%EC%A1%B0%EB%81%BC3.jpeg?alt=media&token=ee4a9e60-8a6f-4d65-99e6-322b361bb572");
        list_o_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%A1%B0%EB%81%BC.jpeg?alt=media&token=428af2ad-fc96-466a-bb2c-5e7c08e4b0e2");
        list_o_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%ED%9B%84%EB%93%9C%EC%A7%91%EC%97%851.jpeg?alt=media&token=6f8b2422-6b3a-4cca-8f96-4b2cf2ff211c");
        list_o_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%85%94%EC%B8%A0.jpeg?alt=media&token=f0f255a1-1c49-49d6-a1a5-e58178828299");
        list_o_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%A1%B0%EB%81%BC.jpeg?alt=media&token=428af2ad-fc96-466a-bb2c-5e7c08e4b0e2");
        list_o_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%84%EC%9A%B0%ED%84%B0%2F%EC%85%94%EC%B8%A0.jpeg?alt=media&token=f0f255a1-1c49-49d6-a1a5-e58178828299");

        list_t_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%A7%A8%ED%88%AC%EB%A7%A82.jpeg?alt=media&token=38576b85-77d0-4a65-89c0-6b808c1ea931");
        list_t_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%ED%9B%84%EB%93%9C%ED%8B%B02.jpeg?alt=media&token=cb4ec6ea-2863-4b8f-972c-6a90062b0328");
        list_t_winter_m5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%A7%A8%ED%88%AC%EB%A7%A84.jpeg?alt=media&token=4a0558bb-12d9-4eaf-8f2a-82f308e59d68");
        list_t_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%ED%9B%84%EB%93%9C%ED%8B%B02.jpeg?alt=media&token=cb4ec6ea-2863-4b8f-972c-6a90062b0328");
        list_t_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%A7%A8%ED%88%AC%EB%A7%A83.jpeg?alt=media&token=f2214dec-9dbc-4c79-8e68-e187271feac3");
        list_t_winter_m15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%A7%A8%ED%88%AC%EB%A7%A84.jpeg?alt=media&token=4a0558bb-12d9-4eaf-8f2a-82f308e59d68");
        list_t_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EC%95%84%EB%85%B8%EB%9D%BD.jpeg?alt=media&token=5a5582f1-b9a7-495f-bb74-c4e9d0a6fe27");
        list_t_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%ED%9B%84%EB%93%9C%ED%8B%B02.jpeg?alt=media&token=cb4ec6ea-2863-4b8f-972c-6a90062b0328");
        list_t_winter_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EA%B8%B4%ED%8C%94.jpeg?alt=media&token=54468234-66f4-4bcb-a250-b37d3d516f59");
        list_t_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EC%95%84%EB%85%B8%EB%9D%BD.jpeg?alt=media&token=5a5582f1-b9a7-495f-bb74-c4e9d0a6fe27");
        list_t_winter_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%ED%9B%84%EB%93%9C%ED%8B%B02.jpeg?alt=media&token=cb4ec6ea-2863-4b8f-972c-6a90062b0328");

        list_t_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EC%95%84%EB%85%B8%EB%9D%BD.jpeg?alt=media&token=5a5582f1-b9a7-495f-bb74-c4e9d0a6fe27");
        list_t_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%A7%A8%ED%88%AC%EB%A7%A84.jpeg?alt=media&token=4a0558bb-12d9-4eaf-8f2a-82f308e59d68");
        list_t_spring_0.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%A7%A8%ED%88%AC%EB%A7%A85.jpeg?alt=media&token=074f0397-2230-4fab-b951-e11fd273bfd8");
        list_t_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EC%95%84%EB%85%B8%EB%9D%BD.jpeg?alt=media&token=5a5582f1-b9a7-495f-bb74-c4e9d0a6fe27");
        list_t_spring_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EA%B8%B4%ED%8C%944.jpeg?alt=media&token=205f306c-cedf-4ef0-b2ff-39a06d7493b8");
        list_t_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EA%B8%B4%ED%8C%94.jpeg?alt=media&token=54468234-66f4-4bcb-a250-b37d3d516f59");
        list_t_spring_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EA%B8%B4%ED%8C%946.jpeg?alt=media&token=31922d45-274c-430d-b7f0-63c3407d9e8c");
        list_t_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EC%95%84%EB%85%B8%EB%9D%BD.jpeg?alt=media&token=5a5582f1-b9a7-495f-bb74-c4e9d0a6fe27");
        list_t_spring_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%A7%A8%ED%88%AC%EB%A7%A84.jpeg?alt=media&token=4a0558bb-12d9-4eaf-8f2a-82f308e59d68");

        list_t_summer_25.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%B0%98%ED%8C%94%ED%8B%B0.jpeg?alt=media&token=6dd9a774-a45f-44f1-8c61-95faf0dc9fac");
        list_t_summer_25.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%B0%98%ED%8C%94%ED%8B%B013.jpeg?alt=media&token=847a8039-35ab-416d-954b-5fe5ff3623ae");
        list_t_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%B0%98%ED%8C%94%ED%8B%B0.jpeg?alt=media&token=6dd9a774-a45f-44f1-8c61-95faf0dc9fac");
        list_t_summer_30.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%B0%98%ED%8C%94%ED%8B%B013.jpeg?alt=media&token=847a8039-35ab-416d-954b-5fe5ff3623ae");
        list_t_summer_35.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%B0%98%ED%8C%94%ED%8B%B0.jpeg?alt=media&token=6dd9a774-a45f-44f1-8c61-95faf0dc9fac");

        list_t_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EC%95%84%EB%85%B8%EB%9D%BD.jpeg?alt=media&token=5a5582f1-b9a7-495f-bb74-c4e9d0a6fe27");
        list_t_autumn_10.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EA%B8%B4%ED%8C%941.jpeg?alt=media&token=e4834612-4045-4cf5-8642-0a7135cd0ba8");
        list_t_autumn_15.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EA%B8%B4%ED%8C%944.jpeg?alt=media&token=205f306c-cedf-4ef0-b2ff-39a06d7493b8");
        list_t_autumn_20.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EC%85%94%EC%B8%A010.jpeg?alt=media&token=6e3b072f-9999-4895-9b75-9567315a40fc");
        list_t_autumn_5.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%83%81%EC%9D%98%2F%EB%A7%A8%ED%88%AC%EB%A7%A84.jpeg?alt=media&token=4a0558bb-12d9-4eaf-8f2a-82f308e59d68");

        list_rain.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%9A%B0%EB%B9%84.jpeg?alt=media&token=7f29cd2e-2ee3-4ce3-bada-fc70a3efec4f");
        list_rain.add("https://firebasestorage.googleapis.com/v0/b/jungoh-87a7b.appspot.com/o/%EC%95%85%EC%84%B8%EC%82%AC%EB%A6%AC%2F%EC%9A%B0%EC%82%B0.jpeg?alt=media&token=7299db74-3258-4e39-ab55-6ea28c3b5db3");


        navigationView=findViewById(R.id.nav);
        drawerLayout=findViewById(R.id.add_clothes);
        findViewById(R.id.logout).setOnClickListener(onClickListener);
        findViewById(R.id.delete_account).setOnClickListener(onClickListener);
        findViewById(R.id.setting).setOnClickListener(onClickListener);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.menu_home:
                        myStartActivity(MainActivity.class);//고쳐야함
                        finish();
                        break;

                    case R.id.menu_addClothes:
                        myStartActivity(AddClothesActivity.class);
                        finish();
                        break;

                    case R.id.menu_myPage:
                        myStartActivity(MyCloset.class);
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });

        //사용자 사진 리스트에 담기
        final Random rand = new Random();
        FirebaseDatabase database;
        database = FirebaseDatabase.getInstance();
        //1
        final ValueEventListener Outer_spring = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot1.getValue(VerticalData.class);
                    recommend_data1.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data1.size()>0){
                int choose = rand.nextInt(recommend_data1.size());
                    if(month == 3 || month == 4 || month == 5){
                        if(0<=_temperature && _temperature <=15){
                            LoadImage loadImage = new LoadImage(recommend_data1.get(choose));
                            img_outer.setImageBitmap(loadImage.getBitmap());
                        }
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data1.clear();
        database.getReference().child("Outer_"+uid).child("spring").addValueEventListener(Outer_spring);

        final ValueEventListener Outer_summer = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot1.getValue(VerticalData.class);
                    recommend_data1.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data1.size()>0){
                int choose = rand.nextInt(recommend_data1.size());
                    if(month == 6 || month == 7 || month == 8){
                        if(20<=_temperature && _temperature <=40){
                            LoadImage loadImage = new LoadImage(recommend_data1.get(choose));
                            img_outer.setImageBitmap(loadImage.getBitmap());
                        }
                    }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data1.clear();
        database.getReference().child("Outer_"+uid).child("summer").addValueEventListener(Outer_summer);

        final ValueEventListener Outer_autumn = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot1.getValue(VerticalData.class);
                    recommend_data1.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data1.size()>0){
                int choose = rand.nextInt(recommend_data1.size());
                    if(month == 9 || month == 10 || month == 11){
                        if(5<=_temperature && _temperature <=25){
                            LoadImage loadImage = new LoadImage(recommend_data1.get(choose));
                            img_outer.setImageBitmap(loadImage.getBitmap());
                        }
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data1.clear();
        database.getReference().child("Outer_"+uid).child("autumn").addValueEventListener(Outer_autumn);

        final ValueEventListener Outer_winter = new ValueEventListener() {@Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                VerticalData verticalData = snapshot1.getValue(VerticalData.class);
                recommend_data1.add(verticalData.imageURL);
                set_img_btn++;
            }
            if(recommend_data1.size()>0){
            int choose = rand.nextInt(recommend_data1.size());
                if(month == 12 || month == 1 || month == 2){
                    if(-15<=_temperature && _temperature <=15){
                        LoadImage loadImage = new LoadImage(recommend_data1.get(choose));
                        img_outer.setImageBitmap(loadImage.getBitmap());
                    }
            }}
        }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data1.clear();
        database.getReference().child("Outer_"+uid).child("winter").addValueEventListener(Outer_winter);


        //2
        final ValueEventListener Top_spring = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data2.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data2.size()>0){
                int choose = rand.nextInt(recommend_data2.size());
                    if(month == 3 || month == 4 || month == 5){
                        if(0<=_temperature && _temperature <=20){
                            LoadImage loadImage = new LoadImage(recommend_data2.get(choose));
                            img_outer.setImageBitmap(loadImage.getBitmap());
                        }
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data2.clear();
        database.getReference().child("Top_"+uid).child("spring").addValueEventListener(Top_spring);

        final ValueEventListener Top_summer = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data2.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data2.size()>0){
                int choose = rand.nextInt(recommend_data2.size());
                    if(month == 6 || month == 7 || month == 8){
                        if(20<=_temperature && _temperature <=40){
                            LoadImage loadImage = new LoadImage(recommend_data2.get(choose));
                            img_top.setImageBitmap(loadImage.getBitmap());
                        }
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data2.clear();
        database.getReference().child("Top_"+uid).child("summer").addValueEventListener(Top_summer);

        final ValueEventListener Top_autumn = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data2.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data2.size()>0){
                int choose = rand.nextInt(recommend_data2.size());
                    if(month == 9 || month == 10 || month == 11){
                        if(5<=_temperature && _temperature <=25){
                            LoadImage loadImage = new LoadImage(recommend_data2.get(choose));
                            img_top.setImageBitmap(loadImage.getBitmap());
                        }
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data2.clear();
        database.getReference().child("Top_"+uid).child("autumn").addValueEventListener(Top_autumn);

        final ValueEventListener Top_winter = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data2.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data2.size()>0){
                int choose = rand.nextInt(recommend_data2.size());
                    if(month == 12 || month == 1 || month == 2){
                        LoadImage loadImage = new LoadImage(recommend_data2.get(choose));
                        img_top.setImageBitmap(loadImage.getBitmap());
                    }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data2.clear();
        database.getReference().child("Top_"+uid).child("winter").addValueEventListener(Top_winter);

        //3
        final ValueEventListener Bottom_spring = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data2.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data3.size()>0){
                int choose = rand.nextInt(recommend_data2.size());
                    if(month == 3 || month == 4 || month == 5){
                        LoadImage loadImage = new LoadImage(recommend_data3.get(choose));
                        img_top.setImageBitmap(loadImage.getBitmap());
                    }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data3.clear();
        database.getReference().child("Bottom_"+uid).child("spring").addValueEventListener(Bottom_spring);

        final ValueEventListener Bottom_summer = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data3.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data3.size()>0){
                int choose = rand.nextInt(recommend_data3.size());
                    if(month == 6 || month == 7 || month == 8){
                        LoadImage loadImage = new LoadImage(recommend_data3.get(choose));
                        img_bottom.setImageBitmap(loadImage.getBitmap());
                    }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data3.clear();
        database.getReference().child("Bottom_"+uid).child("summer").addValueEventListener(Bottom_summer);

        final ValueEventListener Bottom_autumn = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data3.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data3.size()>0){
                int choose = rand.nextInt(recommend_data3.size());
                    if(month == 9 || month == 10 || month == 11){
                        LoadImage loadImage = new LoadImage(recommend_data3.get(choose));
                        img_bottom.setImageBitmap(loadImage.getBitmap());
                    }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data3.clear();
        database.getReference().child("Bottom_"+uid).child("autumn").addValueEventListener(Bottom_autumn);

        final ValueEventListener Bottom_winter = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data3.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data3.size()>0){
                int choose = rand.nextInt(recommend_data3.size());
                    if(month == 12 || month == 1 || month == 2){
                        LoadImage loadImage = new LoadImage(recommend_data3.get(choose));
                        img_bottom.setImageBitmap(loadImage.getBitmap());
                    }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data3.clear();
        database.getReference().child("Bottom_"+uid).child("winter").addValueEventListener(Bottom_winter);

        //4
        final ValueEventListener Accessary_spring = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data4.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data4.size()>0){
                int choose = rand.nextInt(recommend_data4.size());
                if(!(symbol_num == 200 || symbol_num==201 || symbol_num==202 || symbol_num==230 || symbol_num==231 || symbol_num==232 ||
                        (300 <= symbol_num && symbol_num <= 321)|| (500<= symbol_num && symbol_num <=531) || (600 <= symbol_num && symbol_num<= 622))){
                        if(month == 3 || month == 4 || month == 5){
                            LoadImage loadImage = new LoadImage(recommend_data4.get(choose));
                            img_bottom.setImageBitmap(loadImage.getBitmap());
                        }
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data4.clear();
        database.getReference().child("Accessary_"+uid).child("spring").addValueEventListener(Accessary_spring);

        final ValueEventListener Accessary_summer = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data4.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data4.size()>0){
                int choose = rand.nextInt(recommend_data4.size());
                if(!(symbol_num == 200 || symbol_num==201 || symbol_num==202 || symbol_num==230 || symbol_num==231 || symbol_num==232 ||
                        (300 <= symbol_num && symbol_num <= 321)|| (500<= symbol_num && symbol_num <=531) || (600 <= symbol_num && symbol_num<= 622))){
                        if(month == 6 || month == 7 || month == 8){
                            LoadImage loadImage = new LoadImage(recommend_data4.get(choose));
                            img_accessary.setImageBitmap(loadImage.getBitmap());
                        }
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data4.clear();
        database.getReference().child("Accessary_"+uid).child("summer").addValueEventListener(Accessary_summer);

        final ValueEventListener Accessary_autumn = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data4.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data4.size()>0){
                int choose = rand.nextInt(recommend_data4.size());
                if(!(symbol_num == 200 || symbol_num==201 || symbol_num==202 || symbol_num==230 || symbol_num==231 || symbol_num==232 ||
                        (300 <= symbol_num && symbol_num <= 321)|| (500<= symbol_num && symbol_num <=531) || (600 <= symbol_num && symbol_num<= 622))){
                        if(month == 9 || month == 10 || month == 11){
                            LoadImage loadImage = new LoadImage(recommend_data4.get(choose));
                            img_accessary.setImageBitmap(loadImage.getBitmap());
                        }
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        database.getReference().child("Accessary_"+uid).child("autumn").addValueEventListener(Accessary_autumn);

        final ValueEventListener Accessary_winter = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    recommend_data4.add(verticalData.imageURL);
                    set_img_btn++;
                }
                if(recommend_data4.size()>0){
                int choose = rand.nextInt(recommend_data4.size());
                if(!(symbol_num == 200 || symbol_num==201 || symbol_num==202 || symbol_num==230 || symbol_num==231 || symbol_num==232 ||
                        (300 <= symbol_num && symbol_num <= 321)|| (500<= symbol_num && symbol_num <=531) || (600 <= symbol_num && symbol_num<= 622))){
                        if(month == 12 || month == 1 || month == 2){
                            LoadImage loadImage = new LoadImage(recommend_data4.get(choose));
                            img_accessary.setImageBitmap(loadImage.getBitmap());
                        }
                }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        recommend_data4.clear();
        database.getReference().child("Accessary_"+uid).child("winter").addValueEventListener(Accessary_winter);



        Button buttonOpen= (Button)findViewById(R.id.open_button);
        buttonOpen.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.add_clothes) ;
                if (!drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.openDrawer(Gravity.LEFT) ;
                }
            }
        });

        final TextView textview_address = (TextView)findViewById(R.id.place);
        textview_date = (TextView)findViewById(R.id.date);

        textview_feels_like = (TextView)findViewById(R.id.feels_like);
        textview_min = (TextView)findViewById(R.id.min);
        textview_max = (TextView)findViewById(R.id.max);
        textview_symbol = (TextView)findViewById(R.id.symbol_name);
        weather1_time = (TextView)findViewById(R.id.weather1_time);
        weather2_time = (TextView)findViewById(R.id.weather2_time);
        weather3_time = (TextView)findViewById(R.id.weather3_time);
        weather4_time = (TextView)findViewById(R.id.weather4_time);
        temp1 = (TextView)findViewById(R.id.temp1);
        temp2 = (TextView)findViewById(R.id.temp2);
        temp3 = (TextView)findViewById(R.id.temp3);
        temp4 = (TextView)findViewById(R.id.temp4);
        imageView1 = (ImageView)findViewById(R.id.weather1);
        imageView2 = (ImageView)findViewById(R.id.weather2);
        imageView3 = (ImageView)findViewById(R.id.weather3);
        imageView4 = (ImageView)findViewById(R.id.weather4);

        gpsTracker = new GPSTracker(MainActivity.this);

        final double[] latitude = {gpsTracker.getLatitude()};
        final double[] longitude = {gpsTracker.getLongitude()};

        final String key = "317e485dca3d2a93129435b7b6f7eea7";
        final String[] url = {"http://api.openweathermap.org/data/2.5/forecast?"};
        url[0] +="lat="+ latitude[0];
        url[0] +="&lon="+ longitude[0];
        url[0] +="&mode=xml&units=metric&appid="+key;

        final String[] address = {getCurrentAddress(latitude[0], longitude[0])};
        textview_address.setText(address[0]);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");
        Date now = new Date();
        String time_str = formatter.format(now);
        textview_date.setText(time_str);

        n = Integer.parseInt(time_str.substring(5,7)+time_str.substring(8,10)+time_str.substring(12,14));
        ShowTimeMethod();
        month = Integer.parseInt(formatter.format(now).substring(5,7));


        OpenAPI dust = new OpenAPI(url[0]);
        dust.execute();


        Button ShowLocationButton = (Button) findViewById(R.id.re);
        ShowLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View arg0)
            {

                latitude[0] = gpsTracker.getLatitude();
                longitude[0] = gpsTracker.getLongitude();

                url[0] = "http://api.openweathermap.org/data/2.5/forecast?";
                url[0] +="lat="+ latitude[0];
                url[0] +="&lon="+ longitude[0];
                url[0] +="&mode=xml&units=metric&appid="+key;


                address[0] = getCurrentAddress(latitude[0], longitude[0]);
                textview_address.setText(address[0]);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");
                Date now = new Date();
                String time_str = formatter.format(now);
                textview_date.setText(time_str);

                n = Integer.parseInt(time_str.substring(5,7)+time_str.substring(8,10)+time_str.substring(12,14));
                OpenAPI dust = new OpenAPI(url[0]);
                dust.execute();

                textview_feels_like.setText(feels_like[0]+"º");
                textview_min.setText("lowest : "+min[0]+"º");
                textview_max.setText("highest : "+max[0]+"º");
                textview_symbol.setText("weather : "+symbol_name[0]);


                weather1_time.setText(time[0]);
                weather2_time.setText(time[1]);
                weather3_time.setText(time[2]);
                weather4_time.setText(time[3]);

                temp1.setText(temperature[0]+"º");
                temp2.setText(temperature[1]+"º");
                temp3.setText(temperature[2]+"º");
                temp4.setText(temperature[3]+"º");

                if(temperature[0]!=null) _temperature=Double.parseDouble(temperature[0]);

                imageView1.setImageResource(data[0]);
                imageView2.setImageResource(data[1]);
                imageView3.setImageResource(data[2]);
                imageView4.setImageResource(data[3]);


                FirebaseDatabase database;
                database = FirebaseDatabase.getInstance();
                recommend_data1.clear();
                database.getReference().child("Outer_"+uid).child("spring").addValueEventListener(Outer_spring);
                recommend_data1.clear();
                database.getReference().child("Outer_"+uid).child("summer").addValueEventListener(Outer_summer);
                recommend_data1.clear();
                database.getReference().child("Outer_"+uid).child("autumn").addValueEventListener(Outer_autumn);
                recommend_data1.clear();
                database.getReference().child("Outer_"+uid).child("winter").addValueEventListener(Outer_winter);
                recommend_data2.clear();
                database.getReference().child("Top_"+uid).child("spring").addValueEventListener(Top_spring);
                recommend_data2.clear();
                database.getReference().child("Top_"+uid).child("summer").addValueEventListener(Top_summer);
                recommend_data2.clear();
                database.getReference().child("Top_"+uid).child("autumn").addValueEventListener(Top_autumn);
                recommend_data2.clear();
                database.getReference().child("Top_"+uid).child("winter").addValueEventListener(Top_winter);
                recommend_data3.clear();
                database.getReference().child("Bottom_"+uid).child("spring").addValueEventListener(Bottom_spring);
                recommend_data3.clear();
                database.getReference().child("Bottom_"+uid).child("summer").addValueEventListener(Bottom_summer);
                recommend_data3.clear();
                database.getReference().child("Bottom_"+uid).child("autumn").addValueEventListener(Bottom_autumn);
                recommend_data3.clear();
                database.getReference().child("Bottom_"+uid).child("winter").addValueEventListener(Bottom_winter);
                recommend_data4.clear();
                database.getReference().child("Accessary_"+uid).child("spring").addValueEventListener(Accessary_spring);
                recommend_data4.clear();
                database.getReference().child("Accessary_"+uid).child("summer").addValueEventListener(Accessary_summer);
                recommend_data4.clear();
                database.getReference().child("Accessary_"+uid).child("autumn").addValueEventListener(Accessary_autumn);
                recommend_data4.clear();
                database.getReference().child("Accessary_"+uid).child("winter").addValueEventListener(Accessary_winter);

                set_img_btn=1;

            }
        });


        Button left_btn = (Button) findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                cur_temp--;
                if(cur_temp<0){
                    Toast.makeText(MainActivity.this,"이전 항목이 없습니다.",Toast.LENGTH_LONG).show();
                    cur_temp++;
                }
                else{
                    textview_feels_like.setText(feels_like[cur_temp]+"º");
                    textview_min.setText("lowest : "+min[cur_temp]+"º");
                    textview_max.setText("highest : "+max[cur_temp]+"º");
                    textview_symbol.setText("weather : "+symbol_name[cur_temp]);
                }
            }
        });

        Button right_btn = (Button) findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                cur_temp++;
                if(cur_temp>=4){
                    Toast.makeText(MainActivity.this,"다음 항목이 없습니다.",Toast.LENGTH_LONG).show();
                    cur_temp--;
                }
                else{
                    textview_feels_like.setText(feels_like[cur_temp]+"º");
                    textview_min.setText("lowest : "+min[cur_temp]+"º");
                    textview_max.setText("highest : "+max[cur_temp]+"º");
                    textview_symbol.setText("weather : "+symbol_name[cur_temp]);
                }
            }
        });

//옷 추천 이미지 띄우기
        List<String >clothes_urls = view_clothes();
        Glide.with(MainActivity.this).load(clothes_urls.get(0)).placeholder(R.drawable.none_img).error(R.drawable.error_img).into(img_outer);
        Glide.with(MainActivity.this).load(clothes_urls.get(1)).placeholder(R.drawable.none_img).error(R.drawable.error_img).into(img_top);
        Glide.with(MainActivity.this).load(clothes_urls.get(2)).placeholder(R.drawable.none_img).error(R.drawable.error_img).into(img_bottom);
        //비오면 악세서리는 우산 아니면 우비
        if(symbol_num == 200 || symbol_num==201 || symbol_num==202 || symbol_num==230 || symbol_num==231 || symbol_num==232 || (300 <= symbol_num && symbol_num <= 321)
                || (500<= symbol_num && symbol_num <=531) || (600 <= symbol_num && symbol_num<= 622) ){
            int i = (int) (Math.random()*10);
            LoadImage loadImage = new LoadImage(list_rain.get(i));
            img_accessary.setImageBitmap(loadImage.getBitmap());

        }
        LoadImage loadImage = new LoadImage(clothes_urls.get(3));
        img_accessary.setImageBitmap(loadImage.getBitmap());

    }



    //옷 사진 띄우기
    class LoadImage {

        private String imgPath;
        private Bitmap bitmap;

        public LoadImage(String imgPath){
            this.imgPath = imgPath;
        }

        public Bitmap getBitmap(){
            Thread imgThread = new Thread(){
                @Override
                public void run(){
                    try {
                        URL url = new URL(imgPath);
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    }catch (IOException e){
                    }
                }
            };
            imgThread.start();
            try{
                imgThread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                return bitmap;
            }
        }

    }

    private void ShowTimeMethod() {
        final Handler handler = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void handleMessage(Message msg){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");
                Date now = new Date();
                String time_str = formatter.format(now);

                textview_date.setText(time_str);
                SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
                String time_current = formatter1.format(now);

                if(feels_like[0]!=null && show_check_tmp==0) {
                    textview_feels_like.setText(feels_like[0] + "º");
                    textview_min.setText("lowest : " + min[0] + "º");
                    textview_max.setText("highest : " + max[0] + "º");
                    textview_symbol.setText("weather : " + symbol_name[0]);
                    show_check_tmp++;
                }

                weather1_time.setText(time[0]);
                weather2_time.setText(time[1]);
                weather3_time.setText(time[2]);
                weather4_time.setText(time[3]);

                temp1.setText(temperature[0]+"º");
                temp2.setText(temperature[1]+"º");
                temp3.setText(temperature[2]+"º");
                temp4.setText(temperature[3]+"º");

                if(temperature[0]!=null) _temperature=Double.parseDouble(temperature[0]);
                if(time_current.equals("09:00:00")||time_current.equals("12:00:00")|time_current.equals("15:00:00")|time_current.equals("18:00:00")||time_current.equals("21:00:00")){
                    showNoti(time[1],temperature[1],symbol_name[1]);
                }


                if(temperature[0]!=null && set_img_btn>0){

                    List<String> clothes_urls = view_clothes();

                    Random rdm = new Random();

                    for(int i=0;i<4;i++){
                        int r = rdm.nextInt(2);
                        if(r==1){
                            switch(i){
                                case 0:
                                    Glide.with(MainActivity.this).load(clothes_urls.get(0)).placeholder(R.drawable.none_img).error(R.drawable.error_img).into(img_outer); break;
                                case 1:
                                    Glide.with(MainActivity.this).load(clothes_urls.get(1)).placeholder(R.drawable.none_img).error(R.drawable.error_img).into(img_top); break;
                                case 2:
                                    Glide.with(MainActivity.this).load(clothes_urls.get(2)).placeholder(R.drawable.none_img).error(R.drawable.error_img).into(img_bottom); break;
                                case 3:
                                    if (symbol_num == 200 || symbol_num == 201 || symbol_num == 202 || symbol_num == 230 || symbol_num == 231 || symbol_num == 232 || (300 <= symbol_num && symbol_num <= 321)
                                            || (500 <= symbol_num && symbol_num <= 531) || (600 <= symbol_num && symbol_num <= 622)) {
                                        Random rnd = new Random();
                                        int p = rnd.nextInt(2);
                                        LoadImage loadImage = new LoadImage(list_rain.get(p));
                                        img_accessary.setImageBitmap(loadImage.getBitmap());
                                    } else {
                                        LoadImage loadImage = new LoadImage(clothes_urls.get(3));
                                        img_accessary.setImageBitmap(loadImage.getBitmap());
                                    } break;
                            }
                        }
                    } set_img_btn = -100;

                    Toast.makeText(MainActivity.this, "추천 완료", Toast.LENGTH_SHORT).show();
                }
                imageView1.setImageResource(data[0]);
                imageView2.setImageResource(data[1]);
                imageView3.setImageResource(data[2]);
                imageView4.setImageResource(data[3]);


            }
        };
        Runnable task = new Runnable(){
            @Override
            public void run(){
                while(true){
                    try {
                        Thread.sleep(1000);
                    }catch(InterruptedException e){}
                    handler.sendEmptyMessage(1);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void onBackPressed(){

        backPressHandler.onBackPressed("종료하려면 뒤로가기 버튼을 한번 더 누르세요", 3000);

    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.logout:
                    firebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;

                case R.id.delete_account:
                    androidx.appcompat.app.AlertDialog.Builder alert_confirm = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                    alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(MainActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                    finish();
                                                    myStartActivity(LoginActivity.class);
                                                }
                                            });
                                }
                            }
                    );
                    alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "취소", Toast.LENGTH_LONG).show();
                        }
                    });
                    alert_confirm.show();
                    break;

                case R.id.setting:
                    myStartActivity(MyCloset.class);
                    finish();
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.O)


    public void showNoti(String time, String temperature, String symbol){
        builder = null;
        manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder=new NotificationCompat.Builder(this,CHANNEL_ID);
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        builder.setContentTitle(time+"의 체감 온도는? "+ temperature+"º");
        builder.setContentText("날씨는? "+symbol);
        builder.setSmallIcon(R.drawable.icon);
        Notification notification = builder.build();
        manager.notify(1,notification);

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {    }
            else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        } catch (IOException ioException) {
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public class OpenAPI extends AsyncTask<Void, Void, String> {

        private String url;

        public OpenAPI(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {

            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dbFactoty.newDocumentBuilder();

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document doc;
            doc = null;
            try {
                InputStream input = new URL(url).openStream();
                doc = dBuilder.parse(input);
            } catch (IOException | SAXException e) {
            }

            // root tag
            doc.getDocumentElement().normalize();
            // 파싱할 tag
            NodeList nList = doc.getElementsByTagName("time");

            int temp=-1;
            for(int tmp = 0; tmp<nList.getLength() && temp<4; tmp++){
                Node nNode = nList.item(tmp);
                if(nNode.getNodeType() == Node.ELEMENT_NODE){
                    Element eElement = (Element) nNode;

                    String str = eElement.getAttribute("from");
                    int compare = Integer.parseInt(str.substring(5,7)+str.substring(8,10)+str.substring(11,13));

                    if(compare <= n && n <= (compare+3)){ temp=0; }

                    if(0<=temp && temp<4) {
                        time[temp] = eElement.getAttribute("from").substring(11, 13) + "시";
                        NodeList children = eElement.getChildNodes();

                        for (int i = 0; i < children.getLength(); i++) {
                            Node node = children.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element ele = (Element) node;
                                String nodeName = ele.getNodeName();

                                if (nodeName.equals("symbol")) {
                                    symbol[temp] = "@drawable/p" + ele.getAttribute("var");
                                    data[temp] = getResources().getIdentifier(symbol[temp], "drawble", getPackageName());
                                    symbol_num=Integer.parseInt(ele.getAttribute("number"));
                                    symbol_name[temp]=ele.getAttribute("name");
                                }

                                else if (nodeName.equals("feels_like")) {
                                    feels_like[temp] = ele.getAttribute("value");
                                }

                                else if (nodeName.equals("temperature")) {
                                    temperature[temp] = ele.getAttribute("value");
                                    min[temp] = ele.getAttribute("min");
                                    max[temp] = ele.getAttribute("max");
                                }
                            }
                        }
                        temp++;
                    }
                }
            }


            return null;

        }

        @Override
        protected void onPostExecute(String str) {

            super.onPostExecute(str);
        }

    }
    public List<String> view_clothes(){
        List<String> urls = new ArrayList<String>();
        int[] rand_num_list = set_rand_num();

        int rand_outer =rand_num_list[0];
        int rand_top =rand_num_list[1];
        int rand_bottom =rand_num_list[2];
        int rand_accessary =rand_num_list[3];

        img_outer = findViewById(R.id.img_outer);
        img_top = findViewById(R.id.img_top);
        img_bottom = findViewById(R.id.img_bottom);
        img_accessary = findViewById(R.id.img_accessary);

        //winter
        if(month == 12 || month == 1 || month == 2){
            if(-15<=_temperature && _temperature<-5){
                urls.clear();
                urls.add(list_o_winter_m15.get(rand_outer));
                urls.add(list_t_winter_m15.get(rand_top));
                urls.add(list_b_winter_m15.get(rand_bottom));
                urls.add(list_a_winter_m15.get(rand_accessary));

            }
            else if(-5<=_temperature && _temperature < 5){
                urls.clear();
                urls.add(list_o_winter_m5.get(rand_outer));
                urls.add(list_t_winter_m5.get(rand_top));
                urls.add(list_b_winter_m5.get(rand_bottom));
                urls.add(list_a_winter_m5.get(rand_accessary));
            }
            else if(5<=_temperature && _temperature<10){
                urls.clear();
                urls.add(list_o_winter_5.get(rand_outer));
                urls.add(list_t_winter_5.get(rand_top));
                urls.add(list_b_winter_5.get(rand_bottom));
                urls.add( list_a_winter_5.get(rand_accessary));
            }
            else if(10<=_temperature && _temperature<=15){
                urls.clear();
                urls.add(list_o_winter_10.get(rand_outer));
                urls.add(list_t_winter_10.get(rand_top));
                urls.add(list_b_winter_10.get(rand_bottom));
                urls.add( list_a_winter_10.get(rand_accessary));
            }
        }
        //spring
        else if(month == 3 || month == 4 || month == 5){
            if(0<=_temperature && _temperature<5){
                urls.clear();
                urls.add(list_o_spring_0.get(rand_outer));
                urls.add( list_t_spring_0.get(rand_top));
                urls.add(list_b_spring_0.get(rand_bottom));
                urls.add(list_a_spring_0.get(rand_accessary));
            }
            else if(5<=_temperature && _temperature < 10){
                urls.clear();
                urls.add( list_o_spring_5.get(rand_outer));
                urls.add( list_t_spring_5.get(rand_top));
                urls.add( list_b_spring_5.get(rand_bottom));
                urls.add(list_a_spring_5.get(rand_accessary));
            }
            else if(10<=_temperature && _temperature<15){
                urls.clear();
                urls.add(list_o_spring_10.get(rand_outer));
                urls.add(list_t_spring_10.get(rand_top));
                urls.add(list_b_spring_10.get(rand_bottom));
                urls.add(list_a_spring_10.get(rand_accessary));
            }
            else if(15<=_temperature && _temperature<=20){
                urls.clear();
                urls.add(list_o_spring_15.get(rand_outer));
                urls.add(list_t_spring_15.get(rand_top));
                urls.add(list_b_spring_15.get(rand_bottom));
                urls.add( list_a_spring_15.get(rand_accessary));
            }
        }
        //summer
        else if(month == 6 || month == 7 || month == 8){
            if(20<=_temperature && _temperature<25){
                urls.clear();
                urls.add( list_o_summer_20.get(rand_outer));
                urls.add(list_t_summer_20.get(rand_top));
                urls.add(list_b_summer_20.get(rand_bottom));
                urls.add(list_a_summer_20.get(rand_accessary));
            }
            else if(25<=_temperature && _temperature <30){
                urls.clear();
                urls.add( list_o_summer_25.get(rand_outer));
                urls.add( list_t_summer_25.get(rand_top));
                urls.add( list_b_summer_25.get(rand_bottom));
                urls.add( list_a_summer_25.get(rand_accessary));
            }
            else if(30<=_temperature && _temperature<35){
                urls.clear();
                urls.add(list_o_summer_30.get(rand_outer));
                urls.add( list_t_summer_30.get(rand_top));
                urls.add( list_b_summer_30.get(rand_bottom));
                urls.add(list_a_summer_30.get(rand_accessary));
            }
            else if(35<=_temperature && _temperature<=40){
                urls.clear();
                urls.add( list_o_summer_35.get(rand_outer));
                urls.add( list_t_summer_35.get(rand_top));
                urls.add(list_b_summer_35.get(rand_bottom));
                urls.add(list_a_summer_35.get(rand_accessary));
            }
        }
        //autumn
        else if(month == 9 || month == 10 || month == 11){

            if(5<=_temperature && _temperature<10){
                urls.clear();
                urls.add( list_o_autumn_5.get(rand_outer));
                urls.add(list_t_autumn_5.get(rand_top));
                urls.add(list_b_autumn_5.get(rand_bottom));
                urls.add( list_a_autumn_5.get(rand_accessary));
            }
            else if(10<=_temperature && _temperature <15){
                urls.clear();
                urls.add(list_o_autumn_10.get(rand_outer));
                urls.add( list_t_autumn_10.get(rand_top));
                urls.add( list_b_autumn_10.get(rand_bottom));
                urls.add(list_a_autumn_10.get(rand_accessary));
            }
            else if(15<=_temperature && _temperature<20){
                urls.clear();
                urls.add(list_o_autumn_15.get(rand_outer));
                urls.add( list_t_autumn_15.get(rand_top));
                urls.add(list_b_autumn_15.get(rand_bottom));
                urls.add( list_a_autumn_15.get(rand_accessary));
            }
            else if(20<=_temperature && _temperature<=25){
                urls.clear();
                urls.add(list_o_autumn_20.get(rand_outer));
                urls.add(list_t_autumn_20.get(rand_top));
                urls.add(list_b_autumn_20.get(rand_bottom));
                urls.add(list_a_autumn_20.get(rand_accessary));
            }
        }
        return urls;
    }

    private int[] set_rand_num() {
        int[] rand_num = new int[4];
        Random rand = new Random();
        if(month == 12 || month == 1 || month == 2){
            if(-15<=_temperature && _temperature<-5){
                rand_num[0] = rand.nextInt(list_o_winter_m15.size());
                rand_num[1] = rand.nextInt(list_t_winter_m15.size());
                rand_num[2] = rand.nextInt(list_b_winter_m15.size());
                rand_num[3] = rand.nextInt(list_a_winter_m15.size());
            }
            else if(-5<=_temperature && _temperature < 5){
                rand_num[0] = rand.nextInt(list_o_winter_m5.size());
                rand_num[1] = rand.nextInt(list_t_winter_m5.size());
                rand_num[2] = rand.nextInt(list_b_winter_m5.size());
                rand_num[3] = rand.nextInt(list_a_winter_m5.size());
            }
            else if(5<=_temperature && _temperature<10){
                rand_num[0] = rand.nextInt(list_o_winter_5.size());
                rand_num[1] = rand.nextInt(list_t_winter_5.size());
                rand_num[2] = rand.nextInt(list_b_winter_5.size());
                rand_num[3] = rand.nextInt(list_a_winter_5.size());
            }
            else if(10<=_temperature && _temperature<=15){
                rand_num[0] = rand.nextInt(list_o_winter_10.size());
                rand_num[1] = rand.nextInt(list_t_winter_10.size());
                rand_num[2] = rand.nextInt(list_b_winter_10.size());
                rand_num[3] = rand.nextInt(list_a_winter_10.size());
            }
        }
        //spring
        else if(month == 3 || month == 4 || month == 5){
            if(0<=_temperature && _temperature<5){
                rand_num[0] = rand.nextInt(list_o_spring_0.size());
                rand_num[1] = rand.nextInt(list_t_spring_0.size());
                rand_num[2] = rand.nextInt(list_b_spring_0.size());
                rand_num[3] = rand.nextInt(list_a_spring_0.size());
            }
            else if(5<=_temperature && _temperature < 10){
                rand_num[0] = rand.nextInt(list_o_spring_5.size());
                rand_num[1] = rand.nextInt(list_t_spring_5.size());
                rand_num[2] = rand.nextInt(list_b_spring_5.size());
                rand_num[3] = rand.nextInt(list_a_spring_5.size());
            }
            else if(10<=_temperature && _temperature<15){
                rand_num[0] = rand.nextInt(list_o_spring_10.size());
                rand_num[1] = rand.nextInt(list_t_spring_10.size());
                rand_num[2] = rand.nextInt(list_b_spring_10.size());
                rand_num[3] = rand.nextInt(list_a_spring_10.size());
            }
            else if(15<=_temperature && _temperature<=20){
                rand_num[0] = rand.nextInt(list_o_spring_15.size());
                rand_num[1] = rand.nextInt(list_t_spring_15.size());
                rand_num[2] = rand.nextInt(list_b_spring_15.size());
                rand_num[3] = rand.nextInt(list_a_spring_15.size());
            }
        }
        //summer
        else if(month == 6 || month == 7 || month == 8){
            if(20<=_temperature && _temperature<25){
                rand_num[0] = rand.nextInt(list_o_summer_20.size());
                rand_num[1] = rand.nextInt(list_t_summer_20.size());
                rand_num[2] = rand.nextInt(list_b_summer_20.size());
                rand_num[3] = rand.nextInt(list_a_summer_20.size());
            }
            else if(25<=_temperature && _temperature <30){
                rand_num[0] = rand.nextInt(list_o_summer_25.size());
                rand_num[1] = rand.nextInt(list_t_summer_25.size());
                rand_num[2] = rand.nextInt(list_b_summer_25.size());
                rand_num[3] = rand.nextInt(list_a_summer_25.size());
            }
            else if(30<=_temperature && _temperature<35){
                rand_num[0] = rand.nextInt(list_o_summer_30.size());
                rand_num[1] = rand.nextInt(list_t_summer_30.size());
                rand_num[2] = rand.nextInt(list_b_summer_30.size());
                rand_num[3] = rand.nextInt(list_a_summer_30.size());
            }
            else if(35<=_temperature && _temperature<=40){
                rand_num[0] = rand.nextInt(list_o_summer_35.size());
                rand_num[1] = rand.nextInt(list_t_summer_35.size());
                rand_num[2] = rand.nextInt(list_b_summer_35.size());
                rand_num[3] = rand.nextInt(list_a_summer_35.size());
            }
        }
        //autumn
        else if(month == 9 || month == 10 || month == 11){
            if(5<=_temperature && _temperature<10){
                rand_num[0] = rand.nextInt(list_o_autumn_5.size());
                rand_num[1] = rand.nextInt(list_t_autumn_5.size());
                rand_num[2] = rand.nextInt(list_b_autumn_5.size());
                rand_num[3] = rand.nextInt(list_a_autumn_5.size());
            }
            else if(10<=_temperature && _temperature <15){
                rand_num[0] = rand.nextInt(list_o_autumn_10.size());
                rand_num[1] = rand.nextInt(list_t_autumn_10.size());
                rand_num[2] = rand.nextInt(list_b_autumn_10.size());
                rand_num[3] = rand.nextInt(list_a_autumn_10.size());
            }
            else if(15<=_temperature && _temperature<20){
                rand_num[0] = rand.nextInt(list_o_autumn_15.size());
                rand_num[1] = rand.nextInt(list_t_autumn_15.size());
                rand_num[2] = rand.nextInt(list_b_autumn_15.size());
                rand_num[3] = rand.nextInt(list_a_autumn_15.size());
            }
            else if(20<=_temperature && _temperature<=25){
                rand_num[0] = rand.nextInt(list_o_autumn_20.size());
                rand_num[1] = rand.nextInt(list_t_autumn_20.size());
                rand_num[2] = rand.nextInt(list_b_autumn_20.size());
                rand_num[3] = rand.nextInt(list_a_autumn_20.size());
            }
        }
        return rand_num;
    }
}