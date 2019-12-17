package com.naughtybitch.discogsclient.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.naughtybitch.discogsclient.GetStartedActivity;
import com.naughtybitch.discogsclient.R;

public class WelcomeActivity extends AppIntro {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide_1));
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide_2));
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide_3));
        addSlide(SampleSlide.newInstance(R.layout.welcome_slide_4));
        setFadeAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
//        Intent intent = new Intent(this, GetStartedActivity.class);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
