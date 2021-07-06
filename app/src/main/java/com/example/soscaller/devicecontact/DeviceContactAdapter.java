package com.example.soscaller.devicecontact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soscaller.ContactData;
import com.example.soscaller.R;

import java.util.ArrayList;
import java.util.List;

public class DeviceContactAdapter extends RecyclerView.Adapter<DeviceContactAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    public List<ContactData> cont;
    ContactData list;
    private ArrayList<ContactData> arraylist;

    public DeviceContactAdapter(LayoutInflater inflater, List<ContactData> items) {
        this.layoutInflater = inflater;
        this.cont = items;
        this.arraylist = new ArrayList<ContactData>();
        this.arraylist.addAll(cont);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        list = cont.get(position);

        holder.title.setText(list.getName());
        holder.phone.setText(list.getNumber());

        holder.imageView.setOnClickListener(v -> {

            if(list.isChecked()){
                list.setImageView(R.drawable.ic_baseline_check_circle_outline_24);

            }else{
                list.setImageView(R.drawable.ic_baseline_check_circle_24);
            }

            holder.imageView.setImageResource(list.getImageView());
            list.setChecked(!list.isChecked());
        });

    }

    @Override
    public int getItemCount() {
        return cont.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView phone;
        public ImageView imageView;
        public LinearLayout contact_select_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txt_name);
            phone = (TextView) itemView.findViewById(R.id.txt_number);
            imageView = itemView.findViewById(R.id.check_button);
            contact_select_layout = (LinearLayout) itemView.findViewById(R.id.contact_select_layout);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
