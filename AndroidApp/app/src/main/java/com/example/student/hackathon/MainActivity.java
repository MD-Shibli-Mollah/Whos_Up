package com.example.student.hackathon;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static volatile UsersOnline userOnlineList;
    public static NetworkThread networkThread = new NetworkThread();
    public static android.content.Context context;
    public static volatile UserAccount userAccount;
    private static FragmentManager fragmentManager;
    private static LoginFragment loginFragment;
    private static UpdateInfoFragment updateInfoFragment;
    private static UsersOnlineFragment usersOnlineFragment;
    private static VideoFragment videoFragment;
    private static TextFragment textFragment;

    public static void createToast(final String text)
    {

        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Toast.makeText(MainActivity.context.getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        };
        mHandler.sendMessage(new Message());

    }

    public static void killLoginPage()
    {
        if (userAccount != null)
        {
            fragmentManager.beginTransaction().remove(loginFragment).commit();
        }
        else
        {
            MainActivity.createToast("Something went wrong, check your username and password");
            Log.w("SERVER", "Setting up account failure, userAccount == null");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Thread a = new Thread(networkThread);
        a.start();
        context = getApplicationContext();


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transition = fragmentManager.beginTransaction();
        loginFragment = new LoginFragment();
        transition.add(R.id.content_main, loginFragment);
        transition.commit();

        /*ArrayList<ClassInfo> temp = new ArrayList<>();
        temp.add(new ClassInfo("201720", "Scripting Languages", true, true, true));
        temp.add(new ClassInfo("201721", "C Programming", false, false, false));
        userAccount = new UserAccount(1, "Bruno", temp);*/

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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


        if (id == R.id.video) {
            FragmentTransaction transition = fragmentManager.beginTransaction();
            videoFragment = new VideoFragment();
            transition.replace(R.id.content_main, videoFragment);
            transition.commit();
        } else if (id == R.id.text) {

            FragmentTransaction transition = fragmentManager.beginTransaction();
            textFragment = new TextFragment();
            transition.replace(R.id.content_main, textFragment);
            transition.commit();

        } else if (id == R.id.usersOnline) {
            FragmentTransaction transition = fragmentManager.beginTransaction();
            usersOnlineFragment = new UsersOnlineFragment();
            transition.replace(R.id.content_main, usersOnlineFragment);
            transition.commit();

        } else if (id == R.id.updateInfo)
        {
            FragmentTransaction transition = fragmentManager.beginTransaction();
            updateInfoFragment = new UpdateInfoFragment();
            transition.replace(R.id.content_main, updateInfoFragment);
            transition.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
