package com.sourav.stories.OtherKindsOfViews;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sourav.stories.Interfaces.OnBottomSheetClickListner;
import com.sourav.stories.R;
import com.sourav.stories.Stuffs.Tools;

public class BottomSheetViewer extends BottomSheetDialogFragment {
    private OnBottomSheetClickListner clickListener;
    private String time, date, body;
    private TextView tvHeader, tvBody;
    private Button btnEdit, btnDismiss;
    private int pos;
    private BottomSheetBehavior bottomSheetBehavior;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.popup_viewer, container, false);

        if (getArguments()!=null){
            time = getArguments().getString(Tools.TIME, "-");
            date =getArguments().getString(Tools.DATE, "-");
            body = getArguments().getString(Tools.BODY,"-");
            pos = getArguments().getInt(Tools.POSITION,0);
        }

        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        tvHeader = view.findViewById(R.id.viewTitle);
        tvBody = view.findViewById(R.id.viewBody);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDismiss = view.findViewById(R.id.btnDismiss);

        btnEdit.setOnClickListener(v -> {
            clickListener.onBottomSheetButtonClick(v, pos);
            dismiss();
        });

        btnDismiss.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void initData() {
        String header = date + " " +time;
        tvHeader.setText(header);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvBody.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvBody.setText(Html.fromHtml(body));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior =   BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            clickListener = (OnBottomSheetClickListner) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }
}
