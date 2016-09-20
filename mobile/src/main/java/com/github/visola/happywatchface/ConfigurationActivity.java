package com.github.visola.happywatchface;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigurationActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SeekBar.OnSeekBarChangeListener, TextWatcher {

    private EditText mTimeFormat;
    private EditText mDateFormat;
    private TextView mSampleTime;
    private TextView mSampleDate;

    private TextView mBackgroundColor;
    private SeekBar mPickRed;
    private SeekBar mPickGreen;
    private SeekBar mPickBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        mDateFormat = (EditText) findViewById(R.id.text_date_format);
        mDateFormat.addTextChangedListener(this);
        mTimeFormat = (EditText) findViewById(R.id.text_time_format);
        mTimeFormat.addTextChangedListener(this);

        mSampleDate = (TextView) findViewById(R.id.text_sample_date);
        mSampleTime = (TextView) findViewById(R.id.text_sample_time);
        updateSamples();

        mBackgroundColor = (TextView) findViewById(R.id.pick_background_color);

        mPickRed = (SeekBar) findViewById(R.id.pick_red);
        mPickGreen = (SeekBar) findViewById(R.id.pick_green);
        mPickBlue = (SeekBar) findViewById(R.id.pick_blue);

        mPickRed.setOnSeekBarChangeListener(this);
        mPickGreen.setOnSeekBarChangeListener(this);
        mPickBlue.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        // We don't care about when the user is changing the value
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // We don't care about when the user starts changing value
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int red = mPickRed.getProgress();
        int green = mPickGreen.getProgress();
        int blue = mPickBlue.getProgress();

        int color = Color.argb(255, red, green, blue);
        mBackgroundColor.setBackgroundColor(color);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not interested on this
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not interested on this
    }

    @Override
    public void afterTextChanged(Editable editable) {
        updateSamples();
    }

    private void updateSamples() {
        Date now = new Date();

        SimpleDateFormat timeFormat = new SimpleDateFormat(mTimeFormat.getText().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat.getText().toString());

        mSampleTime.setText("Sample time: " + timeFormat.format(now));
        mSampleDate.setText("Sample date: " + dateFormat.format(now));
    }

}
