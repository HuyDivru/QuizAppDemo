package com.example.quizapp.activity_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.example.quizapp.QuestionActivity;
import com.example.quizapp.R;
import com.example.quizapp.adapter.QuestionAdapter;
import com.example.quizapp.adapter_user.QuestionAdapterUser;
import com.example.quizapp.databinding.ActivityQuestionBinding;
import com.example.quizapp.databinding.ActivityQuestionUserBinding;
import com.example.quizapp.model.QuestionModel;
import com.example.quizapp.model_user.QuestionModelUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionActivityUser extends AppCompatActivity {
    ActivityQuestionUserBinding binding;
    private ArrayList<QuestionModelUser> list;
    private int count=0;
    private int postion=0;
    private int score=0;

    CountDownTimer timer;

    FirebaseDatabase database;
    String categoryName;
    private int set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();

        categoryName=getIntent().getStringExtra("categoryName");
        set=getIntent().getIntExtra("setNum",1);

        list=new ArrayList<>();

        database.getReference().child("Sets").child(categoryName).child("questions")
                .orderByChild("setNum").equalTo(set)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()
                                 ) {
                                QuestionModelUser modelUser=dataSnapshot.getValue(QuestionModelUser.class);
                                list.add(modelUser);
                            }
                            if(list.size()>0){
                                for (int i = 0; i <4 ; i++) {
                                    binding.
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}