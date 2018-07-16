package com.example.beijing.qzszg;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ForgetPass extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup radioGroup;
    private int question_num;
    private EditText fg_username;
    private EditText fg_userpass;
    private Button verify;
    private String name;
    private String myanswer;
    private int tid;
    private String answer;
    private int ischeck=-1;

    private final int SUCCESS=100;
    private final int FAILED=999;
    private final int FAILED_ANSWER=555;
    private final String TAG="ForgetPass ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        fg_username = (EditText)findViewById(R.id.fg_username);
        fg_userpass=(EditText)findViewById(R.id.fg_userpass);
        verify=(Button)findViewById(R.id.button_verify);
        verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        name = fg_username.getText().toString().trim();
        myanswer = fg_userpass.getText().toString().trim();
        if (name == null || "".equals(name) || myanswer== null || "".equals(myanswer)) {
            Toast.makeText(ForgetPass.this, "用户名和验证信息不能为空", Toast.LENGTH_SHORT).show();
        }else{
            MyThread Signin = new MyThread(name,myanswer);
            Signin.start();
        }
    }

    private Handler secondhandler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case (SUCCESS):{
                    Toast.makeText(ForgetPass.this,"验证信息正确",Toast.LENGTH_SHORT).show();
                    Intent data = new Intent(ForgetPass.this,ChangePass.class);
                    data.putExtra("USERNAME",name);
                    startActivity(data);
                    finish();
                    break;
                }
                case (FAILED):
                    Toast.makeText(ForgetPass.this,"用户名不正确",Toast.LENGTH_SHORT).show();
                    break;
                case (FAILED_ANSWER):{
                    Toast.makeText(ForgetPass.this,"验证信息不正确",Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.qusetion1:
                question_num=1;
                break;
            case R.id.qusetion2:
                question_num=2;
                break;
            case R.id.qusetion3:
                question_num=3;
                break;
            case R.id.qusetion4:
                question_num=4;
                break;
            default:
                break;
        }
    }

    class MyThread extends Thread{
        private String name;
        private String pass;
        private Connection connection = null;
        private Message msg = new Message();

        private static final String driver_class="com.mysql.jdbc.Driver";
        private static final String driver_url="jdbc:mysql://你的数据库";
        private static final String database_user="数据库用户名";
        private static final String database_password="密码";

        MyThread(String name, String pass){
            this.name=name;
            this.pass=String.valueOf(pass);
        }

        public void run(){
            try{
                Class.forName(driver_class);
                connection = DriverManager.getConnection(driver_url,database_user,database_password);
                String sql = "select * from user where name =  '"+name+"'";

                Statement st=(Statement)connection.createStatement();
                ResultSet rs=st.executeQuery(sql);
                if(rs.next()==false){
                    msg.what=FAILED;
                    secondhandler.sendMessage(msg);
                }
                else{
                    tid = rs.getInt("verifyID");
                    answer = rs.getString("verifyCT");
                    if(tid==question_num && pass.equals(answer.replaceAll("'", "\""))){
                        msg.what=SUCCESS;
                        secondhandler.sendMessage(msg);
                    }
                    else {
                        msg.what=FAILED_ANSWER;
                        secondhandler.sendMessage(msg);
                    }
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
}
