package it.uniba.pioneers.widget;

import static android.widget.Toast.*;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.json.JSONException;

import it.uniba.pioneers.testtool.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetCuratoreRegister extends LinearLayout {

    public void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        layoutInflater.inflate(R.layout.widget_curatore_register, this);
    }

    public WidgetCuratoreRegister(@NonNull Context context) {
        super(context);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
    }

    public WidgetCuratoreRegister(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
    }

    public WidgetCuratoreRegister(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
    }

    public WidgetCuratoreRegister(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
    }

    public void setDayDate(Context context){
        Spinner spinner = (Spinner) this.findViewById(R.id.day_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.days_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setMounthDate(Context context){
        Spinner spinner = (Spinner) this.findViewById(R.id.mounth_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.mounth_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setYearsDate(Context context){
        Spinner spinner = (Spinner) this.findViewById(R.id.years_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.years_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }



}