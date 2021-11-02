package com.example.jungoh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.example.jung_oh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH=1000;
    private BackPressHandler backPressHandler = new BackPressHandler(this);
    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user==null) {
                    myStartActivity(LoginActivity.class);
                    finish();

                }else{
                    myStartActivity(MainActivity.class);
                    finish();

                }

            }
        },SPLASH_DISPLAY_LENGTH);
    }
    public void onBackPressed(){

        backPressHandler.onBackPressed("종료하려면 뒤로가기 버튼을 한번 더 누르세요", 3000);

    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}