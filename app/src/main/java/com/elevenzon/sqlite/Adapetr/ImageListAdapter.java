package com.elevenzon.sqlite.Adapetr;

import android.content.Context;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elevenzon.sqlite.R;

import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageList> {
    ArrayList<String> imageList;
    Context context;

    public ImageListAdapter(ArrayList<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageListAdapter.ImageList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notes_list, viewGroup, false);

        return new ImageList(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageListAdapter.ImageList imageList, int i) {

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
    public class ImageList extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageList(@NonNull View itemView) {
            super(itemView);
        }
    }
}
