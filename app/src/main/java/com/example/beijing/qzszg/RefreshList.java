package com.example.beijing.qzszg;

import android.util.Log;

import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 卢亮 on 2018/7/14.
 */

public class RefreshList extends Thread {
    private boolean finish;
    public void run(){
        finish = false;
        try {
            Connection conn = JDBCUtil.getConn(用户名, 密码);
            String s1 = CommonTid.havedone.toString().replace("[", "").replace("]", "");
            String s2 = CommonTid.errors.toString().replace("[", "").replace("]", "");
            String s3 = CommonTid.collections.toString().replace("[", "").replace("]", "");
            String sql="update userinfo set havedone='"+s1+"', error='"+s2+"', collection='"+s3+"', donum="+String.valueOf(CommonTid.havedone.size())+" where uid="+String.valueOf(CommonTid.uid);
            Statement st=(Statement)conn.createStatement();
            st.execute(sql);
            //st.close();
            //st = (Statement)conn.createStatement();;
            Log.i("SQL", "before");
            ResultSet rs=st.executeQuery("select uid from userques where uid="+String.valueOf(CommonTid.uid)+" and tag='"+ProblemInfo.tag+"'");
            if(rs.next()) {
                if(ProblemInfo.type == 1)
                    st.execute("update userques set lasttid="+String.valueOf(ProblemInfo.tid)+" where uid="+String.valueOf(CommonTid.uid)+" and tag='"+ProblemInfo.tag+"'");
                else if(ProblemInfo.type == 2)
                    st.execute("update userques set lasttid2="+String.valueOf(ProblemInfo.tid)+" where uid="+String.valueOf(CommonTid.uid)+" and tag='"+ProblemInfo.tag+"'");
                else if (ProblemInfo.type == 3)
                    st.execute("update userques set lasttid3="+String.valueOf(ProblemInfo.tid)+" where uid="+String.valueOf(CommonTid.uid)+" and tag='"+ProblemInfo.tag+"'");
                else {}
            }
            else{
                Log.i("SQL", "insert");
                if(ProblemInfo.type == 1)
                    st.execute("insert into userques(uid, tag, lasttid) values("+String.valueOf(CommonTid.uid)+",'"+ProblemInfo.tag+"',"+String.valueOf(ProblemInfo.tid)+")");
                else if(ProblemInfo.type == 2)
                    st.execute("insert into userques(uid, tag, lasttid2) values("+String.valueOf(CommonTid.uid)+",'"+ProblemInfo.tag+"',"+String.valueOf(ProblemInfo.tid)+")");
                else if (ProblemInfo.type == 3)
                    st.execute("insert into userques(uid, tag, lasttid3) values("+String.valueOf(CommonTid.uid)+",'"+ProblemInfo.tag+"',"+String.valueOf(ProblemInfo.tid)+")");
                else {}
            }
            Log.i("SQL", "after");
            conn.close();
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("GetTags"," sqlexception");
        }
        finish = true;
    }
    public boolean isFinish() { return finish; }
}
