package com.example.quizapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.QuestionActivity;
import com.example.quizapp.R;

public class GrideAdapter extends BaseAdapter {
    public int sets=0;
    private String category;
    private  String key;
    private GridListener listener;

    public GrideAdapter(int sets, String category, String key, GridListener listener) {
        this.sets = sets;
        this.category = category;
        this.key = key;
        this.listener = listener;
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
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sets,viewGroup,false);
        }
        else{
            view=convertView;
        }
        if(position==0){
            ((TextView)view.findViewById(R.id.set_name)).setText("+");
        }
        else{
            ((TextView)view.findViewById(R.id.set_name)).setText(String.valueOf(position));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    listener.addSets();
                }
                else{
//                    Toast.makeText(viewGroup.getContext(), "wait",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(viewGroup.getContext(), QuestionActivity.class);
                    intent.putExtra("setNum",position);
                    intent.putExtra("categoryName",category);
                    viewGroup.getContext().startActivity(intent);
                }
            }
        });
        return view;
    }
    public interface GridListener{
        public void addSets();
    }
}
