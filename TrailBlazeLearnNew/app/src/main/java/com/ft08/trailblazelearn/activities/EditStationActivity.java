package com.ft08.trailblazelearn.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ft08.trailblazelearn.application.App;
import com.ft08.trailblazelearn.fragments.StationFragment;
import com.ft08.trailblazelearn.models.Station;
import com.ft08.trailblazelearn.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by keerthanadevi on 20/3/18.
 */

public class EditStationActivity extends AppCompatActivity {

    private String latLong,locationAddress,trailID,stationID,trailKey;
    private EditText gps, stationName, instructions;
    private FirebaseDatabase database;
    private Button addstationBtn;
    private DatabaseReference myRef;
    private DatabaseReference tkref;
    private DatabaseReference sref;
    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_station_dialogbox);

        getBundledData();
        initFirebaseDatabaseRef();
        initUI();
        onClickGps();
        onClickAddButton();
    }

    /*
    *Receiving data from StationAdapter and initializing it
    */
    public void getBundledData(){
        Bundle bundle = getIntent().getExtras();
        stationID = bundle.getString("stationId");
        trailID = bundle.getString("trailId");
        trailKey = bundle.getString("trailKey");
    }

    /*
    * Initializing All Firebase Instances
    * */
    public void initFirebaseDatabaseRef(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Trails");
        tkref= myRef.child(trailKey);
        sref = tkref.child("Stations");
    }

    /*
    * Initializing All Views In Station Details Dialog Box
    * */
    public void initUI(){
        station = (App.trainer.getTrail(trailID)).getStation(stationID);
        String seq=Integer.toString(station.getSeqNum());
        ((TextView) findViewById(R.id.seqNumtxt)).setText(seq);
        ((EditText) findViewById(R.id.stationNametxt)).setText(station.getStationName());
        ((EditText) findViewById(R.id.gpstxt)).setText(station.getAddress());
        ((EditText) findViewById(R.id.instructionsTxt)).setText(station.getInstructions());

        stationName = (EditText) findViewById(R.id.stationNametxt);
        gps = (EditText) findViewById(R.id.gpstxt);
        instructions = (EditText) findViewById(R.id.instructionsTxt);
        addstationBtn = (Button) findViewById(R.id.CreateBtn);
    }

    /*
    * This method opens place picker and allows to edit the selected location and save it
    * */
    public void onClickGps(){
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent;
                    intent = builder.build(EditStationActivity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
    * This Method Sets The Listener For Save Button Of The edited Station Details Dialog Box
    * */
    public void onClickAddButton(){
        addstationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()) {
                    String location = latLong;

                    final String stName = stationName.getText().toString().trim();
                    if(location==null){
                        location = station.getGps();
                    }
                    final String instinfo = instructions.getText().toString().trim();
                    final String address = gps.getText().toString().trim();

                    final Station edstation = (App.trainer.getTrail(trailID)).editStation(stName, instinfo, location,station.getStationID(),address);

                    DatabaseReference edRef = sref.child(station.getStationID());
                    edRef.child("stationName").setValue(edstation.getStationName());
                    edRef.child("instructions").setValue(edstation.getInstructions());
                    edRef.child("gps").setValue(edstation.getGps());
                    edRef.child("address").setValue(edstation.getAddress());

                    Toast.makeText(EditStationActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /*
    * Setting all required location attributes...
    * */
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1){
            if(resultCode==RESULT_OK){

                Place place = PlacePicker.getPlace(EditStationActivity.this,data);
                latLong = place.getLatLng().toString();
                locationAddress = place.getAddress().toString();
                gps.setText(locationAddress);
            }
        }
    }

    /*
     * Validates If All Station Details Have Been Entered Correctly
     * */
    private boolean isValid () {
        boolean isValid = true;
        if (TextUtils.isEmpty(stationName.getText().toString().trim())) {
            stationName.setError("Please fill in station name");
            isValid = false;
        }
        if (TextUtils.isEmpty(gps.getText().toString().trim())) {
            gps.setError("Please specify the location");
            isValid = false;
        }

        if (TextUtils.isEmpty(instructions.getText().toString().trim())) {
            instructions.setError("Please provide instruction");
            isValid = false;
        }
        return isValid;
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
                Intent intent = new Intent(EditStationActivity.this,SelectModeActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        return true;
    }
}
