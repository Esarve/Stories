package com.sourav.story.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourav.story.Interfaces.OnRVClickListner;
import com.sourav.story.R;
import com.sourav.story.Stuffs.StoryData;

import java.util.List;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {
    private List<StoryData> data;
    private Context mContext;
    private OnRVClickListner listner;

    public NewAdapter(Context mContext, List<StoryData> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d("RV", "onBindViewHolder: size" + data.size());
        Log.d("RV", "onBindViewHolder: Called");
        Log.d("RV", "onBindViewHolder: Value of i " + i);
        StoryData story = data.get(i);
        viewHolder.title.setText(story.getBody());
        viewHolder.time.setText(story.getTime());
        viewHolder.date.setText(story.getDate());
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView date;
        LinearLayout parent;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listBody);
            time = itemView.findViewById(R.id.listTime);
            date = itemView.findViewById(R.id.listDate);
            parent = itemView.findViewById(R.id.parentLayout);
        }
    }

    public void setOnClick(OnRVClickListner onClick){
        listner = onClick;
    }
}
