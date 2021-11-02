package com.example.jungoh;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.jung_oh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.example.jungoh.closet_card_view.strSeason;


public class AddClothesActivity extends AppCompatActivity {

    //카메라 권한 체크 후 권한 결정
    final private static String TAG = "Hyein";
    private FirebaseAuth firebaseAuth;
    TextView text;

    //썸네일 사진 요청변수
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String Database_Path="Database";



    Button btn_photo;
    ImageView iv_photo;
    Button btn_assign;
    Button btn_season;
    Button btn_Outer;
    Button btn_Bottom;
    Button btn_Top;
    Button btn_Accessary;


    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle barDrawerToggle;
    private BackPressHandler backPressHandler = new BackPressHandler(this);


    //옷 종류 선택 리스트
    static final String[] SEASON_CHOICE = {"Season"};
    static final String[] CLOSET_MENU = {"Outer", "Top", "Bottom", "Accessary"};

    //옷 종류 선택시 세부항목 리스트
    static final String[] SEASON_LIST = {"Spring", "Summer", "Autumn", "Winter"};
    static final String[] OUTER_LIST = {"cardigan", "leaderJacket", "lightweightPadding", "longPadding", "mustang", "windBreaker", "bbogul", "bbogulVest",
            "shirt", "yasang", "woolenCoat", "jacket", "jersey", "denimJacket", "coat", "trenchCoat", "padding", "paddingVest",
            "paddingCoat", "flightJacket", "hoodZipup"};

    static final String[] TOP_LIST = {"longSleve", "sleeveless", "mentomen", "Tshirt", "bluse", "shirt", "anorak", "onePiece", "hoodT"};
    static final String[] BOTTOM_LIST = {"trouser", "longSkirt", "pants", "slacks", "widePants", "jeans", "skirt", "trainingPants", "trainingSuit"};
    static final String[] ACCESSARY_LIST = {"muffler", "strawHat", "packpack", "bucketHat", "beret", "cap", "beanie", "sunglasses", "scarf", "glasses", "parasol",
            "ecobag", "rainCoat", "umbrella", "gloves", "rainBoots", "crossBag", "bigBag", "woolenHat"};

    private Uri filePath;

    final static int TAKE_PICTURE = 1;

