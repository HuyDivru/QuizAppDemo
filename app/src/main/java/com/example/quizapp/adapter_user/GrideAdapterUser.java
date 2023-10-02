package com.example.quizapp.adapter_user;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.quizapp.QuestionActivity;
import com.example.quizapp.R;
import com.example.quizapp.activity_user.QuestionActivityUser;

public class GrideAdapterUser extends BaseAdapter {
    public int sets=0;
    private String category;

    public GrideAdapterUser(int sets, String category) {
        this.sets = sets;
        this.category = category;
    }

    @Override
    public int getCount() {
        return sets + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        if(convertView==null){
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_set_user,viewGroup,false);
        }
        else{
            view=convertView;
        }
        if(position==0){
            ((CardView) view.findViewById(R.id.card_view_user)).setVisibility(View.GONE);
        }
        else{
            ((TextView)view.findViewById(R.id.set_name_user)).setText(String.valueOf(position));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    Toast.makeText(viewGroup.getContext(), "wait",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(viewGroup.getContext(), QuestionActivityUser.class);
                intent.putExtra("setNum", position);
                intent.putExtra("categoryName", category);
                viewGroup.getContext().startActivity(intent);
            }
        });
        return view;
    }

}
