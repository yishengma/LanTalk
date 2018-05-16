package com.example.asus.lantalk.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.asus.lantalk.R;


/**
 * Created by asus on 18-5-16.
 */

public class ImageDialogFragment extends DialogFragment {

    private String mPath;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_image, null);
        Glide.with(this).load(mPath).into((ImageView) view.findViewById(R.id.iv_photo));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        builder.setView(view);

        return builder.create();
    }

    public void setPath(String path) {
        mPath = path;
    }
}
