package com.nic.sqlite.Adapetr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.sqlite.Activity.ContactList;
import com.nic.sqlite.Activity.HomePage;
import com.nic.sqlite.ImageZoom.ImageMatrixTouchHandler;
import com.nic.sqlite.NoteModel;
import com.nic.sqlite.R;
import com.nic.sqlite.SqliteHelepr;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter  extends RecyclerView.Adapter<ContactListAdapter.ContactList> {
    ArrayList<NoteModel> contactList;
    Activity context;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;


    public ContactListAdapter(ArrayList<NoteModel> contactList, Activity context) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_list_adapter, viewGroup, false);

        return new ContactList(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactList holder, int position) {

        holder.name.setText(contactList.get(position).getUser_name());
        holder.number.setText(contactList.get(position).getUser_mobile_no());
        if(contactList.get(position).getUser_profile()!=null&&contactList.get(position).getUser_profile().length>0){
            holder.profile.setImageBitmap(getImage(contactList.get(position).getUser_profile()));
        }
        else {
            holder.profile.setImageResource(R.drawable.contact_profile);
        }

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForSmsPermission(position);

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((com.nic.sqlite.Activity.ContactList)context).deleteParticularContact(position);
            }
        });

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandedImage(holder.profile.getDrawable());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactList extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView name,number;
        ImageView edit,call;
        public ContactList(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            edit = itemView.findViewById(R.id.edit_icon);
            call = itemView.findViewById(R.id.call_icon);
        }
    }
    public  Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private void checkForSmsPermission(int position) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            //Log.d("TAG", getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else {
            // Permission already granted. Enable the SMS button.
            //enableSmsButton();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+contactList.get(position).getUser_mobile_no()));
            context.startActivity(callIntent);
        }
    }
    private void ExpandedImage(Drawable profile) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View ImagePopupLayout = inflater.inflate(R.layout.image_custom_layout, null);

            ImageView zoomImage = (ImageView) ImagePopupLayout.findViewById(R.id.imgZoomProfileImage);
            zoomImage.setImageDrawable(profile);

            ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(context);
            zoomImage.setOnTouchListener(imageMatrixTouchHandler);
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setView(ImagePopupLayout);

            final AlertDialog alert = dialogBuilder.create();
            alert.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_zoomInOut;
            alert.show();
            alert.getWindow().setBackgroundDrawableResource(R.color.full_transparent);
            alert.setCanceledOnTouchOutside(true);

            zoomImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

