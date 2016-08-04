package com.example.ssalameh.volleyhttpproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//For Spinner in main activity
public class CustomAdapter extends BaseAdapter {
    Context context;
    int flags[];
    String[] Categories;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] flags, String[] Categories) {
        this.context = applicationContext;
        this.flags = flags;
        this.Categories = Categories;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return Categories[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(flags[i]);
        names.setText(Categories[i]);
        return view;
    }
}