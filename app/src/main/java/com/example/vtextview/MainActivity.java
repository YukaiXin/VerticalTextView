package com.example.vtextview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxyu on 2020-02-20
 */
public class MainActivity extends AppCompatActivity {

    String str = "L我爱你H100";
    GridView listView;
    VerticalTextView verticalTextView;
    List<String> stringList = new ArrayList<>();
    TxtAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verticalTextView = findViewById(R.id.paintView);
        listView = findViewById(R.id.listview);
        verticalTextView.setText("满10.3元可用");

        for(int i = 0; i < 100 ; i ++) {
            if (i < 10){
                stringList.add("指定商品可用");
            }else if(i > 10 && i < 20){
                stringList.add("无限制");
            }else {
                stringList.add("满足"+i+"00元");
            }
        }
        listView.setAdapter(adapter = new TxtAdapter(this));
        adapter.setList(stringList);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
