package com.github.davidmihola.brandingexampleproject;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Created by dmta on 11.05.15.
 */
public class ToastClickListener implements View.OnClickListener {

    private final Context context;

    public ToastClickListener(final Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "This - is - evil!", Toast.LENGTH_SHORT).show();
    }
}
