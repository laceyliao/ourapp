package com.example.beijing.qzszg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class AnswerMainAty extends AppCompatActivity implements View.OnClickListener {

    private GetQ getQ;
    private Intent intent;

//    private GetQ getQ = new GetQ(this.getIntent().getStringExtra("name"));

    private Button single,multi,subjective;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_main_aty);
        intent = getIntent();
        getQ = new GetQ(intent.getStringExtra("Tag"));
        getQ.start();
        while (!getQ.isFinish()) {
            try {
                Toast.makeText(this,"正在生成题库，请稍等...",Toast.LENGTH_LONG).show();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        single = findViewById(R.id.btnSingle);
        multi = findViewById(R.id.btnMulti);
        subjective = findViewById(R.id.btnSubjective);



        single.setOnClickListener(this);
        multi.setOnClickListener(this);
        subjective.setOnClickListener(this);

    }

    private int getPos(List<Q_SingleChoose> list){
        int pos = 0;
        GetLast getlast = new GetLast();
        getlast.start();
        while (getlast.isFinish() == false) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("TTTT","4444");
        for (int i=0; i<list.size(); i++){
            if(list.get(i).getTid() == getlast.getTid()){
                pos = i;
                break;
            }
//            else if (list.get(i).getTid() > getlast.getTid())
//                break;
        }
        return pos;
    }

    public void onClick(View v) {
        List<Q_SingleChoose> L = getQ.L;
        List<Q_SingleChoose> L_M = getQ.L_M;
        List<Q_SingleChoose> L_S = getQ.L_S;
        Bundle bundle = new Bundle();
        int pos;
        bundle.putSerializable("L", (Serializable) L);
        bundle.putSerializable("L_M", (Serializable) L_M);
        bundle.putSerializable("L_S", (Serializable) L_S);
        switch (v.getId()) {
            case R.id.btnSingle:
                if(L.size() == 0){
                    Toast.makeText(this,"此类题目没有单选题，请选择其他题目",Toast.LENGTH_SHORT).show();
                    break;
                }

                ProblemInfo.type = 1;
                Log.i("TTTT", "2333");
                pos = getPos(L);
                Log.i("TTTT","5555");
                Intent i_single = new Intent(AnswerMainAty.this,SingleChoose.class);
                i_single.putExtras(bundle);
                i_single.putExtra("Uid",intent.getIntExtra("Uid", -1));
                i_single.putExtra("Tag", intent.getStringExtra("Tag"));
                i_single.putExtra("Pos", pos);
                startActivity(i_single);
                break;
            case R.id.btnMulti:
                if(L_M.size() == 0){
                    Toast.makeText(this,"此类题目没有多选题，请选择其他题目",Toast.LENGTH_SHORT).show();
                    break;
                }
                ProblemInfo.type = 2;
                pos = getPos(L_M);
                Intent i_multi = new Intent(AnswerMainAty.this,MulChoose.class);
                i_multi.putExtras(bundle);
                i_multi.putExtra("Uid",intent.getIntExtra("Uid", -1));
                i_multi.putExtra("Tag", intent.getStringExtra("Tag"));
                i_multi.putExtra("Pos", pos);
                startActivity(i_multi);
                break;
            case R.id.btnSubjective:
                if(L_S.size() == 0){
                    Toast.makeText(this,"此类题目没有简答题，请选择其他题目",Toast.LENGTH_SHORT).show();
                    break;
                }
                ProblemInfo.type = 3;
                pos = getPos(L_S);
                Intent i_subjective = new Intent(AnswerMainAty.this,Subjective.class);
                i_subjective.putExtras(bundle);
                i_subjective.putExtra("Uid",intent.getIntExtra("Uid", -1));
                i_subjective.putExtra("Tag", intent.getStringExtra("Tag"));
                i_subjective.putExtra("Pos", pos);
                startActivity(i_subjective);
                break;
        }
    }


}
