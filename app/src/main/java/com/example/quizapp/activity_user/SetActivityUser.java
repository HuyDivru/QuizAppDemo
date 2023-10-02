package com.example.quizapp.activity_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.quizapp.adapter.GrideAdapter;
import com.example.quizapp.adapter_user.GrideAdapterUser;
import com.example.quizapp.databinding.ActivitySetUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class SetActivityUser extends AppCompatActivity {
    ActivitySetUserBinding binding;

    FirebaseDatabase database;
    GrideAdapterUser adapter;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySetUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        key=getIntent().getStringExtra("key");
        adapter=new GrideAdapterUser(getIntent().getIntExtra("sets", 0),
                getIntent().getStringExtra("category"));
        binding.gridViewUser.setAdapter(adapter);

    }
}