package com.example.beijing.qzszg;

import android.util.Log;

import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 卢亮 on 2018/7/14.
 */

public class GetList extends Thread {
    private Set<Integer> errors = new HashSet<>();
    private Set<Integer> collections = new HashSet<>();
    private Set<Integer> havedone = new HashSet<>();
    private boolean finish;
    private int uid, donum;
    GetList(int uid) {
        this.uid = uid;
    }
    public void run()
    {
        finish = false;
        try {
            Connection conn = JDBCUtil.getConn(用户名, 密码);
            String sql="select havedone,error,collection from userinfo where uid="+String.valueOf(uid);
            Statement st=(Statement)conn.createStatement();
            ResultSet rs=st.executeQuery(sql);
            donum = 0;
            while(rs.next()){
                // String mybook=rs.getString("B_Name");
                if(rs.getString("havedone") != null){
                    String[] s1 = rs.getString("havedone").split(", ");
                    Log.i("TTTTT", s1[0]);
                    if (s1[0].compareTo("") != 0)
                        for(int j=0; j<s1.length; j++)
                            havedone.add(Integer.valueOf(s1[j]));
                    donum = s1.length;
                }
                if(rs.getString("error") != null){
                    String[] s2 = rs.getString("error").split(", ");
                    if (s2[0].compareTo("") != 0)
                        for(int j=0; j<s2.length; j++)
                            errors.add(Integer.valueOf(s2[j]));
                }
                if (rs.getString("collection") != null){
                    String[] s3 = rs.getString("collection").split(", ");
                    if (s3[0].compareTo("") != 0)
                        for(int j=0; j<s3.length; j++)
                            collections.add(Integer.valueOf(s3[j]));
                }
                CommonTid.collections = collections;
                CommonTid.errors = errors;
                CommonTid.havedone = havedone;
                CommonTid.donum = donum;
            }
            //Log.i("NNN",String.valueOf(list.size()));
            conn.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            //Log.d("GetTags"," sqlexception");
        }
        finish = true;
    }
    public boolean isFinish() { return finish; }
    public int getNum() { return donum; }
    public Set<Integer> getErrors() { return errors; }
    public Set<Integer> getDone() { return havedone; }
    public Set<Integer> getCollections() { return collections; }
}
