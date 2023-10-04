package com.example.quizapp.activity_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.quizapp.MainActivity;
import com.example.quizapp.R;
import com.example.quizapp.adapter_user.CategoryAdapterUser;
import com.example.quizapp.databinding.ActivityMainUserBinding;
import com.example.quizapp.model_user.CategoryModelUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivityUser extends AppCompatActivity {
    private ActivityMainUserBinding binding;
    private FirebaseDatabase database;
    ArrayList<CategoryModelUser> list;
    CategoryAdapterUser adapter;
    ProgressDialog progressDialog;
    BottomNavigationView bottomNavigationView;
    Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        list=new ArrayList<>();


        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        if (loadingDialog.getWindow()!=null){
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.admin:
                        Intent intent=new Intent(MainActivityUser.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.user:
                        return true;
                }


                return false;
            }
        });

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        binding.recyclerviewUser.setLayoutManager(layoutManager);
        adapter=new CategoryAdapterUser(this,list);
        binding.recyclerviewUser.setAdapter(adapter);
        database=FirebaseDatabase.getInstance();
        //lấy dữ liệu của firebase
        database.getReference().child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        list.add(new CategoryModelUser(
                                dataSnapshot.child("categoryName").getValue().toString(),
                                dataSnapshot.child("categoryImage").getValue().toString(),
                                dataSnapshot.getKey(),
                                Integer.parseInt(dataSnapshot.child("setNum").getValue().toString())
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();
                } else {
                    Toast.makeText(MainActivityUser.this, "Category not exits", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivityUser.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }
}