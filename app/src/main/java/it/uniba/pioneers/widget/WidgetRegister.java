package it.uniba.pioneers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.uniba.pioneers.testtool.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetRegister extends LinearLayout {

    public void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        layoutInflater.inflate(R.layout.widget_register, this);
    }

    public WidgetRegister(@NonNull Context context) {
        super(context);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
    }

    public WidgetRegister(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
    }

    public WidgetRegister(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
    }

    public WidgetRegister(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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