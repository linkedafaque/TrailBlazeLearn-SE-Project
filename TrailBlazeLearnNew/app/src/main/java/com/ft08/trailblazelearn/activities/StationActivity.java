package com.ft08.trailblazelearn.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ft08.trailblazelearn.R;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.models.Participant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ft08.trailblazelearn.adapters.PagerAdapter;
import com.ft08.trailblazelearn.fragments.StationFragment;
import com.google.firebase.database.ValueEventListener;

public class StationActivity extends AppCompatActivity {
    private ViewPager viewPager;

    /*
    *This where we take care of core business logic...
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);//This is related to view
        getBundledData();
        setPager();
    }

    /*
    *Receiving data from TrailAdapter
    */
    public void getBundledData() {
        Bundle bundle = getIntent().getExtras();
        final String trailID = bundle.getString("trailId");
        final String trailKey = bundle.getString("trailKey");
        //Passing TrailID and TrailKey to stationFragment
        StationFragment.newInstance(trailID, trailKey);
    }

    /*
    *Setting Tabs for StationFragment and LocationsFragment
    */
    public void setPager(){
        this.viewPager= (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(pagerAdapter);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Stations"));
        tabLayout.addTab(tabLayout.newTab().setText("Locations"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homebtn:
                Intent intent = new Intent(StationActivity.this,SelectModeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
         if(App.user instanceof Participant){
             Toast.makeText(StationActivity.this,"Press Home Button to go back", Toast.LENGTH_SHORT).show();
         }
         else { super.onBackPressed();}
    }
}
