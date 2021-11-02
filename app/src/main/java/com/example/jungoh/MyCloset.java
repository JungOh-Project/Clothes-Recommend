package com.example.jungoh;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class MyCloset extends AppCompatActivity {
    //소이꺼
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle barDrawerToggle;
    private FirebaseAuth firebaseAuth;
    private BackPressHandler backPressHandler = new BackPressHandler(this);


    private FirebaseDatabase database;



    public static List<VerticalData> data1 = new ArrayList<>();
    public static List<VerticalData> data2 = new ArrayList<>();
    public static List<VerticalData> data3 = new ArrayList<>();
    public static List<VerticalData> data4 = new ArrayList<>();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_closet);
        database = FirebaseDatabase.getInstance();


        setContentView(R.layout.activity_my_closet);
        findViewById(R.id.delete_account).setOnClickListener(onClickListener);
        findViewById(R.id.Add_Button).setOnClickListener(onClickListener);
        findViewById(R.id.logout).setOnClickListener(onClickListener);
        navigationView=findViewById(R.id.nav);
        drawerLayout=findViewById(R.id.mypage);

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
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mypage) ;
                if (!drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.openDrawer(Gravity.LEFT) ;
                }
            }
        });

        //1
        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.Outer_list);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        final RecyclerViewAdapter1 recyclerViewAdapter1 = new RecyclerViewAdapter1();
        MyListDecoration decoration1 = new MyListDecoration();
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(recyclerViewAdapter1);
        recyclerView1.addItemDecoration(decoration1);
        data1.clear();
        database.getReference().child("Outer_"+uid).child("spring").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot1.getValue(VerticalData.class);
                    data1.add(verticalData);
                }
                recyclerViewAdapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Outer_"+uid).child("summer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot1.getValue(VerticalData.class);
                    data1.add(verticalData);
                }
                recyclerViewAdapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Outer_"+uid).child("autumn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot1.getValue(VerticalData.class);
                    data1.add(verticalData);
                }
                recyclerViewAdapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Outer_"+uid).child("winter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot1.getValue(VerticalData.class);
                    data1.add(verticalData);
                }
                recyclerViewAdapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // 2
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.Top_list);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        final RecyclerViewAdapter2 recyclerViewAdapter2 = new RecyclerViewAdapter2();
        MyListDecoration decoration2 = new MyListDecoration();
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(recyclerViewAdapter2);
        recyclerView2.addItemDecoration(decoration2);
        data2.clear();
        database.getReference().child("Top_"+uid).child("spring").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    data2.add(verticalData);
                }
                recyclerViewAdapter2.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Top_"+uid).child("summer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    data2.add(verticalData);
                }
                recyclerViewAdapter2.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Top_"+uid).child("autumn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    data2.add(verticalData);
                }
                recyclerViewAdapter2.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Top_"+uid).child("winter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot2.getValue(VerticalData.class);
                    data2.add(verticalData);
                }
                recyclerViewAdapter2.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        // 3
        RecyclerView recyclerView3 = (RecyclerView) findViewById(R.id.Bottom_list);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        final RecyclerViewAdapter3 recyclerViewAdapter3 = new RecyclerViewAdapter3();
        MyListDecoration decoration3 = new MyListDecoration();
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setAdapter(recyclerViewAdapter3);
        recyclerView3.addItemDecoration(decoration3);
        data3.clear();
        database.getReference().child("Bottom_"+uid).child("spring").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot3 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot3.getValue(VerticalData.class);
                    data3.add(verticalData);
                }
                recyclerViewAdapter3.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Bottom_"+uid).child("summer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot3 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot3.getValue(VerticalData.class);
                    data3.add(verticalData);
                }
                recyclerViewAdapter3.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Bottom_"+uid).child("autumn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot3 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot3.getValue(VerticalData.class);
                    data3.add(verticalData);
                }
                recyclerViewAdapter3.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Bottom_"+uid).child("winter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot3 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot3.getValue(VerticalData.class);
                    data3.add(verticalData);
                }
                recyclerViewAdapter3.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        // 4
        RecyclerView recyclerView4 = (RecyclerView) findViewById(R.id.Accessary_list);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        final RecyclerViewAdapter4 recyclerViewAdapter4 = new RecyclerViewAdapter4();
        MyListDecoration decoration4 = new MyListDecoration();
        recyclerView4.setLayoutManager(layoutManager4);
        recyclerView4.setAdapter(recyclerViewAdapter4);
        recyclerView4.addItemDecoration(decoration4);
        data4.clear();
        database.getReference().child("Accessary_"+uid).child("spring").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot4 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot4.getValue(VerticalData.class);
                    data4.add(verticalData);
                }
                recyclerViewAdapter4.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Accessary_"+uid).child("summer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot4 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot4.getValue(VerticalData.class);
                    data4.add(verticalData);
                }
                recyclerViewAdapter4.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Accessary_"+uid).child("autumn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot4 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot4.getValue(VerticalData.class);
                    data4.add(verticalData);
                }
                recyclerViewAdapter4.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        database.getReference().child("Accessary_"+uid).child("winter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot4 : snapshot.getChildren()) {
                    VerticalData verticalData = snapshot4.getValue(VerticalData.class);
                    data4.add(verticalData);

                }
                recyclerViewAdapter4.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.logout:
                    firebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;

                case R.id.Add_Button:

                    myStartActivity(AddClothesActivity.class);
                    finish();
                    break;

                case R.id.delete_account:
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MyCloset.this);
                    alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(MyCloset.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MyCloset.this, "취소", Toast.LENGTH_LONG).show();
                        }
                    });
                    alert_confirm.show();
                    break;

            }

        }
    };
    public void onBackPressed(){

        backPressHandler.onBackPressed("종료하려면 뒤로가기 버튼을 한번 더 누르세요", 3000);

    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext()).load(data1.get(position).imageURL).into(((CustomViewHolder)holder).imageView);
        }

        @Override
        public int getItemCount() {
            return data1.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.picture_inmycloset);
            }
        }
    }
    class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext()).load(data2.get(position).imageURL).into(((CustomViewHolder)holder).imageView);
        }

        @Override
        public int getItemCount() {
            return data2.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.picture_inmycloset);
            }
        }
    }
    class RecyclerViewAdapter3 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext()).load(data3.get(position).imageURL).into(((CustomViewHolder)holder).imageView);
        }

        @Override
        public int getItemCount() {
            return data3.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.picture_inmycloset);
            }
        }
    }
    class RecyclerViewAdapter4 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext()).load(data4.get(position).imageURL).into(((CustomViewHolder)holder).imageView);
        }

        @Override
        public int getItemCount() {
            return data4.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.picture_inmycloset);
            }
        }
    }
}