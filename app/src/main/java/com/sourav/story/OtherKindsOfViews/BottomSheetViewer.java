package com.sourav.story.OtherKindsOfViews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sourav.story.Interfaces.OnBottomSheetClickListner;
import com.sourav.story.R;

public class BottomSheetViewer extends BottomSheetDialogFragment {
    public static final String HEADER = "header";
    public static final String  BODY = "body";
    private OnBottomSheetClickListner clickListener;
    private String header, body;
    private TextView tvHeader, tvBody;
    private Button btnEdit, btnDismiss;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_viewer, container, false);

        if (getArguments()!=null){
            header = getArguments().getString(HEADER, "-");
            body = getArguments().getString(BODY,"-");
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
            clickListener.onBottomSheetButtonClick(v);
            dismiss();
        });

        btnDismiss.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void initData() {
        tvBody.setText(body);
        tvHeader.setText(header);
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
