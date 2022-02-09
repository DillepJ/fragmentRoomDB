package com.nic.sqlite.Adapetr;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nic.sqlite.Fragment.Screen1;
import com.nic.sqlite.Fragment.Screen2;
import com.nic.sqlite.Fragment.Screen3;
import com.nic.sqlite.Fragment.Screen5;
import com.nic.sqlite.Fragment.SignaturePadFrag;
import com.nic.sqlite.R;


public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Screen3 homeFragment = new Screen3();
                return homeFragment;
            case 1:
                Screen2 sportFragment = new Screen2(myContext);
                return sportFragment;
            case 2:
                Screen1 movieFragment = new Screen1();
                return movieFragment;
            case 3:
                Screen5 movieFragment1 = new Screen5();
                return movieFragment1;
            case 4:
                SignaturePadFrag movieFragment2 = new SignaturePadFrag();
                return movieFragment2;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}