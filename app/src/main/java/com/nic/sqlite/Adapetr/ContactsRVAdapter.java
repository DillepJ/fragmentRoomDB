package com.nic.sqlite.Adapetr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.amulyakhare.textdrawable.TextDrawable;
import com.nic.sqlite.Activity.ContactDetailActivity;
import com.nic.sqlite.Module.ContactsModal;
import com.nic.sqlite.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContactsRVAdapter extends RecyclerView.Adapter<ContactsRVAdapter.ViewHolder> implements Filterable {

    // creating variables for context and array list.
    private Context context;
    private ArrayList<ContactsModal> contactsModalArrayList;
    private ArrayList<ContactsModal> contactsFilterModalArrayList;
    List<Color> colors;

    // creating a constructor
    public ContactsRVAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayList,List<Color> colors) {
        this.context = context;
        this.contactsModalArrayList = contactsModalArrayList;
        this.contactsFilterModalArrayList = contactsModalArrayList;
        this.colors=colors;
    }

    @NonNull
    @Override
    public ContactsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ContactsRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.contacts_rv_item, parent, false));

    }

    // below method is use for filtering data in our array list
    public void filterList(ArrayList<ContactsModal> filterllist) {
        // on below line we are passing filtered
        // array list in our original array list
        contactsFilterModalArrayList = filterllist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsRVAdapter.ViewHolder holder, int position) {
        // getting data from array list in our modal.
        //ContactsModal modal = contactsFilterModalArrayList.get(position);
        // on below line we are setting data to our text view.
        holder.contactTV.setText(contactsFilterModalArrayList.get(position).getUserName());
// below text drawable is a circular.
        int color = randomColor();

        // below text drawable is a circular.
        TextDrawable drawable2 = TextDrawable.builder().beginConfig()
                .width(100)  // width in px
                .height(100) // height in px
                .endConfig()
                // as we are building a circular drawable
                // we are calling a build round method.
                // in that method we are passing our text and color.
                .buildRound(contactsFilterModalArrayList.get(position).getUserName().substring(0, 1), color);
        // setting image to our image view on below line.
        holder.contactIV.setImageDrawable(drawable2);
        // setting image to our image view on below line.
        //holder.contactIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_account));
        // on below line we are adding on click listener to our item of recycler view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are opening a new activity and passing data to it.
                Intent i = new Intent(context, ContactDetailActivity.class);
                i.putExtra("name", contactsFilterModalArrayList.get(position).getUserName());
                i.putExtra("contact", contactsFilterModalArrayList.get(position).getContactNumber());
                // on below line we are starting a new activity,
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsModalArrayList == null ? 0 : contactsFilterModalArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactsFilterModalArrayList = contactsModalArrayList;
                } else {
                    ArrayList<ContactsModal> filteredList = new ArrayList<>();
                    for (ContactsModal row : contactsModalArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getUserName().toLowerCase().contains(charString.toLowerCase()) || row.getUserName().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactsFilterModalArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactsFilterModalArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactsFilterModalArrayList = (ArrayList<ContactsModal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // on below line creating a variable
        // for our image view and text view.
        private ImageView contactIV;
        private ImageView call_icon;
        private TextView contactTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our image view and text view.
            contactIV = itemView.findViewById(R.id.idIVContact);
            contactTV = itemView.findViewById(R.id.idTVContactName);
            call_icon = itemView.findViewById(R.id.call_icon);
        }
    }

    public int randomColor(){
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }
}

