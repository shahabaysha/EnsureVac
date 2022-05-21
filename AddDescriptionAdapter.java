package com.ensure.vac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddDescriptionAdapter extends RecyclerView.Adapter<AddDescriptionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> arrayList;

    public AddDescriptionAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AddDescriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_description, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddDescriptionAdapter.ViewHolder holder, int position) {

        holder.tvJobDes.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View binding;
        TextView tvJobDes = itemView.findViewById(R.id.tvJobDes);

        public ViewHolder(View binding) {
            super(binding.getRootView());
            this.binding = binding;

        }
    }
}


