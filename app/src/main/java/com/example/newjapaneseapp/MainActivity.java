package com.example.newjapaneseapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.newjapaneseapp.GrammarPage.GrammarDetails;
import com.example.newjapaneseapp.GrammarPage.GrammarListAdapter;
import com.example.newjapaneseapp.KanjiPage.KanjiPageAdapter;
import com.example.newjapaneseapp.KanjiQuiz.KanjiQuizActivity;
import com.example.newjapaneseapp.Parser.KanjiJsonParser;
import com.example.newjapaneseapp.Parser.ParticleJsonParser;

import android.os.Handler;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener, MyActivity {

    private static ViewPager mPager;
    private static final Integer[] images = {};
    private ArrayList<Integer> image_array = new ArrayList<Integer>();
    private volatile int currentpage = 0;
    private volatile int NUM_PAGE = 0;
    private NavigationView navigationView;
    private Timer swipeTimer;
    private ActivityMemory activityMemory = ActivityMemory.getInstance();
    private ViewFlipper view_flipper_0;
    private  RecyclerView recyclerView;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        activityMemory.setCurrentActivity(this);

        title = findViewById(R.id.titleHeader);
        recyclerView = findViewById(R.id.grammarRecycler);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        System.out.println("The RecyclerView is not scrolling");
                        if(recyclerView.computeVerticalScrollOffset() < 1){
                            title.setVisibility(View.VISIBLE);
                        }else {
                            title.setVisibility(View.GONE);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        System.out.println("Scroll Settling");
                        if(recyclerView.computeVerticalScrollOffset() < 1){
                            title.setVisibility(View.VISIBLE);
                        }else {
                            title.setVisibility(View.GONE);
                        }
                        break;

                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    System.out.println("Scrolled Downwards");
                    title.setVisibility(View.GONE);
                } else if (dy < 0) {
                    System.out.println("Scrolled Upwards");
                } else {
                    System.out.println("No Vertical Scrolled");
                }
            }
        });

        KanjiJsonParser kanjiJsonParser = KanjiJsonParser.getInstance();
        kanjiJsonParser.generateList();
        kanjiJsonParser.getNQuestion(10);

        setUpView();
    }

    public void setUpView(){
        view_flipper_0 = findViewById(R.id.view_flipper_0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Menu mi = navigationView.getMenu();
        closeSubMenu(mi);
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
        Menu mi = navigationView.getMenu();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        CoordinatorLayout layout = findViewById(R.id.main_layout);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        if (id == R.id.nav_home) {
            view_flipper_0.setDisplayedChild(0);
        } else if (id == R.id.n5) {
            if(!mi.findItem(R.id.grammarNav).isVisible()) {
                mi.findItem(R.id.grammarNav).setVisible(true);
                mi.findItem(R.id.kanjiNav).setVisible(true);
            }else{
                mi.findItem(R.id.grammarNav).setVisible(false);
                mi.findItem(R.id.kanjiNav).setVisible(false);
            }
        } else if (id == R.id.n4) {
            if(!mi.findItem(R.id.grammarN4av).isVisible()) {
                mi.findItem(R.id.grammarN4av).setVisible(true);
                mi.findItem(R.id.kanjiN4av).setVisible(true);
            }else {
                mi.findItem(R.id.grammarN4av).setVisible(false);
                mi.findItem(R.id.kanjiN4av).setVisible(false);
            }
        }else if (id == R.id.n3) {
            if(!mi.findItem(R.id.grammarN3av).isVisible()) {
                mi.findItem(R.id.grammarN3av).setVisible(true);
                mi.findItem(R.id.kanjiN3av).setVisible(true);
            }else {
                mi.findItem(R.id.grammarN3av).setVisible(false);
                mi.findItem(R.id.kanjiN3av).setVisible(false);
            }
        } else if (id == R.id.grammarN4av) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.grammarNav){
            view_flipper_0.setDisplayedChild(1);
            title.setText("Particles");
            title.setVisibility(View.VISIBLE);
            FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
            floatingActionButton.setVisibility(View.GONE);
            initializeRecyclerView();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.kanjiNav) {
            view_flipper_0.setDisplayedChild(1);
            title.setText("N5 Vocabulary");
            title.setVisibility(View.VISIBLE);
            KanjiJsonParser kanjiJsonParser = KanjiJsonParser.getInstance();
            KanjiPageAdapter kanjiPageAdapter = new KanjiPageAdapter(kanjiJsonParser.getKanji_and_Meaning());
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(kanjiPageAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
            floatingActionButton.setVisibility(View.VISIBLE);
            final Intent intent = new Intent(this, KanjiQuizActivity.class);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
            drawer.closeDrawer(GravityCompat.START);
        }


        return true;
    }

    public void closeSubMenu(Menu mi){
        mi.findItem(R.id.grammarNav).setVisible(false);
        mi.findItem(R.id.kanjiNav).setVisible(false);
        mi.findItem(R.id.grammarN4av).setVisible(false);
        mi.findItem(R.id.kanjiN4av).setVisible(false);
        mi.findItem(R.id.grammarN3av).setVisible(false);
        mi.findItem(R.id.kanjiN3av).setVisible(false);
    }

    private void initializeImage(){
        for(int i=0; i<images.length;i++){
            image_array.add(images[i]);
            mPager = findViewById(R.id.imagePager);
            mPager.setAdapter(new ImagePagerActivity(image_array, this));
        }
        NUM_PAGE = images.length;

        //Auto Start of Viewpager
        startImageTimeTask();
    }

    private void startImageTimeTask(){
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                try {
                    if (currentpage == NUM_PAGE) {
                        currentpage = 0;
                    }
                    mPager.setCurrentItem(currentpage++, true);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        };
        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 8000,8000);
    }

    private void stopImageTimeTask(){
        swipeTimer.cancel();
    }

    private void initializeRecyclerView(){
        ParticleJsonParser particleJsonParser = ParticleJsonParser.getInstance();
        ParticleMemory particleMemory = ParticleMemory.getInstance();
        ArrayList<GrammarDetails> grammarDetailsArrayList = new ArrayList<>();
        for (Map.Entry<String,Integer> entry : particleMemory.getJsonMap().entrySet()) {
            try {
                particleJsonParser.loadJSONFromAsset(entry.getValue());
                JSONObject jsonObject = particleJsonParser.parseJSON(entry.getKey());
                String des = particleJsonParser.getParticleDescription(jsonObject);
                GrammarDetails gr = new GrammarDetails(entry.getKey(), des);
                grammarDetailsArrayList.add(gr);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        GrammarListAdapter grammarListAdapter = new GrammarListAdapter(grammarDetailsArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(grammarListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }
}
