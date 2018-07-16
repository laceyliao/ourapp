package com.example.beijing.qzszg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/7/14.
 */

public class WrongActivity extends AppCompatActivity {
    private static Context context;
    private ListView lv;
    private List<Q_SingleChoose> lists;
    private List<Integer> listsid;
    private ProWrongsAdapter adapter;
    private static final int WRONG=999;
    private int userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong);
        Intent intent = getIntent();
        userID = intent.getIntExtra("UID",-1);
        context = getApplicationContext();
        lv=(ListView)findViewById(R.id.content_wrong);
        Toolbar toolbar = (Toolbar) findViewById(R.id.wrong_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchResult();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ProblemInfo.tid = L.get(position).getTid();
        if(userID<0) return;
        ProblemInfo.type = 0;
        ProblemInfo.tag = "";
        RefreshList refresh = new RefreshList();
        refresh.start();
        while (refresh.isFinish() == false) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("TEST", "errordestroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchResult(){
        Log.d("WrongActivity ","searchResult");
        if(userID < 0 ){
            Toast.makeText(WrongActivity.this,"您还未登录，请先登录",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
       //     Log.d("WrongActivity ",String.valueOf(userID));
            GetQuestionByTID conts = new GetQuestionByTID(CommonTid.errors);
            conts.start();
            while(conts.isFinished()==false){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lists=conts.getQuestions();
            //listsid=conts.listid;
            adapter = new ProWrongsAdapter(lists,listsid,context);
            lv.setAdapter(adapter);

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(WrongActivity.this);
                    builder.setMessage("确定删除？");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //if(listsid.remove(listsid.get(position))){
                            if(CommonTid.errors.remove(lists.get(position).getTid())){
                                lists.remove(lists.get(position));
                                Toast.makeText(WrongActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(WrongActivity.this,"删除不成功",Toast.LENGTH_SHORT).show();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                    return true;
                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view , int position, long id){

                    //Log.d("CollectActivity ",String .valueOf(listsid.get(position)));
                    Q_SingleChoose tmp = lists.get(position);
                    Intent intent;
                    Bundle bundle = new Bundle();
                    List<Q_SingleChoose> problem = new ArrayList<>();
                    problem.add(tmp);
                    if (tmp.getSectionA().length() == 0){
                        intent = new Intent(WrongActivity.this, Subjective.class);
                        bundle.putSerializable("L_S", (Serializable) problem);
                        intent.putExtras(bundle);
                    }
                    else if(tmp.getAnswer().length() == 1){
                        intent = new Intent(WrongActivity.this, SingleChoose.class);
                        bundle.putSerializable("L", (Serializable) problem);
                        //Log.i("EEEE",String.valueOf(problem.size()));
                        intent.putExtras(bundle);
                        //intent.putExtra("Pos", position);
                    }
                    else{
                        intent = new Intent(WrongActivity.this, MulChoose.class);
                        bundle.putSerializable("L_M", (Serializable) problem);
                        intent.putExtras(bundle);
                    }
                    intent.putExtra("Exte", 1);
                    startActivity(intent);
                }
            });
        }

    }
    public static Context getContext(){return context;}
}
