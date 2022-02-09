package com.nic.sqlite.Adapetr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.sqlite.Activity.MyScreenSamples;
import com.nic.sqlite.R;

import java.util.ArrayList;

public class ScreenImageListAdapter extends RecyclerView.Adapter<ScreenImageListAdapter.ImageList> {
    ArrayList<Integer> screenImageList;
    Context context;

    public ScreenImageListAdapter(ArrayList<Integer> screenImageList, Context context) {
        this.screenImageList = screenImageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScreenImageListAdapter.ImageList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_recyler_items, viewGroup, false);

        return new ImageList(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ScreenImageListAdapter.ImageList imageList, int i) {
        imageList.imageView.setImageResource(screenImageList.get(i));

        imageList.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyScreenSamples)context).gotoFullImageView(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return screenImageList.size();
    }
    public class ImageList extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageList(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.screen_image);
        }
    }
}