    //경로변수와 사진쵤영 요청변수 생성
    String mCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO = 1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public String uEmail = user.getEmail();
    public  static String clothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);
        navigationView=findViewById(R.id.nav);
        drawerLayout=findViewById(R.id.add_clothes);
        findViewById(R.id.logout).setOnClickListener(onClickListener);
        findViewById(R.id.delete_account).setOnClickListener(onClickListener);





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




        iv_photo = findViewById(R.id.iv_photo);
        btn_photo = findViewById(R.id.btn_photo);
        btn_assign = findViewById(R.id.btn_assign);
        btn_season = findViewById(R.id.btn_Season);
        btn_Top = findViewById(R.id.btn_Top);
        btn_Bottom = findViewById(R.id.btn_Bottom);
        btn_Outer = findViewById(R.id.btn_Outer);
        btn_Accessary = findViewById(R.id.btn_Accessary);

        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
        final ArrayAdapter adapter_season = new ArrayAdapter(this, android.R.layout.simple_list_item_1, SEASON_CHOICE);
        final ArrayAdapter adapter_clothes = new ArrayAdapter(this, android.R.layout.simple_list_item_1, CLOSET_MENU);
        // listview 생성 및 adapter 지정.
        final ListView listview_season = (ListView) findViewById(R.id.season_list);
        final ListView listview_clothes = (ListView) findViewById(R.id.list_closet);
        listview_season.setAdapter(adapter_season);
        listview_clothes.setAdapter(adapter_clothes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(AddClothesActivity.this, new String[]
                        {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        //버튼 누르면 촬영하는 부분을 dispatchTakePictureIntent를 불러오게함
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_photo:
                        dispatchTakePictureIntent();
                        break;
                }
            }
        });

        btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        btn_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), closet_card_view.class);
                intent.putExtra(Intent.EXTRA_TEXT, SEASON_LIST);
                startActivity(intent);
            }
        });
        btn_Outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), closet_card_view.class);
                intent.putExtra(Intent.EXTRA_TEXT, OUTER_LIST);
                startActivity(intent);
            }
        });

        btn_Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), closet_card_view.class);
                intent.putExtra(Intent.EXTRA_TEXT, TOP_LIST);
                startActivity(intent);
            }
        });

        btn_Bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), closet_card_view.class);
                intent.putExtra(Intent.EXTRA_TEXT, BOTTOM_LIST);
                startActivity(intent);
            }
        });

        btn_Accessary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), closet_card_view.class);
                intent.putExtra(Intent.EXTRA_TEXT, ACCESSARY_LIST);
                startActivity(intent);
            }
        });
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
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(AddClothesActivity.this);
                    alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(AddClothesActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(AddClothesActivity.this, "취소", Toast.LENGTH_LONG).show();
                        }
                    });
                    alert_confirm.show();
                    break;

            }
        }
    };


    //옷 종류 세부사항 고르는 화면 전환
    private void showAlertDialog(final String[] text){
        Intent intent = new Intent(getApplicationContext(), closet_card_view.class);
        intent.putExtra("list", text);
        startActivity(intent);
    }
    // 사진 촬영 후 썸네일만 띄워줌. 이미지를 파일로 저장해야 함
    //촬영한 사진을 이미지 파일로 저장하는 함수
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile( imageFileName,".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 카메라 인텐트 실행하는 부분
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            try { photoFile = createImageFile(); }
            catch (IOException ex) { }
            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.jung_oh.FileProvider", photoFile);
                //사진을 firebase에 저장하기위한 경로 저장
                filePath = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // 카메라로 촬영한 영상을 가져오는 부분
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        //썸네일을 저장후 화면에 사진 띄우기
                        File file = new File(mCurrentPhotoPath);
                        //filePath = Uri.fromFile(file);
                        Bitmap bitmap;
                        if (Build.VERSION.SDK_INT >= 29) {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                            try {
                                bitmap = ImageDecoder.decodeBitmap(source);
                                if (bitmap != null) { iv_photo.setImageBitmap(bitmap); }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                                if (bitmap != null) { iv_photo.setImageBitmap(bitmap); }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    //upload the file
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            final StorageReference storageRef;
            //storage 주소와 폴더 파일명을 지정해 준다.
            final String cloth_type = closet_card_view.strText;
            final String cloth_season = closet_card_view.strSeason;

            if(cloth_type.equals("muffler") || cloth_type.equals("strawHat") || cloth_type.equals("packpack") || cloth_type.equals("bucketHat") || cloth_type.equals("beret") ||
                    cloth_type.equals("cap") || cloth_type.equals("beanie") || cloth_type.equals("sunglasses") || cloth_type.equals("scarf" ) || cloth_type.equals("glasses")
                    || cloth_type.equals("parasol") || cloth_type.equals("ecobag") || cloth_type.equals("rainCoat" )|| cloth_type.equals("umbrella") || cloth_type.equals("gloves")
                    || cloth_type.equals("rainBoots") || cloth_type.equals("crossBag") || cloth_type.equals("bigBag") || cloth_type.equals("woolenHat"))
            {storageRef = storage.getReference( uEmail + "/Accessary/" + filename); clothes = "Accessary";}
            else if(cloth_type.equals("longSleve") || cloth_type.equals("sleeveless") || cloth_type.equals("mentomen") || cloth_type.equals("Tshirt") ||
                    cloth_type.equals("bluse") || cloth_type.equals("shirt") || cloth_type.equals("anorak") || cloth_type.equals("onePiece") || cloth_type.equals("hoodT"))
            {storageRef = storage.getReference( uEmail + "/Top/" + filename);clothes = "Top";}
            else if(cloth_type.equals("trouser") || cloth_type.equals("longSkirt") || cloth_type.equals("pants") || cloth_type.equals("slacks") || cloth_type.equals("widePants")||
                    cloth_type.equals("jeans") || cloth_type.equals("skirt") || cloth_type.equals("trainingPants") || cloth_type.equals("trainingSuit"))
            {storageRef = storage.getReference( uEmail + "/Bottom/" + filename);clothes = "Bottom";}
            else {storageRef = storage.getReference( uEmail + "/Outer/" + filename);clothes = "Outer";}

            //업로드하기
            UploadTask uploadTask = storageRef.putFile(filePath);
            //성공시
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                    Toast.makeText(getApplicationContext(), "storage 업로드 완료!", Toast.LENGTH_SHORT).show();
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference userstore_outer_spring = FirebaseDatabase.getInstance().getReference().child("Outer_"+uid).child("spring");//db
                            DatabaseReference userstore_outer_summer = FirebaseDatabase.getInstance().getReference().child("Outer_"+uid).child("summer");//db
                            DatabaseReference userstore_outer_autumn = FirebaseDatabase.getInstance().getReference().child("Outer_"+uid).child("autumn");//db
                            DatabaseReference userstore_outer_winter = FirebaseDatabase.getInstance().getReference().child("Outer_"+uid).child("winter");//db

                            DatabaseReference userstore_top_spring = FirebaseDatabase.getInstance().getReference().child("Top_"+uid).child("spring");//db
                            DatabaseReference userstore_top_summer = FirebaseDatabase.getInstance().getReference().child("Top_"+uid).child("summer");//db
                            DatabaseReference userstore_top_autumn = FirebaseDatabase.getInstance().getReference().child("Top_"+uid).child("autumn");//db
                            DatabaseReference userstore_top_winter = FirebaseDatabase.getInstance().getReference().child("Top_"+uid).child("winter");//db

                            DatabaseReference userstore_bottom_spring = FirebaseDatabase.getInstance().getReference().child("Bottom_"+uid).child("spring");//db
                            DatabaseReference userstore_bottom_summer = FirebaseDatabase.getInstance().getReference().child("Bottom_"+uid).child("summer");//db
                            DatabaseReference userstore_bottom_autumn = FirebaseDatabase.getInstance().getReference().child("Bottom_"+uid).child("autumn");//db
                            DatabaseReference userstore_bottom_winter = FirebaseDatabase.getInstance().getReference().child("Bottom_"+uid).child("winter");//db

                            DatabaseReference userstore_accessary_spring = FirebaseDatabase.getInstance().getReference().child("Accessary_"+uid).child("spring");//db
                            DatabaseReference userstore_accessary_summer = FirebaseDatabase.getInstance().getReference().child("Accessary_"+uid).child("summer");//db
                            DatabaseReference userstore_accessary_autumn  = FirebaseDatabase.getInstance().getReference().child("Accessary_"+uid).child("autumn");//db
                            DatabaseReference userstore_accessary_winter = FirebaseDatabase.getInstance().getReference().child("Accessary_"+uid).child("winter");//db
                            if(clothes.equals("Outer")){
                                if(cloth_season.equals("Spring")) {
                                    DatabaseReference imagestore = userstore_outer_spring.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Summer")) {
                                    DatabaseReference imagestore = userstore_outer_summer.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Autumn")) {
                                    DatabaseReference imagestore = userstore_outer_autumn.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Winter")) {
                                    DatabaseReference imagestore = userstore_outer_winter.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }else if(clothes.equals("Top")) {
                                if(cloth_season.equals("Spring")) {
                                    DatabaseReference imagestore = userstore_top_spring.push();//사용자의 무작위 키 만들기
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Summer")) {
                                    DatabaseReference imagestore = userstore_top_summer.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Autumn")) {
                                    DatabaseReference imagestore = userstore_top_autumn.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Winter")) {
                                    DatabaseReference imagestore = userstore_top_winter.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }else if(clothes.equals("Bottom")){
                                if(cloth_season.equals("Spring")) {
                                    DatabaseReference imagestore = userstore_bottom_spring.push();//사용자의 무작위 키 만들기
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Summer")) {
                                    DatabaseReference imagestore = userstore_bottom_summer.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Autumn")) {
                                    DatabaseReference imagestore = userstore_bottom_autumn.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Winter")) {
                                    DatabaseReference imagestore = userstore_bottom_winter.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }else if(clothes.equals("Accessary")){
                                if(cloth_season.equals("Spring")) {
                                    DatabaseReference imagestore = userstore_accessary_spring.push();//사용자의 무작위 키 만들기
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Summer")) {
                                    DatabaseReference imagestore = userstore_accessary_summer.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Autumn")) {
                                    DatabaseReference imagestore = userstore_accessary_autumn.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else if(cloth_season.equals("Winter")) {
                                    DatabaseReference imagestore = userstore_accessary_winter.push();//사용자의 무작위 키 만들기
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(uEmail, String.valueOf(uri), cloth_type);
                                    imagestore.setValue(imageUploadInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddClothesActivity.this, "DB 업로드 완료!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    });

                }
            })//실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })//진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }



    }


}
