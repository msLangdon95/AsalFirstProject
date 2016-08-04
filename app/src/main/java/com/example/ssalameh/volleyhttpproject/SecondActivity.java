package com.example.ssalameh.volleyhttpproject;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SecondActivity extends ListActivity {
    SendToSecondActivity Recieved;
    MobileArrayAdapter adapter;
    ListView list;
    LinearLayout footerLayout;
    Button SeeMore;
    int limit;
    String City, Category;
    Bucket SeeMoreResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        list = (ListView) findViewById(android.R.id.list);
        footerLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.footerview, null);
        SeeMore = (Button) footerLayout.findViewById(R.id.btnGetMoreResults);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Recieved = null;
            } else {
                Bundle bundle = this.getIntent().getExtras();
                Recieved = (SendToSecondActivity) bundle.getSerializable("ParsedObject");
            }
        } else {
            Recieved = (SendToSecondActivity) savedInstanceState.getSerializable("ParsedObject");
        }
        City = Recieved.City;
        Category = Recieved.Category;
        limit = Recieved.ParsedObject.Names.size();
        SeeMoreResult = new Bucket();
        adapter = new MobileArrayAdapter(this, Recieved.ParsedObject);
        list.setAdapter(adapter);
        Log.d("ss",String.valueOf(Recieved.ParsedObject.Names.size()));
        if (!Recieved.ParsedObject.EmptyFlag && Recieved.ParsedObject.Names.size()>=10)
            list.addFooterView(footerLayout);
        SeeMore.setOnClickListener(new View.OnClickListener(){// SeeMoreButtonHandler
            public void onClick(View v) {
                SeeMore.setEnabled(false);
                try {
                    SeeMoreResult = new Parser().execute(new HttpRequest(City, Category).execute(limit+5).get()).get();
                    if (limit >= SeeMoreResult.Names.size()) {
                        Log.d("STATE", "NO MORE RESULTS TO SHOW");
                        Toast.makeText(getApplicationContext(), "No more results to show!", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.update(SeeMoreResult);
                        limit=SeeMoreResult.Names.size();
                        SeeMore.setEnabled(true);
                    }
                } catch (Exception e) {
                    Log.d("ERRORInSeeMore", e.getMessage());
                }
            }
        });
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String selectedValue = (String) adapter.getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
    }
}
