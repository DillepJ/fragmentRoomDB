package com.elevenzon.sqlite;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elevenzon.sqlite.Activity.AddlayoutInflater;
import com.elevenzon.sqlite.Activity.ChessTimer;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.viewHolder> {

    Context context;
    Activity activity;
    ArrayList<NoteModel> arrayList;
    DatabaseHelper database_helper1;
    SqliteHelepr database_helper;

    public NotesAdapter(Context context,Activity activity, ArrayList<NoteModel> arrayList) {
        this.context = context;
        this.activity  = activity ;
        this.arrayList = arrayList;
    }

    @Override
    public NotesAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.viewHolder holder, final int position) {

        holder.latlang.setText(""+arrayList.get(position).getLatitude()+arrayList.get(position).getLangtitude());
        holder.title.setText(arrayList.get(position).getTitle());
        holder.description.setText(arrayList.get(position).getDes());
        holder.circleImageViewl.setImageBitmap(MainActivity.getImage(arrayList.get(position).getProfile()));
        database_helper = new SqliteHelepr(context);

        holder.delete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //deleting note
                database_helper.delete(arrayList.get(position).getID());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //display edit dialog
                showDialog(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChessTimer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                //((MainActivity)activity).paymentACtivity();


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, description,latlang;
        ImageView delete, edit;
        CircleImageView circleImageViewl;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            circleImageViewl=itemView.findViewById(R.id.profile);
            latlang=itemView.findViewById(R.id.latlang);
        }
    }

    public void showDialog(final int pos) {
        final EditText title, des;
        Button submit;
        CircleImageView preview_image,circleImageView;
        ImageView close;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title = (EditText) dialog.findViewById(R.id.title);
        des = (EditText) dialog.findViewById(R.id.description);
        submit = (Button) dialog.findViewById(R.id.submit);
        preview_image=dialog.findViewById(R.id.profile_image_preview);
        circleImageView=dialog.findViewById(R.id.profile_image);
        close=dialog.findViewById(R.id.close);

        title.setText(arrayList.get(pos).getTitle());
        des.setText(arrayList.get(pos).getDes());
        preview_image.setVisibility(View.GONE);
        circleImageView.setImageBitmap(MainActivity.getImage(arrayList.get(pos).getProfile()));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("Please Enter Title");
                }else if(des.getText().toString().isEmpty()) {
                    des.setError("Please Enter Description");
                }else {
                    //updating note
                    database_helper.updateNote(title.getText().toString(), des.getText().toString(), arrayList.get(pos).getID(),arrayList.get(pos).getLatitude(),arrayList.get(pos).getLangtitude(),
                            arrayList.get(pos).getDate(),arrayList.get(pos).getProfile());
                    arrayList.get(pos).setTitle(title.getText().toString());
                    arrayList.get(pos).setDes(des.getText().toString());
                    dialog.cancel();
                    //notify list
                    notifyDataSetChanged();
                }
            }
        });
    }



}