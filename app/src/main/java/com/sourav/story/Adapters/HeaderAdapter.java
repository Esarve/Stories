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
    private Integer count;

    public HeaderAdapter(String headerName, Integer number, Context mContext) {
        this.headerName = headerName;
        count = number;
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
        String headerText = "You currently have " + count + " entries";
        String displayName = "Hi "+ headerName;
        viewHolder.name.setText(displayName);
        viewHolder.body.setText(headerText);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
