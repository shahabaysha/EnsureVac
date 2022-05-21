package com.ensure.vac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JobDetailAdapter extends RecyclerView.Adapter<JobDetailAdapter.ViewHolder> {

    private Context context;
    private ArrayList<JobDetailModel> arrayListJobDetailModel;

    public JobDetailAdapter(Context context, ArrayList<JobDetailModel> arrayListJobDetailModel) {
        this.context = context;
        this.arrayListJobDetailModel = arrayListJobDetailModel;
    }

    @NonNull
    @Override
    public JobDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobDetailAdapter.ViewHolder holder, int position) {

        holder.tvTeamName.setText(arrayListJobDetailModel.get(position).getTeamName());
        holder.tvJobDescription.setText(arrayListJobDetailModel.get(position).getJobDescription());
        holder.tvDate.setText(arrayListJobDetailModel.get(position).getDate());
        holder.tvWorkers.setText(arrayListJobDetailModel.get(position).getWorkers());
        holder.tvArea.setText(arrayListJobDetailModel.get(position).getArea());
    }

    @Override
    public int getItemCount() {
        return arrayListJobDetailModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View binding;
        TextView tvTeamName = itemView.findViewById(R.id.tvTeamName);
        TextView tvJobDescription = itemView.findViewById(R.id.tvJobDescription);
        TextView tvDate = itemView.findViewById(R.id.tvDate);
        TextView tvWorkers = itemView.findViewById(R.id.tvWorkers);
        TextView tvArea = itemView.findViewById(R.id.tvArea);

        public ViewHolder(View binding) {
            super(binding.getRootView());
            this.binding = binding;

        }
    }
}
