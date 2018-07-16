package com.example.beijing.qzszg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/7/13.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lv;
    private List<Q_SingleChoose> lists;
    private List<Integer> listsid;
    private ProContentsAdapter adapter;
    private static Context context;
    private ImageView submit;
    private EditText edittext;
    private int uid;

    private static final String TAG="SearchAcitivity ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d(TAG ,"create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = getApplicationContext();
        lv=(ListView)findViewById(R.id.content_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edittext= (EditText)findViewById(R.id.text_search);
        submit = (ImageView)findViewById(R.id.img_search);

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String text=edittext.getText().toString();
        if(!text.isEmpty()){
            searchResult(text);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void searchResult(String text){
        GetContents conts = new GetContents(text);
        conts.start();
        while(conts.is_finished()==false){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lists=conts.list;
        listsid=conts.listid;
        adapter = new ProContentsAdapter(lists, listsid, context);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view , int position, long id){

                Q_SingleChoose tmp = lists.get(position);
                Intent intent;
                Bundle bundle = new Bundle();
                List<Q_SingleChoose> problem = new ArrayList<>();
                problem.add(tmp);
                if (tmp.getAnswer().length() == 0){
                    intent = new Intent(SearchActivity.this, Subjective.class);
                    bundle.putSerializable("L_S", (Serializable) problem);
                    intent.putExtras(bundle);
                }
                else if(tmp.getAnswer().length() == 1){
                    intent = new Intent(SearchActivity.this, SingleChoose.class);
                    bundle.putSerializable("L", (Serializable) problem);
                    //Log.i("EEEE",String.valueOf(problem.size()));
                    intent.putExtras(bundle);
                    //intent.putExtra("Pos", position);
                }
                else{
                    intent = new Intent(SearchActivity.this, MulChoose.class);
                    bundle.putSerializable("L_M", (Serializable) problem);
                    intent.putExtras(bundle);
                }
                if (CommonTid.uid == -1)
                    intent.putExtra("Exte", 1);
                startActivity(intent);
            }
        });
    }
    public static Context getContext(){return context;}
}
