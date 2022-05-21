package com.ensure.vac;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddWorkerAdapter extends RecyclerView.Adapter<AddWorkerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AddWorkerModel> addWorkerModelArrayList;
    private IItemClickListener mListener;

    public AddWorkerAdapter(Context context, ArrayList<AddWorkerModel> addWorkerModelArrayList,
                       IItemClickListener listener) {
        this.context = context;
        this.addWorkerModelArrayList = addWorkerModelArrayList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public AddWorkerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_worker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddWorkerAdapter.ViewHolder holder, int position) {

        holder.userName.setText(addWorkerModelArrayList.get(position).getFullName());

        /*holder.binding.setClickListener((IClickListener) v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putInt("id", v.getId());
            bundle.putString("userName", addWorkerModelArrayList.get(position).getUserName());
            mListener.onItemSelect(bundle);
        });*/


        if (!addWorkerModelArrayList.get(position).isSelected) {
            holder.ivSelect.setVisibility(View.GONE);
        } else {
            holder.ivSelect.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return addWorkerModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View binding;
        TextView userName = itemView.findViewById(R.id.txt_userName);
        ImageView ivSelect = itemView.findViewById(R.id.ivSelect);

        public ViewHolder(View binding) {
            super(binding.getRootView());
            this.binding = binding;

            itemView.setOnClickListener(view -> {
                tickVisibility();
                mListener.onItemClick(
                        addWorkerModelArrayList.get(getLayoutPosition()),
                        getLayoutPosition()
                );
                notifyDataSetChanged();
            });
        }

        private void tickVisibility() {
            addWorkerModelArrayList.get(getLayoutPosition()).isSelected =
                    !addWorkerModelArrayList.get(getLayoutPosition()).isSelected;
            if (addWorkerModelArrayList.get(getLayoutPosition()).isSelected) {
                ivSelect.setVisibility(View.VISIBLE);
            } else {
                ivSelect.setVisibility(View.GONE);
            }
        }
    }

    interface IItemClickListener{
        void onItemClick(AddWorkerModel addWorkerModelArrayList, int position);
    }
}

