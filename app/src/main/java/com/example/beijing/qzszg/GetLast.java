package com.example.beijing.qzszg;

import android.util.Log;

import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 卢亮 on 2018/7/14.
 */

public class GetLast extends Thread {
    private boolean finish;
    private int tid;
    public void run(){
        finish = false;
        try {
            Connection conn = JDBCUtil.getConn(用户名, 密码);
            Statement st=(Statement)conn.createStatement();
            Log.i("TEST", "连接完成");
            ResultSet rs;
            if(ProblemInfo.type ==1){
                Log.i("TEST", "bbb");
                rs = st.executeQuery("select lasttid from userques where uid="+String.valueOf(CommonTid.uid)+" and tag='"+ProblemInfo.tag+"'");
                if(rs.next()){
                    // String mybook=rs.getString("B_Name");
                    tid = rs.getInt("lasttid");
                }
                else tid=0;
                Log.i("TEST","aaa");
            }
            else if(ProblemInfo.type ==2){
                rs = st.executeQuery("select lasttid2 from userques where uid="+String.valueOf(CommonTid.uid)+" and tag='"+ProblemInfo.tag+"'");
                if(rs.next()){
                    // String mybook=rs.getString("B_Name");
                    tid = rs.getInt("lasttid2");
                }
                else tid=0;
            }
            else{
                rs = st.executeQuery("select lasttid3 from userques where uid="+String.valueOf(CommonTid.uid)+" and tag='"+ProblemInfo.tag+"'");
                if(rs.next()){
                    // String mybook=rs.getString("B_Name");
                    tid = rs.getInt("lasttid3");
                }
                else tid=0;
            }
            //Log.i("NNN",String.valueOf(list.size()));
            conn.close();
            st.close();
            rs.close();
            Log.i("TEST", "cccc");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("GetTags"," sqlexception");
        }

        finish = true;
        Log.i("TEST","dddd");
    }
    public boolean isFinish() { return finish; }
    public int getTid() { return tid; }
}
