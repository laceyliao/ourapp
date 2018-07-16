package com.example.beijing.qzszg;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class SelectTag extends AppCompatActivity {
    private ListView lv;
    private List<String> lists;
    private ProTagsAdapter adapter;
    private static Context context;
    private int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        context = getApplicationContext();
        //uid = 6;
        Intent getuid = getIntent();
        uid = getuid.getIntExtra("Uid", 6);
        //Log.i("UID",String.valueOf(uid));
        CommonTid.uid = uid;
        lv = findViewById(R.id.tag_select);
        GetTags tgs = new GetTags();
        tgs.start();
        while (tgs.is_finished() == false){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(uid == -1){
            GetList getlist = new GetList(uid);
            Log.i("before", "getlist");
            getlist.start();
            Log.i("after","get");
            while (getlist.isFinish() == false) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            CommonTid.errors = getlist.getErrors();
            CommonTid.havedone= getlist.getDone();
            CommonTid.collections = getlist.getCollections();
        }
        lists = tgs.list;
        adapter = new ProTagsAdapter(lists, context);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectTag.this, AnswerMainAty.class);
                intent.putExtra("Tag", lists.get(position));
                ProblemInfo.tag = lists.get(position);
                intent.putExtra("Uid", uid);
                startActivity(intent);
            }
        });
    }
    public static Context getContext(){return context;}
}
