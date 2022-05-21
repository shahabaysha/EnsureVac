package com.ensure.vac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<EmergencyModel> arrayListEmergencyModel;
    private IItemClickListener mListener;

    public EmergencyAdapter(Context context, ArrayList<EmergencyModel> arrayListEmergencyModel,
                            IItemClickListener listener) {
        this.context = context;
        this.arrayListEmergencyModel = arrayListEmergencyModel;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public EmergencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_show_emergency, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyAdapter.ViewHolder holder, int position) {

        holder.etTeamName.setText(arrayListEmergencyModel.get(position).getTeamName());
        holder.etArea.setText(arrayListEmergencyModel.get(position).getArea());
        holder.etReason.setText(arrayListEmergencyModel.get(position).getIssue());
    }

    @Override
    public int getItemCount() {
        return arrayListEmergencyModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View binding;
        TextView etTeamName = itemView.findViewById(R.id.etTeamName);
        TextView etArea = itemView.findViewById(R.id.etArea);
        TextView etReason = itemView.findViewById(R.id.etReason);
        TextInputEditText etSolution = itemView.findViewById(R.id.etSolution);
        Button btnSubmit = itemView.findViewById(R.id.btnSubmit);

        public ViewHolder(View binding) {
            super(binding.getRootView());
            this.binding = binding;

            btnSubmit.setOnClickListener(view -> {
                mListener.onItemClick(
                        arrayListEmergencyModel.get(getLayoutPosition()),
                        getLayoutPosition(),
                        etSolution.getText().toString()
                );
            });
        }
    }

    interface IItemClickListener{
        void onItemClick(EmergencyModel emergencyModel, int position, String solution);
    }

    void updateUI(int position) {
        arrayListEmergencyModel.remove(position);
        notifyItemRemoved(position);
    }
}
