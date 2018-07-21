package com.ft08.trailblazelearn.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.adapters.SwipeTabAdapter;


/*
    This class is used to navigate between 3 fragments in the activity
 */
public class SwipeTabsActivity  extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static String calledTrailId;
    private static String calledTrailKey;
    private static String calledStationId;
    private static String calledStationName;
    private static String calledStationInstructions;
    private static String calledStationLocation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_tabs);

        //--------
        viewPager = (ViewPager) findViewById(R.id.swipePager);
        viewPager.setAdapter(new SwipeTabAdapter(getSupportFragmentManager()));
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_swipe);
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Discussions"));
        tabLayout.addTab(tabLayout.newTab().setText("Contributions"));
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //--------
        Bundle savedDataFromPreviousActivity = getIntent().getExtras();
        setCalledStationId((String) savedDataFromPreviousActivity.get("stationId"));
        setCalledTrailId((String) savedDataFromPreviousActivity.get("trailId"));
        setCalledTrailKey((String) savedDataFromPreviousActivity.get("trailKey"));
        setCalledStationName((String) savedDataFromPreviousActivity.get("stationName"));
        setCalledStationInstructions((String) savedDataFromPreviousActivity.get("stationInstructions"));
        setCalledStationLocation((String) savedDataFromPreviousActivity.get("stationLocation"));

    }

    public static String getCalledTrailId() {
        return calledTrailId;
    }

    public static void setCalledTrailId(String calledTrailId) {
        SwipeTabsActivity.calledTrailId = calledTrailId;
    }

    public static String getCalledTrailKey() {
        return calledTrailKey;
    }

    public static void setCalledTrailKey(String calledTrailKey) {
        SwipeTabsActivity.calledTrailKey = calledTrailKey;
    }


    public static String getCalledStationId() {
        return calledStationId;
    }

    public static void setCalledStationId(String calledStationId) {
        SwipeTabsActivity.calledStationId = calledStationId;
    }

    public static String getCalledStationName() {
        return calledStationName;
    }

    public static void setCalledStationName(String calledStationName) {
        SwipeTabsActivity.calledStationName = calledStationName;
    }

    public static String getCalledStationInstructions() {
        return calledStationInstructions;
    }

    public static void setCalledStationInstructions(String calledStationInstructions) {
        SwipeTabsActivity.calledStationInstructions = calledStationInstructions;
    }

    public static String getCalledStationLocation() {
        return calledStationLocation;
    }

    public static void setCalledStationLocation(String calledStationLocation) {
        SwipeTabsActivity.calledStationLocation = calledStationLocation;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(TabLayout.Tab tab) {
    }

    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.homebtn:
                Intent intent = new Intent(SwipeTabsActivity.this, SelectModeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            // action with ID action_settings was selected

            default:
                break;
        }

        return true;

    }
}
