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
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class ChangePass extends AppCompatActivity {

    private int uid;
    private String name;
    private EditText newpass;
    private EditText newpass2;
    private Button new_verify;
    private String pass,pass2;

    private final int SUCCESS=100;
    private final int FAILED=999;
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
    private final String TAG ="ChangePass ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        Intent intent = getIntent();
        name=intent.getStringExtra("USERNAME");
        newpass=(EditText)findViewById(R.id.new_pass);
        newpass2=(EditText)findViewById(R.id.new_pass2);
        new_verify = (Button)findViewById(R.id.change_verify);
        new_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass=newpass.getText().toString();
                pass2=newpass2.getText().toString();

                if(pass.equals(pass2)==false)
                {
                    Toast.makeText(ChangePass.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                }else {
                    boolean judge1 = isPassword(pass);
                    if (judge1 == true) {
                        MyThread Signup = new MyThread(name,pass);
                        Signup.start();

                    } else {
                        Toast.makeText(ChangePass.this, "密码不能有特殊符号", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private Handler secondhandler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case (SUCCESS):
                    Toast.makeText(ChangePass.this,"密码修改成功，请返回登录页面",Toast.LENGTH_SHORT).show();
                    Intent data = new Intent(ChangePass.this,LoginActivity.class);
                    startActivity(data);
                    finish();
                    break;
                case (FAILED):
                    Toast.makeText(ChangePass.this,"密码修改不成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    class MyThread extends Thread{
        private static final String driver_class = "com.mysql.jdbc.Driver";
        private static final String driver_url = "jdbc:mysql://你的数据库";
        private static final String database_user = "数据库用户名";
        private static final String database_password = "密码";
        private String name;
        private String pass;
        private int hashpass;
        private Connection connection = null;
        private Message msg=new Message();

        MyThread(String name, String pass) {
            this.name = name;
            this.hashpass = pass.hashCode();
            this.pass = String.valueOf(hashpass);
        }

        public void run()
        {
            try {
                Class.forName(driver_class);
                connection = DriverManager.getConnection(driver_url, database_user, database_password);
                String sql = "update user set psd='"+pass+"'where name='" + name +"'";
                Statement st = connection.createStatement();
                int result = st.executeUpdate(sql);
                if (result==1) {
                    Log.d(TAG,"update process");
                    msg.what=SUCCESS;
                    secondhandler.sendMessage(msg);

                } else {
                    msg.what=FAILED;
                    secondhandler.sendMessage(msg);
                }
                connection.close();//一定要关闭
                st.close();
            } catch (ClassNotFoundException e) {
                Log.d(TAG, "can't find this database");
                e.printStackTrace();
            } catch (SQLException e) {
                Log.d(TAG, "it is sqlexception");
                e.printStackTrace();
            }
        }
    }

    private static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }
}
