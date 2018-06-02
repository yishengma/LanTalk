package com.example.asus.lantalk.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.asus.lantalk.R;


/**
 * 图像的查看
 */

public class ImageDialogFragment extends DialogFragment {

    private String mPath;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.ImageDialogStyle);
        final LayoutInflater inflater = getActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.dialog_image, null);
        Glide.with(this).load(mPath).placeholder(R.drawable.iv_holder).into((ImageView) view.findViewById(R.id.iv_photo));
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

