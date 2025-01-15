package com.webserva.wings.android.rubik;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExplanationAdapter extends RecyclerView.Adapter<ExplanationAdapter.ExplanationViewHolder> {
    private final List<ExplanationItem> explanationItems;

    public ExplanationAdapter(List<ExplanationItem> explanationItems) {
        this.explanationItems = explanationItems;
    }

    @NonNull
    @Override
    public ExplanationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explanation_item, parent, false);
        return new ExplanationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExplanationViewHolder holder, int position) {
        ExplanationItem item = explanationItems.get(position);
        holder.letterTextView.setText(item.getLetter());
        holder.descriptionTextView.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return explanationItems.size();
    }

    static class ExplanationViewHolder extends RecyclerView.ViewHolder {
        final TextView letterTextView;
        final TextView descriptionTextView;

        ExplanationViewHolder(@NonNull View itemView) {
            super(itemView);
            letterTextView = itemView.findViewById(R.id.letterTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
