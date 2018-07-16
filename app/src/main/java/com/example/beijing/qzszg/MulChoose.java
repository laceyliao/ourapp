package com.example.beijing.qzszg;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MulChoose extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout root;
    private TextView textView, finalans, userans;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6;
    private Button bt, ahead, next;
    private String str;
    private int section_num = 4;
    private List<Q_SingleChoose> L_M ;
    private ImageView collect;

    private int position = 0, exte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Intent i = new Intent(MulChoose.this,SingleChoose.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mul_choose);

        L_M = (List<Q_SingleChoose>) this.getIntent().getSerializableExtra("L_M");
        position = getIntent().getIntExtra("Pos", 0);
        exte = getIntent().getIntExtra("Exte", 0);

        root = findViewById(R.id.linearL);

        collect = findViewById(R.id.img_star);
        textView = findViewById(R.id.textView);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);

        finalans = findViewById(R.id.final_ans);
        userans = findViewById(R.id.user_ans);


        bt = findViewById(R.id.btnSubmit);

        ahead = findViewById(R.id.btnAhead);
        next = findViewById(R.id.btnNext);


        ahead.setOnClickListener(this);
        next.setOnClickListener(this);
        bt.setOnClickListener(this);
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
                str = "";

                if (cb1.isChecked()) {
                    str += "A";
                }
                if (cb2.isChecked()) {
                    str += "B";
                }
                if (cb3.isChecked()) {
                    str += "C";
                }
                if (cb4.isChecked()) {
                    str += "D";
                }

                if (section_num == 5) {
                    if (cb5.isChecked())
                        str += "E";
                } else if (section_num == 6) {
                    if (cb5.isChecked())
                        str += "E";
                    if (cb6.isChecked())
                        str += "F";
                }


                if (str == "") {
                    Toast.makeText(MulChoose.this, "请选择答案", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    L_M.get(position).setUserAnswer(str);
                    if (exte == 0) CommonTid.havedone.add(L_M.get(position).getTid());
                    if (str.compareTo(L_M.get(position).getAnswer()) == 0) {
                        Toast.makeText(MulChoose.this, "所选正确", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MulChoose.this, "所选错误", Toast.LENGTH_SHORT).show();
                        CommonTid.errors.add(L_M.get(position).getTid());
                    }
                    bt.setVisibility(View.GONE);
                    finalans.setText("正确答案："+L_M.get(position).getAnswer());
                    finalans.setVisibility(View.VISIBLE);
                    userans.setText("你的答案："+L_M.get(position).getUserAnswer());
                    userans.setVisibility(View.VISIBLE);
                    setUse(false);
                    break;
                }
            case R.id.img_star:
                if(L_M.get(position).getCollected() == false){
                    L_M.get(position).setCollected(true);
                    if (exte == 0) CommonTid.collections.add(L_M.get(position).getTid());
                    collect.setColorFilter(Color.RED);
                    Toast.makeText(MulChoose.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
                else{
                    L_M.get(position).setCollected(false);
                    if (exte == 0) CommonTid.collections.add(L_M.get(position).getTid());
                    collect.setColorFilter(Color.BLACK);
                    Toast.makeText(MulChoose.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getIntent().getIntExtra("Exte", 0) == 0){
            ProblemInfo.tid = L_M.get(position).getTid();
            ProblemInfo.type = 2;
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


    private void Next() {
        if (position == L_M.size() - 1) {
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

    private void setaview() {

        removebutton();

        textView.setText(L_M.get(position).getDescribe());
        textView.setMovementMethod(new ScrollingMovementMethod());

        if(L_M.get(position).getCollected() == true) {
            collect.setColorFilter(Color.RED);
        }
        else{
            collect.setColorFilter(Color.BLACK);
        }
        if(L_M.get(position).getUserAnswer().length() == 0){
            finalans.setVisibility(View.GONE);
            userans.setVisibility(View.GONE);
            bt.setVisibility(View.VISIBLE);
            setUse(true);
        }
        else{
            String UA=L_M.get(position).getUserAnswer();
            for(int i=0; i<UA.length(); i++){
                if (UA.charAt(i) == 'A')  cb1.setChecked(true);
                if (UA.charAt(i) == 'B')  cb2.setChecked(true);
                if (UA.charAt(i) == 'C')  cb3.setChecked(true);
                if (UA.charAt(i) == 'D')  cb4.setChecked(true);
                if (UA.charAt(i) == 'E')  cb5.setChecked(true);
                if (UA.charAt(i) == 'F')  cb6.setChecked(true);
            }
            finalans.setText("正确答案："+L_M.get(position).getAnswer());
            finalans.setVisibility(View.VISIBLE);
            userans.setText("你的答案："+L_M.get(position).getUserAnswer());
            userans.setVisibility(View.VISIBLE);
            bt.setVisibility(View.GONE);
            setUse(false);
        }

        cb1.setText(L_M.get(position).getSectionA());
        cb2.setText(L_M.get(position).getSectionB());
        cb3.setText(L_M.get(position).getSectionC());
        cb4.setText(L_M.get(position).getSectionD());
        if (L_M.get(position).getSectionE().compareTo("") != 0 ) {
            cb5 = new CheckBox(this);
            cb5.setText(L_M.get(position).getSectionE());
            root.addView(cb5, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (L_M.get(position).getSectionF().compareTo("") == 0) {
                section_num = 5;
            } else {
                section_num = 6;
                cb6 = new CheckBox(this);
                cb6.setText(L_M.get(position).getSectionF());
                root.addView(cb6, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        } else {
            section_num = 4;
        }

    }

    private void setUse(boolean is){
        cb1.setClickable(is);
        cb2.setClickable(is);
        cb3.setClickable(is);
        cb4.setClickable(is);
        if(section_num>4)
            cb5.setClickable(is);
        if (section_num>5)
            cb6.setClickable(is);
    }

    private void removebutton() {
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);
        if (section_num == 5) {
            root.removeView(cb5);
        } else if (section_num == 6) {
            root.removeView(cb5);
            root.removeView(cb6);
        }
    }

}


