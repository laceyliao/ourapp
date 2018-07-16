package com.example.beijing.qzszg;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2018/7/14.
 */

public class GetQuestionByTID extends Thread{

    private List<Q_SingleChoose> quescontent=new ArrayList<>();
    private Set<Integer> listid;
    private String tmp="1",sql;
    private boolean finished;
    public GetQuestionByTID(Set<Integer> listid){
        this.listid = listid;
    }
    public void run(){
        finished = false;
        try{
            Connection conn = JDBCUtil.getConn(用户名,密码);
            sql = "select tid,question,answer,finalans,type from questions where tid =";
            Statement st = (Statement)conn.createStatement();
            ResultSet rs;
            for(Integer i :listid){
                rs=st.executeQuery(sql+String.valueOf(i));
                if((rs.next())){
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
                        //Q.setAnswer(ans);
                    }
                    Q.setAnswer(sections);
                    quescontent.add(Q);
                }
                rs.close();
            }
            conn.close();
            st.close();
        } catch (SQLException e){
            e.printStackTrace();
            Log.d("GetQuestionByTID ","is sqlexception");
        }
        finished = true;
    }
    public List<Q_SingleChoose> getQuestions(){
        return quescontent;
    }
    public boolean isFinished() { return finished; }
}
