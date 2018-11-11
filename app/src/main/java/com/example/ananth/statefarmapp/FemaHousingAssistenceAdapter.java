package com.example.ananth.statefarmapp;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ananth.statefarmapp.models.HousingAssistanceOwner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.ananth.statefarmapp.Util.formatter;

public class FemaHousingAssistenceAdapter extends RecyclerView.Adapter<FemaHousingAssistenceAdapter.MyViewHolder> {
    private List<HousingAssistanceOwner> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout layout;
        public TextView cityText, numInspectedText, averageDamageText, totalDamageText, totalPaidText, disasterNumberText;

        public MyViewHolder(ConstraintLayout v) {
            super(v);
            layout = v;
            cityText = v.findViewById(R.id.cityText);
            numInspectedText = v.findViewById(R.id.numInspectedText);
            averageDamageText = v.findViewById(R.id.averageDamageText);
            totalDamageText = v.findViewById(R.id.totalDamageText);
            totalPaidText = v.findViewById(R.id.totalPaidText);
            disasterNumberText = v.findViewById(R.id.disasterNumberText);
        }
    }

    public FemaHousingAssistenceAdapter() {
        mDataset = new ArrayList<>();
    }

    public void setmDataset(List<HousingAssistanceOwner> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public FemaHousingAssistenceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.damage_summary_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        HousingAssistanceOwner owner = mDataset.get(position);
        holder.cityText.setText(owner.getCity());
        holder.numInspectedText.setText(owner.getApprovedForFemaAssistance()+"/"+owner.getTotalInspected()+" FEMA applicants inspected approved for assistence");

        holder.averageDamageText.setText("$"+formatter.format(owner.getAverageFemaInspectedDamage())+" damage on average ");
        holder.totalDamageText.setText("$"+formatter.format(owner.getTotalDamage())+" total damage");
        holder.totalPaidText.setText("$"+formatter.format(owner.getTotalApprovedIhpAmount())+" in assistence paid out");
        holder.disasterNumberText.setText("#"+owner.getDisasterNumber());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}