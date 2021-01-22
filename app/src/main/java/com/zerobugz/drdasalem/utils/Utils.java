package com.zerobugz.drdasalem.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.zerobugz.drdasalem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Utils {

    public static ProgressDialog progressDialog;


    public static ArrayAdapter<String> arrayAdapter(Activity activity, List<String> list) {
        ArrayAdapter<String> arrayAdapters = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, list);
        arrayAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return arrayAdapters;
    }

    public static ArrayAdapter<CharSequence> arrayAdapter(Activity activity, int resId) {

        ArrayAdapter<CharSequence> arrayAdapters = ArrayAdapter.createFromResource(activity, resId, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return arrayAdapters;
    }

    public static void openActivity(Activity context, Class<?> activityClass, boolean isFinish) {
        Intent i = new Intent(context, activityClass);
        context.startActivity(i);
        if (isFinish)
            context.finish();
    }

    public static String dateformat(String date) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat istFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        try {
            Date utc = utcFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utc);
            calendar.add(Calendar.MINUTE, 330);
            String ist = istFormat.format(calendar.getTime());
            return ist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateutcformat(String date) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat utcFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat istFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        try {
            Date utc = utcFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utc);
            calendar.add(Calendar.MINUTE, 330);
            String ist = istFormat.format(calendar.getTime());
            return ist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void showProgress(Context context) {
        progressDialog = new ProgressDialog(context, R.style.ProgressStyle);
        //  progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    public static void hideProgress() {

        if (progressDialog != null) {
            if (progressDialog.isShowing()) { //check if dialog is showing.
                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) progressDialog.getContext()).getBaseContext();
                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                        progressDialog.dismiss();
                } else //if the Context used wasnt an Activity, then dismiss it too
                    progressDialog.dismiss();
            }
            progressDialog = null;
        }
    }
}
