package com.sourav.story.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourav.story.R;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    private String headerName;
    private String headerText;

    public HeaderAdapter(String headerName, String headerText, Context mContext) {
        this.headerName = headerName;
        this.headerText = headerText;
    }

    @NonNull
    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_header, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        if (headerName != null && headerText != null){
            viewHolder.name.setText(headerName);
            viewHolder.body.setText(headerText);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView body;
        LinearLayout parent;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.headerName);
            body = itemView.findViewById(R.id.headerText);
            parent = itemView.findViewById(R.id.headerParent);
        }
    }
}
