package com.example.beijing.qzszg;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetQ extends Thread {
    private int type;   //1为单选，2为多选，3为简答
    private String tag;
    private boolean finish = false;
    public List<Q_SingleChoose> L = new ArrayList<Q_SingleChoose>();
    public List<Q_SingleChoose> L_M = new ArrayList<Q_SingleChoose>();
    public List<Q_SingleChoose> L_S = new ArrayList<Q_SingleChoose>();
    public GetQ(String tag){
        this.tag = tag;
    }
    @Override
    public void run() {
        Connection cn = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://你的数据库",
                    数据库用户名, 密码);
            st = cn.createStatement();
            String str = "SELECT tid,question,answer,finalans,type FROM questions WHERE tag LIKE '%"+tag+"%'";
            ResultSet rs = st.executeQuery(str);
            while(rs.next()){
                Q_SingleChoose Q = new Q_SingleChoose();
                int id = rs.getInt("tid");
                Q.setTid(id);
                Q.setDescribe(rs.getString("question"));
                String sections = rs.getString("answer");

                if(rs.getString("type").compareTo("1")==0){
                    String []section = sections.split("\n");
                    Q.setSectionA(section[0]);
                    Q.setSectionB(section[1]);
                    if(section.length>2){
                        Q.setSectionC(section[2]);
                        if(section.length>3){
                            Q.setSectionD(section[3]);
                            if(section.length>4){
                                Q.setSectionE(section[4]);
                                if(section.length>5)
                                    Q.setSectionF(section[5]);
                            }
                        }
                    }
                    String ans = rs.getString("finalans").replaceAll("\n","");
                    Q.setAnswer(ans);
                    System.out.println(ans);
                    if(ans.length() == 1)
                    //    if(this.L.size()<10)
                            this.L.add(Q);
                    else
                  //      if(this.L_M.size()<10)
                            this.L_M.add(Q);
                }else{
                    Q.setAnswer(sections);
                //    if(this.L_S.size()<10)
                        this.L_S.add(Q);
                }
            }
            finish = true;
            rs.close();
            st.close();
            cn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean isFinish(){
        return finish;
    }


}
