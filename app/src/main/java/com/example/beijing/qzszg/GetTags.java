package com.example.beijing.qzszg;

import android.util.Log;

import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 卢亮 on 2018/7/13.
 */

public class GetTags extends Thread {
    public List<String> list = new ArrayList<>();
    private boolean finish;
    public void run() {
        finish = false;
        try {
            Connection conn = JDBCUtil.getConn(用户名, 密码);
            String sql="select tag from tags";
            Statement st=(Statement)conn.createStatement();
            ResultSet rs=st.executeQuery(sql);
            int i=0;
            while(rs.next()){
                // String mybook=rs.getString("B_Name");
                list.add(rs.getString("tag"));
            }
            //Log.i("NNN",String.valueOf(list.size()));
            conn.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("GetTags"," sqlexception");
        }
        finish = true;
    }
    public boolean is_finished(){return finish;}
}
