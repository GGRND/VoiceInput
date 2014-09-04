package com.eaaa.voiceinput;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.view.WindowUtils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class Activity_Main extends Activity {

    private TextView txtDemo, txtLocation, txtTime;
    private boolean voiceEnabled = true;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // Request a voice menu on this activity
        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);

        // Ensure screen stays on.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Above requests has to be done before adding content
        
        // setContentView and initials view elements
        initView();

        // initial gestureDetector
        gestureDetector = createGestureDetector(this);
    }
    
    private void initView(){
        setContentView(R.layout.activity_main);
        txtDemo = (TextView)findViewById(R.id.textDemo);
        txtDemo.setText("Voice Demo: " + (voiceEnabled ? "on" : "off"));
        txtLocation = (TextView) findViewById(R.id.textLocation);
        txtLocation.setText("");

        txtTime = (TextView) findViewById(R.id.textTime);
        txtTime.setText("");    	
    }

    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                	
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    am.playSoundEffect(Sounds.TAP);
                    
                    //toggle voice input on/off
                    voiceEnabled = !voiceEnabled;
                    txtDemo.setText("Voice Demo: " + (voiceEnabled ? "on" : "off"));
                    getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS); //revalidates voice input
                    return true;
                } else {
                    return false;
                }
            }
        });
        return gestureDetector;
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            getMenuInflater().inflate(R.menu.main_voice, menu);
            return true;
        }
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            return voiceEnabled;
        }
        return super.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
        	// Change Text depending on what voice command was received
        	// Could also have been other actions like starting another activity.
            switch (item.getItemId()) {
                case R.id.menu_today:
                    txtTime.setText(getString(R.string.menu_today));
                    break;
                case R.id.menu_tomorrow:
                    txtTime.setText(getString(R.string.menu_tomorrow));
                    break;
                case R.id.menu_current:
                    txtLocation.setText(R.string.menu_location_current);
                    break;
                case R.id.menu_copenhagen:
                    txtLocation.setText(R.string.menu_location_copenhagen);
                    break;
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (gestureDetector != null) {
            return gestureDetector.onMotionEvent(event);
        }
        return false;
    }

}