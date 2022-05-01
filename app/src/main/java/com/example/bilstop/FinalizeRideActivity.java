package com.example.bilstop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.bilstop.Classes.Location;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.Models.PolylineData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FinalizeRideActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MIN_NO_PASSENGERS = 1;
    private static final int MAX_NO_PASSENGERS = 4;

    private TextView mDisplayDate;
    private TextView mDisplayTime;
    private TextView passengerNoText;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Button createRide;
    private FloatingActionButton fabDecrease;
    private FloatingActionButton fabIncrease;
    boolean dateSelected = false;
    boolean timeSelected = false;
    int passengers = 3;

    private Calendar rideDate= Calendar.getInstance();
    private int numberOfPassengers=3;

    private FirebaseAuth auth  = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_ride);
        CardView dateCardView = (CardView) findViewById(R.id.date_card);
        CardView timeCardView = (CardView) findViewById(R.id.time_card);
        mDisplayDate = (TextView) findViewById(R.id.date_textview);
        mDisplayTime = (TextView) findViewById(R.id.time_textview);
        passengerNoText = (TextView) findViewById(R.id.textView3);
        createRide = (Button) findViewById(R.id.createButton);
        fabDecrease = (FloatingActionButton) findViewById(R.id.fabDec);
        fabIncrease = (FloatingActionButton) findViewById(R.id.fabInc);


        dateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        mDisplayDate.setText(date);
                        dateSelected = true;
                        if(timeSelected) createRide.setVisibility(View.VISIBLE);
                        mDisplayDate.setVisibility(View.VISIBLE);

                        rideDate.set(Calendar.DAY_OF_MONTH, day);
                        rideDate.set(Calendar.MONTH, month);
                        rideDate.set(Calendar.YEAR, year + 1990);
                    }
                };

                DatePickerDialog dialog = new DatePickerDialog(
                        FinalizeRideActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        timeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                int HOUR = calendar.get(Calendar.HOUR);
                int MINUTE = calendar.get(Calendar.MINUTE);

                mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String timeString = hour + ":" + minute;
                        mDisplayTime.setText(timeString);
                        timeSelected = true;
                        if(dateSelected) createRide.setVisibility(View.VISIBLE);

                        rideDate.set(Calendar.HOUR_OF_DAY, hour);
                        rideDate.set(Calendar.MINUTE, minute);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(FinalizeRideActivity.this, mTimeSetListener,
                        HOUR, MINUTE, true);
                timePickerDialog.show();
            }
        });


        fabDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passengers > MIN_NO_PASSENGERS) {
                    passengers--;
                    passengerNoText.setText("" + passengers);
                    numberOfPassengers=passengers;
                }
            }
        });

        fabIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passengers < MAX_NO_PASSENGERS) {
                    passengers++;
                    passengerNoText.setText("" + passengers);
                    numberOfPassengers=passengers;
                }
            }
        });

        createRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Ride ride = (Ride) getIntent().getSerializableExtra("ride");

                SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat simpleDateFormatHour = new SimpleDateFormat("HH:mm");

                String dateTime = simpleDateFormatDate.format(rideDate.getTime()).toString();
                String hourTime = simpleDateFormatHour.format(rideDate.getTime()).toString();

                Log.d("date",dateTime);
                Log.d("hour",hourTime);

                ride.setRideDate(dateTime);
                ride.setRideHour(hourTime);

                ride.setNumberOfPassenger(numberOfPassengers);

                ride.setDriverName(user.getDisplayName());

                Log.d("ride", ride.toString());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("ridesFromBilkent");


                myRef.push().setValue(ride);

                Intent intent = new Intent(FinalizeRideActivity.this, BottomNavActivity.class);
                startActivity(intent);

            }
        });
    }
}