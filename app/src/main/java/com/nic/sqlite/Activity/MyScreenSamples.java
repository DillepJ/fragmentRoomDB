package com.nic.sqlite.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.nic.sqlite.Adapetr.ScreenImageListAdapter;
import com.nic.sqlite.Fragment.SlideshowDialogFragment;
import com.nic.sqlite.R;

import java.util.ArrayList;

public class MyScreenSamples extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Integer> screensList;

    ScreenImageListAdapter screenImageListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_screen_samples);

        recyclerView = findViewById(R.id.image_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        getScreenList();

    }

    private void getScreenList() {
        screensList = new ArrayList<>();
        screensList.add(R.drawable.capture_9);
        screensList.add(R.drawable.capture_8);
        screensList.add(R.drawable.capture_7);
        screensList.add(R.drawable.capture_6);
        screensList.add(R.drawable.capture_5);
        screensList.add(R.drawable.capture_4);
        screensList.add(R.drawable.capture_3);
        screensList.add(R.drawable.capture_2);
        screensList.add(R.drawable.capture_1);

        screenImageListAdapter = new ScreenImageListAdapter(screensList,this);
        recyclerView.setAdapter(screenImageListAdapter);
    }

    public void gotoFullImageView(int position){
        Bundle bundle = new Bundle();
        bundle.putSerializable("images", screensList);
        bundle.putInt("position", position);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(ft, "slideshow");
    }

}
