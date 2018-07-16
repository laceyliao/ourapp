package com.example.beijing.qzszg;

import android.util.Log;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
/**
 * Created by 卢亮 on 2018/6/9.
 */

public class JDBCUtil {
    private static String dbDriver="com.mysql.jdbc.Driver";
    private static String dbUrl="jdbc:mysql://你的数据库";
    //private static String dbUser = "";
    //private static String dbPwd = "";

    public static Connection getConn(String dbUser, String dbPwd){
        Log.i("Toast","进入getConn方法");
        Connection conn=null;
        try {
            Class.forName(dbDriver);
            conn= DriverManager.getConnection(dbUrl, dbUser, dbPwd);
            Log.i("Connect","连接后"+conn);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i("ZZZ",dbUser);
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            //if (dbUrl.length()!=0)
            Log.i("ZZZ","连接错误");
            e.printStackTrace();
        }
        return conn;
    }
}
