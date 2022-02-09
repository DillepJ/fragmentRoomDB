package com.nic.sqlite.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.nic.sqlite.Adapetr.MyAdapter;
import com.nic.sqlite.R;

public class TabLayoutActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_Cyan);
        setContentView(R.layout.activity_tab_layout);

        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.headings));
        tabLayout.addTab(tabLayout.newTab().setText("S1"));
        tabLayout.addTab(tabLayout.newTab().setText("S2"));
        tabLayout.addTab(tabLayout.newTab().setText("S3"));
        tabLayout.addTab(tabLayout.newTab().setText("S4"));
        tabLayout.addTab(tabLayout.newTab().setText("Signature"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));

        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    setTheme(R.style.AppTheme_Cyan);
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.headings));
                }
                else if(tab.getPosition()==1){
                    setTheme(R.style.AppTheme_Teal);
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.purple_1));
                }
                else if(tab.getPosition()==2){
                    setTheme(R.style.AppTheme_Teal);
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.purple_1));
                }
                else if(tab.getPosition()==3){
                    setTheme(R.style.AppTheme_Cyan);
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.headings));
                }
                else {
                    setTheme(R.style.AppTheme_Teal);
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.purple_1));
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
