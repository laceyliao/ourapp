package com.example.beijing.qzszg;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    private EditText editText;
    private EditText editText2;
    private TextView forgetTV;
    private Button button;
    private Button button2;
    private static final String TAG = "LoginActivity";
    private static final int SUCCESS = 1;
    private static final int FAILED=2;

    private String name;
    private String pass;
    private int uid;
    private int donum=0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    name = data.getStringExtra("name");
                    pass = data.getStringExtra("psd");
                    editText.setText(name);
                    editText2.setText(pass);
                }
                break;
            default:break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        Intent data = new Intent();
//        data.putExtra("name",name);
//        data.putExtra("psd",pass);
//        data.putExtra("state","logined");
//        setResult(RESULT_OK,data);
//        finish();
//    }

    @SuppressLint("HandlerLeak")
    private Handler secondhandler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case (SUCCESS):
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    GetList getlist = new GetList(uid);
                    Log.i("AAA","TTTTT");
                    getlist.start();
                    while (getlist.isFinish() == false) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    CommonTid.uid = uid;
                    CommonTid.errors = getlist.getErrors();
                    CommonTid.havedone= getlist.getDone();
                    CommonTid.collections = getlist.getCollections();
                    Log.i("AAA", String.valueOf(CommonTid.collections.size()));
                    Intent data = new Intent();
                    data.putExtra("UID",uid);
                    data.putExtra("USERNAME",name);
                    data.putExtra("DO",donum);
                    setResult(RESULT_OK,data);
                    finish();
                    break;
                case (FAILED):
                    Toast.makeText(LoginActivity.this,"账号密码不匹配",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    class MyThread extends Thread{
        private String name;
        private String pass;
        private int hashpass;
        private Connection connection = null;
        private Message msg = new Message();

        private static final String driver_class="com.mysql.jdbc.Driver";
        private static final String driver_url="jdbc:mysql://你的数据库";
        private static final String database_user=数据库用户名;
        private static final String database_password=密码;

        MyThread(String name, String pass){
            this.name=name;
            this.hashpass = pass.hashCode();
            this.pass=String.valueOf(hashpass);
        }

        public void run(){
            try{
                Class.forName(driver_class);
                connection = DriverManager.getConnection(driver_url,database_user,database_password);
                String sql = "select uid from user where name =  '"+name+"' and psd = '"+pass+"'";

                Statement st=(Statement)connection.createStatement();
                ResultSet rs=st.executeQuery(sql);
                if(rs.next()==false){
                    msg.what=FAILED;
                    secondhandler.sendMessage(msg);
                }
                else{
                    uid = rs.getInt(1);
                    Log.i("IN", String.valueOf(uid));
                    String sql2 = "select donum from userinfo where uid = "+String.valueOf(uid);
                    ResultSet rs2=st.executeQuery(sql2);
                    if ((rs2.next())) {
                        donum = rs2.getInt(1);
                    }
                    msg.what=SUCCESS;
                    secondhandler.sendMessage(msg);
                }
                connection.close();//一定要关闭
                st.close();
                rs.close();
            }catch (ClassNotFoundException e){
                Log.d(TAG,"can't find the database");
                e.printStackTrace();
            }catch (SQLException e){
                Log.d(TAG,"it is sqlexception");
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);
        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        forgetTV=(TextView)findViewById(R.id.forget_pass);
        forgetTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ZhuCeActivity.class);
                startActivityForResult(intent,1);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editText.getText().toString().trim();
                pass = editText2.getText().toString().trim();

                if (name == null || "".equals(name) || pass == null || "".equals(pass)) {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    MyThread Signin = new MyThread(name,pass);
                    Signin.start();
                }
            }
        });
        forgetTV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetPass.class);
                startActivity(intent);
            }
        });
    }
}
