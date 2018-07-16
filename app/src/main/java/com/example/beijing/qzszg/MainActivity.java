package com.example.beijing.qzszg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private final String TAG="MainActivity ";
    private int userID=-1;
    private int donum=0;
    private String userNAME = "未登录";
    private TextView textView2;
    private TextView textView3;
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    userID = data.getIntExtra("UID",-1);
                    userNAME=data.getStringExtra("USERNAME");
                    donum = data.getIntExtra("DO",0);
                    Log.d(TAG,String.valueOf(userID));
                    Log.d(TAG,userNAME);
                    if(userID >0 ){
                        textView2.setText(userNAME , TextView.BufferType.SPANNABLE);
                        textView3.setText("您已答"+String.valueOf(donum)+"道题，\n开始答题吧！");
                    }
                }
                break;
            default:
                break;
        }
    }



    public int getID(){
        return userID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        Button exer_button = (Button)findViewById(R.id.exercise_button);
        Button sear_button=(Button)findViewById(R.id.search_button);
        sear_button.setOnClickListener(this);
        exer_button.setOnClickListener(this);

        textView2 = (TextView)headerView.findViewById(R.id.user_name);
        textView3 = (TextView)findViewById(R.id.login_info);

        CommonTid.uid = -1;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exercise_button:
                Intent intent1 = new Intent(MainActivity.this, SelectTag.class);
                intent1.putExtra("Uid", userID);
                startActivity(intent1);
                break;
            case R.id.search_button:
                Intent intent2 = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CommonTid.uid != -1)
            textView3.setText("您已答"+String.valueOf(CommonTid.havedone.size())+"道题，\n开始答题吧！");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case (R.id.nav_login):{
                if(userID>0) Toast.makeText(MainActivity.this,"您已登录",Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent,1);
                }
                break;
            }
            case (R.id.nav_logout):{
                if(userID<0){
                    Toast.makeText(MainActivity.this, "您未登录", Toast.LENGTH_SHORT).show();
                    break;
                }
                userID = -1;
                textView2.setText("未登录", TextView.BufferType.SPANNABLE);
                textView3.setText("您已答0道题，\n开始答题吧！");
                CommonTid.uid = -1;
                Toast.makeText(MainActivity.this,"您已退出登录",Toast.LENGTH_SHORT).show();
                break;
            }
            case (R.id.nav_like) :{
                Intent intent = new Intent(MainActivity.this, CollectActivity.class);
                intent.putExtra("UID",userID);
                startActivity(intent);
                break;
         //  Toast.makeText(MainActivity.this,"you click like",Toast.LENGTH_SHORT).show();
             }
            case (R.id.nav_wrong) : {
                Intent intent = new Intent(MainActivity.this, WrongActivity.class);
                intent.putExtra("UID", userID);
                startActivity(intent);
                break;
            }
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
