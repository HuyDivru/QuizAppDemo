package com.example.quizapp.activity_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
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

        getSupportActionBar().hide();
        resetTimer();
        timer.start();
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
                                    binding.optionContainers.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkAsw((Button)view);
                                        }
                                    });
                                }
                                playAnimation(binding.questionUser,0,list.get(postion).getQuestion());

                                binding.buttonNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        binding.buttonNext.setEnabled(false);
                                        binding.buttonNext.setAlpha(0.3f);

                                        enableOption(true);
                                        postion++;
                                        if(postion == list.size()){
                                            Intent intent=new Intent(QuestionActivityUser.this,ScoreActivityUser.class);
                                            intent.putExtra("score",score);
                                            intent.putExtra("totalQuestion",list.size());
                                            startActivity(intent);
                                            finish();

                                            return ;
                                        }
                                        count=0;
                                        playAnimation(binding.questionUser,0,list.get(postion).getQuestion());
                                    }
                                });
                            }
                            else{
                                Toast.makeText(QuestionActivityUser.this,"question not exits",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuestionActivityUser.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetTimer() {
        timer=new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
            Toast.makeText(QuestionActivityUser.this,"time out",Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            binding.optionContainers.getChildAt(i).setEnabled(enable);
            if(enable){
                binding.optionContainers.getChildAt(i).setBackgroundResource(R.drawable.btn_option_back);
            }
        }
    }

    private void playAnimation(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                        if(value==0&& count<4){
                            String option="";
                            if(count==0){
                                option=list.get(postion).getOptionA();
                            }
                            else if(count==1){
                                option=list.get(postion).getOptionB();
                            }
                            else if(count==2){
                                option=list.get(postion).getOptionC();
                            }
                            else if(count==3){
                                option=list.get(postion).getOptionD();
                            }
                            playAnimation(binding.optionContainers.getChildAt(count),0,option);
                            count++;

                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {
                        if(value==0){
                            try{
                                ((TextView) view).setText(data);
                                binding.numIndicator.setText(postion+1+"/"+list.size());
                            }
                            catch (Exception e){
//                                e.printStackTrace();
                                ((Button) view).setText(data);
                            }
                            view.setTag(data);
                            playAnimation(view ,1,data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {

                    }
                });
    }

    private void checkAsw(Button selectedOption) {

        enableOption(false);
        binding.buttonNext.setEnabled(true);
        binding.buttonNext.setAlpha(1);
        if(selectedOption.getText().toString().equals(list.get(postion).getCorrectAsw())){
            score++;

            selectedOption.setBackgroundResource(R.drawable.right_asw);

        }
        else{
            selectedOption.setBackgroundResource(R.drawable.wrong_answ);
            Button correctButton=(Button) binding.optionContainers.findViewWithTag(list.get(postion).getCorrectAsw());
            correctButton.setBackgroundResource(R.drawable.right_asw);
        }

    }
}