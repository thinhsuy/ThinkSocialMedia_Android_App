package com.example.thinksocial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public Database.DataSource myDB;
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private NavigationView nvActionBars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myDB = Database.OpenDB(this, "ThinkSocial.db");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvActionBars = (NavigationView) findViewById(R.id.nvView);

        ArrayList<String>results = myDB.getAllValueReturn("select * from Post", 7);
        for (int i=0; i<results.size(); i++){
            Log.e("Post", results.get(i));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvActionBars);
        SetUpFirstFragment(new PostingFragment());
        setUpHeader();
    }

    //Dragging Nativigation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Set up Nativigation on Click
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;;
        if (menuItem.getItemId()==R.id.navNews){
            fragment = new NewfeedsFragment();
        }
        else if (menuItem.getItemId()==R.id.navPost){
            fragment = new PostingFragment();
        }
        else {
            fragment = new NewfeedsFragment();
        }

        replaceFragmentContent(fragment);

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private void replaceFragmentContent(Fragment fragment){
        if (fragment!=null){
            FragmentManager fragMan = getSupportFragmentManager();
            FragmentTransaction fragTran = fragMan.beginTransaction();
            fragTran.replace(R.id.flContent, fragment);
            fragTran.commit();
        }
    }

    //Set first fragment display
    private void SetUpFirstFragment(Fragment firstFragment){
        replaceFragmentContent(firstFragment);
    }

    private void setUpHeader(){
        View header = nvActionBars.getHeaderView(0);
        TextView tvHeaderText = header.findViewById(R.id.tvHeaderText);;
        TextView tvHeaderSubtext = header.findViewById(R.id.tvHeaderSubtext);
        int id = ((Global)this.getApplication()).getIdCurrentUser();
        String username = Query.getUserInfor(myDB, id, "Username");
        tvHeaderText.setText(username);
        String fullname = Query.getUserInfor(myDB, id, "Name");
        tvHeaderSubtext.setText(fullname);
    }
}