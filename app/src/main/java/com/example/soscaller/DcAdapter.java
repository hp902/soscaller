package com.example.soscaller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DcAdapter extends RecyclerView.Adapter<DcAdapter.ViewHolder>{

    private final ArrayList<ContactData> contactDataList;
    private Context mContext;

    public DcAdapter(ArrayList<ContactData> contactDataList, Context mContext) {
        this.contactDataList = contactDataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ContactData contactData = contactDataList.get(position);
        holder.setContactName(contactData.getName());
        holder.setContactNumber(contactData.getNumber());

    }

    @Override
    public int getItemCount() {
        return contactDataList == null? 0: contactDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView txtName;
        private final TextView txtNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_name);
            txtNumber = itemView.findViewById(R.id.txt_number);
        }

        public void setContactName(String name) {
            txtName.setText(name);
        }

        public void setContactNumber(String number) {
            txtNumber.setText(number);
        }


    }
}
