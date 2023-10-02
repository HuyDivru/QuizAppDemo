package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.quizapp.adapter.GrideAdapter;
import com.example.quizapp.databinding.ActivitySetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class SetActivity extends AppCompatActivity {
    ActivitySetBinding binding;

    FirebaseDatabase database;
    GrideAdapter adapter;
    int a=1;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        key=getIntent().getStringExtra("key");
        adapter=new GrideAdapter(getIntent().getIntExtra("sets", 0),
                getIntent().getStringExtra("category"), key, new GrideAdapter.GridListener() {
            @Override
            public void addSets() {

                database.getReference().child("categories").child(key)
                        .child("setNum").setValue(getIntent().getIntExtra("sets",0)+a++).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    adapter.sets++;
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(SetActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        binding.gridView.setAdapter(adapter);
    }
}
/*
    @Override
protected void onCreate(Bundle savedInstanceState) {
    // ...
    adapter = new GrideAdapter(getIntent().getIntExtra("sets", 0),
            getIntent().getStringExtra("category"), key, new GrideAdapter.GridListener() {
                @Override
                public void addSets() {
                    database.getReference().child("categories").child(key)
                            .child("setNum").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int currentSetNum = dataSnapshot.getValue(Integer.class);
                                int newSetNum = currentSetNum + 1;

                                database.getReference().child("categories").child(key)
                                        .child("setNum").setValue(newSetNum)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    adapter.sets++;
                                                    adapter.notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(SetActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý lỗi nếu cần
                        }
                    });
                }
            });
    // ...
}

 */