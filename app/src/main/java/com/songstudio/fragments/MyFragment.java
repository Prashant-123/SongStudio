package com.songstudio.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.songstudio.BuildConfig;
import com.songstudio.R;

public class MyFragment extends Fragment {
    protected String TAG;
    private ProgressDialog mProgressDialog;
    private boolean DEBUG = BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug");

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void setTAG() {
        TAG = getFragmentManager().getClass().getName();
    }

    protected void showLogs(String msg) {
        if (DEBUG)
            Log.i(TAG, msg);
    }

    protected void showLogs(String TAG,String msg) {
        if (DEBUG)
            Log.i(TAG, msg);
    }

    protected void showProgress(String msg, Boolean cancellable) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();

        mProgressDialog = ProgressDialog.show(getContext(), getResources().getString(R.string.app_name), msg, false, cancellable);
    }

    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    protected void showsnackBar(String msg, int length) {
        View view = getActivity().findViewById(android.R.id.content);
        Snackbar.make(view, msg, length).show();
    }

}
