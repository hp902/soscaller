package com.example.soscaller.devicecontact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soscaller.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SelectUserAdapter extends RecyclerView.Adapter<SelectUserAdapter.MyContactListViewHolder> {

    List<SelectUser> mainInfo;
    private ArrayList<SelectUser> arraylist;
    Context context;


    interface OnItemCheckListener {
        void onItemCheck(SelectUser selectUser);

        void onItemUncheck(SelectUser selectUser);
    }

    @NonNull
    private OnItemCheckListener onItemClick;



    public SelectUserAdapter(Context context, List<SelectUser> mainInfo,@NonNull OnItemCheckListener onItemClick) {
        this.onItemClick = onItemClick;
        this.mainInfo = mainInfo;
        this.context = context;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(mainInfo);
    }

    public class MyContactListViewHolder extends RecyclerView.ViewHolder {
        TextView textViewShowName;
        TextView textViewPhoneNumber;
        CheckBox checkBoxSelectItem;

        public MyContactListViewHolder(View itemView) {
            super(itemView);

            textViewShowName = itemView.findViewById(R.id.name);
            checkBoxSelectItem = itemView.findViewById(R.id.check);
            textViewPhoneNumber = itemView.findViewById(R.id.no);

            checkBoxSelectItem.setOnClickListener(view -> {
                final SelectUser selectUser = mainInfo.get(getAdapterPosition());
                CheckBox checkBox = (CheckBox) view;

                if(checkBox.isChecked() && !selectUser.getCheckedBox()){
                    onItemClick.onItemCheck(selectUser);
                    selectUser.setCheckedBox(true);
                }else{
                    onItemClick.onItemUncheck(selectUser);
                    selectUser.setCheckedBox(false);
                }

                notifyDataSetChanged();
            });
        }
    }

    @NonNull
    @Override
    public MyContactListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview, parent, false);
        return new MyContactListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyContactListViewHolder holder, int position) {

        holder.textViewShowName.setText(mainInfo.get(position).getName());
        holder.textViewPhoneNumber.setText(mainInfo.get(position).getPhone());
        holder.checkBoxSelectItem.setChecked(mainInfo.get(position).getCheckedBox());

    }

    @Override
    public int getItemCount() {
        return mainInfo.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mainInfo.clear();
        if (charText.length() == 0) {
            mainInfo.addAll(arraylist);
        } else {
            for (SelectUser wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mainInfo.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }
}