package com.example.beijing.qzszg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Created by admin on 2018/6/4.
 */

public class ZhuCeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private Button button3;
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
    private static final String TAG = "ZhuCeActivity";

    private final int SUCCESS = 1;
    private final int FAILED = 2;
    private String name;
    private String pass;

    private RadioGroup radioGroup;
    private String answer;
    private EditText fg_userpass;
    private int question_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        radioGroup=(RadioGroup)findViewById(R.id.radio_group2);
        radioGroup.setOnCheckedChangeListener(this);

        fg_userpass=(EditText)findViewById(R.id.input_userpass);

        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editText3.getText().toString().trim();
                pass = editText4.getText().toString().trim();
                String pass2 = editText5.getText().toString().trim();
                answer = fg_userpass.getText().toString().trim();

                if (name == null || "".equals(name) || pass == null || "".equals(pass)) {
                    Toast.makeText(ZhuCeActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                }else  if(pass.equals(pass2)==false)
                {
                    Toast.makeText(ZhuCeActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean judge = isMobile(name);
                    boolean judge1 = isPassword(pass);
                    if (judge == true && judge1 == true) {
                        MyThread Signup = new MyThread(name,pass,question_num,answer);
                        Signup.start();

                    } else {
                        Toast.makeText(ZhuCeActivity.this, "用户名与密码不能有特殊符号", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private Handler secondhandler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case (SUCCESS):
                    Toast.makeText(ZhuCeActivity.this,"注册成功，请返回登录页面",Toast.LENGTH_SHORT).show();
                    Intent data = new Intent();
                    data.putExtra("name",name);
                    data.putExtra("psd",pass);
                    setResult(RESULT_OK,data);
                    finish();
                    break;
                case (FAILED):
                    Toast.makeText(ZhuCeActivity.this,"此账号已被注册",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    private static boolean isMobile(String number) {
        String num = "[a-zA-Z][_a-zA-Z0-9]{5,19}$";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.qusetion11:
                question_num=1;
                break;
            case R.id.qusetion22:
                question_num=2;
                break;
            case R.id.qusetion33:
                question_num=3;
                break;
            case R.id.qusetion44:
                question_num=4;
                break;
            default:
                break;
        }
    }
    class MyThread extends Thread{
        private static final String driver_class = "com.mysql.jdbc.Driver";
        private static final String driver_url = "jdbc:mysql://你的数据库";
        private static final String database_user = 数据库用户名;
        private static final String database_password = 密码;
        private String name;
        private String pass;
        private int hashpass;
        private int uid;
        private int q_num;
        private String myanswer;
        private Connection connection = null;
        private Message msg=new Message();

        MyThread(String name, String pass,int q_num,String myanswer) {
            this.name = name;
            this.hashpass = pass.hashCode();
            this.pass = String.valueOf(hashpass);
            this.q_num=q_num;
            this.myanswer=myanswer;
        }

        public void run()
        {
            try {
                Class.forName(driver_class);
                connection = DriverManager.getConnection(driver_url, database_user, database_password);
                String sql = "select * from user where name = '" + name +"'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.next() == false) {
                    Log.d(TAG,"insert process");
                    String insert = "insert into user( name,psd,verifyID,verifyCT)" + "values('" + name + "','" + pass + "','"+String.valueOf(q_num)+"','"+myanswer.replaceAll("'", "\"")+"')";
                    st.executeUpdate(insert);
                    Log.d(TAG,"insert successfully");
                    String sql2 = "select uid from user where name = '"+name+"'";

                    rs = st.executeQuery(sql2);
                    if(rs.next())
                        uid = rs.getInt(1);
                    Log.d(TAG,String.valueOf(uid));

                    String insert2 = "insert into userinfo(uid)"+"values("+ String.valueOf(uid)+")";
                    st.executeUpdate(insert2);
                    Log.d(TAG,"insert userinfo");

                    msg.what=SUCCESS;
                    secondhandler.sendMessage(msg);

                } else {
                    msg.what=FAILED;
                    secondhandler.sendMessage(msg);
                }
                connection.close();//一定要关闭
                st.close();
                rs.close();
            } catch (ClassNotFoundException e) {
                Log.d(TAG, "can't find this database");
                e.printStackTrace();
            } catch (SQLException e) {
                Log.d(TAG, "it is sqlexception");
                e.printStackTrace();
            }
        }
    }
};


//            try {
//                test(connection);    //测试数据库连接
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }


//    public void test(Connection con1) throws SQLException {
//        try {
//            String sql = "select * from user";        //查询表名为“user”的所有内容
//            Statement stmt = con1.createStatement();        //创建Statement
//            ResultSet rs = stmt.executeQuery(sql);          //ResultSet类似Cursor
//
//            //<code>ResultSet</code>最初指向第一行
//            Bundle bundle = new Bundle();
//            while (rs.next()) {
//                bundle.clear();
//                bundle.putString("username", rs.getString("name"));
//                bundle.putString("userpass", rs.getString("psd"));
//                Message msg = new Message();
//                msg.setData(bundle);
//                handler.sendMessage(msg);
//            }
//            rs.close();
//            stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (con1 != null)
//                try {
//                    con1.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//        }
//    }