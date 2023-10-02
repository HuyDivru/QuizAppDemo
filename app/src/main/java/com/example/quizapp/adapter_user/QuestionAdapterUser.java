package com.example.quizapp.adapter_user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quizapp.R;
import com.example.quizapp.adapter.QuestionAdapter;
import com.example.quizapp.databinding.ItemQuestionBinding;
import com.example.quizapp.model.QuestionModel;
import com.example.quizapp.model_user.QuestionModelUser;

import java.util.ArrayList;

public class QuestionAdapterUser extends RecyclerView.Adapter<QuestionAdapterUser.ViewHolder>{
    Context context;
    ArrayList<QuestionModelUser> list;

    public QuestionAdapterUser(Context context, ArrayList<QuestionModelUser> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public QuestionAdapterUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_question,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapterUser.ViewHolder holder, int position) {
        QuestionModelUser model=list.get(position);

        holder.binding.question.setText(model.getQuestion());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemQuestionBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemQuestionBinding.bind(itemView);
        }
    }
}
