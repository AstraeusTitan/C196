package com.development.astraeus.c196;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Astraeus on 10/18/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private int callingField;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceSate){
        callingField = getActivity().getIntent().getIntExtra("callingField", 0);
        int day;
        int month;
        int year;
        if(!getActivity().getIntent().getBooleanExtra("new", false)){
            year = getActivity().getIntent().getIntExtra("year", 0);
            month = getActivity().getIntent().getIntExtra("month", 0);
            day = getActivity().getIntent().getIntExtra("day", 0);
        } else{
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(callingField != 0){
            TextView field = (TextView) getActivity().findViewById(callingField);
            field.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
        }
    }
}
