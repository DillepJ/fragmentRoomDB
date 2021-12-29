package com.nic.sqlite.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nic.sqlite.R;

import meow.bottomnavigation.MeowBottomNavigation;

public class BottomNavigationActivty extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_activty);

        bottomNavigation =findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_account));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_message));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_explore));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_notification));

    }
}
