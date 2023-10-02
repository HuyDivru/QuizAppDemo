package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizapp.adapter.QuestionAdapter;
import com.example.quizapp.databinding.ActivityQuestionBinding;
import com.example.quizapp.model.QuestionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class QuestionActivity extends AppCompatActivity {
    ActivityQuestionBinding binding;

    FirebaseDatabase database;
    ArrayList<QuestionModel> list;
    QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        list=new ArrayList<>();

        int setNum=getIntent().getIntExtra("setNum", 0);
        String categoryName=getIntent().getStringExtra("categoryName");

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.questionRec.setLayoutManager(layoutManager);
        adapter=new QuestionAdapter(this,list);
        binding.questionRec.setAdapter(adapter);

        database.getReference().child("Sets").child(categoryName).child("questions")
                        .orderByChild("setNum").equalTo(setNum)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for (DataSnapshot dataSnapshot:
                                         snapshot.getChildren()) {
                                        QuestionModel model=dataSnapshot.getValue(QuestionModel.class);
                                        model.setKey(dataSnapshot.getKey());
                                        list.add(model);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(QuestionActivity.this,"question not exit",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(QuestionActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
        binding.questionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuestionActivity.this,AddQuestionActivity.class);
                intent.putExtra("category",categoryName);
                intent.putExtra("setNum",setNum);
                startActivity(intent);
            }
        });

    }
}