package com.example.ssalameh.volleyhttpproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
public class MobileArrayAdapter extends BaseAdapter {
    private final Context context;
    Bucket ParsedObj;
    public MobileArrayAdapter(Context context, Bucket ParsedObj) {
        this.context = context;
        this.ParsedObj=ParsedObj;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_dummy, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textView3);
        textView.setText(ParsedObj.Names.get(position));
        Picasso.with(context)
                .load(ParsedObj.PicURL.get(position))
                .into((ImageView) rowView.findViewById(R.id.imageView2));
        return rowView;
    }

    @Override
    public int getCount() { return ParsedObj.Names.size(); }

    @Override
    public Object getItem(int position) { return ParsedObj.Names.get(position); }

    @Override
    public long getItemId(int position) { return 0; }

    void update(Bucket newBucket){
        this.ParsedObj.Names.clear();
        this.ParsedObj.PicURL.clear();
        this.ParsedObj=newBucket;
        this.notifyDataSetChanged();
    }
}