package com.example.quizapp.activity_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quizapp.R;
import com.example.quizapp.databinding.ActivityScoreUserBinding;

public class ScoreActivityUser extends AppCompatActivity {
    ActivityScoreUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityScoreUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        int correct=getIntent().getIntExtra("score",0);
        int totalQuestion=getIntent().getIntExtra("totalQuestion",0);

        int wrong=totalQuestion-correct;
        binding.totalRight.setText(String.valueOf(correct));
        binding.totalWrong.setText(String.valueOf(wrong));

        binding.totalQuestion.setText(String.valueOf(totalQuestion));

        binding.circularProgressBar.setProgress(totalQuestion);
        binding.circularProgressBar.setProgress(correct);
        binding.circularProgressBar.setProgressMax(totalQuestion);

        binding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScoreActivityUser.this,MainActivityUser.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}