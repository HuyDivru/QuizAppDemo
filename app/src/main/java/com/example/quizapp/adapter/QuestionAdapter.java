package com.example.quizapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.databinding.ItemQuestionBinding;
import com.example.quizapp.model.QuestionModel;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    Context context;
    ArrayList<QuestionModel> list;

    public QuestionAdapter(Context context, ArrayList<QuestionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_question,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        QuestionModel model=list.get(position);

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
