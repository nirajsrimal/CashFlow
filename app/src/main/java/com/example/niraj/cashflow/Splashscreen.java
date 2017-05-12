package com.example.niraj.cashflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Splashscreen extends Activity implements Animation.AnimationListener{

    Animation anim_fadein,bounce1,rotate1;
    ImageView img;
    String u="";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        anim_fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        anim_fadein.setAnimationListener(this);
        bounce1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        bounce1.setAnimationListener(this);
        rotate1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        rotate1.setAnimationListener(this);
        img = (ImageView)findViewById(R.id.logo);
        img.setVisibility(View.VISIBLE);
        img.startAnimation(anim_fadein);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation
        // check for fade in animation
        if(animation==anim_fadein)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView)findViewById(R.id.txt1);
                    textView.setVisibility(View.VISIBLE);
                    textView.startAnimation(bounce1);
                    img.startAnimation(rotate1);

                }
            },200);
        if (animation == bounce1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(sharedPreferences.contains("username"))
                    {
                         u = sharedPreferences.getString("username","");
                    }
                    if(u.equals("")) {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 200);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Animation is repeating
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Animation started
    }
}
