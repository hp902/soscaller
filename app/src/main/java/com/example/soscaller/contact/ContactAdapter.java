package com.example.soscaller.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soscaller.R;
import com.example.soscaller.devicecontact.SelectUser;
import com.example.soscaller.devicecontact.SelectUserAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    // List to store all the contact details
    private ArrayList<SelectUser> contactDataList;
    Context context;

    interface OnItemCheckListener{
        void onItemCheck(SelectUser selectUser);
    }

    @NonNull
    private final OnItemCheckListener onItemClick;

    // Constructor for the Class
    public ContactAdapter(ArrayList<SelectUser> contactsList, Context context, @NonNull OnItemCheckListener onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
        this.contactDataList = contactsList;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return contactDataList == null ? 0 : contactDataList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, final int position) {
        final SelectUser contactData = contactDataList.get(position);

        // Set the data to the views here
        holder.setContactName(contactData.getName());
        holder.setContactNumber(contactData.getPhone());

        holder.delete.setOnClickListener(v -> {
            onItemClick.onItemCheck(contactData);
            notifyItemRangeChanged(position, getItemCount());
        });

    }

    // This is your ViewHolder class that helps to populate data to the view
    public static class ContactHolder extends RecyclerView.ViewHolder {
        //Initialize Variables

        private final TextView txtName;
        private final TextView txtNumber;
        private final ImageView delete;

        public ContactHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_name);
            txtNumber = itemView.findViewById(R.id.txt_number);
            delete = itemView.findViewById(R.id.delete_button);
        }

        public void setContactName(String name) {
            txtName.setText(name);
        }

        public void setContactNumber(String number) {
            txtNumber.setText(number);
        }

    }
}