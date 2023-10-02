package com.example.quizapp.adapter_user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.SetActivity;
import com.example.quizapp.activity_user.SetActivityUser;
import com.example.quizapp.databinding.ItemsCategoryBinding;
import com.example.quizapp.model.CategoryModel;
import com.example.quizapp.model_user.CategoryModelUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapterUser extends RecyclerView.Adapter<CategoryAdapterUser.ViewHolder> {
    Context context;
    ArrayList<CategoryModelUser> list;

    public CategoryAdapterUser(Context context, ArrayList<CategoryModelUser> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapterUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterUser.ViewHolder holder, int position) {
        CategoryModelUser model=list.get(position);
        holder.binding.categoryName.setText(model.getCategoryName());

        Picasso.get()
                .load(model.getCategoryImage())
                .placeholder(R.drawable.logo)
                .into(holder.binding.categoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SetActivityUser.class);
                intent.putExtra("category",model.getCategoryName());
                intent.putExtra("sets",model.getSetNum());
                intent.putExtra("key",model.getKey());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemsCategoryBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemsCategoryBinding.bind(itemView);
        }
    }
}
