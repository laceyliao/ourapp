package com.example.beijing.qzszg;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Subjective extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    Button btn,ahead,next;
    TextView tv1,tv2;
    private int position=0, exte;
    private List<Q_SingleChoose> L_S ;
    private ImageView collect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjective);
        L_S = (List<Q_SingleChoose>) this.getIntent().getSerializableExtra("L_S");
        position = getIntent().getIntExtra("Pos", 0);
        exte = getIntent().getIntExtra("Exte", 0);

        collect = findViewById(R.id.img_star);
        editText = findViewById(R.id.et);
        btn = findViewById(R.id.btnSubjective);
        ahead  = findViewById(R.id.btnAhead);
        next = findViewById(R.id.btnNext);
        tv1 = findViewById(R.id.tvSubjective);
        tv1.setMovementMethod(new ScrollingMovementMethod());

        tv2 = findViewById(R.id.tvAnswer);
        tv2.setMovementMethod(new ScrollingMovementMethod());

        setaview();

        ahead.setOnClickListener(this);
        next.setOnClickListener(this);
        btn.setOnClickListener(this);
        collect.setOnClickListener(this);

    }

    private void Next() {
        if (position == L_S.size() - 1) {
            Toast.makeText(this, "已经是最后一题", Toast.LENGTH_SHORT).show();
        } else {
            position++;
            setaview();

        }
    }

    private void Ahead() {
        if (position == 0) {
            Toast.makeText(this, "已经是第一题", Toast.LENGTH_SHORT).show();
        } else {
            position--;
            setaview();
        }
    }

    private void setaview(){
        if(L_S.get(position).getCollected() == true) {
            collect.setColorFilter(Color.RED);
        }
        else{
            collect.setColorFilter(Color.BLACK);
        }
        editText.setText("请在此输入你的答案");
        tv1.setText(L_S.get(position).getDescribe());
        tv1.setMovementMethod(new ScrollingMovementMethod());

        tv2.setText("");
        tv2.setMovementMethod(new ScrollingMovementMethod());

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAhead:
                Ahead();
                break;
            case R.id.btnNext:
                Next();
                break;
            case R.id.btnSubjective:
                tv2.setText(L_S.get(position).getAnswer());
                break;
            case R.id.img_star:
                if(L_S.get(position).getCollected() == false){
                    L_S.get(position).setCollected(true);
                    if (exte == 0) CommonTid.collections.add(L_S.get(position).getTid());
                    collect.setColorFilter(Color.RED);
                    Toast.makeText(Subjective.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
                else{
                    L_S.get(position).setCollected(false);
                    if (exte == 0) CommonTid.collections.add(L_S.get(position).getTid());
                    collect.setColorFilter(Color.BLACK);
                    Toast.makeText(Subjective.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getIntent().getIntExtra("Exte", 0) == 0){

            ProblemInfo.tid = L_S.get(position).getTid();
            ProblemInfo.type = 3;
            //ProblemInfo.tag = tag;
            RefreshList refresh = new RefreshList();
            refresh.start();
            while (refresh.isFinish() == false) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("TEST", "destroy");
    }

}
