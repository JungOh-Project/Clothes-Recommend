package com.example.jungoh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jung_oh.R;

import static com.example.jung_oh.R.*;

public class closet_card_view extends AppCompatActivity {

    static String strText;
    static String strSeason;
    static final String[] SEASON_LIST= {"Spring", "Summer", "Autumn", "Winter"};
    static final String[] OUTER_LIST = {"cardigan", "leaderJacket", "lightweightPadding", "longPadding", "mustang", "windBreaker", "bbogul", "bbogulVest",
            "shirt", "yasang", "woolenCoat", "jacket", "jersey", "denimJacket", "coat", "trenchCoat", "padding", "paddingVest",
            "paddingCoat", "flightJacket", "hoodZipup"};
    static final String[] TOP_LIST = {"longSleve", "sleeveless", "mentomen", "Tshirt", "bluse", "shirt", "anorak", "onePiece", "hoodT"};
    static final String[] BOTTOM_LIST = {"trouser", "longSkirt", "pants", "slacks", "widePants", "jeans", "skirt", "trainingPants", "trainingSuit"};
    static final String[] ACCESSARY_LIST = {"muffler", "strawHat", "packpack", "bucketHat", "beret", "cap", "beanie", "sunglasses", "scarf", "glasses", "parasol",
            "ecobag", "rainCoat", "umbrella", "gloves", "rainBoots", "crossBag", "bigBag", "woolenHat"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_closet_card_view);
        //ListView list = findViewById(R.id.list_view_alert_dialog_list);

        Intent intent = getIntent();
        String[] array = intent.getExtras().getStringArray(Intent.EXTRA_TEXT);
        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, array);
        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(id.list_view_alert_dialog_list);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview.setAdapter(adapter);
        //리스트 선택 가능하게 만들어줌
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                strText = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), strText, Toast.LENGTH_SHORT).show();
                if((strText.equals("Spring")) || (strText.equals("Summer")) || (strText.equals("Autumn")) || (strText.equals("Winter"))){
                    strSeason = strText;
                }
                finish();
                // TODO : use strText
            }
        });

    }
}