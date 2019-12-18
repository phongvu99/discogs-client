package com.naughtybitch.discogsclient.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.naughtybitch.discogsclient.GetStartedActivity;
import com.naughtybitch.discogsclient.R;

public class WelcomeActivity extends AppIntro {

    SharedPreferences sp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide_1));
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide_2));
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide_3));
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide_4));
        setFadeAnimation();

        /* In your onCreate method */
        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        if (sp.getBoolean("first", false)) {
            Intent intent = new Intent(this, GetStartedActivity.class); // Call the AppIntro java class
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("first", true);
        editor.apply();
        Intent intent = new Intent(this, GetStartedActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("first", true);
        editor.apply();
        Intent intent = new Intent(this, GetStartedActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
