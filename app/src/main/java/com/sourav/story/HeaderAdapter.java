package com.sourav.story;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {
    private String headerName;
    private String headerText;
    private Context mContext;
    private OnRVClickListner listner;

    public HeaderAdapter(String headerName, String headerText, Context mContext) {
        this.headerName = headerName;
        this.headerText = headerText;
        this.mContext = mContext;
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
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setOnClick(OnRVClickListner onClick){
        listner = onClick;
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
