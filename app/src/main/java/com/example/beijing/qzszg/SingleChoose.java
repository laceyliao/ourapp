package com.example.beijing.qzszg;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SingleChoose extends AppCompatActivity implements View.OnClickListener {

    private RadioButton btA, btB, btC, btD;
    private RadioButton btE, btF;
    private Button submit, ahead, next;
    private TextView tv, finalans, userans;
    private String answer, tag;
    private RadioGroup radioGroup;
    private int section_num = 4;
    private List<Q_SingleChoose> L = new ArrayList<Q_SingleChoose>();
    private ImageView collect;

//    private Q_SingleChoose Q0 = new Q_SingleChoose("emmmm", "A", "B", "C", "D", "", "", "D");
//    private Q_SingleChoose Q2 = new Q_SingleChoose("emmmm", "AA", "BB", "C", "D", "", "", "D");
//    private Q_SingleChoose Q1 = new Q_SingleChoose("hhhhh", "A", "BBBBB", "C", "D", "", "", "B");
//    private Q_SingleChoose Q3 = new Q_SingleChoose("hhhhh", "A", "B", "CCCCCC", "D", "", "", "B");
//      public static List<Q_SingleChoose> L = new ArrayList<Q_SingleChoose>();


    private int position = 0, exte;

    private void setaview() {
        tv.setMovementMethod(new ScrollingMovementMethod());
        removebutton();
        if(L.get(position).getCollected() == true) {
            collect.setColorFilter(Color.RED);
        }
        else{
            collect.setColorFilter(Color.BLACK);
        }
        if(L.get(position).getUserAnswer().length() == 0){
            finalans.setVisibility(View.GONE);
            userans.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            setUse(true);
        }
        else{
            String UA=L.get(position).getUserAnswer();
            for(int i=0; i<UA.length(); i++){
                if (UA.charAt(i) == 'A')  btA.setChecked(true);
                if (UA.charAt(i) == 'B')  btB.setChecked(true);
                if (UA.charAt(i) == 'C')  btC.setChecked(true);
                if (UA.charAt(i) == 'D')  btD.setChecked(true);
                if (UA.charAt(i) == 'E')  btE.setChecked(true);
                if (UA.charAt(i) == 'F')  btF.setChecked(true);
            }
            finalans.setText("正确答案："+L.get(position).getAnswer());
            finalans.setVisibility(View.VISIBLE);
            userans.setText("你的答案："+L.get(position).getUserAnswer());
            userans.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            setUse(false);
        }

        tv.setText(L.get(position).getDescribe());
        btA.setText(L.get(position).getSectionA());
        btB.setText(L.get(position).getSectionB());
        btC.setText(L.get(position).getSectionC());
        btD.setText(L.get(position).getSectionD());
        if (L.get(position).getSectionE().compareTo("")!=0) {
            btE = new RadioButton(this);
            btE.setText(L.get(position).getSectionE());
            radioGroup.addView(btE, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            if (L.get(position).getSectionF().compareTo("")==0) {
                section_num = 5;
            } else {
                section_num = 6;
                btF = new RadioButton(this);
                btF.setText(L.get(position).getSectionF());
                radioGroup.addView(btF, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            }
        } else {
            section_num = 4;
        }

    }

    private void setUse(boolean is){
        btA.setClickable(is);
        btB.setClickable(is);
        btC.setClickable(is);
        btD.setClickable(is);
        if(section_num>4)
            btE.setClickable(is);
        if (section_num>5)
            btF.setClickable(is);
    }

    private void removebutton() {
        radioGroup.clearCheck();
        if (section_num == 5) {
            radioGroup.removeView(btE);
        }else if(section_num == 6){
            radioGroup.removeView(btE);
            radioGroup.removeView(btF);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_choose);

        L = (List<Q_SingleChoose>) this.getIntent().getSerializableExtra("L");
        position = getIntent().getIntExtra("Pos", 0);
        exte = getIntent().getIntExtra("Exte", 0);

        //tag = getIntent().getStringExtra("Tag");
        collect = findViewById(R.id.img_star);

        radioGroup = findViewById(R.id.rg);
        tv = findViewById(R.id.describe);
        btA = findViewById(R.id.sectionA);
        btB = findViewById(R.id.sectionB);
        btC = findViewById(R.id.sectionC);
        btD = findViewById(R.id.sectionD);

        submit = findViewById(R.id.btnSubmit);
        ahead = findViewById(R.id.btnAhead);
        next = findViewById(R.id.btnNext);

        finalans = findViewById(R.id.final_ans);
        userans = findViewById(R.id.user_ans);

        ahead.setOnClickListener(this);
        next.setOnClickListener(this);
        submit.setOnClickListener(this);
        collect.setOnClickListener(this);
        setaview();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAhead:
                Ahead();
                break;
            case R.id.btnNext:
                Next();
                break;
            case R.id.btnSubmit:
                if (btA.isChecked()) {
                    answer = "A";
                } else if (btB.isChecked()) {
                    answer = "B";
                } else if (btC.isChecked()) {
                    answer = "C";
                } else if (btD.isChecked()) {
                    answer = "D";
                } else if (section_num == 5) {
                    if (btE.isChecked())
                        answer = "E";
                    else {
                        Toast.makeText(SingleChoose.this, "请选择答案", Toast.LENGTH_SHORT).show();
                        break;
                    }

                } else if (section_num == 6) {
                    if (btE.isChecked())
                        answer = "E";
                    else if (btF.isChecked())
                        answer = "F";
                    else {
                        Toast.makeText(SingleChoose.this, "请选择答案", Toast.LENGTH_SHORT).show();
                        break;
                    }

                } else {
                    Toast.makeText(SingleChoose.this, "请选择答案", Toast.LENGTH_SHORT).show();
                    break;
                }
                L.get(position).setUserAnswer(answer);
                if (exte == 0) CommonTid.havedone.add(L.get(position).getTid());
                if (answer.compareTo(L.get(position).getAnswer()) == 0) {
                    L.get(position).setUserAnswer(answer);
                    Toast.makeText(SingleChoose.this, "所选正确", Toast.LENGTH_SHORT).show();
                } else {
                    L.get(position).setUserAnswer(answer);
                    if (exte == 0) CommonTid.errors.add(L.get(position).getTid());
                    Toast.makeText(SingleChoose.this, "所选错误", Toast.LENGTH_SHORT).show();
                }
                Log.i("TEST","answer");
                submit.setVisibility(View.GONE);
                finalans.setText("正确答案："+L.get(position).getAnswer());
                finalans.setVisibility(View.VISIBLE);
                userans.setText("你的答案："+L.get(position).getUserAnswer());
                userans.setVisibility(View.VISIBLE);
                setUse(false);
                break;
            case R.id.img_star:
                if(L.get(position).getCollected() == false){
                    L.get(position).setCollected(true);
                    if (exte == 0) CommonTid.collections.add(L.get(position).getTid());
                    collect.setColorFilter(Color.RED);
                    Toast.makeText(SingleChoose.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
                else{
                    L.get(position).setCollected(false);
                    if (exte == 0) CommonTid.collections.add(L.get(position).getTid());
                    collect.setColorFilter(Color.BLACK);
                    Toast.makeText(SingleChoose.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exte == 0){
            ProblemInfo.tid = L.get(position).getTid();
            ProblemInfo.type = 1;
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TEST", "stop");
    }

    private void Next() {
        if (position == L.size() - 1) {
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

}

