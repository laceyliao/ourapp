package com.example.beijing.qzszg;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/7/14.
 */

public class GetContents extends Thread {
    public List<Q_SingleChoose> list = new ArrayList<>();
    public List<Integer> listid= new ArrayList<>();

    private boolean finish;
    private String text;
    private String tmp;
    public GetContents(String text){
        this.text = text;
    }
    public void run(){
        finish=false;
        try{
            Connection conn = JDBCUtil.getConn(用户名,密码);
            String sql = "select tid,question,answer,finalans,type from questions where question like '%"+text+"%'";
            Statement st=(Statement)conn.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while ((rs.next())){
                Q_SingleChoose Q = new Q_SingleChoose();
                int id = rs.getInt("tid");
                Q.setTid(id);
                Q.setDescribe(rs.getString("question"));
                String sections = rs.getString("answer");
                if(rs.getString("type").compareTo("1")==0) {
                    String[] section = sections.split("\n");
                    Q.setSectionA(section[0]);
                    Q.setSectionB(section[1]);
                    if (section.length > 2) {
                        Q.setSectionC(section[2]);
                        if (section.length > 3) {
                            Q.setSectionD(section[3]);
                            if (section.length > 4) {
                                Q.setSectionE(section[4]);
                                if (section.length > 5)
                                    Q.setSectionF(section[5]);
                            }
                        }
                    }
                    String ans = rs.getString("finalans").replaceAll("\n", "");
                    Q.setAnswer(ans);
                    list.add(Q);
                }
            }
            conn.close();
            st.close();
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
            Log.d("Getcontents ","is sqlexception");
        }
        finish=true;
    }
    public boolean is_finished(){return finish; }
}
